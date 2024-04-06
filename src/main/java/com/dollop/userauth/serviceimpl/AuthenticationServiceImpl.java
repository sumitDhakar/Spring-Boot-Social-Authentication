package com.dollop.userauth.serviceimpl;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dollop.userauth.constants.AppConstants;
import com.dollop.userauth.entity.AuthResponse;
import com.dollop.userauth.entity.User;
import com.dollop.userauth.entity.UserRegistration;
import com.dollop.userauth.entity.payload.SocialLoginRequest;
import com.dollop.userauth.entity.payload.UserCreateRequest;
import com.dollop.userauth.entityrequest.UserRequest;
import com.dollop.userauth.exception.AuthenticationFailedException;
import com.dollop.userauth.exception.PasswordNotMatchException;
import com.dollop.userauth.exception.ResourceAlreadyExists;
import com.dollop.userauth.exception.UserNotFoundException;
import com.dollop.userauth.fileservice.FileService;
import com.dollop.userauth.googleauthenticator.GoogleSignInValidator;
import com.dollop.userauth.repo.UserRegestrationRepository;
import com.dollop.userauth.repo.UserRepository;
import com.dollop.userauth.service.IAuthenticationService;
import com.dollop.userauth.service.IUserServices;
import com.dollop.userauth.utils.ChangePasswordUtils;
import com.dollop.userauth.utils.EmailUtils;
import com.dollop.userauth.utils.JWTUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import jakarta.mail.MessagingException;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {
	@Autowired
	@Lazy
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtils jwtUtils;

	@Autowired
	private UserRepository usersRepository;
	@Autowired
	private FileService fileService;
	@Autowired
	@Lazy
	private IUserServices usersService;

	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRegestrationRepository userRegistrationRepository;

	@Override
	public AuthResponse login(UserRequest authRequest, Boolean checkVerification) {

		User u = new User();
		this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		u = this.usersRepository.findByEmailAndDeleted(authRequest.getEmail(), false).orElseThrow(
				() -> new UserNotFoundException(AppConstants.USER_ALREADY_FOUND_EMAIL + authRequest.getEmail()));

		AuthResponse response = new AuthResponse();

		if (u.getTwoStepVerification() && checkVerification) {
			response.setMessage("Check Your Mail Box To get  Login ");
			this.requestToGetOtpForForgetPassword(u.getEmail());
			return response;
		}
		String token = jwtUtils.generateToken(authRequest.getEmail());
		System.out.println(token);
		response.setToken(token);
		response.setUser(u);
		response.setMessage("Login Succesfully!!");
		return response;
	}

	private String generateOtp() {
		// Generate a random 4-digit OTP
		SecureRandom secureRandom = new SecureRandom();
		int otp = secureRandom.nextInt(9000) + 1000;

		return String.valueOf(otp);
	}

	@Override
	public AuthResponse loginWithOtp(String otp, UserRequest request) {
		this.checkOtpforRegestrationAndLogin(request.getEmail(), otp);
		return this.login(request, false);
//		return "login Succesfully";
	}

	@Override
	public Boolean changeForgetPassword(UserRequest request, String otp) {
		User user = this.getCurrentUser(request.getEmail());
		if (user != null) {

			UserRegistration registeredUser = this.userRegistrationRepository
					.findByEmailAndDeletedAndOtpAndOtpUsed(user.getEmail(), false, otp, false)
					.orElseThrow(() -> new UserNotFoundException(
							AppConstants.USER_NOT_FOUND_EMAIL + user.getEmail() + " else otp is already used"));

			if (!registeredUser.getOtp().equals(otp))
				throw new AuthenticationFailedException(AppConstants.INVALID_OTP);
			else if (registeredUser.getOtp().equals(otp))
				if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
					throw new PasswordNotMatchException(AppConstants.OLDPASSWORD_AND_NEWPASSWORD_MATCHED);
				}
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			if (usersRepository.save(user) != null) {
				return true;

			}

		}
		return false;
	}

	@Override
	public User getCurrentUser(String email) {
		User user = this.usersRepository.findByEmailAndDeleted(email, false)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_EMAIL + email));
		return user;
	}

	@Override
	public boolean channgePassword(ChangePasswordUtils changePasswordUtils, String email) {
		User user = this.getCurrentUser(email);
		if (user != null) {
			if (passwordEncoder.matches(changePasswordUtils.getNewPassword(), user.getPassword())) {
				throw new PasswordNotMatchException(AppConstants.NEWPASS_AND_OLDPASSARESAME);
			}

			else if (passwordEncoder.matches(changePasswordUtils.getOldPassword(), user.getPassword())) {
				user.setPassword(passwordEncoder.encode(changePasswordUtils.getNewPassword()));
				this.usersRepository.save(user);
			} else {
				throw new PasswordNotMatchException(AppConstants.OLDPASSWORD_NOT_CORRECT);
			}
		}

		return true;

	}

	@Override
	public boolean requestToGetOtpForForgetPassword(String email) {
//	User u = this.getCurrentUser(email);
		UserRegistration u = this.checkUserEmailExistanceInRegistrationTable(email);
		u.setOtp(this.generateOtp());
		u.setOtpUsed(false);
		try {
			this.emailUtils.sendEmail(u.getEmail(), u.getFirstName() + " " + u.getLastName(), u.getOtp());
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.userRegistrationRepository.save(u);

		return true;
	}

	@Override
	public UserRegistration checkUserEmailExistanceInRegistrationTable(String email) {
		UserRegistration userRegistration = this.userRegistrationRepository.findByEmailAndDeleted(email, false)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_EMAIL + email));

		return userRegistration;
	}

	@Override
	public UserRegistration regirterUser(UserRegistration checkVerification) {

		Boolean existsByEmail = this.userRegistrationRepository.existsByEmailAndDeleted(checkVerification.getEmail(),
				false);

		if (existsByEmail) {
			throw new ResourceAlreadyExists(AppConstants.USER_ALREADY_FOUND_EMAIL + " " + checkVerification.getEmail());
		}
		if (checkVerification.getUserImage() != null) {
			
			String profileName = fileService.uploadFileInFolder(checkVerification.getUserImage(), "User");
			checkVerification.setProfilePic(profileName);
		}

		UserRegistration save = this.userRegistrationRepository.save(checkVerification);
		this.requestToGetOtpForForgetPassword(save.getEmail());
		return save;
	}

	@Override
	public UserRegistration verifyRegirterUser(String email, String otp) {
		UserRegistration ur = checkOtpforRegestrationAndLogin(email, otp);
		if (ur != null) {
			return ur;
		}
		return ur;
	}

	private UserRegistration checkOtpforRegestrationAndLogin(String email, String otp) {
		System.err.println(email + otp);
		UserRegistration u = this.userRegistrationRepository
				.findByEmailAndDeletedAndOtpAndOtpUsed(email, false, otp, false)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.OTP_ALREADY_USED));
		if (!u.getOtp().equals(otp)) {
			throw new AuthenticationFailedException(AppConstants.INVALID_OTP);
		}
		u.setOtpUsed(true);

		return this.userRegistrationRepository.save(u);
	}

	@Override
	public AuthResponse checkSocialLoginUser(SocialLoginRequest loginRequest) {
		Payload validateIdToken = GoogleSignInValidator.validateIdToken(loginRequest.getUserToken(),
				loginRequest.getClientId());
		if (validateIdToken.getEmail() == null) {
			throw new AuthenticationFailedException("Unable To Login With Google Try Again !!");
		}
		Optional<User> findByEmailAndDeleted = this.usersRepository.findByEmailAndDeleted(validateIdToken.getEmail(),
				false);
		User createUser = new User();
		if (findByEmailAndDeleted.isEmpty()) {

			UserCreateRequest request = new UserCreateRequest((String) validateIdToken.get("given_name"), (String) validateIdToken.get("family_name"), validateIdToken.getEmail(), (String) validateIdToken.get("picture"),
					validateIdToken.getEmail(), "USER");
			createUser = this.usersService.createUser(request);
		}
		return new AuthResponse(jwtUtils.generateToken(validateIdToken.getEmail()), findByEmailAndDeleted.isPresent()?findByEmailAndDeleted.get():createUser, "Login successfully");

	}

}

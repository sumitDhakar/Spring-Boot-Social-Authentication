package com.dollop.userauth.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.userauth.entity.AuthResponse;
import com.dollop.userauth.entity.User;
import com.dollop.userauth.entity.UserRegistration;
import com.dollop.userauth.entity.payload.SocialLoginRequest;
import com.dollop.userauth.entity.payload.UserCreateRequest;
import com.dollop.userauth.entityrequest.UserRequest;
import com.dollop.userauth.service.IAuthenticationService;
import com.dollop.userauth.service.IUserServices;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/auth/")
@CrossOrigin("*")
public class AuthController {

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private IUserServices userServices;

	@PostMapping("signUp")
	public ResponseEntity<?> createUser(@Valid UserRegistration userRegistration) {
//		System.err.println(userImage.getOriginalFilename());
		
		Map<String, UserRegistration> res = new HashMap<>();
		res.put("data", this.authenticationService.regirterUser(userRegistration));
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

	@PutMapping("verifyOtp")
	public ResponseEntity<?> verifiyRegisteredUser(String email, String otp) {
		UserRegistration ur = this.authenticationService.verifyRegirterUser(email, otp);
		UserCreateRequest u = new UserCreateRequest();
		u.setPassword(ur.getPassword());
		u.setFirstName(ur.getFirstName());
		u.setLastName(ur.getLastName());
		u.setEmail(ur.getEmail());
		u.setRoleName(ur.getRoleName());
		u.setProfilePic(ur.getProfilePic());
		this.userServices.createUser(u);
		Map<String, String> res = new HashMap<>();
		res.put("data", ur != null ? "you Have Registered Succesfully" : "There Is An Error In Registration");
		return ResponseEntity.status(HttpStatus.OK).body(res);
//		return new ResponseEntity<String>(
//				ur != null ? "you Have Registered Succesfully" : "There Is An Error In Registration", HttpStatus.OK);
	}

	@PostMapping("signIn")
	public ResponseEntity<?> login(UserRequest authRequest) {

		Map<String, AuthResponse> res = new HashMap<>();
		res.put("data", this.authenticationService.login(authRequest, true));
		return ResponseEntity.status(HttpStatus.OK).body(res);

//		return new ResponseEntity<AuthResponse>(this.authenticationService.login(authRequest, true), HttpStatus.OK);
	}

	@PostMapping("signInGoogle")
	public ResponseEntity<?> loginwithGoogle(SocialLoginRequest authRequest) {

		Map<String, AuthResponse> res = new HashMap<>();
		res.put("data", this.authenticationService.checkSocialLoginUser(authRequest));
		return ResponseEntity.status(HttpStatus.OK).body(res);

//		return new ResponseEntity<AuthResponse>(this.authenticationService.login(authRequest, true), HttpStatus.OK);
	}

	@PostMapping("withOtp")
	public ResponseEntity<?> login(UserRequest authRequest, String otp) {

		Map<String, AuthResponse> res = new HashMap<>();
		res.put("data", this.authenticationService.loginWithOtp(otp, authRequest));
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	@GetMapping("current-user")
	public ResponseEntity<?> getCurrentUser(Principal p) {
		Map<String, User> res = new HashMap<>();
		res.put("data", this.authenticationService.getCurrentUser(p.getName()));
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	@PutMapping("forget")
	public ResponseEntity<?> changeForgetPassword(UserRequest authRequest, String otp) {
		Map<String, String> res = new HashMap<>();
		res.put("data", this.authenticationService.changeForgetPassword(authRequest, otp) ? "Password Changed "
				: "Somethign Went Wrong ");
		return ResponseEntity.status(HttpStatus.OK).body(res);

	}

	@GetMapping("sendOtp")
	public ResponseEntity<?> sendOtpForForgetPassword(
			@Valid @NotBlank(message = "email must not be blank") String regirterEmail) {
		Map<String, String> res = new HashMap<>();
		res.put("data",
				this.authenticationService.requestToGetOtpForForgetPassword(regirterEmail)
						? "otp send Check Your Mail Box"
						: "Something Wen Wrong");
		return ResponseEntity.status(HttpStatus.OK).body(res);

	}

}

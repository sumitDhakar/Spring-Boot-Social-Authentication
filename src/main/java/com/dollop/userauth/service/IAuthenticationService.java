package com.dollop.userauth.service;

import com.dollop.userauth.entity.AuthResponse;
import com.dollop.userauth.entity.User;
import com.dollop.userauth.entity.UserRegistration;
import com.dollop.userauth.entity.payload.SocialLoginRequest;
import com.dollop.userauth.entityrequest.UserRequest;
import com.dollop.userauth.utils.ChangePasswordUtils;

public interface IAuthenticationService {

	public UserRegistration regirterUser(UserRegistration checkVerification);

	
	public UserRegistration verifyRegirterUser(String email,String otp);
	
	
	public AuthResponse login(UserRequest authRequest,Boolean checkVerification);

	public AuthResponse loginWithOtp(String otp, UserRequest request);
	
	

	public Boolean changeForgetPassword(UserRequest request, String otp);

	public User getCurrentUser(String email);

	public boolean channgePassword(ChangePasswordUtils changePasswordUtils, String email);
	
	public boolean requestToGetOtpForForgetPassword(String email);
	public AuthResponse checkSocialLoginUser(SocialLoginRequest loginRequest) ;
	
	public UserRegistration checkUserEmailExistanceInRegistrationTable(String email);

}

package com.dollop.userauth.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dollop.userauth.entity.User;
import com.dollop.userauth.entity.payload.UserUpdateRequest;
import com.dollop.userauth.service.IAuthenticationService;
import com.dollop.userauth.service.IUserServices;
import com.dollop.userauth.utils.ChangePasswordUtils;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController {

	@Autowired
	private IUserServices userServices;

	@Autowired
	private IAuthenticationService authenticationService;

//	@PutMapping(path = "", consumes = { "multipart/form-data", "application/octet-stream" })
	@PutMapping("update")
	public ResponseEntity<?> createUserByRoleName(@Valid UserUpdateRequest usersRequest) {
		Map<String, User> res = new HashMap<>();
		res.put("data", this.userServices.updateUser(usersRequest));
		return ResponseEntity.status(HttpStatus.CREATED).body(res);

	}

	@GetMapping("byId")
	public ResponseEntity<?> getUserById(Long userId) {
		Map<String, User> res = new HashMap<>();
		res.put("data", this.userServices.getUserById(userId));
		return ResponseEntity.status(HttpStatus.OK).body(res);

	}

	@GetMapping("allUser")
	public ResponseEntity<?> getAllUser() {
		Map<String, List<User>> res = new HashMap<>();
		res.put("data", this.userServices.getAllUser());
		return ResponseEntity.status(HttpStatus.OK).body(res);

	}

	@GetMapping("search")
	public ResponseEntity<?> getAllUserByField(User user) {
		Map<String, List<User>> res = new HashMap<>();
		res.put("data", this.userServices.findAllByField(user));
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	@GetMapping("changePassword")
	public ResponseEntity<?> changePassword(@NotBlank(message = "Old password must not be blank") String oldPassword,
			@NotBlank(message = "New password must not be blank") String newPassword, Principal principal) {
		Map<String, Boolean> res = new HashMap<>();
		res.put("data", this.authenticationService.channgePassword(new ChangePasswordUtils(oldPassword, newPassword),
				principal.getName()));
		return ResponseEntity.status(HttpStatus.OK).body(res);

	}

}

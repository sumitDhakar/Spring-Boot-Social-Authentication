package com.dollop.userauth.service;

import java.util.List;

import com.dollop.userauth.entity.User;
import com.dollop.userauth.entity.payload.UserCreateRequest;
import com.dollop.userauth.entity.payload.UserUpdateRequest;

public interface IUserServices {

	public User createUserCreateRequestToUser(UserCreateRequest createUser);

	public User createUser(UserCreateRequest createRequest);

	public User getUserById(Long userId);

	public List<User> getAllUser();

	public List<User> findAllByField(User user);

	public User updateUser(UserUpdateRequest u);

}

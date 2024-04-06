package com.dollop.userauth.serviceimpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dollop.userauth.constants.AppConstants;
import com.dollop.userauth.entity.Role;
import com.dollop.userauth.entity.User;
import com.dollop.userauth.entity.UserRoles;
import com.dollop.userauth.entity.payload.UserCreateRequest;
import com.dollop.userauth.entity.payload.UserUpdateRequest;
import com.dollop.userauth.exception.DataNotProvideException;
import com.dollop.userauth.exception.ResourceAlreadyExists;
import com.dollop.userauth.exception.UserNotFoundException;
import com.dollop.userauth.fileservice.FileService;
import com.dollop.userauth.repo.UserRepository;
import com.dollop.userauth.service.IAuthenticationService;
import com.dollop.userauth.service.IUserServices;

@Service
public class UserServiceImpl implements IUserServices, UserDetailsService {

	@Autowired
	private UserRepository usersRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private FileService fileService;

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public User createUserCreateRequestToUser(UserCreateRequest createUser) {
		return this.modelMapper.map(createUser, User.class);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = this.usersRepository.findByEmailAndDeleted(email, false)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_EMAIL + " " + email));
		if (user != null) {
			Set<UserRoles> userRoles = user.getUserRoles();

			List<SimpleGrantedAuthority> authorities = userRoles.stream()
					.map(r -> new SimpleGrantedAuthority(r.getRoles().getTitle())).collect(Collectors.toList());

			return new org.springframework.security.core.userdetails.User(email, user.getPassword(), authorities);
		}
		throw new UserNotFoundException(AppConstants.USER_NOT_FOUND_EMAIL + " " + email);
	}

	@Override
	public User createUser(UserCreateRequest createRequest) {

		User user = this.createUserCreateRequestToUser(createRequest);

		boolean isPresent = this.usersRepository.existsByEmailAndDeleted(user.getEmail(), false);
		if (isPresent) {
			throw new ResourceAlreadyExists(AppConstants.USER_ALREADY_FOUND_EMAIL + user.getEmail());
		}

		Set<UserRoles> userRoles = new HashSet<>();
		UserRoles userRole = new UserRoles();

		Role roles = new Role();
		if (createRequest.getRoleName() == null||createRequest.getRoleName()=="") {
			throw new DataNotProvideException(AppConstants.INCOMPLETE_DATA_PROVIDED_ROLE);
		}
		switch (createRequest.getRoleName()) {
		case "ADMIN":
			roles.setId(1l);
			break;
		case "USER":
			roles.setId(2l);
			break;
		case "EMPLOYEE":
			roles.setId(3l);
			break;
    default:
    	roles.setId(2l);
    	break;
		}
		userRole.setRoles(roles);
		userRoles.add(userRole);
		user.setUserRoles(userRoles);
		user.setUserRoles(userRoles);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setCreatedAt(Date.valueOf(LocalDate.now()));
    	User save = this.usersRepository.save(user);
		return save;
	}

	@Override
	public User getUserById(Long userId) {
		User user = this.usersRepository.findByIdAndDeleted(userId, false)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_ID + userId));
		return user;
	}

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		return this.usersRepository.findByDeleted(false);
	}

	@Override
	public List<User> findAllByField(User user) {
		user.setTwoStepVerification(null);
//		user.setOtpUsed(null);
		user.setDeleted(false);
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues() // ignoring null values of variable
				.withIgnoreCase() // ignoring case of letters
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // contains for string
				.withMatcher("id", match -> match.transform(value -> value.map(id -> ((Long) id == 0) ? null : id))) // for
				.withMatcher("userRoles.id",
						match -> match.transform(value -> value.map(id -> ((Long) id == 0) ? null : id))); // for

		Example<User> example = Example.of((user), matcher);

		List<User> findAll = this.usersRepository.findAll(example);
		return findAll;
	}

	@Override
	public User updateUser(UserUpdateRequest u) {
		User user = this.authenticationService.getCurrentUser(u.getEmail());
		user.setFirstName(u.getFirstName());
		user.setLastName(u.getLastName());

		if (u.getUserImage() != null) {
			String profileName = fileService.uploadFileInFolder(u.getUserImage(), "User");
			user.setProfilePic(profileName);
		}

		return this.usersRepository.save(user);
	}

}

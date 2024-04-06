package com.dollop.userauth.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.userauth.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByEmailAndDeleted(String email, boolean b);

//	public Optional<User> findByEmailAndDeletedAndOtpAndOtpUsed(String email, boolean b, String otp, boolean c);

	public boolean existsByEmailAndDeleted(String email, boolean b);

	public Optional<User> findByIdAndDeleted(Long userId, boolean b);

	public List<User> findByDeleted(boolean b);

}

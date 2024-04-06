package com.dollop.userauth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.userauth.entity.UserRegistration;

public interface UserRegestrationRepository extends JpaRepository<UserRegistration, Long> {

	public Optional<UserRegistration> findByEmailAndDeleted(String email, boolean deleted);

	public Optional<UserRegistration> findByEmailAndDeletedAndOtpAndOtpUsed(String email, boolean b, String otp, boolean c);

	public Boolean existsByEmailAndDeleted(String email, boolean b);

}

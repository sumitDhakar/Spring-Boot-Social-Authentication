package com.dollop.userauth.entity;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "First name must not be blank")
	private String firstName;

	@NotBlank(message = "Last name must not be blank")
	private String lastName;

	@NotBlank(message = "Email must not be blank")
	@Email(message = "Invalid email format")

	private String email;
	@NotBlank(message = "Password must not be blank")

	private String password;
	private String profilePic = "https://res.cloudinary.com/dizz5tuug/image/upload/v1706875603/DEFAULT_USER/guest-user_g42o3j.png";
	
	private String roleName="USER";
	private String otp;
	private Boolean otpUsed = false;
	private Boolean deleted = false;

	@Transient
	@JsonIgnore
	private MultipartFile userImage;

}

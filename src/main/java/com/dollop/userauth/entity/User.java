package com.dollop.userauth.entity;

import java.sql.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;

	private String lastName;
	private String email;

	private String password;
	private String profilePic = "https://res.cloudinary.com/dizz5tuug/image/upload/v1706875603/DEFAULT_USER/guest-user_g42o3j.png";

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "u_id")
	@JsonManagedReference
	@EqualsAndHashCode.Exclude
	private Set<UserRoles> userRoles;

//	private String otp;
//	private Boolean otpUsed = false;

	private Date createdAt;

	private Boolean deleted = false;
	private Boolean twoStepVerification = false;

	@PrePersist
	public void created() {
		this.createdAt = new Date(System.currentTimeMillis());
	}

}

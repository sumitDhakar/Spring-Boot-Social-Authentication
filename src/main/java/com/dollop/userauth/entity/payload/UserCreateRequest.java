package com.dollop.userauth.entity.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class UserCreateRequest {
    
    @NotBlank(message = "First name must not be blank")
    private String firstName;
    
    @NotBlank(message = "Last name must not be blank")
    private String lastName;

	@NotBlank(message = "Email must not be blank")
	@Email(message = "Invalid email format")

	private String email;
	private String profilePic = "https://res.cloudinary.com/dizz5tuug/image/upload/v1706875603/DEFAULT_USER/guest-user_g42o3j.png";

	@NotBlank(message = "Password must not be blank")

	private String password;
	 @NotBlank(message = "role name must not be blank")
	   
	private String roleName;

}

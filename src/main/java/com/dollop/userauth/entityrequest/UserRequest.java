package com.dollop.userauth.entityrequest;

import org.hibernate.annotations.Parent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {
    
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
  
	private String email;
    
    @NotBlank(message = "Password must not be blank")

	private String password;
	
}

package com.dollop.userauth.entity.payload;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserUpdateRequest {
    
    @NotBlank(message = "First name must not be blank")
    private String firstName;
    
    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    
	private String email;

	private MultipartFile userImage;

}

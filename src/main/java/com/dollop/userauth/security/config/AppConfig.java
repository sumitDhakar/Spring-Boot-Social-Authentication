package com.dollop.userauth.security.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cloudinary.Cloudinary;

@Configuration
public class AppConfig {
	@Value("${cloud.name}")
	private String cloudName;
	@Value("${cloud.api-key}")
	private String cloudApiKey;
	@Value("${cloud.secret-key}")
	private String apiSecretKey;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(String.format("cloudinary://%s:%s@%s", cloudApiKey, apiSecretKey, cloudName));

	}
}

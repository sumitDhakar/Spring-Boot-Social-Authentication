package com.dollop.userauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.dollop.userauth.entity.Role;
import com.dollop.userauth.repo.RoleRepository;

@SpringBootApplication
public class UserAuthenticationServiceApplication implements CommandLineRunner{

	@Autowired
	private  RoleRepository roleRepository;

	
	public static void main(String[] args) {
		SpringApplication.run(UserAuthenticationServiceApplication.class, args);
	}

	public   void createRole() {
		// TODO Auto-generated method stub
		Role role = new Role(1l, "ADMIN");
		Role role2 = new Role(2l, "EMPLOYEE");
		Role role3 = new Role(3l, "USER");
		

		if (!roleRepository.existsById(1l)) {
		   roleRepository.save(role);
		   roleRepository.save(role2);
		   roleRepository.save(role3);
		}

	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		this.createRole();
	}

}

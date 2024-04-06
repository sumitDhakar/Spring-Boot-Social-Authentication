package com.dollop.userauth.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.userauth.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}

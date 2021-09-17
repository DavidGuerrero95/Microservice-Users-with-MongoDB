package com.appcity.app.usuarios.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.appcity.app.usuarios.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

	@RestResource(path = "role")
	public Optional<Role> findByName(@Param("role") String name);
}

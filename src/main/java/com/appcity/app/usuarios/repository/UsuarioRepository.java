package com.appcity.app.usuarios.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.appcity.app.usuarios.models.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

	@RestResource(path = "exist-user")
	public Boolean existsByUsername(@Param("username") String username);

	@RestResource(path = "exist-email")
	public Boolean existsByEmail(@Param("email") String email);

	@RestResource(path = "exist-phone")
	public Boolean existsByPhone(@Param("phone") String phone);

	@RestResource(path = "findByPhone")
	public Usuario findByPhone(@Param("phone") String phone);

	@RestResource(path = "find-user")
	public Usuario findByUsername(@Param("username") String username);

	@RestResource(path = "exist-cedula")
	public Boolean existsByCedula(@Param("cedula") String cedula);

	@RestResource(path = "buscar")
	public Usuario findByUsernameOrEmailOrPhone(@Param("username") String username, @Param("username") String email,
			@Param("username") String phone);

}
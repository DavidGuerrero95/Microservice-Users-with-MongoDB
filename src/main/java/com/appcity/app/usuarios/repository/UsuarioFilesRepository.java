package com.appcity.app.usuarios.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.appcity.app.usuarios.models.UsuarioFiles;

public interface UsuarioFilesRepository extends MongoRepository<UsuarioFiles, String> {

	@RestResource(path = "buscarUsername")
	public UsuarioFiles findByUsername(@Param("username") String username);

	public UsuarioFiles findImageById(String id, Class<UsuarioFiles> class1);
	

}
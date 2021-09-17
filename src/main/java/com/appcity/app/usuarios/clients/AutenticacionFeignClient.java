package com.appcity.app.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.appcity.app.usuarios.models.Usuario;


@FeignClient(name = "app-autenticacion")
public interface AutenticacionFeignClient {
	
	@DeleteMapping("/autenticacion/eliminarUsuario/{username}")
	public void eliminarUsuario(@PathVariable String username);
	
	@PostMapping("/autenticacion/crearUsuario")
	public void crearUsuario(@RequestBody Usuario usuario);
	
	@PutMapping("/autenticacion/editarUsuario")
	public void editarUsuario(@RequestBody Usuario usuario);

}

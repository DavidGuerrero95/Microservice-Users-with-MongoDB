package com.appcity.app.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.appcity.app.usuarios.models.Usuario;

@FeignClient(name = "app-recomendacion")
public interface RecomendacionesFeignClient {

	@PostMapping("/recomendaciones/crear")
	public void crearRecomendacion(@RequestBody Usuario usuario);

	@DeleteMapping("/recomendaciones/eliminar/{nombre}")
	public void eliminarRecomendacion(@PathVariable String nombre);

	@PutMapping("/recomendaciones/editarUbicacion/{nombre}")
	public void editarUbicacion(@PathVariable String nombre, @RequestBody Usuario usuario);

}

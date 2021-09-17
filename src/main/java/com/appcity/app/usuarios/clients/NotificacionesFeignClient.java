package com.appcity.app.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "app-notificaciones")
public interface NotificacionesFeignClient {

	@PostMapping("/notificaciones/crear")
	public void crearNotificaciones(@RequestParam String nombre);

	@DeleteMapping("/notificaciones/eliminar")
	public void eliminarNotificacion(@RequestParam String nombre);

	@GetMapping("/notificaciones/editarUsuario/{nombre}")
	public void editarUsuario(@PathVariable String nombre, @RequestParam Integer codigo);

	@GetMapping("/notificaciones/verificarCodigoUsuario/{nombre}")
	public Integer verificarCodigoUsuario(@PathVariable String nombre);

	@PutMapping("/notificaciones/eliminarCodigoUsuario/{nombre}")
	public void eliminarCodigoUsuario(@PathVariable String nombre);
}

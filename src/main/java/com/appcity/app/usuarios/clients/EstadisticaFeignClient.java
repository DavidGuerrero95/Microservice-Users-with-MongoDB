package com.appcity.app.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "app-estadistica")
public interface EstadisticaFeignClient {

	@DeleteMapping("/estadistica/borrarEstadisticasUsuario/{nombre}")
	public void borrarEstadisticasUsuario(@PathVariable String nombre);

}

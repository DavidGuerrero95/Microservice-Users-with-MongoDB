package com.appcity.app.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "app-interventor")
public interface InterventorFeignClient {

	@PutMapping("/interventor/peticionEliminarUsuario")
	public void peticionEliminarUsuario(@RequestParam String username);
	
}

package com.appcity.app.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "app-registro")
public interface RegistroFeignClient {

	@GetMapping("/registro/contraseña")
	public String contraseña(@RequestParam String contraseña);
	
}

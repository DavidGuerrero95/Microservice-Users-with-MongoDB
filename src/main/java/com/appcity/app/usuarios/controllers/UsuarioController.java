package com.appcity.app.usuarios.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.appcity.app.usuarios.clients.AutenticacionFeignClient;
import com.appcity.app.usuarios.clients.EstadisticaFeignClient;
import com.appcity.app.usuarios.clients.InterventorFeignClient;
import com.appcity.app.usuarios.clients.NotificacionesFeignClient;
import com.appcity.app.usuarios.clients.RecomendacionesFeignClient;
import com.appcity.app.usuarios.clients.RegistroFeignClient;
import com.appcity.app.usuarios.models.Role;
import com.appcity.app.usuarios.models.Usuario;
import com.appcity.app.usuarios.models.UsuarioFiles;
import com.appcity.app.usuarios.repository.RoleRepository;
import com.appcity.app.usuarios.repository.UsuarioFilesRepository;
import com.appcity.app.usuarios.repository.UsuarioRepository;

import okhttp3.Response;

//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;

@RestController
public class UsuarioController {
	
	@Autowired
	private CircuitBreakerFactory cbFactory;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UsuarioRepository userRepository;

	@Autowired
	UsuarioFilesRepository userFilesRepository;

	@Autowired
	RegistroFeignClient registroFeign;

	@Autowired
	RecomendacionesFeignClient recomendaciones;

	@Autowired
	NotificacionesFeignClient notificaciones;

	@Autowired
	InterventorFeignClient interventor;
	
	@Autowired
	EstadisticaFeignClient estadistica;
	
	@Autowired
	AutenticacionFeignClient autenticacionClient;

	@GetMapping("/roles/lista")
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}

	@GetMapping("/users/listar")
	public List<Usuario> getUsers() {
		return userRepository.findAll();
	}

	@PutMapping("/users/eliminarAdmin/{user}")
	@ResponseStatus(code = HttpStatus.OK)
	public void eliminarAdmin(@PathVariable String username) {
		if (userRepository.existsByUsername(username)) {
			interventor.peticionEliminarUsuario(username);
		}
	}

	@DeleteMapping("/users/eliminar/{user}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public ResponseEntity<?> eliminarId(@PathVariable String user) {
		try {
			Usuario p = userRepository.findByUsernameOrEmailOrPhone(user, user, user);
			String id = p.getId();
			UsuarioFiles files = userFilesRepository.findByUsername(p.getUsername());
			String idFiles = files.getId();
			userRepository.deleteById(id);
			recomendaciones.eliminarRecomendacion(p.getUsername());
			notificaciones.eliminarNotificacion(p.getUsername());
			userFilesRepository.deleteById(idFiles);
			estadistica.borrarEstadisticasUsuario(p.getUsername());
			autenticacionClient.eliminarUsuario(p.getUsername());
			return ResponseEntity.ok("Usuario eliminado correctamente");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error en la eliminacion");
		}

	}

	@PostMapping("/users/crear")
	public String crearUsuario(@RequestBody Usuario user) {
		try {
			UsuarioFiles foto = userFilesRepository.findByUsername("admin");
			UsuarioFiles usuariosFiles = new UsuarioFiles();
			usuariosFiles.setUsername(user.getUsername());
			usuariosFiles.setName(foto.getName());
			usuariosFiles.setCreatedtime(new Date());
			usuariosFiles.setContent(foto.getContent());
			usuariosFiles.setContenttype(foto.getContenttype());
			usuariosFiles.setSize(foto.getSize());
			userFilesRepository.save(usuariosFiles);
			userRepository.save(user);
			recomendaciones.crearRecomendacion(user);
			notificaciones.crearNotificaciones(user.getUsername());
			autenticacionClient.crearUsuario(user);
			return "Usuario: " + user.getPhone() + " Creado exitosamente";
		} catch (Exception e) {
			return "Error en la creacion";
		}

	}

	@PostMapping("/users/crearUsuarios")
	public ResponseEntity<?> crearUsuarios(@RequestBody Usuario usuario) {
		Usuario user = usuario;
		user.setPassword(registroFeign.contraseña(user.getPassword()));
		List<Double> latLon = new ArrayList<Double>();
		BigDecimal bdlat = new BigDecimal(usuario.getUbicacion().get(0)).setScale(5, RoundingMode.HALF_UP);
		BigDecimal bdlon = new BigDecimal(usuario.getUbicacion().get(1)).setScale(6, RoundingMode.HALF_UP);
		latLon.add(bdlat.doubleValue());
		latLon.add(bdlon.doubleValue());
		user.setUbicacion(latLon);
		UsuarioFiles foto = userFilesRepository.findByUsername("admin");
		UsuarioFiles usuariosFiles = new UsuarioFiles();
		usuariosFiles.setUsername(user.getUsername());
		usuariosFiles.setName(foto.getName());
		usuariosFiles.setCreatedtime(new Date());
		usuariosFiles.setContent(foto.getContent());
		usuariosFiles.setContenttype(foto.getContenttype());
		usuariosFiles.setSize(foto.getSize());
		userFilesRepository.save(usuariosFiles);
		userRepository.save(user);
		recomendaciones.crearRecomendacion(user);
		notificaciones.crearNotificaciones(user.getUsername());
		autenticacionClient.crearUsuario(user);
		return ResponseEntity.ok("Registro Exitoso");
	}

	@PutMapping("/users/editarUbicacion/{user}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> editarUbicacion(@PathVariable String user, @RequestBody Usuario usuario) {
		try {
			Usuario usuarioDb = userRepository.findByUsernameOrEmailOrPhone(user, user, user);
			List<Double> latLon = new ArrayList<Double>();
			BigDecimal bdlat = new BigDecimal(usuario.getUbicacion().get(0)).setScale(5, RoundingMode.HALF_UP);
			BigDecimal bdlon = new BigDecimal(usuario.getUbicacion().get(1)).setScale(5, RoundingMode.HALF_UP);
			latLon.add(bdlat.doubleValue());
			latLon.add(bdlon.doubleValue());
			usuarioDb.setUbicacion(latLon);
			userRepository.save(usuarioDb);
			recomendaciones.editarUbicacion(user, usuarioDb);
			autenticacionClient.editarUsuario(usuarioDb);
			return ResponseEntity.ok("Ubicacion actulizada");
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body("Error en la edicio:" + e.getMessage() + "E -->" + e.getLocalizedMessage());
		}
	}

	@PostMapping("/users/crearUsuariosRegistro")
	public void crearUsuariosRegistro(@RequestBody Usuario usuario) {
		Integer codigo = (int) (100000 * Math.random());
		List<Double> listaUbicacion = new ArrayList<Double>();
		listaUbicacion.add(6.2678);
		listaUbicacion.add(-75.594037);
		while (userRepository.existsByCedula(codigo.toString())) {
			codigo = (int) (1000000 * Math.random());
		}
		Usuario usuariCrear = usuario;
		usuariCrear.setNombre("");
		usuariCrear.setApellido("");
		usuariCrear.setCedula(codigo);
		usuariCrear.setUbicacion(listaUbicacion);
		usuariCrear.setIntereses(new ArrayList<String>());
		usuariCrear.setIntentos(0);
		usuariCrear.setEnabled(true);

		List<Role> roles = new ArrayList<Role>();

		if (usuario.getIntereses().isEmpty()) {
			Role userRole = new Role("3", "ROLE_USER");
			// userRole.setName("ROLE_USER");
			// userRole.setId("3");

			roles.add(userRole);
		} else {
			usuario.getIntereses().forEach(role -> {
				switch (role) {
				case "admin":
					Role userRole = new Role("1", "ROLE_ADMIN");
					// userRole.setName("ROLE_ADMIN");
					// userRole.setId("1");
					roles.add(userRole);
					break;
				case "mod":
					Role userRole1 = new Role("2", "ROLE_MODERATOR");
					// userRole1.setName("ROLE_MODERATOR");
					roles.add(userRole1);
					break;
				default:
					Role userRole11 = new Role("3", "ROLE_USER");
					// userRole11.setName("ROLE_USER");
					roles.add(userRole11);
				}
			});

		}
		usuariCrear.setRoles(roles);
		UsuarioFiles foto = userFilesRepository.findByUsername("admin");
		UsuarioFiles usuariosFiles = new UsuarioFiles();
		usuariosFiles.setUsername(usuario.getUsername());
		usuariosFiles.setName(foto.getName());
		usuariosFiles.setCreatedtime(new Date());
		usuariosFiles.setContent(foto.getContent());
		usuariosFiles.setContenttype(foto.getContenttype());
		usuariosFiles.setSize(foto.getSize());
		userFilesRepository.save(usuariosFiles);
		userRepository.save(usuariCrear);
		recomendaciones.crearRecomendacion(usuariCrear);
		notificaciones.crearNotificaciones(usuariCrear.getUsername());
		autenticacionClient.crearUsuario(usuariCrear);
	}

	@PutMapping("/users/editar/{user}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> editar(@RequestBody Usuario usuario, @PathVariable String user) {
		try {
			Usuario usuarioDb = userRepository.findByUsernameOrEmailOrPhone(user, user, user);
			if (usuario.getNombre() != null)
				usuarioDb.setNombre(usuario.getNombre());
			if (usuario.getApellido() != null)
				usuarioDb.setApellido(usuario.getApellido());
			if (usuario.getIntereses() != null)
				usuarioDb.setIntereses(usuario.getIntereses());
			if (usuario.getUbicacion() != null) {
				List<Double> latLon = new ArrayList<Double>();
				BigDecimal bdlat = new BigDecimal(usuario.getUbicacion().get(0)).setScale(5, RoundingMode.HALF_UP);
				BigDecimal bdlon = new BigDecimal(usuario.getUbicacion().get(1)).setScale(6, RoundingMode.HALF_UP);
				latLon.add(bdlat.doubleValue());
				latLon.add(bdlon.doubleValue());
				usuarioDb.setUbicacion(latLon);
				recomendaciones.editarUbicacion(user, usuarioDb);
			}
			if (usuario.getEmail() != null)
				usuarioDb.setEmail(usuario.getEmail());
			if (usuario.getUsername() != null)
				usuarioDb.setUsername(usuario.getUsername());
			userRepository.save(usuarioDb);
			autenticacionClient.editarUsuario(usuarioDb);
			return ResponseEntity.ok("Edicion Exitosa");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error en la edicion");
		}

	}

	@GetMapping("/users/editarPerfil/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public void editarPerfil(@PathVariable String username) {
		Integer codigo = (int) (100000 * Math.random() + 99999);
		notificaciones.editarUsuario(username, codigo);
	}

	@GetMapping("/users/verificarCodigo/{user}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean verificarCodigo(@PathVariable String username, @RequestParam(value = "codigo") Integer codigo) {

		Integer codigoBase = notificaciones.verificarCodigoUsuario(username);
		if (codigoBase == codigo) {
			notificaciones.eliminarCodigoUsuario(username);
			return true;
		} else {
			return false;
		}

	}

	@PutMapping("/users/roleModerator/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public String roleModerator(@PathVariable String username) {
		if (userRepository.existsByUsername(username)) {
			Usuario usuario = userRepository.findByUsername(username);
			Role userRole1 = new Role("2", "ROLE_MODERATOR");
			List<Role> roles = usuario.getRoles();
			if (!roles.contains(userRole1)) {
				roles.add(userRole1);
				usuario.setRoles(roles);
				userRepository.save(usuario);
				autenticacionClient.editarUsuario(usuario);
				return "Role Moderator";
			} else {
				return "Usuario ya tiene Role Moderator";
			}
		} else {
			return "Usuario no existe";
		}
	}

	@GetMapping("/users/verUsuario/{user}")
	@ResponseStatus(code = HttpStatus.OK)
	public String verUsuario(@RequestBody String user) {
		try {
			Usuario usuario = userRepository.findByUsernameOrEmailOrPhone(user, user, user);
			return usuario.getUsername();
		} catch (Exception e) {
			return "Usuario no encontrado" + e.getMessage();
		}
	}

	@GetMapping("/users/encontrarUsuario/{usuario}")
	public Usuario encontrarUsuario(@PathVariable String usuario) {
		return userRepository.findByUsernameOrEmailOrPhone(usuario, usuario, usuario);
	}

	@GetMapping("/users/buscar")
	public Usuario findByUsernameOrEmailOrPhone(@PathVariable String username, @PathVariable String email,
			@PathVariable String phone) {
		return userRepository.findByUsernameOrEmailOrPhone(username, email, phone);
	}

	@GetMapping("/users/existUsername")
	public Boolean existsByUsername(@RequestParam String username) {
		return userRepository.existsByUsername(username);
	}

	@GetMapping("/users/email")
	public Boolean existsByEmail(@RequestParam String email) {
		return userRepository.existsByEmail(email);
	}

	@GetMapping("/users/phone")
	public Boolean existsByPhone(@RequestParam String phone) {
		return userRepository.existsByPhone(phone);
	}

	@GetMapping("/users/findUsername")
	public Usuario findUsername(@RequestParam String username) {
		return userRepository.findByUsername(username);
	}

	@GetMapping("/users/cerrarSesion")
	public String cerrarSesion() {
		String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjUzNDYyNTAsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfTU9ERVJBVE9SIiwiUk9MRV9VU0VSIl0sImp0aSI6IjkwMDdkZGNlLTZkNjktNDAzZi04MDgyLWMwMTdiNTc1MDI0ZCIsImNsaWVudF9pZCI6ImFwcGNpdHkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.-ACrNC_Q5ctvVv7r4l8YjPs1iQToE_kXNfSlHUfoE6g";
		return token;
	}

	@PutMapping("/users/arreglarUsuario")
	public void arreglarUsuario(@RequestParam(value = "image") MultipartFile file) {
		List<Usuario> lista = userRepository.findAll();
		List<UsuarioFiles> listaPhotos = userFilesRepository.findAll();
		for (int i = 0; i < lista.size(); i++) {
			lista.get(i).setGenero(null);
			lista.get(i).setCabezaFamilia(null);
			lista.get(i).setTelefono(null);
			lista.get(i).setStakeholders(null);
			lista.get(i).setEdad(null);
			userRepository.save(lista.get(i));
		}
		for (int i = 0; i < listaPhotos.size(); i++) {

			uploadImage(listaPhotos.get(i).getUsername(), file);

		}
	}

	@PutMapping("/users/file/uploadImage/{username}")
	public String uploadImage(@PathVariable String username, @RequestParam(value = "image") MultipartFile file) {

		String fileName = file.getOriginalFilename();
		if (userRepository.existsByUsername(username)) {
			try {
				UsuarioFiles uploadFile = userFilesRepository.findByUsername(username);
				uploadFile.setName(fileName);
				uploadFile.setCreatedtime(new Date());
				uploadFile.setContent(new Binary(file.getBytes()));
				uploadFile.setContenttype(file.getContentType());
				uploadFile.setSize(file.getSize());

				userFilesRepository.save(uploadFile);
				return "Imagen añadida";
			} catch (IOException e) {
				e.printStackTrace();
				return e.getLocalizedMessage() + e.getMessage();
			}
		} else {
			return "Usuario No existe";
		}

	}

	@GetMapping(value = "/users/file/downloadImage/{username}", produces = { MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE })
	public byte[] image(@PathVariable String username) {
		UsuarioFiles usuario = userFilesRepository.findByUsername(username);
		byte[] data = null;
		UsuarioFiles file = userFilesRepository.findImageById(usuario.getId(), UsuarioFiles.class);
		if (file != null) {
			data = file.getContent().getData();
		}
		return data;
	}

	@GetMapping("/users/verRoleUsuario/{user}")
	public List<String> verRoleUsuario(@PathVariable String user) {
		Usuario usuario = userRepository.findByUsername(user);
		List<Role> roles = usuario.getRoles();
		List<String> rolesList = new ArrayList<String>();
		for (int i = 0; i < roles.size(); i++) {
			rolesList.add(roles.get(i).getName());
		}
		return rolesList;
	}

	@GetMapping("/users/cedula")
	public Response cedula() throws IOException {
		okhttp3.OkHttpClient client = new okhttp3.OkHttpClient().newBuilder().build();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		String payload = "{\"date_expedition\": \"2013-09-04\", \"document_number\": \"1085319765\"}";
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, payload);
		okhttp3.Request request = new okhttp3.Request.Builder()
				.url("https://apitude.co/api/v1.0/requests/registraduria-co/").method("POST", body)
				.addHeader("x-api-key", "API-KEY").addHeader("Content-Type", "application/json").build();
		okhttp3.Response response = client.newCall(request).execute();

		return response;
	}

}

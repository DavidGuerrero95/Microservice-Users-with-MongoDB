package com.appcity.app.usuarios.models;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class Usuario {

	@Id
	private String id;

	@NotBlank(message = "Username cannot be null")
	@Size(max = 20)
	@Indexed(unique = true)
	private String username;

	@NotBlank(message = "Phone cannot be null")
	@Size(max = 50)
	@Indexed(unique = true)
	private String phone;

	@NotBlank(message = "Email cannot be null")
	@Size(max = 50)
	@Email(message = "Email should be valid")
	@Indexed(unique = true)
	private String email;

	@NotBlank(message = "Password cannot be null")
	@Size(min = 6, max = 20, message = "About Me must be between 6 and 20 characters")
	private String password;

	private Boolean enabled;

	private String nombre;

	private String apellido;

	@Size(max = 15)
	@Indexed(unique = true)
	private Integer cedula;

	private List<Double> ubicacion;
	private List<String> intereses;
	private Integer intentos;

	private String actividadEconomica;
	private List<String> datosEconomica;

	// @DBRef
	private List<Role> roles;

	private String genero;
	private Boolean cabezaFamilia;
	private String telefono;
	private String stakeholders;
	private Integer edad;

	public Usuario() {

	}

	public Usuario(String username, String phone, String email, String password, Boolean enabled, String nombre,
			String apellido, Integer cedula, List<Double> ubicacion, List<String> intereses, Integer intentos,
			String actividadEconomica, List<String> datosEconomica, List<Role> roles, String genero,
			Boolean cabezaFamilia, String telefono, String stakeholders, Integer edad) {
		super();
		this.username = username;
		this.phone = phone;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.nombre = nombre;
		this.apellido = apellido;
		this.cedula = cedula;
		this.ubicacion = ubicacion;
		this.intereses = intereses;
		this.intentos = intentos;
		this.actividadEconomica = actividadEconomica;
		this.datosEconomica = datosEconomica;
		this.roles = roles;
		this.genero = genero;
		this.cabezaFamilia = cabezaFamilia;
		this.telefono = telefono;
		this.stakeholders = stakeholders;
		this.edad = edad;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Integer getCedula() {
		return cedula;
	}

	public void setCedula(Integer cedula) {
		this.cedula = cedula;
	}

	public List<Double> getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(List<Double> ubicacion) {
		this.ubicacion = ubicacion;
	}

	public List<String> getIntereses() {
		return intereses;
	}

	public void setIntereses(List<String> intereses) {
		this.intereses = intereses;
	}

	public Integer getIntentos() {
		return intentos;
	}

	public void setIntentos(Integer intentos) {
		this.intentos = intentos;
	}

	public String getActividadEconomica() {
		return actividadEconomica;
	}

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	public List<String> getDatosEconomica() {
		return datosEconomica;
	}

	public void setDatosEconomica(List<String> datosEconomica) {
		this.datosEconomica = datosEconomica;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Boolean getCabezaFamilia() {
		return cabezaFamilia;
	}

	public void setCabezaFamilia(Boolean cabezaFamilia) {
		this.cabezaFamilia = cabezaFamilia;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getStakeholders() {
		return stakeholders;
	}

	public void setStakeholders(String stakeholders) {
		this.stakeholders = stakeholders;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

}

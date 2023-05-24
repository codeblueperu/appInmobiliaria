package mac.inmobiliaria.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.*;

@Entity
@Table(name = "usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "usuario_id")
	private Integer id;
	
	@NotBlank(message = "Sus nombres son obligatorio.")
	@Column(name = "nombres", length = 45, nullable = false)
	private String nombres;

	@NotBlank (message = "Sus apellidos son obligatorio.")
	@Column(name = "apellidos", length = 45, nullable = false)
	private String apellidos;

	@Column(name = "nom_completo", length = 45, nullable = false)
	private String nombreCompleto;

	@NotBlank(message = "El telefono es obligatorio.")
	@Size(min = 9, max = 9, message = "El telefono debe contener 9 digitos.")
	@Pattern(regexp = "^[1-9][0-9]*$", message = "Solo se aceptan numeros")
	@Column(name = "telefono", length = 45, nullable = false)
	private String telefono;

	@NotBlank(message = "Es obligatorio seleccionar una opcion.")
	@Column(name = "sexo", length = 45, nullable = false)
	private String sexo;

	@NotBlank
	@Email
	@Column(name = "email", length = 45, nullable = false)
	private String email;

	@Column(name = "password", length = 250, nullable = false)
	private String password;

	@Column(nullable = true, columnDefinition = "int default 0 ")
	private int codeVerifyrecoverPassword;
	
	@Column(name = "preinstall", nullable = false, columnDefinition = "INT default 0")
	private int preinstall;

	@Enumerated(EnumType.STRING)
	@Column(name = "rol", nullable = false,columnDefinition = "ENUM('ADMIN', 'VENDEDOR')")
	private Rol rol;

	// @NotBlank
	@Transient
	private String password1;

	// @NotBlank
	@Transient
	private String password2;

	public enum Rol {
		ADMIN, VENDEDOR
	}

	@OneToOne
	@JoinColumn(name = "distrito_id")
	private Distrito distrito;
	
	@Column(name = "session_login", nullable = false, columnDefinition = "int default 0")
	private int sessionLogin;

	@PrePersist
	private void asignarFechaCreacion() {
		nombreCompleto = nombres + " " + apellidos;
	}

	@PreUpdate
	private void asignarFechaActualizacion() {
		nombreCompleto = nombres + " " + apellidos;
	}
	
	
	
	public Usuario(Integer id) {
		super();
		this.id = id;
	}

	public Usuario() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Usuario(Integer id, @NotBlank(message = "Sus nombres son obligatorio.") String nombres,
			@NotBlank(message = "Sus apellidos son obligatorio.") String apellidos, String nombreCompleto,
			@NotBlank(message = "El telefono es obligatorio.") @Size(min = 0, max = 9, message = "El telefono debe contener 9 digitos.") String telefono,
			@NotBlank(message = "Es obligatorio seleccionar una opcion.") String sexo, @NotBlank @Email String email,
			String password, int codeVerifyrecoverPassword, int preinstall, Rol rol, String password1, String password2,
			Distrito distrito, int sessionLogin) {
		super();
		this.id = id;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.nombreCompleto = nombreCompleto;
		this.telefono = telefono;
		this.sexo = sexo;
		this.email = email;
		this.password = password;
		this.codeVerifyrecoverPassword = codeVerifyrecoverPassword;
		this.preinstall = preinstall;
		this.rol = rol;
		this.password1 = password1;
		this.password2 = password2;
		this.distrito = distrito;
		this.sessionLogin = sessionLogin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
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

	public int getCodeVerifyrecoverPassword() {
		return codeVerifyrecoverPassword;
	}

	public void setCodeVerifyrecoverPassword(int codeVerifyrecoverPassword) {
		this.codeVerifyrecoverPassword = codeVerifyrecoverPassword;
	}

	public int getPreinstall() {
		return preinstall;
	}

	public void setPreinstall(int preinstall) {
		this.preinstall = preinstall;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public Distrito getDistrito() {
		return distrito;
	}

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}

	public int getSessionLogin() {
		return sessionLogin;
	}

	public void setSessionLogin(int sessionLogin) {
		this.sessionLogin = sessionLogin;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombres=" + nombres + ", apellidos=" + apellidos + ", nombreCompleto="
				+ nombreCompleto + ", telefono=" + telefono + ", sexo=" + sexo + ", email=" + email + ", password="
				+ password + ", codeVerifyrecoverPassword=" + codeVerifyrecoverPassword + ", preinstall=" + preinstall
				+ ", rol=" + rol + ", password1=" + password1 + ", password2=" + password2 + ", distrito=" + distrito
				+ ", sessionLogin=" + sessionLogin + "]";
	}

	
	
}

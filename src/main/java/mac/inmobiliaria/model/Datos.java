package mac.inmobiliaria.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "datos")
public class Datos {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "datos_id")
    private Integer id;


    @Column(name = "nombres",length = 45, nullable = false)
    private String nombres;

    @NotBlank(message = "El telefono es obligatorio.")
	@Size(min = 0, max = 9, message = "El telefono debe contener 9 digitos.")
	@Column(name = "telefono", length = 45, nullable = false)
	private String telefono;

    @Column(name = "dni",length = 45, nullable = false)
    private String dni;

    @Column(name = "correo",length = 50, nullable = false)
    private String email;

    @Column(name = "estado_civil",length = 60, nullable = true)
    private String estadoCivil;

    @Column(name = "lugar_laboral",length = 80, nullable = true)
    private String lugarLaboral;

    @Column(name = "nombres_conyu",length = 255, nullable = true)
    private String nombresConyu;

    @Column(name = "dni_conyu",length = 10, nullable = true)
    private String dniConyu;
    
    @Column(name = "estado", nullable = false, columnDefinition = "int default 1")
    private int estado;
    
    @Column(name = "id_usuario", nullable = true)
    private int  idUsuario;

	public Datos() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Datos(Integer id, String nombres,
			@NotBlank(message = "El telefono es obligatorio.") @Size(min = 0, max = 9, message = "El telefono debe contener 9 digitos.") String telefono,
			String dni, String email, String estadoCivil, String lugarLaboral, String nombresConyu, String dniConyu,
			int estado, int idUsuario) {
		super();
		this.id = id;
		this.nombres = nombres;
		this.telefono = telefono;
		this.dni = dni;
		this.email = email;
		this.estadoCivil = estadoCivil;
		this.lugarLaboral = lugarLaboral;
		this.nombresConyu = nombresConyu;
		this.dniConyu = dniConyu;
		this.estado = estado;
		this.idUsuario = idUsuario;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getLugarLaboral() {
		return lugarLaboral;
	}

	public void setLugarLaboral(String lugarLaboral) {
		this.lugarLaboral = lugarLaboral;
	}

	public String getNombresConyu() {
		return nombresConyu;
	}

	public void setNombresConyu(String nombresConyu) {
		this.nombresConyu = nombresConyu;
	}

	public String getDniConyu() {
		return dniConyu;
	}

	public void setDniConyu(String dniConyu) {
		this.dniConyu = dniConyu;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	@Override
	public String toString() {
		return "Datos [id=" + id + ", nombres=" + nombres + ", telefono=" + telefono + ", dni=" + dni + ", email="
				+ email + ", estadoCivil=" + estadoCivil + ", lugarLaboral=" + lugarLaboral + ", nombresConyu="
				+ nombresConyu + ", dniConyu=" + dniConyu + ", estado=" + estado + ", idUsuario=" + idUsuario + "]";
	}
	
}

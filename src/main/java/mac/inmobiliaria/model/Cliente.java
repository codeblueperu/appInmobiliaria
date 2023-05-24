package mac.inmobiliaria.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "cliente")
public class Cliente {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Integer id;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "dni", length = 50, nullable = false)
    private String dni;

    @Column(name = "correo", length = 50, nullable = false)
    private String correo;

    @Column(name = "telefono_movil", length = 50, nullable = false)
    private String telefono;

    @Column(name = "tipo_cliente", length = 50, nullable = false)
    private String tipo;

    @Column(name = "proyecto", length = 100, nullable = false)
    private String proyecto;

    @Column(name = "fecha_conta", nullable = false)
    private Date creadate;

    @Column(name = "direccion", length = 100, nullable = false)
    private String direccion;
    
    @Column(name = "estado", nullable = false, columnDefinition = "int default 1")
    private int estado;

    @ManyToOne
    @JoinColumn(name = "ubigeo_id", referencedColumnName = "ubigeo_id")
    private Ubigeo ubigeo;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
    private Usuario usuario;
    
    
    @OneToOne
	@JoinColumn(name = "distrito_id")
	private Distrito distrito;

	public Cliente() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	


	public Cliente(Integer id, String nombre, String dni, String correo, String telefono, String tipo, String proyecto,
			Date creadate, String direccion, int estado, Ubigeo ubigeo, Usuario usuario, Distrito distrito) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.dni = dni;
		this.correo = correo;
		this.telefono = telefono;
		this.tipo = tipo;
		this.proyecto = proyecto;
		this.creadate = creadate;
		this.direccion = direccion;
		this.estado = estado;
		this.ubigeo = ubigeo;
		this.usuario = usuario;
		this.distrito = distrito;
	}




	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getDni() {
		return dni;
	}



	public void setDni(String dni) {
		this.dni = dni;
	}



	public String getCorreo() {
		return correo;
	}



	public void setCorreo(String correo) {
		this.correo = correo;
	}



	public String getTelefono() {
		return telefono;
	}



	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}



	public String getTipo() {
		return tipo;
	}



	public void setTipo(String tipo) {
		this.tipo = tipo;
	}



	public String getProyecto() {
		return proyecto;
	}



	public void setProyecto(String proyecto) {
		this.proyecto = proyecto;
	}



	public Date getCreadate() {
		return creadate;
	}



	public void setCreadate(Date creadate) {
		this.creadate = creadate;
	}



	public String getDireccion() {
		return direccion;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}



	public int getEstado() {
		return estado;
	}



	public void setEstado(int estado) {
		this.estado = estado;
	}



	public Ubigeo getUbigeo() {
		return ubigeo;
	}



	public void setUbigeo(Ubigeo ubigeo) {
		this.ubigeo = ubigeo;
	}



	public Usuario getUsuario() {
		return usuario;
	}



	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	


	



	public Distrito getDistrito() {
		return distrito;
	}


	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}




	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", dni=" + dni + ", correo=" + correo + ", telefono="
				+ telefono + ", tipo=" + tipo + ", proyecto=" + proyecto + ", creadate=" + creadate + ", direccion="
				+ direccion + ", estado=" + estado + ", ubigeo=" + ubigeo + ", usuario=" + usuario + ", distrito="
				+ distrito + "]";
	}




	


	

	
}

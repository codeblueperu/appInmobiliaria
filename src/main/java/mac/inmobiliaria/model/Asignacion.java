package mac.inmobiliaria.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


@Entity
@Table(name = "asignacion")
public class Asignacion {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asignacion_id")
    private Integer id;

    @Column(name = "fecha_Inicio", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime creDate;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id")
    private Cliente cliente;

    @Column(name = "descripcion", length = 45, nullable = false)
    private String descripcion;

    @Column(name = "fecha_Actualizada", nullable = true, columnDefinition = "DATETIME  DEFAULT NULL")
    private LocalDateTime datecreta;

    @Column(name = "observaciones", length = 45, nullable = true, columnDefinition = "VARCHAR(45) DEFAULT NULL")
    private String observaciones;

    @Column(name = "estado",length = 45, nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "userLogin_id", referencedColumnName = "usuario_id")
    private Usuario userLogin;

    @PrePersist
    private void asignacionFechaCreacion() {
        creDate = LocalDateTime.now();
    }

    @PreUpdate
    private void asignacionFechaActualizacion() {
        datecreta = LocalDateTime.now();
    }

	public Asignacion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Asignacion(Integer id, LocalDateTime creDate, Cliente cliente, String descripcion, LocalDateTime datecreta,
			String observaciones, String estado, Usuario usuario, Usuario userLogin) {
		super();
		this.id = id;
		this.creDate = creDate;
		this.cliente = cliente;
		this.descripcion = descripcion;
		this.datecreta = datecreta;
		this.observaciones = observaciones;
		this.estado = estado;
		this.usuario = usuario;
		this.userLogin = userLogin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getCreDate() {
		return creDate;
	}

	public void setCreDate(LocalDateTime creDate) {
		this.creDate = creDate;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDateTime getDatecreta() {
		return datecreta;
	}

	public void setDatecreta(LocalDateTime datecreta) {
		this.datecreta = datecreta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(Usuario userLogin) {
		this.userLogin = userLogin;
	}

	@Override
	public String toString() {
		return "Asignacion [id=" + id + ", creDate=" + creDate + ", cliente=" + cliente + ", descripcion=" + descripcion
				+ ", datecreta=" + datecreta + ", observaciones=" + observaciones + ", estado=" + estado + ", usuario="
				+ usuario + ", userLogin=" + userLogin + "]";
	}


}

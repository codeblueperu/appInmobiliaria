package mac.inmobiliaria.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "proforma")
public class Proforma {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "proforma_id")
	private Integer id;

	@Column(name = "entero_Proyecto",length = 60, nullable = false)
	private String enteroProyecto;

	@Column(name = "fecha_Proforma",nullable = false)
	private Date fechaProforma;
	
	@Column(name = "observaciones",nullable = true,columnDefinition = "text")
	private String observaciones;

	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "pago_id", referencedColumnName = "pago_id")
	private Pago pago;

	public Proforma() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Proforma(Integer id, String enteroProyecto, Date fechaProforma, String observaciones, Usuario usuario,
			Pago pago) {
		super();
		this.id = id;
		this.enteroProyecto = enteroProyecto;
		this.fechaProforma = fechaProforma;
		this.observaciones = observaciones;
		this.usuario = usuario;
		this.pago = pago;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEnteroProyecto() {
		return enteroProyecto;
	}

	public void setEnteroProyecto(String enteroProyecto) {
		this.enteroProyecto = enteroProyecto;
	}

	public Date getFechaProforma() {
		return fechaProforma;
	}

	public void setFechaProforma(Date fechaProforma) {
		this.fechaProforma = fechaProforma;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	@Override
	public String toString() {
		return "Proforma [id=" + id + ", enteroProyecto=" + enteroProyecto + ", fechaProforma=" + fechaProforma
				+ ", observaciones=" + observaciones + ", usuario=" + usuario + ", pago=" + pago + "]";
	}
}

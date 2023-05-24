package mac.inmobiliaria.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "inmueble")
public class Inmueble {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inmueble_id")
    private Integer id;

    @Column(name = "proyecto",length = 60, nullable = false)
    private String proyecto;

    @Column(name = "departamento",length = 60, nullable = false)
    private String departamento;

    @Column(name = "precio_Depa", nullable = false)
    private Double precio_Departamento;

    @Column(name = "area_Depa",length = 20, nullable = false)
    private String area_Depa;

    @Column(name = "precio_Estaciona", nullable = false)
    private Double precio_Estaciona;

    @Column(name = "estacionamiento",length = 45, nullable = true)
    private String estacionamiento;

    @Column(name = "precio_Total", nullable = true)
    private Double precio_Total;

    @Column(name = "deposito",length = 30, nullable = true)
    private String deposito;
    
    @Column(name = "estado", nullable = false, columnDefinition = "int default 1")
    private int estado;

	public Inmueble() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Inmueble(Integer id, String proyecto, String departamento, Double precio_Departamento, String area_Depa,
			Double precio_Estaciona, String estacionamiento, Double precio_Total, String deposito, int estado) {
		super();
		this.id = id;
		this.proyecto = proyecto;
		this.departamento = departamento;
		this.precio_Departamento = precio_Departamento;
		this.area_Depa = area_Depa;
		this.precio_Estaciona = precio_Estaciona;
		this.estacionamiento = estacionamiento;
		this.precio_Total = precio_Total;
		this.deposito = deposito;
		this.estado = estado;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getProyecto() {
		return proyecto;
	}



	public void setProyecto(String proyecto) {
		this.proyecto = proyecto;
	}



	public String getDepartamento() {
		return departamento;
	}



	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}



	public Double getPrecio_Departamento() {
		return precio_Departamento;
	}



	public void setPrecio_Departamento(Double precio_Departamento) {
		this.precio_Departamento = precio_Departamento;
	}



	public String getArea_Depa() {
		return area_Depa;
	}



	public void setArea_Depa(String area_Depa) {
		this.area_Depa = area_Depa;
	}



	public Double getPrecio_Estaciona() {
		return precio_Estaciona;
	}



	public void setPrecio_Estaciona(Double precio_Estaciona) {
		this.precio_Estaciona = precio_Estaciona;
	}



	public String getEstacionamiento() {
		return estacionamiento;
	}



	public void setEstacionamiento(String estacionamiento) {
		this.estacionamiento = estacionamiento;
	}



	public Double getPrecio_Total() {
		return precio_Total;
	}



	public void setPrecio_Total(Double precio_Total) {
		this.precio_Total = precio_Total;
	}



	public String getDeposito() {
		return deposito;
	}



	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}



	public int getEstado() {
		return estado;
	}


	public void setEstado(int estado) {
		this.estado = estado;
	}


	@Override
	public String toString() {
		return "Inmueble [id=" + id + ", proyecto=" + proyecto + ", departamento=" + departamento
				+ ", precio_Departamento=" + precio_Departamento + ", area_Depa=" + area_Depa + ", precio_Estaciona="
				+ precio_Estaciona + ", estacionamiento=" + estacionamiento + ", precio_Total=" + precio_Total
				+ ", deposito=" + deposito + ", estado=" + estado + "]";
	}



	
}

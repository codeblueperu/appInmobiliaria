package mac.inmobiliaria.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "provincia")
public class Provincia {

	
	
	@Id
    @Column(name = "provincia_id")
    private Integer id;
	
	
	@Column(name = "provincia")
    private String provincia;
	
	@OneToOne
	@JoinColumn(name = "departamento_id")
	private Departamento departamento;

	
	
	
	public Provincia() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Provincia(Integer id, String provincia, Departamento departamento) {
		super();
		this.id = id;
		this.provincia = provincia;
		this.departamento = departamento;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	@Override
	public String toString() {
		return "Provincia [id=" + id + ", provincia=" + provincia + ", departamento=" + departamento + "]";
	}
	
	
}

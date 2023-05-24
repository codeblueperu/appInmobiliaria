package mac.inmobiliaria.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "departamento")
public class Departamento {

	
	@Id
    @Column(name = "departamento_id")
    private Integer id;
	
	
	@Column(name = "departamento")
    private String departamento;


	
	
	public Departamento() {
		super();
		// TODO Auto-generated constructor stub
	}




	public Departamento(Integer id, String departamento) {
		super();
		this.id = id;
		this.departamento = departamento;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getDepartamento() {
		return departamento;
	}


	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	@Override
	public String toString() {
		return "Departamento [id=" + id + ", departamento=" + departamento + "]";
	}
	
}

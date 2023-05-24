package mac.inmobiliaria.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ubigeo")
public class Ubigeo {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ubigeo_id")
    private Integer id;

    @Column(name = "distrito", length = 45, nullable = false)
    private String distrito;

    @Column(name = "provincia", length = 45, nullable = false)
    private String provincia;

    @Column(name = "departamento", length = 45, nullable = false)
    private String departamento;

	public Ubigeo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Ubigeo(Integer id, String distrito, String provincia, String departamento) {
		super();
		this.id = id;
		this.distrito = distrito;
		this.provincia = provincia;
		this.departamento = departamento;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	@Override
	public String toString() {
		return "Ubigeo [id=" + id + ", distrito=" + distrito + ", provincia=" + provincia + ", departamento="
				+ departamento + "]";
	}
}

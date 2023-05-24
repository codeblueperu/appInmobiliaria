package mac.inmobiliaria.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "distrito")
public class Distrito {
	
	@Id
    @Column(name = "distrito_id")
    private Integer id;
	
	
	@Column(name = "distrito")
    private String distrito;
	
	@OneToOne
	@JoinColumn(name = "provincia_id")
	private Provincia provincia;
	
	
	

	public Distrito() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Distrito(Integer id, String distrito, Provincia provincia) {
		super();
		this.id = id;
		this.distrito = distrito;
		this.provincia = provincia;
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

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	@Override
	public String toString() {
		return "Distrito [id=" + id + ", distrito=" + distrito + ", provincia=" + provincia + "]";
	}
	
	
	

}

package mac.inmobiliaria.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configuraciones")
public class Configuraciones {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idConfiguraciones;
	
	private String userEmail;
	
	private String passwordEmail;
	
	private String hostEmail;
	
	private int puetroEmail;

	public Configuraciones() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Configuraciones(Integer idConfiguraciones, String userEmail, String passwordEmail, String hostEmail,
			int puetroEmail) {
		super();
		this.idConfiguraciones = idConfiguraciones;
		this.userEmail = userEmail;
		this.passwordEmail = passwordEmail;
		this.hostEmail = hostEmail;
		this.puetroEmail = puetroEmail;
	}

	public Integer getIdConfiguraciones() {
		return idConfiguraciones;
	}

	public void setIdConfiguraciones(Integer idConfiguraciones) {
		this.idConfiguraciones = idConfiguraciones;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getPasswordEmail() {
		return passwordEmail;
	}

	public void setPasswordEmail(String passwordEmail) {
		this.passwordEmail = passwordEmail;
	}

	public String getHostEmail() {
		return hostEmail;
	}

	public void setHostEmail(String hostEmail) {
		this.hostEmail = hostEmail;
	}

	public int getPuetroEmail() {
		return puetroEmail;
	}

	public void setPuetroEmail(int puetroEmail) {
		this.puetroEmail = puetroEmail;
	}

	@Override
	public String toString() {
		return "Configuraciones [idConfiguraciones=" + idConfiguraciones + ", userEmail=" + userEmail
				+ ", passwordEmail=" + passwordEmail + ", hostEmail=" + hostEmail + ", puetroEmail=" + puetroEmail
				+ "]";
	}
}

package mac.inmobiliaria.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "pago")
public class Pago {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pago_id")
    private Integer id;

    @Column(name = "cuota_Inicial",length = 30, nullable = false)
    private String cuotaInicial;

    @Column(name = "edificio",length = 60, nullable = false)
    private String edificio;

    @Column(name = "credito_Hipote",length = 45, nullable = false)
    private String creditoHipote;

    @Column(name = "fecha_Cre_Hipo",length = 45, nullable = false)
    private String fechaCreHipo;

    @Column(name = "cuota_Mensual",length = 30, nullable = false)
    private String cuotaMensual;

    @Column(name = "años_Cuo_Men",length = 30, nullable = false)
    private String anosCuoMen;

    @Column(name = "numero_Cuenta",length = 50, nullable = true)
    private String numeroCuenta;

    @Column(name = "nombre_Banco",length = 50, nullable = false)
    private String nombreBanco;

    @ManyToOne
    @JoinColumn(name = "inmueble_id", referencedColumnName = "inmueble_id")
    private Inmueble inmueble;

    @ManyToOne
    @JoinColumn(name = "datos_id", referencedColumnName = "datos_id")
    private Datos datos;
    
    @Column(name = "id_usuario", nullable = true)
    private int  idUsuario;

	public Pago() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public Pago(Integer id, String cuotaInicial, String edificio, String creditoHipote, String fechaCreHipo,
			String cuotaMensual, String anosCuoMen, String numeroCuenta, String nombreBanco, Inmueble inmueble,
			Datos datos, int idUsuario) {
		super();
		this.id = id;
		this.cuotaInicial = cuotaInicial;
		this.edificio = edificio;
		this.creditoHipote = creditoHipote;
		this.fechaCreHipo = fechaCreHipo;
		this.cuotaMensual = cuotaMensual;
		this.anosCuoMen = anosCuoMen;
		this.numeroCuenta = numeroCuenta;
		this.nombreBanco = nombreBanco;
		this.inmueble = inmueble;
		this.datos = datos;
		this.idUsuario = idUsuario;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCuotaInicial() {
		return cuotaInicial;
	}

	public void setCuotaInicial(String cuotaInicial) {
		this.cuotaInicial = cuotaInicial;
	}

	public String getEdificio() {
		return edificio;
	}

	public void setEdificio(String edificio) {
		this.edificio = edificio;
	}

	public String getCreditoHipote() {
		return creditoHipote;
	}

	public void setCreditoHipote(String creditoHipote) {
		this.creditoHipote = creditoHipote;
	}

	public String getFechaCreHipo() {
		return fechaCreHipo;
	}

	public void setFechaCreHipo(String fechaCreHipo) {
		this.fechaCreHipo = fechaCreHipo;
	}

	public String getCuotaMensual() {
		return cuotaMensual;
	}

	public void setCuotaMensual(String cuotaMensual) {
		this.cuotaMensual = cuotaMensual;
	}

	public String getAnosCuoMen() {
		return anosCuoMen;
	}

	public void setAnosCuoMen(String anosCuoMen) {
		this.anosCuoMen = anosCuoMen;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getNombreBanco() {
		return nombreBanco;
	}

	public void setNombreBanco(String nombreBanco) {
		this.nombreBanco = nombreBanco;
	}

	public Inmueble getInmueble() {
		return inmueble;
	}

	public void setInmueble(Inmueble inmueble) {
		this.inmueble = inmueble;
	}

	public Datos getDatos() {
		return datos;
	}

	public void setDatos(Datos datos) {
		this.datos = datos;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}


	@Override
	public String toString() {
		return "Pago [id=" + id + ", cuotaInicial=" + cuotaInicial + ", edificio=" + edificio + ", creditoHipote="
				+ creditoHipote + ", fechaCreHipo=" + fechaCreHipo + ", cuotaMensual=" + cuotaMensual + ", anosCuoMen="
				+ anosCuoMen + ", numeroCuenta=" + numeroCuenta + ", nombreBanco=" + nombreBanco + ", inmueble="
				+ inmueble + ", datos=" + datos + ", idUsuario=" + idUsuario + "]";
	}
	
	

	
}

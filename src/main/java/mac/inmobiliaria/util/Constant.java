package mac.inmobiliaria.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import mac.inmobiliaria.security.UserDetailsImpl;

public class Constant {

    public static final String CRON_ASIGNACION_NO_ATENTIDA = "1 * * * * *";
    public static  String USER_SERVICE_EMAIL = "jam.dta18@gmail.com";
	public static final String PASS_SERVICE_EMAIL = "crnucfcklwtiwczp";
	public static final String HOST_SERVICE_EMAIL = "smtp.gmail.com";
	public static final int PORT_SERVICE_EMAIL = 587;

    public static Integer obtenerUsuarioLogeadoId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl usuario = (UserDetailsImpl) authentication.getPrincipal();
        return usuario.getUsuario().getId();
    }
    
    
    public static int generarCodigo() {
		String pswd = "";
		String parafraseo = "123456789";

		for (int i = 1; i <= 4; i++) {
			pswd += (parafraseo.charAt((int) (Math.random() * parafraseo.length())));
		}

		return Integer.parseInt(pswd);
	}
}

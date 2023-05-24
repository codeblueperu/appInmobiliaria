package mac.inmobiliaria.exception;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



//@ControllerAdvice
public class ExceptionController {
	
//	 @Autowired
//	    private UsuarioRepository usuarioRepository;
	
//	@ExceptionHandler(UsernameNotFoundException.class)
//	public String userNotFoundException(UserNotFoundException ex) {
////		Usuario user=	usuarioRepository.findById(obtenerUsuarioLogeadoId()).get();
////		user.setSessionLogin(0);
////		usuarioRepository.save(user);
//		return "login";
//	}
//
//
//	@ExceptionHandler(IllegalArgumentException.class)
//	public ResponseEntity<?> illegalArgumentException(IllegalArgumentException ex) {
//		//System.err.println(002054);
//		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//	}

}

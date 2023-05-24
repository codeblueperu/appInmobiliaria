package mac.inmobiliaria.controller;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mac.inmobiliaria.model.Distrito;
import mac.inmobiliaria.model.Ubigeo;
import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.UbigeoRepository;
import mac.inmobiliaria.service.EmailNotificationService;
import mac.inmobiliaria.util.Constant;
import mac.inmobiliaria.repository.UsuarioRepository;


@Controller
public class LoginControllerAdmin {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UbigeoRepository ubigeoRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailNotificationService srvEmail;

	@GetMapping("/login")
	public String onLogin(Model model) {
		if (usuarioRepository.findAll().size() == 0) {
			if (ubigeoRepository.findAll().size() == 0) {
				Ubigeo ub = new Ubigeo();
				ub.setDepartamento("Desconocido");
				ub.setDistrito("Desconocido");
				ub.setProvincia("Desconocido");
				ubigeoRepository.save(ub);
			}
			return "redirect:/preinstall";
		}
		return "login";
	}
	
	@PostMapping("/login")
	public String onLoginPost(RedirectAttributes ra) {
		return "redirect:/login";
	}
	

	@GetMapping("/")
	public String index(Model model) {
		return "redirect:/login";
	}

	@GetMapping("/preinstall")
	public String onPreinstallAppWeb(Model model) {
		if (usuarioRepository.findAll().size()> 0) {
			return "redirect:/login";
		}
		List<Ubigeo> listaUbi = ubigeoRepository.findAll();
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("listaUbi", listaUbi);
		return "prepared";
	}

	@GetMapping("/recover-password")
	public String recoverPassword(Model model) {
		return "recoverPassword";
	}

	@PostMapping("/validarEmail/user")
	public String validaremialUser(@RequestParam("email") String email, RedirectAttributes ra) {
		Optional<Usuario> user = usuarioRepository.findByEmail(email);
		if (user.isPresent()) {
			int codeVerificacion = Constant.generarCodigo();
			String charreo = "<!DOCTYPE html>\r\n" + "<html lang='en'>\r\n" + "<head>\r\n"
					+ "	<meta charset='UTF-8'>\r\n" + "	<title>Document</title>\r\n" + "	<style>\r\n"
					+ "		.container{\r\n" + "			width: 100%;\r\n" + "			background: #f7f4f4;\r\n"
					+ "			\r\n"
					+ "			font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;\r\n"
					+ "			padding: .9rem;\r\n" + "		}\r\n" + "		p{font-size: 12px;\r\n"
					+ "			text-align: justify;\r\n" + "		}\r\n" + "		.code{\r\n"
					+ "			font-size: 30px;\r\n" + "			text-align: center;\r\n" + "		}\r\n"
					+ "	</style>\r\n" + "</head>\r\n" + "<body>\r\n" + "	<div class='container'>\r\n"
					+ "		<h4>Hola, " + user.get().getNombreCompleto() + "<h4>\r\n"
					+ "		<p>Se le esta remitiendo su codigo de validación para que pueda proceder con el restablecimiento de su nueva contraseña,\r\n"
					+ "			 recuerde que este codigo de verificación solo tiene una duración de 15 minutos.</p>\r\n"
					+ "			 <p class='code'>" + codeVerificacion + "</p>\r\n"
					+ "			 <p>por favor no comparta este codigo ya que su cuenta estaria en riesgo.</p>\r\n"
					+ "	</div>\r\n" + "</body>\r\n" + "</html>";
			srvEmail.onenviarCorreos("Codigo de verificación de cuenta", charreo, user.get().getEmail(), false, null);

			Usuario update = user.get();
			update.setCodeVerifyrecoverPassword(codeVerificacion);
			usuarioRepository.save(user.get());
			System.err.println(update);
			return "redirect:/verifycodeemail";
		} else {
			ra.addFlashAttribute("redirect", "NOT_EXISTES");
			return "redirect:/recover-password";
		}

		// return "redirect:/recover-password";
		//return "redirect:/verifycodeemail";
	}

	@GetMapping("/verifycodeemail")
	public String srvVerifycodeemail(Model model) {
		return "verifycodeemail";
	}

	@GetMapping("/updatepassword")
	public String srvUpdatepassword(@RequestParam("token") String code, Model model) {
		model.addAttribute("code", code);
		return "updatepassword";
	}

	@PostMapping("/verify-code/user")
	public String verificarCodigousuario(@RequestParam("one") String one, @RequestParam("two") String two,
			@RequestParam("tree") String tree, @RequestParam("for") String fornumber, RedirectAttributes ra,
			Model model) {
		String code = one + two + tree + fornumber;
		Usuario user = usuarioRepository.findByCodeVerifyrecoverPassword(Integer.parseInt(code));
		if (user != null) {
			return "redirect:/updatepassword?token=" + code;
		} else {
			ra.addFlashAttribute("redirect", "NOT_EXISTES");
		}

		return "redirect:/verifycodeemail";
	}

	@PostMapping("/updatepassworduser/now")
	public String updatePasswordUserNow(@RequestParam("code") String code, @RequestParam("password") String password,
			@RequestParam("confirmpassword") String confirmpassword, RedirectAttributes ra, Model model) {

		if (password.compareTo(confirmpassword) != 0) {
			ra.addFlashAttribute("pwerror", "Las contraseñas no son iguales");
			return "redirect:/updatepassword?token=" + code;
		}
		Usuario user = usuarioRepository.findByCodeVerifyrecoverPassword(Integer.parseInt(code));

		user.setPassword(passwordEncoder.encode(confirmpassword));
		usuarioRepository.save(user);

		ra.addFlashAttribute("success",
				"Su contraseña fue modificada con éxito, se le procedera a dirigir al login....");
		return "redirect:/updatepassword?token=" + code;
	}

	@PostMapping("/create/count/user")
	String crear(@ModelAttribute Usuario usuario, BindingResult bindingResult, RedirectAttributes ra, Model model) {

		List<Ubigeo> listaUbi = ubigeoRepository.findAll();
		// validar si el telefono ya está siendo usado
		String telefono = usuario.getTelefono();
		boolean telefonoYaExiste = usuarioRepository.existsByTelefono(telefono);

		if (telefonoYaExiste) {
			bindingResult.rejectValue("telefono", "TelefonoAlreadyExists");
		} else {
			if (telefono.isEmpty()) {
				bindingResult.rejectValue("telefono", "TelefonoNotBlank");
			} else {
				if (!telefono.matches(("[0-9]*")) || telefono.length() < 0 || telefono.length() > 9) {
					bindingResult.rejectValue("telefono", "TelefonoNotNumeric");
				} else {
					if (telefono.length() < 9) {
						bindingResult.rejectValue("telefono", "TelefonoNotNumericthenine");
					}
				}
			}
		}

		// validar si el email ya está siendo usado
		String email = usuario.getEmail();
		boolean usuarioYaExiste = usuarioRepository.existsByEmail(email);

		if (usuarioYaExiste) {
			bindingResult.rejectValue("email", "EmailAlreadyExists");
		} else {
			if (email.isEmpty()) {
				bindingResult.rejectValue("email", "EmailNotBlank");
			}
		}

		String nombres = usuario.getNombres();
		boolean esSoloLetras = usuarioRepository.soloLetras(nombres);
		if (nombres.isEmpty()) {
			bindingResult.rejectValue("nombres", "NombresNotBlank");
		} else {
			if (!esSoloLetras) {
				bindingResult.rejectValue("nombres", "NombresNotLetras");
			}
		}

		String apellidos = usuario.getApellidos();
		boolean esSoloLetrasApe = usuarioRepository.soloLetras(apellidos);
		if (apellidos.isEmpty()) {
			bindingResult.rejectValue("apellidos", "ApellidosNotBlank");
		} else {
			if (!esSoloLetrasApe) {
				bindingResult.rejectValue("apellidos", "ApellidosNotLetras");
			}
		}
		String password = usuario.getPassword();
		if (password.isEmpty()) {
			bindingResult.rejectValue("password", "PasswordNotBlank");
		}

		String sexo = usuario.getSexo();
		if (sexo.isEmpty()) {
			bindingResult.rejectValue("sexo", "SexoNotBlank");
		}
		String ubigeo = String.valueOf(usuario.getDistrito());
		if (ubigeo.isEmpty()) {
			bindingResult.rejectValue("ubigeo", "UbigeoIsNull");
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("usuario", usuario);
			model.addAttribute("listaUbi", listaUbi);
			//model.addAttribute("id", obtenerUsuarioLogeadoId());
			return "prepared";
		}

		usuario.setRol(Usuario.Rol.ADMIN);
		usuario.setDistrito(new Distrito(1));
		usuario.setPreinstall(1);
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		usuarioRepository.save(usuario);
		ra.addFlashAttribute("clase", "success");
		ra.addFlashAttribute("redirect", "OK");
		ra.addFlashAttribute("mensaje", "El usuario administrador se a creado con éxito.");
		return "redirect:/preinstall";
	}
	
	@GetMapping("/logoutuser")
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		//System.err.println(0);
		Usuario user=	usuarioRepository.findById(obtenerUsuarioLogeadoId()).get();
		user.setSessionLogin(0);
		usuarioRepository.save(user);
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    
	    return "redirect:/login?logout";
	}

}

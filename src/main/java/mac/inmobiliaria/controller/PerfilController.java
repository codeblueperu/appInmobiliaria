package mac.inmobiliaria.controller;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.UbigeoRepository;
import mac.inmobiliaria.repository.UsuarioRepository;

@Controller
@RequestMapping("perfil")
public class PerfilController {

	@Autowired
	UsuarioRepository repoUser;

	@Autowired
	private UbigeoRepository ubigeoRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/view/{id}")
	String nuevo(@PathVariable(value = "id") int id, Model model) {

		Usuario usuario = repoUser.findById(id).get();

		model.addAttribute("idDepartamento", usuario.getDistrito().getProvincia().getDepartamento().getId());
		model.addAttribute("idProvincia", usuario.getDistrito().getProvincia().getId());
		model.addAttribute("idDistrito", usuario.getDistrito().getId());
		
		model.addAttribute("idusuario",id);

		model.addAttribute("datauser", repoUser.findById(id).get());
		return "usuarios/perfil";
	}

	@PostMapping("/updateperfil")
	public String srvActualizarDatosPersonales(@ModelAttribute @Validated Usuario usuario, BindingResult bindingResult,
			RedirectAttributes ra, Model model) {

		Usuario exist = repoUser.findById(usuario.getId()).get();

		if (usuario.getTelefono().isEmpty()) {
			bindingResult.rejectValue("telefono", "TelefonoNotBlank");
		} else if (usuario.getTelefono().length() < 9) {
			bindingResult.rejectValue("telefono", "TelefonoNotNumericthenine");
		}

		if (usuario.getNombres().isEmpty()) {
			bindingResult.rejectValue("nombres", "NombresNotBlank");
		} else {
			boolean esSoloLetras = repoUser.soloLetras(usuario.getNombres());
			if (!esSoloLetras) {
				bindingResult.rejectValue("nombres", "NombresNotLetras");
			}
		}

		if (usuario.getApellidos().isEmpty()) {
			bindingResult.rejectValue("apellidos", "ApellidosNotBlank");
		} else {
			boolean esSoloLetrasApe = repoUser.soloLetras(usuario.getApellidos());
			if (!esSoloLetrasApe) {
				bindingResult.rejectValue("apellidos", "ApellidosNotLetras");
			}
		}

		if (usuario.getSexo().isEmpty()) {
			bindingResult.rejectValue("sexo", "SexoNotBlank");
		}

		if (usuario.getDistrito().getId() == null) {
			bindingResult.rejectValue("ubigeo", "UbigeoNotBlank");
		}
		System.err.println(bindingResult.hasErrors());
		if (bindingResult.hasErrors()) {
			model.addAttribute("usuario", usuario);
			model.addAttribute("datauser", repoUser.findById(usuario.getId()).get());
			model.addAttribute("id", obtenerUsuarioLogeadoId());

			return "usuarios/perfil";
		}

		if (usuario.getPassword().isEmpty()) {
			usuario.setPassword(exist.getPassword());
			if (exist.getPreinstall() == 1) {
				usuario.setPreinstall(1);
			}
		} else {
			if (exist.getPreinstall() == 1) {
				usuario.setPreinstall(1);
			}
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		}

		usuario.setRol(exist.getRol());
		repoUser.save(usuario);
		ra.addFlashAttribute("clase", "success");
		ra.addFlashAttribute("mensaje", "Sus datos fueron actualizados con éxito");
		return "redirect:/perfil/view/" + usuario.getId();
	}

	@ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("id", obtenerUsuarioLogeadoId());
		model.addAttribute("dtuser", repoUser.findById(obtenerUsuarioLogeadoId()).get());
	}
}

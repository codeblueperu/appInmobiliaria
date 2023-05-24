package mac.inmobiliaria.controller;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.UsuarioRepository;

@Controller
@RequestMapping("/home")
public class HomeControllerAdmin {
	
	 @Autowired
	    private UsuarioRepository usuarioRepository;

	@GetMapping
	public String index(Model model, RedirectAttributes ra) {
	Usuario user=	usuarioRepository.findById(obtenerUsuarioLogeadoId()).get();
	
	if(user.getSessionLogin() > 1) {
		ra.addFlashAttribute("login",user.getNombres().toUpperCase() + " Usted ya tiene una sesion abierta en otro dispositivo se le recomienda cerrar y volver a intentar");
		return "redirect:/login";
	}

		return "master";
	}
	
	@ModelAttribute
	public void setGenericos(Model model) {
		
		model.addAttribute("dtuser",  usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}
}

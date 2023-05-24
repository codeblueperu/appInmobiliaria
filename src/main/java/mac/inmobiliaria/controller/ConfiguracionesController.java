package mac.inmobiliaria.controller;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import mac.inmobiliaria.model.Configuraciones;
import mac.inmobiliaria.repository.IConfiguracionesRepository;
import mac.inmobiliaria.repository.UsuarioRepository;

@Controller
@RequestMapping("/admin")
public class ConfiguracionesController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired()
	private IConfiguracionesRepository repoSetting;

	@GetMapping("/setting")
	public String index(Model model) {
		  
		  model.addAttribute("setting", repoSetting.findById(1).get());
		return "Setting";
	}
	
	@PostMapping("/update")
	public String update(Configuraciones setting,  BindingResult bindingResult,RedirectAttributes ra) {
		ra.addFlashAttribute("mensaje", "Las configuraciones del servidor de correo se actualizaron correctamente");
		repoSetting.save(setting);
		return "redirect:/admin/setting";
	}

	@ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("id", obtenerUsuarioLogeadoId());
		model.addAttribute("dtuser", usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}

}

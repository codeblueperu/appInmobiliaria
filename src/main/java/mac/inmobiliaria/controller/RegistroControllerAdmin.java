package mac.inmobiliaria.controller;

import mac.inmobiliaria.model.Ubigeo;
import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.UbigeoRepository;
import mac.inmobiliaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;


@Controller
@RequestMapping("/registro")
public class RegistroControllerAdmin {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UbigeoRepository ubigeoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    ModelAndView index() {
        List<Ubigeo> listaUbi = ubigeoRepository.findAll();
        return new ModelAndView("registro")
                .addObject("id", obtenerUsuarioLogeadoId())
                .addObject("usuario", new Usuario())
                .addObject("listaUbi",listaUbi);
    }

    @PostMapping
    ModelAndView crear(@Validated Usuario usuario, BindingResult bindingResult, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("registro")
                    .addObject("id", obtenerUsuarioLogeadoId())
                    .addObject("usuario", usuario);
        }
        // validar si el telefono ya está siendo usado
        String telefono = usuario.getTelefono();
        boolean telefonoYaExiste = usuarioRepository.existsByTelefono(telefono);

        if (telefonoYaExiste) {
            bindingResult.rejectValue("telefono", "TelefonoAlreadyExists");
        }

        // validar si el email ya está siendo usado
        String email = usuario.getEmail();
        boolean usuarioYaExiste = usuarioRepository.existsByEmail(email);

        if (usuarioYaExiste) {
            bindingResult.rejectValue("email", "EmailAlreadyExists");
        }

        // validar si las contraseñas coinciden
        if (!usuario.getPassword1().equals(usuario.getPassword2())) {
            bindingResult.rejectValue("password1", "PasswordNotEqualss");
        }

        if (bindingResult.hasErrors()) {
            return new ModelAndView("registro")
                    .addObject("usuario", usuario);
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword1()));
        usuario.setRol(Usuario.Rol.ADMIN);
        usuarioRepository.save(usuario);

        ra.addAttribute("registroExitoso", "");

        return new ModelAndView("redirect:/login");
    }
    
    @ModelAttribute
	public void setGenericos(Model model) {
		
		model.addAttribute("dtuser",  usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}
}

package mac.inmobiliaria.controller;

import java.util.List;
import java.util.Optional;

import mac.inmobiliaria.model.Asignacion;
import mac.inmobiliaria.model.Cliente;
import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.AsignacionRepository;
import mac.inmobiliaria.repository.ClienteRepository;
import mac.inmobiliaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;


@Controller
@RequestMapping("/listaAsignaciones")
public class ListaAsignacionesControllerAsesor {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AsignacionRepository asignacionRepository;

    @GetMapping
    public String index(Model model,
                        @RequestParam(required = false) String estado,
                        @RequestParam(required = false) String nombre,
                        @RequestParam(required = false) String nombreCompleto,
                        @RequestParam(required = false) String dni,
                        @PageableDefault(size = 3) Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(username);


        Page<Asignacion> asignacionPage;
		List<Cliente> listaCliente;
		List<Usuario> listaUsuario;
//
//		Usuario user = usuarioRepository.findById(obtenerUsuarioLogeadoId()).get();
//		String Rol = String.valueOf(user.getRol());

		if (StringUtils.hasText(estado)) {
			
			asignacionPage = asignacionRepository.findByEstadoAndUsuarioId(estado,usuarioEncontrado.get().getId(), pageable);
			listaCliente = clienteRepository.findByNombre(nombre);
			listaUsuario = usuarioRepository.findByNombres(nombreCompleto);
		} else if (StringUtils.hasText(nombre)) {
			
			asignacionPage = asignacionRepository.findByClienteNombre(nombre, pageable);
			listaCliente = clienteRepository.findByNombre(nombre);
			listaUsuario = usuarioRepository.findByNombres(nombreCompleto);
			
		} else if (StringUtils.hasText(dni)) {
			asignacionPage = asignacionRepository.findByClienteDni(dni, pageable);
			listaCliente = clienteRepository.findByNombre(nombre);
			listaUsuario = usuarioRepository.findByNombres(nombreCompleto);
		} else {
			asignacionPage = asignacionRepository.findByUsuarioId(usuarioEncontrado.get().getId(),pageable);
			listaCliente = clienteRepository.findAll();
			listaUsuario = usuarioRepository.findAll();
		}

		model.addAttribute("asignacionPage", asignacionPage);
		model.addAttribute("listaCliente", listaCliente);
		model.addAttribute("listaUsuario", listaUsuario);
		model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "listaAsignaciones/index";
    }


    @GetMapping("/{id}/editar")
    String editar(@PathVariable Integer id, Model model) {
        Asignacion asignacion = asignacionRepository.findById(id).get();
        
        List<Cliente> listaCli = clienteRepository.findAll();
        List<Usuario> listaUsu = usuarioRepository.findAll();
        model.addAttribute("asignacion", asignacion);
        model.addAttribute("listaCli", listaCli);
        model.addAttribute("listaUsu", listaUsu);
        
        return "listaAsignaciones/formulario";
    }

    @PostMapping("/{id}/editar")
    String actualizar(@PathVariable Integer id, @ModelAttribute Asignacion asignacion,
            BindingResult bindingResult,
            RedirectAttributes ra,
            Model model) {

        Asignacion asignacionDB = asignacionRepository.findById(id).get();
        List<Cliente> listaCli = clienteRepository.findAll();
        List<Usuario> listaUsu = usuarioRepository.findAll();

        asignacionDB.setDescripcion(asignacion.getDescripcion());
        asignacionDB.setObservaciones(asignacion.getObservaciones());
        asignacionDB.setEstado(asignacion.getEstado());

        if (bindingResult.hasErrors()) {
            model.addAttribute("asignacion", asignacion);
            model.addAttribute("listaCli", listaCli);
            model.addAttribute("listaUsu", listaUsu);
            return "listaAsignaciones/formulario";
        }

        asignacionRepository.save(asignacionDB);

        ra.addFlashAttribute("msgExito", "La Asignacion ha sido actualizado correctamente");

        return "redirect:/listaAsignaciones";
    }
    
    @ModelAttribute
	public void setGenericos(Model model) {
		
		model.addAttribute("dtuser",  usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}
}

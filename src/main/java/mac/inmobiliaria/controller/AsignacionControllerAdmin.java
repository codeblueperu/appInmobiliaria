package mac.inmobiliaria.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;

@Controller
@RequestMapping("/admin/asignaciones")
public class AsignacionControllerAdmin {
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private AsignacionRepository asignacionRepository;

	@GetMapping
	public String index(Model model, @RequestParam(required = false) String estado,
			@RequestParam(required = false) String nombre, @RequestParam(required = false) String nombreCompleto,
			@PageableDefault(size = 3) Pageable pageable) {

		Page<Asignacion> asignacionPage;
		List<Cliente> listaCliente;
		List<Usuario> listaUsuario;

		Usuario user = usuarioRepository.findById(obtenerUsuarioLogeadoId()).get();
		String Rol = String.valueOf(user.getRol());

		if (StringUtils.hasText(nombreCompleto) && StringUtils.hasText(estado)) {
			if (Rol.compareTo("ADMIN") == 0 && user.getPreinstall() == 1) {
				asignacionPage = asignacionRepository.findByUsuarioNombreCompletoStartingWithAndEstado(nombreCompleto,
						estado, pageable);
			} else {
				asignacionPage = asignacionRepository.findByUsuarioNombreCompletoStartingWithAndEstadoAndUserLoginId(
						nombreCompleto, estado, user.getId(), pageable);
			}

			listaCliente = clienteRepository.findByNombre(nombre);
			listaUsuario = usuarioRepository.findByNombres(nombreCompleto);
		} else if (StringUtils.hasText(estado)) {
			if (Rol.compareTo("ADMIN") == 0 && user.getPreinstall() == 1) {
				asignacionPage = asignacionRepository.findByEstado(estado, pageable);
			} else {
				asignacionPage = asignacionRepository.findByEstadoAndUserLoginId(estado, user.getId(), pageable);
			}
			listaCliente = clienteRepository.findByNombre(nombre);
			listaUsuario = usuarioRepository.findByNombres(nombreCompleto);
		} else if (StringUtils.hasText(nombre)) {
			if (Rol.compareTo("ADMIN") == 0 && user.getPreinstall() == 1) {
				asignacionPage = asignacionRepository.findByClienteNombre(nombre, pageable);
			} else {
				asignacionPage = asignacionRepository.findByClienteNombreAndUserLoginId(nombre, user.getId(), pageable);
			}

			listaCliente = clienteRepository.findByNombre(nombre);
			listaUsuario = usuarioRepository.findByNombres(nombreCompleto);
		} else if (StringUtils.hasText(nombreCompleto)) {
			if (Rol.compareTo("ADMIN") == 0 && user.getPreinstall() == 1) {
				System.err.println("USERLOGIN");
				asignacionPage = asignacionRepository.findByUsuarioNombreCompleto(nombreCompleto, pageable);
			} else {
				System.err.println("USERLOGIN//");
				asignacionPage = asignacionRepository.findByUsuarioNombreCompletoAndUserLoginId(nombreCompleto, user.getId(),
						pageable);
			}

			listaCliente = clienteRepository.findByNombre(nombre);
			listaUsuario = usuarioRepository.findByNombres(nombreCompleto);

		} else {
			if (Rol.compareTo("ADMIN") == 0 && user.getPreinstall() == 1) {
				asignacionPage = asignacionRepository.findAll(pageable);
			} else {
				asignacionPage = asignacionRepository.findByUserLoginId(user.getId(), pageable);
			}
			listaCliente = clienteRepository.findAll();
			listaUsuario = usuarioRepository.findAll();
		}

		model.addAttribute("asignacionPage", asignacionPage);
		model.addAttribute("listaCliente", listaCliente);
		model.addAttribute("listaUsuario", listaUsuario);
		model.addAttribute("id", obtenerUsuarioLogeadoId());
		return "asignaciones/index";
	}


	@GetMapping("/nuevo")
	String nuevo(Model model) {
		List<Cliente> listaCli = clienteRepository.findByClienteAsignacionNoNull();
		List<Usuario> listaUsu = usuarioRepository.findByNombresForTipo();

		model.addAttribute("asignacion", new Asignacion());
		model.addAttribute("listaCli", listaCli);
		model.addAttribute("listaUsu", listaUsu);
		model.addAttribute("id", obtenerUsuarioLogeadoId());
		return "asignaciones/formulario";
	}

	@PostMapping("/nuevo")
	String crear(@ModelAttribute Asignacion asignacion, BindingResult bindingResult, RedirectAttributes ra,
			Model model) {
		List<Cliente> listaCli = clienteRepository.findAll();
		List<Usuario> listaUsu = usuarioRepository.findAll();
		if (bindingResult.hasErrors()) {
			model.addAttribute("asignacion", asignacion);
			model.addAttribute("listaCli", listaCli);
			model.addAttribute("listaUsu", listaUsu);
			return "asignaciones/formulario";
		}
		Usuario user = new Usuario();
		user.setId(obtenerUsuarioLogeadoId());
		asignacion.setUserLogin(user);
		asignacionRepository.save(asignacion);
		ra.addFlashAttribute("msgExito", "La Asignacion ha sido creado correctamente");
		return "redirect:/admin/asignaciones";
	}

	@GetMapping("/{id}/editar")
	String editar(@PathVariable Integer id, Model model) {
		Asignacion asignacion = asignacionRepository.findById(id).get();
		
		List<Cliente> listaCli = clienteRepository.findAll();
		List<Usuario> listaUsu = usuarioRepository.findAll();
		model.addAttribute("asignacion", asignacion);
		model.addAttribute("listaCli", listaCli);
		model.addAttribute("listaUsu", listaUsu);

		return "asignaciones/formulario";
	}

	@PostMapping("/{id}/editar")
	String actualizar(@PathVariable Integer id, @ModelAttribute Asignacion asignacion, BindingResult bindingResult,
			RedirectAttributes ra, Model model) {

		Asignacion asignacionDB = asignacionRepository.findById(id).get();
		List<Cliente> listaCli = clienteRepository.findAll();
		List<Usuario> listaUsu = usuarioRepository.findAll();

		asignacionDB.setDescripcion(asignacion.getDescripcion());
		asignacionDB.setObservaciones(asignacion.getObservaciones());
		asignacionDB.setEstado(asignacion.getEstado());
		asignacionDB.setCliente(asignacion.getCliente());
		asignacionDB.setUsuario(asignacion.getUsuario());

		if (bindingResult.hasErrors()) {
			model.addAttribute("asignacion", asignacion);
			model.addAttribute("listaCli", listaCli);
			model.addAttribute("listaUsu", listaUsu);
			return "asignaciones/formulario";
		}

		Usuario user = new Usuario();
		user.setId(obtenerUsuarioLogeadoId());
		asignacionDB.setUserLogin(user);

		asignacionRepository.save(asignacionDB);

		ra.addFlashAttribute("msgExito", "La Asignacion ha sido actualizado correctamente");

		return "redirect:/admin/asignaciones";
	}

	@PostMapping("/{id}/eliminar")
	String eliminar(@PathVariable Integer id, RedirectAttributes ra) {

		Asignacion asignacionDB = asignacionRepository.findById(id).get();

		asignacionRepository.delete(asignacionDB);
		ra.addFlashAttribute("msgExito", "La Asignacion ha sido eliminado correctamente");
		return "redirect:/admin/asignaciones";

	}

	@RequestMapping("/{id}/detalles")
	String detlles(@PathVariable Integer id, Model model, RedirectAttributes ra) {

		Asignacion asignacion = asignacionRepository.findById(id).get();

		if (asignacion == null) {
			ra.addFlashAttribute("msgExito", "La Asignacion no tiene detalles");
			return "redirect:/admin/asignaciones";
		}
		model.addAttribute("asignacion", asignacion);
		return "master";

	}

	@ModelAttribute
	public void setGenericos(Model model) {

		model.addAttribute("dtuser", usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}

}

package mac.inmobiliaria.controller;

import mac.inmobiliaria.model.*;
import mac.inmobiliaria.repository.AsignacionRepository;
import mac.inmobiliaria.repository.DatosRepository;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/datos")
public class DatosControllerAsesor {

	@Autowired
	private DatosRepository datosRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private AsignacionRepository srvAsignacion;

	@GetMapping()
	public String index(Model model, 
			@RequestParam(required = false) String nombres,
			@PageableDefault(size = 3) Pageable pageable) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(username);
		Page<Datos> datosPage;

		String Rol = String.valueOf(usuarioEncontrado.get().getRol());

		if (Rol.compareTo("VENDEDOR") == 0) {
			if (StringUtils.hasText(nombres)) {
				datosPage = datosRepository.findByNombresAndEstadoAndIdUsuario(nombres, 1,usuarioEncontrado.get().getId(), pageable);
			} else {
				datosPage = datosRepository.findByEstadoAndIdUsuario(1, usuarioEncontrado.get().getId(), pageable);

			}
		} else {

			if (StringUtils.hasText(nombres)) {
				datosPage = datosRepository.findByNombresAndEstado(nombres, 1, pageable);
			} else {
				datosPage = datosRepository.findByEstado(1, pageable);

			}
		}

		model.addAttribute("datosPage", datosPage);
		model.addAttribute("id", obtenerUsuarioLogeadoId());
		return "datos/index";
	}

	@GetMapping("/buscador")
	public String getBuscarCliente(Model model, @RequestParam(required = false) String nombres,
			@PageableDefault(size = 3) Pageable pageable) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(username);
		Page<Datos> datosPage;

		String Rol = String.valueOf(usuarioEncontrado.get().getRol());

		if (Rol.compareTo("VENDEDOR") == 0) {
			if (StringUtils.hasText(nombres)) {
				datosPage = datosRepository.findByNombresAndEstadoAndIdUsuario(nombres, 0,usuarioEncontrado.get().getId(), pageable);
			} else {
				datosPage = datosRepository.findByEstadoAndIdUsuario(0, usuarioEncontrado.get().getId(), pageable);

			}
		} else {

			if (StringUtils.hasText(nombres)) {
				datosPage = datosRepository.findByNombresAndEstado(nombres, 0, pageable);
			} else {
				datosPage = datosRepository.findByEstado(0, pageable);

			}
		}
		model.addAttribute("datosPage", datosPage);
		model.addAttribute("id", obtenerUsuarioLogeadoId());
		return "datos/buscador";
	}

	@GetMapping("/nuevo")
	String nuevo(Model model) {

		model.addAttribute("datos", new Datos());
		model.addAttribute("id", obtenerUsuarioLogeadoId());
		return "datos/formulario";
	}

	@PostMapping("/nuevo")
	String crear(@ModelAttribute @Validated Datos datos, 
			BindingResult bindingResult, 
			RedirectAttributes ra,
			Model model) {
		
		String telefono = datos.getTelefono();
        boolean telefonoYaExiste = datosRepository.existsByTelefono(telefono);

        if (telefonoYaExiste) {
            bindingResult.rejectValue("telefono", "TelefonoAlreadyExists");
        }else {
            if(telefono.isEmpty()){
                bindingResult.rejectValue("telefono", "TelefonoNotBlank");
            }else {
                if (!telefono.matches(("[0-9]*")) || telefono.length()<0 || telefono.length()>9){
                    bindingResult.rejectValue("telefono", "TelefonoNotNumeric");
                } else {
                    if (telefono.length()<9){
                        bindingResult.rejectValue("telefono", "TelefonoNotNumericthenine");
                    }
                }
            }
        }

		if (bindingResult.hasErrors()) {
			model.addAttribute("datos", datos);
			return "datos/formulario";
		}
		
    	
		//System.err.println(obtenerUsuarioLogeadoId());
		datos.setEstado(1);
		datos.setIdUsuario(obtenerUsuarioLogeadoId());
		datosRepository.save(datos);
		ra.addFlashAttribute("msgExitoDatos", "Los Datos del Cliente ha sido creado correctamente");

		return "redirect:/proforma";
	}

	@GetMapping("/{id}/editar")
	String editar(@PathVariable Integer id, Model model) {
		Datos datos = datosRepository.findById(id).get();
		
		model.addAttribute("datos", datos);
		model.addAttribute("id", obtenerUsuarioLogeadoId());
		return "datos/formulario";
	}

	@PostMapping("/{id}/editar")
	String actualizar(@PathVariable Integer id, @ModelAttribute @Validated Datos datos,
			BindingResult bindingResult,
			RedirectAttributes ra, 
			Model model) {

		Datos datosDB = datosRepository.findById(id).get();
		
		if(!datosDB.getTelefono().equals(datos.getTelefono())) {
			System.err.println("Hola");
            String telefono = datos.getTelefono();
            boolean telefonoYaExiste = datosRepository.existsByTelefono(telefono);

            if (telefonoYaExiste) {
                bindingResult.rejectValue("telefono", "TelefonoAlreadyExists");
            }else {
                if(telefono.isEmpty()){
                    bindingResult.rejectValue("telefono", "TelefonoNotBlank");
                }else {
                    if (!telefono.matches(("[0-9]*")) || telefono.length()<0 || telefono.length()>9){
                    	System.err.println("Hola DDDD");
                        bindingResult.rejectValue("telefono", "TelefonoNotNumeric");
                    } else if(telefono.length()<9) {
                    	System.err.println("Hola");
                    	bindingResult.rejectValue("telefono", "TelefonoNotNumericthenine");
                    }
                }
            }
        }

		datosDB.setNombres(datos.getNombres());
		datosDB.setTelefono(datos.getTelefono());
		datosDB.setDni(datos.getDni());
		datosDB.setEmail(datos.getEmail());
		datosDB.setEstadoCivil(datos.getEstadoCivil());
		datosDB.setLugarLaboral(datos.getLugarLaboral());
		datosDB.setNombresConyu(datos.getNombresConyu());
		datosDB.setDniConyu(datos.getDniConyu());
		
		


		if (bindingResult.hasErrors()) {
			model.addAttribute("datos", datos);
			model.addAttribute("id", obtenerUsuarioLogeadoId());

			return "datos/formulario";
		}
		
		datosDB.setEstado(1);
		
		datosRepository.save(datosDB);
		ra.addFlashAttribute("msgExito", "Los Datos del Cliente ha sido actualizado correctamente");
		return "redirect:/datos";
	}

	@PostMapping("/{id}/eliminar")
	String eliminar(@PathVariable Integer id, RedirectAttributes ra, Model model) {

		Datos datosDB = datosRepository.findById(id).get();

		// List<Proforma> relacionDatosConProforma =
		// proformaRepository.findByAllDatos(id);
//
		// if(relacionDatosConProforma.size()>0){
		// ra.addFlashAttribute("msgError", "Los Datos del Cliente no se ha eliminado,
		// mantiene relacion con Proforma");
		// return "redirect:/datos";
		// }
		// datosRepository.delete(datosDB);
		datosDB.setEstado(0);
		datosRepository.save(datosDB);
		ra.addFlashAttribute("msgExito", "El Cliente se ha deshabilitado correctamente");
		return "redirect:/datos/buscador";

	}

	@PostMapping("/{id}/habilitar")
	String postHabilitarDatos(@PathVariable Integer id, RedirectAttributes ra, Model model) {

		Datos datosDB = datosRepository.findById(id).get();

		// List<Proforma> relacionDatosConProforma =
		// proformaRepository.findByAllDatos(id);
//
		// if(relacionDatosConProforma.size()>0){
		// ra.addFlashAttribute("msgError", "Los Datos del Cliente no se ha eliminado,
		// mantiene relacion con Proforma");
		// return "redirect:/datos";
		// }
		// datosRepository.delete(datosDB);
		datosDB.setEstado(1);
		datosRepository.save(datosDB);
		ra.addFlashAttribute("msgExito", "El Cliente se ha habilitado correctamente");
		return "redirect:/datos";

	}
	
	@GetMapping("/transpasarclientes")
	String transpasarClientes(Model model) {
		List<Usuario> accesoresEntrantes = usuarioRepository.findByRolAndIdNot(Usuario.Rol.VENDEDOR,obtenerUsuarioLogeadoId());
		List<Asignacion> listaclientes = srvAsignacion.findByAllUsuario(obtenerUsuarioLogeadoId());
		System.err.println(listaclientes);
		model.addAttribute("idlogin", obtenerUsuarioLogeadoId());
		model.addAttribute("listaclientes", listaclientes);
		model.addAttribute("accesorentrante", accesoresEntrantes);
		return "clientes/transferir";
	}

	@ModelAttribute
	public void setGenericos(Model model) {

		model.addAttribute("dtuser", usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}

}

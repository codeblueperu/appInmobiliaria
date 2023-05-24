package mac.inmobiliaria.controller;

import java.util.List;

import mac.inmobiliaria.model.Cliente;
import mac.inmobiliaria.model.Ubigeo;
import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.AsignacionRepository;
import mac.inmobiliaria.repository.ClienteRepository;
import mac.inmobiliaria.repository.UbigeoRepository;
import mac.inmobiliaria.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.websocket.server.PathParam;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;

@Controller
@RequestMapping("/admin/clientes")
public class ClienteControllerAdmin {
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private UbigeoRepository ubigeoRepository;

	@Autowired
	private AsignacionRepository asignacionRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping
	public String index(Model model, 
			@RequestParam(required = false) String tipo,
			@RequestParam(required = false) String nombres, 
			@RequestParam(required = false) String distrito,
			@RequestParam(required = false) String telefono, 
			@PageableDefault(size = 5) Pageable pageable) {

		Page<Cliente> clientePage;
		List<Ubigeo> listaUbigeo;

		Usuario user = usuarioRepository.findById(obtenerUsuarioLogeadoId()).get();
		String Rol = String.valueOf(user.getRol());

		if (StringUtils.hasText(nombres)) {
			if (Rol.compareTo("ADMIN") == 0 && user.getPreinstall() == 1) {
				clientePage = clienteRepository.findByNombreContainingAndEstado(nombres,1, pageable);
			} else {
				clientePage = clienteRepository.findByNombreContainingAndUsuarioIdAndEstado(nombres,1, user.getId(), pageable);
			}

			listaUbigeo = ubigeoRepository.findByDistrito(distrito);
		} else if (StringUtils.hasText(tipo)) {
			if (Rol.compareTo("ADMIN") == 0 && user.getPreinstall() == 1) {
				clientePage = clienteRepository.findByTipoAndEstado(tipo,1, pageable);
			} else {
				clientePage = clienteRepository.findByTipoAndUsuarioIdAndEstado(tipo,1,user.getId(),pageable);
			}
			
			listaUbigeo = ubigeoRepository.findAll();
		} else {
			if (Rol.compareTo("ADMIN") == 0 && user.getPreinstall() == 1) {
				clientePage = clienteRepository.findByEstado(1, pageable);
			} else {
				clientePage = clienteRepository.findByEstadoAndUsuarioId(1, user.getId(), pageable);
			}
			listaUbigeo = ubigeoRepository.findAll();
		}
		model.addAttribute("clientePage", clientePage);
		model.addAttribute("listaUbigeo", listaUbigeo);
		model.addAttribute("id", obtenerUsuarioLogeadoId());
		return "clientes/index";
	}

	@GetMapping("/buscador")
	public String index(Model model, 
			@RequestParam(required = false) String tipo,
			@RequestParam(required = false) String nombres, 
			@RequestParam(required = false) String distrito,
			@PageableDefault(size = 5) Pageable pageable) {
		Page<Cliente> clientePage;
		List<Ubigeo> listaUbigeo;

		Usuario user = usuarioRepository.findById(obtenerUsuarioLogeadoId()).get();
		String Rol = String.valueOf(user.getRol());

		
		if (Rol.compareTo("ADMIN") == 0 && user.getPreinstall() == 1) {
			if (StringUtils.hasText(nombres)) {
				clientePage = clienteRepository.findByNombreContainingAndEstado(nombres,0, pageable);
				listaUbigeo = ubigeoRepository.findAll();
			} else if (StringUtils.hasText(tipo)) {
				clientePage = clienteRepository.findByTipoAndEstado(tipo,0, pageable);
				listaUbigeo = ubigeoRepository.findAll();
			} else {
				clientePage = clienteRepository.findByEstado(0, pageable);
				listaUbigeo = ubigeoRepository.findAll();
			}
		}else {
			clientePage = clienteRepository.findByEstado(0, pageable);
			listaUbigeo = ubigeoRepository.findAll();
		}
		model.addAttribute("clientePage", clientePage);
		model.addAttribute("listaUbigeo", listaUbigeo);
		model.addAttribute("id", obtenerUsuarioLogeadoId());
		return "clientes/buscador";
	}

	@GetMapping("/nuevo")
	String nuevo(Model model) {
		List<Ubigeo> listaUbi = ubigeoRepository.findAll();

		model.addAttribute("cliente", new Cliente());
		model.addAttribute("listaUbi", listaUbi);
		model.addAttribute("id", obtenerUsuarioLogeadoId());
		return "clientes/formulario";
	}

	@PostMapping("/nuevo")
	String crear(@ModelAttribute @Validated Cliente cliente, BindingResult bindingResult, RedirectAttributes ra,
			Model model) {
		List<Ubigeo> listaUbi = ubigeoRepository.findAll();

		if (bindingResult.hasErrors()) {
			model.addAttribute("cliente", cliente);
			model.addAttribute("listaUbi", listaUbi);
			return "clientes/formulario";
		}
		
		Usuario userLogin = new Usuario();
		userLogin.setId(obtenerUsuarioLogeadoId());
		cliente.setUsuario(userLogin);
		cliente.setEstado(1);
		clienteRepository.save(cliente);
		
		ra.addFlashAttribute("msgExito", "El cliente ha sido creado correctamente");
		return "redirect:/admin/clientes";
	}

	@GetMapping("/{id}/editar")
	String editar(@PathVariable Integer id, Model model) {
		Cliente cliente = clienteRepository.findById(id).get();
		List<Ubigeo> listaUbi = ubigeoRepository.findAll();
		
		 
		model.addAttribute("idDepartamento", cliente.getDistrito().getProvincia().getDepartamento().getId());
		model.addAttribute("idProvincia", cliente.getDistrito().getProvincia().getId());
		model.addAttribute("idDistrito", cliente.getDistrito().getId());
		
		model.addAttribute("cliente", cliente);
		model.addAttribute("listaUbi", listaUbi);
		model.addAttribute("id", obtenerUsuarioLogeadoId());
		return "clientes/formulario";
	}

	@PostMapping("/{id}/editar")

	String actualizar(@PathVariable Integer id, @ModelAttribute @Validated Cliente cliente, BindingResult bindingResult,
			RedirectAttributes ra, Model model) {

		Cliente clienteDB = clienteRepository.findById(id).get();
		List<Ubigeo> listaUbi = ubigeoRepository.findAll();

		clienteDB.setNombre(cliente.getNombre());
		clienteDB.setDni(cliente.getDni());
		clienteDB.setCorreo(cliente.getCorreo());
		clienteDB.setTelefono(cliente.getTelefono());// Esto no reconoce cuando le doy al boton actualizar
		clienteDB.setTipo(cliente.getTipo());
		clienteDB.setProyecto(cliente.getProyecto());
		clienteDB.setCreadate(cliente.getCreadate());
		clienteDB.setDireccion(cliente.getDireccion());
		clienteDB.setDistrito(cliente.getDistrito());// Esto no reconoce cuando le doy al boton actualizar

		if (bindingResult.hasErrors()) {// PONER UN MENSAJE DE ERROR COMO ELMSGEXITO
			model.addAttribute("cliente", cliente);
			model.addAttribute("listaUbi", listaUbi);
			return "clientes/formulario";
		}
		clienteDB.setEstado(1);
		clienteRepository.save(clienteDB);

		ra.addFlashAttribute("msgExito", "El Cliente ha sido actualizado correctamente");

		return "redirect:/admin/clientes";
	}

	@PostMapping("/{id}/eliminar")
	String eliminar(@PathVariable Integer id, RedirectAttributes ra, Model model) {

		Cliente clienteDB = clienteRepository.findById(id).get();

		// f(clienteDB!=null){
		// Cliente cliente = entityManager.find(Cliente.class, id);
		// entityManager.detach(cliente);
		// ra.addFlashAttribute("msgError", "El Cliente se ha desvinculado de tu
		// registro");
		//

//        List<Asignacion> encontradoRelacionClienteConAsignacion = asignacionRepository.findByAllCliente(id);
//        if(encontradoRelacionClienteConAsignacion.size()>0){
//            ra.addFlashAttribute("msgError", "El Cliente no se puede eliminar, mantiene relacion con alguna Asignacion");
//            return "redirect:/admin/clientes";
//        }

		// clienteRepository.delete(clienteDB);
		clienteDB.setEstado(0);
		clienteRepository.save(clienteDB);
		ra.addFlashAttribute("msgExito", "El Cliente se ha eliminado correctamente");

		return "redirect:/admin/clientes";

	}

	@PostMapping("/{id}/habilitar")
	String habilitarClienteID(@PathVariable Integer id, RedirectAttributes ra, Model model) {

		Cliente clienteDB = clienteRepository.findById(id).get();

		clienteDB.setEstado(1);
		clienteRepository.save(clienteDB);
		ra.addFlashAttribute("msgExito", "Cliente habilitado correctamente");

		return "redirect:/admin/clientes";

	}
	/**
	 * <form action="/transpasarclientes" method="post">
	 * <select name="idsaliente">
	 * <option th></option>
	 * 
	 * 
	 * 
	 * <select id="usuarioDestinoId" name="usuarioDestinoId">
            <option value="">Seleccione un usuario de destino</option>
            <!-- Aquí se generan las opciones de los usuarios -->
            <option th:each="usuario : ${usuarios}" th:value="${usuario.id}" th:text="${usuario.nombre}"></option>
        </select>
        
	 * </select>
	 * </form>
	 * @param idsaliente
	 * @param identrante
	 * @param ra
	 * @param model
	 * @return
	 */
	
	@GetMapping("/transpasarclientes")
	String transpasarClientes(Model model) {
		List<Usuario> listaUsu = usuarioRepository.findNombreCompletoByRol(Usuario.Rol.ADMIN);

		model.addAttribute("listaUsu", listaUsu);
		return "clientes/transferir";
	}
	
	@PostMapping("/transpasarclientes")
	String transpasarClientes(@RequestParam(name="idsaliente") Integer idsaliente,
			@RequestParam(value="identrante") Integer identrante, RedirectAttributes ra, Model model) {

		
		List<Cliente> lista = clienteRepository.findByUsuarioId(idsaliente);
		for (Cliente cliente : lista) {			
			cliente.setUsuario(new Usuario(identrante));
			clienteRepository.save(cliente);
		}

		//clienteDB.setEstado(1);
		//clienteRepository.save(clienteDB);
		ra.addFlashAttribute("msgExito", "La Tranferencia de Cliente fue exitoso!");

		return "redirect:/admin/clientes";

	}
	

	@ModelAttribute
	public void setGenericos(Model model) {
		
		model.addAttribute("dtuser",  usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}
}

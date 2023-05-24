package mac.inmobiliaria.controller;

import mac.inmobiliaria.model.Cliente;
import mac.inmobiliaria.model.Ubigeo;
import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.ClienteRepository;
import mac.inmobiliaria.repository.UbigeoRepository;
import mac.inmobiliaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;

@Controller
@RequestMapping("/admin/administradores")
public class AdministradorControllerAdmin {

    @Autowired
    private UbigeoRepository ubigeoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String index(Model model,
                        @RequestParam(required = false) String nombreCompleto,
                        @RequestParam(required = false) String sexo,
                        @PageableDefault(size = 5) Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Page<Usuario> usuarioPage;

        if (StringUtils.hasText(nombreCompleto)) {
            usuarioPage = usuarioRepository
                    .findByRolAndNombreCompletoContaining(Usuario.Rol.ADMIN, nombreCompleto, pageable);
        } else {
            usuarioPage = usuarioRepository.findByRol(Usuario.Rol.ADMIN,pageable);
        }
        
        Usuario datosLogin = usuarioRepository.findByEmail(username).get();
        
        model.addAttribute("sexo", sexo);
        model.addAttribute("username", username);
        model.addAttribute("usuarioPage", usuarioPage);
        model.addAttribute("datalogin", datosLogin);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "administradores/index";
    }
    @GetMapping("/buscador")
    public String index(Model model,
                        @RequestParam(required = false) String nombreCompleto,
                        @PageableDefault(size = 5) Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Page<Usuario> usuarioPage;

        if (StringUtils.hasText(nombreCompleto)) {
            usuarioPage = usuarioRepository
                    .findByRolAndNombreCompletoContaining(Usuario.Rol.ADMIN, nombreCompleto, pageable);
        } else {
            usuarioPage = usuarioRepository.findByRol(Usuario.Rol.ADMIN,pageable);
        }


        Usuario datosLogin = usuarioRepository.findByEmail(username).get();
        model.addAttribute("username", username);
        model.addAttribute("usuarioPage", usuarioPage);
        model.addAttribute("datalogin", datosLogin);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "administradores/buscador";
    }



    @GetMapping("/nuevo")
    String nuevo(Model model) {
        List<Ubigeo> listaUbi = ubigeoRepository.findAll();

        model.addAttribute("usuario", new Usuario());
        model.addAttribute("listaUbi", listaUbi);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "administradores/registro";
    }


    @PostMapping("/nuevo")
    ModelAndView crear(@Validated Usuario usuario,
    		BindingResult bindingResult,
    		RedirectAttributes ra) {
    	
    	List<Ubigeo> listaUbi = ubigeoRepository.findAll();
    	
    	if (bindingResult.hasErrors()) {
            return new ModelAndView("administradores/registro")
                    .addObject("listaUbi", listaUbi)
                    .addObject("usuario", usuario)
                    .addObject("id", obtenerUsuarioLogeadoId());
        }
        // validar si el telefono ya está siendo usado
        String telefono = usuario.getTelefono();
        boolean telefonoYaExiste = usuarioRepository.existsByTelefono(telefono);

        if (telefonoYaExiste) {
            bindingResult.rejectValue("telefono", "TelefonoAlreadyExists");
        }else {
            if(telefono.isEmpty()){
                bindingResult.rejectValue("telefono", "TelefonoNotBlank");
            }else {
                    if (telefono.length()<9){
                        bindingResult.rejectValue("telefono", "TelefonoNotNumericthenine");
                    }
            }
        }

        // validar si el email ya está siendo usado
        String email = usuario.getEmail();
        boolean usuarioYaExiste = usuarioRepository.existsByEmail(email);

        if (usuarioYaExiste) {
            bindingResult.rejectValue("email", "EmailAlreadyExists");
        }

        if(usuario.getPassword1().isEmpty()){
            bindingResult.rejectValue("password1", "PasswordNotBlank");
        }
        if(usuario.getPassword2().isEmpty()){
            bindingResult.rejectValue("password2", "PasswordNotBlank");
        }

        // validar si las contraseñas coinciden
        if (!usuario.getPassword1().equals(usuario.getPassword2())) {
            bindingResult.rejectValue("password1", "PasswordNotEqualss");
        }


        String sexo = usuario.getSexo();
        if(sexo.isEmpty()){
            bindingResult.rejectValue("sexo", "SexoNotBlank");
        }
        String ubigeo = String.valueOf(usuario.getDistrito());
        if(ubigeo.isEmpty()){
            bindingResult.rejectValue("ubigeo", "UbigeoIsNull");
        }

        if (bindingResult.hasErrors()) {
            return new ModelAndView("administradores/registro")
                    .addObject("listaUbi", listaUbi)
                    .addObject("usuario", usuario)
                    .addObject("id", obtenerUsuarioLogeadoId());
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword1()));
        usuario.setRol(Usuario.Rol.ADMIN);
        usuarioRepository.save(usuario);

        ra.addFlashAttribute("msgExito", "Se ha creado el Administrador con éxito");

        return new ModelAndView("redirect:/admin/administradores");
    }

    @GetMapping("/{id}/editar")
    String editar(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .get();
        List<Ubigeo> listaUbi = ubigeoRepository.findAll();
        
        model.addAttribute("idDepartamento", usuario.getDistrito().getProvincia().getDepartamento().getId());
		model.addAttribute("idProvincia", usuario.getDistrito().getProvincia().getId());
		model.addAttribute("idDistrito", usuario.getDistrito().getId());
		
        model.addAttribute("usuario", usuario);
        model.addAttribute("listaUbi", listaUbi);
        model.addAttribute("id", obtenerUsuarioLogeadoId());

        return "administradores/registro";
    }

    @PostMapping("/{id}/editar")
    String actualizar(@PathVariable Integer id, @ModelAttribute @Validated Usuario usuario,

                      BindingResult bindingResult,
                      RedirectAttributes ra,
                      Model model) {


        Usuario usuarioDB = usuarioRepository.findById(id).get();
        List<Ubigeo> listaUbi = ubigeoRepository.findAll();

        // validar si el telefono ya está siendo usado

        if(!usuarioDB.getTelefono().equals(usuario.getTelefono())) {
            String telefono = usuario.getTelefono();
            boolean telefonoYaExiste = usuarioRepository.existsByTelefono(telefono);

            if (telefonoYaExiste) {
                bindingResult.rejectValue("telefono", "TelefonoAlreadyExists");
            }else {
                if(telefono.isEmpty()){
                    bindingResult.rejectValue("telefono", "TelefonoNotBlank");
                }else {
                	if(telefono.length()<9) {
                    	bindingResult.rejectValue("telefono", "TelefonoNotNumericthenine");
                    }
                }
            }
        }
        // validar si el email ya está siendo usado
        if(!usuarioDB.getEmail().equals(usuario.getEmail())) {
            String email = usuario.getEmail();
            boolean usuarioYaExiste = usuarioRepository.existsByEmail(email);

            if (usuarioYaExiste) {
                bindingResult.rejectValue("email", "EmailAlreadyExists");
            }else {
                if(email.isEmpty()){
                    bindingResult.rejectValue("email", "EmailNotBlank");
                }
            }
        }
        

        usuarioDB.setNombres(usuario.getNombres());
        usuarioDB.setApellidos(usuario.getApellidos());
        usuarioDB.setTelefono(usuario.getTelefono());
        usuarioDB.setSexo(usuario.getSexo());
        usuarioDB.setEmail(usuario.getEmail());
        
        Usuario exist = usuarioRepository.findById(usuario.getId()).get();
		if(usuario.getPassword1().isEmpty() && usuario.getPassword2().isEmpty()) {
			usuario.setPassword1(exist.getPassword1());
			usuario.setPassword2(exist.getPassword2());
			usuario.setPreinstall(1);
		}else {
			
			usuarioDB.setPassword(passwordEncoder.encode(usuario.getPassword1()));
	        usuarioDB.setPassword(passwordEncoder.encode(usuario.getPassword2()));
	        

			// validar si las contraseñas coinciden
	        if (!usuario.getPassword1().equals(usuario.getPassword2())) {
	            bindingResult.rejectValue("password1", "PasswordNotEqualss");
	        }
		}
       
        
        usuarioDB.setDistrito(usuario.getDistrito());

        
        if(usuarioDB.getNombres().equals(usuario.getNombres())) {
        	String nombres = usuario.getNombres();
            boolean esSoloLetras = usuarioRepository.soloLetras(nombres);
            if(nombres.isEmpty()){
                bindingResult.rejectValue("nombres", "NombresNotBlank");
            }else
            {
                if (!esSoloLetras){
                    bindingResult.rejectValue("nombres", "NombresNotLetras");
                }
            }
        }
        String nombres = usuario.getNombres();
        boolean esSoloLetras = usuarioRepository.soloLetras(nombres);
        if(nombres.isEmpty()){
            bindingResult.rejectValue("nombres", "NombresNotBlank");
        }else
        {
            if (!esSoloLetras){
                bindingResult.rejectValue("nombres", "NombresNotLetras");
            }
        }


        String apellidos = usuario.getApellidos();
        boolean esSoloLetrasApe = usuarioRepository.soloLetras(apellidos);
        if(apellidos.isEmpty()){
            bindingResult.rejectValue("apellidos", "ApellidosNotBlank");
        }else
        {
            if (!esSoloLetrasApe){
                bindingResult.rejectValue("apellidos", "ApellidosNotLetras");
            }
        }

        String sexo = usuario.getSexo();
        if(sexo.isEmpty()){
            bindingResult.rejectValue("sexo", "SexoNotBlank");
        }
        String rol = String.valueOf(usuario.getRol());
        if(rol.isEmpty()){
            bindingResult.rejectValue("rol", "RolNotBlank");
        }
        String ubigeo = String.valueOf(usuario.getDistrito());
        if(ubigeo.isEmpty()){
            bindingResult.rejectValue("ubigeo", "UbigeoNotBlank");
        }

        if(bindingResult.hasErrors()){
            usuario.setId(usuarioDB.getId());
            model.addAttribute("usuario", usuario);
            model.addAttribute("listaUbi", listaUbi);

            return "administradores/registro";
        }
        usuarioRepository.save(usuarioDB);

        ra.addFlashAttribute("msgExito", "El Administrador ha sido actualizado correctamente");

        return "redirect:/admin/administradores";
    }

    @PostMapping("/{id}/eliminar")
    String eliminar(@PathVariable Integer id, RedirectAttributes ra) {

        Usuario user = usuarioRepository.findById(obtenerUsuarioLogeadoId()).get();
        String Rol = String.valueOf(user.getRol());

        Usuario usuarioDB = usuarioRepository.findById(id).get();
//        List<Cliente> clienteDB = clienteRepository.findByUsuarioId(id);
//
//        
//            for (Cliente cliente : clienteDB) {
//                if (cliente.getEstado() == 1  && Rol.compareTo("ADMIN") == 0 && user.getPreinstall() == 0) {
//                    ra.addFlashAttribute("msgError", "El Administrador debe deshabilitar sus clientes a cargo antes de eliminarlo");
//                    return "redirect:/admin/administradores";
//                }
//            }
//
//            usuarioRepository.delete(usuarioDB);
//            ra.addFlashAttribute("msgExito", "El Administrador ha sido eliminado correctamente");
//            return "redirect:/admin/administradores";
//        }
        if(Rol.compareTo("ADMIN") == 0 && user.getPreinstall() == 1) {
        	ra.addFlashAttribute("msgError", "El Administrador no se puede eliminar, puedes modificar sus datos");
        	return "redirect:/admin/administradores";
        }
        
        usuarioRepository.delete(usuarioDB);
        ra.addFlashAttribute("msgExito", "El Administrador ha sido eliminado correctamente");
        return "redirect:/admin/administradores";
    }
    
    
    @ModelAttribute
	public void setGenericos(Model model) {
		
		model.addAttribute("dtuser",  usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}
}

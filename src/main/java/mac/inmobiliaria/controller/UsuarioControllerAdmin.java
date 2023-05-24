package mac.inmobiliaria.controller;

import java.util.List;

import javax.management.modelmbean.InvalidTargetObjectTypeException;

import mac.inmobiliaria.model.Asignacion;
import mac.inmobiliaria.model.Proforma;
import mac.inmobiliaria.model.Ubigeo;
import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.AsignacionRepository;
import mac.inmobiliaria.repository.ProformaRepository;
import mac.inmobiliaria.repository.UbigeoRepository;
import mac.inmobiliaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;
/*
 * AUTOR: JAMPIER RODRIGUEZ LAUREANO
 * CARGO: DESARROLLADOR FULLSTACK
 * LOCALIDAD: TRUJILLO,PERU,LA LIBERTAD
 * */

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioControllerAdmin {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UbigeoRepository ubigeoRepository;

    @Autowired
    private AsignacionRepository asignacionRepository;
    
    @Autowired
    private ProformaRepository proformaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping()
    public String index(Model model,
            @RequestParam(required = false) String nombreCompleto,
            @RequestParam(required = false) String distrito,
            @PageableDefault(size = 5) Pageable pageable) {

        Page<Usuario> usuarioPage;
        List<Ubigeo> listaUbigeo;

        if (StringUtils.hasText(nombreCompleto)) {
            usuarioPage = usuarioRepository.findByNombreCompletoContaining(nombreCompleto, pageable);
            listaUbigeo = ubigeoRepository.findByDistrito(distrito);
        } else {
            usuarioPage = usuarioRepository.findByRol(Usuario.Rol.VENDEDOR,pageable);
            listaUbigeo = ubigeoRepository.findAll();
        }
        
        
        model.addAttribute("usuarioPage", usuarioPage);
        model.addAttribute("listaUbigeo", listaUbigeo);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "usuarios/index";
    }

    @GetMapping("/buscador")
    public String index(Model model,
                        @RequestParam(required = false) String nombreCompleto,
                        @RequestParam(required = false) String distrito,
                        @RequestParam(required = false) String sexo,
                        @PageableDefault(size = 5) Pageable pageable) {

        Page<Usuario> usuarioPage;
        List<Ubigeo> listaUbigeo;

        if (StringUtils.hasText(nombreCompleto)) {
            usuarioPage = usuarioRepository.findByNombreCompletoContaining(nombreCompleto, pageable);
            listaUbigeo = ubigeoRepository.findByDistrito(distrito);
        } else {
            usuarioPage = usuarioRepository.findByRol(Usuario.Rol.VENDEDOR,pageable);
            listaUbigeo = ubigeoRepository.findAll();
        }

        model.addAttribute("usuarioPage", usuarioPage);
        model.addAttribute("listaUbigeo", listaUbigeo);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "usuarios/buscador";
    }

    @GetMapping("/nuevo")
    String nuevo(Model model) {
        List<Ubigeo> listaUbi = ubigeoRepository.findAll();

        model.addAttribute("usuario", new Usuario());
        model.addAttribute("listaUbi", listaUbi);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "usuarios/formulario";
    }

    @PostMapping("/nuevo")
    String crear(@ModelAttribute Usuario usuario,
            BindingResult bindingResult,
            RedirectAttributes ra,
            Model model) {

        List<Ubigeo> listaUbi = ubigeoRepository.findAll();
        // validar si el telefono ya está siendo usado
        String telefono = usuario.getTelefono();
        boolean telefonoYaExiste = usuarioRepository.existsByTelefono(telefono);

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

        // validar si el email ya está siendo usado
        String email = usuario.getEmail();
        boolean usuarioYaExiste = usuarioRepository.existsByEmail(email);

        if (usuarioYaExiste) {
            bindingResult.rejectValue("email", "EmailAlreadyExists");
        }else {
            if(email.isEmpty()){
                bindingResult.rejectValue("email", "EmailNotBlank");
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
        String password = usuario.getPassword();
        if(password.isEmpty()){
            bindingResult.rejectValue("password", "PasswordNotBlank");
        }

        String sexo = usuario.getSexo();
        if(sexo.isEmpty()){
            bindingResult.rejectValue("sexo", "SexoNotBlank");
        }
        String ubigeo = String.valueOf(usuario.getDistrito());
        if(ubigeo.isEmpty()){
            bindingResult.rejectValue("ubigeo", "UbigeoIsNull");
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("usuario", usuario);
            model.addAttribute("listaUbi", listaUbi);
            model.addAttribute("id", obtenerUsuarioLogeadoId());
            return "usuarios/formulario";
        }

        usuario.setRol(Usuario.Rol.VENDEDOR);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        ra.addFlashAttribute("msgExito", "El Asesor Comercial ha sido creado correctamente");
        return "redirect:/admin/usuarios";
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
        return "usuarios/formulario";
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
		if(usuario.getPassword().isEmpty()) {
			usuario.setPassword(exist.getPassword());
			usuario.setPreinstall(1);
		}else {
			usuarioDB.setPassword(passwordEncoder.encode(usuario.getPassword()));
		}
        usuarioDB.setDistrito(usuario.getDistrito());



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
            model.addAttribute("usuario", usuario);
            model.addAttribute("listaUbi", listaUbi);
            model.addAttribute("id", obtenerUsuarioLogeadoId());

            return "usuarios/formulario";
        }
        usuarioRepository.save(usuarioDB);

        ra.addFlashAttribute("msgExito", "El Asesor Comercial ha sido actualizado correctamente");

        return "redirect:/admin/usuarios";
    }

    @PostMapping("/{id}/eliminar")
    String eliminar(@PathVariable Integer id, RedirectAttributes ra) {



        Usuario usuarioDB =  usuarioRepository.findById(id).get();

        List<Asignacion> encontradoRelacionUsuarioConAsignacion = asignacionRepository.findByAllUsuario(id);
        List<Proforma> encontradoRelacionUsuarioConProforma= proformaRepository.findByAllProformasUsuario(id);
        

        if(encontradoRelacionUsuarioConAsignacion.size()>0 || encontradoRelacionUsuarioConProforma.size()>0){
            ra.addFlashAttribute("msgError", "El Asesor Comercial no se puede eliminar, mantiene relacion con algun Cliente/Asignacion o Proformas");
            return "redirect:/admin/usuarios";
        }
        usuarioRepository.delete(usuarioDB);
        ra.addFlashAttribute("msgExito", "El Asesor Comercial ha sido eliminado correctamente");
        return "redirect:/admin/usuarios";

    }
    
    
    @ModelAttribute
	public void setGenericos(Model model) {
		
		model.addAttribute("dtuser",  usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}
    
    
}

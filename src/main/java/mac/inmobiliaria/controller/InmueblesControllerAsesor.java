package mac.inmobiliaria.controller;


import mac.inmobiliaria.model.Inmueble;
import mac.inmobiliaria.model.Pago;
import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.InmuebleRepository;
import mac.inmobiliaria.repository.PagoRepository;
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
@RequestMapping("/inmuebles")
public class InmueblesControllerAsesor {


    @Autowired
    private InmuebleRepository inmuebleRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PagoRepository pagoRepository;


    @GetMapping()
    public String index(Model model,
                        @RequestParam(required = false) String departamento,
                        @PageableDefault(size = 3) Pageable pageable) {

//    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//
//        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(username);
         
        Page<Inmueble> inmueblePage;

        if (StringUtils.hasText(departamento)) {
            inmueblePage = inmuebleRepository.findByDepartamentoAndEstado(departamento,1, pageable);
        } else {
            inmueblePage = inmuebleRepository.findByEstado(1,pageable);

        }
        model.addAttribute("inmueblePage", inmueblePage);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "inmuebles/index";
    }
    
    @GetMapping("/buscador")
    public String index(Model model,
                        @RequestParam(required = false) String departamento,
                        @RequestParam(required = false) String proyecto,
                        @PageableDefault(size = 3) Pageable pageable) {

    	Usuario user = usuarioRepository.findById(obtenerUsuarioLogeadoId()).get();
		String Rol = String.valueOf(user.getRol());
		
        Page<Inmueble> inmueblePage;

        if (Rol.compareTo("ADMIN") == 0 && user.getPreinstall() == 1) {
        	if (StringUtils.hasText(departamento)) {
                inmueblePage = inmuebleRepository.findByDepartamentoAndEstado(departamento,0, pageable);
            } else {
                inmueblePage = inmuebleRepository.findByEstado(0,pageable);

            }
            model.addAttribute("inmueblePage", inmueblePage);
            model.addAttribute("id", obtenerUsuarioLogeadoId());
            return "inmuebles/buscador";
        } else {
        	if (StringUtils.hasText(departamento)) {
                inmueblePage = inmuebleRepository.findByDepartamentoAndEstado(departamento,1, pageable);
            } else {
                inmueblePage = inmuebleRepository.findByEstado(1,pageable);

            }
        }
        model.addAttribute("inmueblePage", inmueblePage);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "inmuebles/index";
    }


    @GetMapping("/nuevo")
    String nuevo(Model model) {

        model.addAttribute("inmueble", new Inmueble());
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "inmuebles/formulario";
    }

    @PostMapping("/nuevo")
    String crear(@ModelAttribute @Validated Inmueble inmueble,
                 BindingResult bindingResult,
                 RedirectAttributes ra,
                 Model model) {


        if (bindingResult.hasErrors()) {
            model.addAttribute("inmueble", inmueble);
            return "inmuebles/formulario";
        }

        inmueble.setEstado(1);
        inmuebleRepository.save(inmueble);
        ra.addFlashAttribute("msgExitoInmueble", "El Inmueble ha sido creado correctamente");

        return "redirect:/inmuebles";
    }

    @GetMapping("/{id}/editar")
    String editar(@PathVariable Integer id, Model model) {
        Inmueble inmuebles = inmuebleRepository.findById(id).get();

        model.addAttribute("inmueble", inmuebles);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "inmuebles/formulario";
    }

    @PostMapping("/{id}/editar")

    String actualizar(@PathVariable Integer id, @ModelAttribute @Validated Inmueble inmuebles,
                      BindingResult bindingResult,
                      RedirectAttributes ra,
                      Model model) {


        Inmueble inmuebleDB = inmuebleRepository.findById(id).get();

        inmuebleDB.setProyecto(inmuebles.getProyecto());
        inmuebleDB.setDepartamento(inmuebles.getDepartamento());
        inmuebleDB.setPrecio_Departamento(inmuebles.getPrecio_Departamento());
        inmuebleDB.setArea_Depa(inmuebles.getArea_Depa());
        inmuebleDB.setPrecio_Estaciona(inmuebles.getPrecio_Estaciona());
        inmuebleDB.setEstacionamiento(inmuebles.getEstacionamiento());
        inmuebleDB.setPrecio_Total(inmuebles.getPrecio_Total());
        inmuebleDB.setDeposito(inmuebles.getDeposito());

        if (bindingResult.hasErrors()) {
            model.addAttribute("inmueble", inmuebleDB);
            return "inmuebles/formulario";
        }

        if(inmuebleDB.getEstado()==1) {
        	inmuebleDB.setEstado(1);
            inmuebleRepository.save(inmuebleDB);
            ra.addFlashAttribute("msgExitoInmueble", "El Inmueble ha sido actualizado correctamente");
            return "redirect:/inmuebles";
        } else {
        	inmuebleDB.setEstado(0);
            inmuebleRepository.save(inmuebleDB);
            ra.addFlashAttribute("msgExitoInmueble2", "El Inmueble ha sido actualizado correctamente");
            return "redirect:/inmuebles/buscador";
        }
        
    	
    
        
    }


    @PostMapping("/{id}/eliminar")
    String eliminar(@PathVariable Integer id, RedirectAttributes ra, Model model) {

        Inmueble inmuebleDB = inmuebleRepository.findById(id).get();


//        List<Pago> relacionInmuebleConPago = pagoRepository.findByAllInmueble(id);
//
//        if(relacionInmuebleConPago.size()>0){
//            ra.addFlashAttribute("msgError", "El Inmueble no se ha eliminado, mantiene relacion con algún Pago/Cliente");
//            return "redirect:/inmuebles";
//        }
        inmuebleDB.setEstado(0);
        inmuebleRepository.save(inmuebleDB);
        ra.addFlashAttribute("msgExitoInmueble", "El Inmueble se ha deshabilitado correctamente");
        return "redirect:/inmuebles";

    }
    
    @PostMapping("/{id}/habilitar")
    String postHabilitarDatos(@PathVariable Integer id, RedirectAttributes ra, Model model) {

        Inmueble inmuebleDB = inmuebleRepository.findById(id).get();


//        List<Pago> relacionInmuebleConPago = pagoRepository.findByAllInmueble(id);
//
//        if(relacionInmuebleConPago.size()>0){
//            ra.addFlashAttribute("msgError", "El Inmueble no se ha eliminado, mantiene relacion con algún Pago/Cliente");
//            return "redirect:/inmuebles";
//        }
        
        inmuebleDB.setEstado(1);
        inmuebleRepository.save(inmuebleDB);
        ra.addFlashAttribute("msgExitoInmueble2", "El Inmueble se ha habilitado correctamente");
        return "redirect:/inmuebles/buscador";

    }
    
    @ModelAttribute
	public void setGenericos(Model model) {
		
		model.addAttribute("dtuser",  usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}
    
}

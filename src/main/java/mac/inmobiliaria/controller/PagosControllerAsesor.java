package mac.inmobiliaria.controller;


import mac.inmobiliaria.model.*;
import mac.inmobiliaria.repository.DatosRepository;
import mac.inmobiliaria.repository.InmuebleRepository;
import mac.inmobiliaria.repository.PagoRepository;
import mac.inmobiliaria.repository.ProformaRepository;
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

import java.util.List;
import java.util.Optional;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;

@Controller
@RequestMapping("/pagos")
public class PagosControllerAsesor {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private InmuebleRepository inmuebleRepository;

    @Autowired
    private DatosRepository datosRepository;
    
    @Autowired
    private ProformaRepository proformaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping()
    public String index(Model model,
                        @RequestParam(required = false) String dni,
                        @RequestParam(required = false) String departamento,
                        @RequestParam(required = false) String nombres,
                        @PageableDefault(size = 3) Pageable pageable) {

    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(username);
        
        
        Page<Pago> pagoPage;
        List<Inmueble> listaInmueble;
        List<Datos> listaDatos;

        String Rol = String.valueOf(usuarioEncontrado.get().getRol());
        
        if (Rol.compareTo("VENDEDOR") == 0) {
        	if (StringUtils.hasText(dni)) {
                pagoPage = pagoRepository.findByDatosDniAndIdUsuario(dni,usuarioEncontrado.get().getId(), pageable);
                listaInmueble = inmuebleRepository.findByDepartamento((departamento));
                listaDatos = datosRepository.findByNombres((nombres));
            } else {
            	pagoPage = pagoRepository.findByIdUsuario(usuarioEncontrado.get().getId(),pageable);
                listaInmueble = inmuebleRepository.findAll();
                listaDatos = datosRepository.findAll();
            }
        } else {
        	if (StringUtils.hasText(dni)) {
                pagoPage = pagoRepository.findByDatosDni(dni, pageable);
                listaInmueble = inmuebleRepository.findByDepartamento((departamento));
                listaDatos = datosRepository.findByNombres((nombres));
            } else {
                pagoPage = pagoRepository.findByIdUsuario(usuarioEncontrado.get().getId(),pageable);
                listaInmueble = inmuebleRepository.findAll();
                listaDatos = datosRepository.findAll();
            }
        }
        
        model.addAttribute("pagosPage", pagoPage);
        model.addAttribute("listaInmueble", listaInmueble);
        model.addAttribute("listaDatos", listaDatos);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "pagos/index";
    }


    @GetMapping("/nuevo")
    String nuevo(Model model) {

    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(username);
        
        List<Inmueble> listaInmueble = inmuebleRepository.findByEstado(1);
        List<Datos> listaDatos = datosRepository.findByIdUsuarioAndEstado(usuarioEncontrado.get().getId(),1);

        model.addAttribute("pagos", new Pago());
        model.addAttribute("listaInmueble", listaInmueble);
        model.addAttribute("listaDatos", listaDatos);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "pagos/formulario";
    }

    @PostMapping("/nuevo")
    String crear(@ModelAttribute @Validated Pago pagos,
                 BindingResult bindingResult,
                 RedirectAttributes ra,
                 Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pagos", pagos);
            return "pagos/formulario";
        }

        pagos.setIdUsuario(obtenerUsuarioLogeadoId());
        pagoRepository.save(pagos);
        ra.addFlashAttribute("msgExitoPago", "El Pago ha sido creado correctamente");
        
        return "redirect:/proforma";
    }

    @GetMapping("/{id}/editar")
    String editar(@PathVariable Integer id, Model model) {
        Pago pagos = pagoRepository.findById(id)
                .get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(username);
        
        List<Inmueble> listaInmueble = inmuebleRepository.findByEstado(1);
        List<Datos> listaDatos = datosRepository.findByIdUsuarioAndEstado(usuarioEncontrado.get().getId(),1);

        model.addAttribute("pagos", pagos);
        model.addAttribute("listaInmueble", listaInmueble);
        model.addAttribute("listaDatos", listaDatos);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "pagos/formulario";
    }

    @PostMapping("/{id}/editar")

    String actualizar(@PathVariable Integer id, @ModelAttribute @Validated Pago pagos,
                      BindingResult bindingResult,
                      RedirectAttributes ra,
                      Model model) {


        Pago pagoDB = pagoRepository.findById(id).get();
        List<Inmueble> listaInmueble = inmuebleRepository.findAll();
        List<Datos> listaDatos = datosRepository.findAll();

        pagoDB.setCuotaInicial(pagos.getCuotaInicial());
        pagoDB.setEdificio(pagos.getEdificio());
        pagoDB.setCreditoHipote(pagos.getCreditoHipote());
        pagoDB.setFechaCreHipo(pagos.getFechaCreHipo());
        pagoDB.setCuotaMensual(pagos.getCuotaMensual());
        pagoDB.setAnosCuoMen(pagos.getAnosCuoMen());
        pagoDB.setNumeroCuenta(pagos.getNumeroCuenta());
        pagoDB.setNombreBanco(pagos.getNombreBanco());
        pagoDB.setDatos(pagos.getDatos());
        pagoDB.setInmueble(pagos.getInmueble());

        if (bindingResult.hasErrors()) {
            model.addAttribute("pagos", pagoDB);
            model.addAttribute("listaInmueble", listaInmueble);
            model.addAttribute("listaDatos", listaDatos);

            ra.addFlashAttribute("msgError", "El Pago no ha sido actualizado correctamente");
            return "datos/formulario";
        }

        pagoRepository.save(pagoDB);
        ra.addFlashAttribute("msgExito", "El Pago ha sido actualizado correctamente");
        return "redirect:/pagos";
    }


    @PostMapping("/{id}/eliminar")
    String eliminar(@PathVariable Integer id, RedirectAttributes ra, Model model) {

        Pago pagoDB = pagoRepository.findById(id).get();


        List<Proforma> relacionPagosConProforma = proformaRepository.findByAllProformas(id);

        if(relacionPagosConProforma.size()>0){
            ra.addFlashAttribute("msgError", "El Pago no se ha eliminado, mantiene relacion con alguna Proforma");
            return "redirect:/pagos";
        }
        pagoRepository.delete(pagoDB);
        ra.addFlashAttribute("msgExito", "El Pago se ha eliminado correctamente");
        return "redirect:/pagos";

    }
    
    @ModelAttribute
	public void setGenericos(Model model) {
		
		model.addAttribute("dtuser",  usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}

}

package mac.inmobiliaria.controller;


import mac.inmobiliaria.model.*;
import mac.inmobiliaria.repository.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Optional;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;

@Controller
@RequestMapping("/proforma")
public class ProformaControllerAsesor {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PagoRepository pagoRepository;
    
    @Autowired
    private DatosRepository datosRepository ;

    @Autowired
    private ProformaRepository proformaRepository;



    @GetMapping
    public String index(Model model,
                        @RequestParam(required = false) String dni,
                        @RequestParam(required = false) String nombreBanco,
                        @RequestParam(required = false) String nombreCompleto,
                        @PageableDefault(size = 3) Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(username);

        Page<Proforma> proformaPage;
        List<Pago> listaPago;
        List<Usuario> listaUsuario;

        if(StringUtils.hasText(dni)){
            proformaPage = proformaRepository.findByPagoDatosDni(dni,pageable);
            listaPago = pagoRepository.findByNombreBanco(nombreBanco);
            listaUsuario = usuarioRepository.findByNombres(nombreCompleto);
        } else {
            proformaPage = proformaRepository.findByAllProforma(usuarioEncontrado.get().getId(), pageable);
            listaPago = pagoRepository.findAll();
            listaUsuario = usuarioRepository.findAll();
        }



        model.addAttribute("proformaPage", proformaPage);
        model.addAttribute("listaPago", listaPago);
        model.addAttribute("listaUsuario", listaUsuario);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "proformas/index";
    }

    @GetMapping("/nuevo")
    String nuevo(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(username);

        List<Datos> listaDatos = datosRepository.findAll();
        System.err.println(listaDatos);
        String datosId = null;
        if (listaDatos.size() > 0) {
           Datos datos = listaDatos.get(0); // Obtén el primer objeto Datos de la lista
            datosId = datos.getNombres();
        }
        	
        //QUIERO QUE SE MUESTRE EN EL INPUT LOS NOMBRES DE LOS CLIENTES 
        List<Pago> listaPago = pagoRepository.findAllByDatosNombresAndIdUsuario(datosId,usuarioEncontrado.get().getId());
        List<Usuario> listaUsuario = usuarioRepository.findNombreCompletoByRolAndEmail(username);
        model.addAttribute("listaUsuario", listaUsuario);
        model.addAttribute("proforma", new Proforma());
        model.addAttribute("listaPago", listaPago);

        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "proformas/formulario";
    }

    @PostMapping("/nuevo")
    String crear(@ModelAttribute Proforma proforma,
                 BindingResult bindingResult,
                 RedirectAttributes ra,
                 Model model) {
    	
        if (bindingResult.hasErrors()) {
            model.addAttribute("proforma", proforma);
            return "proformas/formulario";
        }

        proformaRepository.save(proforma);
        ra.addFlashAttribute("msgExito", "La Proforma ha sido creado correctamente");
        return "redirect:/proforma";
    }

    @GetMapping("/{id}/editar")
    String editar(@PathVariable Integer id, Model model) {
        Proforma proforma = proformaRepository.findById(id)
                .get();
        List<Pago> listaPago = pagoRepository.findAll();;
        List<Usuario> listaUsuario = usuarioRepository.findAll();

        model.addAttribute("proforma", proforma);
        model.addAttribute("listaPago", listaPago);
        model.addAttribute("listaUsuario", listaUsuario);

        return "proformas/formulario";
    }

    @PostMapping("/{id}/editar")
    String actualizar(@PathVariable Integer id, @ModelAttribute Proforma proforma,
                      BindingResult bindingResult,
                      RedirectAttributes ra,
                      Model model) {
        Proforma proformaDB = proformaRepository.findById(id).get();
        List<Pago> listaPago = pagoRepository.findAll();;
        List<Usuario> listaUsuario = usuarioRepository.findAll();

        proformaDB.setEnteroProyecto(proforma.getEnteroProyecto());
        proformaDB.setFechaProforma(proforma.getFechaProforma());
        proformaDB.setPago(proforma.getPago());
        proformaDB.setUsuario(proforma.getUsuario());
        proformaDB.setObservaciones(proforma.getObservaciones());

        if (bindingResult.hasErrors()) {
            model.addAttribute("proforma", proformaDB);
            model.addAttribute("listaPago", listaPago);
            model.addAttribute("listaUsuario", listaUsuario);
            ra.addFlashAttribute("msgError", "La Proforma no ha sido actualizado correctamente");

            return "proformas/formulario";
        }

        proformaRepository.save(proformaDB);

        ra.addFlashAttribute("msgExito", "La Proforma ha sido actualizado correctamente");

        return "redirect:/proforma";
    }

    @PostMapping("/{id}/eliminar")
    String eliminar(@PathVariable Integer id, RedirectAttributes ra) {

        Proforma proformaDB = proformaRepository.findById(id).get();

        proformaRepository.delete(proformaDB);
        ra.addFlashAttribute("msgExito", "La Proforma ha sido eliminado correctamente");
        return "redirect:/proforma";
    }
    
    @ModelAttribute
	public void setGenericos(Model model) {
		
		model.addAttribute("dtuser",  usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}
}

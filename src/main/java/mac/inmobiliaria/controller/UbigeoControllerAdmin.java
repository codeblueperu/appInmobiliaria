package mac.inmobiliaria.controller;

import mac.inmobiliaria.model.Cliente;
import mac.inmobiliaria.model.Ubigeo;
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

import java.util.List;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;


@Controller
@RequestMapping("/admin/ubigeos")
public class UbigeoControllerAdmin {

    @Autowired
    private UbigeoRepository ubigeoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public String index(Model model,
            @RequestParam(required = false) String distrito,
            @PageableDefault(size = 5) Pageable pageable) {

        Page<Ubigeo> ubigeoPage;

        if (StringUtils.hasText(distrito)) {
            ubigeoPage = ubigeoRepository.findByDistritoContaining(distrito, pageable);
        } else {
            ubigeoPage = ubigeoRepository.findAll(pageable);
        }

        model.addAttribute("ubigeoPage", ubigeoPage);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "ubigeos/index";
    }

    @GetMapping("/buscador")
    public String index(Model model,
                        @RequestParam(required = false) String distrito,
                        @RequestParam(required = false) String provincia,
                        @PageableDefault(size = 5) Pageable pageable) {

        Page<Ubigeo> ubigeoPage;

        if (StringUtils.hasText(distrito)) {
            ubigeoPage = ubigeoRepository.findByDistritoContaining(distrito, pageable);
        } else {
            ubigeoPage = ubigeoRepository.findAll(pageable);
        }

        model.addAttribute("ubigeoPage", ubigeoPage);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "ubigeos/buscador";
    }

    @GetMapping("/nuevo")
    String nuevo(Model model) {
        model.addAttribute("ubigeo", new Ubigeo());
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "ubigeos/formulario";
    }

    @PostMapping("/nuevo")
    String crear(@ModelAttribute @Validated Ubigeo ubigeo,
            BindingResult bindingResult,
            RedirectAttributes ra,
            Model model) {
        String departamento = ubigeo.getDepartamento();
        boolean esSoloLetrasDepa = ubigeoRepository.soloLetras(departamento);
        if(departamento.isEmpty()){
            bindingResult.rejectValue("departamento", "UbigeoDepaNotBlank");
        }else
        {
            if (!esSoloLetrasDepa){
                bindingResult.rejectValue("departamento", "UbigeoSoloLetras");
            }
        }

        String provincia = ubigeo.getProvincia();
        boolean esSoloLetrasProvi = ubigeoRepository.soloLetras(provincia);
        if(provincia.isEmpty()){
            bindingResult.rejectValue("provincia", "UbigeoProviNotBlank");
        }else
        {
            if (!esSoloLetrasProvi){
                bindingResult.rejectValue("provincia", "UbigeoSoloLetras");
            }
        }

        String distrito = ubigeo.getProvincia();
        boolean esSoloLetrasDist = ubigeoRepository.soloLetras(distrito);
        if(distrito.isEmpty()){
            bindingResult.rejectValue("distrito", "UbigeoDsitNotBlank");
        }else
        {
            if (!esSoloLetrasDist){
                bindingResult.rejectValue("distrito", "UbigeoSoloLetras");
            }
        }


        if (bindingResult.hasErrors()) {
            model.addAttribute("ubigeo", ubigeo);
            return "ubigeos/formulario";
        }
        ubigeoRepository.save(ubigeo);
        ra.addFlashAttribute("msgExito", "El ubigeo ha sido creado correctamente");
        return "redirect:/admin/ubigeos";
    }

    @GetMapping("/{id}/editar")
    String editar(@PathVariable Integer id, Model model) {
        Ubigeo ubigeo = ubigeoRepository.findById(id)
                .get();
        model.addAttribute("ubigeo", ubigeo);
        model.addAttribute("id", obtenerUsuarioLogeadoId());
        return "ubigeos/formulario";
    }

    @PostMapping("/{id}/editar")
    String actualizar(@PathVariable Integer id,
            @ModelAttribute @Validated Ubigeo ubigeo,
            BindingResult bindingResult,
            RedirectAttributes ra,
            Model model) {


        Ubigeo ubigeoDB = ubigeoRepository
                .findById(id)
                .get();

        ubigeoDB.setDistrito(ubigeo.getDistrito());
        ubigeoDB.setProvincia(ubigeo.getProvincia());
        ubigeoDB.setDepartamento(ubigeo.getDepartamento());

        String departamento = ubigeo.getDepartamento();
        boolean esSoloLetrasDepa = ubigeoRepository.soloLetras(departamento);
        if(departamento.isEmpty()){
            bindingResult.rejectValue("departamento", "UbigeoDepaNotBlank");
        }else
        {
            if (!esSoloLetrasDepa){
                bindingResult.rejectValue("departamento", "UbigeoSoloLetras");
            }
        }

        String provincia = ubigeo.getProvincia();
        boolean esSoloLetrasProvi = ubigeoRepository.soloLetras(provincia);
        if(provincia.isEmpty()){
            bindingResult.rejectValue("provincia", "UbigeoProviNotBlank");
        }else
        {
            if (!esSoloLetrasProvi){
                bindingResult.rejectValue("provincia", "UbigeoSoloLetras");
            }
        }

        String distrito = ubigeo.getProvincia();
        boolean esSoloLetrasDist = ubigeoRepository.soloLetras(distrito);
        if(distrito.isEmpty()){
            bindingResult.rejectValue("distrito", "UbigeoDsitNotBlank");
        }else
        {
            if (!esSoloLetrasDist){
                bindingResult.rejectValue("distrito", "UbigeoSoloLetras");
            }
        }


        if (bindingResult.hasErrors()) {
            model.addAttribute("ubigeo", ubigeo);
            return "ubigeos/formulario";
        }
        ubigeoRepository.save(ubigeoDB);

        ra.addFlashAttribute("msgExito", "El ubigeo ha sido actualizado correctamente");

        return "redirect:/admin/ubigeos";
    }

    @PostMapping("/{id}/eliminar")
    String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        Ubigeo ubigeo = ubigeoRepository.findById(id).get();

        List<Cliente> encontradoRelacionCliente = clienteRepository.findByAllCliente(id);

        if(encontradoRelacionCliente.size()>0){
            ra.addFlashAttribute("msgError", "El Ubigeo no se puede eliminar, mantiene relacion con algun Vendedor o Cliente");
            return "redirect:/admin/ubigeos";
        }

        ubigeoRepository.delete(ubigeo);
        ra.addFlashAttribute("msgExito", "El ubigeo ha sido eliminado correctamente");
        return "redirect:/admin/ubigeos";
    }
    
    @ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("dtuser",  usuarioRepository.findById(obtenerUsuarioLogeadoId()).get());
	}
}

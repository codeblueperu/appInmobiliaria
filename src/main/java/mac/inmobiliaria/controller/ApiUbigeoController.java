package mac.inmobiliaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mac.inmobiliaria.repository.DepartamentoRepository;
import mac.inmobiliaria.repository.DistritoRepository;
import mac.inmobiliaria.repository.ProvinciaRepository;

@RestController
@RequestMapping("/Api")
public class ApiUbigeoController {
	
	@Autowired
	private DepartamentoRepository departamentoRepository;
	
	@Autowired
	private ProvinciaRepository provinciaRepository;
	
	@Autowired
	private DistritoRepository distritoRepository;
	
	
	
	@GetMapping("/listaDepartamento")
	public ResponseEntity<?>getListaDepartamento(){
		return ResponseEntity.ok(departamentoRepository.findAll());
	}
	
	@GetMapping("/listaProvincia")
	public ResponseEntity<?>getListaProvincia(@RequestParam("idDepartamento") String idDepartamento){
		return ResponseEntity.ok(provinciaRepository.findByDepartamentoId(Integer.parseInt(idDepartamento)));
	}
	
	@GetMapping("/listaDistrito")
	public ResponseEntity<?>Distrito(@RequestParam("idProvincia") String idProvincia){
		return ResponseEntity.ok(distritoRepository.findByProvinciaId(Integer.parseInt(idProvincia)));
	}
	

}

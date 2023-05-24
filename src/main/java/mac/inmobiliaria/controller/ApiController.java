package mac.inmobiliaria.controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mac.inmobiliaria.model.Usuario;

@RestController
@RequestMapping("api/v1")
public class ApiController {
	
	@PostMapping("/updatePerfilLogin")
	public ResponseEntity<?> srvUpdatePerfilLogin(@Validated @RequestBody Usuario usuario){
		
		HashMap<String, Object> response = new HashMap<>();
		response.put("data", usuario);
		return ResponseEntity.ok(response);
		
	}

}

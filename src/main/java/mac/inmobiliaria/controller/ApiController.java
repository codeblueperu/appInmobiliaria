package mac.inmobiliaria.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.UsuarioRepository;

@RestController
@RequestMapping("api/v1")
public class ApiController {
	
	@Autowired
	private UsuarioRepository repoUser;
	
	@Autowired
	private PasswordEncoder encripto;
	
	@PostMapping("/updatePerfilLogin")
	public ResponseEntity<?> srvUpdatePerfilLogin(@Validated @RequestBody Usuario usuario){
		Usuario userDB = new Usuario();
		userDB = repoUser.findById(usuario.getId()).get();
		
		if(usuario.getPassword().isEmpty() || usuario.getPassword().compareTo("") == 0) {
			usuario.setPassword(userDB.getPassword());
		}else {
			usuario.setPassword(encripto.encode(usuario.getPassword()));
		}
		
		usuario.setPreinstall(userDB.getPreinstall());
		usuario.setCodeVerifyrecoverPassword(userDB.getCodeVerifyrecoverPassword());
		usuario.setRol(userDB.getRol());
		
		repoUser.save(usuario);
		
		HashMap<String, Object> response = new HashMap<>();
		response.put("message", "tus datos fueron modificados con éxito!");
		return ResponseEntity.ok(response);
		
	}

}

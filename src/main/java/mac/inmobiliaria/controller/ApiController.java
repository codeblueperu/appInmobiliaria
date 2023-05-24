package mac.inmobiliaria.controller;

import static mac.inmobiliaria.util.Constant.obtenerUsuarioLogeadoId;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mac.inmobiliaria.model.Asignacion;
import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.AsignacionRepository;
import mac.inmobiliaria.repository.UsuarioRepository;

@RestController
@RequestMapping("api/v1")
public class ApiController {

	@Autowired
	private UsuarioRepository repoUser;

	@Autowired
	private PasswordEncoder encripto;


	@Autowired
	private AsignacionRepository srvAsignacion;

	@PostMapping("/updatePerfilLogin")
	public ResponseEntity<?> srvUpdatePerfilLogin(@Validated @RequestBody Usuario usuario) {
		Usuario userDB = new Usuario();
		userDB = repoUser.findById(usuario.getId()).get();

		if (usuario.getPassword().isEmpty() || usuario.getPassword().compareTo("") == 0) {
			usuario.setPassword(userDB.getPassword());
		} else {
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

	@PostMapping("/tranferirclientesmasivo")
	public ResponseEntity<?> srvTransferirClientes(@RequestParam("clientes") int[] clientes,
			@RequestParam("idaccesor") int idaccesor) {

		for (int i : clientes) {
			Asignacion asignacion = srvAsignacion.findByClienteId(i);
			asignacion.setUsuario(new Usuario(idaccesor));
			srvAsignacion.save(asignacion);
		}
		HashMap<String, Object> response = new HashMap<>();
		response.put("message", "Todos los clientes fueron transpasados al nuevo accesor seleccionado!");
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/tranferirclientemanual")
	public ResponseEntity<?> srvTransferirClienteManual(@RequestParam("idcliente") int idcliente,
			@RequestParam("idaccesor") int idaccesor) {

		
			Asignacion asignacion = srvAsignacion.findByClienteId(idcliente);
			asignacion.setUsuario(new Usuario(idaccesor));
			srvAsignacion.save(asignacion);
		
		HashMap<String, Object> response = new HashMap<>();
		response.put("message", "El cliente fue transpasado al nuevo accesor seleccionado!");
		response.put("data", srvAsignacion.findByAllUsuario(obtenerUsuarioLogeadoId()));
		return ResponseEntity.ok(response);
	}

}

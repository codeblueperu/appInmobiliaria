package mac.inmobiliaria.impl;

import mac.inmobiliaria.model.Asignacion;
import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.AsignacionRepository;
import mac.inmobiliaria.repository.UsuarioRepository;
import mac.inmobiliaria.service.AsginacionService;
import mac.inmobiliaria.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AsginacionServiceImpl implements AsginacionService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    AsignacionRepository asignacionRepository;

    @Autowired
    EmailNotificationService emailService;

    @Override
    public void asignacionNoAtendidas() throws MessagingException {

        List<Usuario> listaVendedor = usuarioRepository.findByRol(Usuario.Rol.VENDEDOR);
       

        if(listaVendedor != null && !listaVendedor.isEmpty()){

            for (Usuario usuario : listaVendedor){

                List<Asignacion> listaAsignaciones = asignacionRepository.findByEstadoNotAndUsuarioId("ATENDIDO",usuario.getId());
                //System.err.println(listaAsignaciones);

                if(listaAsignaciones!=null && !listaAsignaciones.isEmpty() && listaAsignaciones.size()>0){

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", usuario.getNombreCompleto());
                   
                    emailService.send(usuario.getEmail(), map, "Asignaciones no atendidas");
                }

            }

        }

    }

}
package mac.inmobiliaria.scheduled;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import mac.inmobiliaria.repository.UsuarioRepository;
import mac.inmobiliaria.service.AsginacionService;
import mac.inmobiliaria.util.Constant;


@Component
public class ScheduledAsignacion {
	
	   @Autowired
	    AsginacionService asginacionService;
	   
	   @Autowired
	    UsuarioRepository usuarioRepository;

	    @Scheduled(cron = Constant.CRON_ASIGNACION_NO_ATENTIDA)
	    public void asignacionNoAtendidas() throws MessagingException {
	        asginacionService.asignacionNoAtendidas();
	        
	        
	    }
	    
	    @Scheduled(cron = Constant.CRON_ASIGNACION_NO_ATENTIDA)
	    public void asignacionUserLogin() throws MessagingException {
	        usuarioRepository.actualizarSession();
	        
	    }
	    
}


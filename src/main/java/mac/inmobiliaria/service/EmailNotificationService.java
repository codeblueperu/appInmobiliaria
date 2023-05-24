package mac.inmobiliaria.service;

import java.util.Map;

import javax.mail.MessagingException;

public interface EmailNotificationService {

	/**
	 * @author JMI
	 * @param asunto_mensaje
	 * @param mensaje_contexto
	 * @param destinatario_chasqui
	 * @param isAdjuntaFile
	 * @param nameFileAdjunta
	 */
	void onenviarCorreos(String asunto_mensaje, String mensaje_contexto, String destinatario_chasqui,
			boolean isAdjuntaFile, String nameFileAdjunta);
	
	
	void send(String mail, Map<String, Object> map, String subject) throws MessagingException;
}

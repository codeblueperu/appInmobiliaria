package mac.inmobiliaria.impl;

import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import mac.inmobiliaria.service.EmailNotificationService;
import mac.inmobiliaria.util.Constant;
import java.util.Map;

@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
  private SpringTemplateEngine templateEngine;

	@Override
	public void onenviarCorreos(String asunto_mensaje, String mensaje_contexto, String destinatario_chasqui,
			boolean isAdjuntaFile, String nameFileAdjunta) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
//	            REMITENTE
			helper.setFrom(Constant.USER_SERVICE_EMAIL);
//	            DESTINATARIO
			helper.setTo(destinatario_chasqui);
//			ASUNTO EMAIL
			helper.setSubject(asunto_mensaje);
//			CUERPO MENSAJE
			helper.setText(mensaje_contexto, true);

//			VALIDAR SI SE ENVIARA UN FILE
			if (isAdjuntaFile) {
//				RUTEAR EL FILE
				FileSystemResource file = new FileSystemResource(new File(nameFileAdjunta));
				helper.addAttachment("ArchivoAdjunto.pdf", file);
			}

//			ENVIAR MENSAJE EMAIL
			javaMailSender.send(mimeMessage);

		} catch (MessagingException e) {
			throw new IllegalStateException("Error al enviar mensaje servidor de correos.");
		}
	}

	@Override
	public void send(String mail, Map<String, Object> map, String subject) throws MessagingException{
      MimeMessage message = javaMailSender.createMimeMessage();

      MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              StandardCharsets.UTF_8.name());

      Context context = new Context();
      context.setVariables(map);

      String html = templateEngine.process("asignacion-no-atendida", context);
      helper.setTo(mail);
      helper.setText(html, true);
      helper.setSubject(subject);
      helper.setFrom(Constant.USER_SERVICE_EMAIL);

      javaMailSender.send(message);
		
	}


}

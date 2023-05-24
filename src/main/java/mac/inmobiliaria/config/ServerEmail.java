package mac.inmobiliaria.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import mac.inmobiliaria.model.Configuraciones;
import mac.inmobiliaria.repository.IConfiguracionesRepository;
import mac.inmobiliaria.util.Constant;

@Configuration
public class ServerEmail {

	@Autowired
	private IConfiguracionesRepository reposetting;

	@Bean
	public JavaMailSender getJavaMailSender() {
		Configuraciones setting = reposetting.findById(1).get();
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		Constant.USER_SERVICE_EMAIL = setting.getUserEmail();
		mailSender.setHost(setting.getHostEmail());
		mailSender.setPort(setting.getPuetroEmail());

		mailSender.setUsername(setting.getUserEmail());
		mailSender.setPassword(setting.getPasswordEmail());

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", false);
		return mailSender;
	}

}

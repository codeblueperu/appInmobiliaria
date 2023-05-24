package mac.inmobiliaria.impl;

//import mac.inmobiliaria.macinmobiliariapro.service.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//
//@Service
//public class EmailServiceImpl implements EmailService {
//
//    @Autowired
//    private JavaMailSender emailSender;
//
//    @Autowired
//    private SpringTemplateEngine templateEngine;
//
//    @Override
//    public void send(String mail, Map<String, Object> map, String subject) throws MessagingException {
//
//        MimeMessage message = emailSender.createMimeMessage();
//
//        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//                StandardCharsets.UTF_8.name());
//
//        Context context = new Context();
//        context.setVariables(map);
//
//        String html = templateEngine.process("asignacion-no-atendida", context);
//        helper.setTo(mail);
//        helper.setText(html, true);
//        helper.setSubject(subject);
//        helper.setFrom("sofiyurit20@gmail.com");
//
//        emailSender.send(message);
//
//    }
//
//}
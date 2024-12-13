package ar.com.l_airline.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    public void sendMail(String addresseeMail, String subject, String addresseeName) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        mimeMessageHelper.setTo(addresseeMail);
        mimeMessageHelper.setSubject(subject);

        //Thymeleaf
        Context context = new Context();

        //Custom the {$message} variable in HTML
        context.setVariable("userName", addresseeName);
        String htmlContent = templateEngine.process("thanksMail", context);

        //To accept HTML format
        mimeMessageHelper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

}

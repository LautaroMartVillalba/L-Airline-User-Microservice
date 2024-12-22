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

    /**
     *  Configure the mail sending using the project's owner email address, setting a subject and an addressee name
     * @param addresseeMail The recipient of the mail.
     * @param subject Mail subject.
     * @param addresseeName The recipient's name.
     */
    public void sendMail(String addresseeMail, String subject, String addresseeName) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        mimeMessageHelper.setTo(addresseeMail);
        mimeMessageHelper.setSubject(subject);

        Context context = new Context();

        //Set the "username" variable to make it customisable to all registered users.
        context.setVariable("userName", addresseeName);
        //Set the context variable listener and the variable to read.
        String htmlContent = templateEngine.process("thanksMail", context);

        //Use an HTML format, to don't use a plane text
        mimeMessageHelper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

}

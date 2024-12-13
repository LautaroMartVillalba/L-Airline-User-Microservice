package ar.com.l_airline.domains.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Configuration
public class EmailConfig {

    ObjectMapper objectMapper = new ObjectMapper();
    InputStream inputStream = new ClassPathResource("PrivateCredentials.json").getInputStream();
    Map<String, String> jsonParsed = objectMapper.readValue(inputStream, Map.class);

    private final String email = jsonParsed.get("email.username");
    private final String password = jsonParsed.get("email.password");

    public EmailConfig() throws IOException {
    }

    private Properties getEmailPropertiesToGmail(){
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return properties;
    }

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl implement = new JavaMailSenderImpl();

        implement.setJavaMailProperties(getEmailPropertiesToGmail());
        implement.setUsername(email);
        implement.setPassword(password);

        return implement;
    }

    @Bean
    public ResourceLoader resourceLoader(){
        return new DefaultResourceLoader();
    }

}

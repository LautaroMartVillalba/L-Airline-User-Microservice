package ar.com.l_airline;

import ar.com.l_airline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LAirlineApplication {

    public LAirlineApplication(UserService service) {
    }

    public static void main(String[] args) {
		SpringApplication.run(LAirlineApplication.class, args);
	}

	@Autowired
	@Bean
	public CommandLineRunner run(UserService userService){

		return args -> {
		};

	}

}

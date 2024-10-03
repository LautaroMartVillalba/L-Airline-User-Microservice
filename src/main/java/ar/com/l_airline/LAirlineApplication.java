package ar.com.l_airline;

import ar.com.l_airline.entities.user.Roles;
import ar.com.l_airline.entities.user.User;
import ar.com.l_airline.entities.user.UserDTO;
import ar.com.l_airline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LAirlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(LAirlineApplication.class, args);
	}

	@Autowired
	private UserService service;
	@Bean
	public CommandLineRunner run(UserService userService){

		return args -> {

			UserDTO user = UserDTO.builder()
					.name("Julio")
					.email("julio@gmail.com")
					.password("hola")
					.role(Roles.USER)
					.credentialsNoExpired(true)
					.accountNoLocked(true)
					.accountNoExpired(true)
					.isEnabled(true).build();

			userService.createUser(user);
		};

	}

}

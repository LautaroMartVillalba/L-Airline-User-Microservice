package ar.com.l_airline;

import ar.com.l_airline.entities.flight.AirlineName;
import ar.com.l_airline.entities.flight.Flight;
import ar.com.l_airline.entities.flight.FlightDTO;
import ar.com.l_airline.entities.hotel.HotelDTO;
import ar.com.l_airline.entities.hotel.enums.Room;
import ar.com.l_airline.entities.user.Roles;
import ar.com.l_airline.entities.user.User;
import ar.com.l_airline.entities.user.UserDTO;
import ar.com.l_airline.services.FlightService;
import ar.com.l_airline.services.HotelService;
import ar.com.l_airline.services.UserService;
import ar.com.l_airline.ubications.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class LAirlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(LAirlineApplication.class, args);
	}

	@Autowired
	private UserService service;
	@Bean
	public CommandLineRunner run(UserService userService, HotelService hotelService, FlightService flightService){

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

			HotelDTO hotel = HotelDTO.builder()
					.name("Magnus")
					.city(City.PUEBLA)
					.pricePerNight(60)
					.roomType(Room.EXECUTIVE).build();

			hotelService.createHotel(hotel);
		};

	}

}

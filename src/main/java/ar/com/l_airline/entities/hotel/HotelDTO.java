package ar.com.l_airline.entities.hotel;

import ar.com.l_airline.ubications.City;
import ar.com.l_airline.entities.hotel.enums.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class HotelDTO {
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private City city;
    @Enumerated(EnumType.STRING)
    private Room roomType;
    private double pricePerNight;
}

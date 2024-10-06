package ar.com.l_airline.entities.flight;

import ar.com.l_airline.ubications.City;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class FlightDTO {
    private Long id;
    private AirlineName airLine;
    @Enumerated(EnumType.STRING)
    private City origin;
    @Enumerated(EnumType.STRING)
    private City destiny;
    @Column(name = "flight_schedule")
    private LocalDateTime flightSchedule;
    private int layover;
    private double price;
}

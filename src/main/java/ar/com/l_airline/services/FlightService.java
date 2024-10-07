package ar.com.l_airline.services;

import ar.com.l_airline.entities.flight.AirlineName;
import ar.com.l_airline.entities.flight.Flight;
import ar.com.l_airline.entities.flight.FlightDTO;
import ar.com.l_airline.repositories.FlightRepository;
import ar.com.l_airline.ubications.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository repository;

    public boolean validateFlight(FlightDTO dto){
        if(dto.getAirLine().name().isBlank()
        || dto.getOrigin().name().isBlank()
        || dto.getDestiny().name().isBlank()
        || dto.getFlightSchedule().isBefore(LocalDateTime.now())
        || dto.getLayover() <= 3 && dto.getLayover() >= 0){
            return false;
        }
        return true;
    }

    public Optional<Flight> findFlightById(Long id){
        if (id == null){
            return Optional.empty();
        }
        return repository.findById(id);
    }

    public Flight createFlight(FlightDTO dto){
        if (!validateFlight(dto)){
            return null;
        }
        Flight flight = Flight.builder().airLine(dto.getAirLine())
                                        .origin(dto.getOrigin())
                                        .destiny(dto.getDestiny())
                                        .flightSchedule(dto.getFlightSchedule())
                                        .price(dto.getPrice())
                                        .layover(dto.getLayover()).build();

        repository.save(flight);
        return flight;
    }

    public boolean deleteFlight(Long id){
        Optional<Flight> flightInDB = this.findFlightById(id);

        if (flightInDB.isEmpty()){
            return false;
        }
        repository.deleteById(flightInDB.get().getId());
        return true;
    }

    public List<Flight> findByAirLine(AirlineName airline){
        if (airline.name().isBlank()){
            return new ArrayList<>();
        }
        return repository.findByAirLine(airline);
    }

    public List<Flight> findByOrigin(City origin){
        if (origin.name().isBlank()){
            return new ArrayList<>();
        }
        return repository.findByOrigin(origin);
    }

    public List<Flight> findByDestiny(City destiny){
        if (destiny.name().isBlank()){
            return new ArrayList<>();
        }
        return repository.findByDestiny(destiny);
    }

    public List<Flight> findByFlightSchedule (LocalDateTime schedule){
        if (schedule.isBefore(LocalDateTime.now())){
            return null;
        }
        return repository.findByFlightSchedule(schedule);
    }

    public List<Flight> findByPriceBetween (double min, double max){
        if (min < 0 || max < min){
            return null;
        }
        return repository.findByPriceBetween(min, max);
    }

    public Flight updateFlight (Long id, FlightDTO dto){
        Optional<Flight> findFlight = this.findFlightById(id);
        if (!validateFlight(dto) || findFlight.isEmpty()){
            return null;
        }
        Flight flightFound = findFlight.get();

        repository.save(flightFound);
        return  flightFound;
    }
}

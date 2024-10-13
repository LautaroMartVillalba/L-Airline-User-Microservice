package ar.com.l_airline.controllers;

import ar.com.l_airline.entities.flight.AirlineName;
import ar.com.l_airline.entities.flight.Flight;
import ar.com.l_airline.entities.flight.FlightDTO;
import ar.com.l_airline.services.FlightService;
import ar.com.l_airline.ubications.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flight")
//TODO refactor URL with @RequestParam
public class FlightController {

    @Autowired
    private FlightService service;

    @GetMapping("/byId/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Optional<Flight>> byId (@PathVariable Long id){
        Optional<Flight> result = service.findFlightById(id);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byAirline/{airlineName}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Flight>> getByAirLine(@PathVariable AirlineName airlineName){
        List<Flight> result = service.findByAirLine(airlineName);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byOrigin/{origin}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Flight>> byOrigin (@PathVariable City origin){
        List<Flight> result = service.findByOrigin(origin);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byDestiny/{destiny}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Flight>> findByDestiny (@PathVariable City destiny){
        List<Flight> result = service.findByDestiny(destiny);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/bySchedule")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Flight>> bySchedule (@RequestParam int year, @RequestParam int month,
                                                    @RequestParam int day, @RequestParam int hour,
                                                    @RequestParam int minute){
        List<Flight> result = service.findByFlightSchedule(year, month,day, hour, minute);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byPrice/{min}/{max}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Flight>> byPrice (@PathVariable double min, @PathVariable double max){
        List<Flight> result = service.findByPriceBetween(min, max);
        if (result.isEmpty() || result == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/insert")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlightDTO> createFlight (@RequestBody FlightDTO flight){
        Flight result = service.createFlight(flight);
        if (result == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(flight);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id){
        boolean result = service.deleteFlight(id);
        if (!result){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("¡Hotel removed!");
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> update(@PathVariable Long id, @RequestBody FlightDTO flight){
        Flight result = service.updateFlight(id, flight);
        if (result == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

}

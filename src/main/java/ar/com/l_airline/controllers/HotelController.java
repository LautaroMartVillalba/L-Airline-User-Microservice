package ar.com.l_airline.controllers;

import ar.com.l_airline.entities.hotel.Hotel;
import ar.com.l_airline.entities.hotel.HotelDTO;
import ar.com.l_airline.entities.hotel.enums.Room;
import ar.com.l_airline.services.HotelService;
import ar.com.l_airline.ubications.City;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/hotel")
public class HotelController {

    private final HotelService service;

    public HotelController(HotelService service) {
        this.service = service;
    }

    @GetMapping("/byId")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Optional<Hotel>> getById(@RequestParam Long id){
        Optional<Hotel> result = service.findHotelById(id);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byName")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Hotel>> getByName(@RequestParam String name){
        List<Hotel> result = service.findHotelByName(name);
        if (result.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byCity")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Hotel>> findByCity(@RequestParam City city){
        List<Hotel> result = service.findHotelByCity(city);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byRoom")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Hotel>> findByRoom(@RequestParam Room room){
        List<Hotel> result = service.findHotelByRoom(room);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byPrice")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Hotel>> findByPrice(@RequestParam double min, @RequestParam double max){
        List<Hotel> result = service.findHotelByPrice(min, max);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/insert")
    public ResponseEntity<Hotel> createHotel(@RequestBody HotelDTO dto){
        Hotel result = service.createHotel(dto);
        if (result == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteHotel(@RequestParam Long id){
        boolean result = service.deleteHotelById(id);
        if (!result){
            return ResponseEntity.ok("Operation can't be complete.");
        }
        return ResponseEntity.ok("¡Hotel deleted!");
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> updateHotel (@RequestParam Long id, @RequestBody HotelDTO dto){
        Hotel updatedHotel = service.updateHotel(id, dto);

        return ResponseEntity.ok(updatedHotel);
    }
}

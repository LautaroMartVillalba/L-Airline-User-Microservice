package ar.com.l_airline.controllers;

import ar.com.l_airline.entities.hotel.Hotel;
import ar.com.l_airline.entities.hotel.HotelDTO;
import ar.com.l_airline.entities.hotel.enums.Room;
import ar.com.l_airline.services.HotelService;
import ar.com.l_airline.ubications.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelService service;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/insert")
    public ResponseEntity<Hotel> createHotel(@RequestBody HotelDTO dto){
        Hotel result = service.createHotel(dto);
        if (result == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/byId/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Optional<Hotel>> getById(@PathVariable Long id){
        Optional<Hotel> result = service.findHotelById(id);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byName/{name}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Hotel>> getByName(@PathVariable String name){
        List<Hotel> result = service.findHotelByName(name);
        if (result.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byCity/{city}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Hotel>> findByCity(@PathVariable City city){
        List<Hotel> result = service.findHotelByCity(city);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byRoom/{room}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Hotel>> findByRoom(@PathVariable Room room){
        List<Hotel> result = service.findHotelByRoom(room);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byPrice/{min}/{max}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Hotel>> findByPrice(@PathVariable double min, @PathVariable double max){
        List<Hotel> result = service.findHotelByPrice(min, max);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> updateHotel (@PathVariable Long id, @RequestBody HotelDTO dto){
        Hotel updatedHotel = service.updateHotel(id, dto);

        return ResponseEntity.ok(updatedHotel);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id){
        boolean result = service.deleteHotelById(id);
        if (!result){
            return ResponseEntity.ok("Operation can't be complete.");
        }
        return ResponseEntity.ok("¡Hotel deleted!");
    }
}

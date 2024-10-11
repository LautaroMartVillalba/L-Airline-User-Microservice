package ar.com.l_airline.services;

import ar.com.l_airline.entities.hotel.Hotel;
import ar.com.l_airline.entities.hotel.HotelDTO;
import ar.com.l_airline.ubications.City;
import ar.com.l_airline.entities.hotel.enums.Room;
import ar.com.l_airline.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository repository;

    private boolean validateHotel(HotelDTO dto){
        if(dto.getName().isBlank()
        || dto.getRoomType().name().isBlank()
        || dto.getCity().name().isBlank()
        || dto.getPricePerNight() <= 0){
            return false;
        }
        return true;
    }

    public Hotel createHotel (HotelDTO dto){
        Optional<Hotel> dbHotel = repository.findByNameAndCityAndRoomType(dto.getName(),
                                                                          dto.getCity(),
                                                                          dto.getRoomType());
        if (dbHotel.isPresent()
            && dto.getName().equals(dbHotel.get().getName())
            && dto.getCity() == dbHotel.get().getCity()
            && dto.getRoomType() == dbHotel.get().getRoomType()
            && dto.getPricePerNight() == dbHotel.get().getPricePerNight()){
            return null;
        }
        Hotel hotelSave = Hotel.builder()
                               .name(dto.getName())
                               .city(dto.getCity())
                               .roomType(dto.getRoomType())
                               .pricePerNight(dto.getPricePerNight()).build();
         repository.save(hotelSave);
         return hotelSave;
    }

    public Optional<Hotel> findHotelById(Long id){
        if (id <= 0){
            return Optional.empty();
        }
        return repository.findById(id);
    }

    public List<Hotel> findHotelByName(String name){
        if (name.isBlank()){
            return null;
        }
        return repository.findByNameContaining(name);
    }

    public List<Hotel> findHotelByCity(City city){
        if (city == null){
            return null;
        }
        return repository.findByCity(city);
    }

    public List<Hotel> findHotelByRoom(Room room){
        if (room == null){
            return null;
        }
        return repository.findByRoomType(room);
    }

    public List<Hotel> findHotelByPrice(double min, double max){
        if (min <= 0 || max <= min){
            return null;
        }
        return repository.findByPricePerNightBetween(min, max);
    }

    public Hotel updateHotel (Long id, HotelDTO dto){
        Hotel findHotel = this.findHotelById(id).orElseThrow(() -> new RuntimeException("Hotel not found."));

        if (dto.getName() != null){
            findHotel.setName(dto.getName());
        }
        if (dto.getCity() != null){
            findHotel.setCity(dto.getCity());
        }
        if (dto.getRoomType() != null){
            findHotel.setRoomType(dto.getRoomType());
        }
        if (dto.getPricePerNight() <= 0){
            findHotel.setPricePerNight(dto.getPricePerNight());
        }

        repository.save(findHotel);
        return  findHotel;
    }

    public boolean deleteHotelById(Long id){
        Optional<Hotel> findHotel = this.findHotelById(id);
        if (findHotel.isEmpty()){
            return false;
        }
        repository.deleteById(findHotel.get().getId());
        return true;
    }
}

package idatt2106scrumteam10.GIDD.services;

import idatt2106scrumteam10.GIDD.models.Location;
import idatt2106scrumteam10.GIDD.repos.LocationRepository;
import idatt2106scrumteam10.GIDD.repos.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class LocationService {

    Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.services.LocationService.class.getName());

    @Autowired
    LocationRepository locationRepository;

    public Location getOrCreate(Location inputLocation) {
        if (inputLocation.getLatitude() == null || inputLocation.getLongitude() == null) {
            logger.log(Level.WARNING, "Latitude and longitude cannot be null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Latitude and longitude cannot be null");
        }
        Location location = locationRepository.findByLatitudeAndLongitude(inputLocation.getLatitude(), inputLocation.getLongitude()).orElse(null);
        if (location == null) {
            location = locationRepository.save(new Location(inputLocation.getAddress(), inputLocation.getLatitude(), inputLocation.getLongitude()));
        }
        return location;
    }

    public Location findByLatitudeAndLongitude(Double latitude, Double longitude){
        return locationRepository.findByLatitudeAndLongitude(latitude,longitude).orElse(null);
    }
    public Location save(Location location){
    return locationRepository.save(location);
    }

}

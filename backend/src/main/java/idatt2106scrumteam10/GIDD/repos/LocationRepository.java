package idatt2106scrumteam10.GIDD.repos;

import idatt2106scrumteam10.GIDD.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatitudeAndLongitude(double latitude, double longitude);
}

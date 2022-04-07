package idatt2106scrumteam10.GIDD.repos;

import idatt2106scrumteam10.GIDD.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ImageRepository extends JpaRepository<Image,Long> {
    Optional<Image> findByContentAndName(byte[] content, String name);
}

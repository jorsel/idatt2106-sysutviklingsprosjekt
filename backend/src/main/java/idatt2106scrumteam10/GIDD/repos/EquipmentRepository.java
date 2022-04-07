package idatt2106scrumteam10.GIDD.repos;

import idatt2106scrumteam10.GIDD.models.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByItem(String item);

    Optional<Equipment> findByItemAndAmount(String item, int amount);
}

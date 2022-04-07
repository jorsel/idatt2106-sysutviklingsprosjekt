package idatt2106scrumteam10.GIDD.repos;

import idatt2106scrumteam10.GIDD.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReadOrderByIDDesc(Boolean read);
    List<Notification> findByRead(Boolean read);
}

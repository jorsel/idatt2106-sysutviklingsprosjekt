package idatt2106scrumteam10.GIDD.repos;

import idatt2106scrumteam10.GIDD.models.Feedback;
import idatt2106scrumteam10.GIDD.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByUser(User user);
}

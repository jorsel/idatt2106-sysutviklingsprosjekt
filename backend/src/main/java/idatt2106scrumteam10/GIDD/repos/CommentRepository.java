package idatt2106scrumteam10.GIDD.repos;

import idatt2106scrumteam10.GIDD.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}

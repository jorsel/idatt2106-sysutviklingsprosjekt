package idatt2106scrumteam10.GIDD.repos;

import idatt2106scrumteam10.GIDD.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);
    Page<User> findByIsPublicOrderByPointsDesc(boolean isPublic, Pageable pageable);
    Set<User> findByEmailIn(Collection<String> emails);
}

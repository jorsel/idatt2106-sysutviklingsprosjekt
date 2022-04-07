package idatt2106scrumteam10.GIDD.repos;

import idatt2106scrumteam10.GIDD.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTag(String tag);
    Set<Tag> findByTagIn(Collection<String> tags);
}

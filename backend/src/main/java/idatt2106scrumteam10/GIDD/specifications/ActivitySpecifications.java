package idatt2106scrumteam10.GIDD.specifications;

import idatt2106scrumteam10.GIDD.models.Activity;
import idatt2106scrumteam10.GIDD.models.Location;
import idatt2106scrumteam10.GIDD.models.Tag;
import idatt2106scrumteam10.GIDD.models.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActivitySpecifications {

    public static Specification<Activity> findByCriteria(Map<String, Object> filter) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.containsKey("createdBy") && filter.get("createdBy") instanceof User) {
                User user = (User) filter.get("createdBy");
                predicates.add(criteriaBuilder.equal(root.get("createdBy"), user));
            }

            if (filter.containsKey("userUpcoming") && filter.get("userUpcoming") instanceof User) {
                User user = (User) filter.get("userUpcoming");
                predicates.add(criteriaBuilder.or(criteriaBuilder.isMember(user, root.get("participants")), criteriaBuilder.isMember(user, root.get("waitingList"))));
            }

            if (filter.containsKey("userCompleted") && filter.get("userCompleted") instanceof User) {
                User user = (User) filter.get("userCompleted");
                predicates.add(criteriaBuilder.isMember(user, root.get("participants")));
                predicates.add(criteriaBuilder.lessThan(root.get("end"), LocalDateTime.now()));
            } else {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("end"), LocalDateTime.now()));
            }

            if (filter.containsKey("locations")) {
                List<Location> locations = (List<Location>) filter.get("locations");
                predicates.add(root.get("location").in(locations));
            }

            if (filter.containsKey("intensities")) {
                List<Integer> intensities = (List<Integer>) filter.get("intensities");
                predicates.add(root.get("intensity").in(intensities));
            }

            if (filter.containsKey("tags")) {
                Set<Tag> tags = (Set<Tag>) filter.get("tags");
                for (Tag tag : tags) {
                    predicates.add(criteriaBuilder.isMember(tag, root.get("tags")));
                }
            }

            if (filter.containsKey("search")) {
                String search = (String) filter.get("search");
                search = "%" + search;
                search = search.concat("%");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), search.toLowerCase()));
            }

            if (filter.containsKey("dates")) {
                List<LocalDateTime> dates = (List<LocalDateTime>) filter.get("dates");
                List<Predicate> datePredicates = new ArrayList<>();
                for (int i = 0; i < dates.size(); i += 2) {
                    datePredicates.add(criteriaBuilder.between(root.get("start"), dates.get(i), dates.get(i + 1)));
                }
                predicates.add(criteriaBuilder.or(datePredicates.toArray(new Predicate[0])));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}

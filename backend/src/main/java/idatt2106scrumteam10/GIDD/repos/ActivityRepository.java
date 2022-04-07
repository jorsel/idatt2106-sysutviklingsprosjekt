package idatt2106scrumteam10.GIDD.repos;

import idatt2106scrumteam10.GIDD.models.Activity;
import idatt2106scrumteam10.GIDD.models.Tag;
import idatt2106scrumteam10.GIDD.models.User;
import idatt2106scrumteam10.GIDD.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {

    List<Activity> findByCreatedBy(User user);

    List<Activity> findByParticipantsContains(User user);

    List<Activity> findByWaitingListContains(User user);

}


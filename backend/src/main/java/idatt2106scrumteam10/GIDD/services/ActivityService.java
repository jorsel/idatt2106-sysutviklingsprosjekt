package idatt2106scrumteam10.GIDD.services;

import idatt2106scrumteam10.GIDD.models.Activity;
import idatt2106scrumteam10.GIDD.models.Comment;
import idatt2106scrumteam10.GIDD.models.User;
import idatt2106scrumteam10.GIDD.repos.*;
import idatt2106scrumteam10.GIDD.specifications.ActivitySpecifications;
import idatt2106scrumteam10.GIDD.utils.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ActivityService {

    final private int pointPerActivity = 100;

    Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.services.ActivityService.class.getName());

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    LocationService locationService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    TagService tagService;

    @Autowired
    ImageService imageService;

    @Autowired
    Filter filterUtils;

    public Activity findById(Long id) {
        Activity activity = activityRepository.findById(id).orElse(null);
        if (activity == null) {
            logger.log(Level.WARNING, "Activity not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found");
        }
        logger.log(Level.INFO, "Activity with id " + id + " found");
        return activity;
    }

    public Page<Activity> getActivities(Map<String, String> filter) {
        Map<String, Object> newFilter = filterUtils.getNewFilter(filter);
        Specification<Activity> activitySpecification = ActivitySpecifications.findByCriteria(newFilter);
        Pageable pageable = PageRequest.of((Integer) newFilter.get("page"), 4, (Sort) newFilter.get("sort"));
        return activityRepository.findAll(activitySpecification, pageable);
    }

    public List<Activity> getUpcomingActivities(Map<String, String> filter) {
        User user = userService.getUser();
        filter.put("sort", "start_desc");
        Map<String, Object> newFilter = filterUtils.getNewFilter(filter);
        newFilter.put("userUpcoming", user);
        Specification<Activity> activitySpecification = ActivitySpecifications.findByCriteria(newFilter);
        Pageable pageable = PageRequest.of((Integer) newFilter.get("page"), 4, (Sort) newFilter.get("sort"));
        return activityRepository.findAll(activitySpecification, pageable).toList();
    }

    public List<Activity> getActivitiesCreatedByUser(Map<String, String> filter) {
        User user = userService.getUser();
        Map<String, Object> newFilter = filterUtils.getNewFilter(filter);
        newFilter.put("createdBy", user);
        Specification<Activity> activitySpecification = ActivitySpecifications.findByCriteria(newFilter);
        Pageable pageable = PageRequest.of((Integer) newFilter.get("page"), 4, (Sort) newFilter.get("sort"));
        return activityRepository.findAll(activitySpecification, pageable).toList();
    }

    public List<Activity> getCompletedActivities(Map<String, String> filter) {
        User user = userService.getUser();
        Map<String, Object> newFilter = filterUtils.getNewFilter(filter);
        newFilter.put("userCompleted", user);
        Specification<Activity> activitySpecification = ActivitySpecifications.findByCriteria(newFilter);
        Pageable pageable = PageRequest.of((Integer) newFilter.get("page"), 4, (Sort) newFilter.get("sort"));
        return activityRepository.findAll(activitySpecification, pageable).toList();
    }

    public Activity createActivity(Activity activity) {
        User user = userService.getUser();
        activity.setLocation(locationService.getOrCreate(activity.getLocation()));
        activity.setCreatedBy(user);
        activity.setComments(null);
        activity.setTags(tagService.getIfExists(activity.getTags()));
        activity.setParticipants(null);
        activity.setWaitingList(null);
        //activity.setPicture(imageService.saveImage(activity.getPicture()));
        logger.log(Level.INFO, "Activity created");
        return activityRepository.save(activity);
    }

    public boolean deleteActivity(Long id) {
        User user = userService.getUser();
        Activity activity = findById(id);
        if (!user.equals(activity.getCreatedBy())) {
            logger.log(Level.WARNING, "User does not have permission to delete this activity");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User does not have permission to delete this activity");
        }
        notificationService.activityDeleted(activity);
        activityRepository.delete(activity);
        logger.log(Level.INFO, "Activity with ID " + id + " has been deleted");
        return true;
    }

    public Activity updateActivity(Long id, Activity updates) {
        User user = userService.getUser();
        Activity activity = findById(id);
        if (!user.equals(activity.getCreatedBy())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User does not have permission to edit this activity");
        }
        activity.setTitle(updates.getTitle());
        activity.setDescription(updates.getDescription());
        activity.setStart(updates.getStart());
        activity.setEnd(updates.getEnd());
        activity.setMaxAmount(updates.getMaxAmount());
        activity.setIntensity(updates.getIntensity());
        activity.setLocation(locationService.getOrCreate(updates.getLocation()));
        activity.setTags(tagService.getIfExists(updates.getTags()));
        activity.setEquipment(updates.getEquipment());
        //activity.setPicture(imageService.saveImage(updates.getPicture()));
        logger.log(Level.INFO, "Activity with ID " + id + " has been updated.");
        return activityRepository.save(activity);
    }

    public Activity joinActivity(Long id) {
        User user = userService.getUser();
        Activity activity = findById(id);
        if (activity.getParticipants().contains(user) || activity.getWaitingList().contains(user)) {
            logger.log(Level.WARNING, "User with ID " + user.getId() + " already signed up to activity with ID " + activity.getID());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already signed up to this activity");
        }
        if (activity.getParticipants().size() < activity.getMaxAmount()) {
            activity.getParticipants().add(user);
            userService.giveUserPoints(user, activity);
            logger.log(Level.INFO, "User with ID " + user.getId() + " signed up for activity with ID " + activity.getID());
        } else {
            activity.getWaitingList().add(user);
            logger.log(Level.INFO, "User with ID " + user.getId() + " signed up to waiting list for activity with ID " + activity.getID());
        }
        return activityRepository.save(activity);
    }

    public Activity leaveActivity(Long id) {
        Activity activity = findById(id);
        User user = userService.getUser();
        if (!activity.getParticipants().contains(user) && !activity.getWaitingList().contains(user)) {
            logger.log(Level.WARNING, "User with ID " + user.getId() + " is not signed up to activity with ID " + activity.getID());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not signed up to this activity");
        }

        if (activity.getParticipants().contains(user)) {
            activity.getParticipants().remove(user);
            userService.subtractPoints(user, activity);
            updateParticipants(activity);
        }
        activity.getWaitingList().remove(user);
        logger.log(Level.INFO, "User with ID " + user.getId() + " is no longer a participant of activity with ID " + activity.getID());
        return activityRepository.save(activity);
    }

    public Activity addComment(Long id, Comment comment) {
        Activity activity = findById(id);
        User user = userService.getUser();
        
        if (comment.getMessage() != null) {
            if (!comment.getMessage().equals("")) {
                activity.getComments().add(commentRepository.save(new Comment(comment.getMessage(), LocalDateTime.now(), user)));
            }
        }
        return activityRepository.save(activity);
    }

    public Activity deleteComment(Long activityId, Long commentId) {
        Activity activity = findById(activityId);
        Comment comment = commentService.findById(commentId);
        activity.getComments().remove(comment);
        activity = activityRepository.save(activity);
        commentRepository.delete(comment);
        return activity;
    }

    public List<Comment> getComments(Long id) {
        Activity activity = findById(id);
        List<Comment> comments = activity.getComments();
        Collections.reverse(comments);
        return comments;
    }

    public Activity givePoints(Long id, Set<User> participated) {
        Activity activity = findById(id);
        logger.log(Level.INFO, "Giving participants of activity: " + activity.getTitle() + " points");
        userService.giveUsersPoints(participated, activity);
        return activity;
    }

    public void deleteCreatedBy(User user) {
        activityRepository.findByCreatedBy(user).forEach(activity -> {
            notificationService.activityDeleted(activity);
            activityRepository.delete(activity);
        });
    }

    public void updateParticipants(Activity activity) {
        if (activity.getWaitingList().size() > 0) {
            User upgraded = activity.getWaitingList().stream().findFirst().orElse(null);
            if (upgraded != null) {
                activity.getWaitingList().remove(upgraded);
                activity.getParticipants().add(upgraded);
                notificationService.joinedParticipants(activity, upgraded);
            }
        }
    }

    public void removeUserFromParticipationLists(User user) {
        activityRepository.findByParticipantsContains(user).forEach(activity -> {
            activity.getParticipants().remove(user);
            updateParticipants(activity);
            activityRepository.save(activity);
        });
    }

    public void removeUserFromWaitingLists(User user) {
        activityRepository.findByWaitingListContains(user).forEach(activity -> {
            activity.getWaitingList().remove(user);
            activityRepository.save(activity);
        });
    }

    public List<Activity> adminGetAllActivities() {
        return activityRepository.findAll(Sort.by("ID").descending());
    }

    public Activity adminCreateActivity(Activity activity) {
        User user = userService.getByEmail(activity.getCreatedBy().getEmail());
        activity.setCreatedBy(user);
        activity.setLocation(locationService.getOrCreate(activity.getLocation()));
        activity.setComments(null);
        activity.setTags(tagService.getIfExists(activity.getTags()));
        activity.setParticipants(userService.getIfExists(activity.getParticipants()));
        activity.setWaitingList(userService.getIfExists(activity.getWaitingList()));
        //activity.setPicture(imageService.saveImage(activity.getPicture()));
        logger.log(Level.INFO, "Activity created");
        return activityRepository.save(activity);
    }

    public Activity adminEditActivity(Long id, Activity updates) {
        User user = userService.getByEmail(updates.getCreatedBy().getEmail());
        Activity activity = findById(id);
        activity.setTitle(updates.getTitle());
        activity.setDescription(updates.getDescription());
        activity.setStart(updates.getStart());
        activity.setEnd(updates.getEnd());
        activity.setMaxAmount(updates.getMaxAmount());
        activity.setIntensity(updates.getIntensity());
        activity.setLocation(locationService.getOrCreate(updates.getLocation()));
        activity.setTags(tagService.getIfExists(updates.getTags()));
        activity.setEquipment(updates.getEquipment());
        //activity.setPicture(imageService.saveImage(updates.getPicture()));
        logger.log(Level.INFO, "Activity with ID " + id + " has been updated.");
        return activityRepository.save(activity);
    }

    public boolean adminDeleteActivity(Long id) {
        Activity activity = findById(id);
        notificationService.activityDeleted(activity);
        activityRepository.delete(activity);
        logger.log(Level.INFO, "Activity with ID " + id + " has been deleted");
        return true;
    }
}
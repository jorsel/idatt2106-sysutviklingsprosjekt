package idatt2106scrumteam10.GIDD.controllers;

import idatt2106scrumteam10.GIDD.models.*;
import idatt2106scrumteam10.GIDD.services.ActivityService;
import idatt2106scrumteam10.GIDD.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ActivityController {

    Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.controllers.ActivityController.class.getName());

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @GetMapping("/activities")
    public Page<Activity> getActivities(@RequestParam Map<String, String> filter) {
        logger.log(Level.INFO, "Get request /activities" + filter);
        return activityService.getActivities(filter);
    }

    @GetMapping("/activity/{id}")
    public Activity getActivity(@PathVariable Long id) {
        logger.log(Level.INFO, "Get request /activity/" + id);
        return activityService.findById(id);
    }

    @PutMapping("/activity/{id}")
    public Activity updateActivity(@RequestBody Activity newActivity, @PathVariable Long id) {
        logger.log(Level.INFO, "Put request /activity/" + id);
        return activityService.updateActivity(id, newActivity);
    }

    @DeleteMapping("/activity/{id}")
    public boolean deleteActivity(@PathVariable Long id) {
        logger.log(Level.INFO, "Delete request /activity/" + id);
        return activityService.deleteActivity(id);
    }

    @PostMapping("/activity")
    public Activity createActivities(@RequestBody Activity newActivity) {
        logger.log(Level.INFO, "Post request /activity");
        return activityService.createActivity(newActivity);
    }

    @PutMapping("/activity/{id}/join")
    public Activity joinActivity(@PathVariable Long id) {
        logger.log(Level.INFO, "Put request /activity/" + id + "/join");
        return activityService.joinActivity(id);
    }

    @PutMapping("/activity/{id}/leave")
    public Activity leaveActivity(@PathVariable Long id) {
        logger.log(Level.INFO, "Put request /activity/" + id + "/leave");
        return activityService.leaveActivity(id);
    }

    @GetMapping("/profile/activities/upcoming")
    public List<Activity> getUpcomingActivities(@RequestParam Map<String, String> filter) {
        logger.log(Level.INFO, "Get request /profile/activities/upcoming");
        return activityService.getUpcomingActivities(filter);
    }

    @GetMapping("/profile/activities/created")
    public List<Activity> getActivityByUser(@RequestParam Map<String, String> filter) {
        logger.log(Level.INFO, "Get request /profile/activities/created");
        return activityService.getActivitiesCreatedByUser(filter);
    }

    @GetMapping("/profile/activities/completed")
    public List<Activity> getCompletedActivities(@RequestParam Map<String, String> filter) {
        logger.log(Level.INFO, "Get request /profile/activities/completed");
        return activityService.getCompletedActivities(filter);
    }

    @PutMapping("/activity/{id}/comments")
    public Activity addComment(@RequestBody Comment comment, @PathVariable Long id) {
        logger.log(Level.INFO, "Put request /activity/" + id + "/comments");
        return activityService.addComment(id, comment);
    }

    @GetMapping("/activity/{id}/comments")
    public List<Comment> getComments(@PathVariable Long id) {
        logger.log(Level.INFO, "Get request /activity/" + id + "/comments");
        return activityService.getComments(id);
    }

    @PutMapping("/activity/{id}/points")
    public Activity givePoints(@PathVariable Long id, @RequestBody Set<User> participated){
        return activityService.givePoints(id, participated);
    }

    @DeleteMapping("/activity/{activityId}/comments/{commentId}")
    public Activity getComments(@PathVariable Long activityId, @PathVariable Long commentId) {
        logger.log(Level.INFO, "Delete request /activity/" + activityId + "/comments/" + commentId);
        return activityService.deleteComment(activityId, commentId);
    }

    @GetMapping("{id}/jsonstring")
    public String getJsonActivity(@PathVariable Long id) {
        logger.log(Level.INFO, "Get request /activity/" + id + "/comments");
        return activityService.findById(id).PostJSONString();
    }

    @GetMapping("/admin/activities")
    public List<Activity> adminGetAllActivities() {
        return activityService.adminGetAllActivities();
    }

    @PostMapping("/admin/activities")
    public Activity adminCreateActivity(@RequestBody Activity activity) {
        return activityService.adminCreateActivity(activity);
    }

    @PutMapping("/admin/activity/{id}")
    public Activity adminEditActivity(@PathVariable Long id, @RequestBody Activity updates) {
        return activityService.adminEditActivity(id, updates);
    }

    @DeleteMapping("/admin/activity/{id}")
    public boolean adminDeleteActivity(@PathVariable Long id) {
        return activityService.adminDeleteActivity(id);
    }

}


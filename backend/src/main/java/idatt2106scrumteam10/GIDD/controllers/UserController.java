package idatt2106scrumteam10.GIDD.controllers;

import idatt2106scrumteam10.GIDD.models.Image;
import idatt2106scrumteam10.GIDD.models.Notification;
import idatt2106scrumteam10.GIDD.models.User;
import idatt2106scrumteam10.GIDD.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class UserController {

    Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.controllers.UserController.class.getName());

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    User getUser() {
        logger.log(Level.INFO, "Get request /profile");
        return userService.getUser();
    }

    @PostMapping("/register")
    User createUser(@RequestBody User user) {
        logger.log(Level.INFO, "Post request /register");
        return userService.createUser(user);
    }

    @PutMapping(path = "/profile/newPassword")
    boolean updatePassword(@RequestBody Map<String, String> passwords) {
        logger.log(Level.INFO, "Put request update password /profile");
        String oldPassword = passwords.getOrDefault("oldPassword", null);
        String newPassword = passwords.getOrDefault("newPassword", null);
        return userService.updatePassword(oldPassword, newPassword);
    }

    @PutMapping(path = "/profile")
    User updateUser(@RequestBody User updatedUser) {
        logger.log(Level.INFO, "Put request update user /profile");
        return userService.updateUser(updatedUser);
    }

    @PutMapping(path = "/profile/picture")
    User uploadProfilePicture(@RequestBody MultipartFile file) throws IOException {
        logger.log(Level.INFO, "Put request upload profile picture /profile/picture");
        return userService.uploadImage(file);
    }

    @GetMapping(path = "/profile/picture")
    Image getProfilePicture() {
        logger.log(Level.INFO, "Get request profile picture /profile/picture");
        return userService.getProfilePicture();
    }

    @DeleteMapping("/profile")
    boolean deleteUser() {
        logger.log(Level.INFO, "Delete request /profile");
        return userService.deleteUser();
    }

    @GetMapping("/leaderboard")
    List<User> getLeaderboard() {
        logger.log(Level.INFO, "Get request /leaderboard");
        return userService.getLeaderboard();
    }
    @GetMapping("/profile/notifications")
    List<Notification> getUnread(){
        logger.log(Level.INFO,"Get request /profile/notifications");
        return userService.getUnread();
    }
    @PutMapping("/profile/notifications")
    void markAsRead(@RequestBody Notification notification){
        logger.log(Level.INFO,"Put request /profile/notifications");
        userService.markAsRead(notification);
    }

    @GetMapping("/admin/users")
    public List<User> adminGetAllUsers() {
        return userService.adminGetAllUsers();
    }

    @PostMapping("/admin/users")
    public User adminCreateUser(@RequestBody User user) {
        return userService.adminCreateUser(user);
    }

    @PutMapping("/admin/user/{id}")
    public User adminEditUser(@PathVariable Long id, @RequestBody User updates) {
        return userService.adminEditUser(id, updates);
    }

    @DeleteMapping("/admin/user/{id}")
    public boolean adminDeleteUser(@PathVariable Long id) {
        return userService.adminDeleteUser(id);
    }

}
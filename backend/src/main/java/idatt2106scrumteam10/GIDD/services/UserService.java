package idatt2106scrumteam10.GIDD.services;

import idatt2106scrumteam10.GIDD.models.Activity;
import idatt2106scrumteam10.GIDD.models.Image;
import idatt2106scrumteam10.GIDD.models.Notification;
import idatt2106scrumteam10.GIDD.models.User;
import idatt2106scrumteam10.GIDD.repos.NotificationRepository;
import idatt2106scrumteam10.GIDD.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.services.UserService.class.getName());

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    public User getByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.log(Level.WARNING, "User not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        logger.log(Level.INFO, "User with email " + email + " found");
        return user;
    }

    public User getById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            logger.log(Level.WARNING, "User not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        logger.log(Level.INFO, "User with id " + id + " found");
        return user;
    }

    public User getUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		    logger.log(Level.INFO, "Current user found");
			return userRepository.findByEmail(((UserDetails) principal).getUsername());
		} else {
		    logger.log(Level.WARNING, "Current user not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
    }

    public User createUser(User user) {
        if (user.getPassword().equals("")) {
            logger.log(Level.WARNING, "Password can not be empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password can not be empty");
        }
        if (user.getEmail().equals("")) {
            logger.log(Level.WARNING, "Email can not be empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email can not be empty");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            logger.log(Level.WARNING, "User already exists");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = new User(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getIntensity(), user.isPublic());
        newUser.setProfilePic(imageService.getImage(1L));
        newUser.setProfilePicThumbnail(imageService.getImage(2L));
        logger.log(Level.INFO, "Creating new user");
        return userRepository.save(newUser);
    }

    public boolean updatePassword(String oldPassword, String newPassword) {
        User user = getUser();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            logger.log(Level.WARNING, "Incorrect password");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password");
        }
        if (newPassword.equals("")) {
            logger.log(Level.WARNING, "Password can not be empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password can not be empty");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.log(Level.INFO, "Password changed");
        return true;
    }

    public User updateUser(User updatedUser) {
        User user = getUser();
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setDateOfBirth(updatedUser.getDateOfBirth());
        user.setIntensity(updatedUser.getIntensity());
        user.setPublic(updatedUser.isPublic());
        logger.log(Level.INFO, "User updated");
        return userRepository.save(user);
    }

    public boolean deleteUser() {
        User user = getUser();
        activityService.deleteCreatedBy(user);
        activityService.removeUserFromParticipationLists(user);
        activityService.removeUserFromWaitingLists(user);
        userRepository.delete(user);
        logger.log(Level.INFO, "User deleted");
        return true;
    }

    public User uploadImage(MultipartFile file) throws IOException {
        User user = getUser();
        Image image = new Image(file.getBytes(), StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
        user.setProfilePic(imageService.saveImage(image));
        user.setProfilePicThumbnail(imageService.createThumbnail(image));
        return userRepository.save(user);
    }

    public Image getThumbnail() {
        User user = getUser();
        return imageService.getImage(user.getProfilePicThumbnail().getId());
    }

    public Image getProfilePicture() {
        User user = getUser();
        return imageService.getImage(user.getProfilePic().getId());
    }

    public List<User> getLeaderboard(){
        logger.log(Level.INFO,"Getting leaderboard");
        Pageable pageable = PageRequest.of(0, 25);
        return userRepository.findByIsPublicOrderByPointsDesc(true, pageable).toList();
    }
    public List<Notification> getUnread() {
    User user = getUser();
        List<Notification> notifications = user.getNotifications().stream().filter(n->!n.getRead()).collect(Collectors.toList());
    return notifications;
    }
    public void markAsRead(Notification notification){
        User user = getUser();
        Notification n = user.getNotifications().stream().filter(p -> !p.getRead()&&p.getMessage().equals(notification.getMessage())).findFirst().orElse(null);
        if(n==null){
            return;
        }
        n.setRead(true);
        notificationRepository.save(n);
       /* List<Notification> notifications = user.getNotifications().stream().filter(n->!n.getRead()).collect(Collectors.toList());
        notifications.forEach(e -> {
            e.setRead(true);
            notificationRepository.save(e);
        });
        */
    }

    public List<User> adminGetAllUsers() {
        List<User> users = userRepository.findAll();
        logger.log(Level.INFO, "Getting all users from database(as Admin)");
        return users;
    }

    public User adminCreateUser(User user){
        if (user.getPassword().equals("")) {
            logger.log(Level.WARNING, "Password can not be empty");
            return null;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = new User(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getIntensity(), user.isPublic());
        logger.log(Level.INFO, "Creating new user (as Admin)");
        return userRepository.save(newUser);
    }
    public User adminEditUser(Long id, User updates){
        User user = getById(id);
        if (updates.getPassword().equals("")) {
            logger.log(Level.WARNING, "Password can not be empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password can not be empty");
        }
        if (updates.getEmail().equals("")) {
            logger.log(Level.WARNING, "Email can not be empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email can not be empty");
        }
        user.setEmail(updates.getEmail());
        user.setPassword(passwordEncoder.encode(updates.getPassword()));
        user.setFirstName(updates.getFirstName());
        user.setLastName(updates.getLastName());
        user.setDateOfBirth(updates.getDateOfBirth());
        user.setUserRole(updates.getUserRole());
        user.setIntensity(updates.getIntensity());
        user.setPoints(updates.getPoints());
        user.setProfilePic(updates.getProfilePic());
        user.setPublic(updates.isPublic());
        return userRepository.save(user);
    }
    public boolean adminDeleteUser(Long id){
        User user = getById(id);
        userRepository.delete(user);
        return true;
    }

    public Set<User> getIfExists(Set<User> users) {
        return userRepository.findByEmailIn(users.stream().map(User::getEmail).filter(Objects::nonNull).collect(Collectors.toSet()));
    }

    public void giveUsersPoints(Set<User> users, Activity activity) {
        users.forEach(user -> {
            User u = getByEmail(user.getEmail());
            giveUserPoints(u, activity);
        });
    }

    public void giveUserPoints(User user, Activity activity) {
        double doublePoints = 100 * activity.getIntensity() * Math.sqrt((double) Math.subtractExact(Math.toIntExact(Duration.between(activity.getStart(), activity.getEnd()).getSeconds()), 1) / Math.subtractExact(604800, 1));
        int points = Math.toIntExact(Math.round(doublePoints));
        System.out.println("Giving " + points + " points");
        user.givePoints(points);
        userRepository.save(user);
    }

    public void subtractPoints(User user, Activity activity) {
        double doublePoints = 100 * activity.getIntensity() * Math.sqrt((double) Math.subtractExact(Math.toIntExact(Duration.between(activity.getStart(), activity.getEnd()).getSeconds()), 1) / Math.subtractExact(604800, 1));
        int points = -Math.toIntExact(Math.round(doublePoints));
        System.out.println("Removing " + points + " points");
        user.givePoints(points);
        userRepository.save(user);

    }
}
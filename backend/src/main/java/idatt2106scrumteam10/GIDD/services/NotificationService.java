package idatt2106scrumteam10.GIDD.services;

import idatt2106scrumteam10.GIDD.controllers.WebSocketController;
import idatt2106scrumteam10.GIDD.models.Activity;
import idatt2106scrumteam10.GIDD.models.Notification;
import idatt2106scrumteam10.GIDD.models.User;
import idatt2106scrumteam10.GIDD.repos.NotificationRepository;
import idatt2106scrumteam10.GIDD.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class NotificationService {

    Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.services.NotificationService.class.getName());

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSocketController webSocketController;

    public void sendNotification(Notification notification, User user) {
        webSocketController.sendToUser(user, notification);
        logger.log(Level.INFO, "Notification sent to user");
    }

    public void activityDeleted(Activity activity) {
        List<User> userList = new ArrayList<User>(activity.getParticipants());
        userList.addAll(activity.getWaitingList());
        if (userList.size() > 0) {
            for (User user : userList) {
                Notification notification = new Notification("Aktiviteten: '" + activity.getTitle() + "' har blitt kansellert.");
                notificationRepository.save(notification);
                user.getNotifications().add(notification);
                userRepository.save(user);
                sendNotification(notification, user);
            }
        }
    }

    public void joinedParticipants(Activity activity, User user) {
        Notification notification = new Notification("Du er nå på deltakerlista til aktiviteten: " + activity.getTitle());
        notificationRepository.save(notification);
        user.getNotifications().add(notification);
        userRepository.save(user);
        sendNotification(notification, user);
    }

    public List<Notification> getAllUnread() {
        return notificationRepository.findByRead(false);
    }

}

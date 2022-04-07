package idatt2106scrumteam10.GIDD.controllers;

import idatt2106scrumteam10.GIDD.models.Notification;
import idatt2106scrumteam10.GIDD.models.User;
import idatt2106scrumteam10.GIDD.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.logging.Logger;

@RestController
public class WebSocketController {

    Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.controllers.WebSocketController.class.getName());

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendToUser(User user, Notification notification) {
        simpMessagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/notifications", notification);

    }

}

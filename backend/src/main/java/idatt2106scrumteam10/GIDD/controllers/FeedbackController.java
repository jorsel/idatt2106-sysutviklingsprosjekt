package idatt2106scrumteam10.GIDD.controllers;

import idatt2106scrumteam10.GIDD.models.Feedback;
import idatt2106scrumteam10.GIDD.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class FeedbackController {

    Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.controllers.FeedbackController.class.getName());

    @Autowired
    FeedbackService feedbackService;

    @GetMapping("/feedback")
    public List<Feedback> getAllFeedback() {
        logger.log(Level.INFO, "Get request /feedback");
        return feedbackService.getAllFeedback();
    }

    @PostMapping("/feedback")
    public Feedback sendFeedback(@RequestBody Feedback feedback) {
        logger.log(Level.INFO, "Post request /feedback");
        return feedbackService.sendFeedback(feedback);
    }

}

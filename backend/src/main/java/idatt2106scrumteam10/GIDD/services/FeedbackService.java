package idatt2106scrumteam10.GIDD.services;

import idatt2106scrumteam10.GIDD.models.Feedback;
import idatt2106scrumteam10.GIDD.repos.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FeedbackService {

    Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.services.FeedbackService.class.getName());

    @Autowired
    FeedbackRepository feedbackRepository;

    public List<Feedback> getAllFeedback() {
        logger.log(Level.INFO, "Getting all feedback");
        return feedbackRepository.findAll();
    }

    public Feedback sendFeedback(Feedback feedback) {
        if (feedback.getRating() == 0 || feedback.getContent() == null) {
            logger.log(Level.WARNING, "Rating and content can not be empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating and content can not be empty");
        }
        logger.log(Level.INFO, "Saving new feedback");
        return feedbackRepository.save(feedback);
    }

}

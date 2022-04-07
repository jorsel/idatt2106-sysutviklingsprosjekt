package idatt2106scrumteam10.GIDD.services;

import idatt2106scrumteam10.GIDD.models.Comment;
import idatt2106scrumteam10.GIDD.repos.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CommentService {

    Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.services.CommentService.class.getName());

    @Autowired
    CommentRepository commentRepository;

    public Comment findById(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            logger.log(Level.WARNING, "Comment not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        logger.log(Level.INFO, "Comment with id " + id + " found");
        return comment;
    }

}

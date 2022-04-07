package idatt2106scrumteam10.GIDD.controllers;

import idatt2106scrumteam10.GIDD.models.Tag;
import idatt2106scrumteam10.GIDD.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class TagController {

    Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.controllers.TagController.class.getName());

    @Autowired
    TagService tagService;

    @GetMapping("/tags")
    public List<Tag> getAllTags() {
        logger.log(Level.INFO, "Get request /tags");
        return tagService.findAll();
    }

}

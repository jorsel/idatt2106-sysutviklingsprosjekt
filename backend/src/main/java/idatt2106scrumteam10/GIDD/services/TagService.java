package idatt2106scrumteam10.GIDD.services;

import idatt2106scrumteam10.GIDD.models.Tag;
import idatt2106scrumteam10.GIDD.repos.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class TagService {

    Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.services.TagService.class.getName());

    @Autowired
    TagRepository tagRepository;

    public List<Tag> findAll(){
        return tagRepository.findAll();
    }

    public Tag findByTag(String tag){
        return tagRepository.findByTag(tag).orElse(null);
    }

    public Set<Tag> findByTagIn(Set<String> tags) {
        return tagRepository.findByTagIn(tags);
    }

    public Set<Tag> getIfExists(Set<Tag> tags) {
        return tagRepository.findByTagIn(tags.stream().map(Tag::getTag).collect(Collectors.toSet()));
    }

}

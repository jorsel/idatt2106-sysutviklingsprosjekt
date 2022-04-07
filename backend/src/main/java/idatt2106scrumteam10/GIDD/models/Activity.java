package idatt2106scrumteam10.GIDD.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Activity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Long ID;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDateTime start;

    @Column(nullable = false)
    private LocalDateTime end;

    @Column(nullable = false)
    private int maxAmount;

    @Column(nullable = false)
    private int intensity;

    @ManyToOne
    private Location location;

    @ManyToOne
    private User createdBy;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @ManyToMany
    private Set<Tag> tags;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> participants;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> waitingList;

    @ElementCollection
    private List<String> equipment;

    @OneToOne
    private Image picture;

    public Activity() {
    }

    public Activity(String title, String description, LocalDateTime start, LocalDateTime end, int maxAmount,
                    int intensity, Location location, User createdBy, Set<Tag> tags, List<String> equipment, Image picture) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.maxAmount = maxAmount;
        this.intensity = intensity;
        this.tags = tags;
        this.location = location;
        this.createdBy = createdBy;
        this.equipment = equipment;
        this.picture = picture;
    }

    public Activity(String title, String description, LocalDateTime start, LocalDateTime end, int maxAmount,
        Location location,int intensity, Set<Tag> tags, List<String> equipment) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.maxAmount = maxAmount;
        this.intensity = intensity;
        this.tags = tags;
        this.location = location;
        this.equipment = equipment;

    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public Set<User> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(Set<User> waitingList) {
        this.waitingList = waitingList;
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<String> equipment) {
        this.equipment = equipment;
    }

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return maxAmount == activity.maxAmount &&
                intensity == activity.intensity &&
                Objects.equals(ID, activity.ID) &&
                Objects.equals(title, activity.title) &&
                Objects.equals(description, activity.description) &&
                Objects.equals(start, activity.start) &&
                Objects.equals(end, activity.end) &&
                Objects.equals(comments, activity.comments) &&
                Objects.equals(location, activity.location) &&
                Objects.equals(createdBy, activity.createdBy) &&
                Objects.equals(tags, activity.tags) &&
                Objects.equals(participants, activity.participants) &&
                Objects.equals(waitingList, activity.waitingList) &&
                Objects.equals(equipment, activity.equipment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, title, description, start, end, maxAmount, intensity,
                comments, location, createdBy, tags, participants, waitingList, equipment);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", maxAmount=" + maxAmount +
                ", intensity=" + intensity +
                ", comments=" + comments +
                ", location=" + location +
                ", createdBy=" + createdBy +
                ", tags=" + tags +
                ", participants=" + participants +
                ", waitingList=" + waitingList +
                ", equipment=" + equipment +
                '}';
    }

    public String PostJSONString() {
        try {
            HashMap<String, Object> json = new HashMap<>();
            json.put("title", this.title);
            json.put("description", this.description);
            json.put("start", this.start.toString());
            json.put("end", this.start.toString());
            json.put("maxAmount", this.maxAmount);
            json.put("location", this.location);
            json.put("intensity", this.intensity);
            json.put("tags", this.tags);
            json.put("equipment", this.equipment);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
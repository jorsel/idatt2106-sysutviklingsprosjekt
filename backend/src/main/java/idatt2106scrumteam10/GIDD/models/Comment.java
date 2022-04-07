package idatt2106scrumteam10.GIDD.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Comment {

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
    private String message;

    private LocalDateTime dateTime;

    @OneToOne
    private User user;

    public Comment() {
    }

    public Comment(String message, LocalDateTime dateTime, User user) {
        this.message = message;
        this.dateTime = dateTime;
        this.user = user;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(ID, comment.ID) && Objects.equals(message, comment.message) && Objects.equals(dateTime, comment.dateTime) && Objects.equals(user, comment.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, message, dateTime, user);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "ID=" + ID +
                ", message='" + message + '\'' +
                ", dateTime=" + dateTime +
                ", user=" + user +
                '}';
    }
}

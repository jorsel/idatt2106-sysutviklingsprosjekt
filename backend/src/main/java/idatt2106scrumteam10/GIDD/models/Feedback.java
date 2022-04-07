package idatt2106scrumteam10.GIDD.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Feedback {
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
    private int rating;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    private User user;

    public Feedback(String content, int rating, User user) {
        this.setRating(rating);
        this.content = content;
        this.user = user;
    }

    public Feedback() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 1) rating = 1;
        if (rating > 5) rating = 5;
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "";
    }
}

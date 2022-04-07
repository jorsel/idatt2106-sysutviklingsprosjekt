package idatt2106scrumteam10.GIDD.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Tag {

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


    @Column(nullable = false, unique = true)
    private String tag;

    public Tag(String tag) {
        this.tag = tag;
    }

    public Tag() {
    }

    @JsonIgnore
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag1 = (Tag) o;
        return Objects.equals(ID, tag1.ID) &&
                Objects.equals(tag, tag1.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, tag);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "ID=" + ID +
                ", tag='" + tag + '\'' +
                '}';
    }
}

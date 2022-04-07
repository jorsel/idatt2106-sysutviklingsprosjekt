package idatt2106scrumteam10.GIDD.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Notification {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    Long ID;

    private String message;

    private Boolean read;

    public Notification() {

    }

    public Notification(String message) {
        this.message = message;
        this.read = false;
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

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(ID, that.ID) && Objects.equals(message, that.message) && Objects.equals(read, that.read);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, message, read);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "ID=" + ID +
                ", message='" + message + '\'' +
                ", read=" + read +
                '}';
    }
}

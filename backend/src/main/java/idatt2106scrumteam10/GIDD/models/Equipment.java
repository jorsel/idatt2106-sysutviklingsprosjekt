package idatt2106scrumteam10.GIDD.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Equipment {

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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "activity_id", nullable = true)
    Activity activity;

    @Column(nullable = false)
    private String item;

    @Column(nullable = false)
    private int amount;

    public Equipment(String item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public Equipment() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return amount == equipment.amount &&
                Objects.equals(ID, equipment.ID) &&
                Objects.equals(item, equipment.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, item, amount);
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "ID=" + ID +
                ", item='" + item + '\'' +
                ", amount=" + amount +
                '}';
    }
}

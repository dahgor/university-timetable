package dao.entities;

import java.sql.Timestamp;
import java.util.Objects;

public class TimePeriod {

    private int id;
    private Timestamp startHour;
    private Timestamp endHour;

    public TimePeriod() {
    }

    public TimePeriod(int id, Timestamp startHour, Timestamp endHour) {
        this.id = id;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getStartHour() {
        return startHour;
    }

    public void setStartHour(Timestamp startHour) {
        this.startHour = startHour;
    }

    public Timestamp getEndHour() {
        return endHour;
    }

    public void setEndHour(Timestamp endHour) {
        this.endHour = endHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeriod that = (TimePeriod) o;
        return id == that.id &&
                startHour.equals(that.startHour) &&
                endHour.equals(that.endHour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startHour, endHour);
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
                "id=" + id +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                '}';
    }
}

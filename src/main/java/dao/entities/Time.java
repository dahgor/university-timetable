package dao.entities;

import java.sql.Date;
import java.util.Objects;

public class Time {
    private static final int NO_ID = 0;

    private int id;
    private Date date;
    private int timePeriodId;

    public Time() {
    }

    public Time(Date date, int timePeriodId) {
        this.id = NO_ID;
        this.date = date;
        this.timePeriodId = timePeriodId;
    }

    public Time(int id, Date date, int timePeriodId) {
        this.id = id;
        this.date = date;
        this.timePeriodId = timePeriodId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTimePeriodId() {
        return timePeriodId;
    }

    public void setTimePeriodId(int timePeriodId) {
        this.timePeriodId = timePeriodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return id == time.id &&
                timePeriodId == time.timePeriodId &&
                date.equals(time.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, timePeriodId);
    }

    @Override
    public String toString() {
        return "Time{" +
                "id=" + id +
                ", date=" + date +
                ", timePeriodId=" + timePeriodId +
                '}';
    }
}

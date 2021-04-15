package models;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class ScheduledDay {

    private int id;
    private Date date;
    private Map<TimePeriod, Set<ScheduledClass>> schedule;

    public ScheduledDay() {
    }

    public ScheduledDay(int id, Date date, Map<TimePeriod, Set<ScheduledClass>> schedule) {
        this.id = id;
        this.date = date;
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Map<TimePeriod, Set<ScheduledClass>> getSchedule() {
        return schedule;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSchedule(Map<TimePeriod, Set<ScheduledClass>> schedule) {
        this.schedule = schedule;
    }

}

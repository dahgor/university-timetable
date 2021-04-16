package models.schedule;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import models.TimePeriod;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + id;
        result = prime * result + ((schedule == null) ? 0 : schedule.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScheduledDay other = (ScheduledDay) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (id != other.id)
            return false;
        if (schedule == null) {
            if (other.schedule != null)
                return false;
        } else if (!schedule.equals(other.schedule))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScheduledDay [id=" + id + ", date=" + date + ", scheduledClasses=" + schedule.size() + "]";
    }

}

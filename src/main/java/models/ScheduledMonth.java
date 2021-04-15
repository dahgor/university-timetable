package models;

import java.util.Date;
import java.util.List;

public class ScheduledMonth {

    private int id;
    private Date month;
    private List<ScheduledDay> scheduledDays;

    public ScheduledMonth() {
    }

    public ScheduledMonth(int id, Date month, List<ScheduledDay> scheduledDays) {
        this.id = id;
        this.month = month;
        this.scheduledDays = scheduledDays;
    }

    public int getId() {
        return id;
    }

    public Date getMonth() {
        return month;
    }

    public List<ScheduledDay> getScheduledDays() {
        return scheduledDays;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public void setScheduledDays(List<ScheduledDay> scheduledDays) {
        this.scheduledDays = scheduledDays;
    }

}

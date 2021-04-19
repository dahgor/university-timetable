package domain.models.schedule;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((month == null) ? 0 : month.hashCode());
        result = prime * result + ((scheduledDays == null) ? 0 : scheduledDays.hashCode());
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
        ScheduledMonth other = (ScheduledMonth) obj;
        if (id != other.id)
            return false;
        if (month == null) {
            if (other.month != null)
                return false;
        } else if (!month.equals(other.month))
            return false;
        if (scheduledDays == null) {
            if (other.scheduledDays != null)
                return false;
        } else if (!scheduledDays.equals(other.scheduledDays))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScheduledMonth [id=" + id + ", month=" + month + ", scheduledDays=" + scheduledDays.size() + "]";
    }

}

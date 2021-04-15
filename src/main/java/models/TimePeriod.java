package models;

import java.time.LocalTime;

public class TimePeriod {

    private int id;
    private LocalTime start;
    private LocalTime end;

    public TimePeriod() {
    }

    public TimePeriod(int id, LocalTime start, LocalTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

}

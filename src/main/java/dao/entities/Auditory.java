package dao.entities;

import java.util.Objects;

public class Auditory {

    private int id;
    private String location;

    public Auditory() {
    }

    public Auditory(int id, String location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auditory that = (Auditory) o;
        return id == that.id &&
                location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location);
    }

    @Override
    public String toString() {
        return "AuditoryEntity{" +
                "id=" + id +
                ", location='" + location + '\'' +
                '}';
    }
}
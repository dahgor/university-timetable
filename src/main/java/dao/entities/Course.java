package dao.entities;

import java.util.Objects;

public class Course {
    private static final int NO_ID = 0;

    private int id;
    private String name;
    private String description;

    public Course() {
    }

    public Course(int id) {
        this.id = id;
        this.name = null;
        this.description = null;
    }

    public Course(String name, String description) {
        this.id = NO_ID;
        this.name = name;
        this.description = description;
    }

    public Course(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course that = (Course) o;
        return id == that.id &&
                name.equals(that.name) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

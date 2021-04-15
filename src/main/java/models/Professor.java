package models;

import java.util.Set;

public class Professor {

    private int id;
    private String firstName;
    private String lastName;
    private Set<Course> courses;

    public Professor() {
    }

    public Professor(int id, String firstName, String lastName, Set<Course> courses) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.courses = courses;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

}

package models;

import java.util.Set;

public class Group {

    private int id;
    private String name;
    private Set<Course> courses;
    private Set<Student> students;

    public Group() {
    }

    public Group(int id, String name, Set<Course> courses, Set<Student> students) {
        this.id = id;
        this.name = name;
        this.courses = courses;
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

}

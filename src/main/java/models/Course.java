package models;

import java.util.Set;

public class Course {

    private int id;
    private String name;
    private String description;
    private Set<Professor> professors;
    private Set<Group> groups;

    public Course() {
    }

    public Course(int id, String name, String description, Set<Professor> professors, Set<Group> groups) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.professors = professors;
        this.groups = groups;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Professor> getProfessors() {
        return professors;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProfessors(Set<Professor> professors) {
        this.professors = professors;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

}

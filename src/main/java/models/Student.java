package models;

public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private Group group;

    public Student() {
    }

    public Student(int id, String firstName, String lastName, Group group) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
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

    public Group getGroup() {
        return group;
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

    public void setGroup(Group group) {
        this.group = group;
    }

}

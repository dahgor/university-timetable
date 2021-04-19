package dao.entities;

import java.util.Objects;

public class Student {

    private int id;
    private int groupId;
    private String firstName;
    private String lastName;

    public Student() {
    }

    public Student(int id, int groupId, String firstName, String lastName) {
        this.id = id;
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student that = (Student) o;
        return id == that.id &&
                groupId == that.groupId &&
                firstName.equals(that.firstName) &&
                lastName.equals(that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, firstName, lastName);
    }

    @Override
    public String toString() {
        return "StudentEntity{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

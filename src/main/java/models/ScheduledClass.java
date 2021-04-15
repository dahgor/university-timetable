package models;

public class ScheduledClass {

    private int id;
    private Course course;
    private Professor professor;
    private Group group;
    private Auditory auditory;

    public ScheduledClass() {
    }

    public ScheduledClass(int id, Course course, Professor professor, Group group, Auditory auditory) {
        this.id = id;
        this.course = course;
        this.professor = professor;
        this.group = group;
        this.auditory = auditory;
    }

    public int getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Group getGroup() {
        return group;
    }

    public Auditory getAuditory() {
        return auditory;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setAuditory(Auditory auditory) {
        this.auditory = auditory;
    }

}

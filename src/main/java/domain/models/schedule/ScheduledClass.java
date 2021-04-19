package domain.models.schedule;

import domain.models.Auditory;
import domain.models.Course;
import domain.models.Group;
import domain.models.Professor;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((auditory == null) ? 0 : auditory.hashCode());
        result = prime * result + ((course == null) ? 0 : course.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + id;
        result = prime * result + ((professor == null) ? 0 : professor.hashCode());
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
        ScheduledClass other = (ScheduledClass) obj;
        if (auditory == null) {
            if (other.auditory != null)
                return false;
        } else if (!auditory.equals(other.auditory))
            return false;
        if (course == null) {
            if (other.course != null)
                return false;
        } else if (!course.equals(other.course))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (id != other.id)
            return false;
        if (professor == null) {
            if (other.professor != null)
                return false;
        } else if (!professor.equals(other.professor))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScheduledClass [id=" + id + ", course=" + course.getName() + ", professor=" + professor + ", group="
                + group.getName() + ", auditory=" + auditory.getId() + "]";
    }

}

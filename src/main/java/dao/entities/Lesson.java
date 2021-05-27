package dao.entities;

import java.util.Objects;

public class Lesson {
    private static final int NO_ID = 0;

    private int id;
    private int courseId;
    private int professorId;
    private int groupId;
    private int auditoryId;

    public Lesson() {
    }

    public Lesson(int id) {
        this.id = id;
        this.courseId = NO_ID;
        this.professorId = NO_ID;
        this.groupId = NO_ID;
        this.auditoryId = NO_ID;
    }

    public Lesson(int courseId, int professorId, int groupId, int auditoryId) {
        this.id = NO_ID;
        this.courseId = courseId;
        this.professorId = professorId;
        this.groupId = groupId;
        this.auditoryId = auditoryId;
    }

    public Lesson(int id, int courseId, int professorId, int groupId, int auditoryId) {
        this.id = id;
        this.courseId = courseId;
        this.professorId = professorId;
        this.groupId = groupId;
        this.auditoryId = auditoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getAuditoryId() {
        return auditoryId;
    }

    public void setAuditoryId(int auditoryId) {
        this.auditoryId = auditoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson that = (Lesson) o;
        return id == that.id &&
                courseId == that.courseId &&
                professorId == that.professorId &&
                groupId == that.groupId &&
                auditoryId == that.auditoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseId, professorId, groupId, auditoryId);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", professorId=" + professorId +
                ", groupId=" + groupId +
                ", auditoryId=" + auditoryId +
                '}';
    }
}

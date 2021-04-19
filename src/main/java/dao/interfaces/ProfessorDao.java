package dao.interfaces;

import dao.DaoException;
import dao.entities.Professor;

import java.util.List;

public interface ProfessorDao extends Dao<Professor> {
    List<Professor> findProfessorsForCourse(int courseId) throws DaoException;

    void assignProfessorToCourse(int professorId, int courseId) throws DaoException;
}

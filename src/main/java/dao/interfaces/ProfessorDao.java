package dao.interfaces;

import dao.DaoException;
import dao.entities.Professor;

import java.util.List;

public interface ProfessorDao extends Dao<Professor> {

    void changeFirstName(int professorId, String newFirstName) throws DaoException;

    void changeLastName(int professorId, String newLastName) throws DaoException;

    void assignProfessorToCourse(int professorId, int courseId) throws DaoException;

    void deleteProfessorFromCourse(int professorId, int courseId) throws DaoException;

    List<Professor> findByCourse(int courseId) throws DaoException;
}

package dao.interfaces;

import dao.DaoException;
import dao.entities.Group;

import java.util.List;

public interface GroupDao extends Dao<Group> {

    void changeName(int groupId, String newName) throws DaoException;

    void assignGroupToCourse(int groupId, int courseId) throws DaoException;

    void deleteGroupFromCourse(int groupId, int courseId) throws DaoException;

    List<Group> findByCourse(int courseId) throws DaoException;
}

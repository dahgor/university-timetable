package dao.interfaces;

import dao.DaoException;
import dao.entities.Group;

public interface GroupDao extends Dao<Group> {

    void changeName(int groupId, String newName) throws DaoException;

    void assignGroupToCourse(int groupId, int courseId) throws DaoException;

    void deleteGroupFromCourse(int groupId, int courseId) throws DaoException;
}

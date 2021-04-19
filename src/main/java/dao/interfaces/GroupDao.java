package dao.interfaces;

import dao.DaoException;
import dao.entities.Group;

public interface GroupDao extends Dao<Group> {
    void assignGroupToCourse(int groupId, int courseId) throws DaoException;
}

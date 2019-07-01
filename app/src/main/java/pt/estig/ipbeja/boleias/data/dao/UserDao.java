package pt.estig.ipbeja.boleias.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import java.util.List;
import pt.estig.ipbeja.boleias.data.entity.User;

/**
 * The DAO for the User class, with query methods.
 * Implements the BaseDAO.
 * @see pt.estig.ipbeja.boleias.data.dao.BaseDao
 */
@Dao
public abstract class UserDao implements BaseDao<User> {

    /**
     * Query that searches the 'users' collection in the database
     * @return returns a LIveData list of all the Users
     */
    @Query("SELECT * FROM users")
    public abstract LiveData<List<User>> getUsers();

    /**
     * Query of the 'users' collection in the database
     * @param id ID of the user to select
     * @return returns a selected user
     */
    @Query("SELECT * FROM users WHERE id=:id")
    public abstract LiveData<User> getUser(long id);

}

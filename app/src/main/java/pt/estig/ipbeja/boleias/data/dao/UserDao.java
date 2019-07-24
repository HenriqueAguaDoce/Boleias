package pt.estig.ipbeja.boleias.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import pt.estig.ipbeja.boleias.data.entity.User;

@Dao
public interface UserDao {

    @Insert
    long insert(User user);

    @Query("select * from users")
    List<User> getAllUsers();

    @Delete
    int delete(User user);

    @Query("delete from users")
    int deleteAll();

    @Query(("select * from users where email = :userEmail"))
    User getContact(String userEmail);

    @Query(("select * from users where id = :userId"))
    User getContactFromId(long userId);

    @Query(("UPDATE users SET photo=:photo WHERE id = :userId"))
    void updatePhoto(byte[] photo, long userId);

}

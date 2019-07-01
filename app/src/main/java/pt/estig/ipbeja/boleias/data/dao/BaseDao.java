package pt.estig.ipbeja.boleias.data.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;
import java.util.List;


/**
 * @author henriquead
 * Data Access Object
 * Where we'll have all the CRUD options
 */

public interface BaseDao<T> {

    @Insert
    long insert(T obj);

    @Delete
    int delete(T obj);

    @Update
    int update(T obj);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insert(List<T> obj);

    @Delete
    int delete(List<T> obj);

    @Update
    int update(List<T> obj);
}
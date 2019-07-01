package pt.estig.ipbeja.boleias.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import pt.estig.ipbeja.boleias.data.dao.UserDao;
import pt.estig.ipbeja.boleias.data.entity.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class BoleiasDatabase extends RoomDatabase {

    private static BoleiasDatabase instance;

    public synchronized static BoleiasDatabase getInstance(Context context){
        if (context != context.getApplicationContext()) throw new IllegalArgumentException("Must be application context");

        if (instance == null){
            instance = Room.databaseBuilder(context, BoleiasDatabase.class, "boleias_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();
}

package pt.estig.ipbeja.boleias.data.db;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import android.content.Context;
import androidx.annotation.NonNull;

import pt.estig.ipbeja.boleias.data.dao.UserDao;
import pt.estig.ipbeja.boleias.data.dao.VehicleDao;
import pt.estig.ipbeja.boleias.data.entity.User;
import pt.estig.ipbeja.boleias.data.entity.Vehicle;

@Database(entities = {User.class, Vehicle.class}, version = 4, exportSchema = false)
public abstract class BoleiasDatabase extends RoomDatabase {

    private static BoleiasDatabase instance;

    public synchronized static BoleiasDatabase getInstance(Context context){

        context = context.getApplicationContext();

        if (instance == null){
            instance = Room.databaseBuilder(context, BoleiasDatabase.class, "boleias_db")
//                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();
    public abstract VehicleDao vehicleDao();


    // ------------------------- MIGRATIONS ------------------------- //

//    private static final Migration MIGRATION_1_2 = new Migration(3,4) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//            database.execSQL("ALTER TABLE users ADD photo BLOB default NULL;");
//
//        }
//    };

}

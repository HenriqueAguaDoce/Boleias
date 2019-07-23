package pt.estig.ipbeja.boleias.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import pt.estig.ipbeja.boleias.data.dao.UserDao;
import pt.estig.ipbeja.boleias.data.dao.VehicleDao;
import pt.estig.ipbeja.boleias.data.entity.User;
import pt.estig.ipbeja.boleias.data.entity.Vehicle;

@Database(entities = {Vehicle.class}, version = 3, exportSchema = false)
public abstract class BoleiasDatabase extends RoomDatabase {

    private static BoleiasDatabase instance;

    public synchronized static BoleiasDatabase getInstance(Context context){

        context = context.getApplicationContext();

        if (instance == null){
            instance = Room.databaseBuilder(context, BoleiasDatabase.class, "boleias_db")
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    //.fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    //public abstract UserDao userDao();
    public abstract VehicleDao vehicleDao();


    // ------------------------- MIGRATIONS ------------------------- //

    private static final Migration MIGRATION_1_2 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE contacts ADD photo BLOB default NULL;");

        }
    };

}

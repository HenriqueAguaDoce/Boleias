package pt.estig.ipbeja.boleias.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import pt.estig.ipbeja.boleias.data.entity.Vehicle;

@Dao
public interface VehicleDao {
    @Insert
    long insert(Vehicle vehicle);

    @Query("select * from vehicles")
    List<Vehicle> getAllVehicles();

    @Delete
    int delete(Vehicle vehicle);

    @Query("delete from vehicles")
    int deleteAll();

    @Query(("select * from vehicles where id = :vehicleId"))
    Vehicle getVehicle(long vehicleId);
}

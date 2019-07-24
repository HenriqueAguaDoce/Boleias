package pt.estig.ipbeja.boleias.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

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
    List<Vehicle> getVehicle(long vehicleId);
}

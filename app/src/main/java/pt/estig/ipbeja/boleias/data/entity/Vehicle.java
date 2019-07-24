package pt.estig.ipbeja.boleias.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "vehicles", foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id",
            childColumns = "userId", onDelete = ForeignKey.CASCADE))
public class Vehicle {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long userId;

    private String brand;
    private String model;
    private String fuelType;
    private int seatsNumber;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] photo;

    public Vehicle(long id, long userId, String brand, String model, String fuelType, int seatsNumber, byte[] photo) {
        this.id = id;
        this.userId = userId;
        this.brand = brand;
        this.model = model;
        this.fuelType = fuelType;
        this.seatsNumber = seatsNumber;
        this.photo = photo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }

    public void setSeatsNumber(int seatsNumber) {
        this.seatsNumber = seatsNumber;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}

package pt.estig.ipbeja.boleias.data.entity;

public class Trip {
    private int cost;
    private String startingPlace;
    private String arrivingPlace;
    private String tripDate;
    private String tripTime;
    private int seatsAvailable;

    public Trip() {
    }

    public Trip(int cost, String startingPlace, String arrivingPlace, String tripDate, String tripTime, int seatsAvailable) {
        this.cost = cost;
        this.startingPlace = startingPlace;
        this.arrivingPlace = arrivingPlace;
        this.tripDate = tripDate;
        this.tripTime = tripTime;
        this.seatsAvailable = seatsAvailable;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getStartingPlace() {
        return startingPlace;
    }

    public void setStartingPlace(String startingPlace) {
        this.startingPlace = startingPlace;
    }

    public String getArrivingPlace() {
        return arrivingPlace;
    }

    public void setArrivingPlace(String arrivingPlace) {
        this.arrivingPlace = arrivingPlace;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getTripTime() {
        return tripTime;
    }

    public void setTripTime(String tripTime) {
        this.tripTime = tripTime;
    }

    public int getSeatsAvailable(){
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }
}

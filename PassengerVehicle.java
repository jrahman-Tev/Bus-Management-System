public abstract class PassengerVehicle {
    private String driverName;
    private String vehicleNumber;
    private int maxSeats;
    private int currentPassengers;
    private String busStatus; // "Available", "Boarding", "Running", "Completed", "Maintenance"

    public PassengerVehicle(String driverName, String vehicleNumber, int maxSeats) {
        this.driverName = driverName;
        this.vehicleNumber = vehicleNumber;
        this.maxSeats = maxSeats;
        this.currentPassengers = 0;
        this.busStatus = "Available"; // every vehicle starts as available at the terminal
    }

    public PassengerVehicle(String vehicleNumber, int maxSeats) {
        this("Unassigned", vehicleNumber, maxSeats);
    }

    public abstract double calculateFare(double distanceKm);

    public void boardPassengers(int count, String stopName) throws SeatCapacityExceededException {
        if (count <= 0) {
            throw new IllegalArgumentException("Passenger count must be a positive number (got " + count + ").");
        }
        if (currentPassengers + count > maxSeats) {
            throw new SeatCapacityExceededException(
                vehicleNumber + " cannot board " + count + " passenger(s) at " + stopName +
                " — only " + (maxSeats - currentPassengers) + " seat(s) left."
            );
        }
        currentPassengers += count;
        this.busStatus = "Boarding"; // status updates automatically once passengers start boarding
    }

    public String getDriverName() { return driverName; }
    public String getVehicleNumber() { return vehicleNumber; }
    public int getMaxSeats() { return maxSeats; }
    public int getCurrentPassengers() { return currentPassengers; }
    public String getBusStatus() { return busStatus; }

    public void setDriverName(String driverName) { this.driverName = driverName; }
    public void setBusStatus(String busStatus) { this.busStatus = busStatus; }

    @Override
    public String toString() {
        return String.format("[%s] Driver: %s | Seats: %d/%d | Status: %s",
                vehicleNumber, driverName, currentPassengers, maxSeats, busStatus);
    }
}

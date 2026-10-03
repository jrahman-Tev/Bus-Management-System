public class Bus extends PassengerVehicle implements Trackable {
    private int numberOfDoors;
    private String currentLocation;
    private String busId;

    private static final double BASE_FARE = 10.0;
    private static final double RATE_PER_KM = 2.5;

    // A regular bus realistically seats between 20 and 50 (upper end covers larger/express variants)
    private static final int MIN_SEATS = 20;
    private static final int MAX_SEATS = 50;

    public Bus(String driverName, String vehicleNumber, int maxSeats, int numberOfDoors) {
        super(driverName, vehicleNumber, validateSeats(maxSeats));
        this.numberOfDoors = numberOfDoors;
        this.currentLocation = "Chawkbazar";
        this.busId = vehicleNumber; // default busId to the vehicle number, can be changed via setBusId()
    }

    public Bus(String vehicleNumber, int maxSeats) {
        super(vehicleNumber, validateSeats(maxSeats));
        this.numberOfDoors = 2;
        this.currentLocation = "Chawkbazar";
        this.busId = vehicleNumber;
    }

    // Called before super(...) runs, so an invalid value never gets stored at all
    private static int validateSeats(int maxSeats) {
        if (maxSeats < MIN_SEATS || maxSeats > MAX_SEATS) {
            throw new IllegalArgumentException(
                "A Bus must have between " + MIN_SEATS + " and " + MAX_SEATS + " seats (got " + maxSeats + ")."
            );
        }
        return maxSeats;
    }

    @Override
    public double calculateFare(double distanceKm) {
        return BASE_FARE + (distanceKm * RATE_PER_KM);
    }

    // Overrides the parent's boardPassengers() to also update currentLocation —
    // so the bus's location genuinely reflects wherever it last boarded passengers.
    @Override
    public void boardPassengers(int count, String stopName) throws SeatCapacityExceededException {
        super.boardPassengers(count, stopName); // still does the capacity check + throws if needed
        this.currentLocation = stopName;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    @Override
    public String getCurrentLocation() {
        return "Bus " + getBusId() + " is at " + currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    @Override
    public String toString() {
        return super.toString() + " | Type: Bus | Doors: " + numberOfDoors + " | At: " + currentLocation;
    }
}

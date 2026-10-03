public class Tempo extends PassengerVehicle {
    private boolean hasMeter;

    private static final double BASE_FARE = 5.0;
    private static final double RATE_PER_KM = 3.0;
    private static final double NIGHT_SURCHARGE_PERCENT = 0.20;

    // A CNG tempo realistically seats between 4 and 15 passengers
    private static final int MIN_SEATS = 4;
    private static final int MAX_SEATS = 15;

    public Tempo(String driverName, String vehicleNumber, int maxSeats, boolean hasMeter) {
        super(driverName, vehicleNumber, validateSeats(maxSeats));
        this.hasMeter = hasMeter;
    }

    public Tempo(String vehicleNumber, int maxSeats) {
        super(vehicleNumber, validateSeats(maxSeats));
        this.hasMeter = false;
    }

    private static int validateSeats(int maxSeats) {
        if (maxSeats < MIN_SEATS || maxSeats > MAX_SEATS) {
            throw new IllegalArgumentException(
                "A Tempo must have between " + MIN_SEATS + " and " + MAX_SEATS + " seats (got " + maxSeats + ")."
            );
        }
        return maxSeats;
    }

    @Override
    public double calculateFare(double distanceKm) {
        return BASE_FARE + (distanceKm * RATE_PER_KM);
    }

    public double calculateFare(double distanceKm, boolean nightCharge) {
        double fare = calculateFare(distanceKm);
        if (nightCharge) {
            fare += fare * NIGHT_SURCHARGE_PERCENT;
        }
        return fare;
    }

    public boolean isHasMeter() {
        return hasMeter;
    }

    @Override
    public String toString() {
        return super.toString() + " | Type: Tempo | Meter: " + (hasMeter ? "Yes" : "No");
    }
}

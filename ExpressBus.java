public class ExpressBus extends Bus {
    private boolean skipsMinorStops;

    private static final double EXPRESS_SURCHARGE = 15.0;

    // An express bus is a larger vehicle, realistically 45 to 50 seats
    private static final int MIN_SEATS = 45;
    private static final int MAX_SEATS = 50;

    public ExpressBus(String driverName, String vehicleNumber, int maxSeats, int numberOfDoors, boolean skipsMinorStops) {
        super(driverName, vehicleNumber, validateSeats(maxSeats), numberOfDoors);
        this.skipsMinorStops = skipsMinorStops;
    }

    private static int validateSeats(int maxSeats) {
        if (maxSeats < MIN_SEATS || maxSeats > MAX_SEATS) {
            throw new IllegalArgumentException(
                "An ExpressBus must have between " + MIN_SEATS + " and " + MAX_SEATS + " seats (got " + maxSeats + ")."
            );
        }
        return maxSeats;
    }

    @Override
    public double calculateFare(double distanceKm) {
        double baseFare = super.calculateFare(distanceKm);
        return baseFare + EXPRESS_SURCHARGE;
    }

    public boolean isSkipsMinorStops() {
        return skipsMinorStops;
    }

    @Override
    public String toString() {
        return super.toString() + " | Express: " + (skipsMinorStops ? "Yes (skips minor stops)" : "No");
    }
}

import java.util.ArrayList;
import java.util.HashMap;

public class RouteManager implements Trackable {
    private ArrayList<PassengerVehicle> vehicles = new ArrayList<>();
    private HashMap<String, PassengerVehicle> vehicleByNumber = new HashMap<>();
    private String depotLocation;
    private String depotId;

    public RouteManager(String depotLocation) {
        this.depotLocation = depotLocation;
        this.depotId = "DEPOT-01"; // default id, can be changed via setDepotId()
    }

    // ADD operation — rejects duplicate vehicle numbers using the HashMap for a fast lookup
    public boolean registerVehicle(PassengerVehicle v) {
        if (vehicleByNumber.containsKey(v.getVehicleNumber())) {
            return false; // duplicate — registration refused
        }
        vehicles.add(v);
        vehicleByNumber.put(v.getVehicleNumber(), v);
        return true;
    }

    // SEARCH operation — HashMap lookup by key, O(1) instead of scanning the list
    public PassengerVehicle findVehicle(String vehicleNumber) {
        return vehicleByNumber.get(vehicleNumber);
    }

    // REMOVE operation — keeps both structures in sync
    public boolean removeVehicle(String vehicleNumber) {
        PassengerVehicle v = vehicleByNumber.remove(vehicleNumber);
        if (v == null) {
            return false;
        }
        vehicles.remove(v);
        return true;
    }

    // Convenience method the GUI calls: looks a vehicle up, then boards passengers on it
    public void boardPassengersOn(String vehicleNumber, int count, String stopName)
            throws SeatCapacityExceededException {
        PassengerVehicle v = findVehicle(vehicleNumber);
        if (v == null) {
            throw new IllegalArgumentException("No vehicle registered with number: " + vehicleNumber);
        }
        v.boardPassengers(count, stopName);
    }

    // ITERATE — builds a display string by looping over the ArrayList
    public String listAllVehicles() {
        if (vehicles.isEmpty()) {
            return "No vehicles registered yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (PassengerVehicle v : vehicles) {
            sb.append(v.toString()).append("\n");
        }
        return sb.toString();
    }

    // Dashboard summary — counts vehicles by status and totals today's boarded passengers,
    // by looping over the same ArrayList (no new class needed for this).
    public String getDashboardSummary() {
        int available = 0, boarding = 0, running = 0, maintenance = 0, completed = 0;
        int totalPassengersToday = 0;

        for (PassengerVehicle v : vehicles) {
            totalPassengersToday += v.getCurrentPassengers();
            switch (v.getBusStatus()) {
                case "Available":   available++;   break;
                case "Boarding":    boarding++;     break;
                case "Running":     running++;      break;
                case "Maintenance": maintenance++;  break;
                case "Completed":   completed++;    break;
            }
        }

        return String.format(
            "======================================\n" +
            "      CHAWKBAZAR BUS CONTROL SYSTEM\n" +
            "======================================\n" +
            "Total Buses        : %d\n" +
            "Available           : %d\n" +
            "Boarding            : %d\n" +
            "Running             : %d\n" +
            "Maintenance         : %d\n" +
            "Completed           : %d\n" +
            "--------------------------------------\n" +
            "Today's Passengers  : %d\n" +
            "======================================",
            vehicles.size(), available, boarding, running, maintenance, completed, totalPassengersToday
        );
    }

    public ArrayList<PassengerVehicle> getVehicles() {
        return vehicles;
    }

    public int getVehicleCount() {
        return vehicles.size();
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    @Override
    public String getCurrentLocation() {
        return "Depot " + getDepotId() + " is at " + depotLocation;
    }
}

import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Project {
    public static void main(String[] args) {
        // ===== Phase 2 recap: hierarchy + polymorphism (dynamic binding) =====
        Bus bus1 = new Bus("Karim Mia", "CTG-B-1234", 40, 2);
        System.out.println("--- Full Vehicle Details ---");
        System.out.println(bus1);

        PassengerVehicle[] fleet = {
            new Bus("Rahim Uddin", "CTG-B-5678", 35, 2),
            new ExpressBus("Jamal Khan", "CTG-E-9999", 48, 2, true)
        };
        for (PassengerVehicle v : fleet) {
            System.out.println(v); // full toString() for every vehicle, not just bus1
        }

        Tempo tempo1 = new Tempo("Sabbir Ahmed", "CTG-T-4321", 4, true);
        System.out.println(tempo1); // full toString() for Tempo too

        System.out.println("\n--- Fleet Fare Check (20km) ---");
        for (PassengerVehicle v : fleet) {
            System.out.printf("%s -> Fare: %.2f%n", v.getVehicleNumber(), v.calculateFare(20));
        }

        System.out.println("\n--- Tempo Overloaded Fare Test (10km) ---");
        System.out.printf("Day fare:   %.2f%n", tempo1.calculateFare(10));
        System.out.printf("Night fare: %.2f%n", tempo1.calculateFare(10, true));

        // ===== Phase 3: Trackable interface used polymorphically =====
        RouteManager manager = new RouteManager("Chawkbazar Depot");
        Trackable[] trackables = { bus1, manager };

        System.out.println("\n--- Trackable Interface Test ---");
        for (Trackable t : trackables) {
            System.out.println(t.getCurrentLocation());
        }

        // ===== Phase 3: Exception handling — custom exception + built-in exception =====
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Boarding Test ---");
        System.out.print("Enter number of passengers to board on " + bus1.getVehicleNumber() + ": ");

        try {
            int passengers = Integer.parseInt(sc.nextLine().trim());
            bus1.boardPassengers(passengers, "Chawkbazar");
            System.out.println("Boarded successfully: " + bus1);
        } catch (SeatCapacityExceededException e) {
            System.out.println("Booking failed: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: please enter a whole number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }

        sc.close();

        // ===== Phase 4: launch the GUI =====
        SwingUtilities.invokeLater(() -> new RouteGUI().setVisible(true));
    }
}

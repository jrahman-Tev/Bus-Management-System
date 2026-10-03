import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RouteGUI extends JFrame {
    private RouteManager routeManager;

    // Register panel fields
    private JTextField driverNameField;
    private JTextField vehicleNumberField;
    private JTextField maxSeatsField;
    private JComboBox<String> vehicleTypeBox;

    // Board panel fields
    private JTextField boardVehicleNumberField;
    private JTextField passengerCountField;
    private JComboBox<String> stopBox;

    private JTextArea statusArea;
    private JTextArea dashboardArea;
    private JTextArea seatMapArea;
    private String lastBoardedVehicle; // remembers which vehicle to show the seat map for

    public RouteGUI() {
        super("Chawkbazar Bus Control & Management System");
        this.routeManager = new RouteManager("Chawkbazar Depot");

        setSize(650, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanels = new JPanel(new GridLayout(2, 1));
        topPanels.add(buildRegisterPanel());
        topPanels.add(buildBoardPanel());

        add(topPanels, BorderLayout.NORTH);
        add(buildOutputTabs(), BorderLayout.CENTER);
    }

    private JPanel buildRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Register Vehicle"));

        panel.add(new JLabel("Driver Name:"));
        driverNameField = new JTextField();
        panel.add(driverNameField);

        panel.add(new JLabel("Vehicle Number:"));
        vehicleNumberField = new JTextField();
        panel.add(vehicleNumberField);

        panel.add(new JLabel("Max Seats:"));
        maxSeatsField = new JTextField();
        panel.add(maxSeatsField);

        panel.add(new JLabel("Vehicle Type:"));
        vehicleTypeBox = new JComboBox<>(new String[]{"Bus", "Tempo", "ExpressBus"});
        panel.add(vehicleTypeBox);

        JButton registerBtn = new JButton("Register Vehicle");
        registerBtn.addActionListener(this::handleRegister);
        panel.add(new JLabel()); // spacer
        panel.add(registerBtn);

        return panel;
    }

    private JPanel buildBoardPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Board Passengers"));

        panel.add(new JLabel("Vehicle Number:"));
        boardVehicleNumberField = new JTextField();
        panel.add(boardVehicleNumberField);

        panel.add(new JLabel("Passenger Count:"));
        passengerCountField = new JTextField();
        panel.add(passengerCountField);

        panel.add(new JLabel("Stop:"));
        stopBox = new JComboBox<>(new String[]{"Chawkbazar", "Bahaddarhat", "Raozan", "Rangunia", "Chondrogona", "Kaptai"});
        panel.add(stopBox);

        JButton boardBtn = new JButton("Board Passengers");
        boardBtn.addActionListener(this::handleBoard);
        panel.add(new JLabel()); // spacer
        panel.add(boardBtn);

        return panel;
    }

    private JTabbedPane buildOutputTabs() {
        JTabbedPane tabs = new JTabbedPane();

        dashboardArea = new JTextArea();
        dashboardArea.setEditable(false);
        dashboardArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        tabs.addTab("Dashboard", new JScrollPane(dashboardArea));

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        tabs.addTab("Fleet List", new JScrollPane(statusArea));

        seatMapArea = new JTextArea();
        seatMapArea.setEditable(false);
        seatMapArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        tabs.addTab("Seat Map", new JScrollPane(seatMapArea));

        refreshStatus();
        return tabs;
    }

    // Builds a seat-grid display like a real booking screen: filled seats vs. empty seats
    private String buildSeatGrid(PassengerVehicle v) {
        StringBuilder sb = new StringBuilder();
        sb.append(v.getVehicleNumber()).append("\n");
        sb.append("Total Seats     : ").append(v.getMaxSeats()).append("\n");
        sb.append("Booked Seats    : ").append(v.getCurrentPassengers()).append("\n");
        sb.append("Available Seats : ").append(v.getMaxSeats() - v.getCurrentPassengers()).append("\n\n");

        int booked = v.getCurrentPassengers();
        for (int seat = 1; seat <= v.getMaxSeats(); seat++) {
            String label = (seat <= booked) ? "[X]" : String.format("%02d", seat);
            sb.append(label).append(" ");
            if (seat % 8 == 0) sb.append("\n"); // 8 seats per row, like a real bus layout
        }
        return sb.toString();
    }

    private void handleRegister(ActionEvent e) {
        try {
            String driverName = driverNameField.getText().trim();
            String vehicleNumber = vehicleNumberField.getText().trim();
            int maxSeats = Integer.parseInt(maxSeatsField.getText().trim()); // may throw NumberFormatException
            String type = (String) vehicleTypeBox.getSelectedItem();

            PassengerVehicle vehicle;
            switch (type) {
                case "Tempo":
                    vehicle = new Tempo(driverName, vehicleNumber, maxSeats, true);
                    break;
                case "ExpressBus":
                    vehicle = new ExpressBus(driverName, vehicleNumber, maxSeats, 2, true);
                    break;
                default:
                    vehicle = new Bus(driverName, vehicleNumber, maxSeats, 2);
            }

            boolean added = routeManager.registerVehicle(vehicle);
            if (added) {
                JOptionPane.showMessageDialog(this, "Vehicle registered successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "A vehicle with this number is already registered.",
                        "Duplicate Vehicle", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Max Seats must be a whole number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            // thrown by Bus/Tempo/ExpressBus's own seat-range validation
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Invalid Seat Count", JOptionPane.ERROR_MESSAGE);
        }
        refreshStatus();
    }

    private void handleBoard(ActionEvent e) {
        try {
            String vehicleNumber = boardVehicleNumberField.getText().trim();
            int count = Integer.parseInt(passengerCountField.getText().trim()); // may throw NumberFormatException
            String stop = (String) stopBox.getSelectedItem();

            routeManager.boardPassengersOn(vehicleNumber, count, stop); // may throw SeatCapacityExceededException
            lastBoardedVehicle = vehicleNumber; // remember for the Seat Map tab
            JOptionPane.showMessageDialog(this, "Passengers boarded successfully.");
        } catch (SeatCapacityExceededException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Booking Failed", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Passenger Count must be a whole number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Vehicle Not Found", JOptionPane.ERROR_MESSAGE);
        }
        refreshStatus();
    }

    private void refreshStatus() {
        if (statusArea != null) {
            statusArea.setText(routeManager.listAllVehicles());
        }
        if (dashboardArea != null) {
            dashboardArea.setText(routeManager.getDashboardSummary());
        }
        if (seatMapArea != null) {
            if (lastBoardedVehicle != null) {
                PassengerVehicle v = routeManager.findVehicle(lastBoardedVehicle);
                seatMapArea.setText(v != null ? buildSeatGrid(v) : "Vehicle not found.");
            } else {
                seatMapArea.setText("Board passengers on a vehicle to see its seat map here.");
            }
        }
    }
}

# Chawkbazar Bus Control & Management System

A Java Swing application that simulates a bus terminal managing a fleet of buses and tempos along a real route: **Chawkbazar → Bahaddarhat → Raozan → Rangunia → Chondrogona → Kaptai**.

Built for **CSE 1116: Object Oriented Programming Laboratory**, Premier University, Chattogram.

## Features

- Register buses, tempos, and express buses with driver, vehicle number, and seat capacity
- Board passengers at any stop on the route, with automatic capacity checking
- Track each vehicle's live operational status (Available / Boarding / Running / Maintenance / Completed)
- A fleet-wide dashboard, a full vehicle list, and a per-vehicle seat map — all updated live
- Prevents duplicate vehicle registration and unrealistic seat counts

## OOP Concepts Demonstrated

| Concept | Where |
|---|---|
| Abstract class | `PassengerVehicle` |
| Multilevel inheritance | `PassengerVehicle` → `Bus`/`Tempo` → `ExpressBus` |
| Interface implemented by unrelated classes | `Trackable`, implemented by `Bus` and `RouteManager` |
| Method overriding | `calculateFare()` in every subclass |
| Method overloading | `Tempo.calculateFare(km)` / `calculateFare(km, nightCharge)` |
| Custom checked exception | `SeatCapacityExceededException` |
| Collections Framework | `ArrayList` + `HashMap` in `RouteManager` |
| GUI | `RouteGUI` (Java Swing) |

## Project Structure

```
src/
├── Project.java                       # Entry point (main)
├── PassengerVehicle.java              # Abstract parent class
├── Bus.java                           # Child class (implements Trackable)
├── Tempo.java                         # Child class
├── ExpressBus.java                    # Grandchild class (extends Bus)
├── Trackable.java                     # Interface
├── RouteManager.java                  # Collections-based composition class
├── SeatCapacityExceededException.java # Custom checked exception
└── RouteGUI.java                      # Swing GUI
```

## How to Run

Requires a JDK (8 or later).

```bash
cd src
javac *.java
java Project
```

The console will first print a few demonstration tests (fare calculation, polymorphism, exception handling), then the GUI window will open.

## Known Limitations

- Data is not persisted — everything resets when the program closes
- The GUI does not validate blank Driver Name / Vehicle Number fields
- `RouteManager.removeVehicle()` exists but has no GUI button yet

## Author

[Your Name] — CSE 1116, Premier University, Chattogram

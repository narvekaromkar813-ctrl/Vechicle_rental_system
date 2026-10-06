# Case Study 12: Vehicle Rental Management System

**Academic Course:** Java Programming (Semester III)  
**Institution:** ITM Skills University, School of Future Tech  
**Degree:** B.Tech Computer Science and Engineering (2025–29)  
**Student Name:** Omkar Narvekar  

---

## 1. Project Title
**Vehicle Rental Management System** — A complete Java Swing desktop application designed for fleet management, customer registration, vehicle booking and returns, real-time availability tracking, dynamic rental charge calculations, billing, and reporting.

---

## 2. Problem Statement
A vehicle rental company requires an organized software platform to manage its fleet of vehicles, registered customers, active bookings, return processes, availability status, and rental transaction history. The system must allow company staff to:
- Identify available vehicles instantly.
- Register and maintain customer records.
- Create rental bookings with automated charge calculation.
- Process vehicle returns, restore fleet availability, and compute final dues.
- Maintain permanent, chronological rental history.
- Search and sort fleet records using industry-standard algorithmic structures.

---

## 3. Case Study Objectives
1. **Manage vehicle and customer information:** Full CRUD operations for both fleets and clients.
2. **Track vehicle availability:** Real-time tracking using synchronized primitive boolean arrays and object states.
3. **Book and return vehicles:** Seamless lifecycle from reservation to return.
4. **Calculate rental charges:** Automated duration and rate computation (`Days × Rate/Day`).
5. **Maintain rental history:** Persistent chronological ledger of completed rentals.
6. **Search and sort vehicle records:** High-efficiency lookups and flexible sorting criteria.

---

## 4. Key Features
- **Operational Dashboard:** Live counters for total fleet, available vehicles, rented vehicles, customer count, active bookings, completed bookings, and gross revenue.
- **Fleet Management (CRUD):** Add, update, delete, view, and inspect vehicles (Sedan, SUV, Hatchback, Bike, Van). Prevents deleting rented vehicles.
- **Customer Registration (CRUD):** Form validation for 10-digit phone numbers, valid email formats, and driving license IDs.
- **Fleet Availability Tracker:** Visualizes the `boolean[] availabilityStatus` primitive array mapped against fleet indices.
- **Search & Sort Module:**
  - $O(1)$ fast vehicle lookup via `HashMap<String, Vehicle>`.
  - Natural sorted order via `TreeMap<String, Vehicle>`.
  - Multi-attribute sorting (Price low-to-high, high-to-low, Model, Type) using `Comparator<Vehicle>`.
- **Rental Booking Engine:** Validates date intervals, verifies vehicle availability, checks out vehicles, and updates the availability array.
- **Vehicle Return Desk:** Processes returns, validates actual return dates against booking dates, recalculates final fees, frees up the vehicle, and updates `LinkedList<Rental>`.
- **Billing & Invoice Generator:** Generates a formatted printable invoice receipt with customer, vehicle, duration, and financial breakdown.
- **Operations Reports:** Aggregated ledger combining active bookings and completed history with total revenue metrics.

---

## 5. Technologies Used
- **Language:** Java 17+ (SE Development Kit)
- **GUI Framework:** Java Swing (`JFrame`, `JPanel`, `JLabel`, `JTextField`, `JComboBox`, `JButton`, `JTable`, `JScrollPane`, `JOptionPane`, `CardLayout`, `GridBagLayout`)
- **Architecture Pattern:** Model-View-Service-Repository (MVSR) architecture with clean separation of concerns
- **Data Persistence:** In-memory Java Collections Framework (`ArrayList`, `LinkedList`, `HashMap`, `TreeMap`, and primitive `boolean[]` array)

---

## 6. Project Structure

```
Java CaseStudy/
├── run.sh                                   # One-click compile & run script
├── test.sh                                  # Automated integration test runner
├── README.md                                # Comprehensive academic documentation & viva prep
└── src/
    └── com/
        └── vehiclerental/
            ├── Main.java                    # Application entry point
            │
            ├── model/                       # Data Model Classes
            │   ├── Vehicle.java             # Fleet vehicle entity
            │   ├── Customer.java            # Registered customer entity
            │   ├── Rental.java              # Rental transaction entity
            │   └── Invoice.java             # Billing & receipt entity
            │
            ├── repository/                  # In-Memory Storage & Central State
            │   └── DataStore.java           # Central repository holding collections & array
            │
            ├── service/                     # Business Logic Layer
            │   ├── VehicleService.java      # CRUD, HashMap search, TreeMap & Comparator sort
            │   ├── CustomerService.java     # Customer CRUD and validations
            │   ├── RentalService.java       # Booking, return lifecycle, charge math
            │   ├── BillingService.java      # Invoice generation
            │   └── ReportService.java       # KPI calculations and reporting
            │
            ├── exception/                   # Custom Exception Hierarchy
            │   ├── VehicleNotAvailableException.java  # Thrown when booking rented vehicle
            │   ├── InvalidRentalException.java        # Thrown on invalid date ranges
            │   └── ValidationException.java           # Thrown on malformed inputs
            │
            ├── util/                        # Helpers
            │   ├── DateUtil.java            # Date parsing, formatting, difference math
            │   └── ValidationUtil.java      # Regex and bounds checks
            │
            ├── gui/                         # Swing Desktop User Interface
            │   ├── UIStyle.java             # Clean, traditional student-project styling
            │   ├── MainFrame.java           # Main desktop container & sidebar navigation
            │   ├── DashboardPanel.java      # Operational metrics overview
            │   ├── VehiclePanel.java        # Vehicle CRUD panel
            │   ├── CustomerPanel.java       # Customer registration panel
            │   ├── AvailabilityPanel.java   # boolean[] array visualizer
            │   ├── SearchPanel.java         # HashMap search & TreeMap sorting
            │   ├── BookingPanel.java        # Rental reservation desk
            │   ├── ReturnPanel.java         # Vehicle check-in & return
            │   ├── HistoryPanel.java        # LinkedList history ledger
            │   ├── BillingPanel.java        # Text invoice viewer
            │   └── ReportPanel.java         # Operations and transaction report
            │
            └── test/
                └── SystemIntegrationTest.java # 19 automated integration test cases
```

---

## 7. How to Compile and Run

### Prerequisites
- Java JDK 17 or higher installed (`javac` and `java` available in PATH).

### Running via Terminal
1. Open Terminal in the project root:
   ```bash
   cd "/Users/omkarnarvekar/Desktop/Java CaseStudy"
   ```
2. Run the application:
   ```bash
   ./run.sh
   ```
   *Alternatively, compile and launch manually:*
   ```bash
   mkdir -p out
   javac -d out $(find src -name "*.java")
   java -cp out com.vehiclerental.Main
   ```

### Running Automated Test Suite
To verify all 19 test cases covering every required Java concept:
```bash
./test.sh
```

---

## 8. Summary Table of Required Java Concepts

| Java Concept | Where Used in Code | Practical Purpose in Project |
| :--- | :--- | :--- |
| **Classes & Objects** | `Vehicle`, `Customer`, `Rental`, `Invoice` | Models real-world entities with fields, getters/setters, and methods |
| **Constructors** | Model classes (`Vehicle(...)`, `Customer(...)`, etc.) | Initializes state of newly instantiated objects |
| **Primitive Array** | `boolean[] availabilityStatus` in `DataStore.java` | Tracks availability status of the fleet by vehicle index |
| **ArrayList** | `ArrayList<Vehicle>` & `ArrayList<Customer>` | Stores dynamic records allowing resizeable, indexed storage |
| **LinkedList** | `LinkedList<Rental> rentalHistory` in `DataStore.java` | Maintains chronological history of completed rentals ($O(1)$ head insertion) |
| **HashMap** | `HashMap<String, Vehicle> vehicleMap` in `DataStore.java` | Provides $O(1)$ instantaneous search by Registration Number key |
| **TreeMap** | `TreeMap<String, Vehicle> sortedVehicles` in `DataStore.java` | Keeps vehicle fleet automatically sorted alphabetically by Registration Number |
| **CRUD Operations** | `VehiclePanel`, `CustomerPanel`, `VehicleService` | Full Create, Read, Update, and Delete capabilities |
| **Searching** | `SearchPanel.java`, `VehicleService.java` | HashMap lookup by Reg No; linear search by Model and Type |
| **Sorting** | `SearchPanel.java`, `VehicleService.java` | TreeMap key sorting and `Comparator<Vehicle>` (Price, Model, Type) |
| **Java Swing GUI** | `MainFrame`, `JPanel`, `JTable`, `JOptionPane`, etc. | Desktop GUI with clean, professional student-project layout |
| **Exception Handling** | `VehicleNotAvailableException`, `InvalidRentalException` | Handles runtime edge cases, preventing invalid system state |
| **Validation** | `ValidationUtil.java` | Validates phone numbers (10 digits), emails, dates, and price $> 0$ |

---

## 9. In-Depth Explanation of Collections & Arrays

### 1. Primitive Array: `boolean[] availabilityStatus`
- **Location:** [DataStore.java](file:///Users/omkarnarvekar/Desktop/Java%20CaseStudy/src/com/vehiclerental/repository/DataStore.java) and [AvailabilityPanel.java](file:///Users/omkarnarvekar/Desktop/Java%20CaseStudy/src/com/vehiclerental/gui/AvailabilityPanel.java)
- **Implementation:**
  ```java
  public synchronized void syncAvailabilityArray() {
      availabilityStatus = new boolean[vehicles.size()];
      for (int i = 0; i < vehicles.size(); i++) {
          availabilityStatus[i] = vehicles.get(i).isAvailable();
      }
  }
  ```
- **Explanation for Viva:** When a vehicle at index `i` is booked or returned, `availabilityStatus[i]` is updated accordingly. The `AvailabilityPanel` displays this exact primitive array alongside the fleet list.

### 2. `ArrayList<Vehicle>` and `ArrayList<Customer>`
- **Location:** [DataStore.java](file:///Users/omkarnarvekar/Desktop/Java%20CaseStudy/src/com/vehiclerental/repository/DataStore.java)
- **Why ArrayList?** Vehicles and customers are added dynamically. Unlike fixed arrays, `ArrayList` dynamically resizes as new vehicles and customers are registered.

### 3. `LinkedList<Rental> rentalHistory`
- **Location:** [DataStore.java](file:///Users/omkarnarvekar/Desktop/Java%20CaseStudy/src/com/vehiclerental/repository/DataStore.java#L235-L250) and [HistoryPanel.java](file:///Users/omkarnarvekar/Desktop/Java%20CaseStudy/src/com/vehiclerental/gui/HistoryPanel.java)
- **Why LinkedList?** Rental returns represent a chronological event stream. When a rental is closed, `rentalHistory.addFirst(rental)` inserts the record at the head of the doubly-linked list in $O(1)$ constant time.

### 4. `HashMap<String, Vehicle> vehicleMap`
- **Location:** [DataStore.java](file:///Users/omkarnarvekar/Desktop/Java%20CaseStudy/src/com/vehiclerental/repository/DataStore.java#L140-L150) and [VehicleService.java](file:///Users/omkarnarvekar/Desktop/Java%20CaseStudy/src/com/vehiclerental/service/VehicleService.java#L100-L110)
- **Key:** Registration Number (`String`, uppercase)
- **Value:** `Vehicle` object reference
- **Why HashMap?** Searching through a fleet of hundreds of cars linearly takes $O(N)$ time. `HashMap` uses hashing to find the vehicle in $O(1)$ constant time.

### 5. `TreeMap<String, Vehicle> sortedVehicles`
- **Location:** [DataStore.java](file:///Users/omkarnarvekar/Desktop/Java%20CaseStudy/src/com/vehiclerental/repository/DataStore.java#L145-L155) and [SearchPanel.java](file:///Users/omkarnarvekar/Desktop/Java%20CaseStudy/src/com/vehiclerental/gui/SearchPanel.java#L160-L175)
- **Key:** Registration Number (`String`)
- **Why TreeMap?** Built on a Red-Black Tree data structure, `TreeMap` automatically maintains all entries sorted in natural ascending alphabetical order of keys with $O(\log N)$ guarantee.

---

## 10. Demonstration Walkthrough Flow for Viva

Follow these exact steps during your faculty demonstration:

1. **Launch App:** Run `./run.sh`.
2. **Dashboard:** Point out the live KPI metrics (Total Vehicles: 5, Available: 4, Rented: 1, Total Customers: 3, Gross Revenue).
3. **Vehicle CRUD (Vehicle Management screen):**
   - Click on an existing vehicle in the table; observe the form populate automatically.
   - Add a new vehicle (e.g., `MH12XY9999`, Brand: `Tata`, Model: `Nexon`, Type: `SUV`, Rate: `2200`). Show that it appears in the table.
   - Update its daily rate to `2400`.
   - Delete the newly added vehicle.
   - Attempt to delete `MH02CD5678` (Hyundai Creta) which is currently rented. Show the `JOptionPane` preventing deletion because it is in active use.
4. **Customer Registration:** Add a new customer. Try entering an invalid phone number (e.g. 5 digits) or invalid email to demonstrate `ValidationException`.
5. **Vehicle Availability Screen:**
   - Show the table displaying `availabilityStatus[i]`. Explain how index `[i]` reflects the primitive array.
   - Filter by "Available Vehicles Only".
6. **Search & Sort Screen:**
   - Select **Search By:** `Registration Number (HashMap O(1))` and enter `MH01AB1234`. Show the algorithm info banner explaining `vehicleMap.get(...)`.
   - Select **Sort By:** `Registration Number (TreeMap)` to demonstrate red-black tree ordering.
   - Select **Sort By:** `Rental Price: Low to High` to demonstrate `Comparator<Vehicle>`.
7. **Rental Booking Screen:**
   - Select Customer `C001` (Omkar Narvekar) and Vehicle `MH01AB1234` (Honda City).
   - Enter booking dates. Click **Calculate Charge**. Observe the duration and price calculation.
   - Click **Confirm Booking**. The vehicle is booked and marked unavailable.
   - Try booking `MH01AB1234` again immediately to show `VehicleNotAvailableException` in action!
8. **Vehicle Return Screen:**
   - Select the active rental just created.
   - Click **Complete Return**. Notice the prompt asking to view the generated bill.
9. **Billing & Invoicing Screen:**
   - View the generated receipt containing all customer, vehicle, duration, and financial breakdown details.
10. **Rental History Screen:**
    - Show the completed rental appearing at the top of the `LinkedList<Rental>`.
11. **Reports Screen:**
    - Show the updated gross revenue, completed rentals count, and updated fleet utilization metrics.

---

## 11. Viva Questions and Answers

### Q1: What is the difference between `ArrayList` and `LinkedList` in your project?
> **Answer:** `ArrayList` is backed by a dynamic resizing array, making it ideal for `vehicles` and `customers` where index-based access ($O(1)$ `get(i)`) is frequent. `LinkedList` is a doubly-linked list where insertions at the head (`addFirst()`) take $O(1)$ constant time without array copying, making it suitable for appending newly completed rentals to `rentalHistory`.

### Q2: Why did you use `HashMap` for searching vehicle registration numbers?
> **Answer:** In a fleet management system, registration numbers are unique. Using an `ArrayList` requires linear scanning ($O(N)$). `HashMap<String, Vehicle>` computes a hash code on the registration number to perform key lookups in average $O(1)$ constant time.

### Q3: How does `TreeMap` differ from `HashMap`?
> **Answer:** `HashMap` provides $O(1)$ search but does not maintain any ordering of keys. `TreeMap` is implemented as a Red-Black Tree (self-balancing binary search tree) and keeps all keys sorted in natural alphabetical order with $O(\log N)$ search, insertion, and deletion times. We used `TreeMap` to display vehicles naturally sorted by registration number.

### Q4: How did you implement the case study's required `boolean[]` array?
> **Answer:** In `DataStore.java`, we declared `private boolean[] availabilityStatus;`. Whenever vehicles are added, deleted, booked, or returned, `syncAvailabilityArray()` ensures that `availabilityStatus[i]` matches `vehicles.get(i).isAvailable()`. The `AvailabilityPanel` and `VehicleService.getAvailableVehicles()` iterate directly over this primitive array.

### Q5: What custom exceptions did you create and why?
> **Answer:** We created:
> 1. `VehicleNotAvailableException`: Checked exception thrown when someone attempts to book a vehicle that is currently rented.
> 2. `InvalidRentalException`: Thrown if the return date is set prior to the booking date or if rental dates are invalid.
> 3. `ValidationException`: Thrown when form fields are empty, phone numbers do not contain 10 digits, or duplicate registration numbers are entered.

### Q6: How does the application prevent duplicate vehicle registrations?
> **Answer:** In `VehicleService.addVehicle()`, before insertion, we call `dataStore.isRegNoTaken(regNo, null)` which checks `vehicleMap.containsKey(regNo)`. If found, a `ValidationException` is thrown and displayed via `JOptionPane`.

### Q7: Why are models separated from services and GUI?
> **Answer:** This adheres to the **Model-View-Service-Repository (MVSR)** design principle. Model classes hold only entity data and attributes; Service classes encapsulate core business logic and calculations; DataStore manages memory and collections; and GUI panels handle user presentation. This ensures modularity, maintainability, and clean code.

---

**ITM Skills University | School of Future Tech | B.Tech CSE Semester III**

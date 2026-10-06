# Vehicle Rental Management System — Viva Preparation Guide
**Course:** Java Programming (Semester III)  
**Degree:** B.Tech Computer Science and Engineering  
**University:** ITM Skills University (School of Future Tech)  
**Case Study:** Case Study 12 — Vehicle Rental Management System  
**Student Name:** Omkar Narvekar  

---

## SECTION 1 — PROJECT OVERVIEW

### 1-Line Answer: "What is your project?"
> "My project is a desktop-based Vehicle Rental Management System built using Java Swing and Core Java collections to manage vehicles, customers, bookings, returns, and billing."

### 4–5 Line Explanation: "Explain your project."
> "Good morning/afternoon, sir/ma'am. My project is a Vehicle Rental Management System developed for Case Study 12.  
> It allows a rental company to register vehicles and customers, check real-time vehicle availability, create bookings with automatic charge calculations, process vehicle returns, and generate printable invoices.  
> I implemented the core requirements using Java OOP, Collections like `ArrayList`, `LinkedList`, `HashMap`, `TreeMap`, and a primitive `boolean[]` array for availability tracking.  
> The user interface is built with Java Swing without using any external database, keeping all data synchronized in an in-memory repository."

### Why Java Swing is used:
- Java Swing provides built-in GUI components (`JFrame`, `JPanel`, `JTable`, `JOptionPane`) to create standard desktop applications without needing third-party libraries.

---

## SECTION 2 — PROJECT MODULES

1. **Dashboard (`DashboardPanel`)** — Displays operational KPI counts (total fleet, available, rented, customers, revenue) and navigation shortcuts.
2. **Vehicle Management (`VehiclePanel`)** — Performs full CRUD (Add, View, Update, Delete) on vehicles with validation.
3. **Customer Registration (`CustomerPanel`)** — Manages customer profiles including phone, email, and driving license validation.
4. **Vehicle Availability (`AvailabilityPanel`)** — Tracks fleet availability using the required `boolean[] availabilityStatus` primitive array.
5. **Vehicle Search & Sort (`SearchPanel`)** — Searches vehicles via `HashMap` (by registration number) or `ArrayList` (by model/type), and sorts via `TreeMap` and `Comparator`.
6. **Rental Booking (`BookingPanel`)** — Selects customer and available vehicle, calculates days and total charges, and confirms booking.
7. **Vehicle Return (`ReturnPanel`)** — Processes returns for active rentals, calculates final charges, updates availability back to true, and appends to rental history.
8. **Rental Charge Calculation (`RentalService`)** — Automatically calculates `Total Charge = Number of Days × Daily Rental Rate`.
9. **Rental History (`HistoryPanel`)** — Displays completed rental records maintained in a `LinkedList<Rental>`.
10. **Billing & Invoice (`BillingPanel`)** — Generates and displays an official formatted text bill/receipt for any rental transaction.
11. **Rental Reports (`ReportPanel`)** — Computes live business metrics and shows a combined transaction ledger.

---

## SECTION 3 — JAVA CONCEPTS USED IN MY PROJECT

| Concept | Where I Used It | Easy Explanation |
| :--- | :--- | :--- |
| **Classes & Objects** | `Vehicle`, `Customer`, `Rental`, `Invoice` | Classes are blueprints; objects represent real entities in my system. |
| **Constructors** | `Vehicle(...)`, `Customer(...)`, `Rental(...)`, `Invoice(...)` | Used to initialize object data when creating new instances. |
| **Array** | `boolean[] availabilityStatus` in `DataStore` | Primitive array where index `i` stores whether the $i$-th vehicle is available (`true`) or rented (`false`). |
| **ArrayList** | `ArrayList<Vehicle>` & `ArrayList<Customer>` | Resizable list used to store dynamic fleet and customer records. |
| **LinkedList** | `LinkedList<Rental> rentalHistory` in `DataStore` | Stores completed rentals in chronological order using fast head insertion (`addFirst`). |
| **HashMap** | `HashMap<String, Vehicle> vehicleMap` in `DataStore` | Stores Key: Registration Number $\rightarrow$ Value: Vehicle for fast $O(1)$ search. |
| **TreeMap** | `TreeMap<String, Vehicle> sortedVehicles` in `DataStore` | Stores vehicles sorted automatically in alphabetical order of registration numbers. |
| **CRUD** | `VehiclePanel`, `CustomerPanel`, `VehicleService`, `CustomerService` | Create, Read, Update, and Delete operations for managing records. |
| **Searching** | `SearchPanel`, `VehicleService` | Fast search by registration number using `HashMap`, and linear search by model/type using `ArrayList`. |
| **Sorting** | `SearchPanel`, `VehicleService` | Sorted by registration number using `TreeMap`, and by price/model/type using `Comparator<Vehicle>`. |
| **Exception Handling** | `VehicleNotAvailableException`, `InvalidRentalException`, `ValidationException` | Handles errors gracefully using `try-catch` blocks and displays clear alerts via `JOptionPane`. |
| **Validation** | `ValidationUtil.java` | Checks non-empty fields, 10-digit phone numbers, valid email format, and positive daily rates. |
| **Encapsulation** | All model classes (`private` variables with `public` getters/setters) | Keeps class data secure from direct external tampering. |
| **Methods** | All service and utility classes | Reusable blocks of code that perform specific operations like `calculateRentalDays()` or `addVehicle()`. |
| **Inheritance** | Custom exceptions extend `Exception`; GUI panels extend `JPanel` / `JFrame` | Child classes inherit properties and methods from parent classes. |
| **Polymorphism** | Method overriding in `toString()` and table cell renderers | Same method name behaving differently in different classes. |

---

## SECTION 4 — COLLECTIONS VIVA

### 1. Array (`boolean[] availabilityStatus`)
- **What is it?** A fixed-size primitive data structure that stores elements of the same type.
- **Why did I use it?** Required by the case study to track fleet availability by vehicle index.
- **Where is it used?** In `DataStore.java`, synchronized with `vehicles.get(i).isAvailable()`, and displayed in `AvailabilityPanel`.

### 2. ArrayList (`ArrayList<Vehicle>`, `ArrayList<Customer>`)
- **What is it?** A resizable array implementation of the `List` interface.
- **Why did I use it?** Because vehicles and customers can be added or deleted dynamically at runtime.
- **Where is it used?** In `DataStore.java` to maintain fleet records and customer profiles.

### 3. LinkedList (`LinkedList<Rental> rentalHistory`)
- **What is it?** A doubly-linked list collection where elements point to previous and next nodes.
- **Why did I use it?** Ideal for chronological history logs where new completed rentals are inserted at the beginning in $O(1)$ time using `addFirst()`.
- **Where is it used?** In `DataStore.java` and `HistoryPanel.java` to maintain closed rental transactions.

### 4. HashMap (`HashMap<String, Vehicle> vehicleMap`)
- **What is it?** A key-value pair collection that uses hashing.
- **Why did I use it?** To find any vehicle instantly in $O(1)$ average time using its registration number as the key.
- **Where is it used?** In `DataStore.java` and `VehicleService.java` for vehicle lookups.

### 5. TreeMap (`TreeMap<String, Vehicle> sortedVehicles`)
- **What is it?** A key-value collection based on a Red-Black Tree that automatically sorts keys.
- **Why did I use it?** To display vehicles automatically sorted in alphabetical order of registration numbers without manual sorting.
- **Where is it used?** In `DataStore.java` and `SearchPanel.java`.

---

## SECTION 5 — CRUD

- **What is CRUD?** CRUD stands for the four basic operations performed on any data: Create, Read, Update, Delete.
- **What does C mean?** **Create** — Adding a new record (e.g., adding a new vehicle or registering a customer).
- **What does R mean?** **Read** — Viewing records in the system (e.g., displaying records in a `JTable`).
- **What does U mean?** **Update** — Modifying an existing record (e.g., changing a vehicle's rental price).
- **What does D mean?** **Delete** — Removing a record (e.g., deleting a customer who has no active rentals).
- **Where is CRUD used in my project?** In `VehiclePanel` (for vehicle fleet) and `CustomerPanel` (for registered customers).

---

## SECTION 6 — SEARCHING AND SORTING

- **What is searching?** Finding a specific item from a collection based on a given query.
- **How does vehicle search work?**
  - Search by Registration Number calls `vehicleMap.get(regNo)` on `HashMap` in $O(1)$ time.
  - Search by Model or Type scans the `ArrayList<Vehicle>` linearly matching text.
- **What can I search by?** Registration Number, Model, and Vehicle Type.
- **What is sorting?** Arranging records in a specific logical order (ascending or descending).
- **What can I sort by?** Registration Number, Rental Price (Low to High / High to Low), Model, Brand, and Type.
- **What is Comparator?** A Java functional interface (`java.util.Comparator`) used to define custom sorting logic (e.g., `Comparator.comparingDouble(Vehicle::getRentalPricePerDay)`).
- **Why is TreeMap used for sorting?** `TreeMap` automatically keeps all its keys in sorted natural order, so vehicles are always sorted by registration number.

---

## SECTION 7 — EXCEPTION HANDLING AND VALIDATION

- **What is exception handling?** A mechanism using `try-catch` blocks to catch runtime errors and prevent the program from crashing.
- **Why do we need it?** To guide the user when something goes wrong (e.g., invalid dates or missing inputs) instead of crashing the application.
- **What happens if a vehicle is unavailable?** The system throws a custom `VehicleNotAvailableException` and shows an error dialog: *"Vehicle is currently unavailable / already booked."*
- **What is a custom exception?** A user-defined class extending `java.lang.Exception` to represent business-specific errors.
  - My project uses: `VehicleNotAvailableException`, `InvalidRentalException`, and `ValidationException`.
- **What is validation?** Checking that user inputs are correct and complete before saving them.
- **What inputs are validated in my project?**
  - Registration number format and uniqueness.
  - Phone number (must be exactly 10 digits).
  - Email address (must contain `@` and `.`).
  - Rental price (must be greater than 0).
  - Rental dates (return date cannot be earlier than booking date).
  - Vehicle deletion (cannot delete a vehicle that is currently rented).

---

## SECTION 8 — OOP BASIC VIVA

- **What is OOP?** Object-Oriented Programming is a methodology based on objects containing data and methods.
- **What is a class?** A user-defined blueprint from which individual objects are created (e.g., `Vehicle.java`).
- **What is an object?** A real-world runtime instance of a class (e.g., `Vehicle v1 = new Vehicle(...)`).
- **What is a constructor?** A special method called automatically when an object is instantiated to initialize its fields.
- **What is encapsulation?** Wrapping variables and methods into a single unit and keeping fields `private` with `public` getters/setters.
- **What is inheritance?** A mechanism where a child class acquires the properties and methods of a parent class using `extends` (e.g., `ValidationException extends Exception`).
- **What is polymorphism?** The ability of an object or method to take many forms (e.g., method overriding).
- **What is abstraction?** Hiding internal implementation details and showing only essential features to the user.
- **What is a method?** A block of code inside a class that performs a specific action when called.
- **What is a variable?** A named storage location in memory used to hold data.
- **What is an instance variable?** A variable declared inside a class but outside methods, belonging to a specific object (e.g., `brand` in `Vehicle`).
- **What is a static variable?** A variable belonging to the class itself, shared among all instances (e.g., `instance` in `DataStore`).
- **What is `this` keyword?** A reference variable that points to the current object.
- **What is `super` keyword?** A reference variable used to access parent class methods or constructors (e.g., `super(message)` in custom exceptions).
- **What is method overloading?** Having multiple methods with the same name but different parameters in the same class.
- **What is method overriding?** When a child class provides its own specific implementation of a method defined in its parent class (e.g., `toString()`).
- **What is an interface?** A contract that defines method signatures without bodies that implementing classes must define. *(Basic Java concept, implemented via `ActionListener`, `Runnable`)*.
- **What is an abstract class?** A class declared with `abstract` that cannot be instantiated directly and may contain abstract methods. *(Basic Java concept; not directly needed in my project)*.

---

## SECTION 9 — JAVA BASICS VIVA

- **What is Java?** A high-level, class-based, object-oriented, platform-independent programming language.
- **Why is Java platform independent?** Because Java source code compiles into platform-neutral bytecode (`.class`) that can run on any OS having a JVM ("Write Once, Run Anywhere").
- **What is JVM?** Java Virtual Machine — the runtime engine that executes compiled Java bytecode.
- **What is JRE?** Java Runtime Environment — contains the JVM and core libraries needed to run Java programs.
- **What is JDK?** Java Development Kit — the complete software package containing the JRE, compiler (`javac`), and development tools.
- **Difference between JDK, JRE, and JVM?** JDK = Development tools + JRE; JRE = JVM + Library files; JVM = The engine that executes bytecode.
- **What is bytecode?** The intermediate machine-independent code produced by the Java compiler (`javac`).
- **What is a data type?** A classification that specifies the type and size of value a variable can store.
- **Primitive vs Non-Primitive data types?** Primitive types store raw values directly (`int`, `boolean`, `double`); Non-primitive types store references to objects (`String`, `ArrayList`, `Vehicle`).
- **What is String?** An immutable sequence of characters represented as an object in Java.
- **What is an array?** A fixed-size container that stores elements of the same data type in contiguous memory.
- **What is a loop?** A control structure used to execute a block of code repeatedly as long as a condition is true.
- **`for` loop:** Used when the number of iterations is known beforehand.
- **`while` loop:** Checks condition first, then executes code as long as the condition is true.
- **`do-while` loop:** Executes the block at least once before checking the condition.
- **`if-else`:** Conditional statement that executes one block if a condition is true and another if false.
- **`switch`:** Multi-way branch statement that evaluates an expression against multiple `case` values.
- **`break`:** Terminates the current loop or switch block immediately.
- **`continue`:** Skips the rest of the current loop iteration and moves to the next one.
- **`return`:** Exits from the current method and optionally returns a value to the caller.
- **`package`:** A namespace or folder that groups related classes together (e.g., `com.vehiclerental.model`).
- **`import`:** A keyword used to bring classes from other packages into the current file.
- **Access Modifiers:** Keywords that control the visibility of classes, variables, and methods.
- **`public`:** Accessible from anywhere in the application.
- **`private`:** Accessible only within the class where it is declared.
- **`protected`:** Accessible within the same package and subclasses.
- **`default` (no keyword):** Accessible only within the same package.
- **`static`:** Belongs to the class rather than instances of the class.
- **`final`:** Used to declare constants, prevent method overriding, or prevent class inheritance.
- **`null`:** A literal representing an empty reference that points to no object.

---

## SECTION 10 — JAVA SWING VIVA

- **What is Swing?** A standard Java GUI toolkit used to create cross-platform desktop user interfaces.
- **Why did you use Swing?** It is standard in Java, has no external dependencies, and satisfies the Case Study requirement for a desktop GUI.
- **What is `JFrame`?** The top-level window with a title bar, borders, and minimize/maximize/close buttons (`MainFrame`).
- **What is `JPanel`?** A lightweight container used to group and layout GUI components.
- **What is `JLabel`?** A component used to display non-editable text or status information.
- **What is `JButton`?** A clickable button that triggers an action when pressed.
- **What is `JTextField`?** A single-line input field where the user can enter text.
- **What is `JComboBox`?** A dropdown list allowing the user to select one option from many.
- **What is `JTable`?** A UI component that displays multi-column tabular data in rows and columns.
- **What is `JScrollPane`?** A container that provides scrollbars when table or text content exceeds visible space.
- **What is `JOptionPane`?** A standard utility class used to show popup message dialogs, alerts, and confirmations.
- **What is an event?** An occurrence generated by user interaction (like clicking a button or selecting a table row).
- **What is an `ActionListener`?** An interface that listens for action events (e.g., button clicks) and runs callback code.
- **What happens when a button is clicked?** An `ActionEvent` is fired, and the attached `ActionListener` runs its `actionPerformed` or lambda method.
- **Difference between `JFrame` and `JPanel`?** `JFrame` is the main outer window with title bar and minimize/close buttons; `JPanel` is an internal container placed inside a frame to organize layout.

---

## SECTION 11 — PROJECT-SPECIFIC VIVA QUESTIONS

- **Q: Why did you choose this project?**  
  *A:* To solve Case Study 12 by building a complete, real-world fleet and rental management system that demonstrates all required Java concepts.
- **Q: What problem does your project solve?**  
  *A:* It replaces manual paperwork in vehicle rental businesses by automating booking, availability tracking, charges, and records.
- **Q: What is the main purpose of your system?**  
  *A:* To manage vehicle rentals, track real-time fleet availability, prevent double bookings, and calculate charges accurately.
- **Q: What are the main models in your project?**  
  *A:* `Vehicle`, `Customer`, `Rental`, and `Invoice`.
- **Q: How does booking work?**  
  *A:* The user picks a customer and available vehicle, enters dates, calculates charge, and clicks Book; the system verifies availability, marks the vehicle as rented, updates the `boolean[]` array, and creates an active rental.
- **Q: How does vehicle availability change?**  
  *A:* When booked, vehicle availability is set to `false`; when returned, it is set back to `true`, and `DataStore.syncAvailabilityArray()` updates the primitive array.
- **Q: How is rental charge calculated?**  
  *A:* `RentalService` computes the number of days between dates and multiplies it by the daily rate: `Total Charge = Days × Daily Rate`.
- **Q: How does vehicle return work?**  
  *A:* The user selects an active rental, confirms the actual return date, the system recalculates final charges, marks status as `RETURNED`, restores vehicle availability, and appends the rental to `LinkedList<Rental>`.
- **Q: Where is Array used?**  
  *A:* In `DataStore.java` as `boolean[] availabilityStatus`, where index `i` reflects whether the $i$-th vehicle is available.
- **Q: Where is ArrayList used?**  
  *A:* In `DataStore.java` as `ArrayList<Vehicle>` and `ArrayList<Customer>` to store dynamic records.
- **Q: Where is LinkedList used?**  
  *A:* In `DataStore.java` as `LinkedList<Rental> rentalHistory` to maintain completed rentals chronologically.
- **Q: Where is HashMap used?**  
  *A:* In `DataStore.java` as `HashMap<String, Vehicle> vehicleMap` for instant $O(1)$ lookup using Registration Number as key.
- **Q: Where is TreeMap used?**  
  *A:* In `DataStore.java` as `TreeMap<String, Vehicle> sortedVehicles` to keep vehicles naturally sorted by Registration Number.
- **Q: Where is CRUD implemented?**  
  *A:* In `VehiclePanel` and `VehicleService` for vehicles; in `CustomerPanel` and `CustomerService` for customers.
- **Q: How do you handle invalid input?**  
  *A:* `ValidationUtil` validates phone, email, price, and dates, throwing `ValidationException` or `InvalidRentalException` displayed via `JOptionPane`.
- **Q: What happens when someone books an unavailable vehicle?**  
  *A:* `RentalService` detects `!vehicle.isAvailable()` and throws `VehicleNotAvailableException`, showing a warning dialog to the user.
- **Q: Why did you not use a database like MySQL?**  
  *A:* The Case Study specifically requires demonstrating Java Collections Framework and in-memory data structures.
- **Q: What is the role of the service classes?**  
  *A:* Service classes (`VehicleService`, `RentalService`, etc.) separate business logic and calculations from GUI presentation code.
- **Q: How does data move from GUI to the logic?**  
  *A:* The GUI panel collects input from text fields, passes data to a Service class, which validates it and updates `DataStore`.
- **Q: What would you improve in the future?**  
  *A:* In the future, I could add file persistence (JSON/serialization), user authentication (Admin/Staff logins), and online payment gateway integration.

---

## SECTION 12 — "EXPLAIN YOUR CODE" QUESTIONS

- **Explain your `Vehicle` class:**  
  *A:* It is an entity model class with private fields (`vehicleId`, `registrationNumber`, `brand`, `model`, `type`, `rentalPricePerDay`, `available`), a parameterized constructor, getters/setters, and a `toString()` method.

- **Explain your `Customer` class:**  
  *A:* It represents client records with fields for `customerId`, `name`, `phone`, `email`, `drivingLicenseNumber`, and `address`, ensuring customer data is encapsulated.

- **Explain your `Rental` class:**  
  *A:* It associates a `Customer` and a `Vehicle` with rental transaction details including `bookingDate`, `expectedReturnDate`, `actualReturnDate`, `numberOfDays`, `totalCharge`, and `status`.

- **Explain your `DataStore` class:**  
  *A:* It is a singleton repository that holds all application collections (`ArrayList`, `LinkedList`, `HashMap`, `TreeMap`, and `boolean[]` array) and pre-loads realistic sample data.

- **Explain your `RentalService`:**  
  *A:* It contains the business rules for booking and returns, calculates durations and costs, checks vehicle availability, and throws custom exceptions for invalid operations.

- **Explain your GUI structure:**  
  *A:* `MainFrame` uses `BorderLayout` with a header banner at the top, a navigation sidebar on the left, and a `CardLayout` in the center that switches smoothly between the 10 module panels.

- **Explain the booking flow in code:**  
  *A:* The user clicks "Confirm Booking" in `BookingPanel` $\rightarrow$ calls `rentalService.bookVehicle()` $\rightarrow$ validates dates and customer $\rightarrow$ checks `vehicle.isAvailable()` $\rightarrow$ creates `Rental` $\rightarrow$ marks vehicle `setAvailable(false)` $\rightarrow$ calls `dataStore.syncAvailabilityArray()`.

- **Explain the return flow in code:**  
  *A:* In `ReturnPanel`, user selects an active rental and clicks "Complete Return" $\rightarrow$ calls `rentalService.processVehicleReturn()` $\rightarrow$ calculates final charge $\rightarrow$ sets vehicle `setAvailable(true)` $\rightarrow$ updates `boolean[]` array $\rightarrow$ prepends rental to `LinkedList<Rental>`.

- **Explain how `HashMap` is used in code:**  
  *A:* In `DataStore`, `vehicleMap.put(regNo, vehicle)` stores vehicles. In `VehicleService.searchByRegistrationNumber(regNo)`, calling `vehicleMap.get(regNo)` retrieves the vehicle in $O(1)$ time without looping.

- **Explain how `TreeMap` is used in code:**  
  *A:* In `DataStore`, `sortedVehicles.put(regNo, vehicle)` stores entries. Because `TreeMap` is a Red-Black tree, calling `sortedVehicles.values()` returns vehicles automatically sorted by registration number.

---

## SECTION 13 — RAPID FIRE VIVA

1. **Q: What is JVM?**  
   *A:* The engine that executes Java bytecode.
2. **Q: What is JDK?**  
   *A:* The complete Java development kit including compiler and JRE.
3. **Q: What is bytecode?**  
   *A:* Intermediate machine-independent code produced by `javac`.
4. **Q: What is an object?**  
   *A:* A runtime instance of a class.
5. **Q: What is a class?**  
   *A:* A blueprint used to create objects.
6. **Q: What is a constructor?**  
   *A:* A special method used to initialize new objects.
7. **Q: What is encapsulation?**  
   *A:* Wrapping variables and methods together and hiding data using `private`.
8. **Q: What is ArrayList?**  
   *A:* A dynamically resizable array list.
9. **Q: What is LinkedList?**  
   *A:* A sequence of nodes linked by pointers to store dynamic elements.
10. **Q: What is HashMap?**  
    *A:* A key-value pair collection with $O(1)$ average lookup time.
11. **Q: What is TreeMap?**  
    *A:* A key-value map that automatically sorts its keys in natural order.
12. **Q: What is the time complexity of HashMap search?**  
    *A:* $O(1)$ on average.
13. **Q: What is the time complexity of TreeMap search?**  
    *A:* $O(\log N)$.
14. **Q: What is a primitive array?**  
    *A:* A fixed-size container storing primitive data like `boolean[]`.
15. **Q: What does CRUD stand for?**  
    *A:* Create, Read, Update, Delete.
16. **Q: What is `JFrame`?**  
    *A:* A top-level desktop window in Swing.
17. **Q: What is `JPanel`?**  
    *A:* A container component used to organize UI layouts.
18. **Q: What is `JTable`?**  
    *A:* A Swing component used to display data in rows and columns.
19. **Q: What is `JOptionPane`?**  
    *A:* A Swing dialog utility used to show alert messages and confirmations.
20. **Q: What is an exception?**  
    *A:* An unexpected runtime event that interrupts normal program flow.
21. **Q: Name your custom exceptions.**  
    *A:* `VehicleNotAvailableException`, `InvalidRentalException`, `ValidationException`.
22. **Q: What is validation?**  
    *A:* Checking user inputs to ensure they meet required format and rules.
23. **Q: What is `Comparator`?**  
    *A:* A Java interface used to define custom sorting criteria.
24. **Q: What is `this` keyword?**  
    *A:* A reference to the current object instance.
25. **Q: What is `super` keyword?**  
    *A:* A reference to the immediate parent class.
26. **Q: What is method overloading?**  
    *A:* Same method name with different parameters in the same class.
27. **Q: What is method overriding?**  
    *A:* A child class rewriting a parent method with the exact same signature.
28. **Q: What is `static` keyword?**  
    *A:* Marks a member as belonging to the class itself, not individual objects.
29. **Q: What is `final` keyword?**  
    *A:* Prevents reassigning variables, overriding methods, or inheriting classes.
30. **Q: How is total rental charge calculated?**  
    *A:* `Number of Days × Daily Rental Rate`.
31. **Q: Where is `boolean[]` array used?**  
    *A:* In `DataStore.java` to track vehicle availability by index.
32. **Q: Where is `LinkedList` used?**  
    *A:* In `DataStore.java` to store completed rental history chronologically.
33. **Q: Can you delete a vehicle that is currently rented?**  
    *A:* No, the system checks `isAvailable()` and active rentals, blocking deletion.
34. **Q: What LayoutManager is used to switch between screens?**  
    *A:* `CardLayout`.
35. **Q: How do you run the project?**  
    *A:* Using `./run.sh` or `java -cp out com.vehiclerental.Main`.

---

## SECTION 14 — TOP 20 QUESTIONS I MUST KNOW

1. **Explain your project in one sentence:**  
   *A:* A Java Swing desktop system for managing vehicles, customers, bookings, returns, and billing using Core Java collections and arrays.
2. **What are the main classes in your project?**  
   *A:* `Vehicle`, `Customer`, `Rental`, `Invoice`, `DataStore`, `VehicleService`, `RentalService`, and `MainFrame`.
3. **What is the difference between an object and a class?**  
   *A:* A class is a blueprint; an object is an actual instance created in memory from that blueprint.
4. **Why did you use constructors?**  
   *A:* To properly initialize object properties at the time of creation (e.g. `new Vehicle(...)`).
5. **How did you use the required primitive Array?**  
   *A:* As `boolean[] availabilityStatus`, where index `i` stores whether vehicle `i` is available (`true`) or rented (`false`).
6. **Why did you use `ArrayList`?**  
   *A:* To store vehicles and customers dynamically because records can be added or deleted at runtime.
7. **Why did you use `LinkedList`?**  
   *A:* To store completed rental history chronologically using fast $O(1)$ insertions at the head (`addFirst()`).
8. **Why did you use `HashMap`?**  
   *A:* To search vehicles by registration number in $O(1)$ constant time without scanning a list.
9. **Why did you use `TreeMap`?**  
   *A:* To automatically keep vehicles sorted by registration number in alphabetical order.
10. **Where is CRUD implemented in your project?**  
    *A:* In `VehiclePanel` for fleet vehicles and in `CustomerPanel` for registered customers.
11. **How does vehicle searching work?**  
    *A:* Registration number lookup uses `HashMap`; model and vehicle type search uses linear search on `ArrayList`.
12. **How does sorting work in your project?**  
    *A:* Registration number sorting uses `TreeMap`; sorting by rental price, model, and type uses `Comparator<Vehicle>`.
13. **How do you handle exceptions in your project?**  
    *A:* With `try-catch` blocks that catch custom exceptions and display user-friendly error dialogs via `JOptionPane`.
14. **What is `VehicleNotAvailableException`?**  
    *A:* A custom exception thrown when a user attempts to book a vehicle that is already rented.
15. **What validations are present in your project?**  
    *A:* Non-empty fields, valid 10-digit phone, valid email, rental price $> 0$, and return date after booking date.
16. **Why did you choose Java Swing?**  
    *A:* It is built into Java, requires no external dependencies, and provides standard desktop UI components.
17. **What is the difference between `JFrame` and `JPanel`?**  
    *A:* `JFrame` is the main outer window; `JPanel` is an inner container used to group and layout UI elements.
18. **Why is `JTable` used?**  
    *A:* To display records (vehicles, customers, rentals) in a clean, scrollable, tabular format.
19. **What happens during booking?**  
    *A:* The system validates dates and vehicle availability, calculates total charge, creates a `Rental`, marks the vehicle as unavailable, and updates the `boolean[]` array.
20. **What happens during vehicle return?**  
    *A:* The system records actual return date, recalculates charges, sets vehicle availability back to `true`, updates the `boolean[]` array, and saves the rental into `LinkedList<Rental>`.

---

## LAST-MINUTE REVISION (KEY DEFINITIONS IN 1 LINE)

- **Class:** Blueprint of an entity containing fields and methods.
- **Object:** Instance of a class created in memory using `new`.
- **Constructor:** Special method that initializes a new object's fields.
- **Encapsulation:** Keeping data `private` and exposing it via `getters/setters`.
- **`boolean[]` Array:** Primitive array used to track vehicle availability by index.
- **`ArrayList`:** Dynamic resizable list used for vehicles and customers.
- **`LinkedList`:** Linked list used to store completed rental history chronologically.
- **`HashMap`:** Key-value map providing $O(1)$ fast lookup by registration number.
- **`TreeMap`:** Map that automatically maintains keys in natural sorted order.
- **CRUD:** Create, Read, Update, and Delete data operations.
- **`Comparator`:** Interface used to define custom sorting rules (e.g. by price).
- **`VehicleNotAvailableException`:** Custom exception thrown when renting an unavailable car.
- **`ValidationUtil`:** Utility class that validates phone, email, price, and dates.
- **`JFrame`:** The main desktop application window.
- **`JPanel`:** Container panel inside a frame for placing UI controls.
- **`JTable`:** Multi-column grid used to display fleet, customer, and booking data.
- **`JOptionPane`:** Popup dialog utility for showing information and error alerts.
- **Rental Charge Formula:** $\text{Total Charge} = \text{Number of Days} \times \text{Daily Rental Rate}$.

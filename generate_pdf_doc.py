import os
import base64
import subprocess

def get_base64_image(image_path):
    if os.path.exists(image_path):
        with open(image_path, "rb") as f:
            encoded = base64.b64encode(f.read()).decode("utf-8")
            return f"data:image/png;base64,{encoded}"
    return ""

# Load all 10 screenshots
screenshots = {
    "dashboard": get_base64_image("docs/screenshots/dashboard.png"),
    "vehicles": get_base64_image("docs/screenshots/vehicles.png"),
    "customers": get_base64_image("docs/screenshots/customers.png"),
    "availability": get_base64_image("docs/screenshots/availability.png"),
    "search": get_base64_image("docs/screenshots/search.png"),
    "booking": get_base64_image("docs/screenshots/booking.png"),
    "return": get_base64_image("docs/screenshots/return.png"),
    "history": get_base64_image("docs/screenshots/history.png"),
    "billing": get_base64_image("docs/screenshots/billing.png"),
    "reports": get_base64_image("docs/screenshots/reports.png")
}

html_content = f"""<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Vehicle Management System - Project Documentation</title>
<style>
  @page {{
    size: A4 portrait;
    margin: 18mm 16mm 18mm 16mm;
    @bottom-right {{
      content: counter(page);
    }}
  }}

  body {{
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
    color: #2D3748;
    line-height: 1.55;
    font-size: 10pt;
    margin: 0;
    padding: 0;
    background-color: #FFFFFF;
  }}

  /* Page Break Utilities */
  .page-break {{
    page-break-before: always;
  }}
  .avoid-break {{
    page-break-inside: avoid;
  }}

  /* Title / Header Banner */
  .cover-card {{
    background: linear-gradient(135deg, #1E3A5F 0%, #2A4365 100%);
    color: white;
    padding: 24px 28px;
    border-radius: 8px;
    margin-bottom: 22px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.08);
  }}
  .cover-univ {{
    font-size: 11pt;
    font-weight: 600;
    letter-spacing: 0.5px;
    text-transform: uppercase;
    color: #90CDF4;
    margin-bottom: 4px;
  }}
  .cover-title {{
    font-size: 20pt;
    font-weight: 700;
    margin: 4px 0 8px 0;
    color: #FFFFFF;
    line-height: 1.2;
  }}
  .cover-subtitle {{
    font-size: 11pt;
    color: #E2E8F0;
    margin-bottom: 14px;
  }}
  .cover-meta {{
    display: flex;
    justify-content: space-between;
    border-top: 1px solid rgba(255,255,255,0.25);
    padding-top: 12px;
    margin-top: 8px;
    font-size: 9.5pt;
  }}
  .meta-col strong {{
    color: #BEE3F8;
  }}

  /* Section Headings */
  h1 {{
    font-size: 14pt;
    color: #1E3A5F;
    border-bottom: 2px solid #3182CE;
    padding-bottom: 5px;
    margin-top: 20px;
    margin-bottom: 12px;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.4px;
  }}
  h2 {{
    font-size: 11.5pt;
    color: #2C5282;
    margin-top: 14px;
    margin-bottom: 6px;
    font-weight: 600;
  }}
  h3 {{
    font-size: 10.5pt;
    color: #2D3748;
    margin-top: 10px;
    margin-bottom: 4px;
    font-weight: 600;
  }}

  p {{
    margin: 6px 0;
    text-align: justify;
  }}

  ul, ol {{
    margin: 6px 0;
    padding-left: 22px;
  }}
  li {{
    margin-bottom: 3px;
  }}

  /* Tables */
  table {{
    width: 100%;
    border-collapse: collapse;
    margin: 10px 0 14px 0;
    font-size: 9pt;
    background: #FFFFFF;
  }}
  th, td {{
    border: 1px solid #CBD5E0;
    padding: 6px 9px;
    text-align: left;
    vertical-align: top;
  }}
  th {{
    background-color: #EDF2F7;
    color: #1A202C;
    font-weight: 600;
  }}
  tr:nth-child(even) td {{
    background-color: #F7FAFC;
  }}

  /* Code block */
  pre, code {{
    font-family: "SFMono-Regular", Consolas, "Liberation Mono", Menlo, Courier, monospace;
    font-size: 8.5pt;
  }}
  pre {{
    background-color: #F7FAFC;
    border: 1px solid #E2E8F0;
    border-radius: 5px;
    padding: 10px 12px;
    margin: 8px 0;
    line-height: 1.4;
    overflow-x: hidden;
  }}

  /* Screenshot Card */
  .screenshot-container {{
    text-align: center;
    margin: 14px 0 16px 0;
    page-break-inside: avoid;
  }}
  .screenshot-img {{
    width: 95%;
    max-width: 620px;
    border: 1px solid #CBD5E0;
    border-radius: 4px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    display: block;
    margin: 0 auto;
  }}
  .caption {{
    font-size: 8.5pt;
    font-weight: 600;
    color: #4A5568;
    margin-top: 5px;
    font-style: italic;
  }}

  .badge {{
    display: inline-block;
    padding: 2px 7px;
    font-size: 8pt;
    font-weight: 600;
    border-radius: 3px;
    background: #EBF8FF;
    color: #2B6CB0;
    border: 1px solid #BEE3F8;
  }}

  .info-box {{
    background-color: #EBF8FF;
    border-left: 3px solid #3182CE;
    padding: 8px 12px;
    margin: 10px 0;
    font-size: 9pt;
    border-radius: 0 4px 4px 0;
  }}
</style>
</head>
<body>

<!-- HEADER / TITLE -->
<div class="cover-card">
  <div class="cover-univ">ITM Skills University • School of Future Tech</div>
  <div class="cover-title">Vehicle Management System</div>
  <div class="cover-subtitle">Course: Java Programming (Semester III) • Case Study 12 Final Project Documentation</div>
  <div class="cover-meta">
    <div class="meta-col"><strong>Student Name:</strong> Omkar Narvekar</div>
    <div class="meta-col"><strong>Roll No:</strong> 150096725144</div>
    <div class="meta-col"><strong>Degree:</strong> B.Tech Computer Science &amp; Engineering</div>
  </div>
</div>

<!-- 1. CASE STUDY INTRODUCTION -->
<h1>1. Case Study Introduction</h1>

<h3>What is the Vehicle Management System?</h3>
<p>
The <strong>Vehicle Management System</strong> (Vehicle Rental Management System) is a complete desktop application built in Java with a Java Swing graphical user interface. It provides a centralized, automated platform for vehicle rental business staff to manage vehicle fleets, register customers, monitor real-time vehicle availability, create bookings with automatic charge calculations, process returns, generate printable invoices, and view analytical reports.
</p>

<h3>What problem does it solve?</h3>
<p>
Traditional vehicle rental operations rely on manual register entries, paper slips, or ad-hoc spreadsheets. This causes common operational issues:
</p>
<ul>
  <li>Double-booking vehicles because availability is not tracked in real time.</li>
  <li>Calculation errors when computing rental charges based on variable daily rates and rental days.</li>
  <li>Lost rental histories and customer contact information.</li>
  <li>Slow search capabilities when finding vehicles by registration number or car model.</li>
  <li>Difficulty generating professional billing receipts and tracking fleet utilization.</li>
</ul>
<p>
This application solves all these problems by storing data in synchronized, in-memory Java collections with strict input validation and automated charge math.
</p>

<h3>What have I created?</h3>
<p>
I have created a fully functional, 100% operational Java desktop system comprising <strong>11 modular screens</strong> and an in-memory repository architecture without requiring external database installations. The application includes vehicle CRUD, customer CRUD, primitive boolean array tracking, HashMap lookups, TreeMap key sorting, custom exception handlers, billing, and report ledgers.
</p>

<h3>Why is it useful?</h3>
<p>
It is useful because it completely automates the rental lifecycle from customer check-in to vehicle return and billing. It provides immediate visual feedback, guarantees zero double-bookings via business validation, and serves as an academic demonstration of core Java programming concepts for B.Tech Semester III.
</p>

<!-- 2. WHAT I HAVE USED -->
<h1>2. What I Have Used</h1>
<p>
The application is built strictly using standard, platform-independent Java technologies without third-party frameworks:
</p>

<table>
  <thead>
    <tr>
      <th style="width: 25%;">Technology / Feature</th>
      <th>Where and How It Is Used in the Project</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>Java (JDK 17+)</strong></td>
      <td>Core runtime language utilizing object-oriented programming, modern date APIs (<code>java.time.LocalDate</code>), and lambda expressions.</td>
    </tr>
    <tr>
      <td><strong>Java Swing GUI</strong></td>
      <td>Desktop interface using <code>JFrame</code>, <code>JPanel</code>, <code>CardLayout</code>, <code>GridBagLayout</code>, <code>JTable</code>, <code>JButton</code>, <code>JTextField</code>, <code>JComboBox</code>, and <code>JOptionPane</code>.</td>
    </tr>
    <tr>
      <td><strong>Object-Oriented Programming</strong></td>
      <td>Clean separation into entity classes (<code>Vehicle</code>, <code>Customer</code>, <code>Rental</code>, <code>Invoice</code>) with encapsulation, constructors, and method polymorphism.</td>
    </tr>
    <tr>
      <td><strong>Java Collections Framework</strong></td>
      <td>In-memory data structures: <code>ArrayList</code>, <code>LinkedList</code>, <code>HashMap</code>, <code>TreeMap</code>, and primitive <code>boolean[]</code> array.</td>
    </tr>
    <tr>
      <td><strong>Exception Handling</strong></td>
      <td>Custom exception classes (<code>VehicleNotAvailableException</code>, <code>InvalidRentalException</code>, <code>ValidationException</code>) caught with <code>try-catch</code> blocks.</td>
    </tr>
    <tr>
      <td><strong>Data Validation</strong></td>
      <td>Input validation in <code>ValidationUtil.java</code> verifying phone numbers (10 digits), email patterns, positive daily rates, unique registration numbers, and valid date intervals.</td>
    </tr>
    <tr>
      <td><strong>CRUD Operations</strong></td>
      <td>Full Create, Read, Update, and Delete operations for both Vehicles and Customers with constraints (cannot delete rented vehicles).</td>
    </tr>
    <tr>
      <td><strong>Searching Algorithms</strong></td>
      <td>$O(1)$ fast search by Registration Number using <code>HashMap</code>; linear $O(N)$ search by Model and Type using <code>ArrayList</code>.</td>
    </tr>
    <tr>
      <td><strong>Sorting Algorithms</strong></td>
      <td>Natural alphabetical order sorting using <code>TreeMap</code>; multi-criteria sorting using <code>Comparator&lt;Vehicle&gt;</code> (Price low/high, Model, Type).</td>
    </tr>
  </tbody>
</table>

<div class="page-break"></div>

<!-- 3. PROJECT STRUCTURE -->
<h1>3. Project Structure</h1>
<p>
The project follows a clean, industry-standard <strong>Model-View-Service-Repository (MVSR)</strong> architecture with clear package separation:
</p>

<pre>
Java CaseStudy/
├── run.sh                                   # One-click build and execution script
├── test.sh                                  # Automated integration test runner
├── README.md                                # Full documentation and viva guide
├── VIVA.md                                  # Last-minute viva preparation guide
├── docs/screenshots/                        # High-resolution application screenshots
└── src/
    └── com/
        └── vehiclerental/
            ├── Main.java                    # Application launcher
            │
            ├── model/                       # Domain Entities
            │   ├── Vehicle.java             # Fleet vehicle entity
            │   ├── Customer.java            # Registered customer entity
            │   ├── Rental.java              # Rental transaction entity
            │   └── Invoice.java             # Billing receipt entity
            │
            ├── repository/                  # Central In-Memory Storage
            │   └── DataStore.java           # Singleton holding collections & array
            │
            ├── service/                     # Business Logic Layer
            │   ├── VehicleService.java      # Fleet CRUD, search, and sorting logic
            │   ├── CustomerService.java     # Customer management & phone/email validation
            │   ├── RentalService.java       # Booking, return lifecycle & charge calculations
            │   ├── BillingService.java      # Invoice creation and formatting
            │   └── ReportService.java       # Operational statistics and revenue math
            │
            ├── exception/                   # Custom Exception Classes
            │   ├── VehicleNotAvailableException.java  # Thrown when booking rented vehicle
            │   ├── InvalidRentalException.java        # Thrown on invalid rental dates
            │   └── ValidationException.java           # Thrown on malformed user inputs
            │
            ├── util/                        # Helper Utilities
            │   ├── DateUtil.java            # Date parsing, formatting, and duration math
            │   ├── ValidationUtil.java      # Input validation logic
            │   └── GenerateScreenshots.java # Automated UI screenshot generator
            │
            ├── gui/                         # Swing Desktop User Interface
            │   ├── UIStyle.java             # Shared UI color palette and styling tokens
            │   ├── MainFrame.java           # Primary JFrame container with navigation sidebar
            │   ├── DashboardPanel.java      # Live operational KPI summary
            │   ├── VehiclePanel.java        # Vehicle CRUD interface with JTable
            │   ├── CustomerPanel.java       # Customer registration interface
            │   ├── AvailabilityPanel.java   # boolean[] availabilityStatus visualizer
            │   ├── SearchPanel.java         # HashMap search & TreeMap sorting screen
            │   ├── BookingPanel.java        # Vehicle booking form & live charge math
            │   ├── ReturnPanel.java         # Vehicle return processing & availability restore
            │   ├── HistoryPanel.java        # LinkedList rental history viewer
            │   ├── BillingPanel.java        # Formatted printable receipt viewer
            │   └── ReportPanel.java         # Revenue report and transaction ledger
            │
            └── test/
                └── SystemIntegrationTest.java # 19 automated integration test cases
</pre>

<!-- 4. CODE FILE EXPLANATION -->
<h1>4. Code File Explanation</h1>
<p>
The table below explains every single Java file, its class name, and its specific purpose in the application:
</p>

<table>
  <thead>
    <tr>
      <th style="width: 25%;">File Name</th>
      <th style="width: 25%;">Class Name</th>
      <th>Purpose in Project</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>Main.java</code></td>
      <td><code>Main</code></td>
      <td>Application entry point; configures native look-and-feel and launches <code>MainFrame</code> on the Swing Event Dispatch Thread (EDT).</td>
    </tr>
    <tr>
      <td><code>Vehicle.java</code></td>
      <td><code>Vehicle</code></td>
      <td>Entity model storing vehicle ID, registration number, brand, model, vehicle type, daily rental rate, and availability flag.</td>
    </tr>
    <tr>
      <td><code>Customer.java</code></td>
      <td><code>Customer</code></td>
      <td>Entity model storing customer ID, full name, 10-digit phone, email, driving license number, and residential address.</td>
    </tr>
    <tr>
      <td><code>Rental.java</code></td>
      <td><code>Rental</code></td>
      <td>Entity model representing a rental transaction with booking date, expected return date, actual return date, days, rate, and status.</td>
    </tr>
    <tr>
      <td><code>Invoice.java</code></td>
      <td><code>Invoice</code></td>
      <td>Entity model that formats an official itemized text receipt containing customer, vehicle, duration, and financial charges.</td>
    </tr>
    <tr>
      <td><code>DataStore.java</code></td>
      <td><code>DataStore</code></td>
      <td>Singleton repository managing the <code>boolean[]</code> array, <code>ArrayList</code>, <code>LinkedList</code>, <code>HashMap</code>, and <code>TreeMap</code> with pre-loaded sample data.</td>
    </tr>
    <tr>
      <td><code>VehicleService.java</code></td>
      <td><code>VehicleService</code></td>
      <td>Business logic for adding/updating/deleting vehicles, HashMap lookup by registration number, and TreeMap / Comparator sorting.</td>
    </tr>
    <tr>
      <td><code>CustomerService.java</code></td>
      <td><code>CustomerService</code></td>
      <td>Business logic for customer registration, update, deletion checks, and contact validation.</td>
    </tr>
    <tr>
      <td><code>RentalService.java</code></td>
      <td><code>RentalService</code></td>
      <td>Handles booking reservations, duration calculations, rate math (<code>Days × Rate</code>), vehicle returns, and availability state toggling.</td>
    </tr>
    <tr>
      <td><code>BillingService.java</code></td>
      <td><code>BillingService</code></td>
      <td>Generates <code>Invoice</code> instances for active or returned rentals and caches generated bills.</td>
    </tr>
    <tr>
      <td><code>ReportService.java</code></td>
      <td><code>ReportService</code></td>
      <td>Dynamically aggregates fleet KPIs: total fleet, available vehicles, rented vehicles, customer count, active/completed bookings, and gross revenue.</td>
    </tr>
    <tr>
      <td><code>VehicleNotAvailableException.java</code></td>
      <td><code>VehicleNotAvailableException</code></td>
      <td>Custom checked exception thrown when an attempt is made to book a vehicle that is currently rented.</td>
    </tr>
    <tr>
      <td><code>InvalidRentalException.java</code></td>
      <td><code>InvalidRentalException</code></td>
      <td>Custom checked exception thrown when return dates are prior to booking dates or date ranges are invalid.</td>
    </tr>
    <tr>
      <td><code>ValidationException.java</code></td>
      <td><code>ValidationException</code></td>
      <td>Custom checked exception thrown when required fields are empty, phone numbers are not 10 digits, or duplicate IDs are entered.</td>
    </tr>
    <tr>
      <td><code>DateUtil.java</code></td>
      <td><code>DateUtil</code></td>
      <td>Utility class providing date formatting (<code>yyyy-MM-dd</code>), parsing, and date difference calculations.</td>
    </tr>
    <tr>
      <td><code>ValidationUtil.java</code></td>
      <td><code>ValidationUtil</code></td>
      <td>Utility class validating non-empty inputs, phone regex, email regex, registration number formats, and positive rental rates.</td>
    </tr>
    <tr>
      <td><code>UIStyle.java</code></td>
      <td><code>UIStyle</code></td>
      <td>Standard Swing styling helper defining clean enterprise colors, fonts, button borders, and table renderers.</td>
    </tr>
    <tr>
      <td><code>MainFrame.java</code></td>
      <td><code>MainFrame</code></td>
      <td>Main desktop <code>JFrame</code> window featuring university header banner, left navigation sidebar, <code>CardLayout</code>, and status bar.</td>
    </tr>
    <tr>
      <td><code>DashboardPanel.java</code></td>
      <td><code>DashboardPanel</code></td>
      <td>Displays live metric stat boxes, quick navigation buttons, and recent booking activity.</td>
    </tr>
    <tr>
      <td><code>VehiclePanel.java</code></td>
      <td><code>VehiclePanel</code></td>
      <td>GUI panel for adding, viewing, updating, and deleting vehicles with <code>JTable</code> row selection.</td>
    </tr>
    <tr>
      <td><code>CustomerPanel.java</code></td>
      <td><code>CustomerPanel</code></td>
      <td>GUI panel for registering and managing customer accounts with input validation.</td>
    </tr>
    <tr>
      <td><code>AvailabilityPanel.java</code></td>
      <td><code>AvailabilityPanel</code></td>
      <td>GUI panel specifically visualizing the synchronized <code>boolean[] availabilityStatus</code> primitive array.</td>
    </tr>
    <tr>
      <td><code>SearchPanel.java</code></td>
      <td><code>SearchPanel</code></td>
      <td>GUI panel demonstrating $O(1)$ HashMap search and multi-attribute sorting with TreeMap and Comparators.</td>
    </tr>
    <tr>
      <td><code>BookingPanel.java</code></td>
      <td><code>BookingPanel</code></td>
      <td>GUI panel for creating rental reservations with live duration and charge calculations.</td>
    </tr>
    <tr>
      <td><code>ReturnPanel.java</code></td>
      <td><code>ReturnPanel</code></td>
      <td>GUI panel for checking in rented vehicles, computing final dues, and restoring fleet availability.</td>
    </tr>
    <tr>
      <td><code>HistoryPanel.java</code></td>
      <td><code>HistoryPanel</code></td>
      <td>GUI panel displaying all completed rentals stored in the <code>LinkedList&lt;Rental&gt;</code>.</td>
    </tr>
    <tr>
      <td><code>BillingPanel.java</code></td>
      <td><code>BillingPanel</code></td>
      <td>GUI panel displaying formatted monospace billing receipts with copy-to-clipboard functionality.</td>
    </tr>
    <tr>
      <td><code>ReportPanel.java</code></td>
      <td><code>ReportPanel</code></td>
      <td>GUI panel displaying complete business performance indicators and full transaction ledger.</td>
    </tr>
  </tbody>
</table>

<div class="page-break"></div>

<!-- 5. OOP CONCEPTS USED -->
<h1>5. OOP Concepts Used</h1>
<p>
The application is designed around core Object-Oriented Programming (OOP) principles. The table below outlines how each concept is implemented in the actual codebase:
</p>

<table>
  <thead>
    <tr>
      <th style="width: 18%;">OOP Concept</th>
      <th style="width: 22%;">Used In File</th>
      <th style="width: 20%;">Class Name</th>
      <th>Simple Example from Actual Project Code</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>Class</strong></td>
      <td><code>Vehicle.java</code></td>
      <td><code>Vehicle</code></td>
      <td><code>public class Vehicle {{ private String registrationNumber; ... }}</code> acts as blueprint.</td>
    </tr>
    <tr>
      <td><strong>Object</strong></td>
      <td><code>DataStore.java</code></td>
      <td><code>DataStore</code></td>
      <td><code>Vehicle v1 = new Vehicle("V001", "MH01AB1234", "Honda", "City", ...);</code> instantiates a runtime object.</td>
    </tr>
    <tr>
      <td><strong>Encapsulation</strong></td>
      <td><code>Customer.java</code></td>
      <td><code>Customer</code></td>
      <td>All fields are declared <code>private</code> (e.g. <code>private String phone;</code>) and accessed only via public getters and setters (<code>getPhone()</code>, <code>setPhone()</code>).</td>
    </tr>
    <tr>
      <td><strong>Constructor</strong></td>
      <td><code>Rental.java</code></td>
      <td><code>Rental</code></td>
      <td><code>public Rental(String rentalId, Customer customer, Vehicle vehicle, ...) {{ this.rentalId = rentalId; ... }}</code> initializes object state.</td>
    </tr>
    <tr>
      <td><strong>Inheritance</strong></td>
      <td><code>ValidationException.java</code>, <code>MainFrame.java</code></td>
      <td><code>ValidationException</code>, <code>MainFrame</code></td>
      <td><code>public class ValidationException extends Exception</code> inherits standard Java exception behavior; <code>MainFrame extends JFrame</code> inherits window capabilities.</td>
    </tr>
    <tr>
      <td><strong>Polymorphism</strong></td>
      <td><code>Vehicle.java</code>, <code>Invoice.java</code></td>
      <td><code>Vehicle</code>, <code>Invoice</code></td>
      <td>Method overriding: <code>@Override public String toString()</code> provides class-specific string representations across models.</td>
    </tr>
    <tr>
      <td><strong>Abstraction</strong></td>
      <td><em>Not Used (Directly)</em></td>
      <td><em>Not Used</em></td>
      <td><em>Not directly used</em> as no custom <code>abstract class</code> was declared; interfaces like <code>ActionListener</code> and <code>Comparator</code> were used for behavioral contracts.</td>
    </tr>
  </tbody>
</table>

<!-- 6. JAVA COLLECTIONS USED -->
<h1>6. Java Collections Used</h1>
<p>
The project fulfills the explicit requirements of Case Study 12 by utilizing multiple specialized collections and a primitive array:
</p>

<table>
  <thead>
    <tr>
      <th style="width: 18%;">Data Structure</th>
      <th style="width: 22%;">File &amp; Class Name</th>
      <th style="width: 25%;">Variable Name</th>
      <th>What It Stores and Why It Was Used</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>Primitive Array</strong><br><span class="badge">boolean[]</span></td>
      <td><code>DataStore.java</code><br>(<code>DataStore</code>)</td>
      <td><code>boolean[] availabilityStatus</code></td>
      <td>Stores availability flags indexed strictly to fleet vehicles. When vehicle at index <code>i</code> is rented, <code>availabilityStatus[i] = false</code>; when returned, it is set back to <code>true</code>.</td>
    </tr>
    <tr>
      <td><strong>ArrayList</strong><br><span class="badge">ArrayList&lt;T&gt;</span></td>
      <td><code>DataStore.java</code><br>(<code>DataStore</code>)</td>
      <td><code>ArrayList&lt;Vehicle&gt; vehicles</code><br><code>ArrayList&lt;Customer&gt; customers</code></td>
      <td>Stores dynamic fleet and customer records. Provides fast $O(1)$ indexed access for populating Swing <code>JTable</code>s.</td>
    </tr>
    <tr>
      <td><strong>LinkedList</strong><br><span class="badge">LinkedList&lt;T&gt;</span></td>
      <td><code>DataStore.java</code><br>(<code>DataStore</code>)</td>
      <td><code>LinkedList&lt;Rental&gt; rentalHistory</code></td>
      <td>Stores completed rental transactions in chronological order. When a rental is closed, <code>rentalHistory.addFirst(rental)</code> inserts it at the head in $O(1)$ constant time.</td>
    </tr>
    <tr>
      <td><strong>HashMap</strong><br><span class="badge">HashMap&lt;K,V&gt;</span></td>
      <td><code>DataStore.java</code><br>(<code>DataStore</code>)</td>
      <td><code>HashMap&lt;String, Vehicle&gt; vehicleMap</code></td>
      <td>Key: Registration Number (e.g. <code>"MH01AB1234"</code>) $\rightarrow$ Value: <code>Vehicle</code> object. Enables instant $O(1)$ lookups without scanning an entire list.</td>
    </tr>
    <tr>
      <td><strong>TreeMap</strong><br><span class="badge">TreeMap&lt;K,V&gt;</span></td>
      <td><code>DataStore.java</code><br>(<code>DataStore</code>)</td>
      <td><code>TreeMap&lt;String, Vehicle&gt; sortedVehicles</code></td>
      <td>Maintains vehicles automatically sorted in alphabetical order of registration numbers using an internal Red-Black Tree.</td>
    </tr>
  </tbody>
</table>

<div class="page-break"></div>

<!-- 7. GUI USED -->
<h1>7. GUI Components Used</h1>
<p>
The application uses pure Java Swing components to create a clean, responsive desktop interface. All components are styled consistently without third-party UI libraries:
</p>

<table>
  <thead>
    <tr>
      <th style="width: 20%;">Swing Component</th>
      <th style="width: 25%;">Used In File &amp; Class</th>
      <th>Purpose &amp; Application in Project</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>JFrame</strong></td>
      <td><code>MainFrame.java</code> (<code>MainFrame</code>)</td>
      <td>Main top-level application window containing the institutional title banner, left navigation bar, center content area, and status bar.</td>
    </tr>
    <tr>
      <td><strong>JPanel</strong></td>
      <td>All panels in <code>com.vehiclerental.gui</code></td>
      <td>Structural containers used to organize forms, metric cards, action toolbars, and table cards cleanly.</td>
    </tr>
    <tr>
      <td><strong>JButton</strong></td>
      <td><code>VehiclePanel</code>, <code>BookingPanel</code>, etc.</td>
      <td>Interactive trigger buttons styled with distinct visual hierarchy: Primary (Navy), Success (Green for Add/Book), Danger (Red for Delete), Secondary (Gray).</td>
    </tr>
    <tr>
      <td><strong>JLabel</strong></td>
      <td>All panels in <code>com.vehiclerental.gui</code></td>
      <td>Displays form field labels, panel headers, live KPI numeric values, and the bottom status message.</td>
    </tr>
    <tr>
      <td><strong>JTextField</strong></td>
      <td><code>VehiclePanel</code>, <code>CustomerPanel</code>, <code>BookingPanel</code></td>
      <td>Single-line user input fields for vehicle registration numbers, customer names, phone numbers, rental dates, and daily prices.</td>
    </tr>
    <tr>
      <td><strong>JTable</strong></td>
      <td><code>VehiclePanel</code>, <code>AvailabilityPanel</code>, etc.</td>
      <td>Displays tabular records with custom cell heights, custom header styling, selection listeners, and color-coded status badges.</td>
    </tr>
    <tr>
      <td><strong>JComboBox</strong></td>
      <td><code>BookingPanel</code>, <code>ReturnPanel</code>, <code>SearchPanel</code></td>
      <td>Dropdown selection menus for selecting customers, selecting available vehicles, filtering search criteria, and choosing active rentals.</td>
    </tr>
    <tr>
      <td><strong>JOptionPane</strong></td>
      <td><code>VehiclePanel</code>, <code>BookingPanel</code>, <code>ReturnPanel</code></td>
      <td>Modal popup dialogs displaying informative success notifications, warning messages, and deletion confirmation prompts.</td>
    </tr>
    <tr>
      <td><strong>JScrollPane</strong></td>
      <td>All table and invoice panels</td>
      <td>Wraps all <code>JTable</code>s and the billing <code>JTextArea</code> to provide vertical and horizontal scrolling.</td>
    </tr>
    <tr>
      <td><strong>CardLayout</strong></td>
      <td><code>MainFrame.java</code> (<code>MainFrame</code>)</td>
      <td>Layout manager used in the center panel to seamlessly switch between all 10 module screens without opening separate windows.</td>
    </tr>
    <tr>
      <td><strong>GridBagLayout</strong></td>
      <td><code>VehiclePanel</code>, <code>BookingPanel</code>, etc.</td>
      <td>Layout manager used inside form panels to achieve professional, strictly-aligned label and textfield grids.</td>
    </tr>
  </tbody>
</table>

<!-- 8. PROJECT WORKING -->
<h1>8. Project Working &amp; System Flow</h1>
<p>
The system executes a cohesive, end-to-end operational lifecycle:
</p>

<div class="info-box">
  <strong>Complete System Workflow:</strong><br>
  <code>Main.java</code> &rarr; <code>MainFrame</code> (EDT) &rarr; <code>DataStore</code> Initialization &rarr; Operational Dashboard &rarr; Fleet CRUD &rarr; Customer Registration &rarr; Availability Array Tracker &rarr; Rental Booking &rarr; Active Dispatch &rarr; Vehicle Return &rarr; Invoicing &rarr; LinkedList History Ledger &rarr; Analytical Reporting.
</div>

<ol>
  <li><strong>Application Launch:</strong> <code>Main.java</code> sets the system look-and-feel and launches <code>MainFrame</code> on the Event Dispatch Thread (EDT).</li>
  <li><strong>Data Store Initialization:</strong> <code>DataStore</code> initializes the fleet (5 sample vehicles), customers (3 sample customers), 1 active rental, and 1 completed rental. It synchronizes the <code>boolean[] availabilityStatus</code> array immediately.</li>
  <li><strong>Vehicle &amp; Customer Management:</strong> The staff uses <code>VehiclePanel</code> and <code>CustomerPanel</code> to register vehicles and clients. Inputs are validated by <code>ValidationUtil</code>. Deleting currently rented vehicles is blocked.</li>
  <li><strong>Availability Inspection:</strong> The staff checks <code>AvailabilityPanel</code>, which displays the <code>boolean[]</code> array values alongside fleet rows.</li>
  <li><strong>Vehicle Search &amp; Sort:</strong> The staff searches by Registration Number (instant $O(1)$ <code>HashMap</code> lookup) or sorts the fleet by daily price using <code>Comparator&lt;Vehicle&gt;</code>.</li>
  <li><strong>Rental Booking:</strong> In <code>BookingPanel</code>, the staff selects a customer and an available vehicle. The system calculates duration and charge (<code>Days × Rate</code>). Upon confirmation, <code>RentalService</code> validates dates, marks the vehicle unavailable, updates the <code>boolean[]</code> array to <code>false</code>, and adds the rental to active rentals.</li>
  <li><strong>Vehicle Return:</strong> When the vehicle is returned, <code>ReturnPanel</code> takes the actual return date, recalculates charges if overdue, sets vehicle availability back to <code>true</code>, updates the <code>boolean[]</code> array to <code>true</code>, and moves the rental into <code>LinkedList&lt;Rental&gt; rentalHistory</code>.</li>
  <li><strong>Billing &amp; Invoicing:</strong> In <code>BillingPanel</code>, an official text receipt is generated detailing customer info, vehicle specs, duration, and total paid dues.</li>
  <li><strong>Executive Reporting:</strong> <code>ReportPanel</code> aggregates real-time metrics (fleet count, active rentals, total revenue) dynamically.</li>
</ol>

<div class="page-break"></div>

<!-- 9. SCREENSHOTS -->
<h1>9. Application Screenshots</h1>
<p>
Below are the actual high-resolution screenshots captured directly from the running Java Swing application, demonstrating every module in operation:
</p>

<div class="screenshot-container">
  <img src="{screenshots['dashboard']}" class="screenshot-img" alt="Operational Dashboard">
  <div class="caption">Figure 9.1: Operational Dashboard showing live fleet metrics, quick navigation shortcuts, and recent booking activity.</div>
</div>

<div class="screenshot-container">
  <img src="{screenshots['vehicles']}" class="screenshot-img" alt="Vehicle Management">
  <div class="caption">Figure 9.2: Vehicle Management screen showing the vehicle entry form and registered fleet in JTable with CRUD operations.</div>
</div>

<div class="page-break"></div>

<div class="screenshot-container">
  <img src="{screenshots['customers']}" class="screenshot-img" alt="Customer Registration">
  <div class="caption">Figure 9.3: Customer Registration screen with validation for 10-digit phone numbers, valid emails, and driving licenses.</div>
</div>

<div class="screenshot-container">
  <img src="{screenshots['availability']}" class="screenshot-img" alt="Fleet Availability Tracker">
  <div class="caption">Figure 9.4: Vehicle Availability screen displaying the synchronized boolean[] availabilityStatus primitive array.</div>
</div>

<div class="page-break"></div>

<div class="screenshot-container">
  <img src="{screenshots['search']}" class="screenshot-img" alt="Search and Sort Engine">
  <div class="caption">Figure 9.5: Search and Sort screen demonstrating O(1) HashMap lookup and Comparator / TreeMap sorting.</div>
</div>

<div class="screenshot-container">
  <img src="{screenshots['booking']}" class="screenshot-img" alt="Rental Booking Desk">
  <div class="caption">Figure 9.6: Rental Booking screen with automated rental duration and charge calculation (Days x Rate/Day).</div>
</div>

<div class="page-break"></div>

<div class="screenshot-container">
  <img src="{screenshots['return']}" class="screenshot-img" alt="Vehicle Return Processing">
  <div class="caption">Figure 9.7: Vehicle Return screen for processing returns, computing final dues, and restoring fleet availability.</div>
</div>

<div class="screenshot-container">
  <img src="{screenshots['history']}" class="screenshot-img" alt="Rental History Ledger">
  <div class="caption">Figure 9.8: Rental History screen displaying completed transactions maintained in a LinkedList&lt;Rental&gt;.</div>
</div>

<div class="page-break"></div>

<div class="screenshot-container">
  <img src="{screenshots['billing']}" class="screenshot-img" alt="Billing and Invoice">
  <div class="caption">Figure 9.9: Official Billing and Invoice screen generating formatted itemized receipts with payment status.</div>
</div>

<div class="screenshot-container">
  <img src="{screenshots['reports']}" class="screenshot-img" alt="Operations and Financial Report">
  <div class="caption">Figure 9.10: Analytical Reports screen showing real-time KPIs and comprehensive transaction ledger.</div>
</div>

<!-- 10. CONCLUSION -->
<div class="page-break"></div>
<h1>10. Conclusion</h1>

<h3>What I Created</h3>
<p>
For my B.Tech Computer Science Semester III Java Programming final project, I successfully developed a complete, fully functional <strong>Vehicle Rental Management System</strong> adhering strictly to the requirements of Case Study 12. The application provides an integrated desktop platform that manages vehicles, registered customers, reservations, returns, real-time availability tracking, charge calculations, invoicing, and reporting without requiring any external database software.
</p>

<h3>Java Concepts Used</h3>
<p>
Through this project, I demonstrated the practical implementation of essential Java concepts:
</p>
<ul>
  <li><strong>Object-Oriented Programming (OOP):</strong> Modeled real-world entities using encapsulated classes (<code>Vehicle</code>, <code>Customer</code>, <code>Rental</code>, <code>Invoice</code>) with constructors, getters, setters, and method overriding.</li>
  <li><strong>Java Collections Framework:</strong> Successfully implemented and synchronized an <code>ArrayList</code> for dynamic records, a <code>LinkedList</code> for chronological rental history, a <code>HashMap</code> for instant $O(1)$ vehicle search by registration number, and a <code>TreeMap</code> for natural key sorting.</li>
  <li><strong>Primitive Array:</strong> Genuinely utilized a <code>boolean[] availabilityStatus</code> array to track vehicle availability by index across the application.</li>
  <li><strong>Exception Handling &amp; Validation:</strong> Implemented robust custom exceptions (<code>VehicleNotAvailableException</code>, <code>InvalidRentalException</code>, <code>ValidationException</code>) and comprehensive regex validation to safeguard data integrity.</li>
  <li><strong>Java Swing GUI:</strong> Constructed an intuitive desktop user interface utilizing <code>JFrame</code>, <code>CardLayout</code>, <code>GridBagLayout</code>, <code>JTable</code>, and <code>JOptionPane</code>.</li>
</ul>

<h3>What I Learned</h3>
<p>
This case study gave me hands-on experience in architecting a multi-tiered software application using the Model-View-Service-Repository pattern. I learned how to choose the right data structure for specific performance requirements (e.g., <code>HashMap</code> for fast lookups vs. <code>TreeMap</code> for sorted views), how to manage application state across multiple GUI panels, and how to write clean, maintainable, and viva-ready Java code.
</p>

<div style="margin-top: 35px; border-top: 1px solid #CBD5E0; padding-top: 12px; display: flex; justify-content: space-between; font-size: 9pt; color: #718096;">
  <div><strong>Student Name:</strong> Omkar Narvekar</div>
  <div><strong>Roll No:</strong> 150096725144</div>
  <div><strong>Institution:</strong> ITM Skills University (B.Tech CSE)</div>
</div>

</body>
</html>
"""

with open("docs/project_documentation.html", "w", encoding="utf-8") as f:
    f.write(html_content)

print("[+] HTML documentation generated: docs/project_documentation.html")

# Convert HTML to PDF using Google Chrome headless
pdf_path = "Vehicle_Management_System_Documentation.pdf"
chrome_cmd = [
    "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
    "--headless",
    "--disable-gpu",
    "--print-to-pdf-no-header",
    f"--print-to-pdf={pdf_path}",
    os.path.abspath("docs/project_documentation.html")
]

print("[*] Generating final PDF via Google Chrome headless...")
result = subprocess.run(chrome_cmd, capture_output=True, text=True)

if os.path.exists(pdf_path) and os.path.getsize(pdf_path) > 0:
    print(f"[+] SUCCESS! Generated single final PDF: {pdf_path} ({os.path.getsize(pdf_path)} bytes)")
else:
    print("[-] Error generating PDF. Chrome stderr:")
    print(result.stderr)

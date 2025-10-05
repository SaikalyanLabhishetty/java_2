# 🏥 Care Home Management System

A **Java-based application** to efficiently manage residents, staff, rooms, and bed assignments in a care home.
The system provides both a **Console interface** and a **JavaFX-based GUI**, ensuring accessibility and usability for all types of users.

---

## 📁 Project Structure

CareHomeManagementSystem/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── Main.java          # Console interface entry-point
│   │       ├── CareHomeApp.java   # JavaFX GUI entry-point
│   │       └── [other classes...]
│   └── test/
│       └── java/
│           └── [JUnit test files...]
├── pom.xml
└── README.md

---

## 🧩 Features

* 👥 **Resident Management** – Add, edit, view, and delete resident records.
* 🧑‍⚕️ **Staff Management** – Maintain staff details and roles.
* 🛏️ **Room & Bed Assignment** – Assign residents to rooms and beds.
* 🖥️ **Dual Interface** – Use the system via Console or JavaFX GUI.
* 🧪 **JUnit Testing** – Comprehensive test coverage for major components.

---

## ⚙️ Requirements

| Component  | Version                              |
| ---------- | ------------------------------------ |
| **Java**   | 21+                                  |
| **Maven**  | 3.9+                                 |
| **JavaFX** | 21 (managed automatically via Maven) |

---

## 🖋️ How to Compile and Run (Console Mode)

1. Open a terminal and navigate to the project directory.
2. Compile and run the console version:

   javac Main.java
   java Main

✅ This launches the **console-based menu** for resident and staff management.

> Ensure all `.java` files are in the same directory or follow the `src/main/java` Maven structure.

---

## 🎨 How to Run (JavaFX GUI)

1. Ensure you have **Maven** installed.
2. Run the following command:

   mvn javafx:run

✅ This launches the **graphical user interface (GUI)** built using **JavaFX**.

### Maven Configuration Notes

* Your `pom.xml` must include:

  * JavaFX dependencies
  * `javafx-maven-plugin`

JavaFX modules will be **automatically downloaded** and managed by Maven.

---

## 🧪 Running Tests

To execute all **JUnit test cases**, run:

mvn test

✅ Maven will compile and run the tests under `src/test/java/`.

---

## 🧱 Technologies Used

* **Java 21**
* **JavaFX 21**
* **Maven 3.9+**
* **JUnit 5**

---

## 📚 Future Enhancements

* 🗂️ Database integration (e.g., MySQL or SQLite)
* 🌐 Web-based dashboard
* 📊 Analytics and reporting for residents and staff
* 🔒 User authentication and role-based access control
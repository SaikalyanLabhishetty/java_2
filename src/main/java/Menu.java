import exceptions.BedAssignmentException;
import java.util.Scanner;
import java.io.IOException;
import java.lang.ClassNotFoundException;

public class Menu {
    private CareHome careHome;
    private Scanner scanner;

    public Menu() {
        careHome = new CareHome();
        careHome.initializeRooms();
        scanner = new Scanner(System.in);
        // Load saved data
        // In Menu constructor
        try {
            careHome.loadStateFromFile("carehome.dat");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No saved data found or corrupt. Starting fresh.");
        }
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n--- RMIT Care Home System ---");
            System.out.println("1. Add Resident");
            System.out.println("2. Add Staff");
            System.out.println("3. Assign Bed");
            System.out.println("4. Display Residents");
            System.out.println("5. Save and Exit");
            System.out.print("Enter choice: ");

            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        addResident();
                        break;
                    case 2:
                        addStaff();
                        break;
                    case 3:
                        assignBed();
                        break;
                    case 4:
                        careHome.displayResidents();
                        break;
                    case 5:
                        saveAndExit();
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void addResident() {
        System.out.print("Enter Resident ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Resident Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter Medical Info: ");
        String medicalInfo = scanner.nextLine();

        Resident resident = new Resident(id, name, gender, medicalInfo);
        if (careHome.addResident(resident)) {
            System.out.println("Resident added successfully.");
        } else {
            System.out.println("Failed to add resident.");
        }
    }

    private void addStaff() {
        System.out.print("Enter Staff ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Staff Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Role (Doctor/Nurse): ");
        String role = scanner.nextLine();

        Staff staffMember = null;
        if (role.equalsIgnoreCase("Doctor")) {
            staffMember = new Doctor(id, name);
        } else if (role.equalsIgnoreCase("Nurse")) {
            staffMember = new Nurse(id, name);
        } else {
            System.out.println("Invalid role. Must be Doctor or Nurse.");
            return;
        }

        if (careHome.addStaff(staffMember)) {
            System.out.println(role + " added successfully.");
        } else {
            System.out.println("Failed to add staff.");
        }
    }

    private void assignBed() {
        System.out.print("Enter Resident ID: ");
        String residentId = scanner.nextLine();
        System.out.print("Enter Bed ID (e.g. Room1-B1): ");
        String bedId = scanner.nextLine();

        try {
            careHome.assignBed(residentId, bedId);
            System.out.println("Bed assigned successfully.");
        } catch (BedAssignmentException e) {
            System.out.println("Error assigning bed: " + e.getMessage());
        }
    }

    private void saveAndExit() {
        try {
            // In saveAndExit() method
            careHome.saveStateToFile("carehome.dat");
            System.out.println("Data saved. Exiting...");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
        scanner.close();
    }
}

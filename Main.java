import exceptions.AuthorizationException;
import exceptions.BedAssignmentException;

public class Main {
    public static void main(String[] args) {
        CareHome careHome = new CareHome();
        try {
            careHome.assignBed("R001", "Room1-B1");
            System.out.println("Bed assigned successfully.");
        } catch (BedAssignmentException e) {
            System.out.println("Error assigning bed: " + e.getMessage());
        }

        Doctor doc = new Doctor("D001", "Dr. Smith");
        Resident res = new Resident("R001", "John Doe", "Male", "Diabetes");
        Prescription presc = new Prescription("Metformin", "500mg", "Twice a day", doc);

        try {
            careHome.prescribeMedication(doc, res, presc);
            System.out.println("Prescription added successfully.");
        } catch (AuthorizationException e) {
            System.out.println("Authorization error: " + e.getMessage());
        }
    }
}

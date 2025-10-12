import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.AuthorizationException;
import exceptions.BedAssignmentException;

public class CareHome implements Serializable {
    private ArrayList<Resident> residents;
    private ArrayList<Staff> staff;
    private ArrayList<Room> rooms;
    private transient AuditLog auditLog; // Transient to avoid serialization issues

    public CareHome() {
        residents = new ArrayList<>();
        staff = new ArrayList<>();
        rooms = new ArrayList<>();
        initializeAuditLog();
    }

    private void initializeAuditLog() {
        try {
            auditLog = new AuditLog();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a new resident
    public boolean addResident(Resident resident) {
        for (Resident r : residents) {
            if (r.getResidentId().equals(resident.getResidentId())) {
                log("Add Resident failed: Resident with ID " + resident.getResidentId() + " already exists.");
                return false;
            }
        }
        residents.add(resident);
        log("Resident added: " + resident.getName() + " (ID: " + resident.getResidentId() + ")");
        System.out.println("Resident added successfully.");
        return true;
    }

    // Add a new staff member
    public boolean addStaff(Staff member) {
        for (Staff s : staff) {
            if (s.getStaffId().equals(member.getStaffId())) {
                log("Add Staff failed: Staff with ID " + member.getStaffId() + " already exists.");
                return false;
            }
        }
        staff.add(member);
        log("Staff added: " + member.getName() + " (ID: " + member.getStaffId() + ")");
        System.out.println("Staff added successfully.");
        return true;
    }

    // Assign a bed to a resident
    public void assignBed(String residentId, String bedId) throws BedAssignmentException {
        Resident resident = findResidentById(residentId);
        if (resident == null) {
            throw new BedAssignmentException("Resident not found.");
        }
        Bed bed = findBedById(bedId);
        if (bed == null) {
            throw new BedAssignmentException("Bed not found.");
        }
        if (bed.getOccupant() != null) {
            throw new BedAssignmentException("Bed is already occupied.");
        }
        if (resident.getAssignedBed() != null) {
            throw new BedAssignmentException("Resident already has a bed assigned.");
        }

        bed.assignResident(resident);
        resident.setAssignedBed(bed);
        log("Bed assigned: " + bedId + " to resident " + residentId);
    }

    // Helper method to find resident by ID
    public Resident findResidentById(String residentId) {
        for (Resident r : residents) {
            if (r.getResidentId().equals(residentId)) {
                return r;
            }
        }
        return null;
    }

    // Helper method to find bed by ID
    public Bed findBedById(String bedId) {
        for (Room room : rooms) {
            for (Bed bed : room.getBeds()) {
                if (bed.getBedId().equals(bedId)) {
                    return bed;
                }
            }
        }
        return null;
    }

    // Lists residents without a bed assigned
    public List<Resident> getUnassignedResidents() {
        List<Resident> unassigned = new ArrayList<>();
        for (Resident r : residents) {
            if (r.getAssignedBed() == null) {
                unassigned.add(r);
            }
        }
        return unassigned;
    }

    // Lists beds that are unassigned
    public List<Bed> getAvailableBeds() {
        List<Bed> available = new ArrayList<>();
        for (Room room : rooms) {
            for (Bed bed : room.getBeds()) {
                if (bed.getOccupant() == null) {
                    available.add(bed);
                }
            }
        }
        return available;
    }

    // Display all residents (console fallback)
    public void displayResidents() {
        for (Resident r : residents) {
            System.out.println("Resident ID: " + r.getResidentId() + ", Name: " + r.getName() +
                ", Bed: " + (r.getAssignedBed() != null ? r.getAssignedBed().getBedId() : "None"));
        }
    }

    // Only doctors can prescribe meds
    public void prescribeMedication(Staff staffMember, Resident resident, Prescription prescription) throws AuthorizationException {
        if (!staff.contains(staffMember)) {
            throw new AuthorizationException("Staff member not found in staff records.");
        }
        if (!(staffMember instanceof Doctor)) {
            throw new AuthorizationException("Only doctors can prescribe medication.");
        }
        
        // Cast to Doctor since we know it's a Doctor
        Doctor doctor = (Doctor) staffMember;
        resident.addPrescription(prescription);
        
        log("Medication prescribed: " + prescription.getMedicine() + " to resident " + resident.getResidentId() + " by doctor " + doctor.getName());
    }


    // Role-based action checking
    public void performAction(String staffId, String action) throws AuthorizationException {
        Staff staffMember = findStaffById(staffId);
        if (staffMember == null) {
            throw new AuthorizationException("Staff not found.");
        }
        if (!staffMember.canPerformAction(action)) {
            throw new AuthorizationException("Unauthorized: " + staffMember.getRole() + " cannot perform '" + action + "'");
        }
        // Action can proceed
        log("Staff: " + staffId + " performed action: " + action);
    }

    private Staff findStaffById(String staffId) {
        for (Staff s : staff) {
            if (s.getStaffId().equals(staffId)) {
                return s;
            }
        }
        return null;
    }

    // Initialize rooms with beds
    public void initializeRooms() {
        Room room1 = new Room("Room1", 5);
        Room room2 = new Room("Room2", 5);
        rooms.add(room1);
        rooms.add(room2);
        log("Initial rooms created: Room1, Room2 (5 beds each)");
    }

    // Save state to file
    public void saveStateToFile(String filename) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(residents);
        out.writeObject(staff);
        out.writeObject(rooms);
        out.close();
        log("State saved to file: " + filename);
    }

    // Load state from file
    public void loadStateFromFile(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        residents = (ArrayList<Resident>) in.readObject();
        staff = (ArrayList<Staff>) in.readObject();
        rooms = (ArrayList<Room>) in.readObject();
        in.close();
        // Reinitialize audit log after deserialization
        initializeAuditLog();
        log("State loaded from file: " + filename);
    }

    public List<Resident> getAllResidents() {
        return new ArrayList<>(residents);
    }

    // Audit logging helper
    private void log(String message) {
        if (auditLog != null) {
            try {
                auditLog.log(message);
            } catch (SQLException e) {
                System.err.println("Failed to write audit log: " + message);
            }
        }
    }

    // Prevent null pointer on deserialization
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initializeAuditLog();
    }

    public List<Staff> getAllStaff() {
        return new ArrayList<>(staff);
    }
}

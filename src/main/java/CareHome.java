import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.AuthorizationException;
import exceptions.BedAssignmentException;


// Remove the inner Shift class and use standalone Shift class
public class CareHome implements Serializable {
    
    // Keep only Ward as inner class, use Shift from package
    public static class Ward implements Serializable {
        private String wardId;
        private String wardName;
        private List<Room> rooms;
        private String wardType;

        public Ward(String wardId, String wardName, String wardType) {
            this.wardId = wardId;
            this.wardName = wardName;
            this.wardType = wardType;
            this.rooms = new ArrayList<>();
        }

        public void addRoom(Room room) {
            rooms.add(room);
        }

        public List<Room> getAvailableRooms() {
            List<Room> available = new ArrayList<>();
            for (Room room : rooms) {
                if (hasAvailableBed(room)) {
                    available.add(room);
                }
            }
            return available;
        }

        private boolean hasAvailableBed(Room room) {
            for (Bed bed : room.getBeds()) {
                if (bed.getOccupant() == null) {
                    return true;
                }
            }
            return false;
        }

        public String getWardId() { return wardId; }
        public String getWardName() { return wardName; }
        public String getWardType() { return wardType; }
        public List<Room> getRooms() { return new ArrayList<>(rooms); }
        
        @Override
        public String toString() {
            return wardName + " (" + wardType + ")";
        }
    }
    
    // Instance fields
    private static final long serialVersionUID = 1L;
    private ArrayList<Resident> residents;
    private ArrayList<Staff> staff;
    private ArrayList<Ward> wards;
    private List<Shift> shifts; // Reference Shift from package
    private transient AuditLog auditLog;

    public CareHome() {
        residents = new ArrayList<>();
        staff = new ArrayList<>();
        wards = new ArrayList<>();
        shifts = new ArrayList<>();
        initializeAuditLog();
        initializeShifts();
        initializeWards();
    }

    private void initializeWards() {
        // Create medical ward with 6 rooms (1-4 beds each)
        Ward medicalWard = new Ward("W001", "Medical Ward", "Medical");
        medicalWard.addRoom(createRoom("W001-R1", 2));
        medicalWard.addRoom(createRoom("W001-R2", 4));
        medicalWard.addRoom(createRoom("W001-R3", 3));
        medicalWard.addRoom(createRoom("W001-R4", 1));
        medicalWard.addRoom(createRoom("W001-R5", 4));
        medicalWard.addRoom(createRoom("W001-R6", 2));
        
        // Create surgical ward with 6 rooms (1-4 beds each)
        Ward surgicalWard = new Ward("W002", "Surgical Ward", "Surgical");
        surgicalWard.addRoom(createRoom("W002-R1", 3));
        surgicalWard.addRoom(createRoom("W002-R2", 4));
        surgicalWard.addRoom(createRoom("W002-R3", 2));
        surgicalWard.addRoom(createRoom("W002-R4", 1));
        surgicalWard.addRoom(createRoom("W002-R5", 3));
        surgicalWard.addRoom(createRoom("W002-R6", 4));
        
        wards.add(medicalWard);
        wards.add(surgicalWard);
    }
    
    private void initializeShifts() {
        // Define 3 shifts with color codes
        // Make sure these use the same Shift class as in Staff
        shifts.add(new Shift("S001", "Morning", "06:00", "14:00", "#4CAF50")); // Green
        shifts.add(new Shift("S002", "Evening", "14:00", "22:00", "#2196F3")); // Blue
        shifts.add(new Shift("S003", "Night", "22:00", "06:00", "#9C27B0"));  // Purple
    }

    private void initializeAuditLog() {
        try {
            auditLog = new AuditLog();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private Room createRoom(String roomId, int bedCount) {
        if (bedCount < 1 || bedCount > 4) {
            throw new IllegalArgumentException("Bed count must be between 1 and 4");
        }
        return new Room(roomId, bedCount);
    }
    
    // Compatibility method for Menu.java
    public void initializeRooms() {
        wards.clear();
        
        // Create a general ward with 2 rooms of 5 beds each
        Ward generalWard = new Ward("GENERAL", "General Ward", "General");
        
        // Create Room1 and Room2 with 5 beds each
        Room room1 = new Room("Room1", 5);
        Room room2 = new Room("Room2", 5);
        
        generalWard.addRoom(room1);
        generalWard.addRoom(room2);
        
        // Add the general ward
        wards.add(generalWard);
        
        log("Initial rooms created via initializeRooms(): Room1, Room2 (5 beds each)");
    }
    
    public List<Ward> getAvailableWards() {
        List<Ward> availableWards = new ArrayList<>();
        for (Ward ward : wards) {
            if (!ward.getAvailableRooms().isEmpty()) {
                availableWards.add(ward);
            }
        }
        return availableWards;
    }
    
    public List<Ward> getWards() {
        return new ArrayList<>(wards);
    }
    
    // Helper to find room by ID
    public Room findRoomById(String roomId) {
        for (Ward ward : wards) {
            for (Room room : ward.getRooms()) {
                if (room.getRoomId().equals(roomId)) {
                    return room;
                }
            }
        }
        return null;
    }
    
    // Find bed by ID
    public Bed findBedById(String bedId) {
        for (Ward ward : wards) {
            for (Room room : ward.getRooms()) {
                for (Bed bed : room.getBeds()) {
                    if (bed.getBedId().equals(bedId)) {
                        return bed;
                    }
                }
            }
        }
        return null;
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
        // Assign random shifts to staff
       if (member instanceof Doctor) {
            // Clear existing shifts first
            ((Doctor) member).getAssignedShifts().clear();
            // Doctors work all assigned shifts
            for (Shift shift : shifts) {
                ((Doctor) member).addShift(shift);
            }
        } else if (member instanceof Nurse) {
            // Nurses work 1-2 random shifts
            List<Shift> randomShifts = getRandomShifts(1, 2);
            for (Shift shift : randomShifts) {
                // This should work if Shift class is consistent
                ((Nurse) member).addShift(shift);
            }
        }
        
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
    
    // Helper to get random shifts
    private List<Shift> getRandomShifts(int min, int max) {
        List<Shift> result = new ArrayList<>();
        int count = min + (int)(Math.random() * (max - min + 1));
        List<Shift> available = new ArrayList<>(shifts);
        
        for (int i = 0; i < count && !available.isEmpty(); i++) {
            int index = (int)(Math.random() * available.size());
            result.add(available.remove(index));
        }
        return result;
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
        
        // Find which ward and room this bed is in
        Ward ward = getWardByBed(bed);
        Room room = getRoomByBed(bed);
        
        log("Bed assigned: " + bedId + " to resident " + residentId + 
            " in " + (ward != null ? ward.getWardName() : "Unknown") + 
            "/" + (room != null ? room.getRoomId() : "Unknown"));
    }
    
    private Ward getWardByBed(Bed bed) {
        for (Ward ward : wards) {
            for (Room room : ward.getRooms()) {
                if (room.getBeds().contains(bed)) {
                    return ward;
                }
            }
        }
        return null;
    }
    
    private Room getRoomByBed(Bed bed) {
        for (Ward ward : wards) {
            for (Room room : ward.getRooms()) {
                if (room.getBeds().contains(bed)) {
                    return room;
                }
            }
        }
        return null;
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
        for (Ward ward : wards) {
            for (Room room : ward.getRooms()) {
                for (Bed bed : room.getBeds()) {
                    if (bed.getOccupant() == null) {
                        available.add(bed);
                    }
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

    // Save state to file
    public void saveStateToFile(String filename) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(residents);
        out.writeObject(staff);
        out.writeObject(wards);
        out.writeObject(shifts);
        out.close();
        log("State saved to file: " + filename);
    }

    // Load state from file
    public void loadStateFromFile(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        residents = (ArrayList<Resident>) in.readObject();
        staff = (ArrayList<Staff>) in.readObject();
        wards = (ArrayList<Ward>) in.readObject();
        // Read shifts if saved with new format
        try {
            shifts = (ArrayList<Shift>) in.readObject();
        } catch (Exception e) {
            // Handle older versions without shifts
            initializeShifts();
        }
        in.close();
        // Reinitialize audit log after deserialization
        initializeAuditLog();
        log("State loaded from file: " + filename);
    }

    public List<Resident> getAllResidents() {
        return new ArrayList<>(residents);
    }

    // Getter for shifts
    public List<Shift> getShifts() {
        return new ArrayList<>(shifts);
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

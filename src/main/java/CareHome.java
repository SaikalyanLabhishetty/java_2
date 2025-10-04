import java.io.*;
import java.util.ArrayList;
import java.util.List;

import exceptions.AuthorizationException;
import exceptions.BedAssignmentException;

public class CareHome implements Serializable{
    private ArrayList<Resident> residents;
    private ArrayList<Staff> staff;
    private ArrayList<Room> rooms;

    public CareHome() {
        residents = new ArrayList<>();
        staff = new ArrayList<>();
        rooms = new ArrayList<>();
    }

    // Add a new resident
    public boolean addResident(Resident resident) {
        for (Resident r : residents) {
            if (r.getResidentId().equals(resident.getResidentId())) {
                System.out.println("Resident with this ID already exists.");
                return false;
            }
        }
        residents.add(resident);
        System.out.println("Resident added successfully.");
        return true;
    }

    // Add a new staff member
    public boolean addStaff(Staff member) {
        for (Staff s : staff) {
            if (s.getStaffId().equals(member.getStaffId())) {
                System.out.println("Staff with this ID already exists.");
                return false;
            }
        }
        staff.add(member);
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

    // Display all residents (for testing and simple UI)
    public void displayResidents() {
        for (Resident r : residents) {
            System.out.println("Resident ID: " + r.getResidentId() + ", Name: " + r.getName() +
                ", Bed: " + (r.getAssignedBed() != null ? r.getAssignedBed().getBedId() : "None"));
        }
    }

    public void prescribeMedication(Doctor doctor, Resident resident, Prescription prescription) throws AuthorizationException {
        if (!staff.contains(doctor)) {
            throw new AuthorizationException("Doctor not found in staff records.");
        }
        if (!(doctor instanceof Doctor)) {
            throw new AuthorizationException("Only doctors can prescribe medication.");
        }
        resident.addPrescription(prescription);
    }

    public void initializeRooms() {
    Room room1 = new Room("Room1", 5); // creates beds Room1-B1 to Room1-B5
    Room room2 = new Room("Room2", 5);
    rooms.add(room1);
    rooms.add(room2);
}


    public void saveData(String filename) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(residents);
        out.writeObject(staff);
        out.writeObject(rooms);
        out.close();
        System.out.println("Data saved.");
    }

    public void loadData(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        residents = (ArrayList<Resident>) in.readObject();
        staff = (ArrayList<Staff>) in.readObject();
        rooms = (ArrayList<Room>) in.readObject();
        in.close();
        System.out.println("Data loaded.");
    }
    public List<Resident> getAllResidents() {
        return new ArrayList<>(residents); // Or residents.values() if you're using a Map
    }
}

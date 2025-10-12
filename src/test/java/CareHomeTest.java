import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.BedAssignmentException;

import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class CareHomeTest {
    private CareHome careHome;
    private Resident resident1;
    private Resident resident2;
    private Doctor doctor;
    private Nurse nurse;

    @BeforeEach
    void setUp() {
        careHome = new CareHome();
        careHome.initializeRooms();
        resident1 = new Resident("R001", "John Doe", "Male", "None");
        resident2 = new Resident("R002", "Jane Smith", "Female", "Diabetes");
        doctor = new Doctor("D001", "Dr. Smith");
        nurse = new Nurse("N001", "Nurse Wilson");
        
        careHome.addResident(resident1);
        careHome.addResident(resident2);
        careHome.addStaff(doctor);
        careHome.addStaff(nurse);
    }

    @Test
    void testAddResidentSuccess() {
        Resident newResident = new Resident("R003", "Bob Johnson", "Male", "None");
        assertTrue(careHome.addResident(newResident), "Should successfully add new resident");
        assertNotNull(careHome.findResidentById("R003"), "Resident should be found by ID");
    }

    @Test
    void testAddResidentDuplicateId() {
        Resident duplicateResident = new Resident("R001", "John Doe", "Male", "None");
        assertFalse(careHome.addResident(duplicateResident), "Should not add resident with duplicate ID");
    }

    @Test
    void testAddStaffSuccess() {
        Staff newStaff = new Doctor("D002", "Dr. Brown");
        assertTrue(careHome.addStaff(newStaff), "Should successfully add new staff");
        
        boolean staffFound = careHome.getAllStaff().stream()
            .anyMatch(s -> s.getStaffId().equals("D002"));
        assertTrue(staffFound, "Staff should be found in staff list");
    }

    @Test
    void testAssignBedSuccess() throws Exception {
        careHome.assignBed("R001", "Room1-B1");
        
        assertEquals("Room1-B1", resident1.getAssignedBed().getBedId(), 
            "Resident should be assigned to correct bed");
            
        assertEquals(resident1, careHome.findResidentById("R001").getAssignedBed().getOccupant(),
            "Bed should have correct occupant");
    }

    @Test
    void testAssignBedToNonexistentResident() {
        Exception exception = assertThrows(Exception.class, () -> {
            careHome.assignBed("R999", "Room1-B1");
        });
        assertTrue(exception.getMessage().contains("Resident not found"), 
            "Should throw exception for non-existent resident");
    }

    @Test
    void testAssignBedToOccupiedBed() throws Exception {
        careHome.assignBed("R001", "Room1-B1");
        
        Exception exception = assertThrows(Exception.class, () -> {
            careHome.assignBed("R002", "Room1-B1");
        });
        assertTrue(exception.getMessage().contains("Bed is already occupied"), 
            "Should detect occupied bed");
    }

    @Test
    void testGetUnassignedResidents() {
        assertEquals(2, careHome.getUnassignedResidents().size(), 
            "Both residents should be unassigned initially");
        
        try {
            careHome.assignBed("R001", "Room1-B1");
        } catch (Exception e) {
            fail("Should not throw exception when assigning bed");
        }
        
        assertEquals(1, careHome.getUnassignedResidents().size(), 
            "Only unassigned residents should be returned");
        assertEquals("R002", careHome.getUnassignedResidents().get(0).getResidentId());
    }

    @Test
    void testGetAvailableBeds() {
        assertEquals(10, careHome.getAvailableBeds().size(), 
            "All beds should be available initially");
        
        try {
            careHome.assignBed("R001", "Room1-B1");
        } catch (Exception e) {
            fail("Should not throw exception when assigning bed");
        }
        
        assertEquals(9, careHome.getAvailableBeds().size(), 
            "Only unassigned beds should be returned");
        
        String assignedBedId = "Room1-B1";
        boolean found = careHome.getAvailableBeds().stream()
            .anyMatch(bed -> bed.getBedId().equals(assignedBedId));
        assertFalse(found, "Assigned bed should not be in available beds list");
    }

    @Test
    void testPrescribeMedication() throws Exception {
        // Fixed: Now providing the doctor as the 4th parameter
        Prescription prescription = new Prescription("Insulin", "10 units", "Daily", doctor);
        
        assertDoesNotThrow(() -> {
            careHome.prescribeMedication(doctor, resident1, prescription);
        }, "Doctor should be able to prescribe medication");
        
        assertEquals(1, resident1.getPrescriptions().size(),
            "Resident should have one prescription");
        assertEquals("Insulin", resident1.getPrescriptions().get(0).getMedicine());
        assertEquals(doctor, resident1.getPrescriptions().get(0).getPrescribedBy(),
            "Prescription should have correct prescribing doctor");
    }

    @Test
    void testNurseCannotPrescribeMedication() {
        // Fixed: Now providing the doctor as the 4th parameter
        // We'll use the doctor for the prescription, but pass nurse as prescriber
        Prescription prescription = new Prescription("Insulin", "10 units", "Daily", doctor);
        
        Exception exception = assertThrows(Exception.class, () -> {
            careHome.prescribeMedication(nurse, resident1, prescription);
        });
        
        assertTrue(exception.getMessage().contains("Only doctors can prescribe medication"),
            "Nurse should not be able to prescribe medication");
    }

    @Test
    void testSaveAndLoadState() throws IOException, ClassNotFoundException, BedAssignmentException {
        careHome.assignBed("R001", "Room1-B1");
        careHome.assignBed("R002", "Room1-B2");
        
        careHome.saveStateToFile("test_carehome.dat");
        CareHome loadedCareHome = new CareHome();
        loadedCareHome.loadStateFromFile("test_carehome.dat");
        
        assertEquals(2, loadedCareHome.getAllResidents().size(), 
            "Should have same number of residents");
        assertEquals("Room1-B1", loadedCareHome.getAllResidents().get(0).getAssignedBed().getBedId(),
            "Bed assignments should be preserved");
        assertEquals("Room1-B2", loadedCareHome.getAllResidents().get(1).getAssignedBed().getBedId(),
            "Bed assignments should be preserved");
        
        new File("test_carehome.dat").delete();
    }

    @Test
    void testSaveAndLoadWithEmptyState() throws IOException, ClassNotFoundException {
        CareHome emptyCareHome = new CareHome();
        emptyCareHome.initializeRooms();
        
        emptyCareHome.saveStateToFile("empty_carehome.dat");
        CareHome loadedCareHome = new CareHome();
        loadedCareHome.loadStateFromFile("empty_carehome.dat");
        
        assertEquals(0, loadedCareHome.getAllResidents().size(), 
            "Should have no residents");
        assertEquals(0, loadedCareHome.getAllStaff().size(),
            "Should have no staff");
        
        new File("empty_carehome.dat").delete();
    }
}

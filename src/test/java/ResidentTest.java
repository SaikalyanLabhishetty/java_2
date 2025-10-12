import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResidentTest {
    private final String RESIDENT_ID = "R001";
    private final String NAME = "John Doe";
    private final String GENDER = "Male";
    private final String MEDICAL_INFO = "None";

    @Test
    void testResidentCreation() {
        Resident resident = new Resident(RESIDENT_ID, NAME, GENDER, MEDICAL_INFO);
        
        assertEquals(RESIDENT_ID, resident.getResidentId(), "ID should match");
        assertEquals(NAME, resident.getName(), "Name should match");
        assertEquals(GENDER, resident.getGender(), "Gender should match");
        assertEquals(MEDICAL_INFO, resident.getMedicalInfo(), "Medical info should match");
        assertNull(resident.getAssignedBed(), "New resident should not have assigned bed");
        assertNotNull(resident.getPrescriptions(), "Prescriptions list should be initialized");
        assertTrue(resident.getPrescriptions().isEmpty(), "Prescriptions list should be empty");
    }

    @Test
    void testAddPrescription() {
        Resident resident = new Resident(RESIDENT_ID, NAME, GENDER, MEDICAL_INFO);
        Doctor doctor = new Doctor("D001", "Dr. Smith");
        
        // Fixed: Now providing all 4 parameters for Prescription constructor
        Prescription prescription = new Prescription("Insulin", "10 units", "Daily", doctor);
        
        resident.addPrescription(prescription);
        
        assertEquals(1, resident.getPrescriptions().size(), "Should have one prescription");
        assertEquals(prescription, resident.getPrescriptions().get(0), "Should contain added prescription");
    }

    @Test
    void testAddMultiplePrescriptions() {
        Resident resident = new Resident(RESIDENT_ID, NAME, GENDER, MEDICAL_INFO);
        Doctor doctor1 = new Doctor("D001", "Dr. Smith");
        Doctor doctor2 = new Doctor("D002", "Dr. Brown");
        
        Prescription prescription1 = new Prescription("Insulin", "10 units", "Daily", doctor1);
        Prescription prescription2 = new Prescription("Metformin", "500mg", "Twice daily", doctor2);
        
        resident.addPrescription(prescription1);
        resident.addPrescription(prescription2);
        
        assertEquals(2, resident.getPrescriptions().size(), "Should have two prescriptions");
        assertTrue(resident.getPrescriptions().contains(prescription1), "Should contain first prescription");
        assertTrue(resident.getPrescriptions().contains(prescription2), "Should contain second prescription");
    }

    @Test
    void testToString() {
        Resident resident = new Resident(RESIDENT_ID, NAME, GENDER, MEDICAL_INFO);
        
        String expectedStart = "Resident{residentId='R001', name='John Doe'";
        assertTrue(resident.toString().startsWith(expectedStart), 
            "toString should include basic resident info");
    }

    @Test
    void testEqualsAndHashCode() {
        Resident resident1 = new Resident(RESIDENT_ID, NAME, GENDER, MEDICAL_INFO);
        Resident resident2 = new Resident(RESIDENT_ID, NAME, GENDER, MEDICAL_INFO);
        Resident resident3 = new Resident("R002", NAME, GENDER, MEDICAL_INFO);
        
        assertEquals(resident1, resident2, "Residents with same ID should be equal");
        assertEquals(resident1.hashCode(), resident2.hashCode(), 
            "Hash codes should be equal for equal objects");
        assertNotEquals(resident1, resident3, "Residents with different IDs should not be equal");
    }
}

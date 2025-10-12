import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StaffTest {
    private final String STAFF_ID = "S001";
    private final String NAME = "John Smith";

    @Test
    void testDoctorCreation() {
        Doctor doctor = new Doctor(STAFF_ID, NAME);
        
        assertEquals(STAFF_ID, doctor.getStaffId(), "ID should match");
        assertEquals(NAME, doctor.getName(), "Name should match");
        assertEquals("Doctor", doctor.getRole(), "Role should be Doctor");
        assertTrue(doctor.canPerformAction("prescribe"), "Doctor should be able to prescribe");
    }

    @Test
    void testNurseCreation() {
        Nurse nurse = new Nurse(STAFF_ID, NAME);
        
        assertEquals(STAFF_ID, nurse.getStaffId(), "ID should match");
        assertEquals(NAME, nurse.getName(), "Name should match");
        assertEquals("Nurse", nurse.getRole(), "Role should be Nurse");
        assertTrue(nurse.canPerformAction("checkVitals"), "Nurse should be able to check vitals");
        assertFalse(nurse.canPerformAction("prescribe"), "Nurse should not be able to prescribe");
    }

    @Test
    void testDoctorCanPerformAllActions() {
        Doctor doctor = new Doctor(STAFF_ID, NAME);
        
        assertTrue(doctor.canPerformAction("prescribe"), "Doctor should prescribe");
        assertTrue(doctor.canPerformAction("administer"), "Doctor should administer");
        assertTrue(doctor.canPerformAction("chart"), "Doctor should chart");
    }

    @Test
    void testNurseCanPerformLimitedActions() {
        Nurse nurse = new Nurse(STAFF_ID, NAME);
        
        assertTrue(nurse.canPerformAction("checkVitals"), "Nurse should check vitals");
        assertTrue(nurse.canPerformAction("administerMedication"), "Nurse should administer medication");
        assertFalse(nurse.canPerformAction("prescribe"), "Nurse should not prescribe");
        assertFalse(nurse.canPerformAction("diagnose"), "Nurse should not diagnose");
    }

    @Test
    void testToString() {
        Doctor doctor = new Doctor(STAFF_ID, NAME);
        Nurse nurse = new Nurse(STAFF_ID, NAME);
        
        assertTrue(doctor.toString().contains("Doctor"), "Doctor toString should mention role");
        assertTrue(nurse.toString().contains("Nurse"), "Nurse toString should mention role");
        assertTrue(doctor.toString().contains(STAFF_ID), "Doctor toString should include ID");
        assertTrue(nurse.toString().contains(STAFF_ID), "Nurse toString should include ID");
    }

    @Test
    void testEqualsAndHashCode() {
        Doctor doctor1 = new Doctor(STAFF_ID, NAME);
        Doctor doctor2 = new Doctor(STAFF_ID, NAME);
        Doctor doctor3 = new Doctor("S002", NAME);
        Nurse nurse = new Nurse(STAFF_ID, NAME);
        
        assertEquals(doctor1, doctor2, "Same ID doctors should be equal");
        assertEquals(doctor1.hashCode(), doctor2.hashCode(), "Hash codes should match");
        assertNotEquals(doctor1, doctor3, "Different ID doctors should not be equal");
        assertNotEquals(doctor1, nurse, "Doctor and nurse should not be equal");
    }
}

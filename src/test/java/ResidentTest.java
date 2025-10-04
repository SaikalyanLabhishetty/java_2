import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResidentTest {
    @Test
    public void testResidentCreation() {
        Resident r = new Resident("101", "Aditya", "M", "Asthma");
        assertEquals("101", r.getResidentId());
        assertEquals("Aditya", r.getName());
        assertEquals("M", r.getGender());
        assertNull(r.getAssignedBed());
    }

    @Test
    public void testAddPrescription() {
        Doctor doc = new Doctor("201", "Dr. Smith");
        Prescription p = new Prescription("Metformin", "500mg", "Morning", doc);
        Resident r = new Resident("101", "Aditya", "M", "Asthma");
        r.addPrescription(p);
        assertTrue(r.getPrescriptions().contains(p));
    }
}

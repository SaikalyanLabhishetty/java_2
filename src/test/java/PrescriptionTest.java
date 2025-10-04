import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrescriptionTest {
    @Test
    public void testPrescriptionCreation() {
        Doctor doc = new Doctor("201", "Dr. Smith");
        Prescription p = new Prescription("Metformin", "500mg", "Morning", doc);
        assertEquals("Metformin", p.getMedicine());
        assertEquals("500mg", p.getDosage());
        assertEquals("Morning", p.getTiming());
        assertEquals(doc, p.getPrescribedBy());
    }
}

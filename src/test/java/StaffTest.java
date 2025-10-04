import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StaffTest {
    @Test
    public void testDoctorRole() {
        Doctor doc = new Doctor("201", "Dr. Smith");
        assertEquals("Doctor", doc.getRole());
        assertEquals("Dr. Smith", doc.getName());
    }

    @Test
    public void testNurseRole() {
        Nurse nurse = new Nurse("301", "Nurse Mary");
        assertEquals("Nurse", nurse.getRole());
        assertEquals("Nurse Mary", nurse.getName());
    }
}

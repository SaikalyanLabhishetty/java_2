import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BedTest {
    @Test
    public void testAssignResident() {
        Bed bed = new Bed("Room1-B1");
        Resident r = new Resident("101", "Aditya", "M", "Asthma");
        bed.assignResident(r);
        assertEquals(r, bed.getOccupant());
    }

    @Test
    public void testRemoveResident() {
        Bed bed = new Bed("Room1-B1");
        Resident r = new Resident("101", "Aditya", "M", "Asthma");
        bed.assignResident(r);
        bed.removeResident();
        assertNull(bed.getOccupant());
    }
}

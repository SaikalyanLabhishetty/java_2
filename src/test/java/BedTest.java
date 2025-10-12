import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BedTest {
    private final String BED_ID = "Room1-B1";

    @Test
    void testBedCreation() {
        Bed bed = new Bed(BED_ID);
        
        assertEquals(BED_ID, bed.getBedId(), "Bed ID should match");
        assertNull(bed.getOccupant(), "New bed should have no occupant");
    }

    @Test
    void testAssignResident() {
        Bed bed = new Bed(BED_ID);
        Resident resident = new Resident("R001", "John Doe", "Male", "None");
        
        bed.assignResident(resident);
        
        assertEquals(resident, bed.getOccupant(), "Bed should have correct occupant");
    }

    @Test
    void testReleaseResident() {
        Bed bed = new Bed(BED_ID);
        Resident resident = new Resident("R001", "John Doe", "Male", "None");
        
        bed.assignResident(resident);
        bed.release();
        
        assertNull(bed.getOccupant(), "Bed should have no occupant after release");
    }

    @Test
    void testToString() {
        Bed bed = new Bed(BED_ID);
        Resident resident = new Resident("R001", "John Doe", "Male", "None");
        
        // Test unoccupied bed
        assertTrue(bed.toString().contains(BED_ID), "toString should include bed ID");
        assertTrue(bed.toString().contains("Free"), "Free bed should show as free");
        
        // Test occupied bed
        bed.assignResident(resident);
        assertTrue(bed.toString().contains("Occupied"), "Occupied bed should show as occupied");
        assertTrue(bed.toString().contains(BED_ID), "Occupied bed toString should include ID");
    }

    @Test
    void testEqualsAndHashCode() {
        Bed bed1 = new Bed(BED_ID);
        Bed bed2 = new Bed(BED_ID);
        Bed bed3 = new Bed("Room1-B2");
        
        assertEquals(bed1, bed2, "Beds with same ID should be equal");
        assertEquals(bed1.hashCode(), bed2.hashCode(), "Hash codes should match");
        assertNotEquals(bed1, bed3, "Beds with different IDs should not be equal");
    }
}

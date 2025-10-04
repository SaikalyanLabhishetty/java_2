import org.junit.jupiter.api.Test;

import exceptions.BedAssignmentException;

import static org.junit.jupiter.api.Assertions.*;

public class CareHomeTest {
    @Test
    public void testAddResident() {
        CareHome ch = new CareHome();
        Resident r = new Resident("101", "Aditya", "M", "Asthma");
        assertTrue(ch.addResident(r));
    }

    @Test
    public void testAssignBed() throws Exception {
        CareHome ch = new CareHome();
        ch.initializeRooms();
        Resident r = new Resident("101", "Aditya", "M", "Asthma");
        ch.addResident(r);
        assertDoesNotThrow(() -> ch.assignBed("101", "Room1-B1"));
    }

    @Test
    public void testAssignBedOccupied() throws Exception {
        CareHome ch = new CareHome();
        ch.initializeRooms();
        Resident r1 = new Resident("101", "Aditya", "M", "Asthma");
        Resident r2 = new Resident("102", "Kiran", "M", "Cold");
        ch.addResident(r1);
        ch.addResident(r2);
        ch.assignBed("101", "Room1-B1");
        assertThrows(BedAssignmentException.class, () -> ch.assignBed("102", "Room1-B1"));
    }
}

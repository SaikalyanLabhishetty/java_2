import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {
    @Test
    public void testRoomCreation() {
        Room room = new Room("Room1", 3);
        assertEquals("Room1", room.getRoomId());
        assertEquals(3, room.getBeds().size());
        assertEquals("Room1-B1", room.getBeds().get(0).getBedId());
    }
}

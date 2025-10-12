import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {
    private final String ROOM_ID = "Room1";

    @Test
    void testRoomCreationWithBeds() {
        // Given: Create room with 3 beds
        Room room = new Room(ROOM_ID, 3);
        
        // Then: Verify room properties
        assertEquals(ROOM_ID, room.getRoomId(), "Room ID should match constructor parameter");
        assertEquals(3, room.getBeds().size(), "Should have correct number of beds");
        
        // Verify bed IDs are correctly formatted
        assertEquals("Room1-B1", room.getBeds().get(0).getBedId(), "First bed should have correct ID");
        assertEquals("Room1-B2", room.getBeds().get(1).getBedId(), "Second bed should have correct ID");
        assertEquals("Room1-B3", room.getBeds().get(2).getBedId(), "Third bed should have correct ID");
    }

    @Test
    void testRoomWithZeroBeds() {
        // Given: Create room with 0 beds
        Room room = new Room(ROOM_ID, 0);
        
        // Then: Verify room properties
        assertEquals(ROOM_ID, room.getRoomId(), "Room ID should match");
        assertNotNull(room.getBeds(), "getBeds() should return non-null list");
        assertTrue(room.getBeds().isEmpty(), "Room with 0 beds should have empty beds list");
    }

    @Test
    void testRoomWithOneBed() {
        // Given: Create room with 1 bed
        Room room = new Room(ROOM_ID, 1);
        
        // Then: Verify bed creation
        assertEquals(1, room.getBeds().size(), "Should have exactly one bed");
        assertEquals("Room1-B1", room.getBeds().get(0).getBedId(), "Single bed should have correct ID");
    }

    @Test
    void testGetBedsReturnsCorrectList() {
        // Given: Room with multiple beds
        Room room = new Room(ROOM_ID, 2);
        
        // When: Get beds list
        var beds = room.getBeds();
        
        // Then: Verify list properties
        assertNotNull(beds, "getBeds() should return non-null list");
        assertEquals(2, beds.size(), "Should return correct number of beds");
        
        // Verify we can iterate and access bed properties
        for (int i = 0; i < beds.size(); i++) {
            Bed bed = beds.get(i);
            assertNotNull(bed, "Each bed in list should be non-null");
            assertTrue(bed.getBedId().startsWith(ROOM_ID), "Bed ID should start with room ID");
            assertTrue(bed.getBedId().contains("-B" + (i + 1)), "Bed ID should have correct sequence");
            assertNull(bed.getOccupant(), "New beds should be unoccupied");
        }
    }

    @Test
    void testBedIdConsistency() {
        // Given: Room with multiple beds
        Room room = new Room(ROOM_ID, 4);
        
        // Then: Verify all bed IDs follow the pattern
        for (int i = 1; i <= 4; i++) {
            String expectedId = ROOM_ID + "-B" + i;
            assertEquals(expectedId, room.getBeds().get(i - 1).getBedId(), 
                "Bed " + i + " should have correct ID format");
        }
    }

    @Test
    void testRoomIdWithDifferentFormat() {
        // Given: Room with different ID format
        Room room = new Room("Suite-101", 2);
        
        // Then: Verify bed IDs incorporate the room ID correctly
        assertEquals("Suite-101-B1", room.getBeds().get(0).getBedId(), 
            "Bed ID should correctly incorporate room ID with special characters");
        assertEquals("Suite-101-B2", room.getBeds().get(1).getBedId(), 
            "Bed ID should correctly incorporate room ID with special characters");
    }

    @Test
    void testGetBedsReturnsNewListEachTime() {
        // Given: Room with beds
        Room room = new Room(ROOM_ID, 2);
        var firstCall = room.getBeds();
        var secondCall = room.getBeds();
        
        // Then: Verify each call returns a new list instance
        assertNotSame(firstCall, secondCall, "Each call to getBeds() should return a new list");
        assertEquals(2, firstCall.size(), "First call should have correct size");
        assertEquals(2, secondCall.size(), "Second call should have correct size");
        
        // Modify first list - should not affect room's internal state
        firstCall.clear();
        assertEquals(2, room.getBeds().size(), "Room's internal bed list should not be affected by external modifications");
    }

    @Test
    void testRoomWithLargeNumberOfBeds() {
        // Given: Room with 10 beds
        Room room = new Room(ROOM_ID, 10);
        
        // Then: Verify all beds are created correctly
        assertEquals(10, room.getBeds().size(), "Should have exactly 10 beds");
        
        // Verify the last bed has correct ID
        assertEquals("Room1-B10", room.getBeds().get(9).getBedId(), "Last bed should have correct ID");
        
        // Verify no duplicate bed IDs
        java.util.Set<String> bedIds = new java.util.HashSet<>();
        for (Bed bed : room.getBeds()) {
            assertTrue(bedIds.add(bed.getBedId()), "Should not have duplicate bed IDs");
        }
    }

    @Test
    void testToString() {
        // Given: Room with beds
        Room room = new Room(ROOM_ID, 3);
        
        // When: Call toString
        String toString = room.toString();
        
        // Then: Verify string contains relevant information
        assertNotNull(toString, "toString() should not return null");
        assertTrue(toString.contains(ROOM_ID), "toString should include room ID");
        assertTrue(toString.contains("3"), "toString should include bed count");
        assertTrue(toString.contains("Room"), "toString should mention 'Room'");
        assertTrue(toString.contains("Bed"), "toString should mention 'Bed'");
    }

    @Test
    void testEqualsAndHashCode() {
        // Given: Rooms with same and different properties
        Room room1 = new Room(ROOM_ID, 2);
        Room room2 = new Room(ROOM_ID, 2);
        Room room3 = new Room("Room2", 2);
        Room room4 = new Room(ROOM_ID, 3);
        
        // Then: Verify equality rules
        assertEquals(room1, room2, "Rooms with same ID and bed count should be equal");
        assertEquals(room1.hashCode(), room2.hashCode(), "Hash codes should be equal for equal objects");
        
        assertNotEquals(room1, room3, "Rooms with different IDs should not be equal");
        assertNotEquals(room1, room4, "Rooms with same ID but different bed count should not be equal");
        assertNotEquals(room1, null, "Room should not equal null");
        assertNotEquals(room1, "string", "Room should not equal different type");
    }

    @Test
    void testRoomWithNegativeBedCount() {
        // Given: Attempt to create room with negative beds
        // When & Then: Should handle appropriately (depends on your implementation)
        // If your Room constructor throws exception:
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Room(ROOM_ID, -1);
        }, "Should not allow negative bed count");
        
        assertTrue(exception.getMessage().toLowerCase().contains("negative") || 
                   exception.getMessage().toLowerCase().contains("invalid"),
                   "Exception message should indicate invalid bed count");
    }
}

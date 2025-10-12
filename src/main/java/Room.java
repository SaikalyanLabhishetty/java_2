import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String roomId;
    private List<Bed> beds;
    private int bedCount;

    public Room(String roomId, int numberOfBeds) {
        // Validate bed count
        if (numberOfBeds < 0) {
            throw new IllegalArgumentException("Number of beds cannot be negative: " + numberOfBeds);
        }
        
        this.roomId = roomId;
        this.bedCount = numberOfBeds;
        this.beds = new ArrayList<>();
        
        // Create beds only if bed count is positive
        for (int i = 1; i <= numberOfBeds; i++) {
            beds.add(new Bed(roomId + "-B" + i));
        }
    }

    public String getRoomId() {
        return roomId;
    }

    public List<Bed> getBeds() {
        return new ArrayList<>(beds); // Return a copy to prevent external modification
    }
    
    @Override
    public String toString() {
        return "Room{roomId='" + roomId + "', BedCount=" + beds.size() + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Room room = (Room) obj;
        return roomId.equals(room.roomId) && 
               bedCount == room.bedCount; // Also compare bed count
    }
    
    @Override
    public int hashCode() {
        int result = roomId.hashCode();
        result = 31 * result + bedCount; // Include bedCount in hash
        return result;
    }
}

import java.util.ArrayList;
import java.util.List;

public class Ward {
    private String wardId;
    private String wardName;
    private List<Room> rooms;
    private String wardType; // Medical, Surgical, Psychiatric, etc.

    public Ward(String wardId, String wardName, String wardType) {
        this.wardId = wardId;
        this.wardName = wardName;
        this.wardType = wardType;
        this.rooms = new ArrayList<>();
    }

    // Add rooms with varying bed counts (1-4 beds)
    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.hasAvailableBed()) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public String getWardId() { return wardId; }
    public String getWardName() { return wardName; }
    public String getWardType() { return wardType; }
    public List<Room> getRooms() { return new ArrayList<>(rooms); }
    
    @Override
    public String toString() {
        return wardName + " (" + wardType + ")";
    }
}

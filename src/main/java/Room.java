import java.util.ArrayList;

public class Room {
    private String roomId;
    private ArrayList<Bed> beds;

    public Room(String roomId, int bedCount) {
        this.roomId = roomId;
        beds = new ArrayList<>();
        for (int i = 1; i <= bedCount; i++) {
            beds.add(new Bed(roomId + "-B" + i));
        }
    }

    public String getRoomId() { return roomId; }
    public ArrayList<Bed> getBeds() { return beds; }
}

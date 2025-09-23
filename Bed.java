public class Bed {
    private String bedId;
    private Resident occupant;

    public Bed(String bedId) {
        this.bedId = bedId;
        this.occupant = null;
    }

    public String getBedId() { return bedId; }
    public Resident getOccupant() { return occupant; }
    public void assignResident(Resident resident) { this.occupant = resident; }
    public void removeResident() { this.occupant = null; }
}

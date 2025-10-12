import java.io.Serializable;

public class Bed implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String bedId;
    private Resident occupant;

    public Bed(String bedId) {
        this.bedId = bedId;
        this.occupant = null;
    }

    public String getBedId() {
        return bedId;
    }

    public Resident getOccupant() {
        return occupant;
    }

    public void assignResident(Resident resident) {
        this.occupant = resident;
    }

    public void release() {
        this.occupant = null;
    }
    
    @Override
    public String toString() {
        return getBedId() + (getOccupant() != null ? " (Occupied)" : " (Free)");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Bed bed = (Bed) obj;
        return bedId.equals(bed.bedId);
    }
    
    @Override
    public int hashCode() {
        return bedId.hashCode();
    }
}

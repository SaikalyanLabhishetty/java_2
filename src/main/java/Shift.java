// Shift.java
import java.io.Serializable;

public class Shift implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String shiftId;
    private String shiftName;
    private String startTime;
    private String endTime;
    private String colorCode;

    public Shift(String shiftId, String shiftName, String startTime, String endTime, String colorCode) {
        this.shiftId = shiftId;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.colorCode = colorCode;
    }

    public String getShiftId() { return shiftId; }
    public String getShiftName() { return shiftName; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getColorCode() { return colorCode; }
    
    @Override
    public String toString() {
        return shiftName + " (" + startTime + "-" + endTime + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Shift shift = (Shift) obj;
        return shiftId.equals(shift.shiftId);
    }
    
    @Override
    public int hashCode() {
        return shiftId.hashCode();
    }
}

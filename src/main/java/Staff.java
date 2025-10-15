import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String staffId;
    protected String name;
    protected String role;
    protected int shiftCount;
    private List<Shift> assignedShifts;

    public Staff(String staffId, String name, String role) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
        this.shiftCount = 0;
         this.assignedShifts = new ArrayList<>(); // Always initialize
    }

    public String getStaffId() { return staffId; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public int getShiftCount() { return shiftCount; }

    public abstract void performDuty();
    
    public boolean canPerformAction(String action) {
        return true;
    }

    // *** KEY FIX: Fixed type mismatch ***
    // Removed the CareHome.Shift qualification - now using Shift directly
       public void addShift(Shift shift) {
        if (!assignedShifts.contains(shift)) { // Prevent duplicates
            assignedShifts.add(shift);
            shiftCount++;
        }
    }

    public List<Shift> getAssignedShifts() {
        return new ArrayList<>(assignedShifts);
    }
    
    @Override
    public String toString() {
        return "Staff{staffId='" + staffId + "', name='" + name + "', role='" + role + "'}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Staff staff = (Staff) obj;
        return staffId.equals(staff.staffId);
    }
    
    @Override
    public int hashCode() {
        return staffId.hashCode();
    }
}

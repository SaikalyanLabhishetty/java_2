import java.io.Serializable;

public abstract class Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String staffId;
    protected String name;
    protected String role;
    protected int shiftCount;

    public Staff(String staffId, String name, String role) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
        this.shiftCount = 0;
    }

    public String getStaffId() { return staffId; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public int getShiftCount() { return shiftCount; }

    public void addShift() { shiftCount++; }
    public abstract void performDuty();
    
    public boolean canPerformAction(String action) {
        return true;
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

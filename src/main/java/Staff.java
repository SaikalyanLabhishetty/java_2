public abstract class Staff {
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
    // Abstract method for specific responsibilities
    public abstract void performDuty();
}

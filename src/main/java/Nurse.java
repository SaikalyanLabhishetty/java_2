public class Nurse extends Staff {
    public Nurse(String staffId, String name) {
        super(staffId, name, "Nurse");
    }

    @Override
    public void performDuty() {
        // Nurse's responsibilities (administer medication, etc.)
    }

    @Override
    public String getRole() {
        return "Nurse";
    }

    @Override
    public boolean canPerformAction(String action) {
        return "checkVitals".equals(action) || "administerMedication".equals(action);
    }
    
    @Override
    public String toString() {
        return "Nurse{staffId='" + staffId + "', name='" + name + "'}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Nurse nurse = (Nurse) obj;
        return staffId.equals(nurse.staffId);
    }
    
    @Override
    public int hashCode() {
        return staffId.hashCode();
    }
}

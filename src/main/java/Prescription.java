public class Prescription {
    private String medicine;
    private String dosage;
    private String timing;
    private Doctor prescribedBy;

    public Prescription(String medicine, String dosage, String timing, Doctor prescribedBy) {
        this.medicine = medicine;
        this.dosage = dosage;
        this.timing = timing;
        this.prescribedBy = prescribedBy;
    }

    public String getMedicine() { return medicine; }
    public String getDosage() { return dosage; }
    public String getTiming() { return timing; }
    public Doctor getPrescribedBy() { return prescribedBy; }
    
    @Override
    public String toString() {
        return "Prescription{medicine='" + medicine + "', dosage='" + dosage + 
               "', timing='" + timing + "', prescribedBy='" + prescribedBy.getName() + "'}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Prescription that = (Prescription) obj;
        return medicine.equals(that.medicine) && 
               dosage.equals(that.dosage) && 
               timing.equals(that.timing) &&
               (prescribedBy == null ? that.prescribedBy == null : 
                prescribedBy.getStaffId().equals(that.prescribedBy.getStaffId()));
    }
    
    @Override
    public int hashCode() {
        int result = medicine.hashCode();
        result = 31 * result + dosage.hashCode();
        result = 31 * result + timing.hashCode();
        result = 31 * result + (prescribedBy == null ? 0 : prescribedBy.getStaffId().hashCode());
        return result;
    }
}

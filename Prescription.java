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
}

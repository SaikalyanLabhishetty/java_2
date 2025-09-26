import java.io.Serializable;
import java.util.ArrayList;
public class Resident implements Serializable{
    private String residentId;
    private String name;
    private String gender;
    private Bed assignedBed;
    private String medicalInfo;

    private ArrayList<Prescription> prescriptions;

    public Resident(String residentId, String name, String gender, String medicalInfo) {
        this.residentId = residentId;
        this.name = name;
        this.gender = gender;
        this.medicalInfo = medicalInfo;
        this.assignedBed = null;
        this.prescriptions = new ArrayList<>();
    }

    // getters and setters
    public String getResidentId() { return residentId; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public Bed getAssignedBed() { return assignedBed; }
    public String getMedicalInfo() { return medicalInfo; }
    public void setAssignedBed(Bed bed) { this.assignedBed = bed; }
    public void addPrescription(Prescription prescription) {
        prescriptions.add(prescription);
    }
    // getter for prescriptions if needed
    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }
}

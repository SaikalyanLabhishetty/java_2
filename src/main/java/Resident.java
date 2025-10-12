import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Resident implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String residentId;
    private String name;
    private String gender;
    private String medicalInfo;
    private Bed assignedBed;
    private List<Prescription> prescriptions;

    public Resident(String residentId, String name, String gender, String medicalInfo) {
        this.residentId = residentId;
        this.name = name;
        this.gender = gender;
        this.medicalInfo = medicalInfo;
        this.prescriptions = new ArrayList<>();
    }

    public String getResidentId() {
        return residentId;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getMedicalInfo() {
        return medicalInfo;
    }

    public Bed getAssignedBed() {
        return assignedBed;
    }

    public void setAssignedBed(Bed assignedBed) {
        this.assignedBed = assignedBed;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
    }
    
    @Override
    public String toString() {
        return "Resident{residentId='" + residentId + "', name='" + name + 
               "', assignedBed=" + (assignedBed != null ? assignedBed.getBedId() : "None") + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Resident resident = (Resident) obj;
        return residentId.equals(resident.residentId);
    }
    
    @Override
    public int hashCode() {
        return residentId.hashCode();
    }
}

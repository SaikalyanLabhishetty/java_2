import exceptions.AuthorizationException;

public class Doctor extends Staff {
    public Doctor(String staffId, String name) {
        super(staffId, name, "Doctor");
    }

    @Override
    public void performDuty() {
        System.out.println("Doctor is prescribing medication.");
    }

    public void prescribe(Resident resident, Prescription prescription) throws AuthorizationException {
        if (!(this instanceof Doctor)) {
            throw new AuthorizationException("Only doctors can prescribe medications.");
        }
        resident.addPrescription(prescription);
    }

    @Override
    public String getRole() {
        return "Doctor";
    }

    @Override
    public boolean canPerformAction(String action) {
        return true;
    }
    
    @Override
    public String toString() {
        return "Doctor{staffId='" + staffId + "', name='" + name + "'}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Doctor doctor = (Doctor) obj;
        return staffId.equals(doctor.staffId);
    }
    
    @Override
    public int hashCode() {
        return staffId.hashCode();
    }
}

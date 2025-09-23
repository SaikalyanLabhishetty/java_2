import exceptions.AuthorizationException;

public class Doctor extends Staff {
    public Doctor(String staffId, String name) {
        super(staffId, name, "Doctor");
    }

    @Override
    public void performDuty() {
        // Example implementation - can be customized as needed
        System.out.println("Doctor is prescribing medication.");
    }

    public void prescribe(Resident resident, Prescription prescription) throws AuthorizationException {
        if (!(this instanceof Doctor)) {
            throw new AuthorizationException("Only doctors can prescribe medications.");
        }
        resident.addPrescription(prescription);
    }
}

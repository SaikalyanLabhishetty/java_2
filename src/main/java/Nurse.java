public class Nurse extends Staff {
    public Nurse(String staffId, String name) {
        super(staffId, name, "Nurse");
    }

    @Override
    public void performDuty() {
        // Nurse's responsibilities (administer medication, etc.)
    }
}

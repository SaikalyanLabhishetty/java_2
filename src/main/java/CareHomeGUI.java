import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class CareHomeGUI extends Application {
    private CareHome careHome = new CareHome(); // Connect your backend

    @Override
    public void start(Stage primaryStage) {
        careHome.initializeRooms();
        primaryStage.setTitle("Care Home Management System");

        Button addResidentButton = new Button("Add Resident");
        Button addStaffButton = new Button("Add Staff");
        Button assignBedButton = new Button("Assign Bed");
        Button viewResidentsButton = new Button("View Residents");

        addResidentButton.setOnAction(e -> {
            Dialog<Resident> dialog = new Dialog<>();
            dialog.setTitle("Add Resident");

            Label idLabel = new Label("Resident ID:");
            TextField idField = new TextField();
            Label nameLabel = new Label("Name:");
            TextField nameField = new TextField();
            Label genderLabel = new Label("Gender:");
            TextField genderField = new TextField();
            Label medLabel = new Label("Medical Info:");
            TextField medField = new TextField();

            VBox vbox = new VBox(5, idLabel, idField, nameLabel, nameField, genderLabel, genderField, medLabel, medField);
            dialog.getDialogPane().setContent(vbox);

            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(btn -> {
                if (btn == addButtonType) {
                    return new Resident(idField.getText().trim(), nameField.getText().trim(), genderField.getText().trim(), medField.getText().trim());
                }
                return null;
            });

            Optional<Resident> result = dialog.showAndWait();
            result.ifPresent(resident -> {
                boolean added = careHome.addResident(resident);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, added ? "Resident added successfully" : "Failed to add resident");
                alert.showAndWait();
            });
        });

        addStaffButton.setOnAction(e -> {
            Dialog<Staff> dialog = new Dialog<>();
            dialog.setTitle("Add Staff");

            Label idLabel = new Label("Staff ID:");
            TextField idField = new TextField();
            Label nameLabel = new Label("Name:");
            TextField nameField = new TextField();
            Label roleLabel = new Label("Role (Doctor/Nurse):");
            TextField roleField = new TextField();

            VBox vbox = new VBox(5, idLabel, idField, nameLabel, nameField, roleLabel, roleField);
            dialog.getDialogPane().setContent(vbox);

            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(btn -> {
                if (btn == addButtonType) {
                    if ("Doctor".equalsIgnoreCase(roleField.getText().trim())) {
                        return new Doctor(idField.getText().trim(), nameField.getText().trim());
                    } else if ("Nurse".equalsIgnoreCase(roleField.getText().trim())) {
                        return new Nurse(idField.getText().trim(), nameField.getText().trim());
                    } else {
                        return null;
                    }
                }
                return null;
            });

            Optional<Staff> result = dialog.showAndWait();
            result.ifPresent(staff -> {
                boolean added = careHome.addStaff(staff);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, added ? "Staff added successfully" : "Failed to add staff");
                alert.showAndWait();
            });
        });

        assignBedButton.setOnAction(e -> {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Assign Bed");

            Label residentLabel = new Label("Resident ID:");
            TextField residentField = new TextField();
            Label bedLabel = new Label("Bed ID (e.g. Room1-B1):");
            TextField bedField = new TextField();

            VBox vbox = new VBox(5, residentLabel, residentField, bedLabel, bedField);
            dialog.getDialogPane().setContent(vbox);

            ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(btn -> null);

            Optional<Void> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    careHome.assignBed(residentField.getText().trim(), bedField.getText().trim());
                    new Alert(Alert.AlertType.INFORMATION, "Bed assigned successfully.").showAndWait();
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait();
                }
            }
        });
        assignBedButton.setOnAction(event -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Assign Bed");

            TextField residentIdField = new TextField();
            TextField bedIdField = new TextField();
            VBox vbox = new VBox(
                new Label("Resident ID:"), residentIdField,
                new Label("Bed ID (e.g. Room1-B1):"), bedIdField
            );
            vbox.setSpacing(5);
            dialog.getDialogPane().setContent(vbox);

            ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(btn -> btn); // returns ButtonType

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == assignButtonType) {
                try {
                    careHome.assignBed(residentIdField.getText().trim(), bedIdField.getText().trim());
                    new Alert(Alert.AlertType.INFORMATION, "Bed assigned successfully.").showAndWait();
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait();
                }
            }
        });

        viewResidentsButton.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Resident r : careHome.getAllResidents()) {
                sb.append("ID: ").append(r.getResidentId())
                  .append(", Name: ").append(r.getName())
                  .append(", Bed: ").append(r.getAssignedBed() == null ? "None" : r.getAssignedBed().getBedId())
                  .append("\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.length() > 0 ? sb.toString() : "No residents available.");
            alert.setTitle("Residents");
            alert.setHeaderText("Resident List");
            alert.getDialogPane().setMinHeight(200);
            alert.showAndWait();
        });

        VBox vbox = new VBox(12, addResidentButton, addStaffButton, assignBedButton, viewResidentsButton);
        vbox.setPadding(new javafx.geometry.Insets(20));
        primaryStage.setScene(new Scene(vbox, 300, 240));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

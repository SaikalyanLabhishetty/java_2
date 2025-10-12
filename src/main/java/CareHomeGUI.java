import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.util.Optional;
import javafx.scene.control.Menu;

public class CareHomeGUI extends Application {
    private CareHome careHome = new CareHome(); // Connect your backend

    @Override
    public void start(Stage primaryStage) {
        careHome.initializeRooms();
        primaryStage.setTitle("Care Home Management System");
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
        primaryStage.setResizable(true);

        MenuBar menuBar = new MenuBar();
        Menu dataMenu = new Menu("Data");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem loadItem = new MenuItem("Load/Restart");
        dataMenu.getItems().addAll(saveItem, loadItem);
        menuBar.getMenus().add(dataMenu);

        // Save/Load actions
        saveItem.setOnAction(e -> {
            try {
                careHome.saveStateToFile("carehome.dat");
                showInfoDialog("Data saved.");
            } catch (Exception ex) {
                showErrorDialog("Save failed: " + ex.getMessage());
            }
        });
        loadItem.setOnAction(e -> {
            try {
                careHome.loadStateFromFile("carehome.dat");
                showInfoDialog("Data loaded and system restarted.");
            } catch (Exception ex) {
                showErrorDialog("Load failed: " + ex.getMessage());
            }
        });

        Button addResidentButton = new Button("Add Resident");
        Button addStaffButton = new Button("Add Staff");
        Button assignBedButton = new Button("Assign Bed");
        Button assignStaffButton = new Button("Assign Staff to Resident");
        Button viewResidentsButton = new Button("View Residents");
        Button viewStaffButton = new Button("View Staff");

        // Add Resident handler
        addResidentButton.setOnAction(e -> {
            Dialog<Resident> dialog = new Dialog<>();
            dialog.setTitle("Add Resident");

            TextField idField = new TextField();
            TextField nameField = new TextField();
            TextField genderField = new TextField();
            TextField medField = new TextField();
            VBox vbox = new VBox(
                new Label("Resident ID:"), idField,
                new Label("Name:"), nameField,
                new Label("Gender:"), genderField,
                new Label("Medical Info:"), medField
            );
            vbox.setSpacing(5);
            dialog.getDialogPane().setContent(vbox);
            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(btn -> {
                if (btn == addButtonType &&
                        !idField.getText().trim().isEmpty() &&
                        !nameField.getText().trim().isEmpty() &&
                        !genderField.getText().trim().isEmpty()) {
                    return new Resident(idField.getText().trim(), nameField.getText().trim(),
                            genderField.getText().trim(), medField.getText().trim());
                }
                return null;
            });

            Optional<Resident> result = dialog.showAndWait();
            result.ifPresent(resident -> {
                boolean added = careHome.addResident(resident);
                showInfoDialog(added ? "Resident added successfully" : "Failed to add resident");
            });
        });

        // Add Staff handler
        addStaffButton.setOnAction(e -> {
            Dialog<Staff> dialog = new Dialog<>();
            dialog.setTitle("Add Staff");

            TextField idField = new TextField();
            TextField nameField = new TextField();
            TextField roleField = new TextField();
            VBox vbox = new VBox(
                new Label("Staff ID:"), idField,
                new Label("Name:"), nameField,
                new Label("Role (Doctor/Nurse):"), roleField
            );
            vbox.setSpacing(5);
            dialog.getDialogPane().setContent(vbox);
            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(btn -> {
                String role = roleField.getText().trim();
                if (btn == addButtonType &&
                        !idField.getText().trim().isEmpty() &&
                        !nameField.getText().trim().isEmpty() &&
                        (role.equalsIgnoreCase("Doctor") || role.equalsIgnoreCase("Nurse"))) {
                    if (role.equalsIgnoreCase("Doctor"))
                        return new Doctor(idField.getText().trim(), nameField.getText().trim());
                    else
                        return new Nurse(idField.getText().trim(), nameField.getText().trim());
                }
                return null;
            });

            Optional<Staff> result = dialog.showAndWait();
            result.ifPresent(staff -> {
                boolean added = careHome.addStaff(staff);
                showInfoDialog(added ? "Staff added successfully" : "Failed to add staff");
            });
        });

        // Assign Bed handler (USING ComboBox for selection, not text)
        assignBedButton.setOnAction(event -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Assign Bed");

            ComboBox<Resident> residentBox = new ComboBox<>();
            ComboBox<Bed> bedBox = new ComboBox<>();
            residentBox.getItems().addAll(careHome.getUnassignedResidents());
            bedBox.getItems().addAll(careHome.getAvailableBeds());

            residentBox.setPromptText("Select a Resident");
            bedBox.setPromptText("Select a Bed");

            VBox vbox = new VBox(
                new Label("Resident:"), residentBox,
                new Label("Bed:"), bedBox
            );
            vbox.setSpacing(5);
            dialog.getDialogPane().setContent(vbox);

            ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(btn -> btn);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == assignButtonType) {
                Resident selectedResident = residentBox.getValue();
                Bed selectedBed = bedBox.getValue();
                if (selectedResident != null && selectedBed != null) {
                    try {
                        careHome.assignBed(selectedResident.getResidentId(), selectedBed.getBedId());
                        showInfoDialog("Bed assigned successfully.");
                    } catch (Exception ex) {
                        showErrorDialog("Error: " + ex.getMessage());
                    }
                } else {
                    showErrorDialog("Please select both Resident and Bed.");
                }
            }
        });

        // View Residents handler
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

       VBox mainVBox = new VBox(12,
            addResidentButton,
            addStaffButton,
            assignBedButton,
            viewResidentsButton,
            viewStaffButton,
            assignStaffButton
        );

        mainVBox.setPadding(new javafx.geometry.Insets(20));
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mainVBox);

        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();

        // view staff
        viewStaffButton.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Staff s : careHome.getAllStaff()) { // assumes you added getAllStaff()
                sb.append("ID: ").append(s.getStaffId())
                .append(", Name: ").append(s.getName())
                .append(", Role: ").append(s.getRole())
                .append("\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.length() > 0 ? sb.toString() : "No staff available.");
            alert.setTitle("Staff List");
            alert.setHeaderText("Care Home Staff");
            alert.getDialogPane().setMinHeight(200);
            alert.showAndWait();
        });

        assignStaffButton.setOnAction(e -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Assign Staff to Resident");

            ComboBox<Staff> staffBox = new ComboBox<>();
            ComboBox<Resident> residentBox = new ComboBox<>();
            staffBox.getItems().addAll(careHome.getAllStaff());
            residentBox.getItems().addAll(careHome.getAllResidents());

            staffBox.setPromptText("Select Staff");
            residentBox.setPromptText("Select Resident");

            VBox vbox = new VBox(
                new Label("Staff:"), staffBox,
                new Label("Resident:"), residentBox
            );
            vbox.setSpacing(6);
            dialog.getDialogPane().setContent(vbox);

            ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);
            dialog.setResultConverter(btn -> btn);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == assignButtonType) {
                Staff selectedStaff = staffBox.getValue();
                Resident selectedResident = residentBox.getValue();
                if (selectedStaff != null && selectedResident != null) {
                    // Add logic in Resident or CareHome, e.g.:
                    // selectedResident.assignDoctor(selectedStaff); 
                    showInfoDialog("Staff " + selectedStaff.getName() + " assigned to " + selectedResident.getName());
                } else {
                    showErrorDialog("Please select both Staff and Resident.");
                }
            }
        });
        primaryStage.getScene().getStylesheets().add("""
        .button {
            -fx-font-size: 14px;
            -fx-padding: 10 20;
            -fx-background-radius: 8;
            -fx-background-color: #4A90E2;
            -fx-text-fill: white;
        }
        .button:hover {
            -fx-background-color: #3A7BC8;
        }
        .label {
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-text-fill: #333;
        }
        .dialog-pane {
            -fx-font-family: 'Segoe UI';
        }
        """);
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle("Error");
        alert.showAndWait();
    }

    private void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Success");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

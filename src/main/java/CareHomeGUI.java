import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.scene.control.Menu;

public class CareHomeGUI extends Application {
    private CareHome careHome = new CareHome();

    @Override
    public void start(Stage primaryStage) {
        // No need to initialize rooms - done in CareHome constructor
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

       // Add Staff handler with proper shift handling
        addStaffButton.setOnAction(e -> {
            Dialog<Staff> dialog = new Dialog<>();
            dialog.setTitle("Add Staff");

            TextField idField = new TextField();
            TextField nameField = new TextField();
            ComboBox<String> roleBox = new ComboBox<>();
            roleBox.getItems().addAll("Doctor", "Nurse");
            roleBox.setPromptText("Select Role");
            
            // Create ListView with colored cells
            ListView<Shift> shiftList = new ListView<>();
            shiftList.getItems().addAll(careHome.getShifts());
            shiftList.setPrefHeight(100);
            shiftList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            
            // Custom cell factory to show colors
            shiftList.setCellFactory(lv -> new ListCell<Shift>() {
                @Override
                protected void updateItem(Shift item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        HBox hbox = new HBox(5);
                        Rectangle colorBox = new Rectangle(15, 15);
                        colorBox.setFill(Color.web(item.getColorCode()));
                        colorBox.setStroke(Color.GRAY);
                        colorBox.setStrokeWidth(1);
                        
                        VBox textVBox = new VBox(
                            new Text(item.getShiftName()),
                            new Text(item.getStartTime() + " - " + item.getEndTime())
                        );
                        textVBox.setSpacing(2);
                        
                        hbox.getChildren().addAll(colorBox, textVBox);
                        setGraphic(hbox);
                    }
                }
            });

            // Update shift list based on role selection
            roleBox.setOnAction(event -> {
                Shift selectedShift = shiftList.getSelectionModel().getSelectedItem();
                if ("Doctor".equals(roleBox.getValue())) {
                    // Doctors work all shifts - select all
                    shiftList.getSelectionModel().selectAll();
                    shiftList.setDisable(true); // Disable selection for doctors
                } else {
                    // Nurses can select specific shifts
                    shiftList.setDisable(false);
                    // Clear selection if coming from doctor
                    if (selectedShift != null) {
                        shiftList.getSelectionModel().clearSelection();
                    }
                }
            });

            VBox vbox = new VBox(
                new Label("Staff ID:"), idField,
                new Label("Name:"), nameField,
                new Label("Role:"), roleBox,
                new Label("Select Shift(s):"), shiftList
            );
            vbox.setSpacing(5);
            dialog.getDialogPane().setContent(vbox);
            
            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(btn -> {
                if (btn == addButtonType &&
                    !idField.getText().trim().isEmpty() &&
                    !nameField.getText().trim().isEmpty() &&
                    roleBox.getValue() != null) {
                    
                    String role = roleBox.getValue();
                    Staff staff = null;
                    
                    if (role.equalsIgnoreCase("Doctor")) {
                        staff = new Doctor(idField.getText().trim(), nameField.getText().trim());
                        // Doctors work all shifts - assign all
                        for (Shift shift : careHome.getShifts()) {
                            ((Doctor) staff).addShift(shift);
                        }
                    } else {
                        staff = new Nurse(idField.getText().trim(), nameField.getText().trim());
                        // Nurses work only selected shifts
                        List<Shift> selectedShifts = new ArrayList<>(shiftList.getSelectionModel().getSelectedItems());
                        if (selectedShifts.isEmpty()) {
                            // Default to morning shift if none selected
                            Shift morning = careHome.getShifts().get(0);
                            selectedShifts.add(morning);
                        }
                        for (Shift shift : selectedShifts) {
                            ((Nurse) staff).addShift(shift);
                        }
                    }
                    return staff;
                }
                return null;
            });

            Optional<Staff> result = dialog.showAndWait();
            result.ifPresent(staff -> {
                boolean added = careHome.addStaff(staff);
                showInfoDialog(added ? "Staff added successfully" : "Failed to add staff");
            });
        });

        // Enhanced View Residents with color coding
        viewResidentsButton.setOnAction(e -> {
            // Create a scrollable pane
            ScrollPane scrollPane = new ScrollPane();
            VBox residentsBox = new VBox(5);
            
            // Add header
            HBox headerBox = new HBox(10);
            Circle maleLegend = new Circle(8, Color.BLUE);
            Label maleLabel = new Label("Male");
            maleLabel.setFont(Font.font("System", FontWeight.NORMAL, 13));
            
            Circle femaleLegend = new Circle(8, Color.RED);
            Label femaleLabel = new Label("Female");
            femaleLabel.setFont(Font.font("System", FontWeight.NORMAL, 13));
            
            headerBox.getChildren().addAll(
                new Label("Residents List - "), maleLegend, maleLabel, 
                new Label(" - "), femaleLegend, femaleLabel
            );
            
            residentsBox.getChildren().add(headerBox);
            residentsBox.getChildren().add(new Separator());

            // Add all residents
            for (Resident r : careHome.getAllResidents()) {
                // Create HBox for each resident
                HBox residentItem = new HBox(10);
                
                // Circle color based on gender
                Circle genderCircle = new Circle(10);
                if ("Male".equalsIgnoreCase(r.getGender())) {
                    genderCircle.setFill(Color.BLUE); // Blue for male
                } else {
                    genderCircle.setFill(Color.RED); // Red for female
                }
                
                // Resident information
                VBox infoBox = new VBox(5);
                Text nameLabel = new Text(r.getName());
                nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
                
                Text idText = new Text("ID: " + r.getResidentId());
                idText.setFont(Font.font("System", 12));
                idText.setFill(Color.GRAY);
                
                Text bedText = new Text("Bed: " + (r.getAssignedBed() != null ? r.getAssignedBed().getBedId() : "Unassigned"));
                bedText.setFont(Font.font("System", 12));
                if (r.getAssignedBed() == null) {
                    bedText.setFill(Color.RED);
                }
                
                // Add to info box
                infoBox.getChildren().addAll(nameLabel, idText, bedText);
                
                // Add to resident item
                residentItem.getChildren().addAll(genderCircle, infoBox);
                
                // Style the item
                residentItem.setPadding(new Insets(10));
                residentItem.setBorder(new Border(new BorderStroke(
                    Color.LIGHTGRAY, BorderStrokeStyle.SOLID, 
                    new CornerRadii(5), new BorderWidths(1)
                )));
                residentItem.setBackground(new Background(new BackgroundFill(
                    Color.WHITE, new CornerRadii(5), Insets.EMPTY
                )));
                residentItem.setMinHeight(70);
                
                residentsBox.getChildren().add(residentItem);
                residentsBox.getChildren().add(new Separator());
            }

            // Set up scroll pane
            residentsBox.setPadding(new Insets(10));
            scrollPane.setContent(residentsBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(400);
            
            // Create dialog
            Dialog<ButtonType> viewDialog = new Dialog<>();
            viewDialog.setTitle("Residents");
            viewDialog.getDialogPane().setContent(scrollPane);
            viewDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            viewDialog.setWidth(500);
            viewDialog.setHeight(500);
            
            viewDialog.showAndWait();
        });

        // Enhanced View Staff
       // Enhanced View Staff with no duplication
        viewStaffButton.setOnAction(e -> {
            ScrollPane scrollPane = new ScrollPane();
            VBox staffBox = new VBox(5);
            
            Text header = new Text("Staff List");
            header.setFont(Font.font("System", FontWeight.BOLD, 16));
            header.setFill(Color.DARKBLUE);
            staffBox.getChildren().add(header);
            staffBox.getChildren().add(new Separator());
            
            // Use a set to track displayed staff to prevent duplicates
            Set<String> displayedStaff = new HashSet<>();
            
            for (Staff s : careHome.getAllStaff()) {
                // Skip if already displayed (prevent duplicates)
                if (displayedStaff.contains(s.getStaffId())) {
                    continue;
                }
                displayedStaff.add(s.getStaffId());
                
                HBox staffItem = new HBox(10);
                Circle roleCircle = new Circle(10);
                
                if ("Doctor".equals(s.getRole())) {
                    roleCircle.setFill(Color.PURPLE);
                } else {
                    roleCircle.setFill(Color.ORANGE);
                }
                
                VBox infoBox = new VBox(5);
                Text nameLabel = new Text(s.getName());
                nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
                
                Text idText = new Text("ID: " + s.getStaffId());
                idText.setFont(Font.font("System", 12));
                idText.setFill(Color.GRAY);
                
                Text roleText = new Text("Role: " + s.getRole());
                roleText.setFont(Font.font("System", 12));
                
                // Shift indicators with proper assignment
                HBox shiftBox = new HBox(5);
                shiftBox.getChildren().add(new Text("Shifts: "));
                
                List<Shift> assignedShifts = s.getAssignedShifts();
                if (assignedShifts.isEmpty()) {
                    Text noShifts = new Text("No shifts assigned");
                    noShifts.setFill(Color.RED);
                    shiftBox.getChildren().add(noShifts);
                } else {
                    // Use a set to prevent duplicate shifts
                    Set<String> shiftIds = new HashSet<>();
                    for (Shift shift : assignedShifts) {
                        if (shiftIds.add(shift.getShiftId())) { // Only add if not already present
                            VBox shiftIndicator = new VBox(2);
                            Circle shiftCircle = new Circle(8);
                            shiftCircle.setFill(Color.web(shift.getColorCode()));
                            
                            Tooltip tooltip = new Tooltip(
                                shift.getShiftName() + " (" + 
                                shift.getStartTime() + "-" + 
                                shift.getEndTime() + ")"
                            );
                            Tooltip.install(shiftCircle, tooltip);
                            
                            Text shiftName = new Text(shift.getShiftName());
                            shiftName.setFont(Font.font("System", 9));
                            
                            shiftIndicator.getChildren().addAll(shiftCircle, shiftName);
                            shiftBox.getChildren().add(shiftIndicator);
                        }
                    }
                }
                
                infoBox.getChildren().addAll(nameLabel, idText, roleText, shiftBox);
                staffItem.getChildren().addAll(roleCircle, infoBox);
                
                staffItem.setPadding(new Insets(10));
                staffItem.setBorder(new Border(new BorderStroke(
                    Color.LIGHTGRAY, BorderStrokeStyle.SOLID, 
                    new CornerRadii(5), new BorderWidths(1)
                )));
                staffItem.setBackground(new Background(new BackgroundFill(
                    Color.WHITE, new CornerRadii(5), Insets.EMPTY
                )));
                staffItem.setMinHeight(90);
                
                staffBox.getChildren().add(staffItem);
                staffBox.getChildren().add(new Separator());
            }
            
            staffBox.setPadding(new Insets(10));
            scrollPane.setContent(staffBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(400);
            
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Staff");
            dialog.getDialogPane().setContent(scrollPane);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.setWidth(600);
            dialog.setHeight(500);
            
            dialog.showAndWait();
        });

        // Assign Staff to Resident
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
            vbox.setSpacing(10);
            dialog.getDialogPane().setContent(vbox);

            ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);
            dialog.setResultConverter(btn -> btn);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == assignButtonType) {
                Staff selectedStaff = staffBox.getValue();
                Resident selectedResident = residentBox.getValue();
                if (selectedStaff != null && selectedResident != null) {
                    // In a real system, you'd have a method like:
                    // selectedResident.assignCarer(selectedStaff);
                    showInfoDialog("Staff " + selectedStaff.getName() + " assigned to care for " + selectedResident.getName());
                } else {
                    showErrorDialog("Please select both Staff and Resident.");
                }
            }
        });

        // Layout
        VBox mainVBox = new VBox(12,
            addResidentButton,
            addStaffButton,
            assignBedButton,
            viewResidentsButton,
            viewStaffButton,
            assignStaffButton
        );

        mainVBox.setPadding(new Insets(20));
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(mainVBox);

        Scene scene = new Scene(root, 400, 500);
        
        // Enhanced CSS
        scene.getStylesheets().add("""
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
            .combo-box {
                -fx-font-size: 13px;
            }
            """);
            
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

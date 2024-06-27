package com.project;
import com.notifcationPackage.NotificationHelper;
import com.person.User;
import com.project.Project.ProjectStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.project.Project.ProjectStatus.*;

public class ProjektController {


    public Label laufendProjektZahl1;
    public Label beendetProjektZahl11;
    public Label wartendeProjektZahl1;
    public Label beendetProjektZahl111;
    public Label projekteZahler;
    @FXML
    private TableColumn<Project, Double> amount_paidColumn1;
    @FXML
    private Label displayPercentageOfPayedAmount;
    @FXML
    private Label projektBeschreibungLabel;
    @FXML
    private Label projektNameLabel;
    @FXML
    private Label projektbudgetLabel;
    @FXML
    private Label projektStartLabel;
    @FXML
    private Label projektEndLabel;
    @FXML
    private Label projektStatusLabel;
    @FXML
    private Label projektTeilnehmerLabel1;
    @FXML
    private Label labelProjektId;
    @FXML
    private TextField textFieldProjektName;
    @FXML
    private GridPane gridPane;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TableView<Project> projectTableView;
    @FXML
    private TableColumn<Project, String> nameColumn;
    @FXML
    private TableColumn<Project, Double> budgetColumn;
    @FXML
    private TableColumn<Project, LocalDate> startDateColumn;
    @FXML
    private TableColumn<Project, LocalDate> endDateColumn;
    @FXML
    private TableColumn<Project, Integer> participantsColumn;
    @FXML
    private TableColumn<Project, String> statusColumn;
    @FXML
    private TableColumn<Project, Double> amount_paidColumn;
    @FXML
    private TextField budgetTextField;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField teilnehmerTextField;
    @FXML
    private Button createNewProjectButton;
    @FXML
    private Button status;
    @FXML
    private ImageView createIcon;
    @FXML
    private ListView<Project> projectListView;
    @FXML
    private ListView<Project> listView;
    @FXML
    private ImageView st;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Label mo;
    @FXML
    private Button updateButton;
    @FXML
    private Button updateAbbrechenBtn;
    private boolean isUpdateMode = false; // Flag to track update mode
    private ObservableList<Project> projectList;
    protected Project currentProject;
    private List<User> users = new ArrayList<>();
    private User loggedInUser;
    @FXML
    private ProgressBar progr;

    // Initializing the ProjectStatus ComboBox
    private void initComboBox() {
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Laufend", "Beendet", "Wartend");
        statusComboBox.setItems(statusOptions);
    }

    // Initializing the TableView
    private void initTableView() {
        projectList = FXCollections.observableArrayList();
        projectTableView.setItems(projectList);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        budgetColumn.setCellValueFactory(new PropertyValueFactory<>("budget"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        participantsColumn.setCellValueFactory(new PropertyValueFactory<>("sharedPersons"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        amount_paidColumn.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
    }

    @FXML
    public void initialize() {
        System.out.print("Line 127 PC");
        initComboBox();
      initTableView();
     try {
  getAlleProjekte();
     } catch (SQLException e) {
         throw new RuntimeException(e);
      }
       initStatusCounts();
    }

    // Get project information from input fields
    @FXML
    private Project getProjectInformation() {
        String projectName = textFieldProjektName.getText();
        double projectBudget = 0.0;
        int numberOfParticipants = 1; // Default value is 1

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (projectName.isEmpty() || startDate == null || endDate == null) {
            NotificationHelper.showFailedNotification("Fehler", "Überprüfen Sie Ihre Eingaben!");
            return null;
        }

        try {
            projectBudget = Double.parseDouble(budgetTextField.getText());
            if (projectBudget == 0.0) {
                NotificationHelper.showFailedNotification("Fehler", "Das Budget darf nicht null sein!");
                return null;
            }

            String participantsText = teilnehmerTextField.getText().trim();
            if (!participantsText.isEmpty() && !participantsText.equals("0")) {
                numberOfParticipants = Math.max(Integer.parseInt(participantsText), 1);
             }
        } catch (NumberFormatException e) {
            NotificationHelper.showFailedNotification("Fehler", "Prüfen Sie Ihre Eingaben!");
            return null;
        }
        return new Project(projectName, projectBudget, numberOfParticipants, startDate, endDate);
    }

    // Clear text fields
    @FXML
    private void textFelderLoeschen() {
        textFieldProjektName.clear();
        budgetTextField.clear();
        teilnehmerTextField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }

    // Create a new project
    @FXML
    private void createNewProject() {
        Project project = getProjectInformation();
        if (project == null) return;
        ProjectManger.createProject(project.getName(), project.getBudget(), project.getSharedPersons(), project.getStartDate(), project.getEndDate());
        textFelderLoeschen();
        projectList.add(project);
        initialize();
        try {
            getAlleProjekte();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        isUpdateMode = false;
        createNewProjectButton.setText("Create Project");
        project.getMonatlicheZahlungenAPersone();
    }

    // Get all projects from the database
    @FXML
    public void getAlleProjekte() throws SQLException {

            List<Project> projectListFromDb = ProjectRepository.getAllProjects();
             projectList.clear();
            projectList.addAll(projectListFromDb);
            projectTableView.refresh();
            progressDisplay();
    }

    //wenn ein projekt ausgewählt von der Tabelle
    private Project getSelectedProject() {
        return projectTableView.getSelectionModel().getSelectedItem();
    }

    // Show status selection error
    private void showStatusSelectionError() {
        NotificationHelper.showFailedNotification("Fehler", "Bitte wählen Sie einen Status aus dem ComboBox aus.");
    }

    // Show project selection error
    private void showProjectSelectionError() {
        NotificationHelper.showFailedNotification("Fehler", "Bitte wählen Sie ein Projekt aus der Tabelle aus.");
    }

    // Change status of a project
    @FXML
        public void statusAendern() {
        Project project = projectTableView.getSelectionModel().getSelectedItem(); // Get the selected project

        if (project != null) {
            String status = statusComboBox.getValue(); // Get the selected status from the ComboBox

            if (status != null) {
                Project.ProjectStatus projectStatus = Project.ProjectStatus.valueOf(status.toUpperCase()); // Convert the status string to an enum
                project.setStatus(projectStatus); // Set the status of the project

                int projectId = project.getId(); // Assume the project ID is stored in the Project object
                ProjectRepository.setStatusOfProject(projectId, status);

                // Update the status directly in the TableView
                projectTableView.refresh(); // Refresh the table to reflect the changes

                // Show success message
                initStatusCounts();
                NotificationHelper.showSuccessNotification("Updated", "Status wurde geändert");
            } else {
                NotificationHelper.showFailedNotification("Fehler", "Bitte wählen Sie einen Status aus dem ComboBox aus.");
            }
        } else {
            NotificationHelper.showFailedNotification("Fehler", "Bitte wählen Sie ein Projekt aus der Tabelle aus.");
        }
    }


    // Delete a project
    @FXML
    public void delete() {
        Project project = getSelectedProject();
        if (project != null) {
            ProjectManger.deleteProject(project.getId());
            initialize();
            try {
                getAlleProjekte();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            NotificationHelper.showFailedNotification("Projekt fehlt", "Bitte wählen Sie ein Projekt aus der Tabelle");
        }
    }

    // Update project status
    private void updateProjectStatus(Project project, String status) {
        int projectId = project.getId();
        System.out.print(projectId+status);

        ProjectStatus projectStatus = valueOf(status.toUpperCase());
        project.setStatus(projectStatus);
        ProjectRepository.setStatusOfProject(projectId, status);
        projectTableView.refresh();
        try {
            getAlleProjekte();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        projectTableView.refresh();
        NotificationHelper.showSuccessNotification("Successes","Es wurde geändert");
    }


    // Display the value of the ProgressBar
    @FXML
    private void prozentualeBezahlterBeitraege() {
        Project selectedProject = getSelectedProject();
        if (selectedProject != null) {
            double percentagePaid = selectedProject.calculatePercentagePaid();
            progr.setProgress(percentagePaid / 100); // Set progress bar value is ∈[0-1]
            displayPercentageOfPayedAmount.setText(String.valueOf(percentagePaid) + "%");
            projektNameLabel.setText(selectedProject.getName());
            projektbudgetLabel.setText(selectedProject.getBudget() + " €");
            projektStartLabel.setText(String.valueOf(selectedProject.getStartDate()));
            projektEndLabel.setText(String.valueOf(selectedProject.getEndDate()));
            projektStatusLabel.setText(String.valueOf(selectedProject.getStatus()));
            labelProjektId.setText(String.valueOf(selectedProject.getId()));
        }
    }


    private void progressDisplay() {
        projectTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                prozentualeBezahlterBeitraege();
                Project selectedProject = getSelectedProject();

                if (selectedProject != null) {
                    String status = selectedProject.getStatusAsString();
                    //wartendeProjektZahl1.setText(status);
                    prozentualeBezahlterBeitraege();
                    beendetProjektZahl111.setText(status);
                }
            }
        });
    }
    // speichern oder erstellen neues Projekt
    @FXML
    private void createOrUpdateProjects() {
        if (isUpdateMode) {
            saveUpdatedProject();
        } else {
            createNewProject();
        }
    }

    // Update project details
    @FXML
    private void updateProject() {
        Project selectedProject = getSelectedProject();
        if (selectedProject == null) {
            showProjectSelectionError();
            return;
        }

        // Enable update mode
        isUpdateMode = true;
        createNewProjectButton.setText("Update Project"); // Change the button text
        System.out.print("Update button");
        updateAbbrechenBtn.setVisible(true);

        // Populate fields with selected project details
        textFieldProjektName.setText(selectedProject.getName());
        budgetTextField.setText(String.valueOf(selectedProject.getBudget()));
        teilnehmerTextField.setText(String.valueOf(selectedProject.getSharedPersons()));
        startDatePicker.setValue(selectedProject.getStartDate());
        endDatePicker.setValue(selectedProject.getEndDate());

        // Listen for the button click to confirm update
        createNewProjectButton.setOnAction(event -> {
            try {
                Project updatedProject = getProjectInformation();
                if (updatedProject == null) return;

                // Update project details in the database

                NotificationHelper.showFailedNotification("Zeile 361 PC", String.valueOf(updatedProject.getSharedPersons()));
                ProjectRepository.updateProjectInDatabase(
                        selectedProject.getId(),
                        updatedProject.getName(),
                        updatedProject.getBudget(),
                        updatedProject.getSharedPersons(),
                        updatedProject.getStartDate(),
                        updatedProject.getEndDate()
                );

                NotificationHelper.showSuccessNotification("Erfolg", "Das Projekt wurde erfolgreich aktualisiert.");
                textFelderLoeschen(); // Clear text fields
                getAlleProjekte(); // Refresh project list
                isUpdateMode = false; // Reset the flag
                createNewProjectButton.setText("Create Project"); // Reset the button text
            } catch (NumberFormatException e) {
                NotificationHelper.showFailedNotification("Fehler", "Bitte geben Sie gültige Zahlenwerte für Budget und Teilnehmeranzahl ein.");
            } catch (Exception e) {
                NotificationHelper.showFailedNotification("Fehler", "Ein unerwarteter Fehler ist aufgetreten.");
                e.printStackTrace();
            }
        });
    }

    // Save Update
    @FXML
    private void saveUpdatedProject() {
        try {
            Project selectedProject = getSelectedProject(); // Get the selected project
            if (selectedProject == null) return;
            selectedProject.setName(textFieldProjektName.getText());
            selectedProject.setBudget(Double.parseDouble(budgetTextField.getText()));
            selectedProject.setSharedPersons(Integer.parseInt(teilnehmerTextField.getText()));
            selectedProject.setStartDate(startDatePicker.getValue());
            selectedProject.setEndDate(endDatePicker.getValue());

            ProjectRepository.updateProjectInDatabase(selectedProject);

            NotificationHelper.showSuccessNotification("Erfolgreich", "Das Projekt wurde aktualisiert.");
            projectTableView.refresh();
            textFelderLoeschen();
            getAlleProjekte();
            isUpdateMode = false; // Reset the flag
            createNewProjectButton.setText("Create Project"); // Reset the button text
        } catch (NumberFormatException e) {
            NotificationHelper.showFailedNotification("Fehler", "Bitte geben Sie gültige numerische Werte ein.");
        } catch (SQLException e) {
            NotificationHelper.showFailedNotification("Fehler", "Aktualisieren des Projekts in der Datenbank fehlgeschlagen.");
        } catch (Exception e) {
            NotificationHelper.showFailedNotification("Fehler", "Ein unerwarteter Fehler ist aufgetreten.");
            e.printStackTrace();
        }
    }

    @FXML
    public void userPaid(ActionEvent event) {
        try {
            if (loggedInUser != null) {
                loggedInUser.makePayment(loggedInUser.getTotalAmountToPay());
                currentProject.userPaid(loggedInUser.getUsername());
                double monthlyContribution = loggedInUser.getTotalAmountToPay() / currentProject.getDuration();
                String message = String.format(
                        "User %s has paid their amount.\n\nProject Details:\nName: %s\nBudget: %.2f\nDuration: %d months\nMonthly Payment: %.2f\nAmount Paid: %.2f\nRemaining Amount: %.2f",
                        loggedInUser.getUsername(), currentProject.getName(), currentProject.getBudget(),
                        currentProject.getDuration(), monthlyContribution, loggedInUser.getAmountPaid(), loggedInUser.getRemainingAmount()
                );
                // Update the TableView
                projectTableView.refresh();
                getAlleProjekte();
                NotificationHelper.showSuccessNotification("Payment Received", message);
            } else {
                NotificationHelper.showFailedNotification("Error", "No user is currently logged in.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAbbrechen(ActionEvent actionEvent) {
        NotificationHelper.showSuccessNotification("Hey", "I am ready to cancel the update ,but code missing!!");
    }

    // Method to count projects in each status category
    private int countProjectsByStatus(ProjectStatus status) {
        int count = 0;
        for (Project project : projectList) {

            if (project.getStatus() == status) {
                count++;
            }
        }
        return count;
    }
//
private int zahlDerProjekte(){
    int projekte=0;
    for (Project project : projectList) {
        projekte++;
    }
    return projekte;
    }
      // Function to count and display the number of projects with specific statuses
    private void initStatusCounts() {
        int wartendCount = countProjectsByStatus(ProjectStatus.WARTEND);
        int beendetCount = countProjectsByStatus(ProjectStatus.BEENDET);
        int laufendCount = countProjectsByStatus(ProjectStatus.LAUFEND);
        projekteZahler.setText(new StringBuilder().append("Projekte:").append("").append(zahlDerProjekte()).toString());
        wartendeProjektZahl1.setText("Wartend: " + wartendCount);
        beendetProjektZahl11.setText("Beendet: " + beendetCount);
        laufendProjektZahl1.setText("Laufend: " + laufendCount);
    }


}

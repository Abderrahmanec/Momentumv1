package com.task;

import com.database.DatabaseManager;

import com.notifcationPackage.NotificationHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class TaskController implements Initializable {

    public ChoiceBox pirority;
    @FXML
    private TextField title;
    @FXML
    private DatePicker dateStart;
    @FXML
    private TextArea description;
    @FXML
    private ChoiceBox<String> priority;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private ComboBox<Integer> hourComboBox;
    @FXML
    private ComboBox<Integer> minuteComboBox;
    @FXML
    private ComboBox<String> ampmComboBox;
    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Integer> colId;
    @FXML
    private TableColumn<Task, String> colTitle;
    @FXML
    private TableColumn<Task, String> colDescription;

    private ObservableList<Task> taskList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskList = FXCollections.observableArrayList();
        taskTable.setItems(taskList);

        hourComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        minuteComboBox.getItems().addAll(0, 15, 30, 45);
        ampmComboBox.getItems().addAll("AM", "PM");
       // priority.getItems().addAll("Low", "Medium", "High");
        // Optionally set a default value
        //priority.setValue("Medium");

        // Add listener to handle selection changes
       // priority.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

       // showTasks();
    }

    @FXML
    public void addNewTask() {
        Task newTask = createTaskFromInput();

            saveTaskToDatabase(newTask);
            taskList.add(newTask);

    }

    private Task createTaskFromInput() {
        String taskTitle = title.getText();
        String taskDescription = description.getText();
        LocalDate startDate = dateStart.getValue();
        LocalDate endDate = dateEnd.getValue();
        Integer hour = hourComboBox.getValue();
        Integer minute = minuteComboBox.getValue();
        String ampm = ampmComboBox.getValue();

        if (taskTitle != null ) {
            if (ampm.equals("PM") && hour != 12) {
                hour += 12;
            } else if (ampm.equals("AM") && hour == 12) {
                hour = 0;
            }
            LocalTime time = LocalTime.of(hour, minute);
            LocalDateTime dateTime = LocalDateTime.of(startDate, time);

            Task task = new Task(taskTitle, taskDescription);
            // Assuming Task class has methods to set additional fields
            // task.setStartDateTime(dateTime);
            // task.setEndDate(endDate);
            return task;
        } else {
            showValidationError();
            return null;
        }
    }

    private void showValidationError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText("Please fill in all required fields correctly.");
        alert.showAndWait();
    }

    @FXML
    public void showTasks() {
       // taskList.setAll(getTasksFromDatabase());
       // getTasksFromDatabase();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    @FXML
    private ObservableList<Task> getTasksFromDatabase() {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        String query = "SELECT * FROM aufgabe"; // Specify the database name
        try (Connection con = TaskManager.getConnection();
             PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Task task = new Task();
                task.setId(resultSet.getInt("id"));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                // task.setStartDateTime(resultSet.getTimestamp("start").toLocalDateTime());
                // task.setEndDate(resultSet.getDate("end").toLocalDate());
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
        return tasks;
    }

@FXML
    private void saveTaskToDatabase(Task task) {
        String insert = "INSERT INTO aufgabe(title, description) VALUES(?, ?)";
        try (Connection con = TaskManager.getConnection();
             PreparedStatement statement = con.prepareStatement(insert)) {

            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
    }

    @FXML
    public void showProjectCount() {
        System.out.println("Number of tasks: " + taskList.size());
    }

    @FXML
    public void notif() {
        NotificationHelper.notification("warning", "Hey", "Hello, I am working!!");
    }



}

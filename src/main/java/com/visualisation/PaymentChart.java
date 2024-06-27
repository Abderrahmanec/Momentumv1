package com.visualisation;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PaymentChart extends Application {

    @Override
    public void start(Stage stage) {
        // Set up the axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");

        // Set up the bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Project Payments");

        // Set up the data series for payments
        XYChart.Series<String, Number> paymentSeries = new XYChart.Series<>();
        paymentSeries.setName("Payments");

        // Set up the data series for the budget target
        XYChart.Series<String, Number> budgetSeries = new XYChart.Series<>();
        budgetSeries.setName("Budget Target");

        // Retrieve data from the database and add it to the series
        String url = "jdbc:mysql://localhost:3306/project_management";
        String user = "root";
        String password = ""; // replace with your MySQL password

        double budget = 0.0;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Fetch the budget from the database
            ResultSet budgetRs = stmt.executeQuery("SELECT budget FROM projects WHERE id = 1"); // Replace with appropriate project ID or condition
            if (budgetRs.next()) {
                budget = budgetRs.getDouble("budget");
            }

            // Fetch the payments from the database
            ResultSet paymentRs = stmt.executeQuery("SELECT payment_date, payment_amount FROM project_payments");
            Map<LocalDate, Double> paymentData = new HashMap<>();

            while (paymentRs.next()) {
                LocalDate date = paymentRs.getTimestamp("payment_date").toLocalDateTime().toLocalDate();
                double amount = paymentRs.getDouble("payment_amount");

                System.out.println("Date: " + date + " Amount: " + amount); // Debug statement

                paymentData.merge(date, amount, Double::sum);
            }

            for (Map.Entry<LocalDate, Double> entry : paymentData.entrySet()) {
                System.out.println("Aggregated Date: " + entry.getKey() + " Total Amount: " + entry.getValue()); // Debug statement
                paymentSeries.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
                budgetSeries.getData().add(new XYChart.Data<>(entry.getKey().toString(), budget));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add the series to the chart
        barChart.getData().addAll(paymentSeries, budgetSeries);

        // Set up the scene and stage
        Scene scene = new Scene(barChart, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Project Payments Chart");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void addNewTask(ActionEvent event) {
    }
}

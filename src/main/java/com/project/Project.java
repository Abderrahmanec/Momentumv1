package com.project;

import com.notifcationPackage.NotificationHelper;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class Project {

    private long duration;

    // inner class
    public enum ProjectStatus {
        LAUFEND, BEENDET, WARTEND
    }


    private int id;
    private String name;
    private double budget;
    private LocalDate startDate;
    private LocalDate endDate;
    private int sharedPersons;
    private Set<String> paidUsers;
    private double totalPaid;
    private ProjectStatus status;
    private double amountPaid;

    // Constructors
    public Project() {
        this.paidUsers = new HashSet<>();
        this.status = ProjectStatus.WARTEND;
    }

    public Project(String name, double budget, int sharedPersons, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.budget = budget;
        this.sharedPersons = sharedPersons;
        setDates(startDate, endDate);
        this.paidUsers = new HashSet<>();
        this.totalPaid = 0;
        validateAndUpdateStatus();
    }

    public Project(int id, String name, double budget, int sharedPersons, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.sharedPersons = sharedPersons;
        setDates(startDate, endDate);
        this.paidUsers = new HashSet<>();
        this.totalPaid = 0;
        validateAndUpdateStatus();
    }

    public Project(int id, String name, double budget, int sharedPersons, LocalDate startDate, LocalDate endDate, ProjectStatus status) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.sharedPersons = sharedPersons;
        setDates(startDate, endDate);
        this.paidUsers = new HashSet<>();
        this.totalPaid = 0;
        this.status = status;
        validateAndUpdateStatus();
    }

    public Project(int id, String name, double budget, int sharedPersons, LocalDate startDate, LocalDate endDate, ProjectStatus status, double amountPaid) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.sharedPersons = sharedPersons;
        setDates(startDate, endDate);
        this.paidUsers = new HashSet<>();
        this.totalPaid = 0;
        this.status = status;
        this.amountPaid = amountPaid;
        validateAndUpdateStatus();
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getBudget() { return budget; }
    public int getSharedPersons() { return sharedPersons; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }



//    protected void validaDate() {
//        validateAndUpdateStatus();
//    }

    protected void setId(int id) {
        this.id = id;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setBudget(double budget) {
        this.budget = budget;
    }


    protected void setSharedPersons(int sharedPersons) {
        this.sharedPersons = sharedPersons;
    }
    protected void setStartDate(LocalDate startDate) {
        setDates(startDate, this.endDate);
        validateAndUpdateStatus(); // Validate status whenever start date is set
    }

    protected void setEndDate(LocalDate endDate) {
        setDates(this.startDate, endDate);
    }

    protected void setDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && !startDate.isBefore(endDate)) {
            NotificationHelper.showFailedNotification("Eroor", "Start Datum muss before end Datum sein");
            throw new IllegalArgumentException("Start Datum des Projekt muss bevor End Datum des Projektes sein");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        if (startDate != null && endDate != null) {
            this.duration = ChronoUnit.DAYS.between(startDate, endDate);
        }
        validateAndUpdateStatus(); // Validate status whenever dates are set
    }


    // Getters and setters for amountPaid
    public Double getAmountPaid() {
        return this.amountPaid;
    }

    public long getDuration() {
        return getProjectDurationInDays();
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public Set<String> getPaidUsers() {
        return paidUsers;
    }

    // Methods

    public int daysInMonth(long days) {
        return (int) Math.min(days, 30);
    }

    public double getMonatlicheZahlungenAPersone() {
        long durationDays = getProjectDurationInDays();
        if (durationDays == 0) {
            NotificationHelper.showFailedNotification("Fehler bei Eingaben", "Prüfen Sie Ihre Eingabe!");
            return 0;
        }

        double totalMonths = Math.ceil(durationDays / 30.0);
        double monthlyPayment = budget / totalMonths / sharedPersons;

        // Build the notification message
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Project Details:\n");
        messageBuilder.append("Projekte Name: ").append(name).append("\n");
        messageBuilder.append("Budget: ").append(budget).append("\n");
        messageBuilder.append("Startdatum: ").append(startDate).append("\n");
        messageBuilder.append("Enddatum: ").append(endDate).append("\n");
        messageBuilder.append("Gemeinsame Personen: ").append(sharedPersons).append("\n");
        messageBuilder.append("Gesamtdauer (in Monaten): ").append((int) totalMonths).append("\n");
        messageBuilder.append("Monatliche Zahlungen pro Person:\n");

        for (int month = 1; month <= totalMonths; month++) {
            messageBuilder.append("Monat ").append(month).append(": ").append(monthlyPayment).append(" pro Teinehmer\n");
        }
        // Show the combined notification message
        NotificationHelper.notification("INFORMATION", "Project Information", messageBuilder.toString());
        return monthlyPayment;
    }

    public long getProjectDurationInDays() {
        if (startDate != null && endDate != null) {
            return ChronoUnit.DAYS.between(startDate, endDate);
        }
        return 0; // Return 0 if either date is null
    }

    public double calculatePercentagePaid() {
        if (budget == 0) {
            return 0; // Avoid division by zero
        }
        return Math.round((amountPaid / budget) * 100);
    }

    public void getRemainingAmount() {
        double d = budget - amountPaid;
        System.out.println("Es verbleibt noch :" + d + " €");
    }

    public void userPaid(String userName) {
        if (!paidUsers.contains(userName)) {
            paidUsers.add(userName);
            double monthlyPayment = getMonatlicheZahlungenAPersone();
            totalPaid += monthlyPayment;

            // Update the database with the new total paid amount
            try {
                ProjectRepository.updateTotalPaidForProject(totalPaid, id);
            } catch (SQLException e) {
                // Handle database update error
                e.printStackTrace();
            }

            System.out.println(MessageFormat.format("User {0} has paid their amount.", userName));
        } else {
            System.out.println(MessageFormat.format("User {0} has already paid.", userName));
        }
    }


    @Override
    public  String toString(){
        return "Project{id=" + id + ", name='" + name + "', budget=" + budget + ", duration=" + duration + ", startDate=" + startDate + ", endDate=" + endDate + ", sharedPersons=" + sharedPersons + ", status=" + status + "}";
    }

    private void validateAndUpdateStatus() {
        LocalDate currentDate = LocalDate.now();
        if (startDate != null && startDate.equals(currentDate)) {
            status = ProjectStatus.LAUFEND; // Project ist laufend
        } else if (startDate != null && startDate.isAfter(currentDate)) {
            status = ProjectStatus.WARTEND; // Project is warten bis Eintreten des Start-datums
        }
    }


    // Method to get status as a string
    public  String getStatusAsString() {
        switch (status) {
            case WARTEND:
                return "Warten";
            case LAUFEND:
                return "Laufen";
            case BEENDET:
                return "Beendet";
            default:
                return "Unknown";
        }
    }

}
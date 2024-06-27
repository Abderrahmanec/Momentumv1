package com.project;



import com.notifcationPackage.NotificationHelper;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectManger {

    /*/
    @param name für name des Projekts
     */
    public static void createProject(String name, double budget, int sharedPersons,LocalDate startDate , LocalDate endDate) {
        try {
            //String name, double budget, int sharedPersons,LocalDate startDate, LocalDate endDate
            ProjectRepository.addProjectToDatabase( name,  budget,  sharedPersons, startDate ,  endDate);
            NotificationHelper.showSuccessNotification("Erfolg", "Glückwünsch! Sie haben Ihr Projekt erstellt.");
        } catch (SQLException e) {
            NotificationHelper.showFailedNotification("Fehler","Das Projekt könnte nicht gespeichert werden");
            System.out.println("Check if the local db is starting: " + e.getMessage());
        }
    }

    /*/
    @param projectId für ProjektId des Projekts
     */
    public static void deleteProject(int projectId) {
        try {
            ProjectRepository.projektVonDatenbankLoeschen(projectId);
            NotificationHelper.showSuccessNotification("Erfolgreich", "Das Projekt wurde gelöscht");
        } catch (SQLException e) {
            // Handle exceptoion in sql
            NotificationHelper.showFailedNotification("Fehler", "Projekt kann nicht gelöscht werden, bitte prüfen Sie ihre Verbindung.");
        }
    }


    public static void updateProject(Project project) throws SQLException {
        System.out.print( project.getName()+ project.getBudget()+ project.getSharedPersons()+project.getEndDate());
            ProjectRepository.updateProjectInDatabase( project.getId(),
                    project.getName(),
                    project.getBudget(),
                    project.getSharedPersons(),
                    project.getStartDate(),
                    project.getEndDate());
            System.out.println("Project '" + project.getName() + "' updated successfully.");

    }


    public static void percent(double amount, double budget) {
        double percentage = (amount / budget) * 100;
        System.out.println("Sie haben :" + percentage + "% bezahlt");
    }

    public static void createUser(){
        Project p=new Project();
        int u=p.getSharedPersons();
        System.out.print("Kontos"+u);
    }


    // Method to order projects by status
    public static void orderProjectsByStatus(List<Project> projectList) {
        List<Project> projectsWarten = new ArrayList<>();
        List<Project> projectsLaufen = new ArrayList<>();
        List<Project> projectsBeendet = new ArrayList<>();

        for (Project project : projectList) {
            switch (project.getStatus()) {
                case WARTEND:
                    projectsWarten.add(project);
                    break;
                case LAUFEND:
                    projectsLaufen.add(project);
                    break;
                case BEENDET:
                    projectsBeendet.add(project);
                    break;
            }
        }

        // Clear the original list and add projects in the desired order
        projectList.clear();
        projectList.addAll(projectsWarten);
        projectList.addAll(projectsLaufen);
        projectList.addAll(projectsBeendet);
    }
}

package com.task;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Task {

    private String title;
    private String description;
    private LocalDate start;
    private LocalDate end;
    public static ArrayList<Task>tasks;
    public static int numberOfTasks =0;
    LocalDateTime currentDateTime = LocalDateTime.now();
    private  int id;
    public Task(){
   }

public Task(String title,String description ){
    this.title=title;
    this.description=description;
    this.tasks=new ArrayList<>();
    numberOfTasks++;
  }
    // Getters and Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }



    public static int getNumberOfTasks() {
        return numberOfTasks;
    }


    public LocalDate getStartDate() {
        return start;
    }


    public void addNewTask(Task task){
        this.title=task.getTitle();
        this.description=description;
    }

    public void showTasks(){
        for(int i=0;i<tasks.size();i++){
            System.out.println(tasks.get(i).getTitle()+tasks.get(i).getDescription());
        }
    }


    public void setId(int id) {
        this.id=id;
    }
}

//er hat 2 in IT ProjektMangment bekommen+
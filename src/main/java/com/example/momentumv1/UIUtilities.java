package com.example.momentumv1;


import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class UIUtilities {
    public UIUtilities() {
        //Is Empty
    }

    public static void changeBackgroundToDarkMode(Pane paneBackgroundColor, StackPane stackPaneBackgroundColor) {

        paneBackgroundColor.getStyleClass().add("background-black"); // Add the specified style class
        stackPaneBackgroundColor.getStyleClass().add("background-black");
    }



    public static void  changeBackgroundToWhite(Pane paneBackgroundColor, StackPane stackPaneBackgroundColor){
            stackPaneBackgroundColor.setBackground(Background.fill(Color.WHITE));
            paneBackgroundColor.setBackground(Background.fill(Color.WHITE));
    }

    public static final void labelsColorToWhite(Label label, Label name, Label pass){
        String style="-fx-text-fill: #fbfbff";
        label.setStyle(style);
        pass.setStyle(style);
        name.setStyle(style);
    }

    public static final void labelsColorToBlack(Label label, Label name, Label pass){
        String style="-fx-text-fill: #362576";
        label.setStyle(style);
        pass.setStyle(style);
        name.setStyle(style);
    }

    public static void setBorder(Label label) {
        String style = "solid";
        String color = "blue";
        String size = "1px";
        String completeStyle = "-fx-border-width: " + size + "; -fx-border-style: " + style + "; -fx-border-color: " + color + ";";
        label.setStyle(completeStyle);
            }



}


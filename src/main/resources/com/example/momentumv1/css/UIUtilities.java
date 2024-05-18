package com.example.momentumv1.css;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
public class UIUtilities {
    public static final Color DARK_COLOR = Color.web("#171a1e");
    public static final Color WHITE_COLOR = Color.WHITE;

    public static void setBackground(Pane paneBackgroundColor, StackPane stackPaneBackgroundColor, boolean isDarkMode) {
        Color backgroundColor = isDarkMode ? DARK_COLOR : WHITE_COLOR;
        paneBackgroundColor.setBackground(Background.fill(backgroundColor));
        stackPaneBackgroundColor.setBackground(Background.fill(backgroundColor));

    }



    public static void labelsColorToWhite(Label label, Label name, Label pass ,boolean useWhiteText) {
        Color textColor = useWhiteText ? Color.WHITE : Color.BLACK;
        label.setTextFill(textColor);
        name.setTextFill(textColor);
        pass.setTextFill(textColor);

    }

    public static void labelsColorToBlack(Label label, Label name, Label pass, boolean useWritetext) {
        Color textColor = useWritetext ? Color.WHITE : Color.BLACK;
        label.setTextFill(textColor);
        name.setTextFill(textColor);
        pass.setTextFill(textColor);
    }

    public static void setBorder(Label label) {
        String style = "solid";
        String color = "blue";
        String size = "1px";
        String completeStyle = "-fx-border-width: " + size + "; -fx-border-style: " + style + "; -fx-border-color: " + color + ";";
        label.setStyle(completeStyle);
    }



}
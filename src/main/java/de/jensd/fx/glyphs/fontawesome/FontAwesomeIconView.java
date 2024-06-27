package de.jensd.fx.glyphs.fontawesome;

import javafx.scene.text.Font;

public class FontAwesomeIconView extends javafx.scene.text.Text {

    private static final String FONT_PATH = "file:///C:/Users/Public/Studium/S2/Programmieren%202/Documentation/Logo/Icons/FontAwesome.ttf";
    private static final double DEFAULT_FONT_SIZE = 10.0;

    public FontAwesomeIconView() {
        loadFontAwesomeFont();
        setIconFont();
    }

    /**
     * Loads the FontAwesome font.
     */
    private static void loadFontAwesomeFont() {
        try {
            Font.loadFont(FONT_PATH, DEFAULT_FONT_SIZE);
        } catch (Exception e) {
          //  System.err.println(STR."Error loading FontAwesome font: \{e.getMessage()}");
            System.out.print("Errr");
            // Handle the error appropriately
        }
    }

    /**
     * Sets the FontAwesome font family.
     */
    private void setIconFont() {
        this.setFont(Font.font("FontAwesome"));
    }

    // Additional methods for customizing the icon view, such as size, color, etc.

}

package de.sbg.unity.smallteleporter;

public class TextFormat {
    
    public String Color(String color, String text) {
        return "<color=" + color + ">" + text + "</color>";
    }
    
    public String Lowercase(String text) {
        return "<lowercase>" + text + "</lowercase>";
    }
    
    public String Uppercase(String text) {
        return "<allcaps>" + text + "</allcaps>";
    }
    
    public String Bold(String text) {
        return "<b>" + text + "</b>";
    }
    
    public String Italics(String text) {
        return "<i>" + text + "</i>";
    }
    
    public String Underline(String text) {
        return "<u>" + text + "</u>";
    }
    
    public String Superscript(String text) {
        return "<sup>" + text + "</sup>";
    }
    
    public String Subscript(String text) {
        return "<sub>" + text + "</sub>";
    }
    
    public String Sprites(int sprite, String text) {
        return "<sprite=" + sprite  + ">" + text + "</sprite>";
    }
    
    public String Size(String size, String text) {
        return "<size=" + size  + ">" + text + "</size>";
    }
    
}

package main.utils;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/**
 * The LabelManager will be a class which will store all of the labels,
 * create an enum with the type of the Label (e.g. TITLE, TEXT, BUTTON, etc.)
 * and a method to create a label with a specific type.
 * 
 * Each label should be stored to a map with the type as the key and the label as the value.
 */
public class LabelManager {

    private List<Label> labels;

    public LabelManager() {
        this.labels = new ArrayList<>();
    }

    public void addLabel(LabelType type, String text, int x, int y, int width, int height, boolean visible) {
        addLabel(new Label(type, text, x, y, width, height, visible));
    }

    public void addLabel(LabelType type, String text, int x, int y) {
        int calculatedWidth = new Font("Arial", Font.PLAIN, 12).getSize() * text.length();
        int calculatedHeight = new Font("Arial", Font.PLAIN, 12).getSize();
        addLabel(new Label(type, text, x, y, calculatedWidth, calculatedHeight, true));
    }

    public void addLabel(LabelType type, String text, int x, int y, int width, int height) {
        addLabel(new Label(type, text, x, y, width, height, true));
    }

    public void addLabel(LabelType type, String text, int x, int y, boolean visible) {
        int calculatedWidth = new Font("Arial", Font.PLAIN, 12).getSize() * text.length();
        int calculatedHeight = new Font("Arial", Font.PLAIN, 12).getSize();
        addLabel(new Label(type, text, x, y, calculatedWidth, calculatedHeight, visible));
    }

    public void addLabel(Label label) {
        labels.add(label);
    }

    public void drawLabels(JPanel panel, boolean useGraphics) {
        for (Label label : labels) {
            label.drawLabel(panel, useGraphics);
        }
    }

    public void drawLabels(JPanel panel) {
        drawLabels(panel, false);
    }
    
    public class Label {
        private LabelType type;
        private String text;
        private int x;
        private int y;
        private int width;
        private int height;
        private boolean visible;
        
        public Label(LabelType type, String text, int x, int y, int width, int height, boolean visible) {
            this.type = type;
            this.text = text;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.visible = visible;
        }
        
        public LabelType getType() {
            return type;
        }
        
        public String getText() {
            return text;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        public int getWidth() {
            return width;
        }
        
        public int getHeight() {
            return height;
        }
        
        public boolean isVisible() {
            return visible;
        }

        // public void drawLabel(JPanel panel) {
        //     JLabel label = new JLabel(text);
        //     label.setBounds(x, y, width, height);
        //     label.setVisible(visible);
        //     panel.add(label);
        //     System.out.println("Label (#" + label.hashCode() + ") drawn at (" + x + ", " + y + ") with text: " + text);
        // }
        public void drawLabel(JPanel panel, boolean useGraphics) {
            if (useGraphics) {
                Graphics g = panel.getGraphics();
                if (g == null) return;
                if (type == LabelType.TITLE)
                    g.setFont(new Font("Arial", Font.BOLD, 16));
                else if (type == LabelType.BUTTON)
                    g.setFont(new Font("Arial", Font.PLAIN, 12));
                else
                    g.setFont(new Font("Arial", Font.PLAIN, 12));
                if (visible) {
                    g.drawString(text, x, y);
                    System.out.println("Label drawn at (" + x + ", " + y + ") with text: " + text);
                }
            } else {
                JLabel label = new JLabel(text);
                label.setBounds(x, y, width, height);
                label.setVisible(visible);
                panel.add(label);
                System.out.println("Label (#" + label.hashCode() + ") drawn at (" + x + ", " + y + ") with text: " + text);
            }
        }
    }

    public enum LabelType {
        TITLE, TEXT, BUTTON
    }

}

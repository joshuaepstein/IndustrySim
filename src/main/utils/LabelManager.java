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

    private List<Label> cacheRendered = new ArrayList<>();
    private List<Label> labels;

    public LabelManager() {
        this.labels = new ArrayList<>();
    }

    public void addLabel(LabelType type, String text, int x, int y, int width, int height, boolean visible) {
        addLabel(new Label(type, text, x, y, width, height, visible));
    }

    public Label addLabel(LabelType type, String text, int x, int y) {
        int calculatedWidth = new Font("Arial", Font.PLAIN, 12).getSize() * text.length();
        int calculatedHeight = new Font("Arial", Font.PLAIN, 12).getSize();
        return addLabel(new Label(type, text, x, y, calculatedWidth, calculatedHeight, true));
    }

    public Label addLabel(LabelType type, String text, int x, int y, int width, int height) {
        return addLabel(new Label(type, text, x, y, width, height, true));
    }

    public Label addLabel(LabelType type, String text, int x, int y, boolean visible) {
        int calculatedWidth = new Font("Arial", Font.PLAIN, 12).getSize() * text.length();
        int calculatedHeight = new Font("Arial", Font.PLAIN, 12).getSize();
        return addLabel(new Label(type, text, x, y, calculatedWidth, calculatedHeight, visible));
    }

    public Label addLabel(Label label) {
        labels.add(label);
        return label;
    }

    public void drawLabels(JPanel panel) {
        for (Label label : labels) {
            label.drawLabel(panel);
        }
    }

    public List<Label> getLabels() {
        return labels;
    }
    
    public class Label {
        private JLabel label;
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

        public void drawLabel(JPanel panel) {
            if (cacheRendered.contains(this)) return;
            this.label = new JLabel(text);
            label.setBounds(x, y, width, height);
            label.setVisible(visible);
            panel.add(label);
            label.repaint();
            
            System.out.println("Label (#" + label.hashCode() + ") drawn at (" + x + ", " + y + ") with text: " + text);
            cacheRendered.add(this);
        }

        public void remove(JPanel panel) {
            // remove the text from being displayed
            // remove the label from the cache
            if (cacheRendered.contains(this)) {
                cacheRendered.remove(this);
            }
            panel.getGraphics().clearRect(x, y, width, height);
            
            if (panel.getComponents().length > 0) {
                for (int i = 0; i < panel.getComponents().length; i++) {
                    if (panel.getComponents()[i] instanceof JLabel) {
                        JLabel label = (JLabel) panel.getComponents()[i];
                        if (label.getText().equals(text)) {
                            panel.remove(label);
                        }
                    }
                }
            }
        }

        public void repaint() {
            this.label.setText(text);
            this.label.setBounds(x, y, width, height);
            this.label.repaint();
        }

        public void setText(String string) {
            this.text = string;
            repaint();
        }
    }

    public enum LabelType {
        TITLE, TEXT, BUTTON
    }

}

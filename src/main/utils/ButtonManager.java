package main.utils;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.*;

public class ButtonManager {

    private List<Button> cacheRendered = new ArrayList<>();
    private List<Button> buttons;

    public ButtonManager() {
        this.buttons = new ArrayList<>();
    }

    public void addButton(String text, int x, int y, int width, int height, boolean visible, Runnable onClick) {
        addButton(new Button(text, x, y, width, height, visible, onClick));
    }

    public Button addButton(String text, int x, int y, Runnable onClick) {
        int calculatedWidth = new Font("Arial", Font.PLAIN, 12).getSize() * text.length();
        int calculatedHeight = new Font("Arial", Font.PLAIN, 12).getSize();
        System.out.println("Button width: " + calculatedWidth + ", height: " + calculatedHeight);
        return addButton(new Button(text, x, y, calculatedWidth, calculatedHeight, true, onClick));
    }

    public Button addButton(String text, int x, int y, int width, int height, Runnable onClick) {
        return addButton(new Button(text, x, y, width, height, true, onClick));
    }

    public Button addButton(String text, int x, int y, boolean visible, Runnable onClick) {
        int calculatedWidth = new Font("Arial", Font.PLAIN, 12).getSize() * text.length();
        int calculatedHeight = new Font("Arial", Font.PLAIN, 12).getSize();
        return addButton(new Button(text, x, y, calculatedWidth, calculatedHeight, visible, onClick));
    }

    public Button addButton(Button label) {
        buttons.add(label);
        return label;
    }

    public void drawButtons(JPanel panel) {
        for (Button label : getButtons()) {
            label.drawLabel(panel);
        }
    }

    public List<Button> getButtons() {
        return buttons;
    }
    
    public class Button {
        private String text;
        private int x;
        private int y;
        private int width;
        private int height;
        private boolean visible;
        private Runnable onClick;
        private JButton button = null;
        
        public Button(String text, int x, int y, int width, int height, boolean visible, Runnable onClick) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.visible = visible;
            this.onClick = onClick;
        }
        
        public void onClick() {
            onClick.run();
        }

        public Runnable getOnClick() {
            return onClick;
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
            this.button = new JButton(text);
            if (this.width != 0 && this.height != 0) {
                this.button.setBounds(x, y, width, height);
            }
            this.button.setVisible(visible);
            this.button.addActionListener(e -> onClick());
            panel.add(this.button);
            this.button.repaint();
            
            System.out.println("Button (#" + this.hashCode() + ") drawn at (" + x + ", " + y + ") with text: " + text);
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
                    if (panel.getComponents()[i] instanceof JButton) {
                        JButton label = (JButton) panel.getComponents()[i];
                        if (label.getText().equals(text)) {
                            panel.remove(label);
                        }
                    }
                }
            }
        }

        public void repaint() {
            this.button.setText(text);
            this.button.setBounds(x, y, width, height);
            this.button.repaint();
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

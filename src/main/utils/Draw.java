package main.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.App;
import main.factory.FactoryType;

public class Draw {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int[] PADDING = new int[] {10, 10, 10, 10};
    public static final int HEADING_HEIGHT = 100;
    public static final int SPACE_BETWEEN_FACTORIES = 50;
    private static final Map<FactoryType, JLabel> numberOfFactoriesLabels = new HashMap<>();

    public static JLabel drawFactory(App app, JPanel panel, FactoryType factoryType) {
		if (panel == null) {
            System.out.println("Panel is null");
            return null;
        };
        Graphics g = panel.getGraphics();
		if (g == null) {
            System.out.println("Panel graphics is null");
            return null;
        };
		if (!panel.isValid()){
            System.out.println("Panel is not valid");
			panel.validate();
        }
        int factoryTypeIndex = factoryType.ordinal();
        int imageWidth = factoryType.getWidth();
        int x = PADDING[0] + (factoryTypeIndex * imageWidth) + ((factoryTypeIndex + 1) * SPACE_BETWEEN_FACTORIES);
        int y = PADDING[1] + HEADING_HEIGHT;
        System.out.println("Drawing factory " + factoryType.getName() + " at " + x + ", " + y + " with width " + imageWidth);
        
        BufferedImage img = loadImage(factoryType.getImage());
        System.out.println("Loaded image " + factoryType.getImage() + " with width " + img.getWidth() + " and height " + img.getHeight());
                
        tint(img, factoryType.getColor());
        
		panel.getGraphics().drawImage(img, x, y, imageWidth, imageWidth, new ImageObserver() {
			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				return false;
			}
		});

        g.setColor(Color.GRAY);
        g.drawRect(x - 2, y - 2, imageWidth + 4, imageWidth + 4);

        JLabel label = new JLabel();
        int stringWidth = imageWidth;
        label.setBounds(x + (imageWidth / 2) - (stringWidth / 2) - 1, y + imageWidth + 5, stringWidth, g.getFontMetrics().getHeight() - 6);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setText(String.valueOf(app.getFactoryManager().getFactoriesOfType(factoryType).size()));
        panel.add(label);
        
        System.out.println("Drawing factory " + factoryType.getName());

        return label;
    }

    public static JLabel drawLabel(App app, JPanel panel, FactoryType factoryType, int x, int y, int width) {
        JLabel label = new JLabel();
        label.setBounds(x, y + width, width, 20);
        label.setText(String.valueOf(app.getFactoryManager().getFactoriesOfType(factoryType).size()));
        panel.add(label);
        return label;
    }

    public static void disposeFactories(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    public static void tint(BufferedImage image, Color color) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixelColor = new Color(image.getRGB(x, y), true);
                int r = (pixelColor.getRed() + color.getRed()) / 2;
                int g = (pixelColor.getGreen() + color.getGreen()) / 2;
                int b = (pixelColor.getBlue() + color.getBlue()) / 2;
                int a = pixelColor.getAlpha();
                int rgba = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, rgba);
            }
        }
    }

    public static BufferedImage loadImage(String url) {
        ImageIcon icon = new ImageIcon(url);
        Image image = icon.getImage();

        // Create empty BufferedImage, sized to Image
        BufferedImage buffImage = 
        new BufferedImage(
            image.getWidth(null), 
            image.getHeight(null), 
            BufferedImage.TYPE_INT_ARGB);

        // Draw Image into BufferedImage
        Graphics g = buffImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        return buffImage;
    }
}

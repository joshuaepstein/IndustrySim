package main;

import main.factory.FactoryType;
import main.factory.IFactory;
import main.utils.Counter;
import main.utils.Draw;
import main.utils.LabelManager;
import main.utils.SoundManager;
import main.utils.LabelManager.LabelType;
import main.utils.SoundManager.SoundType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	public static List<IFactory> factories = new ArrayList<>();
	private int gameLevel = 1;
	private final JFrame frame;
	private final JPanel rootPanel;
	private final Map<FactoryType, JLabel> resourcesLabels;
	private final Map<FactoryType, JButton> createFactoriesButtons;
	private final Map<String, Long> toasts = new HashMap<>();
	private final Map<FactoryType, Boolean> lockedResource = new HashMap<>() {{
		put(FactoryType.COAL, false);
	}};

	public static Map<FactoryType, Counter> money = new HashMap<>() {{
		put(FactoryType.COAL, new Counter(100));
		put(FactoryType.COPPER, new Counter(200));
	}};
	private long ticks = 0;
	private SoundManager soundManager;
	private LabelManager labelManager;
	// private ToastManager toastManager;

	public static void main(String[] args) {
		new App();
	}

	public App() {
		this.soundManager = new SoundManager("src/resources/sounds/");
		this.labelManager = new LabelManager();

		factories = new ArrayList<>();
		createFactoriesButtons = new HashMap<>();
		resourcesLabels = new HashMap<>();

		frame = new JFrame("Industry Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);

		rootPanel = new JPanel();
		rootPanel.setLayout(null);
		rootPanel.setBounds(0, 0, WIDTH, HEIGHT);
		frame.add(rootPanel);

		frame.setLayout(null);
		frame.setVisible(true);
		// showOnScreen(2, frame); // Temp solution while on PC so that the images do not disappear when moving screens!

		// MARK: Title Label
		this.labelManager.addLabel(LabelType.TITLE, "Industry Simulator", Draw.PADDING[0], Draw.PADDING[1], true);

		for (FactoryType factoryType : FactoryType.values()) {
			int[] returnableValues = Draw.drawFactory(rootPanel, factoryType);
			if (returnableValues == null) continue;
			// Draw.drawLabel(rootPanel, factoryType, returnableValues[0], returnableValues[1], returnableValues[2]);
			
		}
		
		this.labelManager.drawLabels(rootPanel, true);

		Timer timer = new Timer(100, e -> {
			ticks++;
			tickToasts();

			if (ticks % 10 == 0) {
				for (IFactory factory1 : factories) {
					if (!factory1.isOverworked()) {
						money.computeIfAbsent(factory1.getType(), (factoryType) -> new Counter(0)).add(factory1.getProductionPerTick());
					}
				}
			}

			if (money.get(FactoryType.COAL).getValue() >= 500 && gameLevel == 1) {
				gameLevel++;
				addToast("Level up! You are now level " + gameLevel);
				lockedResource.put(FactoryType.COPPER, false);
				this.soundManager.playSound(SoundType.LEVEL_UP);
			}
		});

		timer.setRepeats(true);
		timer.start();
	}

	private void addButtons() {
		int buttonsY = 70;
		for (FactoryType factoryType : FactoryType.values()) {
			JButton button = new JButton("Add " + factoryType.getName() + " (" + factoryType.getCost() + ")");
			button.setBounds(5, buttonsY+=30, 250, 25);
			button.addActionListener(e -> {
				try {
					IFactory factory = factoryType.getFactoryClass().getConstructor(int.class).newInstance(1);
					if (money.getOrDefault(factoryType, new Counter(0)).getValue() < factory.getCost()) {
						addToast("Not enough money to buy " + factoryType.getName() + " factory");
						return;
					} else {
						money.get(factoryType).subtract(factory.getCost());
					}
					// factory.paint(rootPanel, 10 + ((factories.size() + 1) * 16), 250);
					// Draw.drawFactory(rootPanel, factoryType);
					
					this.factories.add(factory);
					addToast("Added " + factoryType.getName() + " factory");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			createFactoriesButtons.put(factoryType, button);
			rootPanel.add(button);
		}
	}

	private void addToast(String text) {
		toasts.put(text, this.ticks);
	}

	private void tickToasts() {
		for (String toast : toasts.keySet()) {
			if (this.ticks - toasts.get(toast) >= 5000) {
				toasts.remove(toast);
				for (Component component : rootPanel.getComponents()) {
					if (component instanceof JLabel) {
						JLabel label = (JLabel) component;
						if (label.getText().equals(toast)) {
							rootPanel.remove(label);
							break;
						}
					}
				}
			} else {
				boolean foundRenderedToast = false;
				for (Component component : rootPanel.getComponents()) {
					if (component instanceof JLabel) {
						JLabel label = (JLabel) component;
						if (label.getText().equals(toast)) {
							foundRenderedToast = true;
							break;
						}
					}
				}
				if (!foundRenderedToast) {
					JLabel label = new JLabel(toast);
					int y = HEIGHT - (5 + (toasts.size() * 5) + 20);
					label.setBounds(5, y, 250, 20);
					rootPanel.add(label);
				}
			}
		}
	}

	public void unlockResource(FactoryType factoryType) {
		lockedResource.put(factoryType, false);
	}

	public static List<IFactory> getFactoriesOfType(FactoryType type) {
		List<IFactory> factories = new ArrayList<>();
		for (IFactory factory : App.factories) {
			if (factory.getType() == type) {
				factories.add(factory);
			}
		}
		return factories;
	}

	public void showOnScreen(int screen, JFrame frame ) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		int width = 0, height = 0;
		if( screen > -1 && screen < gd.length ) {
			width = gd[screen].getDefaultConfiguration().getBounds().width;
			height = gd[screen].getDefaultConfiguration().getBounds().height;
			frame.setLocation(
				((width / 2) - (frame.getSize().width / 2)) + gd[screen].getDefaultConfiguration().getBounds().x, 
				((height / 2) - (frame.getSize().height / 2)) + gd[screen].getDefaultConfiguration().getBounds().y
			);
			frame.setVisible(true);
		} else {
			throw new RuntimeException( "No Screens Found" );
		}
	}
}
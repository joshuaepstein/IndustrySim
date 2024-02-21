package main;

import main.factory.CoalFactory;
import main.factory.FactoryType;
import main.factory.IFactory;
import main.utils.Counter;
import main.utils.Draw;
import main.utils.FactoryManager;
import main.utils.LabelManager;
import main.utils.SoundManager;
import main.utils.UnlockHelper;
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

	private final JFrame frame;
	private final JPanel rootPanel;

	public static Map<FactoryType, Counter> money = new HashMap<>() {{
		put(FactoryType.COAL, new Counter(100));
		put(FactoryType.COPPER, new Counter(200));
	}};

	public Map<FactoryType, JLabel> factoryCountLabels = new HashMap<>();
	
	private static SoundManager soundManager = new SoundManager("src/resources/sounds/");
	private LabelManager labelManager;
	private UnlockHelper unlockHelper;
	private FactoryManager factoryManager;
	// private ToastManager toastManager;

	private Counter ticks = new Counter(0);
	private int gameLevel = 1;

	public static void main(String[] args) {
		new App();
	}

	public App() {
		this.labelManager = new LabelManager();
		this.unlockHelper = new UnlockHelper();
		this.factoryManager = new FactoryManager();

		frame = new JFrame("Industry Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT + 50);

		rootPanel = new JPanel();
		rootPanel.setLayout(null);
		rootPanel.setBounds(0, 0, WIDTH, HEIGHT);
		frame.add(rootPanel);

		frame.setLayout(null);
		frame.setVisible(true);
		// showOnScreen(2, frame); // Temp solution while on PC so that the images do not disappear when moving screens!

		JLabel titleLabel = new JLabel("Industry Simulator");
		titleLabel.setBounds(0, 0, WIDTH, 100);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		rootPanel.add(titleLabel);

		for (FactoryType factoryType : FactoryType.values()) {
			JLabel amountLabel = Draw.drawFactory(this, rootPanel, factoryType);
			amountLabel.repaint();
			factoryCountLabels.put(factoryType, amountLabel);
		}

		String[] labels = new String[] {
			"Coal: " + money.get(FactoryType.COAL).getValue(),
			"Copper: " + money.get(FactoryType.COPPER).getValue()
		};
		main.utils.LabelManager.Label[] labelObjects = new main.utils.LabelManager.Label[labels.length];
		for (int i = 0; i < labels.length; i++) {
			String label = labels[i];
			labelObjects[i] = this.labelManager.addLabel(LabelType.TEXT, label, 10, HEIGHT - 50 + (i * 20));
		}
		factoryManager.addFactory(new CoalFactory());
		Timer timer = new Timer(100, e -> {
			ticks.increment();
			this.labelManager.drawLabels(rootPanel);

			if (ticks.getValue() % 10 == 0) {
				this.factoryManager.tick(gameLevel);
			}

			this.unlockHelper.checkAndUnlockResources(money, gameLevel, soundManager);
			this.factoryCountLabels.forEach((factoryType, label) -> label.setText(String.valueOf(this.factoryManager.getFactoriesOfType(factoryType).size())));
			for (LabelManager.Label label : labelObjects) {
				if (label.getText().startsWith("Coal: ")) {
					label.setText("Coal: " + money.get(FactoryType.COAL).getValue());
				}
			}
		});

		timer.setRepeats(true);
		timer.start();
	}

	/**
	 * Utility function for when on Home PC as it has 3 screens and the images disappear when moving screens
	 * @param screen Screen number to display on
	 * @param frame Frame to display
	 */
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

    public static SoundManager getSoundManager() {
		return soundManager;
    }

	public FactoryManager getFactoryManager() {
		return factoryManager;
	}
}
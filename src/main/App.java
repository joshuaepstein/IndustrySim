package main;

import main.factory.CoalFactory;
import main.factory.FactoryType;
import main.factory.IFactory;
import main.utils.Counter;
import main.utils.Draw;
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
	// private ToastManager toastManager;

	public static List<IFactory> factories = new ArrayList<>();
	private Counter ticks = new Counter(0);
	private int gameLevel = 1;

	public static void main(String[] args) {
		new App();
	}

	public App() {
		this.labelManager = new LabelManager();
		this.unlockHelper = new UnlockHelper();

		factories = new ArrayList<>();

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

		JLabel titleLabel = new JLabel("Industry Simulator");
		titleLabel.setBounds(0, 0, WIDTH, 100);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		rootPanel.add(titleLabel);

		for (FactoryType factoryType : FactoryType.values()) {
			JLabel amountLabel = Draw.drawFactory(rootPanel, factoryType);
			amountLabel.repaint();
			factoryCountLabels.put(factoryType, amountLabel);
		}

		addFactory(new CoalFactory());
		Timer timer = new Timer(100, e -> {
			ticks.increment();
			this.labelManager.drawLabels(rootPanel, false);

			if (ticks.getValue() % 10 == 0) {
				for (IFactory factory1 : factories) {
					if (!factory1.isOverworked()) {
						money.computeIfAbsent(factory1.getType(), (factoryType) -> new Counter(0)).add(factory1.getProductionPerTick());
					}
				}
			}

			// if (money.get(FactoryType.COAL).getValue() >= 500 && gameLevel == 1) {
			// 	gameLevel++;
			// 	addToast("Level up! You are now level " + gameLevel);
			// 	unlockedResources.put(FactoryType.COPPER, false);
			// 	this.soundManager.playSound(SoundType.LEVEL_UP);
			// }
			this.unlockHelper.checkAndUnlockResources(money, gameLevel, soundManager);
			this.factoryCountLabels.forEach((factoryType, label) -> label.setText(String.valueOf(App.getFactoriesOfType(factoryType).size())));
		});
		timer.setRepeats(true);
		timer.start();
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

	public void addFactory(IFactory factory) {
		factories.add(factory);
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
}
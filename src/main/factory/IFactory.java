package main.factory;

import java.awt.*;

public interface IFactory {

	int getCost();

	default int getProductionPerTick() {
		return getProductionPerTick(1);
	}

	int getProductionPerTick(int level);

	int getLevel();

	IFactory setLevel(int level);

	int getUpgradeCost(int level);

	int getTicksUntilOverworked();

	boolean isOverworked();

	IFactory setOverworked(boolean overworked);

	FactoryType getType();

	void upgrade();

	String getName();

	Image getImage();

	default String getImagePath() {
		return "src/resources/Factory.png";
	};

	default int getWidth() {
		return 30;
	};

	Color getColor();
}

package main.factory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class CopperFactory implements IFactory {

	private int level;
	private boolean overworked;

	public CopperFactory() {
		this(1);
	}

	public CopperFactory(int level) {
		this.level = level;
	}

	@Override
	public int getCost() {
		return 200;
	}

	@Override
	public int getProductionPerTick(int level) {
		return 5;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public IFactory setLevel(int level) {
		this.level = level;
		return this;
	}

	@Override
	public int getUpgradeCost(int level) {
		return level <= 5 ? 100 : 200;
	}

	@Override
	public int getTicksUntilOverworked() {
		return 500;
	}

	@Override
	public boolean isOverworked() {
		return this.overworked;
	}

	@Override
	public IFactory setOverworked(boolean overworked) {
		this.overworked = overworked;
		return this;
	}

	@Override
	public void upgrade() {

	}

	@Override
	public Image getImage() {
		return new ImageIcon("src/resources/Factory.png").getImage();
	}

	@Override
	public String getName() {
		return "Copper Factory";
	}

	@Override
	public FactoryType getType() {
		return FactoryType.COPPER;
	}

	@Override
	public Color getColor() {
		return new Color(255, 165, 0);
	}
}

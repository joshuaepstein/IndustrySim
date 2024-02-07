package main.factory;

import javax.swing.*;
import java.awt.*;

public class CoalFactory implements IFactory {

	private int level;
	private boolean overworked;

	public CoalFactory() {
		this(1);
	}

	public CoalFactory(int level) {
		this.level = level;
	}

	@Override
	public int getCost() {
		if (level < 5) return 50;
		return 100;
	}

	@Override
	public int getProductionPerTick(int level) {
		return 100;
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
		return level <= 5 ? 50 : 100;
	}

	@Override
	public int getTicksUntilOverworked() {
		return 0;
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
		return "Coal Factory";
	}

	@Override
	public FactoryType getType() {
		return FactoryType.COAL;
	}

	@Override
	public Color getColor() {
		return Color.BLACK;
	}
}

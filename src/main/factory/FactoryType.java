package main.factory;

import java.awt.Color;

public enum FactoryType {

	COAL("Coal Collector", CoalFactory.class),
	COPPER("Copper Collector", CopperFactory.class),
	;

	private final String name;
	private final Class<? extends IFactory> factoryClass;

	FactoryType(String name, Class<? extends IFactory> factoryClass) {
		this.name = name;
		this.factoryClass = factoryClass;
	}

	public String getName() {
		return name;
	}

	public Class<? extends IFactory> getFactoryClass() {
		return factoryClass;
	}

	public int getWidth() {
		try {
			return factoryClass.getConstructor().newInstance().getWidth();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getImage() {
		try {
			return factoryClass.getConstructor().newInstance().getImagePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getNameAsCamel() {
		return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
	}

	public int getCost() {
		try {
			return factoryClass.getConstructor().newInstance().getCost();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Color getColor() {
		try {
			return factoryClass.getConstructor().newInstance().getColor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

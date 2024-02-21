package main.utils;

import java.util.ArrayList;
import java.util.List;

import main.App;
import main.factory.FactoryType;
import main.factory.IFactory;

public class FactoryManager {
    private final List<IFactory> factories;

    public FactoryManager() {
        this.factories = new ArrayList<>();
    }

    public void addFactory(IFactory factory) {
        this.factories.add(factory);
    }

    public void tick() {
        this.tick(1);
    }

    public void tick(int gameLevel) {
        for (IFactory factory : this.factories) {
            if (!factory.isOverworked()) {
                App.money.computeIfAbsent(factory.getType(), (factoryType) -> new Counter(0)).add(factory.getProductionPerTick(gameLevel));
            }
        }
    }

    public List<IFactory> getFactoriesOfType(FactoryType type) {
		List<IFactory> factories = new ArrayList<>();
		for (IFactory factory : this.factories) {
			if (factory.getType() == type) {
				factories.add(factory);
			}
		}
		return factories;
	}
}

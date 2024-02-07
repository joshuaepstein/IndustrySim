package main.utils;

import java.util.HashMap;
import java.util.Map;

import main.App;
import main.factory.FactoryType;
import main.utils.SoundManager.SoundType;

public class UnlockHelper {
    public Map<FactoryType, Boolean> unlockedResources;
    public static final Map<FactoryType, Integer> unlockCosts = new HashMap<>() {{
        put(FactoryType.COAL, 500);
        put(FactoryType.COPPER, 1000);
    }};

    public UnlockHelper() {
        this.unlockedResources = new HashMap<>();

        // Automatically have COAL unlocked
        this.unlockedResources.put(FactoryType.COAL, true);
    }

    public void unlockResource(FactoryType factoryType) {
        unlockResource(factoryType, App.getSoundManager());
    }

    public void unlockResource(FactoryType factoryType, SoundManager manager) {
        this.unlockedResources.put(factoryType, true);
        manager.playSound(SoundType.LEVEL_UP);
    }

    public boolean isUnlocked(FactoryType factoryType) {
        return this.unlockedResources.get(factoryType);
    }

    public void checkAndUnlockResources(Map<FactoryType, Counter> money, int gameLevel, SoundManager mSoundManager) {
        for (FactoryType factoryType : FactoryType.values()) {
            if (money.get(factoryType).getValue() >= unlockCosts.get(factoryType)) {
                int nextIndex = factoryType.ordinal() + 1;
                if (nextIndex < FactoryType.values().length) {
                    FactoryType nextFactoryType = FactoryType.values()[nextIndex];
                    this.unlockResource(nextFactoryType);
                    mSoundManager.playSound(SoundType.LEVEL_UP);
                    gameLevel++;
                }
            }
        }
    }
}

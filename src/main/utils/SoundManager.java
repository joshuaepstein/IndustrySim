package main.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
    private Map<SoundType, Clip> loadedSounds;
    private String rootPath;

    public SoundManager(String rootPath) {
        this.rootPath = rootPath;
        this.loadedSounds = new HashMap<>();
        loadSounds();
    }

    public void prepareSoundIfNotLoaded(SoundType soundType) {
        if (!loadedSounds.containsKey(soundType)) {
            try {
				String soundName = this.rootPath + soundType.getSoundLocation();
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                loadedSounds.put(soundType, clip);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("Sound " + soundType.getSoundLocation() + " loaded successfully");
            }
        }
    }

    public Clip getOrLoadSound(SoundType soundType) {
        if (!loadedSounds.containsKey(soundType)) {
            prepareSoundIfNotLoaded(soundType);
        }
        return loadedSounds.get(soundType);
    }

    protected void loadSounds() {
        for (SoundType soundType : SoundType.values()) {
            prepareSoundIfNotLoaded(soundType);
        }
    }

    public boolean playSound(SoundType soundType) {
        Clip clip = getOrLoadSound(soundType);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
            return true;
        }
        return false;
    }

    public boolean playSound(SoundType soundType, int times) {
        Clip clip = getOrLoadSound(soundType);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(times);
            return true;
        }
        return false;
    }

    public boolean stopSound(SoundType soundType) {
        Clip clip = getOrLoadSound(soundType);
        if (clip != null) {
            clip.stop();
            return true;
        }
        return false;
    }

    public boolean stopAllSounds() {
        for (SoundType soundType : loadedSounds.keySet()) {
            stopSound(soundType);
        }
        return true;
    }

    public enum SoundType {
        LEVEL_UP("level_up.wav"),
        // TOAST("toast.wav"),
        // CLICK("click.wav")
        ;

        private final String soundLocation;

        SoundType(String soundLocation) {
            this.soundLocation = soundLocation;
        }

        public String getSoundLocation() {
            return soundLocation;
        }

        public static SoundType fromString(String text) {
            for (SoundType b : SoundType.values()) {
                if (b.soundLocation.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
    }
}

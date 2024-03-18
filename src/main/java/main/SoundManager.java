package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.HashMap;
import java.util.Map;

// SINGLETON
public class SoundManager {

    private static SoundManager instance;
    private final Map<String, Clip> soundCache;

    private SoundManager() {
        soundCache = new HashMap<>();
    }

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private Clip getSound(String fileName) {
        if (soundCache.containsKey(fileName))
            return soundCache.get(fileName);

        try {
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(fileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            soundCache.put(fileName, clip);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void playSound(String fileName) {
        Clip clip = getSound(fileName);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
}

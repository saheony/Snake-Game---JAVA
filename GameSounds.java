import javax.sound.sampled.*;
import java.io.InputStream;

public class GameSounds {
    private static Clip eatSound;
    private static Clip gameOverSound;
    static {
        try {
            // Load eat sound
            InputStream eatStream = GameSounds.class.getResourceAsStream("/sounds/eat.wav");
            assert eatStream != null;
            AudioInputStream eatAudio = AudioSystem.getAudioInputStream(eatStream);
            eatSound = AudioSystem.getClip();
            eatSound.open(eatAudio);
            // Load game over sound
            InputStream gameOverStream = GameSounds.class.getResourceAsStream("/sounds/gameover.wav");
            assert gameOverStream != null;
            AudioInputStream gameOverAudio = AudioSystem.getAudioInputStream(gameOverStream);
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(gameOverAudio);
        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
        }
    }
    public static void playEat() {
        if (eatSound != null) {
            eatSound.setFramePosition(0); // Rewind to start
            eatSound.start();
        }
    }
    public static void playGameOver() {
        if (gameOverSound != null) {
            gameOverSound.setFramePosition(0);
            gameOverSound.start();
        }
    }
}

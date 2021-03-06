/*
 * Class allows entire project to easily play sounds
 * The file paths for sounds is contained here
 */
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {
    
    private static final String SOUNDS_LOCATION = "src/resources/sounds/";
    
    protected static void playShootSound() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        playClip(new File(SOUNDS_LOCATION + "shoot.wav"));
    }
    
    protected static void playHitSound() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        playClip(new File(SOUNDS_LOCATION + "hit.wav"));
    }
    
    protected static void playDeathSound() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        playClip(new File(SOUNDS_LOCATION + "death.wav"));
    }
    
    protected static void playExplosionSound() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        playClip(new File(SOUNDS_LOCATION + "explosion.wav"));
    }
    
    protected static void playTimerSound() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        playClip(new File(SOUNDS_LOCATION + "timer.wav"));
    }
    
    private static void playClip(File file) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println("Sound unable to be played");
        }
    }
}

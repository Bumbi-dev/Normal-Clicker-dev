package game.usefullclases;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sounds {
    static Clip clickClip;
    static File clickFile = new File("Assets\\Click.wav");
    static AudioInputStream stream;

    public static void initialize() {
        try {
            stream = AudioSystem.getAudioInputStream(clickFile);
            clickClip = AudioSystem.getClip();
            clickClip.open(stream);
            clickClip.start();

            stream = AudioSystem.getAudioInputStream(clickFile);
            clickClip = AudioSystem.getClip();
            clickClip.open(stream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    public static void playClick() {
        try {
            clickClip.start();
            stream = AudioSystem.getAudioInputStream(clickFile);
            clickClip = AudioSystem.getClip();
            clickClip.open(stream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }
}

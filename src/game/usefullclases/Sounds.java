package game.usefullclases;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sounds {
    //List of the sounds
    public static final File click = new File("Assets\\Click.wav");

    private static AudioInputStream stream;
    private static Clip clickClip;

    public static void initialize() {//initializes one sound, in the future it will initialize mroe
        try {
            stream = AudioSystem.getAudioInputStream(click);
            clickClip = AudioSystem.getClip();
            clickClip.open(stream);
            clickClip.setFramePosition(990000);
            clickClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    public static void playSound(File theSound) {//Plays a sound
        try {
            if(clickClip.isRunning()) {//if it s running it will play the sound over it
                try {
                    stream = AudioSystem.getAudioInputStream(theSound);
                    clickClip = AudioSystem.getClip();
                    clickClip.open(stream);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }

            clickClip.setFramePosition(0);//start the clip from the beggining and plays it
            clickClip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

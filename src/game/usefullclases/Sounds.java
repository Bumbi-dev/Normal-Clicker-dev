package game.usefullclases;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sounds {
    //List of the sounds
    public static final File click = new File("Assets\\Click.wav");
    public static final File money = new File("Assets\\Money.wav");
    static File[] files = {click, money};

    private static AudioInputStream clickStream;
    private static AudioInputStream moneyStream;
    static AudioInputStream[] streams = {clickStream, moneyStream};

    private static Clip clickClip;
    private static Clip moneyClip;
    static Clip[] clips = {clickClip, moneyClip};

    public static void initialize() {//initializes the sounds
        try {
            for (int i = 0; i < streams.length; i++) {
                streams[i] = AudioSystem.getAudioInputStream(files[i]);

                clips[i] = AudioSystem.getClip();
                clips[i].open(streams[i]);
                clips[i].setFramePosition(990000);
                clips[i].start();
            }
        }
        catch(Exception ex) {
            System.out.println("Sound initialization exception");
        }
    }

    public static void playClick() {//Plays click sound

        if(clips[0].isRunning()) {//if it s running it will play the sound over it
            try {
                streams[0] = AudioSystem.getAudioInputStream(files[0]);
                clips[0] = AudioSystem.getClip();
                clips[0].open(streams[0]);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                System.out.println("CLick sound exception");
            }
        }
        clips[0].setFramePosition(0);//start the clip from the beggining and plays it
        clips[0].start();

    }

    public static void playMoney() {//Plays money sound
        if(clips[1].isRunning()) {
            try {
                streams[1] = AudioSystem.getAudioInputStream(files[1]);
                clips[1] = AudioSystem.getClip();
                clips[1].open(streams[1]);
            }
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                System.out.println("Money sound exception");
            }
        }
        clips[1].setFramePosition(0);//start the clip from the beggining and plays it
        clips[1].start();
    }
}

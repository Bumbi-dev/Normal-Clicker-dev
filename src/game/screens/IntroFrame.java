package game.screens;

import game.gameplay.ClickerFrame;
import game.gameplay.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class IntroFrame extends JFrame { //Intro scene, where you choose to continue, or start a new save
    // Interface
    JButton Continue, newGame;
    JLabel intro;
    JPanel mp, bp;

    boolean wait = true;

    public IntroFrame() throws InterruptedException {
        // Initialization
        super("Intro");
        setVisible(true);
        setResizable(false);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);//removes the default icon
        setIconImage(icon);

        intro = new JLabel();
        intro.setFont(new Font("Montserrat", Font.PLAIN, 24));

        newGame = new JButton(); newGame.setFont(new Font("Montserrat", Font.PLAIN, 22));
        Continue = new JButton(); Continue.setFont(new Font("Montserrat", Font.PLAIN, 22));

        Continue.setFocusPainted(false); Continue.setFocusable(false);//prevent buttons from highlighting
        newGame.setFocusPainted(false); newGame.setFocusable(false);

        mp = new JPanel();
        mp.setLayout(new GridBagLayout());

        bp = new JPanel(); bp.setBounds(110, 200, 480, 270);
        bp.setLayout(null);


        // The Introduction

        add(mp);
        mp.add(intro);
        typing("Loading.", "..", 2);

        try {Thread.sleep(1050);}
        catch (InterruptedException ignored){}

        mp.setBounds(0, 0, 585, 100);
        Continue.setBounds(100, 180, 150, 80);
        newGame.setBounds(350, 180, 150, 80);

        bp.add(Continue);
        bp.add(newGame);
        add(bp);

        Continue.setText("Continue");
        newGame.setText("New Game");

        File fila = new File("Assets\\Date Player.txt");

        Continue.addActionListener(e -> {
            if(!fila.exists())
                return;
            dispose();

            Player player = new Player();
            player.loadProgress();

            new ClickerFrame();
        });

        newGame.addActionListener(e -> {
            if(fila.exists()) {
                String[] Options = {"Yes", "No"};
                int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure?", " Start a New Save",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, Options, Options[1]);
                if (PromptResult != 0) {
                    return;
                }
            }

            dispose();
            Player player = new Player();
            player.save();

            new ClickerFrame();
        });
    }

    public void typing( String message, float speed) throws InterruptedException {
        int delay = (int) (100 / speed); // Delay between each character in milliseconds

        int index = 0;

        mp.setLayout(new FlowLayout());

        while (index < message.length()) {
            intro.setText(message.substring(0, index + 1));
            index++;
            Thread.sleep(delay);
        }

    }

    public void typing(String first, String cuv, int times) throws InterruptedException {//Intro sequence with a typewriter effect
        intro.setText(first);
        int delay = 525;

        int count = 0;

        while(count <= cuv.length()) {
            if (count == cuv.length()) {
                if (times > 0) {
                    typing(first, cuv, times - 1);
                    Thread.sleep(delay);
                }
                else {
                    intro.setText("");
                    try {
                        typing("Welcome to Normal Clicker", 0.75f);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            intro.setText(first + cuv.substring(0, count));
            count++;
        }
    }
}
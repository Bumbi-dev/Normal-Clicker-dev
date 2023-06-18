import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class IntroFrame extends JFrame {
    // Interface
    JButton Continue, newGame;
    JLabel intro;
    JPanel mp, bp;
    boolean stai = true;

    public IntroFrame() throws InterruptedException {
        // Initialization
        super("Digger Clicker");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        intro = new JLabel();
        intro.setFont(new Font("Montserrat", Font.PLAIN, 24));

        newGame = new JButton(); newGame.setFont(new Font("Montserrat", Font.PLAIN, 22));
        Continue = new JButton(); Continue.setFont(new Font("Montserrat", Font.PLAIN, 22));

        Continue.setFocusPainted(false);
        newGame.setFocusPainted(false);

        mp = new JPanel();
        mp.setLayout(new GridBagLayout());

        bp = new JPanel(); bp.setBounds(110, 200, 480, 270);
        bp.setLayout(null);


        // Introducerea

        add(mp);
        mp.add(intro);
        typing(intro, "Loading.", "..", 2);

        while(stai) {System.out.print(' '); System.out.print('\b');}
        Thread.sleep(1050);

        mp.setBounds(0, 0, 585, 100);
        Continue.setBounds(100, 180, 150, 80);
        newGame.setBounds(350, 180, 150, 80);

        bp.add(Continue);
        bp.add(newGame);
        add(bp);

        Continue.setText("Continue");
        newGame.setText("New Game");

        Continue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                File fila = new File("Date Player");
                if(!fila.exists())
                    return;

                dispose();

                Player player = new Player();
                player.loadProgress();

                ClickerFrame cf = new ClickerFrame();
                cf.setResizable(false);

            }
        });

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                File fila = new File("Date Player");

                if(fila.exists()) {
                    String[] Optiuni = {"Da", "Nu"};
                    int PromptResult = JOptionPane.showOptionDialog(null, "Esti sigur?", " Start a New Save",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, Optiuni, Optiuni[1]);
                    if (PromptResult != 0) {
                        return;
                    }
                }

                dispose();
                Player player = new Player();
                player.save();

                ClickerFrame cf = new ClickerFrame();
                cf.setResizable(false);
                cf.setVisible(true);
            }
        });
    }


    public void typing(JLabel text, String message, float speed) {
        int delay = (int) (100 / speed); // Delay between each character in milliseconds

        Timer timer = new Timer(delay, new ActionListener() {
            int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                mp.setLayout(new FlowLayout());
                if (index < message.length()) {
                    text.setText(message.substring(0, index + 1));
                    index++;
                } else {
                    stai = false;
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    public void typing(JLabel text, String first, String cuv, int times) {//secventa de inceput
        Timer timer = new Timer(525, new ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count == cuv.length()) {
                    if (times > 0)
                        typing(text, first, cuv, times - 1);
                    else {
                        intro.setText("");
                        typing(intro, "Welcome to Digger Clicker", 0.75f);
                    }
                    ((Timer) e.getSource()).stop();
                }
                text.setText(first + cuv.substring(0, count));
                count++;
            }
        });
        timer.start();
    }
}
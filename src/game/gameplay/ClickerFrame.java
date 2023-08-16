package game.gameplay;


import game.*;
import game.screens.Credits;
import game.usefullclases.Culori;
import game.usefullclases.Sounds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class ClickerFrame extends JFrame {

    JPanel pc;
    Random rand = new Random();
    Instant startTime;

    Progress ps;
    Counter count;
    ClickableSquare clickButton;
    Border border;
    Item rights, moreRights, bonus, question, lessRights, hack, scam, recovery, buyOrDie;
    //when adding a new item, add them in the set/getVariables, in Progress class and in the upgradelist
    Item[] upgradeList;

    boolean tutorialDone = false, negativeUnlocked, isSecondChapter = false;
    int cpsVal = 5, ct = 0;
    double clicks, clickPower;

    public ClickerFrame() {
        // Initialize
        super("Normal Clicker");
        setSize(600, 400);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        exiting();
        Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);//removes the default icon
        setIconImage(icon);

        /**          Components           */
        //button
        int width = 202;
        int squareX = (getWidth() - width) / 2;

        clickButton = new ClickableSquare("Click me", new Color(220, 220, 220));
        clickButton.setBounds(squareX, 160, width , 119);

        border = new Border(clickButton, new Color(80, 80, 80));

        count = new Counter(clicks); count.setBounds(squareX , 115, width, 50);
        count.setVisible(false);

        //Upgrades
        rights = new Item(Culori.available, 0, "Rights");//+ 0.1 CP (clickPower)
        rights.setBounds(430, 35, 124, 102);

        moreRights = new Item(Culori.notAvailable, 100, "More Rights");//+ 1 CP, gradually getting pricier
        moreRights.setBounds(225, 5, 150, 110);

        bonus = new Item(Culori.bonus, 0, "bonus"); //free clicks
        bonus.setBounds(450, 250, 50, 78);

        scam = new Item(Culori.notAvailable, 750, "Scam");//make everything more expensive, + 10 CP
        scam.setBounds(35, 190, 130, 110);

        hack = new Item(Culori.notAvailable, 1500, "Hack");//clicks per second +5 first time, then +10
        hack.setBounds(35, 70, 130, 110);

        lessRights = new Item(Culori.notAvailable, 7000, "Less Rights");//makes all the items disappear except question
        lessRights.setBounds(425, 70, 130, 110);

        question = new Item(Culori.backround, 0, "");//until you afford it it is transparent
        question.setBounds(440, 190, 100, 150);

        recovery = new Item(Culori.available, 0, "RECOVERY");//gets into the minigame
        recovery.setBounds(-460, -215, 130, 110);

        buyOrDie = new Item(Culori.notAvailable, 100, "Buy or Die");//wins the minigame
        buyOrDie.setBounds(225, 5, 150, 110);

        //Adding components
        pc = new JPanel();
        pc.setLayout(null);
        pc.add(clickButton);
        pc.add(border);

        ps = new Progress(this);
        ps.loadProgress();
        add(pc);
        updateComponents();

        //ADMIN COMMANDS
        pc.setFocusable(true);
        KeyListener hecu = new KeyAdapter() {
            int x;
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_K)
                    clicks += clickPower;
                if(e.getKeyCode() == KeyEvent.VK_L) {
                    clicks += clickPower * 100;
                }
                if(e.getKeyCode() == KeyEvent.VK_R) {
                    Player player = new Player();
                    player.save();
                    dispose();

                    ClickerFrame cf = new ClickerFrame();
                    cf.setVisible(true);
                }
                updateProgress();
            }
        };
        pc.requestFocusInWindow();
        pc.addKeyListener(hecu);

        /**-_-_-_-_-_-_- FUNCTIONALITY -_-_-_-_-_-_-_-*/

        rights.button.addMouseListener(new MouseAdapter(){
            boolean x;// for better looking code
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1)//can only be clicked with left click
                    return;

                rights.isBought = true;
                rights.setVisible(false);

                clicks = 0;
                clickPower = 0.1f;

                updateProgress();
            }
        });
        bonus.button.addMouseListener(new MouseAdapter() {//first time gives 20 clicks, then CP * 20 clicks
            boolean x;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1)
                    return;

                if(!moreRights.isBought) {
                    clicks += 20;
                    bonus.setVisible(false);
                }
                else {
                    clicks += clickPower * 20;
                }

                bonus.setBounds(rand.nextInt(20, 530), 300, 50, 78);
                updateComponents();
                updateProgress();
            }
            
        });
        moreRights.button.addMouseListener(new MouseAdapter() {//after scam is bought give +2 CP
            boolean x;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1 || clicks < moreRights.price)
                    return;

                if(isSecondChapter) {
                    if(moreRights.price <= 101)
                        scam.setVisible(true);

                    moreRights.setPrice((int) (moreRights.price * 1.1));
                    moreRights.isBought = true;

                    clicks -= moreRights.price;
                    clickPower += 0.2;

                    updateProgress();
                    return;
                }

                if(!moreRights.isBought) {
                    clicks = 0;
                    clickPower += 0.9;

                    moreRights.isBought = true;
                    moreRights.recolor(Culori.notAvailable);
                }
                else {
                    clicks -= moreRights.price;
                    clickPower++;
                    if(scam.isBought)
                        clickPower++;
                }

                moreRights.setPrice((int)( moreRights.price * 1.15));

                updateProgress();
            }
            
        });

        lessRights.button.addMouseListener(new MouseAdapter() {
            boolean x;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(clicks < lessRights.price || e.getButton() != MouseEvent.BUTTON1)
                    return;

                lessRights.isBought = true;

                clicks = 15;//the question border matches with the background
                clickPower = 0.5;
                cpsVal = 0;

                lessRights.setVisible(false);
                moreRights.setVisible(false);
                hack.setVisible(false);
                scam.setVisible(false);
                bonus.setVisible(false);

                question.setText("???");

                updateProgress();
            }
            
        });
        hack.button.addMouseListener(new MouseAdapter() {
            boolean x;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(clicks < hack.price || e.getButton() != MouseEvent.BUTTON1)
                    return;

                clicks -= hack.price;

                if(isSecondChapter) {
                    updateProgress();
                    return;
                }


                hack.setPrice(1000);

                if(hack.isBought) {
                    cpsVal += 10;
                    updateProgress();
                    return;
                }

                hack.isBought = true;
                question.addText("?");
                cps();
            }
            
        });
        scam.button.addMouseListener(new MouseAdapter() {
            boolean x;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(clicks < scam.price || e.getButton() != MouseEvent.BUTTON1)
                    return;

                scam.isBought = true;
                scam.setVisible(false);

                clicks -= scam.price;
                clickPower += 10;

                lessRights.setPrice(lessRights.price * 10);
                moreRights.setPrice((int) (moreRights.price * 1.15));
                if(hack.price > 1001)
                    hack.setPrice(hack.price * 10);

                if(isSecondChapter) {
                    hack.setVisible(true);
                    return;
                }

                question.addText("?");

                updateProgress();
            }
            
        });

        question.button.addMouseListener(new MouseAdapter() {//ending
            boolean x;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1)
                    return;

                if(question.border.color.equals(Color.black)) {
                    question.isBought = true;
                    clicks = 0;
                    clickPower = 1;
                }

                if(question.butonColor.equals(Culori.question)) {
                    dispose();
                    new Credits(1);//Normal ENDING
                }
            }
            
        });

        recovery.button.addMouseListener(new MouseAdapter() {
            boolean x;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1)
                    return;

                recovery.isBought = true;
                recovery.setVisible(false);
                pc.add(buyOrDie);

                clicks = 0;
                clickPower = 1;

                count.setVisible(true);
                count.update(clicks);
                ps.noStress();
            }
        });
        buyOrDie.button.addMouseListener(new MouseAdapter() {
            boolean x;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1 || clicks < buyOrDie.price)
                    return;

                buyOrDie.isBought = true;
                ps.noStress = false;
                buyOrDie.setVisible(false);

                moreRights.isBought = false;
                scam.isBought = false;
                hack.isBought = false;
                lessRights.isBought = false;

                moreRights.setPrice(100);
                moreRights.setVisible(true);

                clicks = 0;
                clickPower = 0.2;

                isSecondChapter = true;

                updateProgress();
            }
        });

        //the button
        clickButton.addMouseListener(new MouseAdapter() {
            boolean mouseOut;

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1)
                    clickButton.recolor(new Color(180, 180, 180));
                mouseOut = false;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1)
                    return;

                Sounds.playSound(Sounds.click);//click sound

                clickButton.recolor(new Color(220, 220, 220));
                if (mouseOut) {//doesn't add clicks if the cursor is outside
                    clicks -= clickPower;
                    if(negativeUnlocked)//if the negativeUnlocked is true it takes clicks
                        clicks -= clickPower;
                }
                clicks += clickPower; //clickpower += clickpower * (rebirth + 1)

                BigDecimal bd = new BigDecimal(Double.toString(clicks));//Rounds the number
                bd = bd.setScale(1, RoundingMode.HALF_UP);
                clicks = bd.doubleValue();

                updateProgress();
                checkAuto();
            }
            @Override public void mouseExited(MouseEvent e) {mouseOut = true;}
        });

        addComponentListener(new ComponentAdapter() {//When the windows is resized, components are aligned
            private Timer resizeTimer;
            @Override
            public void componentResized(ComponentEvent e) {
                if (resizeTimer != null && resizeTimer.isRunning()) {
                    resizeTimer.restart(); // Restart the timer if it's already running
                } else {
                    resizeTimer = new Timer(300, actionEvent -> updateComponents());
                    resizeTimer.setRepeats(false); // Only execute once
                    resizeTimer.start();
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                if (resizeTimer != null && resizeTimer.isRunning()) {
                    resizeTimer.restart(); // Restart the timer if it's already running
                } else {
                    if(question.isBought) {
                        resizeTimer = new Timer(300, actionEvent -> updateComponents());
                        resizeTimer.setRepeats(false); // Only execute once
                        resizeTimer.start();
                    }
                }
            }
        });

    }

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            System.err.println("Failed to initialize");
        }

        ClickerFrame cf = new ClickerFrame();
        cf.setVisible(true);
    }

    void updateProgress() {
        ps.updateProgress();
    }

    void updateComponents() {
        int x = getX(); x -= x % 4;//for alignment
        int y = getY(); y -= y % 4;

        for(Item item : upgradeList)
            if(item.isVisible() && pc.isAncestorOf(item))
                item.update(x, y);

        clickButton.update(x, y);
        border.update(x, y);
        count.update(x, y);

        if(question.isBought)
            question.setVisible(false);

    }

    void cps() {//free clicks every second
        Thread cpsThread = new Thread(() -> {
            while (cpsVal > 0) {
                if(!isFocused())
                    return;

                clicks += cpsVal;
                count.update(clicks);
                updateProgress();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        cpsThread.start();
    }
    void checkAuto() {//peacefully checks if you are using an autoclicker (restarts system)
        if(ct == 0)
            startTime = Instant.now();
        ct++;

        if(ct == 15) {//if your clicks per second exceed 15
            Duration duration = Duration.between(startTime, Instant.now());
            if (duration.toMillis() <= 1000)
                try {
                    String shutdownCommand = "shutdown /r /t 0";
                    Runtime.getRuntime().exec(shutdownCommand);
                    Player player = new Player();
                    player.save();
                } catch (IOException o) {
                    o.printStackTrace();
                }
            ct = 0;
        }
    }

    void exiting() {//Choosing to save or not when closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                String[] options = { "Yes", "No" };
                int promptResult = JOptionPane.showOptionDialog(null,//dialog box asking if you want to save
                        "Do you want to save?",
                        "Exiting",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options, 0);//predetermined option

                if(promptResult == -1)
                    return;

                if (promptResult == 0) {
                    StringBuilder upgradeuri = new StringBuilder();
                    for (Item item : upgradeList)
                        if (item.isBought)
                            upgradeuri.append(item.name);

                    Player player = new Player(clicks, clickPower, moreRights.price, upgradeuri.toString());
                    if(ps.noStress)//resets everything if you try to save when you're in the minigame
                        player = new Player();
                    player.save();
                }
                dispose();
            }
        });
    }
}
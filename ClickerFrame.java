import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClickerFrame extends JFrame {

    JPanel pc;
    Counter count;
    ClickableSquare clickButton;
    Border border;

    Culori culoare = new Culori();
    Item rights, moreRights, bonus, question, settings, slave, wow;
    Item[] upgradeuriList;

    double clicks, clickPower;
    int rebirths, i;
    int []price = {100, 250, 500, 100000};
    boolean tutorialDone = false, negativeUnlocked;


    public ClickerFrame() {
        // Initialize
        super("Digger Clicker");
        setVisible(true);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        exiting();

        //Components
        int width = 202;
        int squareX = (getWidth() - width) / 2;

        clickButton = new ClickableSquare("Click me", new Color(220, 220, 220));
        clickButton.setBounds(squareX, 160, width , 119);

        border = new Border(clickButton, new Color(80, 80, 80));

        count = new Counter(clicks); count.setBounds(squareX , 115, width, 50);
        count.setVisible(false);

        //upgrade-uri
        rights = new Item(culoare.available, 0, "Rights");//+ 0.1 clickPower
        rights.setBounds(431, 37, 125, 101);

        moreRights = new Item(culoare.notAvailable, 100, "More Rights");//+ 0.9 clickPower + deblocheaza mai multe upgradeuri
        moreRights.setBounds(225, 5, 150, 110);

        bonus = new Item(culoare.bonus, 0, "bonus"); //free clicks
        bonus.setBounds(450, 220, 50, 78);

        slave = new Item(culoare.notAvailable, 75, "Slave");//clickuri automate pe secunda aprox 1
        slave.setBounds(35, 90, 130, 110);

        wow = new Item(culoare.notAvailable, 47, "Wow");
        wow.setBounds(35, 210, 130, 110);

        settings = new Item(culoare.notAvailable, 85, "Settings");//unlock settings
        settings.setBounds(425, 90, 130, 110);

        question = new Item(culoare.question, 215, "???");//secret upgrades if fullscreen
        question.setBounds(425, 210, 130, 110); //pana ti permiti ramane fara culoare, transparenta

        //adaugare componente
        pc = new JPanel();
        pc.setLayout(null);
        pc.add(clickButton);
        pc.add(border);

        loadProgress();
        add(pc);

        //COMENZI PENTRU ADMINI
        pc.setFocusable(true);
        KeyListener hecu = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_K)
                    clicks += clickPower;
                if(e.getKeyCode() == KeyEvent.VK_L) {
                    clicks += clickPower * 10;
                }
                if(e.getKeyCode() == KeyEvent.VK_R) {
                    Player player = new Player();
                    player.save();
                    dispose();
                    ClickerFrame cf = new ClickerFrame();
                }

                upgradeProgress();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        pc.requestFocusInWindow();
        pc.addKeyListener(hecu);

        /**-_-_-_-_-_-_- FUNCTIONALITATE -_-_-_-_-_-_-_-*/

        rights.buton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1)
                    return;
                rights.isBought = true;
                rights.setVisible(false);

                clicks = 0;
                clickPower = 0.1f;

                upgradeProgress();
            }
            int x;
        });
        moreRights.buton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1)
                    return;

                if (clicks >= price[i]) {
                    if(!moreRights.isBought) {
                        clicks = 0;
                        clickPower += 0.9;

                        moreRights.isBought = true;
                        moreRights.recolor(culoare.notAvailable);
                    }
                    else {
                        clicks -= price[i];
                        clickPower += 1;
                    }
                    i++;
                    moreRights.setPrice(price[i]);

                    upgradeProgress();
                }
            }
            int x;
        });
        bonus.buton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1)
                    return;
                clicks += 20;
                bonus.setVisible(false);

                count.update(clicks);
                upgradeProgress();
            }
            int x;
        });

        //butonu
        clickButton.addMouseListener(new MouseAdapter() {
            boolean mouseOut;

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1)
                    clickButton.recolor(new Color(180, 180, 180));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1)
                    return;

                clickButton.recolor(new Color(220, 220, 220));

                if (mouseOut) {//nu se pune click daca ii mouse ul inafara
                    clicks -= clickPower;
                    if(!negativeUnlocked)//daca ii upgradeu deblocat acuma scade cand ii mouse in afara
                        clicks -= clickPower;
                }
                clicks += clickPower; //clickpower += clickpower * (rebirth + 1)

                upgradeProgress();
            }

            @Override public void mouseEntered(MouseEvent e) {mouseOut = false;}
            @Override public void mouseExited(MouseEvent e) {mouseOut = true;}
        });
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            System.err.println("Failed to initialize");
        }

        IntroFrame IF = new IntroFrame();
        IF.setVisible(true);
        IF.setResizable(false);

        //ClickerFrame cf = new ClickerFrame();//pentru testing
        //cf.setVisible(true);//la fel
        //cf.setResizable(false);//atat


        //buton secret daca dai fulscreen
        //upgrade - expand .setResizable(true)
        //settings button - cumparat

        //pt autoclicker "restart pc"
        //question - forma de nether portal, se construieste incet incet pana se deblocheaza cu totu - prima oara bordura dupa ? dupa ?? dupa ??? poate dupa un upgrade sa apara un semnu intrebarii
        //dupa ce apar toate semnele de intrebare apare un countdown cu cate clickuri mai trebuie facute
        // GAMEPLAYUL = Tot cumperi iteme sa te faci mai puternic (+ x clickpower / *x clicpower) si la un moment dat cumperi un item care da rebirth sau termina jocu
        //poate sa fie si un timer/ leaderboard cu cele mai putine clickuri.
        // a negative shop, appears when the count is negative - al doilea ending
        // item misterios - dark side, colare mov cu border negru descrierea:"???" sa deblocheze negative count
    }


    public void upgradeProgress() {
        //tutorialu
        count.update(clicks);
        if(!rights.isBought) {
            if (clicks < 10)
                return;

            pc.add(rights);
            pc.repaint();
        } else {
            pc.add(count);
            tutorialDone = true;
        }
        if(!tutorialDone)  //cand se adauga count ii gata tutorialu, pana atunci nu poti face progres
            return;

        /**------  CHESTII INTERESANTE  ---------**/
        if(!moreRights.isBought) {
            if (clicks >= 25) {
                pc.add(moreRights);
            }
            if (clicks > 100) {
                clicks = 100;
                count.update(clicks);
                moreRights.recolor(culoare.available);
            }
            if(clicks >= price[i]) {
                moreRights.recolor(culoare.available);
            } else moreRights.recolor(culoare.notAvailable);

            if((int)clicks == 50) {
                pc.add(bonus);
            }
            pc.repaint();
            return;
        } else pc.add(moreRights);

        if(clicks < 10 && !(settings.isBought || question.isBought || slave.isBought || clickPower >= 2))
            return;

        //de aici apar butoanele importante
        pc.add(settings);
        pc.add(question);
        pc.add(slave);
        pc.add(wow);
        pc.repaint();

        if(clicks >= price[i])
            moreRights.recolor(culoare.available);
        else moreRights.recolor(culoare.notAvailable);

        if(clicks >= settings.price)
            settings.recolor(culoare.available);

        if(clicks >= question.price)
            question.recolor(culoare.question);

        if(clicks >= slave.price)
            slave.recolor(culoare.available);

        if(clicks >= wow.price)
            wow.recolor(culoare.available);
    }

    private void loadProgress() {
        Player player = new Player();
        player.loadProgress();

        clicks = player.clicks;
        clickPower = player.clickPower;
        rebirths = player.rebirths;

        upgradeuriList = new Item[]{rights, moreRights, question, settings, slave, bonus};

        for (Item item : upgradeuriList) {
            if (player.upgradeuri.contains(item.name)) {
                item.isBought = true;
            }
        }
        upgradeProgress();
    }

    public void exiting() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                String[] options = { "Da", "Nu" };
                int promptResult = JOptionPane.showOptionDialog(null, "Vrei sa salvezi?", "Exiting",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

                if (promptResult == 0) {
                    StringBuilder upgradeuri = new StringBuilder();
                    for (Item item : upgradeuriList)
                        if (item.isBought) {
                            upgradeuri.append(item.name);
                        }
                    Player player = new Player(clicks, clickPower, rebirths, upgradeuri.toString());
                    player.save();
                }
            }
        });
    }
}
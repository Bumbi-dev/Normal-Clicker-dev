package game.gameplay;

import game.screens.Credits;
import game.usefullclases.Culori;
import game.usefullclases.Sounds;
import game.usefullclases.gameVariablesAndMethods;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class itemFunctionality extends gameVariablesAndMethods {

    public void init() {
        rights.button.addMouseListener(new MouseAdapter() {
            boolean x;// for better looking code

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1)//can only be clicked with left click
                    return;
                Sounds.playMoney();

                rights.isBought = true;
                rights.setVisible(false);

                clicks = 0;
                clickPower = 0.1f;

                pc.add(count);
                updateProgress();
            }
        });
        bonus.button.addMouseListener(new MouseAdapter() {//first time gives 20 clicks, then CP * 20 clicks
            boolean x;

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1)
                    return;
                Sounds.playMoney();

                if (!moreRights.isBought) {
                    clicks += 20;
                    bonus.setVisible(false);
                } else {
                    clicks += clickPower * 20;
                }

                Random rand = new Random();
                bonus.setBounds(rand.nextInt(20, 530), 300, 50, 78);
                updateProgress();
            }

        });
        moreRights.button.addMouseListener(new MouseAdapter() {//after scam is bought give +2 CP
            boolean x;

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1 || clicks < moreRights.price)
                    return;
                Sounds.playMoney();

                if (isSecondChapter) {
                    if (moreRights.price <= 101)
                        scam.setVisible(true);

                    clicks -= moreRights.price;
                    clickPower += 0.2;

                    moreRights.setPrice((int) (moreRights.price * 1.1));
                    moreRights.isBought = true;

                    updateProgress();
                    return;
                }

                if (!moreRights.isBought) {
                    clicks = 0;
                    clickPower += 0.9;

                    moreRights.isBought = true;
                } else {
                    clicks -= moreRights.price;
                    clickPower++;
                    if (scam.isBought)
                        clickPower++;
                }

                moreRights.setPrice((int) (moreRights.price * 1.15));

                updateProgress();
            }

        });

        lessRights.button.addMouseListener(new MouseAdapter() {
            boolean x;

            @Override
            public void mouseReleased(MouseEvent e) {
                if (clicks < lessRights.price || e.getButton() != MouseEvent.BUTTON1)
                    return;
                Sounds.playMoney();

                lessRights.isBought = true;

                clicks = 15;//the question border matches with the background
                clickPower = 0.5;
                ClickerFrame.cpsVal = 0;

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
                if (clicks < hack.price || e.getButton() != MouseEvent.BUTTON1)
                    return;
                Sounds.playMoney();

                clicks -= hack.price;

                if (isSecondChapter) {
                    updateProgress();
                    return;
                }

                hack.setPrice(1000);

                if (hack.isBought) {
                    ClickerFrame.cpsVal += 10;
                    updateProgress();
                    return;
                }

                hack.isBought = true;
                question.addText("?");
                ClickerFrame.cps();
            }

        });
        scam.button.addMouseListener(new MouseAdapter() {
            boolean x;

            @Override
            public void mouseReleased(MouseEvent e) {
                if (clicks < scam.price || e.getButton() != MouseEvent.BUTTON1)
                    return;
                Sounds.playMoney();

                scam.isBought = true;
                scam.setVisible(false);

                clicks -= scam.price;
                clickPower += 10;

                lessRights.setPrice(lessRights.price * 10);
                moreRights.setPrice((int) (moreRights.price * 1.15));
                if (hack.price > 1001)
                    hack.setPrice(hack.price * 10);

                if (isSecondChapter) {
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
                if (e.getButton() != MouseEvent.BUTTON1)
                    return;

                if (question.border.color.equals(Color.black)) {
                    question.isBought = true;
                    clicks = 0;
                    clickPower = 1;
                }

                if (question.butonColor.equals(Culori.question))
                    new Credits(Credits.NORMAL_ENDING);//Normal ENDING

            }

        });

        recovery.button.addMouseListener(new MouseAdapter() {
            boolean x;

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1)
                    return;
                Sounds.playMoney();

                firstChapterDone = true;

                recovery.isBought = true;
                recovery.setVisible(false);
                ClickerFrame.pc.add(buyOrDie);

                clicks = 0;
                clickPower = 1;

                ClickerFrame.count.setVisible(true);
                ClickerFrame.count.update(clicks);
                ps.buyOrDieMinigame();
            }
        });
        buyOrDie.button.addMouseListener(new MouseAdapter() {
            boolean x;

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1 || clicks < buyOrDie.price)
                    return;
                Sounds.playMoney();

                buyOrDie.isBought = true;
                ps.isMinigame = false;
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

                Progress.cpsThread.interrupt();
                updateProgress();
            }
        });
    }
}

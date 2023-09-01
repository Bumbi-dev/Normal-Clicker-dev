package game.gameplay;

import game.*;
import game.screens.Credits;
import game.usefullclases.Culori;
import game.usefullclases.GameVariables;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Progress extends GameVariables {
    ClickerFrame cf;
    JPanel pc;

    Counter count;

    boolean noStress = false;
    int Timer;

    public Progress (ClickerFrame cf) {
        this.cf = cf;
    }

    void updateProgress() {
        count.update(clicks);

        if(!buyOrDie.isBought) {
            firstChapter();
            return;
        }

        /**_____SECOND CHAPTER_______**/
        //Recovering Phase
        //when hack is bought add a new button

        //slow progress chapter
        cf.updateComponents();
        updateVisibility();
    }

    private void firstChapter() {
        //TUTORIAL PHASE
        if (!rights.isBought) {
            if (clicks < 10)
                return;

            pc.add(rights);
            pc.repaint();
            if(clicks >= 100)
                new Credits("Such an enthusiast");
        } else {
            pc.add(count);
            tutorialDone = true;
        }
        if (!tutorialDone) //when count appears the tutorial is done, until then you can't make progress
            return;
        /**------  STARTING  ---------**/
        if (!moreRights.isBought) {// Suspense until 25 clicks, nothing on the screen, then "moreRights" appears
            if (clicks >= 25) {
                pc.add(moreRights);
            }
            if (clicks > 100) {
                clicks = 100;
                count.update(clicks);
                moreRights.recolor(Culori.available);
            }
            if (clicks >= moreRights.price) {
                moreRights.recolor(Culori.available);
            } else moreRights.recolor(Culori.notAvailable);

            if ((int) clicks == 50) {
                pc.add(bonus);
            }
            pc.repaint();
            return;
        } else pc.add(moreRights);

        if (clicks < 10 && !(lessRights.isBought || hack.isBought || clickPower >= 2)) {//after another 10 clicks the other buttons appear
            bonus.setVisible(false);
            bonus.setBounds(100, 300, 50, 78);
            return;
        }

        // INTENSE GAMEPLAY PHASE
        pc.add(lessRights);
        pc.add(question);
        pc.add(hack);
        pc.add(scam);
        pc.repaint();

        if (clicks >= 25_000 && !lessRights.isBought)
            bonus.setVisible(true);
        if (clicks >= 50_000)
            bonus.setVisible(false);

        if (!question.desc.getText().equals("???") && clickPower > 1) {
            updateVisibility();
            return;
        }

        if(!recovery.isBought)
            count.setVisible(false);

        if(!question.isBought) {
            clicks = Math.min(255, clicks);// The outline slowly darkens until it is pitch black
            int x = (int) (255 - clicks);

            question.add(question.button);
            question.add(question.border);
            question.border.recolor(new Color(x, x, x));
            return;
        }

        cf.setResizable(true);
        question.recolor(Culori.question);
        pc.add(recovery);

        //MINIGAME PHASE
        if(noStress) {
            if(Timer > 10)
                if (clicks + 9 >= buyOrDie.price)
                    buyOrDie.setPrice(++buyOrDie.price);

            updateVisibility();
        }
    }

    void noStress() {// "minigame" - if you don't buy the item before the timer runs out you "die" / get bad ending, the price increases as you approach it and when there are 5 seconds left the clicks are reset to 100, but the price stays the same
        buyOrDie.setVisible(true);
        buyOrDie.setPrice(100);
        buyOrDie.recolor(Culori.notAvailable);
        AtomicInteger x = new AtomicInteger(100);

        Thread cpsThread = new Thread(() -> {
            noStress = true;
            cf.updateComponents();
            while(x.get() > 0 && noStress) {
                buyOrDie.setDesc("Buy or Die: " + x.decrementAndGet() + "s");//displays the time left
                pc.repaint();

                if(x.get() < 95 && !cf.isVisible())//if the windows is closed the thread is stopped
                    break;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.Timer = x.get();

                if(this.Timer == 10) {
                    clicks = Math.min(clicks, 100);
                    count.update(clicks);
                    updateVisibility();
                }
            }
            if(cf.isVisible() && noStress) {
                cf.dispose();
                if(clicks >= 100)
                    new Credits(Credits.DEATH, "You could try being more patient next time!");//Bad ENDING
                else
                    new Credits(Credits.DEATH, "Why bother clicking");
            }
        });

        cpsThread.start();
    }

    void updateVisibility() {//If the button is affordable it makes them green, if not red
        for(Item item: upgradeList) {
            if(item.equals(bonus) || item.equals(question) || item.equals(rights)) {
                continue;
            }
            if (clicks >= item.price)
                item.recolor(Culori.available);
            else item.recolor(Culori.notAvailable);
        }
    }
    void loadProgress() {//get the progress from file
        getObjects();

        Player player = new Player();
        player.loadProgress();

        clicks = player.clicks;
        clickPower = player.clickPower;
        moreRights.setPrice(player.price);

        upgradeList = new Item[]{rights, moreRights, scam, hack, bonus, lessRights, question, recovery, buyOrDie};

        for (Item item : upgradeList)
            if (player.upgradeuri.contains(item.name)) {
                item.isBought = true;
                if(!item.equals(moreRights))
                    item.setVisible(false);
            }

        if(buyOrDie.isBought)
            isSecondChapter = true;

        if(isSecondChapter) {
            moreRights.setVisible(true);
            if(moreRights.isBought) {
                scam.setVisible(true);
            }
            if(scam.isBought)
                hack.setVisible(true);
            if(hack.isBought)
                lessRights.setVisible(true);
        }

        question.isBought = player.upgradeuri.contains("???");

        if(scam.isBought) {
            lessRights.setPrice(lessRights.price * 10);
            hack.setPrice(hack.price * 10);

            question.addText("?");
        }
        if(hack.isBought) {
            question.addText("?");
            hack.setVisible(true);
            cf.cps();
            hack.setPrice(1000);
        }
        if(lessRights.isBought) {
            cf.cpsVal = 0;

            moreRights.setVisible(false);
            hack.setVisible(false);
            scam.setVisible(false);

            question.setText("???");

            if(!question.isBought) {
                question.add(question.button);
                question.add(question.border);
            }
        }
        if(recovery.isBought && !buyOrDie.isBought)
            noStress();

        question.setVisible(true);

        updateProgress();
    }

    private void getObjects() {
        count = cf.count;
        pc = cf.pc;

        rights = cf.rights;
        moreRights = cf.moreRights;
        bonus = cf.bonus;
        question = cf.question;
        lessRights = cf.lessRights;
        hack = cf.hack;
        scam = cf.scam;
        recovery = cf.recovery;
        buyOrDie = cf.buyOrDie;
    }
}
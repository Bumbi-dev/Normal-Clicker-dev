import javax.swing.*;
import java.awt.*;

public class Progress {
    ClickerFrame cf;
    JPanel pc;

    Counter count;
    Item rights, moreRights, bonus, question, lessRights, hack, scam;
    Item[] upgradeList;

    boolean tutorialDone = false, negativeUnlocked;
    double clicks, clickPower;

    public Progress (ClickerFrame cf) {
        this.cf = cf;
    }

    void updateProgress() {
        getVariables();
        count.update(clicks);
        //tutorial
        if(!rights.isBought) {
            if (clicks < 10) {
                setVariables(); return;
            }
            System.out.println("a");
            pc.add(rights);
            pc.repaint();
        } else {
            pc.add(count);
            tutorialDone = true;
        }
        if(!tutorialDone) { //when count appears the tutorial is done, until then you can't make progress
            setVariables(); return;
        }
        /**------  INTERESTING THINGS  ---------**/
        if(!moreRights.isBought) {// Suspense until 25 clicks, nothing on the screen, then "moreRights" appears
            if (clicks >= 25) {
                pc.add(moreRights);
            }
            if (clicks > 100) {
                clicks = 100;
                count.update(clicks);
                moreRights.recolor(Culori.available);
            }
            if(clicks >= moreRights.price) {
                moreRights.recolor(Culori.available);
            } else moreRights.recolor(Culori.notAvailable);

            if((int)clicks == 50) {
                pc.add(bonus);
            }
            pc.repaint();
            setVariables(); return;
        } else pc.add(moreRights);

        if(clicks < 10 && !(lessRights.isBought || hack.isBought || clickPower >= 2)) {//after another 10 clicks the other buttons appear
            bonus.setVisible(false);
            bonus.setBounds(100, 300, 50, 78);
            setVariables(); return;
        }
        // Important buttons
        pc.add(lessRights);
        pc.add(question);
        pc.add(hack);
        pc.add(scam);
        pc.repaint();

        if(clicks >= 25_000 && !bonus.isBought)
            bonus.setVisible(true);
        if(clicks >= 50_000) {
            bonus.setVisible(false);
            bonus.isBought = true;
        }

        if(!question.desc.getText().equals("???") && clickPower > 1) {
            updateVisibility();
            setVariables(); return;
        }

        question.add(question.buton);
        question.add(question.border);

        clicks = Math.min(255, clicks);// The outline slowly darkens until it is pitch black
        int x =(int) (255 - clicks);

        question.border.recolor(new Color(x, x, x));

        if(question.isBought) {
            cf.setResizable(true);
            //pc.add button, when that button is bought unlock the counter, set click to 0, set clickpower to 0
            question.recolor(Culori.question);
        }
        count.setVisible(false);
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
        setVariables();
    }
    void loadProgress() {//get the progress from file
        getVariables();
        Player player = new Player();
        player.loadProgress();

        clicks = player.clicks;
        clickPower = player.clickPower;
        moreRights.setPrice(player.price);

        upgradeList = new Item[]{rights, moreRights, scam, hack, bonus, lessRights, question};
        cf.upgradeList = upgradeList;

        for (Item item : upgradeList) {
            if (player.upgradeuri.contains(item.name)) {
                item.isBought = true;
                if(!item.equals(moreRights))
                    item.setVisible(false);
            }
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

            question.add(question.buton);
            question.add(question.border);
            question.desc.setText("???");
        }

        question.setVisible(true);

        setVariables();
        updateProgress();
    }

    void getVariables() {
        pc = cf.pc;
        count = cf.count;
        rights = cf.rights;
        moreRights = cf.moreRights;
        bonus = cf.bonus;
        question = cf.question;
        lessRights = cf.lessRights;
        hack = cf.hack;
        scam = cf.scam;
        upgradeList = cf.upgradeList;

        tutorialDone = cf.tutorialDone;
        negativeUnlocked = cf.negativeUnlocked;
        clicks = cf.clicks;
        clickPower = cf.clickPower;
    }
    void setVariables() {
        cf.pc = pc;

        cf.count = count;
        cf.rights = rights;
        cf.moreRights = moreRights;
        cf.bonus = bonus;
        cf.question = question;
        cf.lessRights = lessRights;
        cf.hack = hack;
        cf.scam = scam;
        cf.upgradeList = upgradeList;

        cf.tutorialDone = tutorialDone;
        cf.negativeUnlocked = negativeUnlocked;
        cf.clicks = clicks;
        cf.clickPower = clickPower;
    }
}
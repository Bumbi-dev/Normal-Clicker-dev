import javax.swing.*;
import java.awt.*;

public class Progresu {
    ClickerFrame cf;

    JPanel pc;

    Counter count;
    Item rights, moreRights, bonus, question, lessRights, hack, scam;
    Item[] upgradeList;

    boolean tutorialDone = false, negativeUnlocked;
    double clicks, clickPower;

    public Progresu (ClickerFrame cf) {
        this.cf = cf;
    }

    void updateProgress1() {
        getVariables();
        count.update(clicks);
        //tutorial
        if(!rights.isBought) {
            if (clicks < 10) {
                setVariables(); return;
            }
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
            expansion();
            question.recolor(Culori.question);
        }
        count.setVisible(false);
    }

    void updateVisibility() {//If the button is affordable it makes them green, if not red
        if(clicks >= lessRights.price)
            lessRights.recolor(Culori.available);
        else lessRights.recolor(Culori.notAvailable);

        for(Item item: upgradeList) {
            if(item.equals(bonus) || item.equals(question)) {
                setVariables(); return;
            }
            if (clicks >= item.price)
                item.recolor(Culori.available);
            else item.recolor(Culori.notAvailable);
        }
         setVariables(); return;
    }

    void expansion() {
        cf.setResizable(true);
        //pc.add
        //pc.add
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

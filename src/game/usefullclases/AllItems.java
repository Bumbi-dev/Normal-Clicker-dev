package game.usefullclases;

import game.gameplay.Item;

import javax.swing.*;

public class AllItems extends JFrame {//help me god
    public static Item rights, moreRights, bonus, question, lessRights, hack, scam, recovery, buyOrDie;
    public static Item []upgradelist = {rights, moreRights, scam};

    public AllItems(String text) {
        super(text);
    }
}

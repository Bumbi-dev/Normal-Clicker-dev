package game.usefullclases;

import game.gameplay.Item;

import javax.swing.*;

public class GameVariables extends JFrame {//maybe remove JFrame and create object in ClickerFrame class

    public static boolean tutorialDone = false, negativeUnlocked, firstChapterDone = false, isSecondChapter = false;
    public static double clicks, clickPower;

    public static Item rights, moreRights, bonus, question, lessRights, hack, scam, recovery, buyOrDie;
    public static Item []upgradeList = {rights, moreRights, bonus, question, lessRights, hack, scam, recovery, buyOrDie};

    public GameVariables(String text) {
        super(text);
    }

    public GameVariables() {}//default constructor
}
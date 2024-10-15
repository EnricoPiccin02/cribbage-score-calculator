package com.sdm.units;

public class App {
    public static void main(String[] args) {
        Hand h = new Hand();
        h.addCards("0♦J♥Q♠A♣9♦"); // "5♥5♦5♠J♣5♣"
        h.showHand();
        h.getScore();
    }
}
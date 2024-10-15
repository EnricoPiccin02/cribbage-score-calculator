package com.sdm.units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Hand {
    private static final int CARDS_PER_HAND = 5;
    private List<Card> hand;
    private int runPoints;
    private int fiftPoints;
    private int pairPoints;
    private int flushPoints;
    
    public Hand(){
        hand = new ArrayList<>();
        runPoints = fiftPoints = pairPoints = flushPoints = 0;
    }

    public final boolean addCard(String card){
        Card c = new Card(card);
        if(c.validate() && hand.size() < CARDS_PER_HAND)
            return hand.add(c);
        else
            return false;
    }

    public final void addCards(String hand){
        Pattern.compile("(?=[AJQK0[2-9]][♣♦♥♠])")
               .splitAsStream(hand.replaceAll(" ", ""))
               .collect(Collectors.toList())
               .forEach(this::addCard);
    }

    public final void getScore(){
        runPoints = fiftPoints = pairPoints = flushPoints = 0;
        System.out.println("fifteen-twos = " + calcFiftPoints() + " pts.");
        System.out.println("runs = " + calcRunPoints() + " pts.");
        System.out.println("pairs = " + calcPairPoints() + " pts.");
        System.out.println("flush = " + calcFlushPoints() + " pts.");
        System.out.println("TOTAL = " + getTotPoints() + " pts.");
    }

    public final void showHand(){
        hand.forEach(c -> System.out.println(c));
    }
    
    private final int calcFiftPoints(){
        List<Card> tempList = new ArrayList<>(hand.size());
        tempList.addAll(0, hand);
        calcAllFift(getZeroHand(2), 2);
        return fiftPoints;
    }
    
    private final int calcAllFift(List<Card> tempList, int r){
        if(r > hand.size())
            return 0;
        else
            return calcAllCombValue(tempList, 0, hand.size() - 1, 0, r) +
                   calcAllFift(getZeroHand(r + 1), r + 1);
    }

    private final int calcAllCombValue(List<Card> tempList, int start, int end, int index, int r){
        if (index == r)
            return fiftPoints += tempList.stream().mapToInt(Card::getValue).sum() == 15 ? 2 : 0;

        for (int i = start; i <= end && end - i + 1 >= r - index; i++){
            tempList.set(index, hand.get(i));
            calcAllCombValue(tempList, i + 1, end, index + 1, r);
        }

        return 0;
    }

    private final List<Card> getZeroHand(int nCards){
        List<Card> tmpL = new ArrayList<>();
        for(int i = 0; i < nCards; i++)
            tmpL.add(new Card('0', '♣'));
        return tmpL;
    }

    private final int calcRunPoints(){
        hand.stream().sorted((c1, c2) -> c1.getValue() - c2.getValue()).reduce((c1, c2) -> {
            runPoints += c1.getNextRank() == c2.rank() ? 1 : runPoints > 1 ? 0 : -runPoints; 
            return c2;
        });
        return runPoints = runPoints > 1 ? runPoints + 1 : 0;
    }

    private final int calcPairPoints(){
        Map<Character,Integer> countRanks = new HashMap<>();

        hand.stream().map(Card::rank).distinct().forEach(rank -> {
            hand.forEach(card -> {
                countRanks.merge(rank, rank == card.rank() ? 1 : 0, Integer::sum);
            });
        });

        countRanks.forEach((rank, count) -> {
            pairPoints += switch(count){
                case 2 -> 2;
                case 3 -> 6;
                case 4 -> 12;
                default -> 0;
            };
        });

        return pairPoints;
    }

    private final int calcFlushPoints(){
        hand.stream().reduce((c1, c2) -> {
            flushPoints += c1.suite() == c2.suite() ? 1 : 0;
            return c2;
        });

        return flushPoints = switch(flushPoints){
            case 3, 4 -> flushPoints + 1;
            default -> 0;
        } + (hand.contains(new Card('J', hand.getLast().suite())) ? 1 : 0); 
    }

    private final int getTotPoints(){
        return fiftPoints + runPoints + pairPoints + flushPoints;
    }
}
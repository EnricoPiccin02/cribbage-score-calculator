package com.sdm.units;

public record Card(char rank, char suite) {
    public Card(String card){
        this(card != null && card.length() > 1 ? card.toCharArray()[0] : '\0',
             card != null && card.length() > 1 ? card.toCharArray()[1] : '\0');
    }

    public final boolean validate(){
        return switch(rank){
            case 'A', '0', '2', '3', '4', '5', '6', '7', '8', '9', 'J', 'Q', 'K' -> true;
            default -> false;
        } && switch(suite){
            case '♣', '♦', '♥', '♠' -> true;
            default -> false;
        };
    }

    public final int getValue(){
        return switch(rank){
            case 'A' -> 1;
            case 'J', 'Q', 'K', '0' -> 10;
            default -> Character.getNumericValue(rank);
        };
    }

    public final char getNextRank(){
        return switch(rank){
            case 'A' -> '2';
            case '2' -> '3';
            case '3' -> '4';
            case '4' -> '5';
            case '5' -> '6';
            case '6' -> '7';
            case '7' -> '8';
            case '9' -> '0';
            case '0' -> 'J';
            case 'J' -> 'Q';
            case 'Q' -> 'K';
            default -> '\0';
        };
    }
}
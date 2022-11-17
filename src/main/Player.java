package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Player {
    int playerIdx;
    ArrayList<Card> deck;
    Hero hero;

    public Player(int playerIdx, ArrayList<Card> deck, Hero hero){
        this.playerIdx = playerIdx;
        this.deck = deck;
        this.hero = hero;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }

    public void setPlayerIdx(int playerIdx) {
        this.playerIdx = playerIdx;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }
}

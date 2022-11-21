package main.Game;

import main.Card.Card;
import main.Card.Hero;

import java.util.ArrayList;

public class Player {
    ArrayList<Card> deck;
    ArrayList<Card> cardsInHand;
    Hero hero;
    int mana;
    int max_mana;

    public Player(ArrayList<Card> deck){
        this.deck = deck;
        this.mana = 1;
        this.max_mana = 10;
        cardsInHand = new ArrayList<>();
    }

    public Player(Player player){
        this.deck = player.getDeck();
        this.mana = player.getMana();
        this.cardsInHand = player.getCardsInHand();
    }

    public void addMana(int mana){
        this.mana += mana;
    }

    public void removeMana(int mana){
        this.mana -= mana;
    }

    public void placeCardInHand(){
        if (!deck.isEmpty())
            cardsInHand.add(deck.remove(0));
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
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

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getMana() {
        return mana;
    }
}

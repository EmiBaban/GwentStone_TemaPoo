package main.Game;

import main.Card.Card;
import main.Card.Hero;
import java.util.ArrayList;

public class Player {
    private ArrayList<Card> deck;
    private final ArrayList<Card> cardsInHand;
    private Hero hero;
    private int mana;

    public Player(final ArrayList<Card> deck) {
        this.deck = deck;
        this.mana = 1;
        cardsInHand = new ArrayList<>();
    }

    public Player(final Player player) {
        this.deck = player.getDeck();
        this.mana = player.getMana();
        this.cardsInHand = player.getCardsInHand();
        this.hero = player.hero;
    }

    public final void addMana(final int mana) {
        this.mana += mana;
    }

    public final void removeMana(final int mana) {
        this.mana -= mana;
    }

    public final void placeCardInHand() {
        if (!deck.isEmpty()) {
            cardsInHand.add(deck.remove(0));
        }
    }

    public final ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public final void setDeck(final ArrayList<Card> deck) {
        this.deck = deck;
    }

    public final ArrayList<Card> getDeck() {
        return deck;
    }

    public final void setHero(final Hero hero) {
        this.hero = hero;
    }

    public final Hero getHero() {
        return hero;
    }

    public final void setMana(final int mana) {
        this.mana = mana;
    }

    public final int getMana() {
        return mana;
    }
}

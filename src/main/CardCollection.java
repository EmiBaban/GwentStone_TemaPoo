package main;

import java.util.ArrayList;

public class CardCollection {
    private ArrayList<Card> cards = new ArrayList<Card>();

    public CardCollection() {
    }

    public void add(Card card) {
        cards.add(card);
    }
}

package main.Card;

import fileio.CardInput;

public class Hero extends Card {
    public int health;
    public Hero(CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName());
        this.health = 30;
    }
}

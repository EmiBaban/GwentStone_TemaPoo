package main.Card;

import fileio.CardInput;

public class Hero extends Card {
    private int health;
    public Hero(final CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(),
                cardInput.getColors(), cardInput.getName());

        health = 30;
    }
    public final void setHealth(final int health) {
        this.health = health;
    }

    public final int getHealth() {
        return health;
    }
}

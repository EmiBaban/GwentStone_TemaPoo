package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Hero extends Card{
    public int health;
    public Hero(CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName());
        this.health = 30;
    }
}

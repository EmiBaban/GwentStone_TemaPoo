package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Environment extends Card{
    public Environment(CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName());
    }
}

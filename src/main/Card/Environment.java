package main.Card;

import fileio.CardInput;
import main.Card.Card;

public class Environment extends Card {
    public Environment(CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName());
    }

    public Environment(Environment environment) {
        super(environment.mana, environment.description, environment.colors, environment.name);
    }
    public void Firestorm(){

    }
    public void Winterfell(){

    }

}

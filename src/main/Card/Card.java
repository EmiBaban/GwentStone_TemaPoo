package main.Card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import main.Game.Game;

import java.util.ArrayList;

@JsonIgnoreProperties({"frozen"})

public class Card {
    private static boolean frozen;
    public int mana;
    public String description;
    public ArrayList<String> colors;
    public String name;

    public Card(int mana, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.colors = colors;
        this.name = name;
        this.description = description;
    }

    public static void setFrozen(boolean frozen) {
        Card.frozen = frozen;
    }

    public boolean getFrozen(){
        return frozen;
    }

    public static int getCardRow(Card card, int playerIdx, Game game) {
        if (game.getFrontRow().contains(card.name)) {
            return (playerIdx == 1) ? 2 : 1;
        }
        else
            return (playerIdx == 1)? 3 : 0;

    }

    public boolean canBePlacedOnTable() {
        return false;
    }
}

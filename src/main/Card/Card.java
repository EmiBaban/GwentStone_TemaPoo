package main.Card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import main.Game.Game;

import java.util.ArrayList;

@JsonIgnoreProperties({"frozen"})
public class Card {
    private boolean frozen;
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;

    public Card(final int mana, final String description,
                final ArrayList<String> colors, final String name) {
        this.mana = mana;
        this.colors = colors;
        this.name = name;
        this.description = description;
    }

    public final void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    public final boolean getFrozen() {
        return frozen;
    }

    /**
     *
     * @param card
     * @param playerIdx
     * @param game
     * @return
     */
    public static int getCardRow(final Card card, final int playerIdx, final Game game) {
        if (game.getFrontRow().contains(card.name)) {
            return (playerIdx == 1) ? 2 : 1;
        } else {
            return (playerIdx == 1) ? 3 : 0;
        }
    }

    /**
     *
     * @return
     */
    public boolean canBePlacedOnTable() {
        return false;
    }
    public final void setMana(final int mana) {
        this.mana = mana;
    }

    public final int getMana() {
        return mana;
    }

    public final void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final String getDescription() {
        return description;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }
}

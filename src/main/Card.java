package main;

import java.util.ArrayList;

public class Card {
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
}

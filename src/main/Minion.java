package main;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;

@JsonPropertyOrder({"mana", "attackDamage", "health", "description", "colors", "name"})
public class Minion extends Card{
    public int health;
    public int attackDamage;
    public Minion(CardInput cardInput) {
       super(cardInput.getMana(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName());
       this.health = cardInput.getHealth();
       this.attackDamage = cardInput.getAttackDamage();
    }

//    public Minion(Minion minion) {
//        super(minion.mana, minion.description, minion.colors, minion.name);
//        this.health = minion.health;
//        this.attackDamage = minion.attackDamage;
//    }
}

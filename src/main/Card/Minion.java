package main.Card;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fileio.CardInput;

import java.util.List;

@JsonPropertyOrder({"mana", "attackDamage", "health", "description", "colors", "name"})
public class Minion extends Card {
    private int health;
    private int attackDamage;
    public Minion(final CardInput cardInput) {
       super(cardInput.getMana(), cardInput.getDescription(),
               cardInput.getColors(), cardInput.getName());
       this.health = cardInput.getHealth();
       this.attackDamage = cardInput.getAttackDamage();
    }
    public Minion(final Minion minion) {
        super(minion.getMana(), minion.getDescription(),
                minion.getColors(), minion.getName());
        this.health = minion.health;
        this.attackDamage = minion.attackDamage;
    }

    public final int getHealth() {
        return health;
    }

    public final void setHealth(final int health) {
        this.health = health;
    }

    public final void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     *
     * @return
     */
    public final boolean canBePlacedOnTable() {
        return true;
    }

    /**
     *
     * @param minionList
     * @return
     */
    public final boolean isTank(final List minionList) {
        return minionList.contains(getName());
    }

    /**
     *
     * @param minion
     */
    public final void attackCard(final Minion minion) {
        minion.setHealth(minion.getHealth() - attackDamage);
    }
}

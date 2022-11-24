package main.Card;

import fileio.CardInput;
import main.Game.Game;

import java.util.ArrayList;
import java.util.Comparator;

public class Environment extends Card {
    public Environment(final CardInput cardInput) {
        super(cardInput.getMana(), cardInput.getDescription(),
                cardInput.getColors(), cardInput.getName());
    }

    public Environment(final Environment environment) {
        super(environment.getMana(), environment.getDescription(),
                environment.getColors(), environment.getName());
    }

    /**
     *
     * @param game
     * @param affectedRow
     */
    public final void firestormAbility(final Game game, final int affectedRow) {
            ArrayList<Minion> minions = game.getRow(affectedRow);
            for (Minion minion : minions) {
                minion.setHealth(minion.getHealth() - 1);
            }
    }

    /**
     *
     * @param game
     * @param affectedRow
     */
    public final void winterfellAbility(final Game game, final int affectedRow) {
        ArrayList<Minion> minions = game.getRow(affectedRow);
        for (Minion minion : minions) {
            minion.setFrozen(true);
        }

    }

    public final void heartHoundAbility(final Game game, final int affectedRow) {
            ArrayList<Minion> minions = game.getRow(affectedRow);
            Minion maxHealthMinion = minions.stream().
                    max(Comparator.comparing(Minion::getHealth)).get();
            minions.remove(maxHealthMinion);
            game.getTable().get(game.oppositeRow(affectedRow)).add(maxHealthMinion);
    }


}

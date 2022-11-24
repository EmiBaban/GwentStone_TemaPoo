package main.Game;

import main.Card.Card;
import main.Card.Minion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private int currentRound = 1;
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private final Player startingPlayer;
    private final ArrayList<ArrayList<Minion>> table = new ArrayList<ArrayList<Minion>>();
    private final List<String> frontRow = Arrays.asList("The Ripper",
            "Miraj", "Goliath", "Warden");
    private final List<String> backRow = Arrays.asList("Sentinel",
            "Berserker", "The Cursed One", "Disciple");

    private ArrayList<Minion> attackCards = new ArrayList<>();

    public Game(final Player player1, final Player player2, final int startingPlayer) {
        this.player1 = player1;
        this.player2 = player2;
        if (startingPlayer == 1) {
            currentPlayer = player1;
        } else {
            currentPlayer = player2;
        }
        this.startingPlayer = currentPlayer;

        for (int i = 0; i < 4; i++) {
            table.add(new ArrayList<Minion>());
        }
    }

    public final ArrayList<ArrayList<Minion>> getTable() {
        return table;
    }

    /**
     * @return
     */
    public final void endTurn() {
        if (currentPlayer != startingPlayer) {
            currentRound++;
            player1.addMana(Math.min(currentRound, 10));
            player2.addMana(Math.min(currentRound, 10));

            player1.placeCardInHand();
            player2.placeCardInHand();
        }
        resetFrozenCards();
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    /**
     *
     * @return
     */
    public final Player getPlayer1() {
        return player1;
    }

    public final Player getPlayer2() {
        return player2;
    }

    public final List<String> getFrontRow() {
        return frontRow;
    }

    public final int getCurrentPlayer() {
        if (currentPlayer == player1) {
            return 1;
        } else {
            return 2;
        }
    }

    public final boolean isRowFull(final int row) {
        return table.get(row).size() == 5;
    }

    public final ArrayList<Minion> getRow(final int row) {
        return table.get(row);
    }

    public final int oppositeRow(final int row) {
        if (currentPlayer == player1) {
            if (row == 0) {
                return 3;
            } else {
                return 2;
            }
        }
        if (row == 3) {
            return 0;
        } else {
            return 1;
        }
    }

    public final Minion getCardAtPosition(final int x, final int y) {
//        if (table.get(x).size() > y) {
//            return table.get(x).get(y);
//        }
//        return null;
        try {
            return table.get(x).get(y);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public final boolean isEnemyRow(final int row) {
        if (currentPlayer == player1 && (row == 2 || row == 3)) {
            return true;
        }
        return currentPlayer == player2 && (row == 0 || row == 1);
    }

    public final void resetFrozenCards() {
        if (currentPlayer == player1) {
            for (Card card : getRow(2)) {
                card.setFrozen(false);
            }
            for (Card card : getRow(3)) {
                card.setFrozen(false);
            }
        } else {
            for (Card card : getRow(1)) {
                card.setFrozen(false);
            }
            for (Card card : getRow(0)) {
                card.setFrozen(false);
            }
        }
    }

    public final void addAttackCards(final Minion minion) {
        attackCards.add(minion);
    }

    public final void setAttackCards(final ArrayList<Minion> attackCards) {
        this.attackCards = attackCards;
    }

    public final ArrayList<Minion> getAttackCards() {
        return attackCards;
    }


    public final boolean enemyHasTanks(final List minionList) {
        if (currentPlayer == player1) {
            for (Minion minion : getRow(1)) {
                if (minion.isTank(minionList)) {
                    return true;
                }
            }
        }
        for (Minion minion : getRow(2)) {
            if (minion.isTank(minionList)) {
                return true;
            }
        }
        return false;
    }

    public final void removeDeadCardsFromTable() {
        for (ArrayList<Minion> row
                : table) {
            row.removeIf(minion -> minion.getHealth() <= 0);
        }
    }

}

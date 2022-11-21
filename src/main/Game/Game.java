package main.Game;

import main.Card.Minion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    int turn = 1;
    int round = 1;
    int playerTurn;
    public Player player1;
    public Player player2;
    ArrayList<ArrayList<Minion>> table = new ArrayList<ArrayList<Minion>>();

    List<String> frontRow = Arrays.asList("The Ripper", "Miraj", "Goliath", "Warden");
    List<String> backRow = Arrays.asList("Sentinel", "Berserker", "The Cursed One", "Disciple");

    public Game(Player player1, Player player2, int playerTurn){
        this.player1 = player1;
        this.player2 = player2;
        this.playerTurn = playerTurn;

        table.add(new ArrayList<Minion>());
        table.add(new ArrayList<Minion>());
        table.add(new ArrayList<Minion>());
        table.add(new ArrayList<Minion>());
    }

    public void setTable(ArrayList<ArrayList<Minion>> table) {
        this.table = table;
    }

    public ArrayList<ArrayList<Minion>> getTable() {
        return table;
    }

    public void endTurn() {
        if(++turn > 2)
            nextRound();

        if(playerTurn == 1){
            playerTurn = 2;
        } else {
            playerTurn = 1;
        }
    }

    private void nextRound() {
        round++;
        turn = 1;

        int extraMana = Math.min(round, 10);

        getPlayer1().addMana(extraMana);
        getPlayer2().addMana(extraMana);

        getPlayer1().placeCardInHand();
        getPlayer2().placeCardInHand();
    }


    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setFrontRow(List<String> frontRow) {
        this.frontRow = frontRow;
    }

    public List<String> getFrontRow() {
        return frontRow;
    }

    public void setBackRow(List<String> backRow) {
        this.backRow = backRow;
    }

    public List<String> getBackRow() {
        return backRow;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public boolean isRowFull(int row) {
        return table.get(row).size() == 5;
    }
}

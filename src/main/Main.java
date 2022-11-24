package main;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.*;
import main.Card.Card;
import main.Card.Environment;
import main.Card.Hero;
import main.Card.Minion;
import main.Game.Game;
import main.Game.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        //TODO add here the entry point to your implementation

        List<String> minionList = Arrays.asList("The Ripper", "Miraj", "The Cursed One",
                "Disciple", "Sentinel", "Berserker", "Goliath", "Warden");
        List<String> environmentList = Arrays.asList("Firestorm",
                "Winterfell", "Heart Hound");

        DecksInput player1Decks = inputData.getPlayerOneDecks();
        DecksInput player2Decks = inputData.getPlayerTwoDecks();

        for (GameInput gameInput : inputData.getGames()) {
            int player1Idx = gameInput.getStartGame().getPlayerOneDeckIdx();
            int player2Idx = gameInput.getStartGame().getPlayerTwoDeckIdx();

            ArrayList<CardInput> player1Deck = player1Decks.getDecks().get(player1Idx);
            ArrayList<CardInput> player2Deck = player2Decks.getDecks().get(player2Idx);

            Random seed = new Random(gameInput.getStartGame().getShuffleSeed());
            Collections.shuffle(player1Deck, seed);
            Random seed2 = new Random(gameInput.getStartGame().getShuffleSeed());
            Collections.shuffle(player2Deck, seed2);

            ArrayList<Card> cards1 = new ArrayList<>();
            ArrayList<Card> cards2 = new ArrayList<>();

            for (CardInput cardInput : player1Deck) {
                if (minionList.contains(cardInput.getName())) {
                    cards1.add(new Minion(cardInput));
                }
                if (environmentList.contains(cardInput.getName())) {
                    cards1.add(new Environment(cardInput));
                }
            }

            for (CardInput cardInput : player2Deck) {
                if (minionList.contains(cardInput.getName())) {
                    cards2.add(new Minion(cardInput));
                }
                if (environmentList.contains(cardInput.getName())) {
                    cards2.add(new Environment(cardInput));
                }
            }

            Player player1 = new Player(cards1);
            Player player2 = new Player(cards2);

            player1.getCardsInHand().add(player1.getDeck().remove(0));
            player2.getCardsInHand().add(player2.getDeck().remove(0));

            player1.setHero(new Hero(gameInput.getStartGame().getPlayerOneHero()));
            player2.setHero(new Hero(gameInput.getStartGame().getPlayerTwoHero()));

            Game game = new Game(player1, player2, gameInput.getStartGame().getStartingPlayer());

            for (ActionsInput action : gameInput.getActions()) {
                switch (action.getCommand()) {
                    default : break;

                    case "getPlayerDeck":
                        if (action.getPlayerIdx() == 2) {
                            output.addObject()
                                    .put("command", action.getCommand())
                                    .put("playerIdx", action.getPlayerIdx())
                                    .putPOJO("output", player2.getDeck());
                        } else {
                            output.addObject()
                                    .put("command", action.getCommand())
                                    .put("playerIdx", action.getPlayerIdx())
                                    .putPOJO("output", player1.getDeck());
                        }
                        break;

                    case "getPlayerHero":
                        if (action.getPlayerIdx() == 2) {
                            output.addObject()
                                    .put("command", action.getCommand())
                                    .put("playerIdx", action.getPlayerIdx())
                                    .putPOJO("output", player2.getHero());
                            break;
                        } else {
                            output.addObject()
                                    .put("command", action.getCommand())
                                    .put("playerIdx", action.getPlayerIdx())
                                    .putPOJO("output", player1.getHero());
                        }
                        break;

                    case "getPlayerTurn":
                        output.addObject()
                                .put("command", action.getCommand())
                                .put("output", game.getCurrentPlayer());
                        break;

                case "endPlayerTurn":
                    game.endTurn();
                    game.removeDeadCardsFromTable();
                    game.getAttackCards().clear();

                    break;

                case "getCardsInHand":
                    ArrayList<Card> cardsCopy = new ArrayList<>();
                    if (action.getPlayerIdx() == 2) {
                        for (Card card : player2.getCardsInHand()) {
                            if (minionList.contains(card.getName())) {
                                cardsCopy.add(new Minion((Minion) card));
                            }
                            if (environmentList.contains(card.getName())) {
                                cardsCopy.add(new Environment((Environment) card));
                            }
                        }
                    }

                    if (action.getPlayerIdx() == 1) {
                        for (Card card : player1.getCardsInHand()) {
                            if (minionList.contains(card.getName())) {
                                cardsCopy.add(new Minion((Minion) card));
                            }
                            if (environmentList.contains(card.getName())) {
                                cardsCopy.add(new Environment((Environment) card));
                            }
                        }
                    }
                    output.addObject().put("command", action.getCommand())
                            .put("playerIdx", action.getPlayerIdx())
                            .putPOJO("output", cardsCopy);
                    break;

                case "getPlayerMana":
                    if (action.getPlayerIdx() == 2) {
                        output.addObject().put("command", action.getCommand())
                                .put("playerIdx", action.getPlayerIdx())
                                .putPOJO("output", player2.getMana());
                    } else if (action.getPlayerIdx() == 1) {
                        output.addObject().put("command", action.getCommand())
                                .put("playerIdx", action.getPlayerIdx())
                                .putPOJO("output", player1.getMana());
                    }
                    break;

                   case "placeCard" :
                       Player player = (game.getCurrentPlayer() == 1)
                               ? game.getPlayer1() : game.getPlayer2();

                       if (player.getCardsInHand().size() <= action.getHandIdx()) {
                           break;
                       }

                       Card card = player.getCardsInHand().get(action.getHandIdx());
                       int row = Card.getCardRow(card, game.getCurrentPlayer(), game);

                       if (!card.canBePlacedOnTable()) {
                           output.addObject().put("command", action.getCommand())
                                   .put("handIdx", action.getPlayerIdx())
                                   .putPOJO("error", "Cannot place environment card on table.");
                           break;

                       }

                       if (player.getMana() < card.getMana()) {
                           output.addObject().put("command", action.getCommand())
                                   .put("handIdx", action.getPlayerIdx())
                                   .putPOJO("error",
                                           "Not enough mana to place card on table.");
                           break;
                       }

                       if (game.isRowFull(row)) {
                           output.addObject().put("command", action.getCommand())
                                   .put("handIdx",
                                           action.getPlayerIdx())
                                   .putPOJO("error",
                                           "Cannot place card on table since row is full.");
                           break;

                       }

                       player.getCardsInHand().remove(action.getHandIdx());
                       player.removeMana(card.getMana());
                       game.getTable().get(row).add((Minion) card);

                       break;

                    case "getCardsOnTable":
                        output.addObject().put("command", action.getCommand())
                                .putPOJO("output", game.getTable());
                        break;

                    case "getEnvironmentCardsInHand":
                        ArrayList<Card> envCards = new ArrayList<>();

                        if (action.getPlayerIdx() == 1) {
                            for (Card envCard : player1.getCardsInHand()) {
                                if (environmentList.contains(envCard.getName())) {
                                    envCards.add(new Environment((Environment) envCard));
                                }
                            }
                        } else {
                            for (Card envCard : player2.getCardsInHand()) {
                                if (environmentList.contains(envCard.getName())) {
                                    envCards.add(new Environment((Environment) envCard));
                                }
                            }
                        }
                        output.addObject().put("command", action.getCommand())
                                .put("playerIdx", action.getPlayerIdx())
                                .putPOJO("output", envCards);
                        break;

                    case "getCardAtPosition":
                        Minion minion = game.getCardAtPosition(action.getX(), action.getY());
                        if (minion != null) {
                            output.addObject().put("command", action.getCommand())
                                    .putPOJO("output", new Minion(minion));
                        } else {
                            output.addObject().put("output", "No card available at that position.");
                        }
                        break;

                    case "useEnvironmentCard":
                        player = (game.getCurrentPlayer() == 1)
                                ? game.getPlayer1() : game.getPlayer2();
                        card = player.getCardsInHand().get(action.getHandIdx());

                        if (!environmentList.contains(card.getName())) {
                            output.addObject().put("command", action.getCommand())
                                    .put("handIdx", action.getHandIdx())
                                    .put("affectedRow", action.getAffectedRow())
                                    .putPOJO("error",
                                            "Chosen card is not of type environment.");
                            break;
                        }

                        if (player.getMana() < card.getMana()) {
                            output.addObject().put("command", action.getCommand())
                                    .put("handIdx", action.getHandIdx())
                                    .put("affectedRow", action.getAffectedRow())
                                    .putPOJO("error",
                                            "Not enough mana to use environment card.");
                            break;
                        }

                        if (game.isEnemyRow(action.getAffectedRow())) {
                            output.addObject().put("command", action.getCommand())
                                    .put("handIdx", action.getHandIdx())
                                    .put("affectedRow", action.getAffectedRow())
                                    .putPOJO("error",
                                            "Chosen row does not belong to the enemy.");
                            break;
                        }

                        if (card.getName().equals("Heart Hound")
                                && game.isRowFull(game.oppositeRow(action.getAffectedRow()))) {
                            output.addObject().put("command", action.getCommand())
                                    .put("handIdx", action.getHandIdx())
                                    .put("affectedRow", action.getAffectedRow())
                                    .putPOJO("error",
                                            "Cannot steal enemy card "
                                                    + "since the player's row is full.");
                            break;
                        }

                        Environment environment = (Environment) card;
                        if (environment.getName().equals("Firestorm")) {
                            environment.firestormAbility(game, action.getAffectedRow());
                        }
                        if (environment.getName().equals("Winterfell")) {
                            environment.winterfellAbility(game, action.getAffectedRow());
                        }
                        if (environment.getName().equals("Heart Hound")) {
                            environment.heartHoundAbility(game, action.getAffectedRow());
                        }
                        player.getCardsInHand().remove(card);
                        player.removeMana(card.getMana());
                        break;

                    case "getFrozenCardsOnTable":
                        ArrayList<Minion> cards = new ArrayList<>();

                        for (ArrayList<Minion> arrayListRow : game.getTable()) {
                            for (Minion min : arrayListRow) {
                                if (min.getFrozen()) {
                                    cards.add(min);
                                }
                            }
                        }
                        output.addObject().put("command", action.getCommand())
                                .putPOJO("output", cards);
                        break;

                    case "cardUsesAttack":
                        Coordinates attacker = action.getCardAttacker();
                        Coordinates defender = action.getCardAttacked();
                        minion = game.getCardAtPosition(attacker.getX(), attacker.getY());
                        Minion enemy = game.getCardAtPosition(defender.getX(), defender.getY());

                        if (minion == null) {
                            break;
                        }
                        if (game.isEnemyRow(defender.getX())) {
                            output.addObject().put("command", action.getCommand())
                                    .putPOJO("cardAttacker", attacker)
                                    .putPOJO("cardAttacked", defender)
                                    .put("error",
                                            "Attacked card does not belong to the enemy.");
                            break;
                        }
                        if (game.getAttackCards().contains(minion)) {
                            output.addObject().put("command", action.getCommand())
                                    .putPOJO("cardAttacker", attacker)
                                    .putPOJO("cardAttacked", defender)
                                    .put("error",
                                            "Attacker card has already attacked this turn.");
                            break;
                        }
                        if (minion.getFrozen()) {
                            output.addObject().put("command", action.getCommand())
                                    .putPOJO("cardAttacker", attacker)
                                    .putPOJO("cardAttacked", defender)
                                    .put("error",
                                            "Attacker card is frozen.");
                            break;
                        }
                        if (game.enemyHasTanks(minionList)) {
                            enemy = game.getCardAtPosition(defender.getX(), defender.getY());

                            if (enemy == null || !enemy.isTank(minionList)) {
                                output.addObject().put("command", action.getCommand())
                                        .putPOJO("cardAttacker", attacker)
                                        .putPOJO("cardAttacked", defender)
                                        .put("error",
                                                "Attacked card is not of type 'Tank'.");
                            }
                            break;
                        }
                        if (enemy == null) {
                            break;
                        }
                        minion.attackCard(enemy);
                        game.getAttackCards().add(minion);
                        break;
                }
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}

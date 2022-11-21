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
import main.Game.Action;
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

        List<String> minionList = Arrays.asList("The Ripper", "Miraj", "The Cursed One", "Disciple", "Sentinel", "Berserker", "Goliath", "Warden");
        List<String> environmentList = Arrays.asList("Firestorm", "Winterfell", "Heart Hound");


//        ArrayList<Card> cardsInHand1 = new ArrayList<>();
//        ArrayList<Card> cardsInHand2 = new ArrayList<>();
//
//        cardsInHand1.add(player1.getDeck().remove(0));
//        cardsInHand2.add(player2.getDeck().remove(0));

      //  Game game = new Game(player1, player2);
//        game.nextRound();
//        System.out.println(player1.getCardsInHand());

//        player1.PlaceCardInHand(player1.getDeck());
//        System.out.println(player1.getCardsInHand());

//        ArrayList<Card> hand = new ArrayList<>();
//        hand.add(player1.getDeck().remove(0));
//        System.out.println(player1.getDeck().get(0).name);

        DecksInput player1Decks = inputData.getPlayerOneDecks();
        DecksInput player2Decks = inputData.getPlayerTwoDecks();

        for(GameInput gameInput : inputData.getGames()){
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

            for(CardInput cardInput : player1Deck){
                if(minionList.contains(cardInput.getName())) cards1.add(new Minion(cardInput));
                if(environmentList.contains(cardInput.getName())) cards1.add(new Environment(cardInput));
            }

            for(CardInput cardInput : player2Deck){
                if(minionList.contains(cardInput.getName())) cards2.add(new Minion(cardInput));
                if(environmentList.contains(cardInput.getName())) cards2.add(new Environment(cardInput));
            }

            Player player1 = new Player(cards1);
            Player player2 = new Player(cards2);

            player1.getCardsInHand().add(player1.getDeck().remove(0));
            player2.getCardsInHand().add(player2.getDeck().remove(0));

            player1.setHero(new Hero(gameInput.getStartGame().getPlayerOneHero()));
            player2.setHero(new Hero(gameInput.getStartGame().getPlayerTwoHero()));

            Game game = new Game(player1, player2, gameInput.getStartGame().getStartingPlayer());

            for(ActionsInput action : gameInput.getActions()){
                switch (action.getCommand()) {
                    case "getPlayerDeck":
                        if(action.getPlayerIdx() == 2){
                            output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", player2.getDeck());
                            break;
                        }
                        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", player1.getDeck());
                        break;
                    case "getPlayerHero":
                        if(action.getPlayerIdx() == 2) {
                            output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", player2.getHero());
                            break;
                        }
                        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", player1.getHero());
                        break;
                    case "getPlayerTurn":
                        output.addObject().put("command", action.getCommand()).put("output", game.getPlayerTurn());
                        break;
                case "endPlayerTurn":
                    game.endTurn();
                    break;
                case "getCardsInHand":
                    ArrayList<Card> cardsCopy = new ArrayList<>();

                    if(action.getPlayerIdx() == 2){
                        for(Card card : player2.getCardsInHand()){
                            if(minionList.contains(card.name)) cardsCopy.add(new Minion((Minion) card));
                            if(environmentList.contains(card.name)) cardsCopy.add(new Environment((Environment) card));
                        }
                    }

                    if(action.getPlayerIdx() == 1){
                        for(Card card : player1.getCardsInHand()){
                            if(minionList.contains(card.name)) cardsCopy.add(new Minion((Minion) card));
                            if(environmentList.contains(card.name)) cardsCopy.add(new Environment((Environment) card));
                        }
                    }
                    output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", cardsCopy);
                    break;
                case "getPlayerMana":
                    if(action.getPlayerIdx() == 2){
                        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", player2.getMana());
                    } else if (action.getPlayerIdx() == 1) {
                        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", player1.getMana());
                    }
                    break;
                   case "placeCard" :
                       Player player = (game.getPlayerTurn() == 1)? game.getPlayer1() : game.getPlayer2();
                       System.out.println(action.getCommand() + " " + action.getHandIdx());

                       if (player.getCardsInHand().size() <= action.getHandIdx())
                           break;

                       Card card = player.getCardsInHand().get(action.getHandIdx());

                       int row = Card.getCardRow(card, game.getPlayerTurn(), game);

                       boolean error = false;
                       String msg = null;
                       if (!card.canBePlacedOnTable()) {
                           error = true;
                           msg = "Cannot place environment card on table.";
                       }

                       if (player.getMana() < card.mana) {
                           error = true;
                           msg = "Not enough mana to place card on table.";
                       }

                       if (game.isRowFull(row)) {
                           error = true;
                           msg = "Cannot place card on table since row is full.";
                       }

                       if(!error){
                           player.getCardsInHand().remove(action.getHandIdx());
                           player.removeMana(card.mana);
                           game.getTable().get(row).add((Minion) card);
                       }
                       else{
                           output.addObject().put("command", action.getCommand()).put("handIdx", action.getPlayerIdx()).putPOJO("error", msg);
                       }
                       break;

                    case "getCardsOnTable":
                        output.addObject().put("command", action.getCommand()).putPOJO("output", game.getTable());

                }
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}

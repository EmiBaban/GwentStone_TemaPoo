package main;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.Input;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

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

        ArrayList<String> minionList = new ArrayList<>();
        ArrayList<String> environmentList = new ArrayList<>();

        minionList.add("The Ripper");
        minionList.add("Miraj");
        minionList.add("The Cursed One");
        minionList.add("Disciple");
        minionList.add("Sentinel");
        minionList.add("Berseker");
        minionList.add("Goliath");
        minionList.add("Warden");
        environmentList.add("Firestorm");
        environmentList.add("Winterfell");
        environmentList.add("Hear Hound");

        DecksInput player1Decks = inputData.getPlayerOneDecks();
        DecksInput player2Decks = inputData.getPlayerTwoDecks();

        int player1Idx = inputData.getGames().get(0).getStartGame().getPlayerOneDeckIdx();
        int player2Idx = inputData.getGames().get(0).getStartGame().getPlayerTwoDeckIdx();

        ArrayList<CardInput> player1Deck = player1Decks.getDecks().get(player1Idx);
        ArrayList<CardInput> player2Deck = player2Decks.getDecks().get(player2Idx);

        Random seed = new Random(inputData.getGames().get(0).getStartGame().getShuffleSeed());
        Collections.shuffle(player1Deck, seed);
        Random seed2 = new Random(inputData.getGames().get(0).getStartGame().getShuffleSeed());
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

        cards1.remove(0);
        cards2.remove(0);

        Hero hero1 = new Hero(inputData.getGames().get(0).getStartGame().getPlayerOneHero());
        Hero hero2 = new Hero(inputData.getGames().get(0).getStartGame().getPlayerTwoHero());

        ArrayList<ActionsInput> actiuni = inputData.getGames().get(0).getActions();

        int firstPlayer = inputData.getGames().get(0).getStartGame().getStartingPlayer();

        for(ActionsInput action : actiuni){
            switch (action.getCommand()) {
                case "getPlayerDeck":
                    if(action.getPlayerIdx() == 2){
                        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", cards2);
                        break;
                    }
                    output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", cards1);
                    break;
                case "getPlayerHero":
                    if(action.getPlayerIdx() == 2) {
                        output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", hero2);
                        break;
                    }
                    output.addObject().put("command", action.getCommand()).put("playerIdx", action.getPlayerIdx()).putPOJO("output", hero1);
                    break;
                case "getPlayerTurn":
                    output.addObject().put("command", action.getCommand()).put("output", firstPlayer);
                    break;
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}

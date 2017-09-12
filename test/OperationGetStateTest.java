import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import freecell.model.Card;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Test for getGameState method of Operation Method.
 */
public class OperationGetStateTest {

  @Test
  public void testStateInitial() {
    FreecellOperations<Card> newModel = new FreecellModel();
    assertEquals("", newModel.getGameState());
  }

  /**
   * Testing start new game with Cascade pile number 4-52, Operation pile number 1-52.
   * Simulated round robin style card dealing with pre-defined suit order.
   */
  @Test
  public void testStateNewGameDealCorrect_ShuffleOff() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();

    for (int cPile = 4; cPile < 53; cPile++) {
      for (int oPile = 1; oPile < 53; oPile++) {
        newModel.startGame(testDeck, cPile, oPile, false);

        //check that actual is as expected!
        String[] simulated = helperDealCard(cPile, oPile).split("\n");
        String[] actual = newModel.getGameState().split("\n");
        for (int i = 0; i < simulated.length; i++) {
          assertEquals(simulated[i], actual[i]);
        }
      }
    }
  }

  /**
   * Start invalid doesn't effect game state.
   */
  @Test
  public void testStartDoesNotChangeAfterInvalid() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 52, 1, false);

    //check that actual is as expected!
    String[] simulated = helperDealCard(52, 1).split("\n");
    String[] actual = newModel.getGameState().split("\n");
    for (int i = 0; i < simulated.length; i++) {
      assertEquals(simulated[i], actual[i]);
    }

    //invalid start game
    try {
      newModel.startGame(testDeck, 3, 1, false);
    } catch (IllegalArgumentException e) {
      //catches exception
    }

    //check that actual is not changed
    for (int i = 0; i < simulated.length; i++) {
      assertEquals(simulated[i], actual[i]);
    }
  }

  /**
   * Testing start new game with shuffle, Cascade pile number 4-52, Operation pile number 1-52.
   * Check that there is no duplicates, and no missing cards in the result piles.
   */
  @Test
  public void testStateNewGameDealCorrect_ShuffleOn() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();

    for (int cPile = 4; cPile < 53; cPile++) {
      for (int oPile = 1; oPile < 10; oPile++) {
        newModel.startGame(testDeck, cPile, oPile, true);

        //check that actual is as expected!
        String[] actual = newModel.getGameState().split("\n");
        List<String> actualList = Arrays.asList(actual);

        List<String> actualCascadePile = new ArrayList<>();
        for (int i = 0; i < actualList.size(); i++) {
          if (actualList.get(i).substring(0, 1).equals("C")) {
            actualCascadePile.add(actualList.get(i));
          }
        }
        /*for(String i: actualCascadePile){
          System.out.println(i);
        }*/

        List<String> seperatePile = new ArrayList<>();
        for (int i = 0; i < cPile; i++) {
          String s = "C" + String.valueOf(i + 1) + ":";
          //System.out.println(s.length());
          seperatePile.add(actualCascadePile.get(i).substring(s.length()));
        }

        /*
        for(String i: seperatePile){
          System.out.println(i);
        }*/

        //clean cards
        List<String> shuffledCards = new ArrayList<>();
        for (String i : seperatePile) {
          shuffledCards.addAll(Arrays.asList(i.replace(" ", "").split(",")));
        }

        /*
        for (String i : shuffledCards) {
          System.out.println(i);
        }*/

        //check there are same number of cascade piles
        assertEquals(52, shuffledCards.size());

        //check no duplicates, by asserting that every card in a simulated deck
        //exists in the shuffled card deck
        String[] simulatedDeck = helperDeck();
        for (int i = 0; i < 52; i++) {
          assertTrue(shuffledCards.contains(simulatedDeck[i]));
        }
      }
    }
  }


  /**
   * Testing mid game, even with illegal move, getGameState Method works correctly.
   */
  @Test
  public void testStateMidGame() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 52, 52, false);

    //move all cards from cascade pile to open pile
    for (int i = 0; i < 52; i++) {
      newModel.move(PileType.CASCADE, i, 0, PileType.OPEN, i);
    }

    //Test gameState not Effected
    String[] gameState = newModel.getGameState().split("\n");
    String[] deckCard = helperDeck();
    String expected;

    for (int i = 1; i <= 4; i++) {
      expected = "F" + i + ":";
      assertEquals(expected, gameState[i - 1]);
    }
    for (int i = 5; i <= (5 + 51); i++) {
      expected = "O" + (i - 4) + ": " + deckCard[i - 5];
      assertEquals(expected, gameState[i - 1]);
    }
    for (int i = 57; i <= 57 + 51; i++) {
      expected = "C" + (i - 56) + ":";
      assertEquals(expected, gameState[i - 1]);
    }

    // try illegal move
    try {
      newModel.move(PileType.CASCADE, 0, 1, PileType.OPEN, 1);
    } catch (IllegalArgumentException e) {
      //catch exception
    }

    //check state does not change after illegal move.
    for (int i = 1; i <= 4; i++) {
      expected = "F" + i + ":";
      assertEquals(expected, gameState[i - 1]);
    }
    for (int i = 5; i <= (5 + 51); i++) {
      expected = "O" + (i - 4) + ": " + deckCard[i - 5];
      assertEquals(expected, gameState[i - 1]);
    }
    for (int i = 57; i <= 57 + 51; i++) {
      expected = "C" + (i - 56) + ":";
      assertEquals(expected, gameState[i - 1]);
    }

  }


  /**
   * Testing end game state, and checks that game state does not change after illegal moves.
   */
  @Test
  public void testStateGame_EndGame() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 52, 4, false);

    //move all cards from cascade pile to foundation pile
    for (int j = 0; j < 4; j++) {
      for (int i = 0; i < 13; i++) {
        newModel.move(PileType.CASCADE, (i + 13 * j), 0, PileType.FOUNDATION, j);
      }
    }
    assertTrue(newModel.isGameOver());

    //check that actual is as expected!
    String[] simulated = helperEndGameCard(52, 4).split("\n");
    String[] actual = newModel.getGameState().split("\n");
    for (int i = 0; i < simulated.length; i++) {
      assertEquals(simulated[i], actual[i]);
    }

    // try illegal move
    try {
      newModel.move(PileType.CASCADE, 0, 1, PileType.OPEN, 1);
    } catch (IllegalArgumentException e) {
      //catch exception
    }

    //game state does not change with illegal move
    for (int i = 0; i < simulated.length; i++) {
      assertEquals(simulated[i], actual[i]);
    }
  }

  /**
   * Helper function for initiating a string of card values in string.
   */
  private String[] helperDeck() {
    //forming a deck
    String[] suits = {"♦", "♣", "♥", "♠"};
    String[] val = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    String[] deckString = new String[52];
    for (int j = 0; j <= 3; j++) {
      for (int i = 0; i <= 12; i++) {
        deckString[i + j * 13] = val[i] + suits[j];
      }
    }
    return deckString;
  }

  /**
   * Helper function simulating the correct result for dealing card.
   */
  private String helperDealCard(int numCascadePile, int numOpenPile) {

    String[] deckString = helperDeck();

    //initiating game
    List<String> result = new ArrayList<>();
    for (int i = 1; i <= 4; i++) {
      result.add("F" + i + ":" + "\n");
    }
    for (int i = 1; i <= numOpenPile; i++) {
      result.add("O" + i + ":" + "\n");
    }

    //deal 52 cards round robin style:
    String[] casResult = new String[numCascadePile];
    Arrays.fill(casResult, "");
    int j = 0;
    int count = 0;
    while (j < 52) {
      for (int i = 0; i < numCascadePile; i++) {
        //System.out.println(j+"-"+i+":"+count+"="+ (i + numCascadePile * count));
        String card = deckString[i + numCascadePile * count];
        casResult[j % numCascadePile] += ", " + card;
        j++;

        if (j == 52) {
          break;
        }
      }
      count++;
    }

    //format and return
    for (int i = 0; i < numCascadePile; i++) {
      String r = "C" + (i + 1) + ":" + casResult[i].substring(1) + "\n";
      result.add(r);
    }
    String toReturn = "";
    for (String s : result) {
      toReturn = toReturn + s;
    }
    return toReturn;
  }

  /**
   * Helper function simulating the correct result for the end game pile.
   */
  private String helperEndGameCard(int numCascadePile, int numOpenPile) {

    String[] deckString = helperDeck();
    String diamond = "";
    String club = "";
    String heart = "";
    String spade = "";
    for (int i = 0; i < 13; i++) {
      diamond += ", " + deckString[i];
      club += ", " + deckString[i + 13];
      heart += ", " + deckString[i + 13 * 2];
      spade += ", " + deckString[i + 13 * 3];
    }

    List<String> result = new ArrayList<>();
    //add result, remove ", " from the beginning
    result.add("F1: " + diamond.substring(2) + "\n");
    result.add("F2: " + club.substring(2) + "\n");
    result.add("F3: " + heart.substring(2) + "\n");
    result.add("F4: " + spade.substring(2) + "\n");

    for (int i = 1; i <= numOpenPile; i++) {
      result.add("O" + i + ":" + "\n");
    }
    //format and return
    for (int i = 1; i <= numCascadePile; i++) {
      String r = "C" + i + ":\n";
      result.add(r);
    }
    String toReturn = "";
    for (String s : result) {
      toReturn = toReturn + s;
    }
    return toReturn;
  }

}

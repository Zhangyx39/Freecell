import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import freecell.model.Card;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test for GetDeck in Operation Class.
 * Check for valid deck.
 * Check deck does not change when game start shuffle/ no shuffle.
 * Check deck does not change when game mid shuffle/ no shuffle.
 * Check deck does not change when game over no shuffle.
 */
public class OperationGetDeckTest {

  /**
   * The getDeck method return a valid deck of cards.
   * A deck is valid if it has 52 cars, no duplicates,
   * and each card has value between 1-13, of suit [HEART,DIAMONDS,ClUB,SPADE].
   */
  @Test
  public void testValidDeck() {
    List<String> validSuitString = new ArrayList<>();
    validSuitString.add("HEART");
    validSuitString.add("DIAMOND");
    validSuitString.add("CLUB");
    validSuitString.add("SPADE");

    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();

    assertTrue(testDeck.size() == 52);
    for (Card i : testDeck) {
      //check value
      boolean validValue = i.getValue() < 14 && i.getValue() > 0;

      //check suit
      String suit = i.getSuit().toString();
      boolean validSuilt = validSuitString.contains(suit);

      assertTrue(validSuilt && validValue);
    }

    //check for duplicates
    Set<Card> set = new HashSet<>();
    for (Card c : testDeck) {
      assertTrue(set.add(c));
    }
  }

  /**
   * Testing for deck does not change when game start without shuffle.
   */
  @Test
  public void testDeckUnchanged_GameStart_NoShuffle() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> deck0 = newModel.getDeck();
    String originalDeck = "";
    for (Card i : deck0) {
      originalDeck += i.toString();
    }

    //after dealing cards
    newModel.startGame(deck0, 4, 2, false);
    String newDeck = "";
    for (Card i : deck0) {
      newDeck += i.toString();
    }
    //check the original testDeck is still a valid deck
    assertEquals(originalDeck, newDeck);
  }

  /**
   * Testing for deck does not change when game start with shuffle.
   */
  @Test
  public void testDeckUnchanged_GameStart_Shuffle() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> deck0 = newModel.getDeck();
    String originalDeck = "";
    for (Card i : deck0) {
      originalDeck += i.toString();
    }

    //after dealing cards
    newModel.startGame(deck0, 4, 2, true);
    String newDeck = "";
    for (Card i : deck0) {
      newDeck += i.toString();
    }
    //check the original testDeck is still a valid deck
    assertEquals(originalDeck, newDeck);
  }

  /**
   * Testing for deck does not change when mid game move with shuffle.
   */
  @Test
  public void testDeckUnchanged_GameMid_Shuffle() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    String originalDeck = "";
    for (Card i : testDeck) {
      originalDeck += i.toString();
    }

    //move from cascade pile to Open pile
    newModel.startGame(testDeck, 52, 52, true);
    try {
      for (int i = 0; i < 52; i++) {
        newModel.move(PileType.CASCADE, i, 0, PileType.OPEN, i);
      }
    } catch (IllegalArgumentException e) {
      fail();
    }

    //after moving cards
    String newDeck = "";
    for (Card i : testDeck) {
      newDeck += i.toString();
    }
    //check the original testDeck is still a valid deck
    assertEquals(originalDeck, newDeck);
  }

  /**
   * Testing for deck does not change when mid game move without shuffle.
   */
  @Test
  public void testDeckUnchanged_GameMid_NoShuffle() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    String originalDeck = "";
    for (Card i : testDeck) {
      originalDeck += i.toString();
    }

    //move from cascade pile to Open pile
    newModel.startGame(testDeck, 52, 52, false);
    try {
      for (int i = 0; i < 52; i++) {
        newModel.move(PileType.CASCADE, i, 0, PileType.OPEN, i);
      }
    } catch (IllegalArgumentException e) {
      fail();
    }

    //after moving cards
    String newDeck = "";
    for (Card i : testDeck) {
      newDeck += i.toString();
    }
    //check the original testDeck is still a valid deck
    assertEquals(originalDeck, newDeck);
  }

  /**
   * Testing for deck does not change when game over without shuffle.
   */
  @Test
  public void testDeckUnchanged_GameOver_NoShuffle() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    String originalDeck = "";
    for (Card i : testDeck) {
      originalDeck += i.toString();
    }

    newModel.startGame(testDeck, 52, 4, false);

    //move all cards from cascade pile to foundation pile
    for (int j = 0; j < 4; j++) {
      for (int i = 0; i < 13; i++) {
        newModel.move(PileType.CASCADE, (i + 13 * j), 0, PileType.FOUNDATION, j);
      }
    }
    TestCase.assertTrue(newModel.isGameOver());

    //after game over
    String newDeck = "";
    for (Card i : testDeck) {
      newDeck += i.toString();
    }
    //check the original testDeck is still a valid deck
    assertEquals(originalDeck, newDeck);
  }

}

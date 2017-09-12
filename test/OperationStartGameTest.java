import org.junit.Test;

import java.util.List;

import freecell.model.Card;
import freecell.model.CardImpl;
import freecell.model.Deck;
import freecell.model.DeckImpl;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.Suit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Testing for StartGame method in Operation Class.
 */
public class OperationStartGameTest {

  /**
   * Testing startGame method with valid and invalid input:
   * Test includes:
   * startGame_Success : adding successfully with shuffle/ no shuffle valid deck, and pile number.
   * startGame_InvalidDeck_NumberNot52 : failed with deck of not 52 cards.
   * startGame_InvalidDeck_ContainDuplicates : failed with deck contains duplicates.
   * startGame_InvalidCascadePileNum:failed with cascade number < 4.
   * startGame_InvalidOpenPilesNum: open pile more than <1.
   */
  @Test
  public void startGame_Success() {
    for (int cascadeNum = 4; cascadeNum < 20; cascadeNum++) {
      for (int openPileNum = 1; openPileNum < 20; openPileNum++) {
        FreecellOperations<Card> newModel = new FreecellModel();
        List<Card> testDeck = newModel.getDeck();
        //random shuffle or no shuffle
        boolean shuffle = Math.random() < 0.5;
        try {
          newModel.startGame(testDeck, cascadeNum, openPileNum, shuffle);
        } catch (IllegalArgumentException e) {
          fail();
        }
      }
    }
  }

  /**
   * Testing deck does not change when game start.
   */
  @Test
  public void unChangeDeck() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 4, 2, true);

    //check the original testDeck is still a valid deck
    Deck original = new DeckImpl(testDeck);
    assertTrue(original.isValidDeck());
  }

  /**
   * Testing startGame method with invalid Deck less than 52.
   *
   * @throws IllegalArgumentException Not a valid deck
   */
  @Test(expected = IllegalArgumentException.class)
  public void startGame_InvalidDeck_NumberNot52() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    testDeck.remove(0);
    testDeck.remove(1);

    newModel.startGame(testDeck, 4, 2, true);
  }

  /**
   * Testing startGame method with invalid Deck duplicates.
   *
   * @throws IllegalArgumentException Not a valid deck
   */
  @Test(expected = IllegalArgumentException.class)
  public void startGame_InvalidDeck_ContainDuplicates() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    testDeck.remove(0);
    testDeck.remove(1);
    testDeck.add(new CardImpl(1, Suit.HEART));
    testDeck.add(new CardImpl(1, Suit.HEART));

    newModel.startGame(testDeck, 4, 2, true);
  }

  /**
   * Testing startGame method with invalid Deck null.
   *
   * @throws IllegalArgumentException Not a valid deck
   */
  @Test(expected = IllegalArgumentException.class)
  public void startGame_InvalidDeck_NullInput() {
    FreecellOperations<Card> newModel = new FreecellModel();

    newModel.startGame(null, 4, 2, true);
  }

  /**
   * Testing startGame method with invalid number of Cascade Piles.
   *
   * @throws IllegalArgumentException if Cascade pile is less than less than 4.
   */
  @Test(expected = IllegalArgumentException.class)
  public void startGame_InvalidCascadePileNum() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 3, 2, true);
  }

  /**
   * Testing startGame method with invalid number of Open Piles.
   *
   * @throws IllegalArgumentException if Open pile is less than less than 1.
   */
  @Test(expected = IllegalArgumentException.class)
  public void startGame_InvalidOpenPilesNum() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 6, 0, true);
  }

}

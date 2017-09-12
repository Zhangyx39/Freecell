import org.junit.Before;
import org.junit.Test;

import java.util.List;

import freecell.model.Card;
import freecell.model.Deck;
import freecell.model.DeckImpl;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test for move method in Operation Class.
 */
public class OperationMoveTest {

  private FreecellOperations<Card> cascadeModel = new FreecellModel();
  private FreecellOperations<Card> openModel = new FreecellModel();
  private FreecellOperations<Card> foundationModel = new FreecellModel();

  /**
   * cascadeModel: 52 cards in cascade pile, to be moved to open and foundation pile.
   * openModel: 52 cards in open pile, to be moved to cascade and foundation pile.
   * foundationModel: 52 cards in foundation pile, to be moved to open and cascade pile.
   */
  @Before
  public void setUp() {
    List<Card> testDeck = cascadeModel.getDeck();
    cascadeModel.startGame(testDeck, 52, 52, false);

    openModel.startGame(testDeck, 52, 52, false);
    try {
      for (int i = 0; i < 52; i++) {
        openModel.move(PileType.CASCADE, i, 0, PileType.OPEN, i);
      }
    } catch (IllegalArgumentException e) {
      fail();
    }

    foundationModel.startGame(testDeck, 52, 52, false);
    try {
      for (int j = 0; j < 4; j++) {
        for (int i = 0; i < 13; i++) {
          foundationModel.move(PileType.CASCADE, (i + 13 * j), 0, PileType.FOUNDATION, j);
        }
      }
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  /**
   * Testing Move method with valid and invalid input:
   * Test includes:
   * Move_Success :
   * Move_InvalidSourcePileNumber: if pileNumber is out of bounds.
   * Move_InvalidCardIndex: if index is out of bounds.
   * Move_InvalidDestinationNumber: if destNum is out of bounds for destPile.
   */
  @Test
  public void Move_Success_CascadePile_OPen() {
    //move from Cascade pile to Open pile
    try {
      for (int i = 0; i < 52; i++) {
        cascadeModel.move(PileType.CASCADE, i, 0, PileType.OPEN, i);
      }
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  // Move from Cascade Pile to Foundation Pile
  @Test
  public void Move_Success_CascadePile_Foundation() {
    try {
      for (int j = 0; j <= 3; j++) {
        for (int i = 0; i <= 12; i++) {
          cascadeModel.move(PileType.CASCADE, (i + 13 * j), 0, PileType.FOUNDATION, j);
        }
      }
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  // Move from Open Pile to Cascade Pile
  @Test
  public void Move_Success_Open_toCascade() {
    try {
      for (int i = 0; i < 52; i++) {
        openModel.move(PileType.OPEN, i, 0, PileType.CASCADE, i);
      }
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  // Move from Open Pile to Foundation Pile
  @Test
  public void Move_Success_Open_toFoundation() {
    try {
      for (int j = 0; j <= 3; j++) {
        for (int i = 0; i <= 12; i++) {
          openModel.move(PileType.OPEN, (i + 13 * j), 0, PileType.FOUNDATION, j);
        }
      }
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  // Move from Foundation Pile to Open Pile
  // Foundation topmost index start from 12 down to 0!
  @Test
  public void Move_Success_Foundation_toOpen() {
    try {
      for (int j = 0; j < 4; j++) {
        for (int i = 12; i > 0; i--) {
          foundationModel.move(PileType.FOUNDATION, j, i, PileType.OPEN, (i + 13 * j));
        }
      }
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  // Move from Foundation Pile to Cascade Pile
  // Foundation topmost index start from 12 down to 0!
  @Test
  public void Move_Success_Foundation_toCascade() {
    try {
      for (int j = 0; j < 4; j++) {
        for (int i = 12; i > 0; i--) {
          foundationModel.move(PileType.FOUNDATION, j, i, PileType.CASCADE, (i + 13 * j));
        }
      }
    } catch (IllegalArgumentException e) {
      fail();
    }
  }


  /**
   * Testing move method with invalid Source Pile Number.
   *
   * @throws IllegalArgumentException Pile does not exist
   */
  // for cascade as source pile
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidSourcePileNumber_Cascade() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 4, 2, true);
    newModel.move(PileType.CASCADE, 5, 6, PileType.CASCADE, 2);
  }

  //for foundation as source pile
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidSourcePileNumber_Foundation() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 4, 2, true);
    newModel.move(PileType.FOUNDATION, 5, 6, PileType.CASCADE, 2);
  }

  //for Open as source pile
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidSourcePileNumber_Open() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 4, 2, true);
    newModel.move(PileType.OPEN, 5, 6, PileType.CASCADE, 2);
  }

  /**
   * Testing move method with invalid Destination Pile Number.
   *
   * @throws IllegalArgumentException Pile does not exist
   */
  // for cascade as source pile
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidDestPileNumber_Cascade() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 53, 2, true);
    newModel.move(PileType.CASCADE, 1, 6, PileType.CASCADE, 55);
  }

  //for foundation as source pile
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidDestPileNumber_Foundation() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 52, 2, true);
    newModel.move(PileType.CASCADE, 1, 6, PileType.FOUNDATION, 5);
  }

  //for Open as source pile
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidDestPileNumber_Open() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 4, 2, true);
    newModel.move(PileType.CASCADE, 1, 6, PileType.OPEN, 5);
  }

  /**
   * Testing move method with null input.
   *
   * @throws IllegalArgumentException Wrong source type: null
   */
  // for source type
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidSource_null() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 4, 2, true);
    newModel.move(null, 5, 6, PileType.CASCADE, 2);
  }

  // for destination type
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidDestination_null() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 4, 2, true);
    newModel.move(PileType.CASCADE, 5, 6, null, 2);
  }

  /**
   * Testing with invalid index.
   */
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidIndex() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 4, 2, true);
    newModel.move(PileType.CASCADE, 1, 2, PileType.OPEN, 5);
  }

  /**
   * Testing with invalid from Cascade to Cascade.
   */
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidMoveCascadeToCascade() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 52, 2, true);
    newModel.move(PileType.CASCADE, 1, 0, PileType.CASCADE, 2);
  }

  /**
   * Testing with invalid from Cascade to Open.
   */
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidMoveCascadeToOpen() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 52, 2, true);
    newModel.move(PileType.CASCADE, 1, 0, PileType.OPEN, 2);
  }

  /**
   * Testing with invalid move Open to Cascade.
   */
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidMoveOpenToCascade() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 52, 2, false);
    //move 1 Ace card from cascade to open
    newModel.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    //move 1 Ace card from open to cascade
    newModel.move(PileType.OPEN, 0, 0, PileType.CASCADE, 1);
  }

  /**
   * Testing with invalid from Cascade to Open.
   */
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidMoveOpenToFoundation() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 52, 2, false);
    //move a non-Ace card from cascade to open
    newModel.move(PileType.CASCADE, 1, 0, PileType.OPEN, 0);
    //move a non-Ace card from open to Foundation
    newModel.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 1);
  }


  /**
   * Testing with invalid move Foundation to Cascade.
   */
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidMoveFoundToCascade() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 52, 2, false);

    //move 1 Ace card from cascade to foundation
    newModel.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);

    //move 1 Ace card from Foundation to cascade
    newModel.move(PileType.FOUNDATION, 0, 0, PileType.CASCADE, 1);
  }

  /**
   * Testing with invalid from Foundation to Open.
   */
  @Test(expected = IllegalArgumentException.class)
  public void Move_InvalidMoveFoundToOpen() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 52, 2, false);
    //move 1 Ace card from cascade to foundation
    newModel.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);

    //move 1 card from cascade to open
    newModel.move(PileType.CASCADE, 1, 0, PileType.OPEN, 0);

    //move 1 Ace card from Foundation to Open
    newModel.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 0);
  }


  /**
   * Testing deck does not change with game playing.
   */
  @Test
  public void unChangeDeck() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();

    //move from cascade pile to Open pile
    newModel.startGame(testDeck, 52, 52, false);
    try {
      for (int i = 0; i < 52; i++) {
        newModel.move(PileType.CASCADE, i, 0, PileType.OPEN, i);
      }
    } catch (IllegalArgumentException e) {
      fail();
    }

    //check the original testDeck is still a valid deck
    Deck original = new DeckImpl(testDeck);
    assertTrue(original.isValidDeck());
  }
}

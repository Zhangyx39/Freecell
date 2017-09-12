import org.junit.Test;

import java.util.List;

import freecell.model.Card;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Class for testing the IsGaveOver Method of operations.
 */
public class OperationIsGameOverTest {

  /**
   * When model has just been initiated, but not started, game over is false.
   */
  @Test
  public void testGameOver_GameInitial() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();

    assertFalse(newModel.isGameOver());
    try {
      newModel.startGame(testDeck, 52, 3, false);
    } catch (IllegalArgumentException e) {
      //catch illegal start game.
    }

    //check that illegal game start will not change game state.
    assertFalse(newModel.isGameOver());
  }

  /**
   * When game just started, game over is false.
   * Check illegal start game will not change game state.
   */
  @Test
  public void testGameOver_GameStart() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 52, 4, false);
    assertFalse(newModel.isGameOver());

    try {
      newModel.startGame(testDeck, 52, 3, false);
    } catch (IllegalArgumentException e) {
      //catch illegal start game.
    }

    //check that illegal game start will not change game state.
    assertFalse(newModel.isGameOver());
  }

  /**
   * When game is playing, game over is false.
   * Check illegal move does not change game state.
   */
  @Test
  public void testGameOver_GameMid() {
    FreecellOperations<Card> newModel = new FreecellModel();
    List<Card> testDeck = newModel.getDeck();
    newModel.startGame(testDeck, 52, 52, false);

    //move from cascade pile to open pile
    for (int i = 0; i < 52; i++) {
      newModel.move(PileType.CASCADE, i, 0, PileType.OPEN, i);
      assertFalse(newModel.isGameOver());
    }

    //move from open pile to cascade pile
    for (int i = 0; i < 52; i++) {
      newModel.move(PileType.OPEN, i, 0, PileType.CASCADE, i);
      assertFalse(newModel.isGameOver());
    }
    //move from cascade pile to foundation pile
    for (int i = 0; i < 13; i++) {
      newModel.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, 1);
      assertFalse(newModel.isGameOver());
    }

    try {
      newModel.move(PileType.CASCADE, 1, 0, PileType.FOUNDATION, 1);
    } catch (IllegalArgumentException e) {
      //catch illegal move
    }

    //check game state does not change after illegal move
    assertFalse(newModel.isGameOver());

  }

  /**
   * Check that when all card is placed into the Foundation Pile, game over.
   * Check that illegal move will not change the game state.
   */
  @Test
  public void testGameOver_GameEnd() {
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

    try {
      newModel.move(PileType.CASCADE, 1, 0, PileType.FOUNDATION, 1);
    } catch (IllegalArgumentException e) {
      //catch illegal move
    }

    //check game state does not change after illegal move
    assertTrue(newModel.isGameOver());
  }
}

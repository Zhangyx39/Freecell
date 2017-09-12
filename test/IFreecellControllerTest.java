import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import freecell.controller.FreecellController;
import freecell.controller.IFreecellController;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the IFreecellController.
 */
public class IFreecellControllerTest {

  /*
  Table of Contents

  Null input:
      testNullReadable
      testNullAppendable
      testNullDeck
      testNullModel

  Illegal input for playGame:
      testInvalidDeckDuplicate
      testInvalidDeckNotComplete
      testInvalidDeckEmpty
      testInvalidDeckOtherObject
      testInvalidNumberOfCascadePiles
      testInvalidNumberOfOpenPiles

  PlayGame correctly:
      testPlayGameAgainAfterIllegalInput
      testPlayTwiceNoShuffle
      testShuffleCorrectly
      testPlayTwice
      testPlayTwiceDifferentDeckAndModel
      testModelMidGame
      testModelGameOver

  Quit game:
      testQuitLowerCase
      testQuitUpperCase
      testQuitWrongInputs
      testQuitMid
      testQuitSource
      testQuitIndex
      testQuitDestination

  Wrong move input:
      testNoMoveNoQuit
      testNoMoreMoveNoQuit
      testWrongSource
      testWrongIndex
      testWrongDestination

  Illegal move:
      testIllegalMoveSourceOutOfBound
      testIllegalMoveDestinationOutOfBound
      testIllegalMoveWrongIndex
      testIllegalMoveNotMoving
      testIllegalMoveNonEmptyOpen
      testIllegalMoveWrongOrderCascade
      testIllegalMOveWrongOrderFoundation
      testStateNotChangeAfterIllegalMove

  Legal move:
      testLegalMoveCascade
      testLegalMoveOpen
      testLegalMoveFoundation

  Game over:
      testGameOverCorrectly(with a deck provided by other model)
      testGameOverCorrectly2(with a deck provided by the same model)
      testNoMoreMovesAfterOver
      testPlayAgainAfterGameOver

  Other tests:
      testDeckNotChange
      testDeckNotChangeShuffle
      testModelMutateAfterPlayGame
      testMutateReadableBeforePlayGame
      testMutateAppendableBeforePlayGame

  Helper method:
      createSortedDeck
      stringToValue
      moveTestHelper
      moveIllegalTestHelper
      countAppearances
   */

  private StringBuffer input;
  private StringBuffer expected;
  private Appendable out;
  private IFreecellController controllerDefault;
  private FreecellOperations modelDefault;
  private FreecellOperations modelToCreateDeck;
  private FreecellOperations modelTester;
  private List<Object> deckDefault;
  private List<Object> deckSorted;
  private String testerInitialState;

  /**
   * Set up variables that can be used in many tests.
   */
  @Before
  public void setup() {
    input = new StringBuffer();
    expected = new StringBuffer();
    out = new StringBuffer();
    controllerDefault = new FreecellController(new StringReader(input.toString()), out);
    modelDefault = new FreecellModel();

    // this model do nothing but to create decks.
    modelToCreateDeck = new FreecellModel();

    deckDefault = modelToCreateDeck.getDeck();
    // a sorted deck is like this :A♠, A♣, A♥, A♦, 2♠, ... K♠, K♣, K♥, K♦
    deckSorted = createSortedDeck(modelToCreateDeck);

    // a standard model for me to test many cases. It has 52 cascade piles
    // and each pile has a card in ascending order. It also has 52 empty open
    // piles and 4 empty foundations.
    modelTester = new FreecellModel();
    modelTester.startGame(deckSorted, 52, 52, false);

    // the initial game state of modelTester.
    testerInitialState = modelTester.getGameState() + "\n";
  }


  /**
   * Tests for all possible null inputs.
   */
  @Test(expected = IllegalStateException.class)
  public void testNullReadable() {
    IFreecellController badController = new FreecellController(null, out);
    badController.playGame(deckSorted, modelDefault, 4, 4, true);
  }

  @Test(expected = IllegalStateException.class)
  public void testNullAppendable() {
    IFreecellController badController = new FreecellController(new StringReader(input.toString()),
        null);
    badController.playGame(deckSorted, modelDefault, 4, 4, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullDeck() {
    controllerDefault.playGame(null, modelDefault, 8, 4, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    controllerDefault.playGame(deckDefault, null, 8, 4, false);
  }


  /**
   * Tests for illegal inputs for playGame.
   */
  // With a deck with duplicates.
  @Test
  public void testInvalidDeckDuplicate() {
    List<Object> badDeck = modelToCreateDeck.getDeck();
    badDeck.set(0, badDeck.get(1));
    controllerDefault.playGame(badDeck, modelDefault, 8, 4, false);
    assertEquals("Could not start game.", out.toString());
  }

  // With a deck that lose one card.
  @Test
  public void testInvalidDeckNotComplete() {
    List<Object> badDeck = modelToCreateDeck.getDeck();
    badDeck.remove(0);
    controllerDefault.playGame(badDeck, modelDefault, 8, 4, false);
    assertEquals("Could not start game.", out.toString());
  }

  // With a empty deck
  @Test
  public void testInvalidDeckEmpty() {
    List<Object> badDeck = modelToCreateDeck.getDeck();
    while (badDeck.size() != 0) {
      badDeck.remove(0);
    }
    controllerDefault.playGame(badDeck, modelDefault, 8, 4, false);
    assertEquals("Could not start game.", out.toString());
  }

  // With a list of other things.
  @Test
  public void testInvalidDeckOtherObject() {
    List<Integer> badDeck = new ArrayList<>();
    for (int i = 0; i < 52; i++) {
      badDeck.add(i);
    }
    controllerDefault.playGame(badDeck, modelDefault, 8, 4, false);
    assertEquals("Could not start game.", out.toString());
  }

  @Test
  public void testInvalidNumberOfCascadePiles() {
    controllerDefault.playGame(deckDefault, modelDefault, 1, 4, false);
    assertEquals("Could not start game.", out.toString());
  }

  @Test
  public void testInvalidNumberOfOpenPiles() {
    controllerDefault.playGame(deckDefault, modelDefault, 8, 0, false);
    assertEquals("Could not start game.", out.toString());
  }


  /**
   * Test that playGame works correctly in all other cases.
   */
  // Previous illegal input should not effect valid input
  @Test
  public void testPlayGameAgainAfterIllegalInput() {
    // move a card to confirm the game starts correctly with legal input after
    // several times of failing to start with illegal input.
    input.append("C1 1 O1\n");
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    List<Object> badDeck = modelToCreateDeck.getDeck();
    badDeck.remove(0);
    newController.playGame(badDeck, modelDefault, 8, 0, false); // invalid
    newController.playGame(deckSorted, modelDefault, 8, 0, false); // invalid
    newController.playGame(deckSorted, modelDefault, 0, 4, false); // invalid
    newController.playGame(deckSorted, modelDefault, 52, 52, false); // valid
    expected.append("Could not start game.");
    expected.append("Could not start game.");
    expected.append("Could not start game.");
    expected.append(testerInitialState);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1, PileType
        .OPEN, 1);
    expected.append("Game quit prematurely.\n");

    assertEquals(expected.toString(), out.toString());
  }

  // call playGame twice with same valid inputs and no shuffle. should get
  // same states.
  @Test
  public void testPlayTwiceNoShuffle() {
    input.append("Q\n");
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckDefault, modelDefault, 4, 1, false);
    newController.playGame(deckDefault, modelDefault, 4, 1, false);
    StringBuffer beforeShuffle = new StringBuffer();
    StringBuffer afterShuffle = new StringBuffer();
    BufferedReader reader = new BufferedReader(new StringReader(out.toString()));
    for (int i = 0; i < 10; i++) {
      try {
        beforeShuffle.append(reader.readLine() + '\n');
      } catch (IOException e) {
        fail();
      }
    }
    for (int i = 0; i < 10; i++) {
      try {
        afterShuffle.append(reader.readLine() + '\n');
      } catch (IOException e) {
        fail();
      }
    }
    assertEquals(beforeShuffle.toString(), afterShuffle.toString());
  }

  // The states should be different with a shuffled deck.
  // There is a 1/52! chance to fail this test when the deck stay the same
  // after shuffling.
  @Test
  public void testShuffleCorrectly() {
    input.append("Q\n");
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckDefault, modelDefault, 4, 1, false);
    newController.playGame(deckDefault, modelDefault, 4, 1, true);
    StringBuffer beforeShuffle = new StringBuffer();
    StringBuffer afterShuffle = new StringBuffer();
    BufferedReader reader = new BufferedReader(new StringReader(out.toString()));
    for (int i = 0; i < 10; i++) {
      try {
        beforeShuffle.append(reader.readLine() + '\n');
      } catch (IOException e) {
        fail();
      }
    }
    for (int i = 0; i < 10; i++) {
      try {
        afterShuffle.append(reader.readLine() + '\n');
      } catch (IOException e) {
        fail();
      }
    }
    assertNotEquals(beforeShuffle.toString(), afterShuffle.toString());
  }

  // play a game and quit then play again, should start a new game.
  @Test
  public void testPlayTwice() {
    input.append("C1 1 O1\n");
    input.append("Q\n");
    input.append("C2 1 O1\n");
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);

    expected.append(testerInitialState);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1,
        PileType.OPEN, 1);
    expected.append("Game quit prematurely.\n");
    modelTester.startGame(deckSorted, 52, 52, false);
    expected.append(testerInitialState);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 2, 1,
        PileType.OPEN, 1);
    expected.append("Game quit prematurely.\n");

    assertEquals(expected.toString(), out.toString());
  }

  // play a game and quit then play again, should start a new game with
  // different deck and different model
  @Test
  public void testPlayTwiceDifferentDeckAndModel() {
    input.append("C1 1 O1\n");
    input.append("Q\n");
    input.append("C2 13 O1\n");
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    newController.playGame(deckDefault, new FreecellModel(), 4, 1, false);

    expected.append(testerInitialState);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1,
        PileType.OPEN, 1);
    expected.append("Game quit prematurely.\n");
    modelTester.startGame(deckDefault, 4, 1, false);
    expected.append(modelTester.getGameState() + "\n");
    moveTestHelper(modelTester, expected, PileType.CASCADE, 2, 13,
        PileType.OPEN, 1);
    expected.append("Game quit prematurely.\n");

    assertEquals(expected.toString(), out.toString());
  }

  // test playGame works correctly with a model that has already started a game.
  @Test
  public void testModelMidGame() {
    // move a card to check the game works correctly.
    input.append("C1 1 O1\n");
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    // start a game in the model and move some cards.
    modelDefault.startGame(deckDefault, 4, 4, true);
    modelDefault.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    modelDefault.move(PileType.CASCADE, 1, 12, PileType.OPEN, 1);
    modelDefault.move(PileType.CASCADE, 2, 12, PileType.OPEN, 3);

    // check if we can play a game with this controller.
    newController.playGame(deckSorted, modelDefault, 52, 52, false);

    expected.append(testerInitialState);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1,
        PileType.OPEN, 1);
    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // test playGame works correctly given a model that has already game over.
  @Test
  public void testModelGameOver() {
    // move a card to check the game works correctly.
    input.append("C1 13 O1\n");
    input.append("Q\n");

    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    // start a game in the model and play until game over.
    for (int i = 0; i < 52; i++) {
      int pileNum = i % 4 + 1;
      moveTestHelper(modelTester, new StringBuffer(), PileType.CASCADE, i + 1, 1,
          PileType.FOUNDATION, pileNum);
    }
    assertTrue(modelTester.isGameOver());

    // check if we can play a game with this controller.
    newController.playGame(deckSorted, modelTester, 4, 4, false);
    modelDefault.startGame(deckSorted, 4, 4, false);
    expected.append(modelDefault.getGameState() + "\n");
    moveTestHelper(modelDefault, expected, PileType.CASCADE, 1, 13,
        PileType.OPEN, 1);
    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }


  /**
   * Tests for quit game.
   */
  // quit a game with a lower case 'q'
  @Test
  public void testQuitLowerCase() {
    input.append("q ");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(modelDefault.getGameState() + "\n");
    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // quit a game with an upper case 'Q'
  @Test
  public void testQuitUpperCase() {
    input.append("Q ");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(modelDefault.getGameState() + "\n");
    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // the controller should ignore wrong inputs and read the valid "Q" at last
  // and quit the game
  @Test
  public void testQuitWrongInputs() {
    input.append("quit "); // wrong input
    input.append("Quit "); // wrong input
    input.append("123 "); // wrong input
    input.append("P "); // wrong input
    input.append("? "); // wrong input
    input.append("0 "); // wrong input
    input.append("  "); // wrong input
    input.append("\n "); // wrong input
    input.append("C1 1 O1\n"); // to prove that the game is still playing.
    input.append("Q "); // valid quit
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1,
        PileType.OPEN, 1);
    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // quit in the middle of the game
  @Test
  public void testQuitMid() {
    input.append("C1 1 O1\n");
    input.append("C2 1 O2\n");
    input.append("C3 1 O3\n");
    input.append("C4 1 O4\n");
    input.append("Q ");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1,
        PileType.OPEN, 1);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 2, 1,
        PileType.OPEN, 2);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 3, 1,
        PileType.OPEN, 3);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 4, 1,
        PileType.OPEN, 4);
    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // test quit game properly when expected to enter the source pile
  // It actually has been tested for many many times.
  @Test
  public void testQuitSource() {
    input.append("C1 1 O1\n");
    input.append("Q ");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1,
        PileType.OPEN, 1);
    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // test quit game properly when expected to enter the index
  @Test
  public void testQuitIndex() {
    input.append("C1\n"); // the source has been enter
    input.append("Q\n"); // if not quit, should be an card index here
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);
    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // test quit game properly when expected to enter destination pile
  @Test
  public void testQuitDestination() {
    input.append("C1\n"); // the source has been entered
    input.append("1\n"); // the card index has been entered
    input.append("Q\n"); // if not quit, should be an destination pile here
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);
    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }


  /**
   * Tests for bad inputs.
   */
  // test empty input
  @Test
  public void testNoMoveNoQuit() {
    input.append(""); // empty input, no move, no quit.
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);
    // According to the documentation in the interface. If at any point
    // readable has ended but the game is still playing, the game should be
    // quited and append such message to the appendable.
    expected.append("Game quit prematurely. Reached end of input file.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // test an input without a "q" or "Q"
  @Test
  public void testNoMoreMoveNoQuit() {
    input.append("C1 1 O1\n"); // a valid move and end.
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1,
        PileType.OPEN, 1);
    // According to the documentation in the interface. If at any point
    // readable has ended but the game is still playing, the game should be
    // quited and append such message to the appendable.
    expected.append("Game quit prematurely. Reached end of input file.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // test bad inputs for the source pile
  @Test
  public void testWrongSource() {
    // the controller should ignore all bad inputs and take the valid one
    // in the end.
    input.append("c1\n"); // lower case 'c' is invalid. Bad input.
    input.append("C\n"); // missing the pile number. Bad input.
    input.append("Cascade1\n"); // Bad input.
    input.append("<C1>\n"); // Bad input.
    input.append("(C1)\n"); // Bad input.
    input.append("C+1\n"); // Bad input.
    input.append("C-1\n"); // Bad input.
    input.append("C1.234\n"); // Bad input.
    input.append("CC1\n"); // Bad input.
    input.append("C1C\n"); // Bad input.
    input.append("AC1\n"); // Bad input.
    input.append("C1?\n"); // Bad input.
    input.append("C1!\n"); // Bad input.
    input.append("C 1\n"); // separated by a blank space. Bad input.
    input.append("K1\n"); // K is not a source pile. Bad input.
    input.append("11\n"); // a number. Bad input.
    input.append("    \n"); // blank spaces. Bad input.
    input.append("\n"); // a new line. Bad input.
    input.append("??\n"); // Bad input.

    input.append("C1\n"); // valid source
    input.append("1\n"); // valid index
    input.append("O1\n"); // valid destination
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1,
        PileType.OPEN, 1);
    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // test bad inputs for the card index
  @Test
  public void testWrongIndex() {
    input.append("C1\n"); // a valid source

    // the controller should ignore all bad inputs and keeps asking for
    // a valid input of the card index
    input.append("-100\n"); // negative number. Bad input.
    input.append("0\n"); // zero. Bad input.
    input.append("C1\n"); // C1 is a source/destination input. Bad input.
    input.append("O1\n"); // O1 is a source/destination input. Bad input.
    input.append("F1\n"); // F1 is a source/destination input. Bad input.
    input.append("K1\n"); // Bad input.
    input.append("abc\n"); // Bad input.
    input.append("ABC\n"); // Bad input.
    input.append("0.123\n"); // Bad input.
    input.append("+1\n"); // Bad input.
    input.append("-1\n"); // Bad input.
    input.append("1!\n"); // Bad input.
    input.append("1?\n"); // Bad input.
    input.append("(1)\n"); // Bad input.
    input.append("[1]\n"); // Bad input.
    input.append("\"1\"\n"); // Bad input.
    input.append(":)\n"); // Bad input.
    input.append("    \n"); // blank spaces. Bad input.
    input.append("\n"); // a new line. Bad input.
    input.append("??\n"); // Bad input.

    input.append("1\n"); // valid index
    input.append("O1\n"); // valid destination
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1,
        PileType.OPEN, 1);
    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // test bad inputs for the destination pile
  @Test
  public void testWrongDestination() {
    input.append("C1\n"); // a valid source
    input.append("1\n"); // valid index

    // the controller should ignore all bad inputs and keeps asking for
    // a valid input of the destination pile
    input.append("f1\n"); // lower case 'f' is invalid. Bad input.
    input.append("F\n"); // missing the pile number. Bad input.
    input.append("Foundation1\n"); // Bad input.
    input.append("<F1>\n"); // Bad input.
    input.append("(F1)\n"); // Bad input.
    input.append("F+1\n"); // Bad input.
    input.append("F-1\n"); // Bad input.
    input.append("F1.234\n"); // Bad input.
    input.append("FF1\n"); // Bad input.
    input.append("F1F\n"); // Bad input.
    input.append("AF1\n"); // Bad input.
    input.append("F1?\n"); // Bad input.
    input.append("F1!\n"); // Bad input.
    input.append("F 1\n"); // separated by a blank space. Bad input.
    input.append("K1\n"); // K is not a source pile. Bad input.
    input.append("11\n"); // a number. Bad input.
    input.append("    \n"); // blank spaces. Bad input.
    input.append("\n"); // a new line. Bad input.
    input.append("??\n"); // Bad input.

    input.append("F1\n"); // valid destination
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1,
        PileType.FOUNDATION, 1);
    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  /**
   * Tests for illegal moves.
   */
  // test when the source pile number is out of bound
  @Test
  public void testIllegalMoveSourceOutOfBound() {
    input.append("C0 1 O1\n"); // wrong cascade pile number
    input.append("C100 1 O1\n"); // wrong cascade pile number
    input.append("O0 1 O1\n"); // wrong open pile number
    input.append("O100 1 O1\n"); // wrong open pile number
    input.append("F0 1 O1\n"); // wrong foundation pile number
    input.append("F100 1 O1\n"); // wrong foundation pile number
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);

    // the controller should transmit "Invalid move...." for 6 times.
    int invalidTimes = countAppearances(out.toString(), "Invalid move. Try again.");
    assertEquals(6, invalidTimes);
  }

  // test when the destination pile number is out of bound
  @Test
  public void testIllegalMoveDestinationOutOfBound() {
    input.append("C1 1 C0\n"); // wrong cascade pile number
    input.append("C1 1 C100\n"); // wrong cascade pile number
    input.append("C1 1 O0\n"); // wrong open pile number
    input.append("C1 1 O100\n"); // wrong open pile number
    input.append("C1 1 F0\n"); // wrong foundation pile number
    input.append("C1 1 F100\n"); // wrong foundation pile number
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);

    // the controller should transmit "Invalid move...." for 6 times.
    int invalidTimes = countAppearances(out.toString(), "Invalid move. Try again.");
    assertEquals(6, invalidTimes);
  }

  // test when the card index is wrong
  @Test
  public void testIllegalMoveWrongIndex() {
    input.append("C1 5 O1\n"); // wrong index: not the index of the card
    // on top
    input.append("C1 100 O1\n"); // wrong index: out of bound
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 13, 52, false);
    expected.append(testerInitialState);

    // the controller should transmit "Invalid move...." for 2 times.
    int invalidTimes = countAppearances(out.toString(), "Invalid move. Try again.");
    assertEquals(2, invalidTimes);
  }

  // test when trying to move a card to where it already stays
  @Test
  public void testIllegalMoveNotMoving() {
    // start the test with a card in the open pile and another the foundation
    input.append("C1 1 O1\n");
    input.append("C2 1 F1\n");

    input.append("C3 1 C3\n"); // trying to move a card from C3 to C3
    input.append("O1 1 O1\n"); // trying to move a card from O1 to O1
    input.append("F1 1 F1\n"); // trying to move a card from F1 to F1
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);

    // the controller should transmit "Invalid move...." for 3 times.
    int invalidTimes = countAppearances(out.toString(), "Invalid move. Try again.");
    assertEquals(3, invalidTimes);
  }

  // test when trying to move a card to a non-empty open pile
  @Test
  public void testIllegalMoveNonEmptyOpen() {
    // start the test with two cards in the open pile and one the foundation
    input.append("C1 1 O1\n");
    input.append("C2 1 O2\n");
    input.append("C3 1 F1\n");

    input.append("C4 1 O1\n"); // move from cascade to non-empty open
    input.append("O2 1 O1\n"); // move from open to non-empty open
    input.append("F1 1 O1\n"); // move from foundation to non-empty open
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);

    // the controller should transmit "Invalid move...." for 3 times.
    int invalidTimes = countAppearances(out.toString(), "Invalid move. Try again.");
    assertEquals(3, invalidTimes);
  }

  // test when trying to move a wrong card to a cascade pile
  @Test
  public void testIllegalMoveWrongOrderCascade() {
    // start the test with one A♠ in open pile and one A♣ in foundation pile
    input.append("C1 1 O1\n");
    input.append("C2 1 F1\n");

    input.append("O1 1 C5\n"); // invalid move A under 2, same color
    input.append("O1 1 C11\n"); // invalid move A under 3, wrong order
    input.append("F1 1 C5\n"); // invalid move A under 2, same color
    input.append("F1 1 C11\n"); // invalid move A under 3, wrong order
    input.append("C3 1 C9\n"); // invalid move A under 2, same color
    input.append("C3 1 C9\n"); // invalid move A under 3, wrong order
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);

    // the controller should transmit "Invalid move...." for 6 times.
    int invalidTimes = countAppearances(out.toString(), "Invalid move. Try again.");
    assertEquals(6, invalidTimes);
  }

  // test when trying to move a wrong card to a foundation pile
  @Test
  public void testIllegalMOveWrongOrderFoundation() {
    // start the test with one A♠ in open pile and one A♣ in foundation pile
    input.append("C1 1 O1\n");
    input.append("C2 1 F1\n");

    input.append("O1 1 F1\n"); // invalid move A♠ on A♣
    input.append("C3 1 F1\n"); // invalid move A♥ on A♣
    input.append("C5 1 F1\n"); // invalid move 2♠ on A♣
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);

    // the controller should transmit "Invalid move...." for 3 times.
    int invalidTimes = countAppearances(out.toString(), "Invalid move. Try again.");
    assertEquals(3, invalidTimes);
  }

  // after trying to cast a illegal move, the state of the game should not
  // change.
  @Test
  public void testStateNotChangeAfterIllegalMove() {
    input.append("C1 1 F5\n"); // invalid move
    input.append("C2 1 O0\n"); // invalid move
    input.append("C3 100 C4\n"); // invalid move
    input.append("C5 1 C6\n"); // invalid move

    input.append("C1 1 O1\n"); // a valid move to check the state.
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);

    StringBuffer finalState = new StringBuffer();
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1,
        PileType.OPEN, 1);
    expected.append("Game quit prematurely.\n");
    BufferedReader reader = new BufferedReader(new StringReader(out.toString()));
    // ignore previous messages
    for (int i = 0; i < 52 + 52 + 4 + 4; i++) {
      try {
        reader.readLine();
      } catch (IOException e) {
        fail();
      }
    }
    // get the final state
    for (int i = 0; i < 52 + 52 + 4 + 1; i++) {
      try {
        finalState.append(reader.readLine() + "\n");
      } catch (IOException e) {
        fail();
      }
    }

    // the controller should transmit "Invalid move...." for 4 times.
    int invalidTimes = countAppearances(out.toString(), "Invalid move. Try again.");
    assertEquals(4, invalidTimes);
    // the final state is what we expect.
    assertEquals(expected.toString(), finalState.toString());
  }


  /**
   * Tests for all legal moves.
   */
  // Legal moves from cascade to other piles(including itself).
  @Test
  public void testLegalMoveCascade() {
    input.append("C1 1 O1\n");
    input.append("C2 1 F1\n");
    input.append("C6 1 F1\n");
    input.append("C3 1 C1\n");
    input.append("C4 1 C5\n");
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);

    // move from cascade to open
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1, PileType
        .OPEN, 1);

    // move from cascade to empty foundation
    moveTestHelper(modelTester, expected, PileType.CASCADE, 2, 1, PileType
        .FOUNDATION, 1);

    // move from cascade to foundation with card(s)
    moveTestHelper(modelTester, expected, PileType.CASCADE, 6, 1, PileType
        .FOUNDATION, 1);

    // move from cascade to empty cascade
    moveTestHelper(modelTester, expected, PileType.CASCADE, 3, 1, PileType
        .CASCADE, 1);

    // move from cascade to cascade with card(s)
    moveTestHelper(modelTester, expected, PileType.CASCADE, 4, 1, PileType
        .CASCADE, 5);

    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // Legal moves from open to other piles(including itself).
  @Test
  public void testLegalMoveOpen() {
    input.append("C1 1 O1\n");
    input.append("C2 1 O2\n");
    input.append("C3 1 O3\n");
    input.append("C4 1 O4\n");
    input.append("C5 1 O5\n");
    input.append("O1 1 F1\n");
    input.append("O5 1 F1\n");
    input.append("O2 1 C1\n");
    input.append("O3 1 C6\n");
    input.append("O4 1 O6\n");
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);

    // put 5 cards to 5 open piles to start the test
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1, PileType
        .OPEN, 1);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 2, 1, PileType
        .OPEN, 2);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 3, 1, PileType
        .OPEN, 3);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 4, 1, PileType
        .OPEN, 4);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 5, 1, PileType
        .OPEN, 5);

    // move from open to empty foundation
    moveTestHelper(modelTester, expected, PileType.OPEN, 1, 1, PileType
        .FOUNDATION, 1);

    // move from open to foundation with card(s)
    moveTestHelper(modelTester, expected, PileType.OPEN, 5, 1, PileType
        .FOUNDATION, 1);

    // move from open to empty cascade
    moveTestHelper(modelTester, expected, PileType.OPEN, 2, 1, PileType
        .CASCADE, 1);

    // move from open to cascade with card(s)
    moveTestHelper(modelTester, expected, PileType.OPEN, 3, 1, PileType
        .CASCADE, 6);

    // move from open to empty open
    moveTestHelper(modelTester, expected, PileType.OPEN, 4, 1, PileType
        .OPEN, 6);

    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  // Legal moves from foundation to other piles(including itself).
  @Test
  public void testLegalMoveFoundation() {
    input.append("C1 1 F1\n");
    input.append("C2 1 F2\n");
    input.append("C3 1 F3\n");
    input.append("C4 1 F4\n");
    input.append("F1 1 C1\n");
    input.append("F2 1 C7\n");
    input.append("F3 1 O1\n");
    input.append("F4 1 F1\n");
    input.append("Q\n");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);

    // put 4 cards to 4 foundation piles to start the test
    moveTestHelper(modelTester, expected, PileType.CASCADE, 1, 1, PileType
        .FOUNDATION, 1);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 2, 1, PileType
        .FOUNDATION, 2);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 3, 1, PileType
        .FOUNDATION, 3);
    moveTestHelper(modelTester, expected, PileType.CASCADE, 4, 1, PileType
        .FOUNDATION, 4);

    // move foundation to empty cascade
    moveTestHelper(modelTester, expected, PileType.FOUNDATION, 1, 1, PileType
        .CASCADE, 1);

    // move foundation to cascade with card(s)
    moveTestHelper(modelTester, expected, PileType.FOUNDATION, 2, 1, PileType
        .CASCADE, 7);

    // move foundation to empty open
    moveTestHelper(modelTester, expected, PileType.FOUNDATION, 3, 1, PileType
        .OPEN, 1);

    // move foundation to empty foundation
    moveTestHelper(modelTester, expected, PileType.FOUNDATION, 4, 1, PileType
        .FOUNDATION, 1);

    expected.append("Game quit prematurely.\n");
    assertEquals(expected.toString(), out.toString());
  }

  /**
   * Test for game works correctly until over.
   */
  // With a deck provided by other model.
  @Test
  public void testGameOverCorrectly() {
    expected.append(testerInitialState);
    // put all 52 cards to the foundation piles.
    for (int i = 0; i < 52; i++) {
      int pileNum = i % 4 + 1;
      String command = "C" + (i + 1) + " 1 F" + pileNum + "\n";
      input.append(command);
      moveTestHelper(modelTester, expected, PileType.CASCADE, i + 1, 1,
          PileType.FOUNDATION, pileNum);
    }
    // expect a "Game over" after the final game state.
    expected.append("Game over.\n");

    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    assertEquals(expected.toString(), out.toString());
  }

  // With a deck provided by the same model.
  @Test
  public void testGameOverCorrectly2() {
    List<Object> deckFromThisModel = createSortedDeck(modelDefault);

    expected.append(testerInitialState);
    // put all 52 cards to the foundation piles.
    for (int i = 0; i < 52; i++) {
      int pileNum = i % 4 + 1;
      String command = "C" + (i + 1) + " 1 F" + pileNum + "\n";
      input.append(command);
      moveTestHelper(modelTester, expected, PileType.CASCADE, i + 1, 1,
          PileType.FOUNDATION, pileNum);
    }
    // expect a "Game over" after the final game state.
    expected.append("Game over.\n");

    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckFromThisModel, modelDefault, 52, 52, false);
    assertEquals(expected.toString(), out.toString());
  }

  // The game should be quited after game over, no more moves.
  @Test
  public void testNoMoreMovesAfterOver() {

    expected.append(testerInitialState);
    // put all 52 cards to the foundation piles.
    for (int i = 0; i < 52; i++) {
      int pileNum = i % 4 + 1;
      String command = "C" + (i + 1) + " 1 F" + pileNum + "\n";
      input.append(command);
      moveTestHelper(modelTester, expected, PileType.CASCADE, i + 1, 1,
          PileType.FOUNDATION, pileNum);
    }
    // expect a "Game over" after the final game state.
    expected.append("Game over.\n");

    input.append("F1 13 O1"); // should be ignore
    input.append("F2 13 C1"); // should be ignore
    input.append("F3 13 F4"); // should be ignore
    input.append("Q"); // should be ignore

    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    assertEquals(expected.toString(), out.toString());
  }

  // Should able to call playGame after game over and start a new game with
  // same controller.
  @Test
  public void testPlayAgainAfterGameOver() {
    expected.append(testerInitialState);
    // put all 52 cards to the foundation piles.
    for (int i = 0; i < 52; i++) {
      int pileNum = i % 4 + 1;
      String command = "C" + (i + 1) + " 1 F" + pileNum + "\n";
      input.append(command);
      moveTestHelper(modelTester, expected, PileType.CASCADE, i + 1, 1,
          PileType.FOUNDATION, pileNum);
    }
    // expect a "Game over" after the final game state.
    expected.append("Game over.\n");
    // quit the second new game
    input.append("Q\n");
    // expect to see the initial state of the second new game
    expected.append(testerInitialState);
    expected.append("Game quit prematurely.\n");

    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    // start a new game again.
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    assertEquals(expected.toString(), out.toString());
  }


  /**
   * Other tests.
   */
  // the deck passed to the controller will stay unchanged after game play success.
  @Test
  public void testDeckNotChange() {
    List original = new ArrayList<>();
    for (Object d : deckSorted) {
      original.add(d);
    }

    input.append("C1 0 O1");
    input.append("Q");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);

    for (int i = 0; i < deckSorted.size(); i++) {
      assertEquals(original.get(i), deckSorted.get(i));
    }
  }

  // the deck passed to the controller will stay unchanged even though we
  // playGame with shuffle.
  @Test
  public void testDeckNotChangeShuffle() {
    List original = new ArrayList<>();
    for (Object d : deckSorted) {
      original.add(d);
    }

    input.append("C1 0 O1");
    input.append("Q");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, true);

    for (int i = 0; i < deckSorted.size(); i++) {
      assertEquals(original.get(i), deckSorted.get(i));
    }
  }


  // If readable is mutated after we pass it to controller, it will not affect the controller.
  // Instead of game quit prematurely, the last out put indicates game quit due to
  // reaching the end of input file. Signaling that the second "Q" is not read.
  @Test
  public void testMutateReadableBeforePlayGame() {
    input.append("C1 1 O1");
    input.append("\nQ");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);

    input.append("\nQ");
    newController.playGame(deckSorted, modelDefault, 52, 52, false);

    String[] output = out.toString().split("\n");

    assertEquals("Game quit prematurely. Reached end of input file.", output[output.length - 1]);
  }

  // If appendable is mutated after we pass it to controller, it will not affect the controller.
  // The world "Hi" is not contained in the list of outputs,
  // signaling that the second out.append is not passed to the model.
  @Test
  public void testMutateAppendableBeforePlayGame() {
    input.append("C1 1 O1");
    input.append("\nQ");
    input.append("\nQ");

    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    try {
      out.append("\nHi");
    } catch (IOException e) {
      fail();
    }
    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    String[] output = out.toString().split("\n");
    List outputList = Arrays.asList(output);

    assertFalse(outputList.contains("Hi"));
  }

  // After play game, movel state becomes mutated.
  @Test
  public void testModelMutateAfterPlayGame() {

    assertEquals("", modelDefault.getGameState());
    input.append("Q");
    IFreecellController newController = new FreecellController(new
        StringReader(input.toString()), out);

    newController.playGame(deckSorted, modelDefault, 52, 52, false);
    expected.append(testerInitialState);

    assertEquals(expected.toString(), modelDefault.getGameState() + "\n");
  }

  // And one more: If the implementation of controller is generic, what about
  // creating a FreecellController<Integer>? What to expect?

  /**
   * Create a deck of sorted cards.
   * The order of card is from small value to large value. With same value,
   * the order of suit is Spade, Club, Heart and Diamond.
   * For example: A♠, A♣, A♥, A♦, 2♠, 2♣, 2♥, 2♦, 3♠, ... K♠, K♣, K♥, K♦.
   *
   * @param model a model to create a default deck
   * @param <K> the Card type
   * @return a sorted list of card
   */
  private static <K> List<K> createSortedDeck(FreecellOperations<K> model) {
    List<K> deck = model.getDeck();
    /**
     * The comparator to compare two cards according to the output of
     * toString method of a card object. With this comparator, we can sort a
     * list of cards without knowing the name of the card class.
     */

    // sort the list of card with a anonymous comparator class.
    deck.sort((K o1, K o2) -> {
      String s1 = o1.toString();
      String s2 = o2.toString();
      int val1 = stringToValue(s1);
      int val2 = stringToValue(s2);
      Character suit1 = s1.charAt(s1.length() - 1);
      Character suit2 = s2.charAt(s2.length() - 1);
      if (val1 != val2) {
        return Integer.compare(val1, val2);
      } else {
        return Character.compare(suit1, suit2);
      }
    });
    return deck;

    /*
     no need for the comparator class.
    class CardComparator implements Comparator<K> {
      @Override
      public int compare(K o1, K o2) {
        String s1 = o1.toString();
        String s2 = o2.toString();
        int val1 = stringToValue(s1);
        int val2 = stringToValue(s2);
        Character suit1 = s1.charAt(s1.length() - 1);
        Character suit2 = s2.charAt(s2.length() - 1);
        if (val1 != val2) {
          return Integer.compare(val1, val2);
        } else {
          return Character.compare(suit1, suit2);
        }
      }
    }
    deck.sort(new CardComparator());
    */
  }

  /**
   * Get the value of a card with the string it provides.
   *
   * @param str the string provided by the card
   * @return the value of a card
   */
  private static int stringToValue(String str) {
    if (str.length() == 3) {
      return 10;
    } else if (str.charAt(0) == 'A') {
      return 1;
    } else if (str.charAt(0) == 'J') {
      return 11;
    } else if (str.charAt(0) == 'Q') {
      return 12;
    } else if (str.charAt(0) == 'K') {
      return 13;
    } else {
      return Integer.parseInt(str.substring(0, 1));
    }
  }

  /**
   * Helper method to make a valid move in the given model. And then append the
   * state of the game after move to the input StringBuffer.
   *
   * @param model the model to make the move
   * @param recorder a StringBuffer to record the state after move
   * @param source source pile type
   * @param pileNumber source pile number
   * @param cardIndex card index
   * @param destination destination pile type
   * @param destPileNumber destination pile number
   */
  private static void moveTestHelper(FreecellOperations model, StringBuffer
      recorder, PileType source, int pileNumber, int cardIndex, PileType
      destination, int destPileNumber) {
    model.move(source, pileNumber - 1, cardIndex - 1, destination,
        destPileNumber - 1);
    recorder.append(model.getGameState() + "\n");
  }

  /**
   * Helper method to count how many times a string sequence appears in the
   * given string. This method is used to count the number of invalid move as
   * we only know the first half of the message and don't know the second half.
   *
   * @param str a long string to be check
   * @param sequence a string sequence
   * @return the number of appearances of the sequence in the other string
   */
  private static int countAppearances(String str, String sequence) {
    String[] num = str.split(sequence);
    return num.length - 1;
  }


}
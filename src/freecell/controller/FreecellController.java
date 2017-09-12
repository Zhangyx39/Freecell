package freecell.controller;

import freecell.model.Card;
import freecell.model.PileType;
import java.io.IOException;
import java.util.List;

import freecell.model.FreecellOperations;
import java.util.Scanner;

/**
 * An implementation of the FreeCell Controller.
 * The Controller implements all functions of IFreeCellController.
 */
public class FreecellController implements IFreecellController<Card> {

  private Appendable output;
  private Scanner scan;
  private Readable input;

  /**
   * Constructor for controller.
   *
   * @param rd readable
   * @param ap appendable
   */
  public FreecellController(Readable rd, Appendable ap) {
    this.input = rd;
    this.output = ap;
    try {
      this.scan = new Scanner(rd);
    } catch (NullPointerException e) {
      //catches the exception and do nothing
    }
  }

  @Override
  public void playGame(List<Card> deck, FreecellOperations<Card> model, int numCascades,
      int numOpens,
      boolean shuffle) throws IllegalArgumentException, IllegalStateException {

    if (input == null || output == null) {
      throw new IllegalStateException("Readable or appendable not initialized");
    }

    if (deck == null) {
      throw new IllegalArgumentException("deck is null");
    }
    if (model == null) {
      throw new IllegalArgumentException("model is null");
    }
    //game start.
    if (startGame(deck, model, numCascades, numOpens, shuffle)) {
      appendOut(model.getGameState());

      String src = "";
      String des = "";
      String index = "";

      //read input
      //Scanner scan = new Scanner(inputRead);

      while (true) {
        if (model.isGameOver()) {
          appendOut("Game over.");
          return;
        }

        if (!scan.hasNext()) {
          appendOut("Game quit prematurely. Reached end of input file.");
          return;
        }

        String input = scan.next();
        if (input.equals("q") || input.equals("Q")) {
          appendOut("Game quit prematurely.");
          return;
        }

        // read 3 valid input, repeat until valid
        if (src.length() == 0) {
          if (!expectedPileInput(input)) {
            continue;
          } else {
            src = input;
          }
        } else if (index.length() == 0) {
          if (!isNumber(input)) {
            continue;
          } else if (Integer.valueOf(input) < 1) {
            continue;
          } else {
            index = input;
          }
        } else if (des.length() == 0) {
          if (!expectedPileInput(input)) {
            continue;
          } else {
            des = input;
          }
        }

        // if all input are valid
        if (des.length() >= 2) {
          String moveMsg = parseInputAndMove(src, index, des, model);
          src = "";
          index = "";
          des = "";
          if (moveMsg.equals("")) {
            appendOut(model.getGameState());
          } else {
            moveMsg = "Invalid move. Try again." + " " + moveMsg;
            appendOut(moveMsg);
          }
        }
      }
    }
  }

  /**
   * Check if a pile input is unexpected, if source, destination pile letters are invalid.
   * Or if the pile numbers are not actual number.
   *
   * @param input Input String
   * @return false if any input is invalid, true other wise.
   */
  private boolean expectedPileInput(String input) {

    input = input.replace(" ", "");
    if (input.length() < 2) {
      return false;
    }

    //check source and destination type
    PileType pile = pileName(input.substring(0, 1));
    if (pile == null) {
      return false;
    }

    //check source number and destination number

    //check all digit of srcNumber is a number, not +1 ++1 or -1 --1
    boolean allDigit = true;
    for (int i = 1; i < input.length(); i++) {
      String srcNum0 = input.substring(i, i + 1);
      allDigit = allDigit && isNumber(srcNum0);
    }

    //check overall is a number, example: "1" "10"
    String srcNum = input.substring(1);

    return allDigit && isNumber(srcNum);
  }

  /**
   * Check if a string is a valid number.
   *
   * @param s String input
   * @return true if is valid, else false.
   */
  private boolean isNumber(String s) {
    try {
      int num = Integer.valueOf(s);
    } catch (NumberFormatException e) {
      return false;
    }

    return true;
  }


  /**
   * Parse the input of the game into three valid inputs to move.
   * Input line should be in the form of "C1 0 F1" or "C1\n0\nF1"
   *
   * @param src source string
   * @param ind index string
   * @param des destination string
   * @param model the game model
   * @return Error message if the game move is illegal, else an empty string.
   */
  private String parseInputAndMove(String src, String ind, String des, FreecellOperations model) {
    String errorMsg = "";

    int cardIndex = Integer.valueOf(ind);

    PileType srcPile = pileName(src.substring(0, 1));
    int srcNum = Integer.valueOf(src.substring(1));

    PileType desPile = pileName(des.substring(0, 1));
    int desNum = Integer.valueOf(des.substring(1));

    try {
      model.move(srcPile, srcNum - 1, cardIndex - 1, desPile, desNum - 1);
    } catch (IllegalArgumentException e) {
      errorMsg += e;
    }
    return errorMsg;
  }

  /**
   * Decode character into PileType, it non exist, return null.
   *
   * @param code a single letter
   * @return Pile type or Null if did not found.
   */
  private PileType pileName(String code) {
    switch (code) {
      case "C":
        return PileType.CASCADE;
      case "F":
        return PileType.FOUNDATION;
      case "O":
        return PileType.OPEN;
      default:
        return null;
    }
  }


  /**
   * The startGame catches the exceptions thrown from Model for invalid game parameters.
   * It appends a message "Could not start game." to the output.
   * If an invalid deck of card was passed, for example, not of type Card,
   * catches ClassCastException, and append "Could not start game."
   *
   * @param deck deck of card
   * @param model game model
   * @param numCascades number of cascade piles
   * @param numOpens number of open piles
   * @param shuffle whether or not to shuffle
   * @return true if game started, else false
   */
  private boolean startGame(List<Card> deck, FreecellOperations<Card> model, int numCascades,
      int numOpens,
      boolean shuffle) {
    boolean success = true;
    try {
      model.startGame(deck, numCascades, numOpens, shuffle);
    } catch (IllegalArgumentException | ClassCastException e) {
      success = false;
      appendOut("Could not start game.");
    }
    return success;
  }


  /**
   * Append to the output stream. Quit the game if append fail.
   *
   * @param msg the message to be printed out.
   */
  private void appendOut(String msg) {
    try {
      this.output.append(msg);
      if (!msg.contains("start game")) {
        this.output.append("\n");
      }
    } catch (IOException appendFail) {
      // appendable failed to append, quit game.
      quitGame();
    }
  }

  /**
   * Method quits game immediately.
   */
  private void quitGame() {
    System.exit(0);
  }

//  /**
//   * Mini game run.
//   **/
//  public static void main(String a[]) {
//    InputStreamReader in = new InputStreamReader(System.in);
//    Appendable out = System.out;
//
//    FreecellOperations model = new FreecellModel();
//    IFreecellController controller = new FreecellController(in, out);
//
//    try {
//      controller.playGame(model.getDeck(), model, 4, 4, false);
//    } catch (IllegalArgumentException e) {
//      // catch exception.
//    }
//  }
}

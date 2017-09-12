package freecell.controller;

import java.util.List;

import freecell.model.FreecellOperations;

/**
 * This is the Interface for the FreeCellController.
 * It is parametrized over the card type.
 * The controller should throw an IllegalStateException if either of its Readable
 * and Appendable objects have not been initialized (i.e. if they are null).
 * It should throw an IllegalArgumentException if a null deck or model is passed to it.
 * The controller should not propagate any exceptions thrown by the model to its caller.
 */
public interface IFreecellController<K> {

  /**
   * Play a new game of free cell with given deck, with or without shuffling.
   *
   * <p>This method first verifies that the deck and the model is not null.
   * It should throw Illegal Argument Exception if either of these inputs is null.
   * The deck to be used is the one provided as input, and not necessarily
   * the one returned from the model.</p>
   *
   * <p>This method also verifies that the given deck is a valid deck, and that
   * the number of cascade piles and number of open piles are valid number.
   * If not, it appends in the output stream "Could not start game."</p>
   *
   * <p>An invalid deck is defined as a deck that has one or more of these flaws:
   * <ul><li>It does not have 52 cards</li>
   * <li>It has duplicate cards</li>
   * <li>It has at least one invalid card(invalid suit or invalid number) </li> </ul>
   * An invalid cascade pile number is any number less than 4.
   * An invalid open pile number is any number less than 1.</p>
   *
   * <p>If at any point, the input is either the letter 'q' or the letter 'Q',
   * the controller should write the message "Game quit prematurely."
   * on a separate line to the Appendable object, and return.</p>
   *
   * <p>At any point if the Appendable fails to transmit output,
   * the controller should immediately quit the game.</p>
   *
   * <p>At any point if the Readable reaches the end without a 'q' or 'Q', the controller
   * should quit the game.</p>
   *
   * <p>At any point the game is over, this controller should append to the
   * output stream "Game over.\n"</p>
   *
   * <p>If the game is ongoing, wait for user input from the Readable object.
   * A valid user input for a move is a sequence of three inputs (separated by spaces or newlines):
   * <ul><li>The source pile (e.g., "C1", as a single word).
   * The pile number begins at 1, so that it is more human-friendly.</li>
   * <li>The card index, again with the index beginning at 1.
   * <li>The destination pile (e.g., "F2", as a single word).</li>
   * The pile number is again counted from 1.</li></ul>
   * The controller will parse these inputs and pass the information on to the model
   * to make the move.</p>
   *
   * <p>If an input is unexpected, The method ask the user to input it again, until
   * a valid input is entered.
   * <ul><li>An unexpected input is can be one or more of the these flaws:
   * <li>Any letter other than 'q' or 'Q' to quit the game.
   * <li>Any letter other than 'C', 'F', 'O' to name a pile.</li>
   * <li>Anything that cannot be parsed to a valid number after the pile letter.</li>
   * <li>Anything that is not a number for the card index.</li>
   * <li>Anything card index that is less than 1.</li></ul></p>
   *
   *
   * <p>If the user entered the source pile correctly but the card index incorrectly,
   * the controller should ask for only the card index again, not the source pile,
   * and likewise for the destination pile.</p>
   *
   * <p>If the move was invalid as signalled by the model, the controller should transmit
   * a message to the Appendable object "Invalid move. Try again." plus any informative
   * message about why the move was invalid (all on one line), and resume waiting for
   * valid input.
   *
   * <p>The additional information should be a string of an empty space concatenated
   * with the appropriate exception message thrown by the model for user friendliness.</p>
   *
   * <p>The controller should throw an IllegalStateException if either of its
   * Readable and Appendable objects have not been initialized.
   * It should throw an IllegalArgumentException if a null deck or model is passed to it.
   * The controller should not propagate any exceptions thrown by the model to its caller.</p>
   *
   * <p>If a move is valid, the controller transmit game state to the Appendable object
   * exactly as the model provides it formatted as follows:
   * <pre>
   * F1:[b]f11,[b]f12,[b],...,[b]f1n1[n] (Cards in foundation pile 1 in order)
   * F2:[b]f21,[b]f22,[b],...,[b]f2n2[n] (Cards in foundation pile 2 in order)
   * ...
   * Fm:[b]fm1,[b]fm2,[b],...,[b]fmnm[n] (Cards in foundation pile m in
   * order)
   * O1:[b]o11[n] (Cards in open pile 1)
   * O2:[b]o21[n] (Cards in open pile 2)
   * ...
   * Ok:[b]ok1[n] (Cards in open pile k)
   * C1:[b]c11,[b]c12,[b]...,[b]c1p1[n] (Cards in cascade pile 1 in order)
   * C2:[b]c21,[b]c22,[b]...,[b]c2p2[n] (Cards in cascade pile 2 in order)
   * ...
   * Cs:[b]cs1,[b]cs2,[b]...,[b]csps (Cards in cascade pile s in order)
   *
   * where [b] is a single blankspace, [n] is newline. Note that there is no
   * newline on the last line
   * </pre></p>
   *
   * @param deck the deck to be dealt
   * @param model A free cell model
   * @param numCascades number of cascade piles
   * @param numOpens number of open piles
   * @param shuffle if true, shuffle the deck else deal the deck as-is
   * @throws IllegalArgumentException thrown if deck or model is null.
   * @throws IllegalStateException if the controller has not been initialized properly to receive
   *         input and transmit output, null readable or appendable.
   */
  void playGame(List<K> deck, FreecellOperations<K> model, int numCascades,
      int numOpens, boolean shuffle) throws IllegalArgumentException, IllegalStateException;


}

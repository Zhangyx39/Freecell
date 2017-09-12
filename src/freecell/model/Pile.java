package freecell.model;

/**
 * The is the interface for Pile classes: Cascade, Open, and Foundation.
 */
public interface Pile {

  /**
   * Add a card to pile.
   * A card cannot be added if:
   * <ul>
   * <li>Cascades: its color is not different from that of the currently last card
   * or its value is not exactly one less than that of the currently last card</li>
   * <li>Foundations: the card suit is different from that of the pile,
   * or its value is not one more than that of the card currently on top of the pile.\
   * Empty Foundation can only add Ace to the top</li>
   * <li>Opens: there already exist a card in the Open File</li>
   * </ul>
   *
   * @param c card to be added
   * @return true if add successfully, else return false.
   */
  boolean add(Card c);

  /**
   * Remove the last/top card from pile.
   * removeTop returns false if: the pile is an empty list without a card to be
   * removed
   *
   * @return true if removed successfully, else return false.
   */
  boolean removeTop();


  /**
   * Get the top of the pile. If list is empty, return Null.
   *
   * @return Card, the first or top of the pile.
   */
  Card getTop();

  /**
   * Get the number of cards in this pile.
   * @return an integer number of cards
   */
  int size();
}

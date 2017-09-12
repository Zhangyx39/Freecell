package freecell.model;


/**
 * This class represent a foundation pile of cards. A foundation is a list of
 * cards in ascending order only. It extends all functions of pile abstract.
 */
public class Foundation extends PileAbstract {

  public Foundation() {
    super();
  }

  @Override
  public boolean add(Card c) {
    if (pile.isEmpty() && c.getValue() == 1) {
      pile.add(c);
      return true;
    }

    if (pile.isEmpty() && c.getValue() != 1) {
      return false;
    }

    Card top = pile.get(pile.size() - 1);
    if (c.oneGreater(top) && top.sameSuit(c)) {
      pile.add(c);
      return true;
    } else {
      return false;
    }
  }
}

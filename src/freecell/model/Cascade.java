package freecell.model;

import java.util.List;

/**
 * This class represent a cascade pile of cards.
 * The cascade pile contains a list of cards in descending order of alternating color.
 */
public class Cascade extends PileAbstract {

  public Cascade(List<Card> cascade) {
    this.pile = cascade;
  }

  public Cascade() {
    super();
  }

  @Override
  public boolean add(Card c) {
    if (pile.isEmpty()) {
      pile.add(c);
      return true;
    }

    Card top = pile.get(pile.size() - 1);
    if (top.differentColor(c) && top.oneGreater(c)) {
      pile.add(c);
      return true;
    } else {
      return false;
    }
  }
}

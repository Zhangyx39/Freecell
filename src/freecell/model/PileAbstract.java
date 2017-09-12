package freecell.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class save some codes for classes that implement Pile.
 */
public abstract class PileAbstract implements Pile {

  protected List<Card> pile;

  public PileAbstract() {
    this.pile = new ArrayList<>();
  }

  @Override
  public boolean removeTop() {
    if (pile.isEmpty()) {
      return false;
    }
    pile.remove(pile.size() - 1);
    return true;
  }

  @Override
  public Card getTop() {
    if (pile.isEmpty()) {
      return null;
    }
    return pile.get(pile.size() - 1);
  }

  @Override
  public String toString() {
    if (pile.isEmpty()) {
      return "";
    }

    String toReturn = " " + pile.get(0).toString();
    for (int i = 1; i < pile.size(); i++) {
      toReturn = toReturn + ", " + pile.get(i).toString();
    }
    return toReturn;
  }

  @Override
  public int size() {
    return this.pile.size();
  }
}

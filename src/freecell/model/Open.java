package freecell.model;

/**
 * This class represent a open pile of cards. It extends the pileAbstract class.
 * The open file can only contain one card.
 */
public class Open extends PileAbstract {

  public Open() {
    super();
  }

  @Override
  public boolean add(Card c) {
    if (pile.isEmpty()) {
      pile.add(c);
      return true;
    } else {
      return false;
    }
  }
}

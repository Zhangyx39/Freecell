package freecell.model;

import java.util.ArrayList;
import java.util.List;


/**
 * The model for Freecell game, it implements functions specified in the
 * FreecellOperations and parameterize it with Card Class.
 */
public class FreecellModel implements FreecellOperations<Card> {

  private List<Pile> foundations;
  private List<Pile> opens;
  private List<Pile> cascades;
  private boolean hasBegun;

  /**
   * Constructor for FreecellModel.
   */
  public FreecellModel() {
    hasBegun = false;
    foundations = new ArrayList<>();
    opens = new ArrayList<>();
    cascades = new ArrayList<>();
  }

  @Override
  public List<Card> getDeck() {
    return new DeckImpl().getDeck();
  }

  @Override
  public void startGame(List<Card> deck, int numCascadePiles, int numOpenPiles,
      boolean shuffle) throws IllegalArgumentException {
    Deck myDeck = new DeckImpl(deck);

    // Check whether the input deck is valid.
    if (!myDeck.isValidDeck()) {
      throw new IllegalArgumentException("Not a valid deck");
    }

    // Check whether the input number of Cascade Pile is valid.
    if (numCascadePiles < 4) {
      throw new IllegalArgumentException("Must have more than 3 Cascade Piles");
    }

    // Check whether the input number of Open Pile is valid.
    if (numOpenPiles < 1) {
      throw new IllegalArgumentException("Must have more than 1 Open Piles");
    }

    // Shuffle the deck if needed.
    if (shuffle) {
      myDeck.shuffle();
    }

    foundations.clear();
    opens.clear();
    cascades.clear();

    // Initialize cascade piles in roundrobin fashion.
    List<Card>[] initCascades = new ArrayList[numCascadePiles];
    for (int i = 0; i < numCascadePiles; i++) {
      initCascades[i] = new ArrayList<>();
    }
    for (int i = 0; i < 52; i++) {
      initCascades[i % numCascadePiles].add(myDeck.get(i).clone());
    }
    for (int i = 0; i < numCascadePiles; i++) {
      cascades.add(new Cascade(initCascades[i]));
    }

    // Initialize empty open piles.
    for (int i = 0; i < numOpenPiles; i++) {
      opens.add(new Open());
    }

    // Initialize empty foundation piles.
    for (int i = 0; i < 4; i++) {
      foundations.add(new Foundation());
    }
    hasBegun = true;
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex,
      PileType destination, int destPileNumber) throws IllegalArgumentException {
    if (!hasBegun) {
      throw new IllegalArgumentException("Game has not begun.");
    }

    // Check and get the source and destination piles.
    if (source == null) {
      throw new IllegalArgumentException("Wrong source type: null");
    }
    if (destination == null) {
      throw new IllegalArgumentException("Wrong destination type: null");
    }
    List<Pile> sourceList = typeToPiles(source);
    List<Pile> destinationList = typeToPiles(destination);

    // Check pileNumber and DestPileNumber.
    if (pileNumber < 0 || pileNumber > sourceList.size() - 1) {
      throw new IllegalArgumentException("Wrong pile number: " + pileNumber);
    }
    if (destPileNumber < 0 || destPileNumber > destinationList.size() - 1) {
      throw new IllegalArgumentException("Wrong destination pile number: " +
          destPileNumber);
    }

    // Check and get the source card.
    Pile sourcePile = sourceList.get(pileNumber);
    Pile destinationPile = destinationList.get(destPileNumber);
    if (sourcePile.size() == 0) {
      throw new IllegalArgumentException("No card in this pile.");
    }
    if (cardIndex != sourcePile.size() - 1) {
      throw new IllegalArgumentException("Cannot move this card at index: " +
          cardIndex);
    }
    Card toMove = sourcePile.getTop();

    // Try to move.
    if (destinationPile.add(toMove)) {
      sourcePile.removeTop();
    } else {
      throw new IllegalArgumentException("Illegal move.");
    }
  }

  @Override
  public boolean isGameOver() {
    if (!hasBegun) {
      return false;
    }
    for (Pile f : foundations) {
      if (f.getTop() == null || f.getTop().getValue() != 13) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String getGameState() {
    StringBuilder toReturn = new StringBuilder("");
    if (!hasBegun) {
      return toReturn.toString();
    }

    for (int i = 0; i < foundations.size(); i++) {
      toReturn.append("F" + (i + 1) + ":" + foundations.get(i).toString() +
          "\n");
    }
    for (int i = 0; i < opens.size(); i++) {
      toReturn.append("O" + (i + 1) + ":" + opens.get(i).toString() + "\n");
    }
    for (int i = 0; i < cascades.size(); i++) {
      toReturn.append("C" + (i + 1) + ":" + cascades.get(i).toString() + "\n");
    }
    return toReturn.substring(0, toReturn.length() - 1);
  }

  /**
   * Given a PileType return the list of piles of that type.
   *
   * @param type one of OPEN, CASCADE or FOUNDATION
   * @return the list of that type
   */
  private List<Pile> typeToPiles(PileType type) {
    switch (type) {
      case CASCADE:
        return cascades;
      case FOUNDATION:
        return foundations;
      case OPEN:
        return opens;
      default:
        return null;
    }
  }
}

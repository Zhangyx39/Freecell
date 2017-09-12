import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import freecell.model.Card;
import freecell.model.CardImpl;
import freecell.model.Foundation;
import freecell.model.Pile;
import freecell.model.Suit;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the Foundation Class.
 * Test includes:
 * Adding to non Ace card Foundation piles when empty;
 * Adding to Ace card Foundation piles when empty;
 * Adding to nonEmpty Foundation pile with incorrect color;
 * Adding to nonEmpty Foundation pile with incorrect value;
 * Adding to nonEmpty Foundation pile successfully.
 */
public class PileFoundationTest {

  private Pile noneEmptyHeartFoundation;
  private Pile noneEmptyDiamondFoundation;
  private Pile noneEmptyClubFoundation;
  private Pile noneEmptySpadeFoundation;

  private Card heartRedAce = new CardImpl(1, Suit.HEART);
  private Card heartRed2 = new CardImpl(2, Suit.HEART);

  private Card diamondRedAce = new CardImpl(1, Suit.DIAMOND);
  private Card diamondRed2 = new CardImpl(2, Suit.DIAMOND);

  private Card clubBlackAce = new CardImpl(1, Suit.CLUB);
  private Card clubBlack2 = new CardImpl(2, Suit.CLUB);

  private Card spadeBlackAce = new CardImpl(1, Suit.SPADE);
  private Card spadeBlack2 = new CardImpl(2, Suit.SPADE);

  /**
   * Setting up four non-empty foundation piles for testing.
   */

  @Before
  public void setUp() {

    noneEmptyHeartFoundation = new Foundation();
    noneEmptyDiamondFoundation = new Foundation();
    noneEmptyClubFoundation = new Foundation();
    noneEmptySpadeFoundation = new Foundation();

    noneEmptyHeartFoundation.add(heartRedAce);
    noneEmptyDiamondFoundation.add(diamondRedAce);
    noneEmptyClubFoundation.add(clubBlackAce);
    noneEmptySpadeFoundation.add(spadeBlackAce);

  }

  /**
   * Testing for emptyFoundation add method.
   * If the inserted card is an Ace card, add card to pile, and return true;
   * else false, card not added.
   */
  @Test
  public void testFoundationPile_EmptyAddSuccess() {
    List<Card> fourAces = new ArrayList<>();
    fourAces.add(heartRedAce);
    fourAces.add(diamondRedAce);
    fourAces.add(clubBlackAce);
    fourAces.add(spadeBlackAce);

    String[] output = new String[]{" A♥", " A♦", " A♣", " A♠"};

    for (Card i : fourAces) {
      Pile foundationPile = new Foundation();
      assertTrue(foundationPile.add(i));
      assertEquals(output[fourAces.indexOf(i)], foundationPile.toString());
    }
  }

  @Test
  public void testFoundationPile_EmptyAddFail() {
    List<Card> fourNonAce = new ArrayList<>();
    fourNonAce.add(heartRed2);
    fourNonAce.add(diamondRed2);
    fourNonAce.add(clubBlack2);
    fourNonAce.add(spadeBlack2);

    for (Card i : fourNonAce) {
      Pile foundationPile = new Foundation();
      assertFalse(foundationPile.add(i));
      assertEquals("", foundationPile.toString());
    }
  }

  /**
   * A Non Empty Foundation can fail in 2 ways if added card:
   * 1- is not the same suit as the foundation;
   * 2- is not 1 + in value of the same suit.
   */
  @Test
  public void testFoundationPile_NonEmptyAddFail_NotSameSuit() {
    List<Pile> nonEmptyPile = new ArrayList<>();
    nonEmptyPile.add(noneEmptyHeartFoundation);
    nonEmptyPile.add(noneEmptyDiamondFoundation);
    nonEmptyPile.add(noneEmptyClubFoundation);
    nonEmptyPile.add(noneEmptySpadeFoundation);

    List<Card> fourNonAce = new ArrayList<>();
    fourNonAce.add(diamondRed2);
    fourNonAce.add(clubBlack2);
    fourNonAce.add(spadeBlack2);
    fourNonAce.add(heartRed2);

    String[] output = new String[]{" A♥", " A♦", " A♣", " A♠"};

    for (Pile foundation : nonEmptyPile) {
      int index = nonEmptyPile.indexOf(foundation);
      //ensure add is false
      assertFalse(foundation.add(fourNonAce.get(index)));
      //ensure pile did not change
      assertEquals(output[index], foundation.toString());
    }
  }

  @Test
  public void testFoundationPile_NonEmptyAddFail_IncorrectValue() {
    List<Pile> nonEmptyPile = new ArrayList<>();
    nonEmptyPile.add(noneEmptyHeartFoundation);
    nonEmptyPile.add(noneEmptyDiamondFoundation);
    nonEmptyPile.add(noneEmptyClubFoundation);
    nonEmptyPile.add(noneEmptySpadeFoundation);

    List<Card> fourNonAce = new ArrayList<>();
    fourNonAce.add(new CardImpl(3, Suit.HEART));
    fourNonAce.add(new CardImpl(5, Suit.DIAMOND));
    fourNonAce.add(new CardImpl(1, Suit.CLUB));
    fourNonAce.add(new CardImpl(4, Suit.SPADE));

    String[] output = new String[]{" A♥", " A♦", " A♣", " A♠"};

    for (Pile foundation : nonEmptyPile) {
      //get current index
      int index = nonEmptyPile.indexOf(foundation);
      //ensure add is false
      assertFalse(foundation.add(fourNonAce.get(index)));
      //ensure pile did not change
      assertEquals(output[index], foundation.toString());
    }
  }

  /**
   * Testing for successfully adding to nonEmptyFoundation.
   */
  @Test
  public void testFoundationPile_NonEmptyAddSuccess() {

    //testCards 2-13 of four suit
    List<Card> testCards = new ArrayList<>();
    for (int j = 1; j <= 4; j++) {
      for (int i = 2; i <= 13; i++) {
        testCards.add(new CardImpl(i, Suit.values()[j - 1]));
      }
    }

    //add each card to the pile of same color, assert true.
    for (Card i : testCards) {
      switch (i.getSuit()) {
        case HEART:
          assertTrue(noneEmptyHeartFoundation.add(i));
          break;
        case DIAMOND:
          assertTrue(noneEmptyDiamondFoundation.add(i));
          break;
        case CLUB:
          assertTrue(noneEmptyClubFoundation.add(i));
          break;
        case SPADE:
          assertTrue(noneEmptySpadeFoundation.add(i));
          break;
        default:
      }
    }
    //ensures the top of each pile is the largest value.
    assertEquals(13, noneEmptyHeartFoundation.getTop().getValue());
    assertEquals(13, noneEmptyDiamondFoundation.getTop().getValue());
    assertEquals(13, noneEmptyClubFoundation.getTop().getValue());
    assertEquals(13, noneEmptySpadeFoundation.getTop().getValue());
  }

}
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import freecell.model.Card;
import freecell.model.CardImpl;
import freecell.model.Cascade;
import freecell.model.Pile;
import freecell.model.Suit;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the Cascade Class.
 * Test includes:
 * Adding to Cascade piles when empty;
 * Adding to nonEmpty Cascade pile with incorrect color;
 * Adding to nonEmpty Cascade pile with incorrect value;
 * Adding to nonEmpty Cascade pile successfully.
 */
public class PileCascadeTest {

  private List<Suit> blackColor = new ArrayList<>();
  private List<Suit> redColor = new ArrayList<>();


  /**
   * Set up colors of suits.
   * Black color = {Club, Spade}.
   * Red color = {Heart Diamond}.
   */
  @Before
  public void setUp() {
    blackColor.add(Suit.CLUB);
    blackColor.add(Suit.SPADE);
    redColor.add(Suit.HEART);
    redColor.add(Suit.DIAMOND);
  }

  /**
   * Testing adding card of four different colors to
   * empty CascadePile, check returns true each time.
   */
  @Test
  public void testCascadePile_EmptyAddSuccess() {
    for (int i = 1; i < 14; i++) {
      for (int j = 0; j < 4; j++) {
        Pile emptyCascadePile = new Cascade();
        Card c = new CardImpl(i, Suit.values()[j]);
        assertTrue(emptyCascadePile.add(c));
        assertEquals(emptyCascadePile.toString().substring(1), c.toString());
      }
    }
  }

  /**
   * A Non Empty Foundation can fail in 2 ways if added card.
   * 1- is not different color.
   * 2- is not 1 less in value.
   */
  @Test
  public void testCascadePile_NonEmptyAddFail_NotDiffColor() {
    //Red card cannot be added to a pile with red colored top card
    for (int i = 0; i < 2; i++) {
      Pile cascadePileRed = new Cascade();
      cascadePileRed.add(new CardImpl(3, blackColor.get(i)));
      cascadePileRed.add(new CardImpl(2, redColor.get(i)));
      for (int j = 0; j < 2; j++) {
        assertFalse(cascadePileRed.add(new CardImpl(1, redColor.get(i))));
        assertFalse(cascadePileRed.add(new CardImpl(1, redColor.get(i))));
      }
    }
    //Black card cannot be added to a pile with black colored top card
    for (int i = 0; i < 2; i++) {
      Pile cascadePileRed = new Cascade();
      cascadePileRed.add(new CardImpl(3, redColor.get(i)));
      cascadePileRed.add(new CardImpl(2, blackColor.get(i)));
      for (int j = 0; j < 2; j++) {
        assertFalse(cascadePileRed.add(new CardImpl(1, blackColor.get(i))));
        assertFalse(cascadePileRed.add(new CardImpl(1, blackColor.get(i))));
      }
      assertTrue(cascadePileRed.add(new CardImpl(1, redColor.get(i))));
    }
  }


  /**
   * Testing failed adding to Cascade Pile when the value of card is not
   * exactly 1 less than the top card.
   */
  @Test
  public void testCascadePile_NonEmptyAddFail_IncorrectVal() {
    //Red colored pile value 5 at top
    Pile cascadePileRed = new Cascade();
    cascadePileRed.add(new CardImpl(6, Suit.SPADE));
    cascadePileRed.add(new CardImpl(5, Suit.HEART));

    int cardVal = cascadePileRed.getTop().getValue();

    //testing for value same or greater than the top card
    for (int m = 13; m >= cardVal; m--) {
      assertFalse(cascadePileRed.add(new CardImpl(m, Suit.SPADE)));
      assertFalse(cascadePileRed.add(new CardImpl(m, Suit.CLUB)));
    }
    //testing for value at most 2 less than the top card val
    for (int m = cardVal - 2; m > 0; m--) {
      assertFalse(cascadePileRed.add(new CardImpl(m, Suit.SPADE)));
      assertFalse(cascadePileRed.add(new CardImpl(m, Suit.CLUB)));
    }

    //Black colored pile value 5 at top
    Pile cascadePileBlack = new Cascade();
    cascadePileRed.add(new CardImpl(6, Suit.HEART));
    cascadePileBlack.add(new CardImpl(5, Suit.SPADE));
    int cardValBlack = cascadePileRed.getTop().getValue();

    //testing for value same or greater than the top card
    for (int m = 13; m >= cardValBlack; m--) {
      assertFalse(cascadePileBlack.add(new CardImpl(m, Suit.HEART)));
      assertFalse(cascadePileBlack.add(new CardImpl(m, Suit.DIAMOND)));
    }
    //testing for value at most 2 less than the top card val
    for (int m = cardValBlack - 2; m > 0; m--) {
      assertFalse(cascadePileBlack.add(new CardImpl(m, Suit.HEART)));
      assertFalse(cascadePileBlack.add(new CardImpl(m, Suit.DIAMOND)));
    }
  }

  /**
   * Testing for successfully adding to nonEmptyFoundation.
   */
  @Test
  public void testCascadePile_NonEmptyAddSuccess() {
    //start a pile with 13 of Heart
    Pile cascadePile = new Cascade();
    cascadePile.add(new CardImpl(13, Suit.HEART));
    String result = cascadePile.toString();
    Card newCard;

    for (int i = 12; i > 0; i--) {
      //generating random suit of either black or red color
      int random = Math.random() < 0.5 ? 1 : 0;
      //creating new card
      if (cascadePile.getTop().getSuit().equals(Suit.HEART)
              || cascadePile.getTop().getSuit().equals(Suit.DIAMOND)) {
        //if color is red, cascade black cards
        newCard = new CardImpl(i, blackColor.get(random));
      } else {
        //cascade red cards
        newCard = new CardImpl(i, redColor.get(random));
      }
      result = result + ", " + newCard.toString();
      assertTrue(cascadePile.add(newCard));
    }

    assertEquals(result, cascadePile.toString());
  }
}

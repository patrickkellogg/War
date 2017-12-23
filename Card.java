// FILE: Card.java
// Written by Patrick Kellogg (kelloggp@nag.cs.colorado.edu)
// Wednesday, February 25, 1998

// This program is submitted to meet the requirements
// for CSCI 3155 Portfolio #3
// The program creates some simple classes for a playing card and a
// deck of cards, and then does a "monte Carlo" simulation of
// the card game "War".

//This file implements the "Card" class which is the simplest element
//of my class structure: a playing card.

/**
 */
public class Card {

  int pips;
  String suit;
  boolean faceup;

  Card() {
    //Default constructor
    pips = 0;
    suit = "";
    faceup = false;
  }

  Card(int pipsin, String suitin, boolean faceupin) {
    // Constructor
    pips = pipsin;
    suit = suitin;
    faceup = faceupin;
  }

  public String toString() {
    //Overwrites the toString function for the class

    String result;

    if (faceup) {

      switch (pips) {

      case 1 : result = "Ace "; break;
      case 2 : result = "Two "; break;
      case 3 : result = "Three "; break;
      case 4 : result = "Four "; break;
      case 5 : result = "Five "; break;
      case 6 : result = "Six "; break;
      case 7 : result = "Seven "; break;
      case 8 : result = "Eight "; break;
      case 9 : result = "Nine "; break;
      case 10: result = "Ten "; break;
      case 11: result = "Jack "; break;
      case 12: result = "Queen "; break;
      case 13: result = "King "; break;
      default: result = "None "; break;
      }

      result = result + "of ";
      result = result + suit;
    }
    else {
      result = "XXXXXXXXXXXXXXX";
    }

    return result;

  }

  public void TurnOver() {
    //This routine will turn the card over, no matter what state it is in
    faceup = (!faceup);
  }

  public void TurnOver(boolean faceupin) {
    //This routine takes a variable to see what state the card should be in
    faceup = faceupin;
  }

  public int compareTo(Card o) {
    //This routine is similar to the one in the java.lang.Comparable package
    //It returns -1, 0 , or 1, if "this" Object is less
    //than, equal to, or greater than the given Object.

    //Do the comparison
    //See if the two are equal
    if (pips == o.pips) {
      return 0;
    }
    //See if the given card is greater that "this"
    if (pips < o.pips) {
      //Return -1
      return -1;
    }
    //Otherwise, return 1
    return 1;
  }
}

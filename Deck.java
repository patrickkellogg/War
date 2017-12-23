// FILE: Deck.java
// Written by Patrick Kellogg (kelloggp@nag.cs.colorado.edu)
// Wednesday, February 25, 1998

// This program is submitted to meet the requirements
// for CSCI 3155 Portfolio #3
// The program creates some simple classes for a playing card and a
// deck of cards, and then does a "monte Carlo" simulation of
// the card game "War".

//This file implements the "Deck" class which is a collection
//of Card objects

/**
 */
public class Deck {
  //The deck have two variables: a name and an array of cards
  String decktype;
  public Card[] cardarray;

  int temppips;
  String tempsuit;
  int tempplace;

  Deck() {
    //Constructor

    //Give it a name
    decktype = "Standard";

    //Create the size of the card array
    cardarray = new Card[52];

    //Start adding cards
    for (int suitcounter = 0; suitcounter < 4; suitcounter++) {

      //Find the name of the suit
      switch (suitcounter) {

      case 0 : tempsuit = "Hearts"; break;
      case 1 : tempsuit = "Diamonds"; break;
      case 2 : tempsuit = "Spades"; break;
      case 3 : tempsuit = "Clubs"; break;
      default: tempsuit = "No Trump"; break;
      }

      //Loop through the possible card values
      for (int cardcounter = 0; cardcounter < 13; cardcounter++) {

	//Find the number of pips on the card
	temppips = cardcounter + 1;

	//This is the array index of the card
        tempplace = (cardcounter + (13 * suitcounter));

	//Create a card with this information
	Card tempcard = new Card(temppips, tempsuit, true);
	//Insert the card into the Deck
	cardarray[tempplace] = tempcard;
      }
    }
  }

  public String toString() {
    //Return the deck type
    return decktype;
  }

}

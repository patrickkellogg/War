// FILE: CardStack.java
// Written by Patrick Kellogg (kelloggp@nag.cs.colorado.edu)
// Wednesday, February 25, 1998

// This program is submitted to meet the requirements
// for CSCI 3155 Portfolio #3
// The program creates some simple classes for a playing card and a
// deck of cards, and then does a "monte Carlo" simulation of
// the card game "War".

//This file implements the "CardStack" class, which is a variable
//length array of Card objects 

import java.lang.Math;

/**
 * @com.register ( clsid=1C662032-D70C-11D2-ABFB-856B3B8A694D, typelib=1C662030-D70C-11D2-ABFB-856B3B8A694D )
 */
public class CardStack {

  Card[] cardarray;

  CardStack() {
    //Constructor

    //Clear the array just in case
    cardarray = new Card[0];
  }

  public String toString() {
    //Override the default string for this class

    //Create a result string
    String result;

    //Send the number of cards to the result
    result = "Number of cards: " + cardarray.length;

    if (cardarray.length > 0) {

      //Loop through the entire array and print the values
      for (int cardlooper = 0; cardlooper < cardarray.length; cardlooper++) {
      
	//Add the current card
	result = result + cardarray[cardlooper];
	//And a newline character
	result = result + "\n";
      }
    }

    //Return the result
    return result;
  }

  public Card RemoveCard(int cardplace) {
    //This routine will remove a card from the Stack
    
    //Make sure the card is in the cardstack
    //assert(cardplace < cardarray.length);

    //Create a card to hold the given card
    Card tempcard;
    tempcard = cardarray[cardplace];

    //Make a shorter array
    Card[] newarray = new Card[(cardarray.length-1)];

    //Copy the array from 0 to the cardplace
    for (int copylooper = 0; copylooper < cardplace; copylooper++) {
      //Copy the cards one by one
      newarray[copylooper] = cardarray[copylooper];
    }
    //Copy the rest of the array
    for (int copylooper = (cardplace + 1); copylooper < cardarray.length; copylooper++) {
      //Copy the cards one by one
      newarray[(copylooper - 1)] = cardarray[copylooper];
    }

    //Make "this" point to the new array
    cardarray = newarray;

    //Return the card we pulled out
    return tempcard;
  }

  public void InsertCard(Card addcard) {
    //Insert the new card at the top of the cardstack

    //Create a new array to hold the new larger array
    Card[] bigarray = new Card[(cardarray.length+1)];

    //Copy the array "up" one value
    for (int cardlooper = 0; cardlooper < cardarray.length; cardlooper++) {
      //Copy the cards one by one
      bigarray[(cardlooper+1)] = cardarray[cardlooper];
    }

    //Add the new card
    bigarray[0] = addcard;

    //Make "this" point to the new array
    cardarray = bigarray;

  }

  public void RandomShuffle() {
    //This routine will shuffle the cards
    
    //Create an cardstack to hold the finished result
    CardStack newcs = new CardStack();

    //Loop until there are no more cards in the cardstack
    while (cardarray.length > 0) {

      //Find a random number, between 0 and the length of the array and round it
      double randomnum = Math.random();
      randomnum = randomnum * (cardarray.length);
      int cardloc = (int) Math.floor(randomnum);

      //Remove the card and put it in shuffledarray
      newcs.InsertCard(this.RemoveCard(cardloc));
    }

    //Make "this" point to the new array
    cardarray = newcs.cardarray;
  }

  public void TurnOver() {
    //This routine turns over all the cards in a stack, regardless of their state

    //Loop through all the cards
    for (int turnlooper = 0; turnlooper < cardarray.length; turnlooper++) {
      //Turn the card over
      cardarray[turnlooper].faceup = (!(cardarray[turnlooper].faceup));
    }
  }

  public void TurnOver(boolean faceupin) {
    //This routine makes the resulting cards all face up or all face down
    
    //Loop through all the cards
    for (int facelooper = 0; facelooper < cardarray.length; facelooper++) {
      //Turn the card over to the correct state
      cardarray[facelooper].faceup = faceupin;
    }
  }

}


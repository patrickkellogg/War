// FILE: War.java
// Written by Patrick Kellogg (kelloggp@nag.cs.colorado.edu)
// and Peter Smith (smithp@colorado.edu)

// This program is submitted to meet the requirements
// for CSCI 3155 Portfolio #3
// The program creates some simple classes for a playing card and a
// deck of cards, and then does a "Monte Carlo" simulation of
// the card game "War".

//This class implements the "War" class that contains the rules
//for the game, and more importantly, the main program.

import java.io.*;
import java.lang.*;

public class War {

  //The game keeps track of the two cardstacks, the discard stacks, and the temp stacks
  static CardStack cs1 = new CardStack();
  static CardStack cs2 = new CardStack();
  static CardStack csdis1 = new CardStack();
  static CardStack csdis2 = new CardStack();
  static CardStack csInHand = new CardStack();
  static CardStack csdraw = new CardStack();

  //Create a variable to see if anybody won (1 = 1 won, 2 = 2 won, 3 = nobody won)
  static int winner;
  //Create a variable to store the number of turns left, and a constant for the max number
  static int turnsleft;
  static final int MAXTURNS = 10000;

  //Variable for the loop counter
  static int nNumGames;

  //Variable to see how many Kings are in stack 1
  static int nKings;
  //Variable to find a "total count" of all the pips
  static int nTotalPips;

  static FileWriter fw;

  public String toString() {
    //This function overrides the default Object routine

    //Create an output string
    String result = "";

    //Print "1" for each card in stack #1
    for (int onelooper = 0; onelooper < cs1.cardarray.length; onelooper++) {
      //Put "1" in the string
      result = result + "|";
    }
    //Print "1" for each card in discard #1
    for (int onelooper = 0; onelooper < csdis1.cardarray.length; onelooper++) {
      //Put "1" in the string
      result = result + "|";
    }

    //Print "2" for each card in stack #2
    for (int twolooper = 0; twolooper < cs2.cardarray.length; twolooper++) {
      //Put "2" in the string
      result = result + "-";
    }
    //Print "2" for each card in discard #2
    for (int twolooper = 0; twolooper < csdis2.cardarray.length; twolooper++) {
      //Put "2" in the string
      result = result + "-";
    }

    //Return the string
    return result;
  }

  public static void TransferDiscard(CardStack discardstack, CardStack playstack) {
    //This routine will flip over the discard stack when the main stack is out

    //Make sure the playstack is out
    if (playstack.cardarray.length == 0) {

      //Loop through the discardstack and copy the cards over
      while (discardstack.cardarray.length > 0) {

	//Remove the card and put it in mainstack
	playstack.InsertCard(discardstack.RemoveCard(0));
      }
    }  
  }

  public static void CountKings() {
    //This routine counts how many kings are in stack 1

    //Clear the variable
    nKings = 0;

    //Start the loop
    for (int kinglooper = 0; kinglooper < cs1.cardarray.length; kinglooper++) {

      //See if we should add
      if (cs1.cardarray[kinglooper].pips == 13) {
        nKings = nKings + 1;
        
      }
    } 
  }

  public static void TotalPips() {
    //This routine will count the total number of pips in the hand

    //Clear the variable
    nTotalPips = 0;

    //Start the loop
    for (int pipslooper = 0; pipslooper < cs1.cardarray.length; pipslooper++) {

      //Add them up
      nTotalPips = nTotalPips + cs1.cardarray[pipslooper].pips;
        
    }
  }

  public static void SendToFile(int nWhoWon) {
    //This routine will send who won to a file

    System.out.println ("Who won " + nWhoWon);
    System.out.println ("Number of kings: " + nKings);

    try {

      Integer i = new Integer (nKings);

      fw.write(i.toString());

      if(nWhoWon == 1)
	fw.write ( " 0\n" );
      else
	fw.write (" 1\n");

      //if (nNumGames != 49)
      //fw.write (", ");    

    }
    catch (IOException ioe)
    {
      System.out.println( "IO error: " + ioe );
    }
  }

  public static void DoMatch() {
    //This one's tricky. For a draw, the comparison cards AND THREE MORE into a temp stack,
    //Then, draw one more card from each stack and compare them as before. Winner takes all

    //See if anybody loses by not having enough cards
    if (((cs1.cardarray.length) + (csdis1.cardarray.length)) < 4) {
      //Player one loses
      winner = 2;
    }
    else if (((cs2.cardarray.length) + (csdis2.cardarray.length)) < 4) {
      //Player two loses
      winner = 1;
    }
    else {
      
      //Draw three cards from each stack and put them in the temp draw stack
      for (int onedrawloop = 0; (winner == 0 && onedrawloop < 3); onedrawloop++) {
	//See if stack one needs be turned over
	if (cs1.cardarray.length == 0) {
	  //Call the routine
	  TransferDiscard(csdis1, cs1);
	}
	//Transfer the card from the main pile to the temp pile
	csdraw.InsertCard(cs1.RemoveCard(0));
      }
      //Do the same for stack two
      for (int twodrawloop = 0; (winner == 0 && twodrawloop < 3); twodrawloop++) {
	//See if stack two needs be turned over
	if (cs2.cardarray.length == 0) {
	  //Call the routine
	  TransferDiscard(csdis2, cs2);
	}
	//Transfer the card from the main pile to the temp pile
	csdraw.InsertCard(cs2.RemoveCard(0));
      }

      //Make sure there is no winner
      if (winner == 0) {

	//See if stack one needs be turned over
	if (cs1.cardarray.length == 0) {
	  //Call the routine
	  TransferDiscard(csdis1, cs1);
	}

	//Do the same for stack two
	if (cs2.cardarray.length == 0) {
	  //Call the routine
	  TransferDiscard(csdis2, cs2);
	}

	//Now compare the two top cards again
	Card testone = cs1.RemoveCard(0);
	Card testtwo = cs2.RemoveCard(0);
	
	//Compare the two cards
	if ((testone.compareTo(testtwo)) == 1) {
	  //Put both cards into the discard stack of stack one
	  csdis1.InsertCard(testone);
	  csdis1.InsertCard(testtwo);
	  //Stack one also gets the entire draw stack
	  while (csdraw.cardarray.length > 0) {
	    //Move the cards one by one
	    csdis1.InsertCard(csdraw.RemoveCard(0));
	  }  
	}
	else if ((testtwo.compareTo(testone)) == 1) {
	  //Put both cards into the discard stack of stack two
	  csdis2.InsertCard(testtwo);
	  csdis2.InsertCard(testone);
	  //Stack two also get the entire draw stack
	  while (csdraw.cardarray.length > 0) {
	    //Move the cards one by one
	    csdis2.InsertCard(csdraw.RemoveCard(0));
	  }
	}
	else {
	  //Belive if or not, do another draw
	  //Put the two cards in the draw stack to see who winds them
	  csdraw.InsertCard(testone);
	  csdraw.InsertCard(testtwo);
	  //Call the recursive routine
	  DoMatch();
	}
      }
    }
  }

  public static void main(String args[]) {

    try {
      fw = new FileWriter ("output.txt");
    }
    catch (IOException ioe) {
      System.out.println("An error occurred: " + ioe);
    }

    //for (int nNumTrials = 0; nNumTrials < 2; nNumTrials++) { 

      for (nNumGames = 0; nNumGames < 1000; nNumGames++ ) {

        //Clear the variables
        cs1 = new CardStack();
        cs2 = new CardStack();
        csdis1 = new CardStack();
        csdis2 = new CardStack();
        csInHand = new CardStack();
        csdraw = new CardStack();
        winner = 0;
        turnsleft = 0;

        //Create a new game of War
        War wargame = new War();

        //Create a standard deck 
        Deck deckStandard = new Deck();

        //Put the entire deck "in hand"
        csInHand.cardarray = deckStandard.cardarray;

        //Shuffle the stack several times
        csInHand.RandomShuffle();

        //Put the first 26 cards in the first cardstack
        for (int cardlooper = 0; cardlooper < 26; cardlooper++) {
          //Copy all the cards one by one
          cs1.InsertCard(csInHand.RemoveCard(0));
        }

        //Put the next 26 cards in the second cardstack
        for (int cardlooper = 0; cardlooper < 26; cardlooper++) {
          //Copy all the cards one by one
          cs2.InsertCard(csInHand.RemoveCard(0));
        }

        //Count the number of kings in stack 1
        CountKings();

        //Turn over both stacks
        cs1.TurnOver();
        cs2.TurnOver();

        //Make sure nobody won yet
        winner = 0;

        //Set the number of turns
        turnsleft = MAXTURNS;

        //Play until the max number of turns runs out, or somebody wins
        while (turnsleft > 0 && (winner == 0)) {

          //Print the current state of the game
          //System.out.println(wargame);

          //See if stack one needs be turned over
          if (cs1.cardarray.length == 0) {
	    //Call the routine
	    TransferDiscard(csdis1, cs1);
          }
          //See if the stack two needs be turned over
          if (cs2.cardarray.length == 0) {
	    //Call the routine
	    TransferDiscard(csdis2, cs2);
          }

          //Turn over one card from each stack
          Card testone = cs1.RemoveCard(0);
          Card testtwo = cs2.RemoveCard(0);

          //Compare the two cards
          if ((testone.compareTo(testtwo)) == 1) {
	    //Make sure both cards are face down
	    testone.TurnOver(false);
	    testtwo.TurnOver(false);
	    //Put both cards into the discard stack of stack one
	    csdis1.InsertCard(testone);
	    csdis1.InsertCard(testtwo);
          }
          else if ((testtwo.compareTo(testone)) == 1) {
	    //Make sure both cards are face down
	    testone.TurnOver(false);
	    testtwo.TurnOver(false);
	    //Put both cards into the discard stack of stack two
	    csdis2.InsertCard(testtwo);
	    csdis2.InsertCard(testone);
          }
          else {
	    //Do the special three-card-draw when there is a match
	    //Put the two cards in the draw stack to see who winds them
	    csdraw.InsertCard(testone);
	    csdraw.InsertCard(testtwo);
	    //Do the recursive routine
	    DoMatch();
          }

          //See if anybody is a loser
          if ((winner == 0) && (cs1.cardarray.length == 0) && (csdis1.cardarray.length == 0)) {
	    //Player 1 is  a loser
	    winner = 2;
          }
          else if ((winner == 0) && (cs2.cardarray.length == 0) && (csdis2.cardarray.length == 0)) {
	    //Player 2 is a loser
	    winner = 1;
          }
    
          //Decrement the number of turns left
          turnsleft--;
        }

        //Create a string for the result
        String result;
    
        //Print the result
        switch (winner) {
      
          case 1 : result = "Player 1 is the winner in " + (MAXTURNS - turnsleft) +  " turns"; break;
          case 2 : result = "Player 2 is the winner in " + (MAXTURNS - turnsleft) + " turns"; break;
          case 0 : result = "There was no winner in " + MAXTURNS + " turns."; break;
          default: result = "Unknown winner."; break;
        }
        //Add the info of how many kings player 1 had
        result = result + "\nPlayer 1 had " + nKings + " kings.";

        //Finish the main routine with a result
        //System.out.println(result);

        //Print results to file
        SendToFile(winner);
      }



      //Add a new line
      try {
        fw.write ( "\n" );
      }
      catch (IOException ioe)
      {
        System.out.println( "IO error: " + ioe );
      }

      //Show us what trial we are on
      //System.out.println( "Trial number: " + (nNumTrials+1));

      //}
    
    try{
      fw.close();
    }
    catch(IOException ioe)
    {
      System.out.println("An error occurred: " + ioe);
    }
  }
}


import java.applet.*;		//Imports Applet
import java.awt.*;			//Imports Button, TextArea
import java.awt.event.*;	//Imports ActionListener
import Card;				//Imports the Card class
import CardStack;			//Imports the CardStack class
import Deck;				//Imports the Deck class
import java.io.*;

public class WarApplet extends Applet
{
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

	//Create the interactive componenets for the Applet
	TextArea txtareaGame = new TextArea(12,70);
	Button btnPlay = new Button("Play Game");
	
	//Create a boolean value so we know if a game is running
	boolean bRunning;
	
	//This is the first routine that runs	
	//Create the listener for the button
	class PlayListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			//See if we can start a game
			if (bRunning == false)
			{
				//Start a new game
				StartGame();
			}
		}
	}
	
	public void init()
	{
		add(new Label("Click on the button to start a new game of War"));
			
		//Add the text area
		txtareaGame.setEditable(false);
		add(txtareaGame);
			
		//Add the button
		add(btnPlay);
			
		//Set the flag to "Not Running"
		bRunning = false;
			
		//Tell the button what it should do when clicked
		btnPlay.addActionListener(new PlayListener());
	}	
	public String CreateStatus() {
	//This function converts the status to a string

		//Create an output string
		String result = "";

		//Print "1" for each card in stack #1
		for (int onelooper = 0; onelooper < cs1.cardarray.length; onelooper++) {
			//Put "1" in the string
			result = result + "\\";
		}
		//Print "1" for each card in discard #1
		for (int onelooper = 0; onelooper < csdis1.cardarray.length; onelooper++) {
			//Put "1" in the string
			result = result + "\\";
		}

		//Print "2" for each card in stack #2
		for (int twolooper = 0; twolooper < cs2.cardarray.length; twolooper++) {
			//Put "2" in the string
			result = result + "/";
		}
		//Print "2" for each card in discard #2
		for (int twolooper = 0; twolooper < csdis2.cardarray.length; twolooper++) {
			//Put "2" in the string
			result = result + "/";
		}
		//Put a carriage return at the end		result = result + "\n";		
		//Return the string
		return result;
	}

	public void TransferDiscard(CardStack discardstack, CardStack playstack) {
	//This routine will flip over the discard stack when the main stack is out

		//Make sure the playstack is out
		if (playstack.cardarray.length == 0) {

			//Loop through the discardstack and copy the cards over
			while (discardstack.cardarray.length > 0) {

				//Remove the card and put it in mainstack
				playstack.InsertCard(discardstack.RemoveCard(0));
			}
		}	}  

	public void DoMatch() {
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
					//Believe if or not, do another draw
					//Put the two cards in the draw stack to see who winds them
					csdraw.InsertCard(testone);
					csdraw.InsertCard(testtwo);
					//Call the recursive routine
					DoMatch();
				}
			}
		}
	}		
	//This routine start a new game of War and plays it one time through
	public void StartGame()
	{
		//Clear the TextArea
		txtareaGame.setText("Starting game\n");
		
		//Set the flag to not do it again
		bRunning = true;

        //Clear the variables
        cs1 = new CardStack();
        cs2 = new CardStack();
        csdis1 = new CardStack();
        csdis2 = new CardStack();
        csInHand = new CardStack();
        csdraw = new CardStack();
        winner = 0;
        turnsleft = 0;

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
			txtareaGame.append(CreateStatus());

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
      
			case 1 : result = "Player 1 is the winner in " + (MAXTURNS - turnsleft) +  " turns\n";					 break;
			case 2 : result = "Player 2 is the winner in " + (MAXTURNS - turnsleft) + " turns\n";					 break;
			case 0 : result = "There was no winner in " + MAXTURNS + " turns\n";					 break;
			default: result = "Unknown winner\n";					 break;
		}

        //Finish the main routine with a result
		txtareaGame.append(result);

		//Let the applet run another game		bRunning = false;
	}
}

package game;
import javax.swing.event.*;
import java.util.*;

/**
 * This is model part of the program. It holds the board information
 * 
 * @author Marietta Asemwota
 * @author Stan Yanakiev
 * @author Yihua Li
 * @date 10/30/2017
 */
public class BoardModel {
	int[] boardA;
	int[] boardB;
	int defaultStones; 
	boolean playerBTurn; // false = Player A, true = player B
	int numToAdd; //number to add to each pit -- standard is 1
	
	private ArrayList<ChangeListener> listeners;
	private ArrayList<String> moves; //list of moves to help with undo
	Deque<String> stack;
	private int undoChances; 

	public BoardModel() 
	{
		boardA = new int[7];
		boardB = new int[7];
		defaultStones = 0; 
		
		// Player A always starts first
		playerBTurn = false;
		
		numToAdd = 1;
		listeners = new ArrayList<ChangeListener>();
		
		stack = new ArrayDeque<String>();
		undoChances = 0;
	}
	
	/**
	 * Set the number of stones in each pit based on user input 
	 * @param stones - the number of stones
	 */
	public void setStones(int stones) 
	{
		defaultStones = stones;
	
		//PLAYER A
		for (int i = 0; i < boardA.length; i++) 
		{
			boardA[i] = stones;
			if (i == 6) //if mancala for player A
				boardA[6] = 0;
		}
		
		//PLAYER B
		for (int i = 0; i < boardB.length; i++) 
		{
			boardB[i] = stones;
			if (i == 6) //if mancala for player B
				boardB[6] = 0;
		}
		
		System.out.println("Starting Board: ");
		printStones();
		System.out.println("-------------------------------");
	}
	
	/**
	 * Get the board for player A
	 * @return array of ints
	 */
	public int[] getBoardA()
	{
		return boardA;
	}
	
	/**
	 * Get the board for player B
	 * @return array of ints
	 */
	public int[] getBoardB()
	{
		return boardB;
	}
	
	public void setTurn()
	{
		playerBTurn = !playerBTurn;
	}

	public void printStones()
	{
		// PLAYER B (prints it backwards)
		for (int i = boardB.length - 1; i >= 0; i--) 
		{
			if(i == 6) //the mancala
			{
				System.out.print("{" + boardB[i] +  "} ");
			}
			else
				System.out.print(boardB[i] + " ");
		}
		System.out.print("         <--- Player B");
		System.out.println(" ");
		
		//PLAYER A
		System.out.print("    ");
		for (int i = 0; i < boardA.length; i++) 
		{
			if(i == 6) //the mancala
			{
				System.out.print("{" + boardA[i] +  "} ");
			}
			else
				System.out.print(boardA[i] + " ");
		}
		System.out.print("     <--- Player A");
		System.out.println("\n ");
	}

	
	/**
	 * Given the board and a pit, check if the pit is empty
	 * @param playerBTurn - the board to check through
	 * @param pit - the pit to look in
	 * @return whether it has 0 stones or not
	 */
	public boolean isEmpty(boolean playerBTurn, int pit)
	{
		if (!playerBTurn) // Player A's turn
		{
			if (boardA[pit - 1] == 0) 
			{
				return true;
			} 
		}
		
		else //player B turn
		{
			if (boardB[pit - 1] == 0) 
			{
				return true;
			} 
		}
		
		return false;
	}
	
	
	/**
	 * Place stones from one pit to the others
	 * 
	 * @param playerBTurn
	 *            - whose turn it is (who owns the pit)
	 * @param pit
	 *            - the pit to move from (1 - 7)
	 */
	public void placeStones(boolean playerBTurn, int pit) 
	{
		// when stones are moved, notify the change listener
		// Notify all observers of the change to the invoice
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners)
			listener.stateChanged(event);

		if (pit == 7) 
		{
			System.out.println("Can Not Choose The Mancala");
		} 
		else 
		{
			int stonesLeft;
			
			String move = ""; //to keep track of this move 
			//stack.push(move);
			
	
			if (!playerBTurn) // Player A's turn
			{
				stonesLeft = boardA[pit - 1]; 	// how many stones to move
				
				move += "A";
				move += pit;
				move += stonesLeft;
				
				boardA[pit - 1] = 0; 			// all stones are taken from the chosen pit

				// go through board A starting from chosen pit
				goThroughBoardA(stonesLeft, pit);
				setTurn(); 						//opponents turn
				printStones();
				System.out.println("Player B's turn now");
				System.out.println("-------------------------------");
			}

			else // Player B's turn
			{
				stonesLeft = boardB[pit - 1];
				
				move += "B";
				move += pit;
				move += stonesLeft;
				
				boardB[pit - 1] = 0;

				goThroughBoardB(stonesLeft, pit);
				setTurn(); // opponents turn
				printStones();
				System.out.println("Player A's turn now");
				System.out.println("-------------------------------");
			}
			
			stack.push(move);
			undoChances++;
		}
	}

	/**
	 * Go through and place stones on Player A's side
	 * 
	 * @param stonesLeft
	 *            - the stones left to distribute
	 * @param pit
	 *            - the pit to start from; 0 if starting over
	 */
	public void goThroughBoardA(int stonesLeft, int pit)
	{
		//to make this able to traverse the board and subtract stones, be able to change numberToAdd
		//numToAdd = 1; 
		
		//if pit == 0 -> start over
		for (int i = pit; i < boardA.length; i++) 
		{
			if (stonesLeft > 0) 
			{
				if(i == 6 && playerBTurn) // if its Players B turn, skip the Player's A mancala
				{
					continue;
				}
				boardA[i] = boardA[i] + numToAdd;
				stonesLeft--;
			}
		}
		if(stonesLeft > 0) // still stones left? go to boardB
		{
			goThroughBoardB(stonesLeft, 0); 
		}
	}
	
	/**
	 * Go through and place stones on Player B's side
	 * @param stonesLeft - the stones left to distribute
	 * @param pit - the pit to start from; 0 if starting over
	 */
	public void goThroughBoardB(int stonesLeft, int pit)
	{
		//if pit == 0 --> start over
		for (int i = pit; i < boardB.length; i++) 
		{
			if (stonesLeft > 0) 
			{
				if(i == 6 && !playerBTurn) //if its Players A turn, skip the Player's B mancala
				{
					continue;
				}
				boardB[i] = boardB[i] + numToAdd;
				stonesLeft--;
			}
		}
		if(stonesLeft > 0)
		{
			goThroughBoardA(stonesLeft, 0);
		}
	}
	
	/**
    		Adds a change listener to the board model
    		@param listener the change listener to add
	 */
	public void addChangeListener(ChangeListener listener)
	{    
		listeners.add(listener); 
	}

}
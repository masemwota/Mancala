package game;

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

	public BoardModel() 
	{
		boardA = new int[7];
		boardB = new int[7];
		defaultStones = 0; 
		
		// Player A always starts first
		playerBTurn = false;
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
		 * Place stones from one pit to the others 
		 * @param playerBTurn - whose turn it is (who owns the pit)
		 * @param pit - the pit to move from (1 - 7)
		 */
		public void placeStones(boolean playerBTurn, int pit) 
		{
			if (pit == 7) 
			{
				System.out.println("Can Not Choose The Mancala");
			}
			else 
			{ 
				int stonesLeft;
				
				if (!playerBTurn) // Player A's turn
				{
					stonesLeft = boardA[pit-1]; //how many stones to move
					boardA[pit - 1] = 0; // all stones are taken from the chosen pit

					//go through board A starting from chosen pit
					goThroughBoardA(stonesLeft, pit); 
					playerBTurn = true; // opponents turn
				} 
				
				else // Player B's turn
				{
					stonesLeft = boardB[pit-1];
					boardB[pit - 1] = 0;

					goThroughBoardB(stonesLeft, pit);
					playerBTurn = false;
				}
				printStones();
			}
		}
	
	
		/**
		 * Go through and place stones on Player A's side
		 * @param stonesLeft - the stones left to distribute
		 * @param pit - the pit to start from; 0 if starting over
		 */
	public void goThroughBoardA(int stonesLeft, int pit)
	{
		//if pit == 0 -> start over
		for (int i = pit; i < boardA.length; i++) 
		{
			if (stonesLeft > 0) 
			{
				if(i == 6 && playerBTurn) // if its Players B turn, skip the Player's A mancala
				{
					//continue;
				}
				boardA[i] = boardA[i] + 1;
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
					//continue;
				}
				boardB[i] = boardB[i] + 1;
				stonesLeft--;
			}
		}
		if(stonesLeft > 0)
		{
			goThroughBoardA(stonesLeft, 0);
		}
	}
}
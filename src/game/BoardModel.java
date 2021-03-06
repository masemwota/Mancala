package game;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * This is model part of the program. It holds the board information
 * The board information is all the pits and all the stones in the pit and mancala
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
    boolean freeTurn = false;//helper variable for freeTurn
    boolean playerBTurn; // false = Player A, true = player B
    int numToAdd; //number to add to each pit -- standard is 1
    int stonesEmpty; //stones left in the pit after taken from -- standard is 0

    private ArrayList<ChangeListener> listeners;
    Deque<String> stack; //list of moves to help with undo
    private int undoChances; //to decide whether a move has been made, so can undo
    private int playerUndoChances; //player can undo at most 3 times a turn

    
    /**
     * Initialize the board model
     */
    public BoardModel()
    {
        boardA = new int[7];
        boardB = new int[7];
        defaultStones = 0;
        stonesEmpty = 0;

        // Player A always starts first
        playerBTurn = false;

        numToAdd = 1;
        listeners = new ArrayList<ChangeListener>();

        stack = new ArrayDeque<String>();
        undoChances = 0;
        playerUndoChances = 3;
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
  
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : listeners)
            listener.stateChanged(event);
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

    /**
     * Switch the player's turn and update the board
     * the current player now has 3 chances to undo
     */
    public void setTurn()
    {
        playerBTurn = !playerBTurn;
        update();
        playerUndoChances = 3;
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
		int stonesLeft;

		String move = ""; // to keep track of this move

		if (!playerBTurn) // Player A's turn
		{
			stonesLeft = boardA[pit - 1]; // how many stones to move

			move += "A";
			move += pit;
			move += stonesLeft;

			stack.push(move);

			boardA[pit - 1] = stonesEmpty; // all stones are taken from the chosen pit

			// go through board A starting from chosen pit
			goThroughBoardA(stonesLeft, pit);
		}

		else // Player B's turn
		{
			stonesLeft = boardB[pit - 1];

			move += "B";
			move += pit;
			move += stonesLeft;

			stack.push(move);

			boardB[pit - 1] = stonesEmpty;

			goThroughBoardB(stonesLeft, pit);
		}

		update();
		undoChances++;
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
        for (int i = pit; i < boardA.length; i++)
        {
            // If it is our last stone to place and the pit has no stones,
            // steal the opponents stones in the opposite pit
            if (stonesLeft == 1 && boardA[i] == 0 && i != 6 && !playerBTurn && boardB[5-i] != 0)
            {
            		int counterB = 5 - i; 
            		int temp = boardB[counterB]; // gets the stones out of the opposite pit
                 
                //save the board -- special case 
                String move = "S"; 
                move += "A" + i + 0; //original is 0
                move += "A" + 6 + boardA[6]; //mancala for A 
                move += "B" + counterB + temp; //original for B pit 
                
               
                boardB[counterB] = 0; // reset to 0
                boardA[6] = boardA[6] + temp + 1; // puts those stones in the mancala plus the extra one
                stonesLeft--;
                
                stack.push(move);
            }

            else if (stonesLeft > 0)
            {
                if (i == 6 && playerBTurn) // if its Players B turn, skip the Player's A mancala
                {
                    continue;
                }
                //if the last stone is placed into your own mancala, you got a free turn
                if (i == 6 && stonesLeft == 1)
                {
                    freeTurn = true;
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
            if (stonesLeft == 1 && boardB[i] == 0 && i != 6 && playerBTurn && boardA[5 - i]!=0)
            {
                int counterA = 5 - i;
                int temp = boardA[counterA]; // gets the stones out of the opposite pit
                
                //save the board -- special case 
                String move = "S"; 
                move += "B" + i + 0; //original is 0
                move += "B" + 6 + boardB[6]; //mancala for B 
                move += "A" + counterA + temp; //original for opposite A pit 
                
                boardA[counterA] = 0; // reset to 0
                boardB[6] = boardB[6] + temp + 1; // puts those stones in the mancala plus the extra one
                stonesLeft--;
                
                stack.push(move);
            }
            else if (stonesLeft > 0)
            {
                if(i == 6 && !playerBTurn) //if its Players A turn, skip the Player's B mancala
                {
                    continue;
                }
                //if the last stone is placed into your own mancala, you get a free turn
                if (i == 6 && stonesLeft == 1)
                {
                    freeTurn = true;

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
     * Checks if Players A or Bs pits are empty
     * @return emptyPits true for one player
     */
    public String isGameFinished()
    {
        String emptyPits = "";

        //Go through boardA
        for(int i = 0; i < boardA.length -1; i ++)
        {
            //Checks if pit i is empty
            if(boardA[i] == 0 )
            {
                emptyPits = "A";

            }
            else
            {
                emptyPits = "N";
                break;
            }
        }

        //If Player A Pits is not empty
        if (emptyPits.equals("N"))
        {
            //Go through boardB
            for (int i = 0; i < boardB.length - 1; i++)
            {
                //Checks if pit is empty
                if (boardB[i] == 0)
                {
                    emptyPits = "B";
                } else {
                    emptyPits = "N";
                    break;
                }
            }
        }

        // Puts the remaining stones into the players mancala
        int temp = 0;
        if(emptyPits.equals("A"))
        {
            for(int i = 0; i < boardB.length -1; i ++)
            {
                temp += boardB[i];
                boardB[i] = 0;
            }
            boardB[6] += temp;
        }
        else if(emptyPits.equals("B"))
        {
            for(int i = 0; i < boardA.length -1; i ++)
            {
                temp += boardA[i];
                boardA[i] = 0;
            }
            boardA[6] += temp;
        }
        update();
        return emptyPits;
    }

    /**
     * Get how many chances there are undo
     * @return
     */
    public int getUndo()
    {
        return undoChances;
    }

    /**
     * Get how many chances the player has to undo
     * @return
     */
    public int getPlayerUndo()
    {
        return playerUndoChances;
    }

    /**
     * Check is the stack is empty 
     * @return true if it is; false otherwise
     */
    public boolean stackIsEmpty()
    {
        int size = stack.size();
        if(size == 0)
            return true;
        else
            return false;
    }


    /**
     * Empty the stack and reset variable
     */
    public void emptyStack()
    {
        stack.clear();
        undoChances = 0; 
    }


    /**
     * Undo the last move
     * @return true if move is undone
     * @return false is no more chances
     */
    public boolean undo()
    {
        if((undoChances > 0) && (playerUndoChances > 0))
        {
        		String move = stack.pop();
            undoChances--;
            
            String first = move.substring(0,1);
            
            if(first.equals("S"))
            {
            		//special case
            		return undoSpecial(move);
            }

            else 
            {
            		String player = move.substring(0,1);
            		String pitStr = move.substring(1, 2);
                String stonesStr = move.substring(2);

                int pit = Integer.parseInt(pitStr);
                int stone = Integer.parseInt(stonesStr);

                //undo the action by putting stones back in pit and calling placeStones with -1
                numToAdd = -1;
                stonesEmpty = stone;

                //get the board and correct pit
                if(player.equals("A"))
                {
                    boardA[pit - 1] = stone;
                    placeStones(false, pit);
                }

                else //player B
                {
                    boardB[pit - 1] = stone;
                    placeStones(true, pit);
                }

                //because place stones adds to the stack and increment chances
                undoChances--;
                stack.pop();

                //after done
                //reset to default values
                numToAdd = 1;
                stonesEmpty = 0;

                //player has one less chance to undo
                playerUndoChances--;
                update();
                return true;
            }
        }


        else
        {
            update();
            return false;
        }
    }
    
    /**
     * To undo the special cases where stones are stolen 
     * @param move - the string with the move to undo
     * @return whether the move is undone - always true
     */
    public boolean undoSpecial(String move)
    {
    		//undo the other move to return the non special stones 
    		//increment undo chances because they don't lose a chance just because it's special
    		undoChances++;
    		undo(); 
    	
    		//now undo the special case 
    		//Example: S A50 [1,2,3] A61 [4,5,6] B14 [7,8,9]
    		
    		String playerStr = move.substring(1,2); 
    		
    		String pitPlayerStr = move.substring(2,3); 
    		int pitPlayer = Integer.parseInt(pitPlayerStr); 
    		
    		String stonesPlayerStr = move.substring(3,4); 
    		int stonesPlayer = Integer.parseInt(stonesPlayerStr);
    		
    		String mancalaPlayerStr = move.substring(6,7); 
    		int mancalaPlayer = Integer.parseInt(mancalaPlayerStr);
    		
    		String pitOpponentStr = move.substring(8,9); 
    		int pitOpponent = Integer.parseInt(pitOpponentStr);
    		
    		String stonesOpponentStr = move.substring(9); 
    		int stonesOpponent = Integer.parseInt(stonesOpponentStr);
    		
    		if(playerStr.equals("A"))
    		{	
    			//it is player A's move 
    			//restore last pit
    			boardA[pitPlayer] = stonesPlayer; 
    			//restore mancala
    			boardA[6] = mancalaPlayer; 
    			//restore opponent's pit 
    			boardB[pitOpponent] = stonesOpponent;
    		}
    		
    		else
    		{	
    			//it is player B's move 
    			//restore last pit
    			boardB[pitPlayer] = stonesPlayer; 
    			//restore mancala
    			boardB[6] = mancalaPlayer; 
    			//restore opponent's pit 
    			boardA[pitOpponent] = stonesOpponent;
    		}
  
    		update();
    		return true;
    }

    public void setBoardA(int[] boardA) {
        this.boardA = boardA;
    }
    public void setBoardB(int[] boardB) {
        this.boardB = boardB;
    }

    /**
     Adds a change listener to the board model
     @param listener the change listener to add
     */
    public void addChangeListener(ChangeListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Update the data in boardModel
     */
    public void update(){
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : listeners)
            listener.stateChanged(event);
    }
}

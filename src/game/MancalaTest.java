package game;
/**
 * Test class for Mancala. Runs the game.
 * 
 * @author Marietta Asemwota
 * @author Stan Yanakiev
 * @author Yihua Li
 */
public class MancalaTest
{
    public static void main(String [] args)
    {
    		BoardModel boardModel = new BoardModel();
        BoardComponent myFrame = new BoardComponent(boardModel);
        boardModel.addChangeListener(myFrame);
    }
}
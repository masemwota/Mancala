package game;
import java.awt.*;

/**
 * Game board formatter using Strategy pattern
 * @author Marietta Asemwota
 * @author Stan Yanakiev
 * @author Yihua Li
 */
public interface BoardFormatter {
	
	/**
	 * Decide on a shape to format the pits
	 * @return the shape of the pits
	 */
    Shape formatPitShape();

    /**
     * Format the mancala shape
     * @return the shape of the mancala
     */
    Shape formatMancalaShape();

    /**
     * Format the stones in the pits 
     * @param stoneNum - the number of stones in the pit
     * @param stoneIndex - how the stones should be arranged - 
     * @return the shape of the stones in the pit
     */
    Shape formatPitStoneShape(int stoneNum, int stoneIndex);

    /**
     * Format the stones in the mancala 
     * @param stoneNum - the number of stones in the mancala
     * @param stoneIndex - the index of the particular stone to help shaping
     * @return the shape of the mancala stones
     */
    Shape formatMancalaStoneShape(int stoneNum, int stoneIndex);

    /**
     * Format the color of the board 
     * @return the color of the board
     */
    Color formatBoardColor();

    /**
     * Format and choose the color of the pits
     * @return the pit color
     */
    Color formatPitColor();

    /**
     * Format and choose the color of the stones
     * @return the stone color
     */
    Color formatStoneColor();

    /**
     * Choose and Format the height of the mancala
     * @return the height
     */
    int formatMancalaHeight();

    /**
     * Choose and Format the width of the mancala 
     * @return the width
     */
    int formatMancalaWidth();

    /**
     * Choose and format the height of the pits
     * @return the pit height
     */
    int formatPitHeight();

    /**
     * Choose and format the width of the pits 
     * @return the pit width
     */
    int formatPitWdith();
}
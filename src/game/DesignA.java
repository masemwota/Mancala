package game;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Style A of the game board
 *
 * @author Yihua Li
 */
public class DesignA implements BoardFormatter {
    final static int MAN_HEIGHT = 200;
    final static int MAN_WIDTH = 100;
    final static int PIT_HEIGHT = 100;
    final static int PIT_WIDTH = 100;
    final static int S_HEIGHT = 10;
    final static int S_WIDTH = 10;
    
    
    
    @Override
    /**
     * Format the pits as an ellipse
     * @return the shape of the pits
     */
    public Shape formatPitShape() {
        return new Ellipse2D.Double(0,0,PIT_WIDTH,PIT_HEIGHT);
    }
    
    @Override
    /**
     * Format the mancala shape as an ellipse
     * @return the shape of the mancala
     */
    public Shape formatMancalaShape() {
        return new Ellipse2D.Double(0,0,MAN_WIDTH,MAN_HEIGHT);
    }
    
    @Override
    /**
     * Format the stones in the pits as rectangles
     * @param stoneNum - the number of stones in the pit
     * @param stoneIndex - how the stones should be arranged -
     * @return the shape of the stones in the pit
     */
    public Shape formatPitStoneShape(int stoneNum, int stoneIndex) {
        double x = (double)PIT_WIDTH / 2 - S_WIDTH / 2;
        double y = (double)PIT_HEIGHT / 2 - S_HEIGHT / 2;
        if (stoneIndex == 0){
            return new Rectangle2D.Double(x, y, S_WIDTH, S_HEIGHT);
        }
        double degree = 360 / (stoneNum - 1) * stoneIndex;
        x += 30 * Math.sin(Math.toRadians(degree));
        y += 30 * Math.cos(Math.toRadians(degree));
        
        return new Rectangle2D.Double(x, y, S_WIDTH, S_HEIGHT);
    }
    
    @Override
    /**
     * Format the stones in the mancala as rectangles
     * @param stoneNum - the number of stones in the mancala
     * @param stoneIndex - the index of the particular stone to help shaping
     * @return the shape of the mancala stones
     */
    public Shape formatMancalaStoneShape(int stoneNum, int stoneIndex){
        double x = (double)MAN_WIDTH / 2 - S_WIDTH / 2;
        double y = (double)MAN_HEIGHT / 2 - S_HEIGHT / 2;
        if (stoneIndex == 0){
            return new Rectangle2D.Double(x, y, S_WIDTH, S_HEIGHT);
        }
        double degree = 360 / (stoneNum - 1) * stoneIndex;
        x += 30 * Math.sin(Math.toRadians(degree));
        y += 30 * Math.cos(Math.toRadians(degree));
        
        return new Rectangle2D.Double(x, y, S_WIDTH, S_HEIGHT);
    }
    
    @Override
    /**
     * Format the color of the board
     * @return the color of the board
     */
    public Color formatBoardColor() {
        return Color.LIGHT_GRAY;
    }
    
    @Override
    /**
     * Format and choose the color of the pits
     * @return the pit color
     */
    public Color formatPitColor() {
        return Color.ORANGE;
    }
    
    @Override
    /**
     * Format and choose the color of the stones
     * @return the stone color
     */
    public Color formatStoneColor() {
        return Color.GRAY;
    }
    
    @Override
    /**
     * Choose and Format the height of the mancala
     * @return the height
     */
    public int formatMancalaHeight() {
        return MAN_HEIGHT;
    }
    
    @Override
    /**
     * Choose and Format the width of the mancala
     * @return the width
     */
    public int formatMancalaWidth() {
        return MAN_WIDTH;
    }
    
    @Override
    /**
     * Choose and format the height of the pits
     * @return the pit height
     */
    public int formatPitHeight() {
        return PIT_HEIGHT;
    }
    
    @Override
    /**
     * Choose and format the width of the pits
     * @return the pit width
     */
    public int formatPitWdith() {
        return PIT_WIDTH;
    }
}

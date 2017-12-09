package game;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Style C of the game board
 * @author Stan Yanakiev
 */
public class DesignC implements BoardFormatter {
    final static int MAN_HEIGHT = 200;
    final static int MAN_WIDTH = 100;
    final static int PIT_HEIGHT = 100;
    final static int PIT_WIDTH = 100;
    final static int S_HEIGHT = 10;
    final static int S_WIDTH = 10;


    @Override
    public Shape formatPitShape() {
        return new Ellipse2D.Double(0,0,PIT_WIDTH,PIT_HEIGHT);
    }

    @Override
    public Shape formatMancalaShape() {
        return new RoundRectangle2D.Double(0,0,MAN_WIDTH,MAN_HEIGHT, 20, 20);
    }

    @Override
    public Shape formatPitStoneShape(int stoneNum, int stoneIndex) {
        double x = (double)PIT_WIDTH / 2 - S_WIDTH / 2;
        double y = (double)PIT_HEIGHT / 2 - S_HEIGHT / 2;
        if (stoneIndex == 0){
            return new RoundRectangle2D.Double(x, y, S_WIDTH, S_HEIGHT, 20, 20);
        }
        double degree = 360 / (stoneNum - 1) * stoneIndex;
        x += 30 * Math.sin(Math.toRadians(degree));
        y += 30 * Math.cos(Math.toRadians(degree));

        return new RoundRectangle2D.Double(x, y, S_WIDTH, S_HEIGHT, 20, 20);
    }

    @Override
    public Shape formatMancalaStoneShape(int stoneNum, int stoneIndex){
        double x = (double)MAN_WIDTH / 2 - S_WIDTH / 2;
        double y = (double)MAN_HEIGHT / 2 - S_HEIGHT / 2;
        if (stoneIndex == 0){
            return new RoundRectangle2D.Double(x, y, S_WIDTH, S_HEIGHT, 20, 20);
        }
        double degree = 360 / (stoneNum - 1) * stoneIndex;
        x += 30 * Math.sin(Math.toRadians(degree));
        y += 30 * Math.cos(Math.toRadians(degree));

        return new RoundRectangle2D.Double(x, y, S_WIDTH, S_HEIGHT, 20, 20);
    }

    @Override
    public Color formatBoardColor() {
        return Color.WHITE;
    }

    @Override
    public Color formatPitColor() {
        return Color.LIGHT_GRAY;
    }

    @Override
    public Color formatStoneColor() {
        return Color.BLACK;
    }

    @Override
    public int formatMancalaHeight() {
        return MAN_HEIGHT;
    }

    @Override
    public int formatMancalaWidth() {
        return MAN_WIDTH;
    }

    @Override
    public int formatPitHeight() {
        return PIT_HEIGHT;
    }

    @Override
    public int formatPitWdith() {
        return PIT_WIDTH;
    }


}
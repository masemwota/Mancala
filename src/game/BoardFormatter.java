package game;
import java.awt.*;

/**
 * Game board formatter using Strategy pattern
 * @author Marietta Asemwota
 * @author Stan Yanakiev
 * @author Yihua Li
 */
public interface BoardFormatter {
    Shape formatPitShape();

    Shape formatMancalaShape();

    Shape formatPitStoneShape(int stoneNum, int stoneIndex);

    Shape formatMancalaStoneShape(int stoneNum, int stoneIndex);

    Color formatBoardColor();

    Color formatPitColor();

    Color formatStoneColor();

    int formatMancalaHeight();

    int formatMancalaWidth();

    int formatPitHeight();

    int formatPitWdith();
}
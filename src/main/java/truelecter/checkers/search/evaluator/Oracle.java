package truelecter.checkers.search.evaluator;

import truelecter.checkers.board.Board;

/**
 * Board evaluation algorithm
 * Initial code : https://github.com/AshishPrasad/Checkers/blob/master/checkers/Oracle.java
 *
 * @author apurv and ashish
 */
public class Oracle implements Evaluator {

    public final int POINT_WON = 100000;
    public final int POINT_KING = 2000;
    public final int POINT_NORMAL = 1000;
    public final int POINT_CENTRAL_PIECE = 100;
    public final int POINT_END_PIECE = 50;
    public final int POINT_DEFENCE = 50;
    public final int POINT_ATTACK_NORMAL = 30;
    public final int POINT_ATTACK_KING = 60;

    private int checkerDifferencePoints(Board board) {
        int value = 0;
        // Scan across the board
        for (byte x = 0; x < 8; x++) {
            // Check only valid cols
            for (byte y = 0; y < 8; y ++) {
                switch (board.getCellValue(y, x)) {
                    case Board.ENEMY_KING:
                        value -= POINT_KING;
                        break;
                    case Board.ENEMY_NORMAL:
                        value -= POINT_NORMAL;
                        break;
                    case Board.OUR_KING:
                        value += POINT_KING;
                        break;
                    case Board.OUR_NORMAL:
                        value += POINT_NORMAL;
                        break;
//                    default:
//                        System.out.println("WHAT?");
                }
            }
        }
        return value;
    }

    @Override
    public int evaluate(Board b, byte turn) {
        if (b.state(turn) == 0) {
            int value = checkerDifferencePoints(b);
         //   System.out.println(value);
            if (turn > 0) {
                value = -value / (b.ourChecker + b.ourKings);
            } else {
                value = value / (b.theirChecker + b.theirKings);
            }
            return value;
        } else {
            switch (b.state(turn)) {
                case -1:
                    return Integer.MIN_VALUE;
                    //      return -POINT_WON * turn;
                case 1:
                    return Integer.MAX_VALUE;
                //TODO evaluation for draw
                default:
                    return Integer.MIN_VALUE;
            }
        }
    }

}

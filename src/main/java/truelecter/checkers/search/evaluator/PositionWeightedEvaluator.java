package truelecter.checkers.search.evaluator;

import truelecter.checkers.board.Board;

/**
 * https://github.com/desht/checkers/blob/master/src/main/java/me/desht/checkers/ai/evaluation/PositionWeightedEvaluator.java
 */
public class PositionWeightedEvaluator implements Evaluator {

    private static final int EDGE_PENALTY = 10;
    private static final int KING_SCORE = 200;
    private static final int PIECE_SCORE = 100;

    private int startColor;

    public PositionWeightedEvaluator(byte starter){
        startColor = starter;
    }

    @Override
    public int evaluate(Board b, byte turn) {
        int score = 0;

        for (byte row = 0; row < 8; row++) {
            int rankVal = startColor == turn ? row : 7 - row;
            for (byte col = 0; col < 8; col++) {
                switch (b.getCellValue(col, row)) {
                    case Board.OUR_NORMAL:
                        score += PIECE_SCORE + Math.pow(rankVal, 2);
                        break;
                    case Board.OUR_KING:
                        score += KING_SCORE;
                        if (row == 0 || row == 7 || col == 0 || col == 7) {
                            score -= EDGE_PENALTY;
                        }
                        break;
                    case Board.ENEMY_NORMAL:
                        score -= PIECE_SCORE + Math.pow(rankVal, 2);
                        break;
                    case Board.ENEMY_KING:
                        score -= KING_SCORE;
                        if (row == 0 || row == 7 || col == 0 || col == 7) {
                            score += EDGE_PENALTY;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return score * turn;
    }

}
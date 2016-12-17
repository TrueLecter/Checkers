package truelecter.checkers.search;

import java.util.List;

import truelecter.checkers.board.Board;
import truelecter.checkers.board.MoveChain;
import truelecter.checkers.search.evaluator.Evaluator;

/**
 * AI implementing Minimax with Alpha Beta pruning
 * 
 * @author TrueLecter
 */
public class MiniMax_Old {
    private Evaluator evaluator;
    private MoveChain bestMove = null;
    private int initialDepth = 0;
    private int iterations = 0;
    
    public MiniMax_Old(Evaluator evaluator) {
        if (evaluator == null)
            throw new IllegalArgumentException("Evaluator can't be null");
        this.evaluator = evaluator;
    }

    public MoveChain perform(Board b, int depth, byte turn) {
        initialDepth = depth;
        iterations = 0;
        alphaBeta(b, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, turn);
        System.out.println("iterations: "+iterations);
        return bestMove;
    }

    private int alphaBeta(Board b, int alpha, int beta, int depth, byte turn) {
        iterations++;
        if (depth == 0) {
//            bestMove = b.getFromMove();
            return evaluator.evaluate(b, turn);
        }
        List<MoveChain> possibleMoves = b.movesFor(turn);
        if (possibleMoves.isEmpty()) {
            // Draw
            bestMove = b.getFromMove();
            return 0;
        }
        // Our turn. Maximize
        //System.out.println(turn);
        if (turn < 0) {
            for (MoveChain move : possibleMoves) {
                // System.out.println("------");
                // System.out.println(move);
                int ab = alphaBeta(new Board(b, move), alpha, beta, depth - 1, (byte) (turn * -1));
                if (ab > alpha) {
                    alpha = ab;
                    if (depth == initialDepth)
                        bestMove = move;
                }
                if (alpha >= beta) {
                    return alpha;
                }
            }
            return alpha;
        }
        if (turn > 0) {
            for (MoveChain move : possibleMoves) {
                int ab = alphaBeta(new Board(b, move), alpha, beta, depth - 1, (byte) (turn * -1));
                if (ab < beta) {
                    beta = ab;
                    if (depth == initialDepth)
                        bestMove = move;
                }
                if (beta <= alpha) {
                    return beta;
                }
            }
            return beta;
        }
        throw new IllegalArgumentException("turn cannot be 0");
    }

}

package truelecter.checkers.search.evaluator;

import truelecter.checkers.board.Board;

public interface Evaluator {
    public int evaluate(Board b, byte turn);
}

package truelecter.checkers.search.evaluator;

import truelecter.checkers.board.Board;

import java.util.Random;

/**
 * Created by TrueLecter on 22.12.2016.
 */
public class RandomOracle implements Evaluator {

    private Random r = new Random();

    public int evaluate(Board b, byte turn) {
        return r.nextInt(100);
    }

}

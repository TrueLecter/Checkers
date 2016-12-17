package truelecter.checkers.search;

import fr.pixelprose.minimax4j.Difficulty;
import fr.pixelprose.minimax4j.IA;
import truelecter.checkers.board.Board;
import truelecter.checkers.board.MoveChain;
import truelecter.checkers.search.evaluator.Evaluator;

import java.util.List;

/**
 * Created by TrueLecter on 16.12.2016.
 */
public class MiniMax extends IA<MoveChain> {

    private byte turn = -1;
    private Board board;
    private Evaluator evaluator;

    private Difficulty difficulty = new Difficulty() {
        @Override
        public int getDepth() {
            return 999;
        }
    };

    public MiniMax setBoard(Board board){
        this.board = board;
        return this;
    }

    public MiniMax(Evaluator evaluator) {
        super(Algorithm.MINIMAX);
        this.evaluator = evaluator;
    }

    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public boolean isOver() {
        return board.state(turn) != 0;
    }

    @Override
    public void makeMove(MoveChain moves) {
        //System.out.println("board == null = " + (board == null));
        board = board.makeMove(moves);
        next();
    }

    @Override
    public void unmakeMove(MoveChain moves) {
        board = board.getFromBoard();
        previous();
    }

    @Override
    public List<MoveChain> getPossibleMoves() {
        return board.movesFor(turn);
    }

    @Override
    public double evaluate() {
        //System.out.println(evaluator.evaluate(board, turn));
        return evaluator.evaluate(board, turn);
    }

    @Override
    public double maxEvaluateValue() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void next() {
        turn *= -1;
    }

    @Override
    public void previous() {
        turn *= -1;
    }
}

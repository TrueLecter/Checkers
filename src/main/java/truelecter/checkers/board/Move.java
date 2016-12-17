package truelecter.checkers.board;

import truelecter.checkers.board.Board.Cell;

public class Move {
    public Cell from;
    public Cell to;
    public Cell captured;

    public Move(Cell from, Cell to) {
        this(from, to, null);
    }

    public Move(Cell from, Cell to, Cell captured) {
        this.from = from;
        this.to = to;
        this.captured = captured;
    }

    @Override
    public String toString() {
        return "Move{" + from + " -> " + to + (captured != null ? (", captured: " + captured) : "") + "}";
    }
    
    public boolean isEndMove(){
        return from.x == to.x && from.y == to.y;
    }
}

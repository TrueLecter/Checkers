package truelecter.checkers.board;

import java.util.LinkedList;
import java.util.List;

/**
 * Board class for checkers. We are always playing as a bottom player.
 *
 * @author TrueLecter
 */
// □ ■ □ ■ □ ■ □ ■
// ■ □ ■ □ ■ □ ■ □
// □ ■ □ ■ □ ■ □ ■
// ■ □ ■ □ ■ □ ■ □
// □ ■ □ ■ □ ■ □ ■
// ■ □ ■ □ ■ □ ■ □
// □ ■ □ ■ □ ■ □ ■
// ■ □ ■ □ ■ □ ■ □

public class Board {
    // 10 - enemy king
    // 1 - enemy
    // 0 - free cell
    // -1 - we
    // -10 - our king
    public static final byte ENEMY_KING = 10;
    public static final byte ENEMY_NORMAL = 1;
    public static final byte OUR_KING = -10;
    public static final byte OUR_NORMAL = -1;

    private byte[][] board = new byte[8][8];
    private MoveChain fromMove = null;
    private Board fromBoard = null;

    public int ourChecker;
    public int ourKings;
    public int theirChecker;
    public int theirKings;

    public Board(byte[][] board) {
        for (byte t = 0; t < 8; t += 2) {
            for (byte x = 0; x < 8; x++) {
                byte y = (byte) (x % 2 + t);
                byte c = board[x][y];
                if (c != 0) {
                    byte sign = sign(board[x][y]);
                    if (board[x][y] * sign > 1) {
                       // this.board[x][y] = (byte) (10 * sign);
                        if (sign > 0) {
                            theirKings++;
                        } else {
                            ourKings++;
                        }
                    } else {
                        //this.board[x][y] = sign;
                        if (sign > 0) {
                            theirChecker++;
                        } else {
                            ourChecker++;
                        }
                    }
                }
            }
        }
        this.board = board;
    }

    public Board(Board previous, MoveChain multiMove) {
        for (byte y = 0; y < 8; y++) {
            for (byte x = 0; x < 8; x++) {
                board[x][y] = previous.board[x][y];
                switch (board[x][y]) {
                    case Board.ENEMY_KING:
                        theirKings++;
                        break;
                    case Board.ENEMY_NORMAL:
                        theirChecker++;
                        break;
                    case Board.OUR_KING:
                        ourKings++;
                        break;
                    case Board.OUR_NORMAL:
                        ourChecker++;
                        break;
                }
            }
        }
        fromMove = multiMove;
        makeMove(multiMove);
        fromBoard = previous;
    }

    public Board(Board previous, Move move) {
        this(previous, new MoveChain(move));
    }

    public MoveChain getFromMove() {
        return fromMove;
    }

    public Board getFromBoard() {
        return fromBoard == null ? this : fromBoard;
    }

    public Board makeMove(MoveChain multiMove) {
        for (int i = 0; i < multiMove.size(); i++) {
            Move m = multiMove.get(i);
            board[m.to.x][m.to.y] = board[m.from.x][m.from.y];
            board[m.from.x][m.from.y] = 0;
            if (m.captured != null) {
                board[m.captured.x][m.captured.y] = 0;
            }
        }
        return this;
    }

    public Board unmakeMove(MoveChain multiMove) {
        byte moved = multiMove.get(multiMove.size() - 1).to.value();
        for (int i = multiMove.size(); i > 0; i--) {
            Move m = multiMove.get(i);
            board[m.from.x][m.from.y] = board[m.to.x][m.to.y];
            board[m.to.x][m.to.y] = 0;
            if (m.captured != null) {
                board[m.captured.x][m.captured.y] = (byte) -moved;
            }
        }
        return this;
    }

    public List<MoveChain> movesFor(byte who) {
        if (who == 0) {
            throw new IllegalArgumentException("Idk who is it");
        }
        List<MoveChain> moves = new LinkedList<>();
        boolean onlyBite = false;
        byte sign = sign(who);
        for (byte t = 0; t < 8; t += 2) {
            for (byte x = 0; x < 8; x++) {
                byte y = (byte) (x % 2 + t); // (0,0) (1,1) (2,0) (3,1)
                // Move only our checkers
                if (board[x][y] != 0 && sign(board[x][y]) == sign) {
                    MoveType mt = loopAction(false, x, y);
                    if (mt.isBite && !onlyBite) {
                        onlyBite = true;
                        moves.clear();
                    }
                    if (onlyBite) {
                        if (mt.isBite) {
                            moves.addAll(mt.moves);
                        }
                    } else {
                        moves.addAll(mt.moves);
                    }
                }
            }
        }
        return moves;
    }

    private MoveType loopAction(boolean onlyBite, byte x, byte y) {
        List<MoveChain> moves = new LinkedList<>();
        List<Cell>[] res = movesFor(x, y);
        List<Cell> biteable = res[0];
        Cell self = biteable.remove(0);
        if (biteable.isEmpty() && !onlyBite) {
            // Noone to bite
            for (Cell dest : res[1]) {
                moves.add(new MoveChain(new Move(self, dest)));
            }
            return new MoveType(moves, false);
        } else {
            for (Cell dest : biteable) {
                // moves.add(new MoveChain(new Move(self, dest)));
                List<Move> movez = new LinkedList<>();
                boolean isMan = self.value() == 1 || self.value() == -1;
                Cell t = new Cell(dest);
                switch (t.from) {
                    case LEFT_UP:
                        while (t.isLegal()) {
                            movez.add(new Move(self, (t = t.rd()), dest));
                            if (isMan || t.isChecker()) {
                                break;
                            }
                        }
                        break;
                    case RIGHT_UP:
                        while (t.isLegal()) {
                            movez.add(new Move(self, (t = t.ld()), dest));
                            if (isMan || t.isChecker()) {
                                break;
                            }
                        }
                        break;
                    case LEFT_DOWN:
                        while (t.isLegal()) {
                            movez.add(new Move(self, (t = t.ru()), dest));
                            if (isMan || t.isChecker()) {
                                break;
                            }
                        }
                        break;
                    case RIGHT_DOWN:
                        while (t.isLegal()) {
                            movez.add(new Move(self, (t = t.lu()), dest));
                            if (isMan || t.isChecker()) {
                                break;
                            }
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown direction");
                }

                // if (!isMan)
                // movez.remove(movez.size() - 1);
                for (Move m : movez) {
                    if (m.to.isLegal() && m.to.value() == 0) {
                        Board helpBoard = new Board(this, m);
                        List<MoveChain> movezz = helpBoard.loopAction(true, m.to.x, m.to.y).moves;
                        if (movezz.isEmpty()) {
                            moves.add(new MoveChain(m));
                        } else {
                            for (MoveChain mc : movezz) {
                                moves.add(new MoveChain(m).append(mc));
                            }
                        }
                    }
                }

            }
            return new MoveType(moves, true);
        }
    }

    /**
     * Test all possible move cells if there is any we can bite
     *
     * @return List of cell containing enemies we can bite
     */
    @SuppressWarnings("unchecked")
    private List<Cell>[] movesFor(byte x, byte y) {
        Cell c = new Cell(x, y);
        List<Cell> biteable = new LinkedList<>();
        List<Cell> moves = new LinkedList<>();
        // Add self not to create new one in future
        biteable.add(c);
        byte sign = sign(c.value());
        if (sign * c.value() == 10) {
            Cell t = c.lu();
            while (t.isLegal()) {
                if (t.isChecker()) {
                    if (sign(t.value()) != sign) {
                        if ((t = t.lu()).isLegal() && t.value() == 0)
                            biteable.add(t.rd());
                        break;
                    }
                } else {
                    moves.add(t);
                }
                t = t.lu();
            }
            t = c.ld();
            while (t.isLegal()) {
                if (t.isChecker()) {
                    if (sign(t.value()) != sign) {
                        if ((t = t.ld()).isLegal() && t.value() == 0)
                            biteable.add(t.ru());
                        break;
                    }
                } else {
                    moves.add(t);
                }
                t = t.ld();
            }
            t = c.ru();
            while (t.isLegal()) {
                if (t.isChecker()) {
                    if (sign(t.value()) != sign) {
                        if ((t = t.ru()).isLegal() && t.value() == 0)
                            biteable.add(t.ld());
                        break;
                    }
                } else {
                    moves.add(t);
                }
                t = t.ru();
            }
            t = c.rd();
            while (t.isLegal()) {
                if (t.isChecker()) {
                    if (sign(t.value()) != sign) {
                        if ((t = t.rd()).isLegal() && t.value() == 0)
                            biteable.add(t.lu());
                        break;
                    }
                } else {
                    moves.add(t);
                }
                t = t.rd();
            }
        } else {
            Cell t = c.lu();
            // Test if cell is in board bounds, checker in this cell is enemy
            // and next cell is free
            if (t.isLegal())
                if (t.isChecker()) {
                    if (sign(t.value()) != sign && (t = t.lu()).isLegal() && t.value() == 0) {
                        biteable.add(t.rd());
                    }
                } else {
                    if (sign == -1) {
                        moves.add(t);
                    }
                }
            t = c.ld();
            if (t.isLegal())
                if (t.isChecker()) {
                    if (sign(t.value()) != sign && (t = t.ld()).isLegal() && t.value() == 0)
                        biteable.add(t.ru());
                } else {
                    if (sign == 1) {
                        moves.add(t);
                    }
                }
            t = c.ru();
            if (t.isLegal())
                if (t.isChecker()) {
                    if (sign(t.value()) != sign && (t = t.ru()).isLegal() && t.value() == 0)
                        biteable.add(t.ld());
                } else {
                    if (sign == -1) {
                        moves.add(t);
                    }
                }
            t = c.rd();
            if (t.isLegal())
                if (t.isChecker()) {
                    if (sign(t.value()) != sign && (t = t.rd()).isLegal() && t.value() == 0)
                        biteable.add(t.lu());
                } else {
                    if (sign == 1) {
                        moves.add(t);
                    }
                }
        }
        return new List[]{biteable, moves};
    }

    /**
     * Returns current board state
     *
     * @return 0 - if still playing</br>
     * -1 - if we won</br>
     * 1 - if enemy won</br>
     * -128 - if draw
     */
    public byte state(byte turn) {
        int our;
        if ((our = (ourKings + ourChecker)) == 0) {
            return 1;
        }
        int their;
        if ((their = (theirKings + theirChecker)) == 0) {
            return -1;
        }
        if ((our == their && their == 1 && ourKings == theirKings) || movesFor(turn).isEmpty()) {
            return 0;
        }
        return 0;
    }

    public class Cell {
        public byte x;
        public byte y;
        public Direction from;

        public Cell(byte x, byte y) {
            this.x = x;
            this.y = y;
        }

        public Cell(Cell c) {
            this(c.x, c.y);
            setDirection(c.from);
        }

        public Cell add(byte x, byte y) {
            this.x += x;
            this.y += y;
            return this;
        }

        public Cell substract(byte x, byte y) {
            this.x -= x;
            this.y -= y;
            return this;
        }

        public Cell lu() {
            return lu((byte) 1);
        }

        public Cell lu(byte b) {
            return new Cell(this).substract(b, b).setDirection(Direction.LEFT_UP);
        }

        public Cell ld() {
            return ld((byte) 1);
        }

        public Cell ld(byte b) {
            return new Cell(this).substract((byte) -b, b).setDirection(Direction.LEFT_DOWN);
        }

        public Cell rd() {
            return rd((byte) 1);
        }

        public Cell rd(byte b) {
            return new Cell(this).add(b, b).setDirection(Direction.RIGHT_DOWN);
        }

        public Cell ru() {
            return ru((byte) 1);
        }

        public Cell ru(byte b) {
            return new Cell(this).add((byte) -b, b).setDirection(Direction.RIGHT_UP);
        }

        public boolean isChecker() {
            return isLegal() && (Board.this.board[x][y] != 0);
        }

        public byte value() {
            if (isLegal()) {
                return Board.this.board[x][y];
            }
            // Illegal cell
            return -128;
        }

        public boolean isLegal() {
            return Board.isLegal(x, y);
        }

        private Cell setDirection(Direction d) {
            from = d;
            return this;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }

    }

    static boolean isLegal(byte x, byte y) {
        return x > -1 && x < 8 && y > -1 && y < 8;
    }

    private class MoveType {
        List<MoveChain> moves;
        boolean isBite;

        MoveType(List<MoveChain> moves, boolean isBite) {
            this.moves = moves;
            this.isBite = isBite;
        }
    }

    public static enum Direction {
        LEFT_UP, LEFT_DOWN, RIGHT_UP, RIGHT_DOWN, INITIAL;

        public Direction negate() {
            switch (this) {
                case LEFT_UP:
                    return RIGHT_DOWN;
                case LEFT_DOWN:
                    return RIGHT_UP;
                case RIGHT_UP:
                    return LEFT_DOWN;
                case RIGHT_DOWN:
                    return LEFT_UP;
                default:
                    return INITIAL;
            }
        }
    }

    private byte sign(byte b) {
        return (byte) (b > 0 ? 1 : -1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                switch (board[y][x]) {
                    case 1:
                        sb.append('b');
                        break;
                    case 10:
                        sb.append('B');
                        break;
                    case -1:
                        sb.append('w');
                        break;
                    case -10:
                        sb.append('W');
                        break;
                    default:
                        sb.append('.');
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Cell getCell(byte x, byte y) {
        return new Cell(x, y);
    }

    public byte getCellValue(byte x, byte y) {
        return isLegal(x, y) ? this.board[x][y] : -128;
    }

}

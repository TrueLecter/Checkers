package truelecter.checkers.board.test;

import java.util.List;

import checkers.pojo.checker.CheckerColor;
import truelecter.checkers.board.Board;
import truelecter.checkers.board.BoardPrinter;
import truelecter.checkers.board.MoveChain;

public class BoardTest {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        int[][] boarda = new int[][]{ //
                {+1, -0, +1, -0, +1, -0, +1, -0}, //
                {-0, +1, -0, +1, -0, +1, -0, +1}, //
                {+1, -0, +1, -0, +1, -0, -0, -0}, //
                {-0, -0, -0, -0, -0, -0, -0, +1}, //
                {-0, -0, -0, -0, -0, -0, -0, -0}, //
                {-0, -1, -0, -1, -0, -1, -0, -1}, //
                {-1, -0, -1, -0, -1, -0, -1, -0}, //
                {-0, -1, -0, -1, -0, -1, -0, -1} //
        };

        int[][] boardb = new int[][]{ //
                {0, 0, 0, 0, 0, 0, 0, 0}, //
                {0, 0, 0, 0, 0, 0, 0, 0}, //
                {0, 0, 1, 0, 0, 0, -1, 0}, //
                {0, 0, 0, -1, 0, -1, 0, 0}, //
                {0, 0, 0, 0, 10, 0, -1, 0}, //
                {0, 0, 0, 0, 0, -1, 0, 0}, //
                {0, 0, 0, 0, 0, 0, 0, 0}, //
                {0, 0, 0, 0, 0, 0, 0, 0} //
        };

        int[][] boardc = new int[][]{ //
                {0, 0, 0, 0, 0, 0, 0, 0}, //
                {0, -10, 0, 0, 0, 0, 0, 0}, //
                {0, 0, 1, 0, 1, 0, 0, 0}, //
                {0, 0, 0, 0, 0, 0, 0, 0}, //
                {0, 0, 0, 0, 0, 0, 0, 0}, //
                {0, 0, 0, 0, 0, 0, 0, 0}, //
                {0, 0, 0, 0, 0, 0, 1, 0}, //
                {0, 0, 0, 0, 0, 0, 0, 0} //
        };

        int[][] boardi = boardc;

        byte[][] boardbyte = new byte[8][8];
        for (int i = 0; i < boardi.length; i++) {
            for (int j = 0; j < boardi[i].length; j++) {
                boardbyte[i][j] = (byte) boardi[i][j];
            }
        }

        Board b = new Board(boardbyte);
        System.out.println(b);
        byte forr = -1;
        System.out.println(BoardPrinter.print(b, forr, CheckerColor.BLACK));
        List<MoveChain> moves = b.movesFor(forr);
        System.out.println("Moves size: " + moves.size());
        //System.out.println(BoardPrinter.print(b, forr, CheckerColor.BLACK));
        for (MoveChain m : moves) {
            System.out.println("------------");
            System.out.println(m);
            System.out.println(BoardPrinter.print(new Board(b, m), forr, CheckerColor.BLACK));
//            System.out.println(new Board(b, m));
        }
    }
}

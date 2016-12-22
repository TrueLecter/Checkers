package truelecter.checkers.client;

import java.util.Collections;

import checkers.client.CheckersBot;
import checkers.client.Client;
import checkers.pojo.board.Letters;
import checkers.pojo.board.Numbers;
import checkers.pojo.checker.CheckerColor;
import checkers.pojo.checker.Position;
import checkers.pojo.step.Step;
import checkers.pojo.step.StepUnit;
import com.sheremet.checkers.client.BoardRenderer;
import truelecter.checkers.board.Board;
import truelecter.checkers.board.BoardPrinter;
import truelecter.checkers.board.MoveChain;
import truelecter.checkers.search.MiniMax;
import truelecter.checkers.search.evaluator.Oracle;
import static truelecter.checkers.client.util.Translator.*;

public class AIPlayerClient {

    private static class BooleanWrapper {
        private boolean value;

        BooleanWrapper(boolean b) {
            value = b;
        }

        boolean set(boolean v) {
            value = v;
            return value;
        }
    }

    private static CheckerColor myColor = null;

    public static void main(final String[] args) {
        int port;
        try {
            port = args.length > 1 ? Integer.valueOf(args[1]) : 3000;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Using default port: 3000");
            port = 3000;
        }
//        String ip = args.length > 0 ? args[0] : "192.168.0.100";
        String ip = args.length > 0 ? args[0] : "localhost";

        //final MiniMax_Old mm = new MiniMax_Old(new Oracle());
        final MiniMax mm = new MiniMax(new Oracle());
        final BooleanWrapper isWhite = new BooleanWrapper(false);
        final BooleanWrapper asWhiteFirstMoveDone = new BooleanWrapper(false);

        final BoardRenderer visual = new BoardRenderer();

        Client client = new Client(ip, port, new CheckersBot() {
            @Override
            public void onGameStart(CheckerColor checkerColor) {
                System.out.println(checkerColor);
                isWhite.set(checkerColor == CheckerColor.WHITE);
                myColor = checkerColor;
            }

            @Override
            public Step next(checkers.pojo.board.Board board) {
                System.out.println("My turn");
                Board b = translate(board, isWhite.value, myColor);
//                System.out.println(b);
//                System.out.println(b.state((byte) -1));
//                System.out.println(b.movesFor((byte) -1));
                System.out.println(BoardPrinter.print(board));
//                if (isWhite.value && !asWhiteFirstMoveDone.value) {
//                    asWhiteFirstMoveDone.value = true;
//                    return FIRST_MOVE_WHITE;
//                }
                //MoveChain mc = mm.perform(b, (byte) 4, (byte) -1);
                MoveChain mc = mm.setBoard(b).getBestMove();
                System.out.println("Choosen move: " + mc);
                return translate(mc, isWhite.value);
            }

            @Override
            public void onGameEnd(String s) {
                System.out.println(s);
            }

            @Override
            public String clientBotName() {
                return args.length > 2 ? args[2] : "?";
            }

            @Override
            public void show(checkers.pojo.board.Board board) {
                BoardPrinter.print(board);
                visual.render(board);
            }
        });
        client.run();
    }

    private static final Step FIRST_MOVE_WHITE = new Step(
            Collections.singletonList(new StepUnit(new Position(Letters.B, Numbers._6), new Position(Letters.A, Numbers._5))));

}
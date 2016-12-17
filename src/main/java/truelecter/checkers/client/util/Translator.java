package truelecter.checkers.client.util;

import checkers.pojo.board.Letters;
import checkers.pojo.board.Numbers;
import checkers.pojo.checker.Checker;
import checkers.pojo.checker.CheckerColor;
import checkers.pojo.checker.CheckerType;
import checkers.pojo.checker.Position;
import checkers.pojo.step.Step;
import checkers.pojo.step.StepUnit;
import truelecter.checkers.board.Board;
import truelecter.checkers.board.Move;
import truelecter.checkers.board.MoveChain;

/**
 * Created by TrueLecter on 11.12.2016.
 */
public class Translator {
    public static Board translate(checkers.pojo.board.Board b, boolean isWhite, CheckerColor myColor) {
        byte[][] rb = new byte[8][8];

        for (Checker cc : b.getCheckers()) {
            if (!isWhite) {
                Position p = cc.getPosition();
                byte c = (byte) (p.getLetter().getValue() - 1);
                byte r = (byte) (p.getNumber().getValue() - 1);
                int s = (cc.getColor() == CheckerColor.BLACK ? -1 : 1);
                int m = (cc.getType() == CheckerType.QUEEN ? 10 : 1);

                rb[r][c] = (byte) (s * m);
            } else {
                Position p = cc.getPosition();
                byte c = (byte) (8 - p.getLetter().getValue());
                byte r = (byte) (8 - p.getNumber().getValue());
                int s = (cc.getColor() == CheckerColor.WHITE ? -1 : 1);
                int m = (cc.getType() == CheckerType.QUEEN ? 10 : 1);

                rb[r][c] = (byte) (s * m);

            }
        }
        //System.out.println("Converted: ");
        Board res = new Board(rb);
        //System.out.println(res);
        return res;
    }

    public static byte[] translate(Position p, boolean isWhite) {
        byte rx, ry;
        if (isWhite) {
            // rx = (byte) (8 - p.getLetter().getValue());
            // ry = (byte) (p.getNumber().getValue() - 1);
            rx = (byte) (8 - p.getLetter().getValue());
            ry = (byte) (8 - p.getNumber().getValue());
        } else {
            // rx = (byte) (p.getLetter().getValue() - 1);
            // ry = (byte) (8 - p.getNumber().getValue());
            rx = (byte) (p.getLetter().getValue() - 1);
            ry = (byte) (p.getNumber().getValue() - 1);
        }
        return new byte[]{ry, rx};
    }

    public static Step translate(MoveChain mc, boolean isWhite) {
        Step s = new Step();
        for (int i = 0; i < mc.size(); i++) {
            Move m = mc.get(i);
            s.addStep(new StepUnit(translate(m.from, isWhite), translate(m.to, isWhite)));
            //   System.out.println(s);
        }
        System.out.println(s.getSteps().get(0).getFrom() + " -> " + s.getSteps().get(0).getTo());
        return s;
    }

    public static Position translate(Board.Cell c, boolean isWhite) {
        Letters l;
        Numbers n;
        if (!isWhite) {
//            l = Letters.getByValue(c.x + 1);
//            n = Numbers.getByValue(c.y + 1);
            l = Letters.getByValue(c.y + 1);
            n = Numbers.getByValue(c.x + 1);
        } else {
//            l = Letters.getByValue(8 - c.x);
//            n = Numbers.getByValue(8 - c.y);
            l = Letters.getByValue(8 - c.y);
            n = Numbers.getByValue(8 - c.x);
        }
        return new Position(l, n);
    }
}

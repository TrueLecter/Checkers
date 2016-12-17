package truelecter.checkers.board;

import checkers.pojo.board.Letters;
import checkers.pojo.board.Numbers;
import checkers.pojo.checker.Checker;
import checkers.pojo.checker.CheckerColor;
import checkers.pojo.checker.CheckerType;
import checkers.pojo.checker.Position;
import truelecter.checkers.client.util.Translator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrueLecter on 11.12.2016.
 */
public class BoardPrinter {
    public static String print(checkers.pojo.board.Board board) {
        StringBuilder result = new StringBuilder();
//top letter labels
        for (Letters letters : Letters.values())
            result.append("   ").append(letters.toString().replace("_", ""));
        result.append('\n');
//main body
        for (Numbers num : Numbers.values()) {
            result.append(' ');
            for (int i = 0; i < Letters.values().length - 1; ++i)
                result.append("+---");
            result.append("+---+\n");
            result.append(num.toString().replace("_", ""));
            result.append('|');
            for (Letters let : Letters.values()) {
                result.append(' ');
                result.append(getLetter(board.get(new Position(let, num))));
                result.append(" |");
            }
            result.append('\n');
        }
//bottom of the board
        result.append(' ');
        for (int i = 0; i < Letters.values().length - 1; ++i)
            result.append("+---");
        result.append("+---+\n");
        return result.toString();
    }

    //print checker
    private static char getLetter(Checker checker) {
        if (checker == null)
            return ' ';
        else if (checker.getColor() == CheckerColor.BLACK)
            return (checker.getType() == CheckerType.SIMPLE) ? 'b' : 'B';
        else
            return (checker.getType() == CheckerType.SIMPLE) ? 'w' : 'W';
    }

    public static String print(Board b, byte me, CheckerColor myColor) {
        return print(translate(b, me, myColor));
    }

    private static checkers.pojo.board.Board translate(Board b, byte me, CheckerColor myColor) {
        byte cellValue;
        List<Checker> c = new ArrayList<>();
        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                cellValue = b.getCellValue(x, y);
                if (cellValue != 0 && cellValue != -128) {
                    c.add(new Checker(me == Math.signum(b.getCellValue(x, y)) ? myColor : myColor.opposite(),
                                    Math.abs(cellValue) == 1 ? CheckerType.SIMPLE : CheckerType.QUEEN,
                                    Translator.translate(b.getCell(x, y), myColor == CheckerColor.WHITE)));
                }
            }
        }
        checkers.pojo.board.Board r =new checkers.pojo.board.Board();
        r.getCheckers().clear();
        r.getCheckers().addAll(c);
        return r;
    }

}
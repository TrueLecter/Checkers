package truelecter.checkers.board;

import java.util.LinkedList;

public class MoveChain extends LinkedList<Move> implements fr.pixelprose.minimax4j.Move {
    private static final long serialVersionUID = -5593230106875934557L;

    public MoveChain(Move m) {
        super();
        add(m);
    }

    @Override
    public String toString(){
        StringBuilder res = new StringBuilder("MoveChain{");
        for (Move m : this){
            if (m == getLast()){
                res.append(m).append("}");
                break;
            }
            res.append(m).append(" -> ");
        }
        return res.toString();
    }

    public MoveChain append(MoveChain mc) {
        for (Move m : mc){
            add(m);
        }
        return this;
    }

}

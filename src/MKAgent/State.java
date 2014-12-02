package MKAgent;

import java.util.ArrayList;

public class State {
    public boolean isMyTurn;
    public Side mySide;
    // This is the move that led to the state of board
    public Move move;
    public Board board;

    public State(boolean isMyTurn, Board board, Side side, Move move) {
        this.board = board;
        this.isMyTurn = isMyTurn;
        this.mySide = side;
        this.move = move;
    }

    public boolean isEndState() {
        return Kalah.gameOver(this.board);
    }

    public double evaluate() {
        return board.getSeedsInStore(mySide) - board.getSeedsInStore(mySide.opposite());
    }

    public ArrayList<State> getChildStates() {
        ArrayList<State> states = null;
        try {
            states = new ArrayList<State>();

            for (int hole = 1; hole <= board.getNoOfHoles(); hole++) {
                Move move = new Move(mySide, hole);
                if (Kalah.isLegalMove(board, move)) {
                    Board newBoard = board.clone();
                    Kalah.makeMove(newBoard, move);
                    states.add(new State(!isMyTurn, newBoard, mySide.opposite(), move));
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        // So far, states doesn't consider making an extra move. Need to add code to add the
        // extra states here.
        return states;
    }
}

package MKAgent;

import java.util.ArrayList;

public class State {
    public Side mySide;
    // This is the move that led to the state of board
    public Move move;
    public Board board;

    public State(Board board, Side side, Move move) {
        this.board = board;
        this.mySide = side;
        this.move = move;
    }

    public boolean isEndState() {
        return Kalah.gameOver(this.board);
    }

    public double evaluate(boolean isMyTurn) {
        double score = 0;
        double myStoreSeeds = board.getSeedsInStore(mySide);
        double oppStoreSeeds = board.getSeedsInStore(mySide.opposite());

        score = 2 * ((isMyTurn) ? myStoreSeeds - oppStoreSeeds : oppStoreSeeds - myStoreSeeds);

        for (int hole = 1; hole <= board.getNoOfHoles(); hole++)
            if (board.getSeeds(mySide, hole) == 0 && isSeedable(mySide, hole))
                if (isMyTurn)
                    score += board.getSeedsOp(mySide, hole) / 2.0;
                else
                    score -= board.getSeedsOp(mySide, hole) / 2.0;

        // for (int hole = 1; hole <= board.getNoOfHoles(); hole++)
        //     if (board.getSeeds(mySide.opposite(), hole) == 0 && isSeedable(mySide.opposite(), hole))
        //         if (isMyTurn)
        //             score -= board.getSeedsOp(mySide.opposite(), hole) / 2.0;
        //         else

        int mySideSeeds = 0, oppSideSeeds = 0;
        for (int i = 1; i <= board.getNoOfHoles(); i++) {
            mySideSeeds += board.getSeeds(mySide, i);
            oppSideSeeds += board.getSeeds(mySide.opposite(), i);
        }

        score += ((isMyTurn) ? (mySideSeeds - oppSideSeeds) : (oppSideSeeds - mySideSeeds)) / 3.0;

        for (int i = 1; i <= board.getNoOfHoles(); i++) {
            if (board.getNoOfHoles() - i + 1 == board.getSeeds(mySide, i))
                if (isMyTurn)
                    score += 1;
                else
                    score -= 1;
        }


        // System.err.println("Evaluation:");
        // System.err.println("==================");
        // System.err.println("The board: ");
        // System.err.println(board);
        // System.err.println("My turn? :" + isMyTurn);
        // System.err.println("Side :" + mySide);
        // System.err.println("Score: " + score);
        // System.err.println("==================");

        return score;
    }

    public boolean isSeedable(Side side, int hole)
    {
        // Left side
        for (int i = hole - 1; i > 0; i--)
            if (hole - i == board.getSeeds(side, i))
                return true;
        // Right side
        for (int i = hole + 1; i <= board.getNoOfHoles(); i++)
        {
            if (15 - (i - hole) == board.getSeeds(side, i))
                return true;
        }
        return false;
    }

    public double evaluateEndState(boolean isMyTurn) {
        double myStoreSeeds = board.getSeedsInStore(mySide);
        double oppStoreSeeds = board.getSeedsInStore(mySide.opposite());
        if (isMyTurn)
            return (myStoreSeeds > oppStoreSeeds) ? 1000 : myStoreSeeds - oppStoreSeeds;
        else
            return (oppStoreSeeds > myStoreSeeds) ? 1000 : oppStoreSeeds - myStoreSeeds;
    }

    public ArrayList<State> getChildStates() {
        ArrayList<State> states = null;
        try {
            states = new ArrayList<State>();

            for (int hole = 1; hole <= board.getNoOfHoles(); hole++) {
                Move move = new Move(mySide, hole);
                if (Kalah.isLegalMove(board, move)) {
                    Board newBoard = board.clone();
                    Side side = Kalah.makeMove(newBoard, move);
                    states.add(new State(newBoard, side, move));
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

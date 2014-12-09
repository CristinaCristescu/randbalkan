package MKAgent;

import java.util.ArrayList;

public class State {
    public static int counter = 0;
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
        // if (isMyTurn) {
        //     // System.err.println("My Side: " + (mySide == Side.NORTH ? "North" : "South"));
        //     return board.getSeedsInStore(mySide) - board.getSeedsInStore(mySide.opposite());
        // }
        // else {
        //     // System.err.println("My Side: " + (mySide.opposite() == Side.NORTH ? "North" : "South"));
        //     return board.getSeedsInStore(mySide.opposite()) - board.getSeedsInStore(mySide);
        // }

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

        for (int hole = 1; hole <= board.getNoOfHoles(); hole++)
            if (board.getSeeds(mySide.opposite(), hole) == 0 && isSeedable(mySide.opposite(), hole))
                if (isMyTurn)
                    score -= board.getSeedsOp(mySide.opposite(), hole) / 2.0;
                else
                    score += board.getSeedsOp(mySide.opposite(), hole) / 2.0;


        int mySideSeeds = 0, oppSideSeeds = 0;
        for (int i = 1; i <= board.getNoOfHoles(); i++) {
            mySideSeeds += board.getSeeds(mySide, i);
            oppSideSeeds += board.getSeeds(mySide.opposite(), i);
        }

        if (myStoreSeeds + oppStoreSeeds >= 80)
            score += ((isMyTurn) ? (mySideSeeds - oppSideSeeds) : (oppSideSeeds - mySideSeeds));

        // for (int i = 1; i <= board.getNoOfHoles(); i++) {
        //     if (board.getNoOfHoles() - i + 1 == board.getSeeds(mySide, i))
        //         if (isMyTurn)
        //             score += 1;
        //         else
        //             score -= 1;
        // }

        // score += leftMinusRight(board,mySide);
        // score -= leftMinusRight(board,mySide.opposite());

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

    public ArrayList<State> getChildStates() {
        counter++;
        // System.err.println("counter :" + counter);
        // System.err.println("my side is " + mySide);
        // System.err.println("The board is \n" + board);
        ArrayList<State> states = null;
        try {
            states = new ArrayList<State>();

            for (int hole = 1; hole <= board.getNoOfHoles(); hole++) {
                Move move = new Move(mySide, hole);
                if (Kalah.isLegalMove(board, move)) {
                    Board newBoard = board.clone();
                    Side side = Kalah.makeMove(newBoard, move);
                    // System.err.println("Possible next move side is: " + side);
                    // System.err.println(newBoard);
                    if (side == mySide)
                        states.add(new State(isMyTurn, newBoard, side, move));
                    else
                        states.add(new State(!isMyTurn, newBoard, side, move));
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        // So far, states doesn't consider making an extra move. Need to add code to add the
        // extra states here.
        return states;
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
    public double evaluateEndState() {
        double myStoreSeeds = board.getSeedsInStore(mySide);
        double oppStoreSeeds = board.getSeedsInStore(mySide.opposite());
        if (isMyTurn)
            return (myStoreSeeds > oppStoreSeeds) ? 1000 : myStoreSeeds - oppStoreSeeds;
        else
            return (oppStoreSeeds > myStoreSeeds) ? 1000 : oppStoreSeeds - myStoreSeeds;
    }

    public static int leftMinusRight(Board board, Side side)
      {
            int leftSeeds = 0, rightSeeds = 0;

            // GET LEFT SEEDS ( 1 to size/2 )
            for (int i = 1; i < board.getNoOfHoles() / 2; i++)
                leftSeeds += board.getSeeds(side, i);

            // GET RIGHT SEEDS ( size/2 to size )
            for (int i = board.getNoOfHoles() / 2; i <= board.getNoOfHoles(); i++)
                rightSeeds += board.getSeeds(side, i);

            return leftSeeds - rightSeeds;

      }
}

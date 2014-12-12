package MKAgent;

import java.util.ArrayList;
import java.util.Collections;

public class State implements Comparable<State> {
    public boolean isMyTurn;
    public Side mySide;
    // This is the move that led to the state of board
    public Move move;
    public Board board;
    public double score;
    public String hash;

    public State(boolean isMyTurn, Board board, Side side, Move move) {
        this.board = board;
        this.isMyTurn = isMyTurn;
        this.mySide = side;
        this.move = move;
    }

    public boolean isEndState() {
        return Kalah.gameOver(this.board);
    }

    public double orderEvaluate() {
        double score = 0;
        double myStoreSeeds = board.getSeedsInStore(mySide);
        double oppStoreSeeds = board.getSeedsInStore(mySide.opposite());
        score = 2 *((isMyTurn) ? myStoreSeeds - oppStoreSeeds : oppStoreSeeds - myStoreSeeds);

        // for (int hole = 1; hole <= 7; hole++)
        //     if (board.getSeeds(mySide, hole) == 0 && isSeedable(mySide, hole))
        //         if (isMyTurn)
        //             score += board.getSeedsOp(mySide, hole) / 2.0;
        //         else
        //             score -= board.getSeedsOp(mySide, hole) / 2.0;

        // for (int hole = 1; hole <= 7; hole++)
        //     if (board.getSeeds(mySide.opposite(), hole) == 0 && isSeedable(mySide.opposite(), hole))
        //         if (isMyTurn)
        //             score -= board.getSeedsOp(mySide.opposite(), hole) / 2.0;
        //         else
        //             score += board.getSeedsOp(mySide.opposite(), hole) / 2.0;

        // for (int hole = 1; hole <= 7; hole++)
        //     if (board.getSeeds(mySide, hole) == 0 && isSeedable(mySide, hole))
        //         if (isMyTurn)
        //             score += board.getSeedsOp(mySide, hole) / 2.0;
        //         else
        //             score -= board.getSeedsOp(mySide, hole) / 2.0;

        // for (int hole = 1; hole <= 7; hole++)
        //     if (board.getSeeds(mySide.opposite(), hole) == 0 && isSeedable(mySide.opposite(), hole))
        //         if (isMyTurn)
        //             score -= board.getSeedsOp(mySide.opposite(), hole) / 2.0;
        //         else
        //             score += board.getSeedsOp(mySide.opposite(), hole) / 2.0;

        return score;
    }

    public double evaluate() {
        double score = 0;
        double myStoreSeeds = board.getSeedsInStore(mySide);
        double oppStoreSeeds = board.getSeedsInStore(mySide.opposite());

        score = 2 *((isMyTurn) ? myStoreSeeds - oppStoreSeeds : oppStoreSeeds - myStoreSeeds);

        // for (int hole = 1; hole <= 7; hole++)
        //     if (board.getSeeds(mySide, hole) == 0 && isSeedable(mySide, hole))
        //         if (isMyTurn)
        //             score += board.getSeedsOp(mySide, hole) / 2.0;
        //         else
        //             score -= board.getSeedsOp(mySide, hole) / 2.0;

        // for (int hole = 1; hole <= 7; hole++)
        //     if (board.getSeeds(mySide.opposite(), hole) == 0 && isSeedable(mySide.opposite(), hole))
        //         if (isMyTurn)
        //             score -= board.getSeedsOp(mySide.opposite(), hole) / 2.0;
        //         else
        //             score += board.getSeedsOp(mySide.opposite(), hole) / 2.0;


        if (myStoreSeeds + oppStoreSeeds >= 80) {
            int mySideSeeds = 0, oppSideSeeds = 0;
            for (int i = 1; i <= 7; i++) {
                mySideSeeds += board.getSeeds(mySide, i);
                oppSideSeeds += board.getSeeds(mySide.opposite(), i);
            }
            score += ((isMyTurn) ? (mySideSeeds - oppSideSeeds) : (oppSideSeeds - mySideSeeds));
        }

        // for (int i = 1; i <= 7; i++) {
        //     if (board.getNoOfHoles() - i + 1 == board.getSeeds(mySide, i))
        //         if (isMyTurn)
        //             score += 2;
        //         else
        //             score -= 2;
        // }

        // score += leftMinusRight(board,mySide);
        // score -= leftMinusRight(board,mySide.opposite());

        return score;
    }

    public ArrayList<State> getChildStates(int depth) throws Exception {
        ArrayList<State> states = new ArrayList<State>(7);

        for (int hole = 1; hole <= board.getNoOfHoles(); hole++) {
            Move move = new Move(mySide, hole);
            if (Kalah.isLegalMove(board, move)) {
                Board newBoard = board.clone();
                Side side = Kalah.makeMove(newBoard, move);
                if (side == mySide)
                    states.add(new State(isMyTurn, newBoard, side, move));
                else
                    states.add(new State(!isMyTurn, newBoard, side, move));
            }
        }

        if (depth > 12) {
            for (State state : states) {
                if (isMyTurn) {
                    if (mySide == state.mySide)
                        state.score = AlphaBeta.max2(6, state, -1000, 1000);
                    else
                        state.score = AlphaBeta.min2(6, state, -1000, 1000);

                }
                else {
                    if (mySide == state.mySide)
                        state.score = AlphaBeta.min2(6, state, -1000, 1000);
                    else
                        state.score = AlphaBeta.max2(6, state, -1000, 1000);
                }
            }
        } else if (depth > 6) {
            for (State state : states) {
                if (isMyTurn) {
                    if (mySide == state.mySide)
                        state.score = AlphaBeta.max2(3, state, -1000, 1000);
                    else
                        state.score = AlphaBeta.min2(3, state, -1000, 1000);

                }
                else {
                    if (mySide == state.mySide)
                        state.score = AlphaBeta.min2(3, state, -1000, 1000);
                    else
                        state.score = AlphaBeta.max2(3, state, -1000, 1000);
                }
            }
        } else {
            for (State state : states) {
                state.score = state.orderEvaluate();
            }
        }
        Collections.sort(states);

        return states;
    }

    public ArrayList<State> getChildStates2() throws Exception {
        ArrayList<State> states = new ArrayList<State>(7);

        for (int hole = 1; hole <= board.getNoOfHoles(); hole++) {
            Move move = new Move(mySide, hole);
            if (Kalah.isLegalMove(board, move)) {
                Board newBoard = board.clone();
                Side side = Kalah.makeMove(newBoard, move);
                if (side == mySide)
                    states.add(new State(isMyTurn, newBoard, side, move));
                else
                    states.add(new State(!isMyTurn, newBoard, side, move));
            }
        }
        return states;
    }

    public ArrayList<State> getChildStatesMinimax() throws Exception {
        ArrayList<State> states = new ArrayList<State>(7);

        for (int hole = 1; hole <= board.getNoOfHoles(); hole++) {
            Move move = new Move(mySide, hole);
            if (Kalah.isLegalMove(board, move)) {
                Board newBoard = board.clone();
                Side side = Kalah.makeMove(newBoard, move);
                if (side == mySide)
                    states.add(new State(isMyTurn, newBoard, side, move));
                else
                    states.add(new State(!isMyTurn, newBoard, side, move));
            }
        }
        return states;
    }

    public boolean isSeedable(Side side, int hole)
    {
        // Left side
        for (int i = hole - 1; i > 0; i--)
            if (hole - i == board.getSeeds(side, i))
                return true;
        // Right side
        for (int i = hole + 1; i <= 7; i++)
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
            return (myStoreSeeds > oppStoreSeeds) ? 1000 : -1000;
        else
            return (oppStoreSeeds > myStoreSeeds) ? 1000 : -1000;
    }

    public static int leftMinusRight(Board board, Side side) {
        int leftSeeds = 0, rightSeeds = 0;

        // GET LEFT SEEDS ( 1 to size/2 )
        for (int i = 1; i <=3; i++)
            leftSeeds += board.getSeeds(side, i);

        // Ignore the middle one

        // GET RIGHT SEEDS ( size/2 to size )
        for (int i = 5; i <= 7; i++)
            rightSeeds += board.getSeeds(side, i);
        return leftSeeds - rightSeeds;
    }

    @Override
    public int compareTo(State otherState) {
        return (this.score < otherState.score ) ? 1: (this.score > otherState.score) ? -1 : 0;
    }

    @Override
    public String toString() {
        return "" + score;
    }
}

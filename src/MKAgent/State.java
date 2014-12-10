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

	public State(boolean isMyTurn, Board board, Side side, Move move) {
		this.board = board;
		this.isMyTurn = isMyTurn;
		this.mySide = side;
		this.move = move;
		this.score = 0.0;
	}

	public boolean isEndState() {
		return Kalah.gameOver(this.board);
	}

	public double evaluate() {
		double score = 0;
		double myStoreSeeds = board.getSeedsInStore(mySide);
		double oppStoreSeeds = board.getSeedsInStore(mySide.opposite());

		// Heuristic 1
		score = 2 * ((isMyTurn) ? myStoreSeeds - oppStoreSeeds : oppStoreSeeds - myStoreSeeds);

		// Heuristic 2
		for (int hole = 1; hole <= board.getNoOfHoles(); hole++)
		    if (board.getSeeds(mySide, hole) == 0 && isSeedable(mySide, hole))
		        if (isMyTurn)
		            score += board.getSeedsOp(mySide, hole) / 2.0;
		        else
		            score -= board.getSeedsOp(mySide, hole) / 2.0;

		// Heuristic 3
		for (int hole = 1; hole <= board.getNoOfHoles(); hole++)
		    if (board.getSeeds(mySide.opposite(), hole) == 0 && isSeedable(mySide.opposite(), hole))
		        if (isMyTurn)
		            score -= board.getSeedsOp(mySide.opposite(), hole) / 2.0;
		        else
		            score += board.getSeedsOp(mySide.opposite(), hole) / 2.0;


        //Heuristic 4
		if (myStoreSeeds + oppStoreSeeds >= 40) {

		    int mySideSeeds = 0, oppSideSeeds = 0;
		    for (int i = 1; i <= board.getNoOfHoles(); i++) {
		        mySideSeeds += board.getSeeds(mySide, i);
		        oppSideSeeds += board.getSeeds(mySide.opposite(), i);
		    }
		    score += ((isMyTurn) ? (mySideSeeds - oppSideSeeds) : (oppSideSeeds - mySideSeeds));
		}

		// Heuristic 5
		for (int i = 1; i <= board.getNoOfHoles(); i++) {
		    if (board.getNoOfHoles() - i + 1 == board.getSeeds(mySide, i))
		        if (isMyTurn)
		            score += 2;
		        else
		            score -= 2;
		}

		// Heuristic 6
		score += leftMinusRight(board,mySide);
		score -= leftMinusRight(board,mySide.opposite());

		return score;
	}

	public ArrayList<State> getChildStates() throws Exception {
		ArrayList<State> states = new ArrayList<State>(7);

		for (int hole = 1; hole <= board.getNoOfHoles(); hole++) {
			Move move = new Move(mySide, hole);
			if (Kalah.isLegalMove(board, move)) {
				Board newBoard = board.clone();
				Side side = Kalah.makeMove(newBoard, move);
				State aState;
				if (side == mySide){
					aState = new State(isMyTurn, newBoard, side, move);
					//states.add(new State(isMyTurn, newBoard, side, move));
				}
				else{
					aState = new State(!isMyTurn, newBoard, side, move);
					//states.add(new State(!isMyTurn, newBoard, side, move));
				}
				aState.score = evaluate();
				states.add(aState);
			}
		}

		Collections.sort(states);
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
			return (myStoreSeeds > oppStoreSeeds) ? 1000 : myStoreSeeds - oppStoreSeeds;
		else
			return (oppStoreSeeds > myStoreSeeds) ? 1000 : oppStoreSeeds - myStoreSeeds;
	}

	public static int leftMinusRight(Board board, Side side)
	{
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
        return (this.score < otherState.score ) ? 1: (this.score > otherState.score) ? -1 : 0 ;
    }

    @Override
    public String toString() {
        return "" + score;
    }
}
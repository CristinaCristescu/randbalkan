package MKAgent;
import java.util.ArrayList;
import java.util.Collections;

// This was tested to see if it returns same values with Minimax(which it should)
// TODO: refactor everything into one method once testing is complete to speed stuff up
public class AlphaBeta {

    public static double max(int depth, State state, double alpha, double beta) {
        if (depth == 0 || state.isEndState()) {
             //System.err.println("Base case | Depth: " + depth + "Score: " + state.evaluate());
            return state.evaluate();
        }

        ArrayList<State> childStates;
        if (depth > 4) {
            childStates = moveOrdering(3, state);
        } else {
            childStates = state.getChildStates();
        }    

        
        double currentValue;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = max(depth, childState, alpha, beta);
            else
                currentValue = min(depth - 1, childState, alpha, beta);
            if (currentValue >= beta) {
                // System.err.println("Depth: " + depth + "Score: " + beta);
                return beta;
            }
            if (currentValue > alpha)
                alpha = currentValue;
        }
        // System.err.println("Depth: " + depth + "Score: " + alpha);
        return alpha;
    }

    public static double min (int depth, State state, double alpha, double beta) {
        if (depth == 0 || state.isEndState()) {
            // System.err.println("Base case | Depth: " + depth + "Score: " + state.evaluate());
            return state.evaluate();
        }

        ArrayList<State> childStates;
        if (depth > 4) {
            childStates = moveOrdering(3, state);
        } else {
            childStates = state.getChildStates();
        }         double currentValue;

        for (State childState : childStates) {
            // System.err.println("State: \n");
            if (state.mySide == childState.mySide)
                currentValue = min(depth, childState, alpha, beta);
            else
                currentValue = max(depth - 1, childState, alpha, beta);

            if (currentValue <= alpha) {
                // System.err.println("Depth: " + depth + "Score: " + alpha);
                return alpha;
            }
            if (currentValue < beta)
                beta = currentValue;
        }
        // System.err.println("Depth: " + depth + "Score: " + beta);
        return beta;
    }

    public static ArrayList<State> moveOrdering(int depth, State state) {

        ArrayList<State> childStates = state.getChildStates();

        // In case there's only one possible move, return that
        if (childStates.size() == 1)
            return childStates;

        for (State childState : childStates) {
            childState.score = min(depth - 1, childState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            //System.err.println(childState.score);
        }

        Collections.sort(childStates);
        return childStates;
    }
    // Do the first iteration of minimax for player MAX (us)
    public static Move getBestMove(int depth, State state) {
        ArrayList<State> childStates;

        double bestValue = Double.NEGATIVE_INFINITY, currentValue;

        State bestState = null;
        //System.err.println("Before ordering: " + state.getChildStates().toString());
        childStates = moveOrdering(3, state);
        //System.err.println("After ordering: " + childStates.toString());

        // In case there's only one possible move, return that
        if (childStates.size() == 1)
            return childStates.get(0).move;

        for (State childState : childStates) {
            currentValue = min(depth - 1, childState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestState = childState;
            }
        }

        // TODO: Remove this if once debugging is over
        if (bestState != null)
            return bestState.move;
        else
        {
            // System.err.println("Balkan algorithm tried to perform an illegal move.");
            return null;
        }
    }
}

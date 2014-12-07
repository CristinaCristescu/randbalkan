package MKAgent;
import java.util.ArrayList;

// This was tested to see if it returns same values with Minimax(which it should)
// TODO: refactor everything into one method once testing is complete to speed stuff up
public class AlphaBeta {

    public static double max(int depth, State state, double alpha, double beta) {
        if (depth == 0) {
            return state.evaluate(true);
        }
        if (state.isEndState()) {
            return state.evaluateEndState(true);
        }

        ArrayList<State> childStates = state.getChildStates();
        double currentValue;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = max(depth - 1, childState, alpha, beta);
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
        if (depth == 0) {
            return state.evaluate(false);
        }
        if (state.isEndState()) {
            return state.evaluateEndState(false);
        }

        ArrayList<State> childStates = state.getChildStates();
        double currentValue;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = min(depth - 1, childState, alpha, beta);
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

    // Do the first iteration of minimax for player MAX (us)
    public static Move getBestMove(int depth, State state) {
        ArrayList<State> childStates = state.getChildStates();
        double bestValue = Double.NEGATIVE_INFINITY, currentValue;

        State bestState = null;

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

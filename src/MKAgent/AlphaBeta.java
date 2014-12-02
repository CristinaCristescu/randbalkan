package MKAgent;
import java.util.ArrayList;

// This was tested to see if it returns same values with Minimax(which it should)
// TODO: refactor everything into one method once testing is complete to speed stuff up
public class AlphaBeta {

    public static double max(int depth, State state, double alpha, double beta) {
        if (depth == 0 || state.isEndState())
            return state.evaluate();

        ArrayList<State> childStates = state.getChildStates();
        double bestValue = alpha, currentValue;

        for (State childState : childStates) {
            currentValue = min(depth - 1, childState, bestValue, beta);
            if (currentValue > bestValue)
                bestValue = currentValue;
            if (bestValue > beta)
                return beta;
        }
        return bestValue;
    }

    public static double min (int depth, State state, double alpha, double beta) {
        if (depth == 0 || state.isEndState())
            return state.evaluate();

        ArrayList<State> childStates = state.getChildStates();
        double bestValue = beta, currentValue;

        for (State childState : childStates) {
            currentValue = max(depth - 1, childState, alpha, bestValue);
            if (currentValue < bestValue)
                bestValue = currentValue;
            if (bestValue < alpha)
                return alpha;
        }
        return bestValue;
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
            System.err.println("Balkan algorithm tried to perform an illegal move.");
            return null;
        }
    }
}

package MKAgent;
import java.util.ArrayList;

public class AlphaBeta {

    public static double max(int depth, State state, double alpha, double beta) throws Exception {
        if (depth == 0) {
            return state.evaluate();
        }
        if (state.isEndState()) {
            return state.evaluateEndState();
        }

        ArrayList<State> childStates = state.getChildStates();
        double currentValue;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = max(depth - 1, childState, alpha, beta);
            else
                currentValue = min(depth - 1, childState, alpha, beta);
            if (currentValue >= beta) {
                return beta;
            }
            if (currentValue > alpha)
                alpha = currentValue;
        }
        return alpha;
    }

    public static double min (int depth, State state, double alpha, double beta) throws Exception {
        if (depth == 0) {
            return state.evaluate();
        }
        if (state.isEndState()) {
            return state.evaluateEndState();
        }

        ArrayList<State> childStates = state.getChildStates();
        double currentValue;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = min(depth - 1, childState, alpha, beta);
            else
                currentValue = max(depth - 1, childState, alpha, beta);

            if (currentValue <= alpha) {
                return alpha;
            }
            if (currentValue < beta)
                beta = currentValue;
        }
        return beta;
    }

    public static Move getBestMove(int depth, State state) throws Exception {
        ArrayList<State> childStates = state.getChildStates();
        double bestValue = Double.NEGATIVE_INFINITY, currentValue;

        State bestState = null;

        // In case there's only one possible move, return that
        if (childStates.size() == 1)
            return childStates.get(0).move;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = max(depth, childState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            else
                currentValue = min(depth, childState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestState = childState;
            }
        }
        return bestState.move;
    }
}

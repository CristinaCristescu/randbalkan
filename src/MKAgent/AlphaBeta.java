package MKAgent;
import java.util.ArrayList;
import java.util.HashMap;

public class AlphaBeta {
    public static boolean isTimeOver = false;
    public static long startTime;
    public static long duration;
    public static Move getBestMoveID(int depth, State state) throws Exception {
        isTimeOver = false;
        Move bestMove = null, currentMove = null;
        System.err.println("Move: ");
        for (int i = 10; i <= depth; i++) {
            currentMove = getBestMove(i, state);
            if (isTimeOver)
                break;
            System.err.println("depth: " + i);
            bestMove = currentMove;
        }
        return bestMove;
    }

    public static double max(int depth, State state, double alpha, double beta) throws Exception {
        if (isTimeOver || timeOver())
            return -1;
        if (depth == 0) {
            return state.evaluate();
        }
        if (state.isEndState()) {
            return state.evaluateEndState();
        }

        ArrayList<State> childStates = state.getChildStates(depth);
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

    public static double min(int depth, State state, double alpha, double beta) throws Exception {
        if (isTimeOver || timeOver())
            return -1;
        if (depth == 0) {
            return state.evaluate();
        }
        if (state.isEndState()) {
            return state.evaluateEndState();
        }

        ArrayList<State> childStates = state.getChildStates(depth);
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

    public static double max2(int depth, State state, double alpha, double beta) throws Exception {
        if (isTimeOver || timeOver())
            return -1;
        if (depth == 0) {
            return state.evaluate();
        }
        if (state.isEndState()) {
            return state.evaluateEndState();
        }

        ArrayList<State> childStates = state.getChildStates2();
        double currentValue;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = max2(depth - 1, childState, alpha, beta);
            else
                currentValue = min2(depth - 1, childState, alpha, beta);
            if (currentValue >= beta) {
                return beta;
            }
            if (currentValue > alpha)
                alpha = currentValue;
        }
        return alpha;
    }

    public static double min2(int depth, State state, double alpha, double beta) throws Exception {
        if (isTimeOver || timeOver())
            return -1;
        if (depth == 0) {
            return state.evaluate();
        }
        if (state.isEndState()) {
            return state.evaluateEndState();
        }

        ArrayList<State> childStates = state.getChildStates2();
        double currentValue;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = min2(depth - 1, childState, alpha, beta);
            else
                currentValue = max2(depth - 1, childState, alpha, beta);

            if (currentValue <= alpha) {
                return alpha;
            }
            if (currentValue < beta)
                beta = currentValue;
        }
        return beta;
    }


    public static Move getBestMove(int depth, State state) throws Exception {
        ArrayList<State> childStates = state.getChildStates(depth);
        double bestValue = Double.NEGATIVE_INFINITY, currentValue;

        State bestState = null;

        // In case there's only one possible move, return that
        if (childStates.size() == 1)
            return childStates.get(0).move;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = max(depth - 1, childState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            else
                currentValue = min(depth - 1, childState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestState = childState;
            }
        }
        if (bestState != null)
            return bestState.move;
        else return null;
    }

    public static boolean timeOver() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) < duration)
            return false;
        isTimeOver = true;
        return true;
    }
}

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
        for (int i = 13; i <= depth; i++) {
            currentMove = getBestMove(i, state);
            if (isTimeOver)
                break;
            bestMove = currentMove;
        }
        return bestMove;
    }

    public static int max(int depth, State state, int alpha, int beta) throws Exception {
        if (isTimeOver || timeOver())
            return -1;
        if (depth == 0) {
            return state.evaluate();
        }
        if (state.isEndState()) {
            return state.evaluateEndState();
        }

        ArrayList<State> childStates = state.getChildStates(depth);
        int currentValue;

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

    public static int min(int depth, State state, int alpha, int beta) throws Exception {
        if (isTimeOver || timeOver())
            return -1;
        if (depth == 0) {
            return state.evaluate();
        }
        if (state.isEndState()) {
            return state.evaluateEndState();
        }

        ArrayList<State> childStates = state.getChildStates(depth);
        int currentValue;

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

    public static int max2(int depth, State state, int alpha, int beta) throws Exception {
        if (isTimeOver || timeOver())
            return -1;
        if (depth == 0) {
            return state.evaluate();
        }
        if (state.isEndState()) {
            return state.evaluateEndState();
        }

        ArrayList<State> childStates = state.getChildStates2();
        int currentValue;

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

    public static int min2(int depth, State state, int alpha, int beta) throws Exception {
        if (isTimeOver || timeOver())
            return -1;
        if (depth == 0) {
            return state.evaluate();
        }
        if (state.isEndState()) {
            return state.evaluateEndState();
        }

        ArrayList<State> childStates = state.getChildStates2();
        int currentValue;

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
        int bestValue = -1000, currentValue;

        State bestState = null;

        // In case there's only one possible move, return that
        if (childStates.size() == 1)
            return childStates.get(0).move;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = max(depth - 1, childState, -1000, 1000);
            else
                currentValue = min(depth - 1, childState, -1000, 1000);
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

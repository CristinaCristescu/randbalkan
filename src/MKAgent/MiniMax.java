package MKAgent;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.IOException;

public class MiniMax {


    public static double max(int depth, State state) {
        if (depth == 0 || state.isEndState())
            return state.evaluate();

        ArrayList<State> childStates = state.getChildStates();
        double bestValue = Double.NEGATIVE_INFINITY, currentValue;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = max(depth - 1, childState);
            else
                currentValue = min(depth - 1, childState);

            if (currentValue > bestValue)
                bestValue = currentValue;
        }
        return bestValue;
    }

    public static double min (int depth, State state) {
        System.err.println("Depth: " + depth);
        if (depth == 0 || state.isEndState())
            return state.evaluate();

        ArrayList<State> childStates = state.getChildStates();
        double bestValue = Double.POSITIVE_INFINITY, currentValue;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = min(depth - 1, childState);
            else
                currentValue = max(depth - 1, childState);

            if (currentValue < bestValue)
                bestValue = currentValue;
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
            currentValue = min(depth - 1, childState);
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

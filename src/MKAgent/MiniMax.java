package MKAgent;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.IOException;

public class MiniMax {

    public static double max(int depth, State state) throws Exception{
        if (state.isMyTurn != true)
            System.err.println("WTF");
        if (depth == 0) {
            return state.evaluate();
        }
        if (state.isEndState()) {
            return state.evaluateEndState();
        }


        ArrayList<State> childStates = state.getChildStatesMinimax();
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

    public static double min (int depth, State state) throws Exception {
        if (state.isMyTurn != false)
            System.err.println("WTF");
        if (depth == 0) {
            return state.evaluate();
        }
        if (state.isEndState()) {
            return state.evaluateEndState();
        }


        ArrayList<State> childStates = state.getChildStatesMinimax();
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

    public static Move getBestMove(int depth, State state) throws Exception {
        ArrayList<State> childStates = state.getChildStatesMinimax();
        double bestValue = Double.NEGATIVE_INFINITY, currentValue;

        State bestState = null;

        if (childStates.size() == 1)
            return childStates.get(0).move;

        for (State childState : childStates) {
            if (state.mySide == childState.mySide)
                currentValue = max(depth - 1, childState);
            else
                currentValue = min(depth - 1, childState);
            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestState = childState;
            }
        }

        // TODO: Remove this if once debugging is over
        return bestState.move;

    }
}

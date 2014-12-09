package MKAgent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

// This was tested to see if it returns same values with Minimax(which it should)
// TODO: refactor everything into one method once testing is complete to speed stuff up
public class AlphaBeta {

    public static int currentDepth;
    public static Hashtable<Board, EntryForTT> transpTable;
    public static int[][][] z_table;

    public static void init_zobrist()
    {
        //  fill a table of random numbers/bitstrings
        Random random = new Random();
        z_table = new int[2][8][86]; // THIS HAS TO BE GLOBAL

        for (int i=0; i <= 7; i++)
        {
            for(int seed = 0; seed <= 85; seed++)
            {
                z_table[0][i][seed] = random.nextInt(7);
                z_table[1][i][seed] = random.nextInt(7);
            }
        } 
    }


    public static void create_TT()
    {
        transpTable = new Hashtable<Board, EntryForTT>(100,10);
    }


    public static double max(int depth, State state, double alpha, double beta) {
        // if (depth == 0 || state.isEndState()) {
        //     // System.err.println("Base case | Depth: " + depth + "Score: " + state.evaluate());
        //     return state.evaluate();
        // }
         if (depth == 0) {
            // Return value from TT if it was prev computed, for the current depth
            if(transpTable.containsKey(state.board))
            {
                if(currentDepth <= transpTable.get(state.board).depth)
                {
                    return transpTable.get(state.board).score;
                }
            }

            double evaluatedScore = state.evaluate();
            transpTable.put(state.board, new EntryForTT(currentDepth, evaluatedScore));
            return evaluatedScore;
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
        // if (depth == 0 || state.isEndState()) {
        //     // System.err.println("Base case | Depth: " + depth + "Score: " + state.evaluate());
        //     return state.evaluate();
        // }
        if (depth == 0) {
            // Return value from TT if it was prev computed, for the current depth
            if(transpTable.containsKey(state.board))
            {
                if(currentDepth <= transpTable.get(state.board).depth)
                {
                    return transpTable.get(state.board).score;
                }
            }

            double evaluatedScore = state.evaluate();
            transpTable.put(state.board, new EntryForTT(currentDepth, evaluatedScore));
            return evaluatedScore;
        }


        if (state.isEndState()) {
            return state.evaluateEndState();
        }

        ArrayList<State> childStates = state.getChildStates();
        double currentValue;

        for (State childState : childStates) {
            // System.err.println("State: \n");
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
        currentDepth = depth;
        ArrayList<State> childStates = state.getChildStates();
        double bestValue = Double.NEGATIVE_INFINITY, currentValue;

        State bestState = null;

        // In case there's only one possible move, return that
        if (childStates.size() == 1)
            return childStates.get(0).move;

        if (state.isMyTurn != true)
            System.err.println("WTF");

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

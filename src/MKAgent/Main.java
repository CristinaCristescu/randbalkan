package MKAgent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.Integer;
import java.lang.Double;
import java.lang.Math;

/**
 * The main application class. It also provides methods for communication
 * with the game engine.
 */
public class Main
{


    private static PrintWriter writer = null;
    private static int noHoles = 7;
    private static int noSeeds = 7;
    private static int ai_depth = 8;
    private static boolean first;
    private static Side side;
    private static Random random = new Random();
    private static Board globalBoard = new Board(noHoles, noSeeds);
    private static int bestValue;
    private static int chosenHole;
    private static Double alpha;
    private static Double beta; 

    /**
     * Input from the game engine.
     */
    private static Reader input = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Sends a message to the game engine.
     * @param msg The message.
     */
    public static void sendMsg (String msg)
    {
    	System.out.print(msg);
    	System.out.flush();
    }

    /**
     * Receives a message from the game engine. Messages are terminated by
     * a '\n' character.
     * @return The message.
     * @throws IOException if there has been an I/O error.
     */
    public static String recvMsg() throws IOException
    {
    	StringBuilder message = new StringBuilder();
    	int newCharacter;

    	do
    	{
    		newCharacter = input.read();
    		if (newCharacter == -1)
    			throw new EOFException("Input ended unexpectedly.");
    		message.append((char)newCharacter);
    	} while((char)newCharacter != '\n');

		return message.toString();
    }

    public static int evalFunction(Board aBoard)
    {
        return aBoard.getSeedsInStore((first ? Side.SOUTH : Side.NORTH)) - aBoard.getSeedsInStore((first ? Side.NORTH : Side.SOUTH));
    }

// Basic minimax algorithm - no alpha-beta pruning or heuristics
    public static int minimax(int depth, boolean isPlayer1, Board aBoard, double alpha, double beta)
    {
        try{
//      if depth is 0 return evaluation function
        if(depth == 0)
            return evalFunction(aBoard);
//      if it's Player 1's turn
        if (isPlayer1)
        {
            writer.println("MAX: " + depth + " \n" + aBoard);
            bestValue = Integer.MIN_VALUE;
            // evaluate all holes
            for(int hole = 1; hole <= aBoard.getNoOfHoles(); hole++)
            {
                //  duplicate board

                Move move = new Move((first ? Side.SOUTH : Side.NORTH),hole);


                //  if move is legal
                if(Kalah.isLegalMove(aBoard, move))
                {
                    writer.println("CURRENT BOARD" + aBoard);
                    Board bBoard = aBoard.clone();
                    //  simulate move
                    Kalah.makeMove(bBoard, move);
                    writer.println("CURRENT CHANGED BOARD" + bBoard);

                    int score = minimax(depth-1, false, bBoard, alpha, beta);
                    if (score <= alpha)
                    {
                        continue;
                    } else if (score < beta) {
                        alpha = score;
                        writer.println("CURRENT (SHOULD NOT BE CHANGED)" + aBoard);
                        //  record best value and corresponding hole if it beats result so far
                        if(bestValue <= score)
                        {
                            bestValue = score;
                            if (depth == ai_depth)
                                chosenHole = hole;
                            writer.println("chosenhole changed?" + chosenHole);
                        }
                    } else {
                        return;
                    }    
                }
            }
        }
        else
        {

            writer.println("MIN: " + depth + " \n" + aBoard);
            bestValue = Integer.MAX_VALUE;
            // evaluate all holes
            for(int hole = 1; hole <= aBoard.getNoOfHoles(); hole++)
            {
                //  duplicate board

                Move move = new Move((first ? Side.SOUTH : Side.NORTH),hole);
                //  if move is legal
                if(Kalah.isLegalMove(aBoard, move))
                {
                    writer.println("CURRENT BOARD" + aBoard);
                    Board bBoard = aBoard.clone();
                    //  simulate move
                    Kalah.makeMove(bBoard, move);
                    writer.println("CURRENT CHANGED BOARD" + bBoard);
                    int score = minimax(depth-1, true, bBoard, alpha, beta);
                    if (score <= alpha)
                    {
                        return;
                    } else if (score <beta)
                    {
                        beta = score;    
                        writer.println("CURRENT (SHOULD NOT BE CHANGED)" + aBoard);
                        //  record best value and corresponding hole if it beats result so far
                        if(bestValue >= score)
                        {
                            bestValue = score;
                            if (depth == ai_depth)
                                chosenHole = hole;
                            writer.println("chosenhole changed?" + chosenHole);
                        }
                    } else {
                        continue;
                    }    
                }
            }
        }
        }
        catch (Exception e) {
           writer.println("FUCK U JAVA");
        }

        return bestValue;
    }

	/**
	 * The main method, invoked when the program is started.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args)
	{
        try
        {
            writer = new PrintWriter(new FileWriter("afile", true));
            Board duplicateB;
            String s;
            while (true)
            {
                System.err.println();
                s = recvMsg();
                System.err.print("Received: " + s);
                try {
                    MsgType mt = Protocol.getMessageType(s);
                    switch (mt)
                    {
                        case START: System.err.println("A start.");
                            first = Protocol.interpretStartMsg(s);
                            System.err.println("Starting player? " + first);
                            side = (first) ? Side.SOUTH : Side.NORTH;
                            if(first)
                            {
                                try {
                                    duplicateB = globalBoard.clone();
                                    alpha = Double.NEGATIVE_INFINITY;
                                    beta = Double.POSITIVE_INFINITY;
                                    int bestEvalScore = minimax(ai_depth, true, duplicateB, alpha, beta);
                                    writer.println("INITIAL: player " + first + " hole - " + chosenHole + " and eval " + bestEvalScore);
                                    sendMsg(Protocol.createMoveMsg(chosenHole));
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }

                            }
                            break;
                        case STATE: System.err.println("A state.");
                            Protocol.MoveTurn r = Protocol.interpretStateMsg (s, globalBoard);
                            System.err.println("This was the move: " + r.move);
                            System.err.println("Is the game over? " + r.end);
                            if (r.again) {
                                try {
                                    duplicateB = globalBoard.clone();
                                    writer.println("DUPLICATED: \n" + duplicateB);
                                    alpha = Double.NEGATIVE_INFINITY;
                                    beta = Double.POSITIVE_INFINITY;
                                    int bestEvalScore = minimax(ai_depth, true, duplicateB, alpha, beta);
                                    writer.println("MIDDLE: player " + first + " hole - " + chosenHole + " and eval " + bestEvalScore);
                                    sendMsg(Protocol.createMoveMsg(chosenHole));
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }

                            }
                            if (!r.end) System.err.println("Is it our turn again? " + r.again);
                            System.err.print("The board:\n" + globalBoard);
                            break;
                        case END: System.err.println("An end. Bye bye!"); return;
                    }

                } catch (InvalidMessageException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        catch (IOException e)
        {
            System.err.println("This shouldn't happen: " + e.getMessage());
        } finally {
            writer.close();
        }
    }
}

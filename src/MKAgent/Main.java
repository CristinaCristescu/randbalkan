package MKAgent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * The main application class. It also provides methods for communication
 * with the game engine.
 */
public class Main
{
    private static int noHoles = 7;
    private static int noSeeds = 7;
    private static int ai_depth = 5;
    private static boolean first;
    private static Side side;
    private static Board globalBoard = new Board(noHoles, noSeeds);


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


	/**
	 * The main method, invoked when the program is started.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args)
	{
        try {
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
                                Move move = MiniMax.getBestMove(ai_depth, new State(true, globalBoard, side, null));
                                sendMsg(Protocol.createMoveMsg(move.getHole()));
                            }
                            break;
                        case STATE: System.err.println("A state.");
                            Protocol.MoveTurn r = Protocol.interpretStateMsg (s, globalBoard);
                            System.err.println("This was the move: " + r.move);
                            System.err.println("Is the game over? " + r.end);
                            // If swapped
                            if (r.move == -1) {
                                side = side.opposite();
                            }
                            if (r.again) {
                                Move move = MiniMax.getBestMove(ai_depth, new State(true, globalBoard, side, null));
                                sendMsg(Protocol.createMoveMsg(move.getHole()));
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
        } catch (IOException e) {
            System.err.println("This shouldn't happen: " + e.getMessage());
        }

    }
}

package balkanAI;
import java.util.Random;

import KalahAI.KalahAI;

/**
 * The main application class. It also provides methods for communication
 * with the game engine.
 */
public class BalkanAI implements KalahAI
{
	private boolean secondTurn;
    private boolean first;
    private int noHoles;
    private Side ourSide;
    private Random random = new Random();
    private Board board;

    public BalkanAI(int noHoles, int side)
    {
    	this.noHoles = noHoles;
    	board = new Board(noHoles,noHoles);
    	ourSide = side == 0 ? Side.NORTH : Side.SOUTH;
    }
    
    public String toString()
    {
    	return "BalkanAI";
    }
    /**
     * Input from the game engine.
     */
    /*
    private static Reader input = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Sends a message to the game engine.
     * @param msg The message.
     */
    /*
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
    /*
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

    */
	/**
	 * The main method, invoked when the program is started.
	 * @param args Command line arguments.
	 */
	public String makeMove(String s)
	{
        
        System.err.print("Received: " + s);
        try {
            MsgType mt = Protocol.getMessageType(s);
            switch (mt)
            {
                case START:
                    System.err.println("A start.");
                    first = Protocol.interpretStartMsg(s);
                    System.err.println("Starting player? " + first);
                    if (first)
                    {
                      Move move = new Move(ourSide, random.nextInt(noHoles) + 1);
                      
                      String msg;
                      msg = Protocol.createMoveMsg(move.getHole());
                      
                      System.out.print(msg);
                      return msg;
                    }
                    else
                    {
                    	secondTurn = true;
                    }
                    return "";
                    
                    
                case STATE: 
                	System.err.println("A state.");
                    
                	Protocol.MoveTurn r = Protocol.interpretStateMsg (s, board);
                	//System.err.println("This was the move: " + r.move);
                    //System.err.println("Is the game over? " + r.end);
                    
                    //if (!r.end) 
                    //	System.err.println("Is it our turn again? " + r.again);
                    
                	System.err.print("The board:\n" + board);
       
                    if (r.again) {
                        Move move = new Move(ourSide, random.nextInt(noHoles) + 1);
                        while(!Kalah.isLegalMove(board, move))
                        	move = new Move(ourSide, random.nextInt(noHoles) + 1);
                        
                        String msg = Protocol.createMoveMsg(move.getHole());	
                        System.out.print(ourSide + "  " + msg);
                        return msg;
                    }
                    break;
                case END: 
                	System.err.println("An end. Bye bye!"); 
                	
                return "";
            }

        } catch (InvalidMessageException e) {
            System.err.println(e.getMessage());
        }
        return "";
        
    }
}

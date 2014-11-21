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

/**
 * The main application class. It also provides methods for communication
 * with the game engine.
 */
public class Main
{
	// Global variables
	public static int bestValue;
	public static int chosenHole;
	public static boolean first;
	public static Board globalBoard;

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
	public static int minimax(int depth, boolean isPlayer1, Board aBoard)
	{
//		if depth is 0 return evaluation function
		if(depth == 0)
			return evalFunction(aBoard);
//		if it's Player 1's turn
		if (isPlayer1)
		{
			bestValue = Integer.MIN_VALUE;
			chosenHole = 1;
			// evaluate all holes
			for(int hole = 1; hole <= aBoard.getNoOfHoles(); hole++)
			{
				//	duplicate board

				Kalah duplicateK = new Kalah(aBoard);			
				Move move = new Move((isPlayer1 ? Side.SOUTH : Side.NORTH),hole);
				//	if move is legal
     			if(duplicateK.isLegalMove(move))
				{
					//	simulate move
					duplicateK.makeMove(move);

					int score = minimax(depth-1, false, aBoard);
					//	record best value and corresponding hole if it beats result so far
					if(bestValue < score)
					{
						bestValue = score;
						chosenHole = hole;
					}
				}
			}
		}
		else
		{
			bestValue = Integer.MAX_VALUE;
			// evaluate all holes
			for(int hole = 1; hole <= aBoard.getNoOfHoles(); hole++)
			{
				//	duplicate board
		
				Kalah duplicateK = new Kalah(aBoard);
				Move move = new Move((isPlayer1 ? Side.SOUTH : Side.NORTH),hole);
				//	if move is legal
				if(duplicateK.isLegalMove(move))
				{
					//	simulate move
					duplicateK.makeMove(move);
					int score = minimax(depth-1, true, aBoard);
					//	record best value and corresponding hole if it beats result so far
					if(bestValue > score)
					{
						bestValue = score;
						chosenHole = hole;
					}
				}
			}
		}
		return bestValue;
	}

	public static int count = 0;


	/**
	 * The main method, invoked when the program is started.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args)
	{

		try
		{
			PrintWriter writer = new PrintWriter(new FileWriter("afile", true));
			Board duplicateB;

			String s;
			while (true)
			{		
				writer.println();
				count++;
				writer.println("while loop: " + count);

				s = recvMsg();
				duplicateB = null;
				writer.println("Received: " + s);
				try {
					MsgType mt = Protocol.getMessageType(s);
					switch (mt)
					{
						case START: writer.println("A start.");
							first = Protocol.interpretStartMsg(s);
							writer.println("Starting player? " + first);
							globalBoard = new Board(7,7);

									
							if(first)
							{								
								try {
									duplicateB = globalBoard.clone();					
								} catch (CloneNotSupportedException e) {
									e.printStackTrace();
								}
								int bestEvalScore = minimax(3, first, duplicateB);					
								writer.println("player " + first + " hole - " + chosenHole + " and eval " + bestEvalScore);
								sendMsg(Protocol.createMoveMsg(chosenHole));
							}						
							break;

						case STATE: writer.println("A state.");									
							Protocol.MoveTurn r = Protocol.interpretStateMsg (s, globalBoard);				
							writer.println("This was the move: " + r.move);
							writer.println("Is the game over? " + r.end);
							if (!r.end) writer.println("Is it our turn again? " + r.again);
							writer.println("The board:\n" + globalBoard);
							
							break;
						case END: writer.println("An end. Bye bye!"); return;
					}					
			

				} catch (InvalidMessageException e) {
					writer.println(e.getMessage());
				}

				try {
					duplicateB = globalBoard.clone();					
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}		
				int bestEvalScore = minimax(3, first, duplicateB);					
				writer.println("player " + first + " hole - " + chosenHole + " and eval " + bestEvalScore);
				sendMsg(Protocol.createMoveMsg(chosenHole));	

				writer.close();
			}			
		}
		catch (IOException e)
		{
			System.err.println("This shouldn't happen: " + e.getMessage());
		}

	}
	
}

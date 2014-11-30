package JimmyPlayer;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

import KalahAI.KalahAI;

public class JimmyPlayer implements KalahAI
{
  public Side ourSide;
  public boolean secondTurn;
  
  
  
  /*
  private static Reader input = new BufferedReader(new InputStreamReader(System.in));
  
  public static void sendMsg(String paramString)
  {
    System.out.print(paramString);
    System.out.flush();
  }
  
  public static String recvMsg()
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i;
    do
    {
      i = input.read();
      if (i == -1) {
        throw new EOFException("Input ended unexpectedly.");
      }
      localStringBuilder.append((char)i);
    } while ((char)i != '\n');
    return localStringBuilder.toString();
  }*/
  
  public String makeMove(String str1)
  {
    try
    {
        System.err.println();
        System.err.print("Received: " + str1);
        MsgType localMsgType = Protocol.getMessageType(str1);
        int i;
        Board localBoard;
        String str2;
        switch (localMsgType)
        {
        case START: 
          System.err.println("A start.");
          boolean bool = Protocol.interpretStartMsg(str1);
          System.err.println("Starting player? " + bool);
          if (bool)
          {
            i = 1;
            ourSide = Side.SOUTH;
            localBoard = new Board(7, 7);
            
            int j = TreeMethods.getBestMove(localBoard,ourSide);
            

            str2 = Protocol.createMoveMsg(j);
            
            System.out.print(str2);
            return str2;
          }
          else
          {
            ourSide = Side.NORTH;
            secondTurn = true;
          }
          return "";
        case STATE: 
          System.err.println("A state.");
          localBoard = new Board(7, 7);
          
          Protocol.MoveTurn localMoveTurn = Protocol.interpretStateMsg(str1, localBoard);
          //System.err.println("This was the move: " + localMoveTurn.move);
          //System.err.println("Is the game over? " + localMoveTurn.end);
          if (!localMoveTurn.end) {
          //System.err.println("Is it our turn?" + localMoveTurn.again);
          }
          System.err.print("The board:\n" + localBoard);
          if (localMoveTurn.again) {
            i = 1;
          } else {
            i = 0;
          }
          if (i != 0) {
            if (secondTurn)
            {
              str2 = Protocol.createSwapMsg();
              
              System.out.print(str2);
              
              ourSide = ourSide.opposite();
              System.out.println("I have swapped");
              


              secondTurn = false;
              return str2;
            }
            else
            {
              if (localMoveTurn.move == -1) {
                ourSide = ourSide.opposite();
              }
              int k = TreeMethods.getBestMove(localBoard,ourSide);
              

              String str3 = Protocol.createMoveMsg(k);
              
              System.out.print(str3);
              return str3;
            }
          }
          break;
        case END: 
          System.err.println("An end. Bye bye!");
          
          return "";
        }
    }
    catch (InvalidMessageException localInvalidMessageException)
    {
      System.err.println(localInvalidMessageException.getMessage());
    }
    catch (Exception localException)
    {
      System.err.println("This shouldn't happen: " + localException.getMessage());
    }
    
    return "";
  }
}

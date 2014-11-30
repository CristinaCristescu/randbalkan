package JimmyAI;

import KalahAI.KalahAI;

public class JimmyAI implements KalahAI
{
  public Side ourSide;
  public boolean secondTurn;
  private Board localBoard;
  
  public JimmyAI(int noHoles, int side)
  {
	  localBoard = new Board(noHoles, noHoles);
	  ourSide = side == 0 ? Side.NORTH : Side.SOUTH;
  }
  
  public String toString()
  {
  	return "JimmyAI";
  }
  
  public String makeMove(String str1)
  {
    try
    {
        System.err.println();
        System.err.print("Received: " + str1);
        MsgType localMsgType = Protocol.getMessageType(str1);
        int i;
        String str2;
        switch (localMsgType)
        {
        case START: 
          System.err.println("A start.");
          boolean first = Protocol.interpretStartMsg(str1);
          System.err.println("Starting player? " + first);
          if (first)
          {
            i = 1;
            
            int j = TreeMethods.getBestMove(localBoard,ourSide);           
            str2 = Protocol.createMoveMsg(j);
            
            System.out.print(str2);
            return str2;
          }
          else
          {
            secondTurn = true;
          }
          return "";
        case STATE: 
          System.err.println("A state.");
          
          Protocol.MoveTurn localMoveTurn = Protocol.interpretStateMsg(str1, localBoard);
          //System.err.println("This was the move: " + localMoveTurn.move);
          //System.err.println("Is the game over? " + localMoveTurn.end);
          if (!localMoveTurn.end) {
          //System.err.println("Is it our turn?" + localMoveTurn.again);
          }
          System.err.print("The board:\n" + localBoard);
          if (localMoveTurn.again)
            i = 1;
          else 
            i = 0;
          
          if (i != 0) 
          {
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

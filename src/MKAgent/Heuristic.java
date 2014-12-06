package MKAgent;


public class Heuristic
{
  public static double getScore(State state)
  {
	Board board = state.board;
	Side side = state.mySide;
	boolean myTurn = state.isMyTurn;
	
    double score = 0;    
    int mySeeds = board.getSeedsInStore(side);
    int opponentSeeds = board.getSeedsInStore(side.opposite());
    
    
    // Kalah Advantage -- HEURISTIC 0
    if ((mySeeds != 0) || (opponentSeeds != 0)) 
    {
      if (mySeeds != opponentSeeds)
      {
        int maxSeeds = mySeeds > opponentSeeds ? mySeeds : opponentSeeds;
        int minSeeds = mySeeds < opponentSeeds ? mySeeds : opponentSeeds;
        
        if (mySeeds > opponentSeeds)
        {
          maxSeeds = mySeeds;
          minSeeds = opponentSeeds;
        }
        else
        {
          maxSeeds = opponentSeeds;
          minSeeds = mySeeds;
        }
        
        
        // The magic happens right here
        score = (1.0D / maxSeeds * (maxSeeds - minSeeds) + 1) * maxSeeds;

        // Reverse score
        if (opponentSeeds > mySeeds)
          score *= -1;
      }
    }
    
    
    // HEURISTIC 1
    // Add stones we can steal from opponent board.
    // Multiplied by a factor of 1/2 because a bird in the hand is worth 2 in the bush,
    // therefore, one in the bush is half a bird in the hand.
    for (int i = 1; i <= board.getNoOfHoles(); i++) 
      if ((board.getSeeds(side, i) == 0) && (isSeedable(board, side, i)))
        score += board.getSeeds(side.opposite(), i) / 2;
   

    // Remove stones the opponent can steal from us.
    // Multiplied by a factor of 1/2 because minus one bird in the hand is worth minus 2 in the bush,
    // therefore, minus one in the bush is minus a half a bird in the hand.
    for (int i = 1; i <= board.getNoOfHoles(); i++)
      if ((board.getSeeds(side.opposite(), i) == 0) && (isSeedable(board, side.opposite(), i)))
        score -= board.getSeeds(side, i) / 2;
   
    ////////////////////////////
    
    // HEURISTIC 2
    // Add 1 (we can investigate other ways of increasing the function)
    // for any of our moves that leads to a free second move
    for (int i = 1; i <= board.getNoOfHoles(); i++) 
      if (board.getNoOfHoles() - i + 1 == board.getSeeds(side, i))
        score += 1;
        

    // Remove 1 (we can investigate other ways of decreasing the function) 
    // for any opponent move that leads to a free second move
    for (int i = 1; i <= board.getNoOfHoles(); i++) 
        if (board.getNoOfHoles() - i + 1 == board.getSeeds(side.opposite(), i))
          score -= 1;
       
    
    
    // HEURISTIC 3 --- we might need to investigate this
    // In the first few states of the game,
    // we consider seeds on the opponent board as being advantageous to us
    // because there is a high chance that they will lead to one of the above 
    // two heuristics taking place.
    
    // This is a bit dodgy, and I don't understand why it's added with a factor of 1/2
    int seedsOnMyBoard = 0;
    for (int i = 1; i <= board.getNoOfHoles(); i++)
    	seedsOnMyBoard += board.getSeeds(side, i);
      
    int seedsOnOpponentBoard = 0;
    for (int i = 1; i <= board.getNoOfHoles(); i++)
    	seedsOnOpponentBoard += board.getSeeds(side.opposite(), i);
      
    int seedDifference = seedsOnOpponentBoard - seedsOnMyBoard;
    
    score += seedDifference / 2;
    ///////////////////////////////////////////////////////////////////////
    
        
    return score * (myTurn ? 1 : -1); // le score
  }
  
  public static boolean isSeedable(Board board, Side side, int hole)
  {
    for (int i = hole - 1; i > 0; i--) 
      if (hole - i == board.getSeeds(side, i))
	     return true;
    
    return false;
  }
}


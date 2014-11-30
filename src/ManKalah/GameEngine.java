package ManKalah;


import JimmyAI.JimmyAI;
import KalahAI.KalahAI;
import balkanAI.BalkanAI;

public class GameEngine
{
  private static final Side startingSide = Side.SOUTH;
  /*
  private static final int holes = 7;
  private static final int seeds = 7;
  private static final long agentTimeout = 3600000L;
  private static final boolean printBoardToStderr = true;
  private static final boolean allowSwapping = true;*/
  private Kalah kalah;
  private Player playerNorth;
  private Player playerSouth;
  
  public static void main(String[] args)
  {  
    KalahAI player1 = new BalkanAI(7, startingSide == Side.NORTH ? 0 : 1);
    KalahAI player2 = new BalkanAI(7, startingSide == Side.NORTH ? 1 : 0);
   
    Player playerSouth;
    Player playerNorth;
    
    if (startingSide == Side.NORTH)
    {
      playerNorth = new Player(1, player1.toString(), player1, Side.NORTH);
      playerSouth = new Player(2, player2.toString(), player2, Side.SOUTH);
    }
    else
    {
      playerSouth = new Player(1, player1.toString(), player1, Side.SOUTH);
      playerNorth = new Player(2, player2.toString(), player2, Side.NORTH);
    }

    Board board = new Board(7, 7);
    Kalah kalah = new Kalah(board);
    


    PrintingBoardObserver observer = new PrintingBoardObserver(System.err);
    board.addObserver(observer);
    observer.update(board, null);
    

    GameEngine game = new GameEngine(kalah, playerNorth, playerSouth);
    try
    {
      Thread.sleep(500L);
    }
    catch (InterruptedException localInterruptedException) {}
    Player abortingPlayer = game.runMatch(startingSide);
    

    game.evaluate(abortingPlayer);
    System.exit(0);
  }
  
  private GameEngine(Kalah kalah, Player playerNorth, Player playerSouth)
  {
    this.kalah = kalah;
    this.playerNorth = playerNorth;
    this.playerSouth = playerSouth;
  }
  
  private Player runMatch(Side startingSide)
  {
    Player abortingPlayer = null;
    boolean skipEndMessages = false;
    Player waitingPlayer;
    Player activePlayer;
    if (startingSide == Side.NORTH)
    {
      activePlayer = this.playerNorth;
      waitingPlayer = this.playerSouth;
    }
    else
    {
      activePlayer = this.playerSouth;
      waitingPlayer = this.playerNorth;
    }
    try
    {
      String activePlayerMessage = "";
      
      try
      {
        this.playerNorth.getMove(Protocol.createStartMsg(Side.NORTH));
      }
      catch (Exception e)
      {
        abortingPlayer = this.playerNorth;throw e;
      }
      try
      {
    	  activePlayerMessage = this.playerSouth.getMove(Protocol.createStartMsg(Side.SOUTH));
      }
      catch (Exception e)
      {
        abortingPlayer = this.playerSouth;throw e;
      }
      
      boolean gameOver = false;
      for (int moveCount = 1; !gameOver; moveCount++)
      {
    	activePlayer.incrementMoveCount();
        MsgType msgType = Protocol.getMessageType(activePlayerMessage);
        if ((msgType == MsgType.SWAP) && (moveCount == 2))
        {
          this.playerNorth.changeSide();
          this.playerSouth.changeSide();
          
          Player tmpPlayer = this.playerNorth;
          this.playerNorth = this.playerSouth;
          this.playerSouth = tmpPlayer;
          

          tmpPlayer = activePlayer;
          activePlayer = waitingPlayer;
          waitingPlayer = tmpPlayer;
          

          System.err.println("Move: Swap");
          activePlayerMessage = activePlayer.getMove(Protocol.createSwapInfoMsg(this.kalah.getBoard()));
        }
        else
        {
          if (msgType != MsgType.MOVE) {
            throw new InvalidMessageException("Expected a move message.");
          }
          
          int hole = Protocol.interpretMoveMsg(activePlayerMessage);
          
          if (hole < 1) {
            throw new InvalidMessageException("Expected a positive hole number but got " + 
              hole + ".");
          }
          
          Move move = new Move(activePlayer.getSide(), hole);
          
          if (!this.kalah.isLegalMove(move)) {
            throw new IllegalMoveException();
          }
          Side turn = this.kalah.makeMove(move);
          
          if (moveCount == 1) {
            turn = waitingPlayer.getSide();
          }
          
          gameOver = this.kalah.gameOver();
          if (turn != activePlayer.getSide())
          {
            Player tmpPlayer = activePlayer;
            activePlayer = waitingPlayer;
            waitingPlayer = tmpPlayer;
          }
          
          activePlayerMessage = activePlayer.getMove(Protocol.createStateMsg(move, this.kalah.getBoard(), gameOver, true));
        }
      }
    }
    catch (InvalidMessageException e)
    {
      abortingPlayer = activePlayer;
      System.err.println("Error: Invalid message. " + e.getMessage() + " Agent " + 
        abortingPlayer.getName() + " does not obey the protocol.");
    }
    catch (IllegalMoveException e)
    {
      abortingPlayer = activePlayer;
      System.err.println("Error: Agent " + abortingPlayer.getName() + 
        " tried to perform an illegal move.");
    }
    catch (Exception e)
    {
      if (abortingPlayer == null) {
        abortingPlayer = activePlayer;
      }
      System.err.println("Error: Connection to agent " + abortingPlayer.getName() + 
        " broke down. " + e.getMessage());
      
      Player sanePlayer = abortingPlayer == this.playerNorth ? this.playerSouth : this.playerNorth;
      sanePlayer.getMove(Protocol.createEndMsg());
      
      skipEndMessages = true;
    }
    if (!skipEndMessages)
    {
      String endMessage = Protocol.createEndMsg();
      try
      {
        this.playerNorth.getMove(endMessage);
      }
      catch (Exception localIOException2) {}
      try
      {
        this.playerSouth.getMove(endMessage);
      }
      catch (Exception localIOException3) {}
    }
    return abortingPlayer;
  }
  
  private void evaluate(Player abortingPlayer)
  {
    boolean northWon = false;boolean southWon = false;
    int score = 0;
    if (abortingPlayer == this.playerNorth)
    {
      southWon = true;
    }
    else if (abortingPlayer == this.playerSouth)
    {
      northWon = true;
    }
    else
    {
      int seedDifference = this.kalah.getBoard().getSeedsInStore(Side.NORTH) - 
        this.kalah.getBoard().getSeedsInStore(Side.SOUTH);
      if (seedDifference > 0) {
        northWon = true;
      } else if (seedDifference < 0) {
        southWon = true;
      }
      score = seedDifference;
      if (score < 0) {
        score = -score;
      }
    }
    System.err.println();
    if ((northWon) || (southWon))
    {
      System.err.print("WINNER: Player ");
      if (northWon) {
        System.err.println(this.playerNorth.getPlayerNumber() + 
          " (" + this.playerNorth.getName() + ")");
      } else if (southWon) {
        System.err.println(this.playerSouth.getPlayerNumber() + 
          " (" + this.playerSouth.getName() + ")");
      }
    }
    else
    {
      System.err.println("DRAW");
    }
    if (abortingPlayer != null) {
      System.err.println("MATCH WAS ABORTED");
    } else {
      System.err.println("SCORE: " + score);
    }
    long millisecsPerMoveSouth = this.playerSouth.getMoveCount() == 0 ? 0L : this.playerSouth.getOverallResponseTime() / this.playerSouth.getMoveCount();
    long millisecsPerMoveNorth = this.playerNorth.getMoveCount() == 0 ? 0L : this.playerNorth.getOverallResponseTime() / this.playerNorth.getMoveCount();
    
    System.err.println("\nPlayer " + this.playerSouth.getPlayerNumber() + 
      " (" + this.playerSouth.getName() + "): " + 
      this.playerSouth.getMoveCount() + " moves, " + 
      millisecsPerMoveSouth + " milliseconds per move");
    System.err.println("Player " + this.playerNorth.getPlayerNumber() + 
      " (" + this.playerNorth.getName() + "): " + 
      this.playerNorth.getMoveCount() + " moves, " + 
      millisecsPerMoveNorth + " milliseconds per move");
    System.err.println();
    


    String resultsPlayerNorth = (northWon ? "1" : "0") + " " + millisecsPerMoveNorth;
    String resultsPlayerSouth = (southWon ? "1" : "0") + " " + millisecsPerMoveSouth;
    if (this.playerNorth.getPlayerNumber() == 1)
    {
      System.out.println(resultsPlayerNorth);
      System.out.println(resultsPlayerSouth);
    }
    else
    {
      System.out.println(resultsPlayerSouth);
      System.out.println(resultsPlayerNorth);
    }
  }
}

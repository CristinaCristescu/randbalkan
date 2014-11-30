package ManKalah;

public class Kalah
{
  private final Board board;
  
  public Kalah(Board board)
    throws NullPointerException
  {
    if (board == null) {
      throw new NullPointerException();
    }
    this.board = board;
  }
  
  public Board getBoard()
  {
    return this.board;
  }
  
  public boolean isLegalMove(Move move)
  {
    return isLegalMove(this.board, move);
  }
  
  public Side makeMove(Move move)
  {
    return makeMove(this.board, move);
  }
  
  public boolean gameOver()
  {
    return gameOver(this.board);
  }
  
  public static boolean isLegalMove(Board board, Move move)
  {
    return (move.getHole() <= board.getNoOfHoles()) && (board.getSeeds(move.getSide(), move.getHole()) != 0);
  }
  
  public static Side makeMove(Board board, Move move)
  {
    int seedsToSow = board.getSeeds(move.getSide(), move.getHole());
    board.setSeeds(move.getSide(), move.getHole(), 0);
    
    int holes = board.getNoOfHoles();
    int receivingPits = 2 * holes + 1;
    int rounds = seedsToSow / receivingPits;
    int extra = seedsToSow % receivingPits;
    if (rounds != 0)
    {
      for (int hole = 1; hole <= holes; hole++)
      {
        board.addSeeds(Side.NORTH, hole, rounds);
        board.addSeeds(Side.SOUTH, hole, rounds);
      }
      board.addSeedsToStore(move.getSide(), rounds);
    }
    Side sowSide = move.getSide();
    int sowHole = move.getHole();
    for (; extra > 0; extra--)
    {
      sowHole++;
      if (sowHole == 1) {
        sowSide = sowSide.opposite();
      }
      if (sowHole > holes)
      {
        if (sowSide == move.getSide())
        {
          sowHole = 0;
          board.addSeedsToStore(sowSide, 1);
        }
        else
        {
          sowSide = sowSide.opposite();
          sowHole = 1;
        }
      }
      else {
        board.addSeeds(sowSide, sowHole, 1);
      }
    }
    if ((sowSide == move.getSide()) && 
      (sowHole > 0) && 
      (board.getSeeds(sowSide, sowHole) == 1) && 
      (board.getSeedsOp(sowSide, sowHole) > 0))
    {
      board.addSeedsToStore(move.getSide(), 1 + board.getSeedsOp(move.getSide(), sowHole));
      board.setSeeds(move.getSide(), sowHole, 0);
      board.setSeedsOp(move.getSide(), sowHole, 0);
    }
    Side finishedSide = null;
    if (holesEmpty(board, move.getSide())) {
      finishedSide = move.getSide();
    } else if (holesEmpty(board, move.getSide().opposite())) {
      finishedSide = move.getSide().opposite();
    }
    if (finishedSide != null)
    {
      int seeds = 0;
      Side collectingSide = finishedSide.opposite();
      for (int hole = 1; hole <= holes; hole++)
      {
        seeds += board.getSeeds(collectingSide, hole);
        board.setSeeds(collectingSide, hole, 0);
      }
      board.addSeedsToStore(collectingSide, seeds);
    }
    board.notifyObservers(move);
    if (sowHole == 0) {
      return move.getSide();
    }
    return move.getSide().opposite();
  }
  
  protected static boolean holesEmpty(Board board, Side side)
  {
    for (int hole = 1; hole <= board.getNoOfHoles(); hole++) {
      if (board.getSeeds(side, hole) != 0) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean gameOver(Board board)
  {
    return (holesEmpty(board, Side.NORTH)) || (holesEmpty(board, Side.SOUTH));
  }
}

package JimmyAI;

public class Kalah
{
  private final Board board;
  
  public Kalah(Board paramBoard)
    throws NullPointerException
  {
    if (paramBoard == null) {
      throw new NullPointerException();
    }
    this.board = paramBoard;
  }
  
  public Board getBoard()
  {
    return this.board;
  }
  
  public boolean isLegalMove(Move paramMove)
  {
    return isLegalMove(this.board, paramMove);
  }
  
  public Side makeMove(Move paramMove)
  {
    return makeMove(this.board, paramMove);
  }
  
  public boolean gameOver()
  {
    return gameOver(this.board);
  }
  
  public static boolean isLegalMove(Board paramBoard, Move paramMove)
  {
    return (paramMove.getHole() <= paramBoard.getNoOfHoles()) && (paramBoard.getSeeds(paramMove.getSide(), paramMove.getHole()) != 0);
  }
  
  public static Side makeMove(Board paramBoard, Move paramMove)
  {
    int i = paramBoard.getSeeds(paramMove.getSide(), paramMove.getHole());
    paramBoard.setSeeds(paramMove.getSide(), paramMove.getHole(), 0);
    
    int j = paramBoard.getNoOfHoles();
    int k = 2 * j + 1;
    int m = i / k;
    int n = i % k;
    if (m != 0)
    {
      for (int i1 = 1; i1 <= j; i1++)
      {
        paramBoard.addSeeds(Side.NORTH, i1, m);
        paramBoard.addSeeds(Side.SOUTH, i1, m);
      }
      paramBoard.addSeedsToStore(paramMove.getSide(), m);
    }
    Side localSide1 = paramMove.getSide();
    int i2 = paramMove.getHole();
    for (; n > 0; n--)
    {
      i2++;
      if (i2 == 1) {
        localSide1 = localSide1.opposite();
      }
      if (i2 > j)
      {
        if (localSide1 == paramMove.getSide())
        {
          i2 = 0;
          paramBoard.addSeedsToStore(localSide1, 1);
        }
        else
        {
          localSide1 = localSide1.opposite();
          i2 = 1;
        }
      }
      else {
        paramBoard.addSeeds(localSide1, i2, 1);
      }
    }
    if ((localSide1 == paramMove.getSide()) && (i2 > 0) && (paramBoard.getSeeds(localSide1, i2) == 1) && (paramBoard.getSeedsOp(localSide1, i2) > 0))
    {
      paramBoard.addSeedsToStore(paramMove.getSide(), 1 + paramBoard.getSeedsOp(paramMove.getSide(), i2));
      paramBoard.setSeeds(paramMove.getSide(), i2, 0);
      paramBoard.setSeedsOp(paramMove.getSide(), i2, 0);
    }
    Side localSide2 = null;
    if (holesEmpty(paramBoard, paramMove.getSide())) {
      localSide2 = paramMove.getSide();
    } else if (holesEmpty(paramBoard, paramMove.getSide().opposite())) {
      localSide2 = paramMove.getSide().opposite();
    }
    if (localSide2 != null)
    {
      int i3 = 0;
      Side localSide3 = localSide2.opposite();
      for (int i4 = 1; i4 <= j; i4++)
      {
        i3 += paramBoard.getSeeds(localSide3, i4);
        paramBoard.setSeeds(localSide3, i4, 0);
      }
      paramBoard.addSeedsToStore(localSide3, i3);
    }
    paramBoard.notifyObservers(paramMove);
    if (i2 == 0) {
      return paramMove.getSide();
    }
    return paramMove.getSide().opposite();
  }
  
  protected static boolean holesEmpty(Board paramBoard, Side paramSide)
  {
    for (int i = 1; i <= paramBoard.getNoOfHoles(); i++) {
      if (paramBoard.getSeeds(paramSide, i) != 0) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean gameOver(Board paramBoard)
  {
    return (holesEmpty(paramBoard, Side.NORTH)) || (holesEmpty(paramBoard, Side.SOUTH));
  }
}

package JimmyPlayer;

import java.util.ArrayList;
import java.util.Observable;

public class Board
  extends Observable
  implements Cloneable
{
  private static final int NORTH_ROW = 0;
  private static final int SOUTH_ROW = 1;
  private final int holes;
  private int[][] board;
  
  private static int indexOfSide(Side paramSide)
  {
    switch (paramSide)
    {
    case NORTH: 
      return 0;
    case SOUTH: 
      return 1;
    }
    return -1;
  }
  
  public Board(int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    if (paramInt1 < 1) {
      throw new IllegalArgumentException("There has to be at least one hole, but " + paramInt1 + " were requested.");
    }
    if (paramInt2 < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + paramInt2 + " were requested.");
    }
    this.holes = paramInt1;
    this.board = new int[2][paramInt1 + 1];
    for (int i = 1; i <= paramInt1; i++)
    {
      this.board[0][i] = paramInt2;
      this.board[1][i] = paramInt2;
    }
  }
  
  public Board(Board paramBoard)
  {
    this.holes = paramBoard.holes;
    this.board = new int[2][this.holes + 1];
    for (int i = 0; i <= this.holes; i++)
    {
      this.board[0][i] = paramBoard.board[0][i];
      this.board[1][i] = paramBoard.board[1][i];
    }
  }
  
  public Board clone()
    throws CloneNotSupportedException
  {
    return new Board(this);
  }
  
  public int getNoOfHoles()
  {
    return this.holes;
  }
  
  public int getSeeds(Side paramSide, int paramInt)
    throws IllegalArgumentException
  {
    if ((paramInt < 1) || (paramInt > this.holes)) {
      throw new IllegalArgumentException("Hole number must be between 1 and " + (this.board[0].length - 1) + " but was " + paramInt + ".");
    }
    return this.board[indexOfSide(paramSide)][paramInt];
  }
  
  public void setSeeds(Side paramSide, int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    if ((paramInt1 < 1) || (paramInt1 > this.holes)) {
      throw new IllegalArgumentException("Hole number must be between 1 and " + (this.board[0].length - 1) + " but was " + paramInt1 + ".");
    }
    if (paramInt2 < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + paramInt2 + " were requested.");
    }
    this.board[indexOfSide(paramSide)][paramInt1] = paramInt2;
    setChanged();
  }
  
  public void addSeeds(Side paramSide, int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    if ((paramInt1 < 1) || (paramInt1 > this.holes)) {
      throw new IllegalArgumentException("Hole number must be between 1 and " + (this.board[0].length - 1) + " but was " + paramInt1 + ".");
    }
    if (paramInt2 < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + paramInt2 + " were requested.");
    }
    try{
    this.board[indexOfSide(paramSide)][paramInt1] += paramInt2;
    }
    catch(Exception e)
    {
    	String s = "hello";
    	s +="s";
    }
    setChanged();
  }
  
  public int getSeedsOp(Side paramSide, int paramInt)
    throws IllegalArgumentException
  {
    if ((paramInt < 1) || (paramInt > this.holes)) {
      throw new IllegalArgumentException("Hole number must be between 1 and " + this.holes + " but was " + paramInt + ".");
    }
    return this.board[(1 - indexOfSide(paramSide))][(this.holes + 1 - paramInt)];
  }
  
  public void setSeedsOp(Side paramSide, int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    if ((paramInt1 < 1) || (paramInt1 > this.holes)) {
      throw new IllegalArgumentException("Hole number must be between 1 and " + (this.board[0].length - 1) + " but was " + paramInt1 + ".");
    }
    if (paramInt2 < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + paramInt2 + " were requested.");
    }
    this.board[(1 - indexOfSide(paramSide))][(this.holes + 1 - paramInt1)] = paramInt2;
    setChanged();
  }
  
  public void addSeedsOp(Side paramSide, int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    if ((paramInt1 < 1) || (paramInt1 > this.holes)) {
      throw new IllegalArgumentException("Hole number must be between 1 and " + (this.board[0].length - 1) + " but was " + paramInt1 + ".");
    }
    if (paramInt2 < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + paramInt2 + " were requested.");
    }
    this.board[(1 - indexOfSide(paramSide))][(this.holes + 1 - paramInt1)] += paramInt2;
    setChanged();
  }
  
  public int getSeedsInStore(Side paramSide)
  {
    return this.board[indexOfSide(paramSide)][0];
  }
  
  public void setSeedsInStore(Side paramSide, int paramInt)
    throws IllegalArgumentException
  {
    if (paramInt < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + paramInt + " were requested.");
    }
    this.board[indexOfSide(paramSide)][0] = paramInt;
    setChanged();
  }
  
  public void addSeedsToStore(Side paramSide, int paramInt)
    throws IllegalArgumentException
  {
    if (paramInt < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + paramInt + " were requested.");
    }
    this.board[indexOfSide(paramSide)][0] += paramInt;
    setChanged();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    
    localStringBuilder.append(this.board[0][0] + "  --");
    for (int i = this.holes; i >= 1; i--) {
      localStringBuilder.append("  " + this.board[0][i]);
    }
    localStringBuilder.append("\n");
    for (int i = 1; i <= this.holes; i++) {
      localStringBuilder.append(this.board[1][i] + "  ");
    }
    localStringBuilder.append("--  " + this.board[1][0] + "\n");
    
    return localStringBuilder.toString();
  }
  
  public ArrayList<Integer> getPossibleMoves(Side paramSide)
  {
    ArrayList localArrayList = new ArrayList();
    int i = indexOfSide(paramSide);
    for (int j = 1; j <= getNoOfHoles(); j++) {
      if (this.board[i][j] > 0) {
        localArrayList.add(Integer.valueOf(j));
      }
    }
    return localArrayList;
  }
}

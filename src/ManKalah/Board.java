package ManKalah;

import java.util.Observable;

public class Board
  extends Observable
  implements Cloneable
{
  private static final int NORTH_ROW = 0;
  private static final int SOUTH_ROW = 1;
  private final int holes;
  private int[][] board;
  
  private static int indexOfSide(Side side)
  {
    switch (side)
    {
    case NORTH: 
      return 0;
    case SOUTH: 
      return 1;
    }
    return -1;
  }
  
  public Board(int holes, int seeds)
    throws IllegalArgumentException
  {
    if (holes < 1) {
      throw new IllegalArgumentException("There has to be at least one hole, but " + holes + " were requested.");
    }
    if (seeds < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + seeds + " were requested.");
    }
    this.holes = holes;
    this.board = new int[2][holes + 1];
    for (int i = 1; i <= holes; i++)
    {
      this.board[0][i] = seeds;
      this.board[1][i] = seeds;
    }
  }
  
  public Board(Board original)
  {
    this.holes = original.holes;
    this.board = new int[2][this.holes + 1];
    for (int i = 0; i <= this.holes; i++)
    {
      this.board[0][i] = original.board[0][i];
      this.board[1][i] = original.board[1][i];
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
  
  public int getSeeds(Side side, int hole)
    throws IllegalArgumentException
  {
    if ((hole < 1) || (hole > this.holes)) {
      throw new IllegalArgumentException("Hole number must be between 1 and " + (this.board[0].length - 1) + " but was " + hole + ".");
    }
    return this.board[indexOfSide(side)][hole];
  }
  
  public void setSeeds(Side side, int hole, int seeds)
    throws IllegalArgumentException
  {
    if ((hole < 1) || (hole > this.holes)) {
      throw new IllegalArgumentException("Hole number must be between 1 and " + (this.board[0].length - 1) + " but was " + hole + ".");
    }
    if (seeds < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + seeds + " were requested.");
    }
    this.board[indexOfSide(side)][hole] = seeds;
    setChanged();
  }
  
  public void addSeeds(Side side, int hole, int seeds)
    throws IllegalArgumentException
  {
    if ((hole < 1) || (hole > this.holes)) {
      throw new IllegalArgumentException("Hole number must be between 1 and " + (this.board[0].length - 1) + " but was " + hole + ".");
    }
    if (seeds < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + seeds + " were requested.");
    }
    this.board[indexOfSide(side)][hole] += seeds;
    setChanged();
  }
  
  public int getSeedsOp(Side side, int hole)
    throws IllegalArgumentException
  {
    if ((hole < 1) || (hole > this.holes)) {
      throw new IllegalArgumentException("Hole number must be between 1 and " + this.holes + " but was " + hole + ".");
    }
    return this.board[(1 - indexOfSide(side))][(this.holes + 1 - hole)];
  }
  
  public void setSeedsOp(Side side, int hole, int seeds)
    throws IllegalArgumentException
  {
    if ((hole < 1) || (hole > this.holes)) {
      throw new IllegalArgumentException("Hole number must be between 1 and " + (this.board[0].length - 1) + " but was " + hole + ".");
    }
    if (seeds < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + seeds + " were requested.");
    }
    this.board[(1 - indexOfSide(side))][(this.holes + 1 - hole)] = seeds;
    setChanged();
  }
  
  public void addSeedsOp(Side side, int hole, int seeds)
    throws IllegalArgumentException
  {
    if ((hole < 1) || (hole > this.holes)) {
      throw new IllegalArgumentException("Hole number must be between 1 and " + (this.board[0].length - 1) + " but was " + hole + ".");
    }
    if (seeds < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + seeds + " were requested.");
    }
    this.board[(1 - indexOfSide(side))][(this.holes + 1 - hole)] += seeds;
    setChanged();
  }
  
  public int getSeedsInStore(Side side)
  {
    return this.board[indexOfSide(side)][0];
  }
  
  public void setSeedsInStore(Side side, int seeds)
    throws IllegalArgumentException
  {
    if (seeds < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + seeds + " were requested.");
    }
    this.board[indexOfSide(side)][0] = seeds;
    setChanged();
  }
  
  public void addSeedsToStore(Side side, int seeds)
    throws IllegalArgumentException
  {
    if (seeds < 0) {
      throw new IllegalArgumentException("There has to be a non-negative number of seeds, but " + seeds + " were requested.");
    }
    this.board[indexOfSide(side)][0] += seeds;
    setChanged();
  }
  
  public String toString()
  {
    StringBuilder boardString = new StringBuilder();
    
    boardString.append(this.board[0][0] + "  --");
    for (int i = this.holes; i >= 1; i--) {
      boardString.append("  " + this.board[0][i]);
    }
    boardString.append("\n");
    for (int i = 1; i <= this.holes; i++) {
      boardString.append(this.board[1][i] + "  ");
    }
    boardString.append("--  " + this.board[1][0] + "\n");
    
    return boardString.toString();
  }
}

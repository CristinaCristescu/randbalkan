package ManKalah;

public class Move
{
  private final Side side;
  private final int hole;
  
  public Move(Side side, int hole)
    throws IllegalArgumentException
  {
    if (hole < 1) {
      throw new IllegalArgumentException("Hole numbers must be >= 1, but " + hole + " was given.");
    }
    this.side = side;
    this.hole = hole;
  }
  
  public Side getSide()
  {
    return this.side;
  }
  
  public int getHole()
  {
    return this.hole;
  }
}

package JimmyAI;

public class Move
{
  private final Side side;
  private final int hole;
  
  public Move(Side paramSide, int paramInt)
    throws IllegalArgumentException
  {
    if (paramInt < 1) {
      throw new IllegalArgumentException("Hole numbers must be >= 1, but " + paramInt + " was given.");
    }
    this.side = paramSide;
    this.hole = paramInt;
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

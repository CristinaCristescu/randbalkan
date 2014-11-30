package JimmyPlayer;

public enum Side
{
  NORTH,  SOUTH;
  
  private Side() {}
  
  public Side opposite()
  {
    switch (this.name())
    {
    case "NORTH": 
      return SOUTH;
    case "SOUTH": 
      return NORTH;
    }
    return NORTH;
  }
}

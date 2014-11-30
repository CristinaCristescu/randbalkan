package ManKalah;

import KalahAI.KalahAI;

public class Player
{
  private String name;
  private int playerNumber;
  private KalahAI agent;
  private Side side;
  private int moveCount = 0;
  private long overallResponseTime = 0L;
  
  public Player(int playerNumber, String name, KalahAI agent, Side initialSide)
  {
    this.playerNumber = playerNumber;
    this.name = name;
    this.setAgent(agent);
    this.side = initialSide;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public Side getSide()
  {
    return this.side;
  }
  
  public int getPlayerNumber()
  {
    return this.playerNumber;
  }
  
  public void changeSide()
  {
    this.side = this.side.opposite();
  }
  
  public int getMoveCount()
  {
    return this.moveCount;
  }
  
  public void incrementMoveCount()
  {
    this.moveCount += 1;
  }
  
  public long getOverallResponseTime()
  {
    return this.overallResponseTime;
  }
  
  public void incrementOverallResponseTime(long additionalResponseTime)
  {
    this.overallResponseTime += additionalResponseTime;
  }
  
  public String getMove(String msg)
  {
	  return this.getAgent().makeMove(msg);
  }

public KalahAI getAgent() {
	return agent;
}

public void setAgent(KalahAI agent) {
	this.agent = agent;
}
}

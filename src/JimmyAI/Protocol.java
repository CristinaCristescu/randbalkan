package JimmyAI;

public class Protocol
{
  public static String createMoveMsg(int paramInt)
  {
    return "MOVE;" + paramInt + "\n";
  }
  
  public static String createSwapMsg()
  {
    return "SWAP\n";
  }
  
  public static MsgType getMessageType(String paramString)
    throws InvalidMessageException
  {
    if (paramString.startsWith("START;")) {
      return MsgType.START;
    }
    if (paramString.startsWith("CHANGE;")) {
      return MsgType.STATE;
    }
    if (paramString.equals("END\n")) {
      return MsgType.END;
    }
    throw new InvalidMessageException("Could not determine message type.");
  }
  
  public static boolean interpretStartMsg(String paramString)
    throws InvalidMessageException
  {
    if (paramString.charAt(paramString.length() - 1) != '\n') {
      throw new InvalidMessageException("Message not terminated with 0x0A character.");
    }
    String str = paramString.substring(6, paramString.length() - 1);
    if (str.equals("South")) {
      return true;
    }
    if (str.equals("North")) {
      return false;
    }
    throw new InvalidMessageException("Illegal position parameter: " + str);
  }
  
  public static MoveTurn interpretStateMsg(String paramString, Board paramBoard)
    throws InvalidMessageException
  {
    MoveTurn localMoveTurn = new MoveTurn();
    if (paramString.charAt(paramString.length() - 1) != '\n') {
      throw new InvalidMessageException("Message not terminated with 0x0A character.");
    }
    String[] arrayOfString1 = paramString.split(";", 4);
    if (arrayOfString1.length != 4) {
      throw new InvalidMessageException("Missing arguments.");
    }
    if (arrayOfString1[1].equals("SWAP")) {
      localMoveTurn.move = -1;
    } else {
      try
      {
        localMoveTurn.move = Integer.parseInt(arrayOfString1[1]);
      }
      catch (NumberFormatException localNumberFormatException1)
      {
        throw new InvalidMessageException("Illegal value for move parameter: " + localNumberFormatException1.getMessage());
      }
    }
    String[] arrayOfString2 = arrayOfString1[2].split(",", -1);
    if (2 * (paramBoard.getNoOfHoles() + 1) != arrayOfString2.length) {
      throw new InvalidMessageException("Board dimensions in message (" + arrayOfString2.length + " entries) are not as expected (" + 2 * (paramBoard.getNoOfHoles() + 1) + " entries).");
    }
    try
    {
      for (int i = 0; i < paramBoard.getNoOfHoles(); i++) {
        paramBoard.setSeeds(Side.NORTH, i + 1, Integer.parseInt(arrayOfString2[i]));
      }
      paramBoard.setSeedsInStore(Side.NORTH, Integer.parseInt(arrayOfString2[paramBoard.getNoOfHoles()]));
      for (int i = 0; i < paramBoard.getNoOfHoles(); i++) {
        paramBoard.setSeeds(Side.SOUTH, i + 1, Integer.parseInt(arrayOfString2[(i + paramBoard.getNoOfHoles() + 1)]));
      }
      paramBoard.setSeedsInStore(Side.SOUTH, Integer.parseInt(arrayOfString2[(2 * paramBoard.getNoOfHoles() + 1)]));
    }
    catch (NumberFormatException localNumberFormatException2)
    {
      throw new InvalidMessageException("Illegal value for seed count: " + localNumberFormatException2.getMessage());
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new InvalidMessageException("Illegal value for seed count: " + localIllegalArgumentException.getMessage());
    }
    localMoveTurn.end = false;
    if (arrayOfString1[3].equals("YOU\n"))
    {
      localMoveTurn.again = true;
    }
    else if (arrayOfString1[3].equals("OPP\n"))
    {
      localMoveTurn.again = false;
    }
    else if (arrayOfString1[3].equals("END\n"))
    {
      localMoveTurn.end = true;
      localMoveTurn.again = false;
    }
    else
    {
      throw new InvalidMessageException("Illegal value for turn parameter: " + arrayOfString1[3]);
    }
    return localMoveTurn;
  }
  
  public static class MoveTurn
  {
    public boolean end;
    public boolean again;
    public int move;
  }
}

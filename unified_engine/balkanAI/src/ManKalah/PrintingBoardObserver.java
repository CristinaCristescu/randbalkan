package ManKalah;

import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

public class PrintingBoardObserver
  implements Observer
{
  protected PrintStream outputStream;
  
  public PrintingBoardObserver(PrintStream outputStream)
  {
    if (outputStream == null) {
      throw new NullPointerException();
    }
    this.outputStream = outputStream;
  }
  
  public void update(Observable o, Object arg)
  {
    Board board = (Board)o;
    if ((arg != null) && ((arg instanceof Move)))
    {
      Move move = (Move)arg;
      this.outputStream.print("Move: ");
      switch (move.getSide())
      {
      case NORTH: 
        this.outputStream.print("North"); break;
      case SOUTH: 
        this.outputStream.print("South");
      }
      this.outputStream.println(" - " + move.getHole());
    }
    this.outputStream.print(board);
  }
}

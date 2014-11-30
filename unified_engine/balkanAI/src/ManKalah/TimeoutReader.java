/*package ManKalah;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TimeoutReader
  extends Thread
{
  private Player player;
  private boolean getNextMessage = false;
  private boolean terminate = false;
  private String message = null;
  private IOException ioException = null;
  private Thread requestor = null;
  
  public TimeoutReader(Player player)
  {
    this.player = player;
  }
  
  public String recvMsg(long milliseconds)
    throws IOException, TimeoutException
  {
    if (milliseconds < 0L) {
      milliseconds = 0L;
    }
    this.message = null;
    this.ioException = null;
    synchronized (this)
    {
      this.requestor = Thread.currentThread();
    }
    this.getNextMessage = true;
    synchronized (this)
    {
      notifyAll();
    }
    try
    {
      sleep(milliseconds);
      
      throw new TimeoutException("Receiving message from agent has timed out.");
    }
    catch (InterruptedException localInterruptedException)
    {
      if (this.ioException != null) {
        throw this.ioException;
      }
      synchronized (this)
      {
        this.requestor = null;
      }
    }
    return this.message;
  }
  
  public void run()
  {
    for (;;)
    {
      synchronized (this)
      {
        try
        {
          wait();
        }
        catch (InterruptedException localInterruptedException) {}
      }
      if (this.terminate) {
        return;
      }
      while (this.getNextMessage)
      {
        this.getNextMessage = false;
        try
        {
          this.message = this.player.recvMsg();
        }
        catch (IOException e)
        {
          this.ioException = e;
        }
        synchronized (this)
        {
          if (this.requestor != null) {
            this.requestor.interrupt();
          }
        }
      }
    }
  }
  
  public void finish()
  {
    this.terminate = true;
    synchronized (this)
    {
      notifyAll();
    }
  }
}
*/
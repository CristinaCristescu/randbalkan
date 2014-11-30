package ManKalah;

public class Timer
{
  private long milliseconds = 0L;
  private long startTime = 0L;
  
  public void start()
  {
    this.startTime = System.currentTimeMillis();
  }
  
  public void stop()
  {
    long stopTime = System.currentTimeMillis();
    this.milliseconds += stopTime - this.startTime;
  }
  
  public void reset()
  {
    this.milliseconds = 0L;
  }
  
  public long time()
  {
    return this.milliseconds;
  }
}

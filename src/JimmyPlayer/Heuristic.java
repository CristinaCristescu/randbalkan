package JimmyPlayer;

public class Heuristic
{
  public static double getScore(Board paramBoard, Side paramSide)
  {
    double d1 = 0.0D;
    
    double d2 = paramBoard.getSeedsInStore(paramSide);
    
    double d3 = paramBoard.getSeedsInStore(paramSide.opposite());
    if ((d2 != 0.0D) || (d3 != 0.0D)) {
      if (d2 != d3)
      {
        double d4;
        double d5;
        if (d2 > d3)
        {
          d4 = d2;
          d5 = d3;
        }
        else
        {
          d4 = d3;
          d5 = d2;
        }
        d1 = (1.0D / d4 * (d4 - d5) + 1.0D) * d4;
        if (d3 > d2) {
          d1 *= -1.0D;
        }
      }
    }
    for (int i = 1; i <= paramBoard.getNoOfHoles(); i++) {
      if ((paramBoard.getSeeds(paramSide, i) == 0) && 
        (isSeedable(paramBoard, paramSide, i))) {
        d1 += paramBoard.getSeedsOp(paramSide, i) / 2;
      }
    }
    for (int i = 1; i <= paramBoard.getNoOfHoles(); i++) {
      if (paramBoard.getNoOfHoles() - i + 1 == paramBoard.getSeeds(paramSide, i)) {
        d1 += 1.0D;
      }
    }
    int i = 0;
    for (int j = 1; j <= paramBoard.getNoOfHoles(); j++) {
      i += paramBoard.getSeeds(paramSide, j);
    }
    int j = 0;
    for (int k = 1; k <= paramBoard.getNoOfHoles(); k++) {
      j += paramBoard.getSeeds(paramSide.opposite(), k);
    }
    int k = i - j;
    
    d1 += k / 2;
    for (int m = 1; m <= paramBoard.getNoOfHoles(); m++) {
      if ((paramBoard.getSeeds(paramSide.opposite(), m) == 0) && 
        (isSeedable(paramBoard, paramSide.opposite(), m))) {
        d1 -= paramBoard.getSeedsOp(paramSide.opposite(), m) / 2;
      }
    }
    return d1;
  }
  
  public static boolean isSeedable(Board paramBoard, Side paramSide, int paramInt)
  {
    boolean bool = false;
    for (int i = paramInt - 1; i > 0; i--) {
      if (paramInt - i == paramBoard.getSeeds(paramSide, i))
      {
        bool = true;
        break;
      }
    }
    return bool;
  }
}

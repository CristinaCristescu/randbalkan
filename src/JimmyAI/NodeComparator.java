package JimmyAI;

import java.util.Comparator;

public class NodeComparator
  implements Comparator<Node>
{
  public int compare(Node localNode1, Node localNode2)
  {
    double d1 = localNode1.score.doubleValue();
    double d2 = localNode2.score.doubleValue();
    
    if (d1 > d2) {
      return -1;
    }
    if (d1 < d2) {
      return 1;
    }
    return 0;
  }
}

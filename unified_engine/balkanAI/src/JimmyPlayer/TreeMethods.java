package JimmyPlayer;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

public class TreeMethods
{
  private static int currentID = 0;
  
  public static Tree buildTree(Board paramBoard, int paramInt, Side side)
  {
    Node localNode = new Node(null, paramBoard, currentID, 0, null, side, side);
    currentID += 1;
    Tree localTree = new Tree(localNode);
    
    buildTreeRecurse(localNode, 1, paramInt, side);
    


    return localTree;
  }
  
  private static void buildTreeRecurse(Node paramNode, int paramInt1, int paramInt2, Side side)
  {
    if (paramInt1 <= paramInt2)
    {
      ArrayList localArrayList = paramNode.board.getPossibleMoves(paramNode.nextMove);
      for (int i = 0; i < localArrayList.size(); i++) {
        try
        {
          Board localBoard = paramNode.board.clone();
          Move localMove = new Move(paramNode.nextMove, ((Integer)localArrayList.get(i)).intValue());
          Side localSide1 = Kalah.makeMove(localBoard, localMove);
          
          Side localSide2 = paramNode.ourSide;
          if (((Integer)localArrayList.get(i)).intValue() == 8) {
            localSide2 = paramNode.ourSide.opposite();
          }
          Node localNode = new Node(paramNode, localBoard, currentID, paramInt1, (Integer)localArrayList.get(i), localSide1, localSide2);
          currentID += 1;
          paramNode.children.add(localNode);
          buildTreeRecurse(localNode, paramInt1 + 1, paramInt2, side);
        }
        catch (Exception localException)
        {
          System.out.println("Error: " + localException.getMessage());
        }
      }
    }
    else
    {
      double d = Heuristic.getScore(paramNode.board, paramNode.nextMove);
      if (paramNode.nextMove != side) {
        d *= -1.0D;
      }
      paramNode.score = Double.valueOf(d);
    }
  }
  
  private static ArrayList<Node> getbestLeafNodesRecurse(Node paramNode, ArrayList<Node> paramArrayList)
  {
    if (paramNode.children == null) {
      paramArrayList.add(paramNode);
    } else {
      for (int i = 0; i < paramNode.children.size(); i++) {
        paramArrayList = getbestLeafNodesRecurse((Node)paramNode.children.get(i), paramArrayList);
      }
    }
    return paramArrayList;
  }
  
  private static ArrayList<Node> getbestNodes(Tree paramTree)
  {
    ArrayList localArrayList1 = new ArrayList();
    
    localArrayList1 = getbestLeafNodesRecurse(paramTree.root, localArrayList1);
    
    Collections.sort(localArrayList1, new NodeComparator());
    
    int i = localArrayList1.size() / 100;
    
    ArrayList localArrayList2 = new ArrayList();
    for (int j = 0; j < i; j++) {
      localArrayList2.add(localArrayList1.get(j));
    }
    return localArrayList2;
  }
  
  private static int getBestMoveWithDepth(Board paramBoard, int paramInt, Side side)
  {
    try
    {
      Tree localTree = buildTree(paramBoard, paramInt, side);
      
      ArrayList localArrayList = getbestNodes(localTree);
      for (int j = 0; j < localArrayList.size(); j++) {
        buildTreeRecurse((Node)localArrayList.get(j), paramInt, paramInt + 5, side);
      }
      return localTree.alphaBeta(localTree.root);
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      return getBestMoveWithDepth(paramBoard, paramInt - 1, side);
    }
  }
  
  public static int getBestMove(Board paramBoard, Side side)
  {
    int i = getBestMoveWithDepth(paramBoard, 7, side);
    return i;
  }
}

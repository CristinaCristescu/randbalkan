package JimmyPlayer;

import java.util.ArrayList;

public class Tree
{
  public Node root;
  
  public Tree(Node paramNode)
  {
    this.root = paramNode;
  }
  
  public void print()
  {
    this.root.print();
  }
  
  public int minimax(Node paramNode)
  {
    Node localNode = minimaxMain(paramNode);
    
    localNode.print();
    int i = getBestMove(localNode);
    return i;
  }
  
  private Node minimaxMain(Node paramNode)
  {
    Node[] arrayOfNode = new Node[paramNode.children.size()];
    if (paramNode.children.isEmpty()) {
      return paramNode;
    }
    for (int i = 0; i < paramNode.children.size(); i++) {
      arrayOfNode[i] = minimaxMain(paramNode.children.get(i));
    }
    if (Side.NORTH == paramNode.nextMove) {
      return max(arrayOfNode);
    }
    return min(arrayOfNode);
  }
  
  private Node min(Node[] paramArrayOfNode)
  {
    Node localNode = paramArrayOfNode[0];
    for (int i = 1; i < paramArrayOfNode.length; i++) {
      if ((localNode == null) || (localNode.score == null)) {
        localNode = paramArrayOfNode[i];
      } else if ((paramArrayOfNode[i] != null) && (paramArrayOfNode[i].score != null)) {
        if (paramArrayOfNode[i].score.doubleValue() < localNode.score.doubleValue()) {
          localNode = paramArrayOfNode[i];
        }
      }
    }
    return localNode;
  }
  
  private Node max(Node[] paramArrayOfNode)
  {
    Node localNode = paramArrayOfNode[0];
    for (int i = 1; i < paramArrayOfNode.length; i++) {
      if ((localNode == null) || (localNode.score == null)) {
        localNode = paramArrayOfNode[i];
      } else if ((paramArrayOfNode[i] != null) && (paramArrayOfNode[i].score != null)) {
        if (paramArrayOfNode[i].score.doubleValue() > localNode.score.doubleValue()) {
          localNode = paramArrayOfNode[i];
        }
      }
    }
    return localNode;
  }
  
  public int alphaBeta(Node paramNode)
  {
    Node localNode = alphaBetaMain(paramNode);
    if ((localNode == null) || (localNode.parent == null)) {
      localNode = minimaxMain(paramNode);
    }
    int i = getBestMove(localNode);
    return i;
  }
  
  public Node alphaBetaMain(Node paramNode)
  {
    Node localNode1 = new Node(4.9E-324D);
    Node localNode2 = new Node(1.7976931348623157E+308D);
    Node localNode3 = alphaBetaMax(paramNode, localNode1, localNode2);
    return localNode3;
  }
  
  private Node alphaBetaMax(Node paramNode1, Node paramNode2, Node paramNode3)
  {
    if (paramNode1.children.isEmpty()) {
      return paramNode1;
    }
    for (int i = 0; i < paramNode1.children.size(); i++)
    {
      Node localNode;
      if (((Node)paramNode1.children.get(i)).nextMove == paramNode1.ourSide) {
        localNode = alphaBetaMax((Node)paramNode1.children.get(i), paramNode2, paramNode3);
      } else {
        localNode = alphaBetaMin((Node)paramNode1.children.get(i), paramNode2, paramNode3);
      }
      if (localNode.score == null) {
        return paramNode3;
      }
      if (paramNode3.score == null) {
        return localNode;
      }
      if (localNode.score.doubleValue() >= paramNode3.score.doubleValue()) {
        return paramNode3;
      }
      if (localNode.score.doubleValue() > paramNode2.score.doubleValue()) {
        paramNode2 = localNode;
      }
    }
    return paramNode2;
  }
  
  private Node alphaBetaMin(Node paramNode1, Node paramNode2, Node paramNode3)
  {
    if (paramNode1.children.isEmpty()) {
      return paramNode1;
    }
    for (int i = 0; i < paramNode1.children.size(); i++)
    {
      Node localNode;
      if (((Node)paramNode1.children.get(i)).nextMove == paramNode1.ourSide) {
        localNode = alphaBetaMax((Node)paramNode1.children.get(i), paramNode2, paramNode3);
      } else {
        localNode = alphaBetaMin((Node)paramNode1.children.get(i), paramNode2, paramNode3);
      }
      if (localNode.score == null) {
        return paramNode2;
      }
      if (paramNode2.score == null) {
        return localNode;
      }
      if (localNode.score.doubleValue() <= paramNode2.score.doubleValue()) {
        return paramNode2;
      }
      if (localNode.score.doubleValue() < paramNode3.score.doubleValue()) {
        paramNode3 = localNode;
      }
    }
    return paramNode3;
  }
  
  private int getBestMove(Node paramNode)
  {
    Node localNode = paramNode;
    while (localNode.parent.parent != null) {
      localNode = localNode.parent;
    }
    return localNode.moveFromParent.intValue();
  }
}

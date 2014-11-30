package JimmyAI;

import java.util.ArrayList;

public class Node
{
  public ArrayList<Node> children;
  public Node parent;
  public Board board;
  public Side nextMove;
  public Double score;
  public int id;
  public int depth;
  public Integer moveFromParent;
  public Side ourSide;
  
  public Node(Node paramNode, Board paramBoard, int paramInt1, int paramInt2, Integer paramInteger, Side paramSide1, Side paramSide2)
  {
    this.children = new ArrayList<Node>();
    this.parent = paramNode;
    this.board = paramBoard;
    this.moveFromParent = paramInteger;
    this.id = paramInt1;
    this.depth = paramInt2;
    this.nextMove = paramSide1;
    this.score = null;
    this.ourSide = paramSide2;
  }
  
  public Node(double score)
  {
    this.score = score;
    this.children = new ArrayList<Node>();
  }
  
  public void addChild(Node paramNode)
  {
    this.children.add(paramNode);
  }
  
  public void print()
  {
    System.out.println("Node: " + this.id + " Depth: " + this.depth + " Score: " + this.score + " Side: " + this.nextMove);
    System.out.println("Move from parent: " + this.moveFromParent);
    if (this.parent != null) 
    {
      System.out.println("Parent: " + this.parent.id + " Children: ");
    } 
    else 
    {
      System.out.println("Parent: null Children: ");
    }
    for (int i = 0; i < this.children.size(); i++) 
    {
    	System.out.println(this.children.get(i).id);
    }
  }
}

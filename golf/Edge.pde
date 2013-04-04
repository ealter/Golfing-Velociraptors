public class Edge {
  private Node _startNode, _endNode;
  private float _length;
  private color _stroke;
  
  public Edge(Node startNode, Node endNode, float length) {
    _startNode = startNode;
    _endNode = endNode;
    _length = length;
    _stroke = color(0, 0, 0);
  }
  
  public Node getStartNode() {
    return _startNode;
  }
  
  public Node getEndNode() {
    return _endNode;
  }
  
  public float getLength() {
    return _length;
  }
  
  public void render() {
    stroke(_stroke);
    line(_startNode.getX(), _startNode.getY(), _endNode.getX(), _endNode.getY());
  }
}

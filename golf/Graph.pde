public class Graph extends RelativeSizeComponent {
  private final float KC = 1000;
  private final float KS = .01;
  private final float ENERGY_LIMIT = .001;
  
  private HashMap<Integer, Node> _nodes;
  private ArrayList<Edge> _edges;
  private boolean _simulating;
  
  public Graph(float x, float y, float width, float height) {
    super(x, y, width, height);
    _nodes = new HashMap<Integer, Node>();
    _edges = new ArrayList<Edge>();
    _simulating = true;
  }
  
  public void addNode(int id) {
    if (_nodes.get(id) == null) {
      _nodes.put(id, new Node(id, random(width + 1), random(height + 1), 20));
    }
  }
  
  public Node getNode(int id) {
    return _nodes.get(id);
  }
  
  public ArrayList<Node> getNodes() {
    ArrayList<Node> nodes = new ArrayList<Node>();
    for (Node n : _nodes.values()) {
      nodes.add(n);
    }
    return nodes;
  }
  
  public void addEdge(int startID, int endID, float length) {
    addNode(startID);
    addNode(endID);
    _edges.add(new Edge(getNode(startID), getNode(endID), length));
  }
  
  public ArrayList<Edge> getEdges() {
    return _edges;
  }
  
  public void setSimulating(boolean simulating) {
    _simulating = simulating;
  }
  
  public void update() {
    if (_simulating) {
      // REPULSION
      for (Node n : _nodes.values()) {
        for (Node other : _nodes.values()) {
          if (n != other) {
            PVector repel = new PVector(other.getX() - n.getX(), other.getY() - n.getY());
            repel.normalize();
            float mag = - KC / n.getDistance2(other);
            repel.mult(mag);
            n.applyForce(repel.x, repel.y);
          }
        }
      }
      
      // ATTRACTION
      for (Edge e : _edges) {
        Node a = e.getStartNode();
        Node b = e.getEndNode();
        PVector attract = new PVector(a.getX() - b.getX(), a.getY() - b.getY());
        attract.normalize();
        float mag = - KS * (a.getDistance(b) - e.getLength());
        attract.mult(mag);
        a.applyForce(attract.x, attract.y);
        b.applyForce(-attract.x, -attract.y);
      }
      
      // MOVEMENT
      float totalEnergy = 0;
      for (Node n : _nodes.values()) {
        totalEnergy += n.getVelocity2();
        n.update();
      }
      
      if (totalEnergy < ENERGY_LIMIT) {
        _simulating = false;
      }
    }
  }
  
  public void render() {
    super.render();
    for (Edge e : _edges) {
      e.render();
    }
    for (Node n : _nodes.values()) {
      n.render();
    }
  }
}

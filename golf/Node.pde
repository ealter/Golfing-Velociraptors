public class Node {
  private int _id;
  private float _x, _y;
  private float _vx, _vy;
  private float _radius;
  private color _stroke, _fill;
  private boolean _userOverride;
  
  public Node(int id, float x, float y, float radius) {
    _id = id;
    _x = x; _y = y;
    _vx = 0; _vy = 0;
    _radius = radius;
    _stroke = color(0, 0, 0);
    _fill = color(255, 0, 0);
    _userOverride = false;
  }
  
  public int getID() {
    return _id;
  }
  
  public float getX() {
    return _x;
  }
  
  public float getY() {
    return _y;
  }
  
  public void moveDelta(float dx, float dy) {
    _x += dx;
    _y += dy;
  }
  
  public void applyForce(float fx, float fy) {
    _vx += fx;
    _vy += fy;
  }
  
  public float getDistance(Node n) {
    return sqrt(getDistance2(n));
  }
  
  public float getDistance2(Node n) {
    return (n.getX() - _x) * (n.getX() - _x) + (n.getY() - _y) * (n.getY() - _y);
  }
  
  public float getVelocity() {
    return sqrt(getVelocity2());
  }
  
  public float getVelocity2() {
    return _vx * _vx + _vy * _vy;
  }
  
  public boolean containsPoint(int x, int y) {
    return (x - _x) * (x - _x) + (y - _y) * (y - _y) <= _radius * _radius;
  }
  
  public void setOverriding(boolean override) {
    _userOverride = override;
  }
  
  public void update() {
    if (!_userOverride) {
      _x += _vx;
      _y += _vy;
    }
    _vx *= .9;
    _vy *= .9;
  }
  
  public void render() {
    stroke(_stroke);
    fill(_fill);
    ellipse(_x, _y, _radius * 2, _radius * 2);
    if (containsPoint(mouseX, mouseY)) {
      fill(color(255, 255, 255, 128));
      ellipse(_x, _y, _radius * 2, _radius * 2);
    }
    stroke(color(0, 0, 0));
    fill(color(0, 0, 0));
    textAlign(CENTER, CENTER);
    text(_id, _x, _y);
  }
}

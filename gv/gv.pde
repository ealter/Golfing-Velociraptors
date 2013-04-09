Graph _graph;
Node _moving;
int _x, _y;

void setup() {
  size(600, 400);
  Parser parser = new Parser("um-outcomes.anon");
  _graph = new Graph(0, 0, 1.0, 1.0);
  _graph.setStroke(color(255, 0, 0, 0));
  _graph.setFill(color(255, 255, 255));
  _graph.addEdge(0, 1, 100);
  _graph.addEdge(1, 2, 100);
  _graph.addEdge(2, 3, 100);
  _graph.addEdge(4, 0, 100);
  _graph.addEdge(4, 1, 100);
  _graph.addEdge(4, 2, 100);
  _graph.addEdge(4, 3, 100);
  _graph.addEdge(5, 4, 100);
  _graph.addEdge(6, 5, 100);
  _graph.addEdge(7, 5, 100);

  mouseEvents.subscribe(new MainHandler());
}

void draw() {
  background(color(0, 0, 0));
  _graph.update();
  _graph.render();

  _graph.drawTooltip();
}


class MainHandler implements MouseEventHandler {
  public MainHandler() {}

  void mouseEvent_pressed() {
    if (mouseButton == LEFT) {
      for (Node n : _graph.getNodes()) {
        if (n.containsPoint(mouseX, mouseY)) {
          _moving = n;
          _x = mouseX;
          _y = mouseY;
        }
      }
      if (_moving != null) {
        _graph.setSimulating(true);
        _moving.setOverriding(true);
      }
    }
  }

  void mouseEvent_dragged() {
    if (_moving != null) {
      _graph.setSimulating(true);
      _moving.moveDelta((float)(mouseX - _x), (float)(mouseY - _y));
      _x = mouseX;
      _y = mouseY;
    }
  }

  void mouseEvent_released() {
    if (mouseButton == LEFT) {
      if (_moving != null) {
        _moving.setOverriding(false);
        _moving = null;
      }
    }
  }

  void mouseEvent_moved() {}
  void mouseEvent_clicked() {}
}


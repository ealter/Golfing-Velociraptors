public interface MouseEventHandler {
  void mouseEvent_moved   ();
  void mouseEvent_clicked ();
  void mouseEvent_dragged ();
  void mouseEvent_pressed ();
  void mouseEvent_released();
}

public class MouseEventDispatcher {
  ArrayList _subscribers;
  int _previousX;
  int _previousY;

  public MouseEventDispatcher() {
    _subscribers = new ArrayList();
    updatePreviousMousePoint();
  }

  public void subscribe(MouseEventHandler obj) {
    _subscribers.add(obj);
  }

  public int previousX() {
    return _previousX;
  }

  public int previousY() {
    return _previousY;
  }

  private void updatePreviousMousePoint() {
    _previousX = mouseX;
    _previousY = mouseY;
  }

  //Don't call these outside of this file
  public void _mouseWasMoved() {
    updatePreviousMousePoint();
    for (Object e : _subscribers) {
      ((MouseEventHandler)e).mouseEvent_moved();
    }
  }

  public void _mouseWasClicked() {
    updatePreviousMousePoint();
    for (Object e : _subscribers) {
      ((MouseEventHandler)e).mouseEvent_clicked();
    }
  }

  public void _mouseWasDragged() {
    updatePreviousMousePoint();
    for (Object e : _subscribers) {
      ((MouseEventHandler)e).mouseEvent_dragged();
    }
  }

  public void _mouseWasPressed() {
    updatePreviousMousePoint();
    for (Object e : _subscribers) {
      ((MouseEventHandler)e).mouseEvent_pressed();
    }
  }

  public void _mouseWasReleased() {
    updatePreviousMousePoint();
    for (Object e : _subscribers) {
      ((MouseEventHandler)e).mouseEvent_released();
    }
  }
}

MouseEventDispatcher mouseEvents = new MouseEventDispatcher();

void mouseClicked() {
  mouseEvents._mouseWasClicked();
}

void mouseDragged() {
  mouseEvents._mouseWasDragged();
}

void mouseMoved() {
  mouseEvents._mouseWasMoved();
}

void mousePressed() {
  mouseEvents._mouseWasPressed();
}

void mouseReleased() {
  mouseEvents._mouseWasReleased();
}


public abstract class RelativeSizeComponent {
  protected RelativeSizeComponent _parent;
  protected float _x, _y, _width, _height;
  protected color _stroke, _fill;
  protected boolean _visible;
  protected String _tooltip;
  
  public RelativeSizeComponent(float x, float y, float width, float height) {
    this(null, x, y, width, height);
  }
  
  public RelativeSizeComponent(RelativeSizeComponent parent, float x, float y, float width, float height) {
    _parent = parent;
    _x = x;
    _y = y;
    _width = width;
    _height = height;
    _visible = true;
  }
  
  public float getLeft() {
    if (_parent != null) {
      return _parent.getLeft() + _parent.getWidth() * _x;
    } else {
      return width * _x;
    }
  }
  
  public float getTop() {
    if (_parent != null) {
      return _parent.getTop() + _parent.getHeight() * _y;
    } else {
      return height * _y;
    }
  }
  
  public float getRight() {
    if (_parent != null) {
      return _parent.getLeft() + _parent.getWidth() * (_x + _width);
    } else {
      return width * (_x + _width);
    }
  }
  
  public float getBottom() {
    if (_parent != null) {
      return _parent.getTop() + _parent.getHeight() * (_y + _height);
    } else {
      return height * (_y + _height);
    }
  }
  
  public float getWidth() {
    if (_parent != null) {
      return _parent.getWidth() * _width;
    } else {
      return width * _width;
    }
  }
  
  public float getHeight() {
    if (_parent != null) {
      return _parent.getHeight() * _height;
    } else {
      return height * _height;
    }
  }
  
  public void setStroke(color stroke) {
    _stroke = stroke;
  }
  
  public color getStroke() {
    return _stroke;
  }
  
  public void setFill(color fill) {
    _fill = fill;
  }
  
  public color getFill() {
    return _fill;
  }
  
  public void setVisible(boolean visible) {
    _visible = visible;
  }
  
  public boolean isVisible() {
    return _visible;
  }
  
  public void setTooltip(String tooltip) {
    _tooltip = tooltip;
  }
  
  public boolean containsPoint(float x, float y) {
    return x >= getLeft() && x <= getRight() && y >= getTop() && y <= getBottom();
  }
  
  public void render() {
    if (_visible) {
      stroke(_stroke);
      fill(_fill);
      rect(getLeft(), getTop(), getWidth(), getHeight());
    }
  }
  
  public void drawTooltip() {
    if (_visible && _tooltip != null && containsPoint(mouseX, mouseY)) {
      stroke(color(0, 0, 0));
      fill(color(255, 255, 202));
      textSize(12);
      rect(mouseX, mouseY - 18, textWidth(_tooltip) + 4, 18);
      fill(color(0, 0, 0));
      textAlign(LEFT, BOTTOM);
      text(_tooltip, mouseX + 2, mouseY - 2);
    }
  }
}

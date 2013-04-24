package edu.tufts.cs.gv.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JSlider;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.tufts.cs.gv.controller.VizEventType;
import edu.tufts.cs.gv.controller.VizState;
import edu.tufts.cs.gv.model.Dataset;
import edu.tufts.cs.gv.model.graph.Edge;
import edu.tufts.cs.gv.model.graph.Graph;
import edu.tufts.cs.gv.model.graph.Vertex;
import edu.tufts.cs.gv.util.Colors;
import edu.tufts.cs.gv.util.DrawingHelp;
import edu.tufts.cs.gv.util.Vector;

public class GraphView extends VizView implements MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener {
	private static final long serialVersionUID = 1L;

	private static double Kc = 50;
	private static double Ks = .05;
	private static double Kg = 1;
	private static double SPRING_LENGTH = 7.5;
	private static final double radius = 10;
	private static final double diameter = radius * 2;
	private static final List<String> helpString = Arrays.asList("A graph of tests. Each test is connected to other tests that have",
																 "a strict subset of passing students. Hover over a node to get the",
																 "name of the test and see the passing and failing students on the",
																 "left. You can zoom the view with the mouse wheel and pan by",
																 "dragging.");
	
	private static final double zoom_rate = .9;
	
	private Dataset dataset;
	private Graph graph;
	
	private Vertex moving;
	private Point lastPoint;
	
	private Vector offset;
	private double scale;
	
	private Vector lastSize;
	
	public GraphView() {
		VizState.getState().addVizUpdateListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addComponentListener(this);
		dataset = null;
		graph = null;
		lastPoint = null;
		offset = new Vector();
		lastSize = new Vector(this.getWidth(), this.getHeight());
		this.setToolTipText("");
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		ToolTipManager.sharedInstance().setInitialDelay(100);
	}
	
	@Override
	public void vizUpdated(VizEventType eventType) {
		VizState state = VizState.getState();
		if (state.getDataset() != dataset) {
			dataset = state.getDataset();
			graph = new Graph(dataset);
			for (Vertex v : graph.getVertices()) {
				if (v.isRoot()) {
					v.setX(diameter);
					v.setY(getHeight() / 2);
				} else {
					v.setX(Math.random() * getWidth() * .5 + .25 * getWidth());
					v.setY(Math.random() * getWidth() * .5 + .25 * getHeight());
				}
			}
			moving = null;
			offset = new Vector(0, 0);
			scale = 1;
		} else if (eventType == VizEventType.HOVERING_STUDENT) {
			if (dataset != null && graph != null) {
				String student = VizState.getState().getMousedOverStudent();
				if (student != null) {
					for (Vertex v : graph.getVertices()) {
						v.setSelected(false);
						if (v.getTestNames().size() > 0) {
							String test = v.getTestNames().iterator().next();
							if (dataset.getPassersOfTest(test).contains(student)) {
								v.setSelected(true);
							}
						}
					}
				} else {
					for (Vertex v : graph.getVertices()) {
						v.setSelected(false);
					}
				}
			}
		}
	}

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Colors.canvasBackground);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (graph != null) {
			g.setColor(Colors.foreground);
			for (Edge e : graph.getEdges()) {
				Vector a = worldToScreen(e.getA().getX(), e.getA().getY());
				Vector b = worldToScreen(e.getB().getX(), e.getB().getY());
				g.drawLine((int)a.x, (int)a.y, (int)b.x, (int)b.y);
			}
			for (Vertex v : graph.getVertices()) {
				if (VizState.getState().getMousedOverStudent() == null) {
					g.setColor(Colors.hover);
				} else {
					if (v.isSelected()) {
						g.setColor(Colors.pass);
					} else {
						g.setColor(Colors.fail);
					}
				}
				Vector center = worldToScreen(v.getX(), v.getY());
				double radius = calcRadius(v.getTestNames().size());
				double diameter = 2 * radius;
				g.fillOval((int)(center.x - radius * scale), (int)(center.y - radius * scale), (int)(diameter * scale), (int)(diameter * scale));
				g.setColor(Colors.foreground);
				g.drawOval((int)(center.x - radius * scale), (int)(center.y - radius * scale), (int)(diameter * scale), (int)(diameter * scale));
			}
		}
		if (VizState.getState().isShowingHelp()) {
			DrawingHelp.renderHelpText(this, helpString, g);
		}
	}
	
	public int calcRadius(int numTests) {
		return Math.max((int)(Math.sqrt(numTests) * radius), 5);
	}
	
	public Vector worldToScreen(Point world) {
		return new Vector(world.x * scale + offset.x, world.y * scale + offset.y);
	}
	
	public Vector worldToScreen(double x, double y) {
		return new Vector(x * scale + offset.x, y * scale + offset.y);
	}
	
	public Vector screenToWorld(Point screen) {
		return new Vector((screen.x - offset.x) / scale, (screen.y - offset.y) / scale); 
	}
	
	public Vector screenToWorld(double x, double y) {
		return new Vector((x - offset.x) / scale, (y - offset.y) / scale); 
	}

	@Override
	public void update() {
		if (graph != null) {
			for (Vertex a : graph.getVertices()) {
				for (Vertex b : graph.getVertices()) {
					if (a == b) { continue; }
					Vector repel = new Vector(b.getX() - a.getX(), b.getY() - a.getY());
					repel.normalize();
					double mag = - (Kc * a.getTestNames().size() * b.getTestNames().size()) / (a.getDistance2(b));
					if (a.getDistance2(b) < .0001) {
						mag = 0;
					}
					repel.scale(mag);
					a.applyForce(repel.getX(), repel.getY());
				}
				a.applyForce(Kg, 0);
			}
			for (Edge e : graph.getEdges()) {
				Vertex a = e.getA();
				Vertex b = e.getB();
				Vector attract = new Vector(a.getX() - b.getX(), a.getY() - b.getY());
				attract.normalize();
				double mag = - Ks * (a.getDistance(b) - (e.getStudentDiff() / (float)dataset.getAllStudents().size())
						* getWidth() * SPRING_LENGTH);
				attract.scale(mag);
				a.applyForce(attract.getX(), attract.getY());
				b.applyForce(-attract.getX(), -attract.getY());
			}
			for (Vertex v : graph.getVertices()) {
				v.move();
			}
		}
	}
	
	public String getToolTipText(MouseEvent e) {
		if (graph != null) {
			Vector point = screenToWorld(e.getPoint());
			for (Vertex v : graph.getVertices()) {
				if (v.getDistance(point.x, point.y) <= calcRadius(v.getTestNames().size())) {
					String tooltip = "<html>";
					if (v.getTestNames().size() == 0) {
						tooltip += "No Tests";
					} else {
						for (String test : v.getTestNames()) {
							tooltip += test + "<br>";
						}
					}
					tooltip += "</html>";
					return tooltip;
				}
			}
			return null;
		}
		return null;
	}
	
	public ChangeListener getSpringListener() {
		return new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Ks = ((JSlider)e.getSource()).getValue() / 10000.0;
			}
		};
	}
	
	public ChangeListener getSpringLengthListener() {
		return new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SPRING_LENGTH = ((JSlider)e.getSource()).getValue() / 100.0;
			}
		};
	}
	
	public ChangeListener getRepulstionListener() {
		return new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Kc = ((JSlider)e.getSource()).getValue() / 10.0;
			}
		};
	}
	
	public ChangeListener getGravityListener() {
		return new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Kg = ((JSlider)e.getSource()).getValue() / 1000.0;
			}
		};
	}
	
	public void componentResized(ComponentEvent e) {
		offset.x += (getWidth() - lastSize.x) / 2;
		offset.y += (getHeight() - lastSize.y) / 2;
		lastSize = new Vector(getWidth(), getHeight());
	}
	
	public void mouseDragged(MouseEvent e) {
		if (moving != null) {
			double dx = e.getX() - lastPoint.x;
			double dy = e.getY() - lastPoint.y;
			moving.moveDelta(dx / scale, dy / scale);
			lastPoint = e.getPoint();
		} else {
			double dx = e.getX() - lastPoint.x;
			double dy = e.getY() - lastPoint.y;
			offset.x += dx;
			offset.y += dy;
			lastPoint = e.getPoint();
		}
	}

	public void mousePressed(MouseEvent e) {
		if (graph == null) { return; }
		Vector p = screenToWorld(e.getPoint());
		for (Vertex v : graph.getVertices()) {
			if (v.getDistance(p.x, p.y) <= calcRadius(v.getTestNames().size())) {
				moving = v;
			}
		}
		if (moving != null) {
			lastPoint = e.getPoint();
			moving.setOverriding(true);
		} else {
			lastPoint = e.getPoint();
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (moving != null) {
			moving.setOverriding(false);
			moving = null;
			lastPoint = null;
		} else {
			lastPoint = null;
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (graph == null) { return; }
		Vector p = screenToWorld(e.getPoint());
		Set<String> selectedTests = new HashSet<>();
		boolean none = true;
		for (Vertex v : graph.getVertices()) {
			if (v.getDistance(p.x, p.y) <= calcRadius(v.getTestNames().size())) {
				selectedTests.addAll(v.getTestNames());
				none = false;
			}
		}
		if (none) {
			VizState.getState().setMousedOverTests(null);
		} else {
			VizState.getState().setMousedOverTests(selectedTests);
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		Vector centerWorld = screenToWorld(e.getPoint());
		for (int i = 0; i < Math.abs(e.getWheelRotation()); i++) {
			if (e.getWheelRotation() < 0) {
				scale /= zoom_rate;
			} else {
				scale *= zoom_rate;
			}
		}
		Vector newCenterWorld = screenToWorld(e.getPoint());
		offset.x += (newCenterWorld.x - centerWorld.x) * scale;
		offset.y += (newCenterWorld.y - centerWorld.y) * scale;
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
}

package edu.tufts.cs.gv.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

import edu.tufts.cs.gv.controller.VizEventType;
import edu.tufts.cs.gv.controller.VizState;
import edu.tufts.cs.gv.model.Dataset;
import edu.tufts.cs.gv.model.graph.Edge;
import edu.tufts.cs.gv.model.graph.Graph;
import edu.tufts.cs.gv.model.graph.Vertex;
import edu.tufts.cs.gv.util.Vector;

public class GraphView extends VizView implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	private static final double Kc = 200;
	private static final double Ks = .05;
	private static final double Kg = .001;
	private static final double ENERGY_LIMIT = .5;
	private static final double radius = 10;
	private static final double diameter = radius * 2;
	
	private Dataset dataset;
	private Graph graph;
	private boolean simulating;
	private Vertex moving;
	private Point lastPoint;
	
	public GraphView() {
		VizState.getState().addVizUpdateListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		dataset = null;
		graph = null;
		simulating = false;
		lastPoint = null;
	}
	
	@Override
	public void vizUpdated(VizEventType eventType) {
		VizState state = VizState.getState();
		if (state.getDataset() != dataset) {
			dataset = state.getDataset();
			graph = new Graph(dataset);
			for (Vertex v : graph.getVertices()) {
				if (v.isRoot()) {
					v.setX(getWidth() / 2);
					v.setY(diameter);
				} else {
					v.setX(Math.random() * getWidth() * .5 + .25 * getWidth());
					v.setY(Math.random() * getWidth() * .5 + .25 * getHeight());
				}
			}
			simulating = true;
		}
		if (eventType == VizEventType.HOVERING_TESTS) {
			System.out.println(VizState.getState().getMousedOverTests().toString());
		}
	}

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (graph != null) {
			g.setColor(Color.BLACK);
			for (Edge e : graph.getEdges()) {
				g.drawLine((int)e.getA().getX(), (int)e.getA().getY(), (int)e.getB().getX(), (int)e.getB().getY());
			}
			for (Vertex v : graph.getVertices()) {
				if (v.isSelected()) {
					g.setColor(Color.CYAN);
				} else {
					g.setColor(Color.RED);
				}
				g.fillOval((int)(v.getX() - radius), (int)(v.getY() - radius), (int)diameter, (int)diameter);
				g.setColor(Color.BLACK);
				g.drawOval((int)(v.getX() - radius), (int)(v.getY() - radius), (int)diameter, (int)diameter);
			}
		}
	}

	@Override
	public void update() {
		if (graph != null && simulating) {
			for (Vertex a : graph.getVertices()) {
				for (Vertex b : graph.getVertices()) {
					if (a == b) { continue; }
					Vector repel = new Vector(b.getX() - a.getX(), b.getY() - a.getY());
					repel.normalize();
					double mag = - Kc / (a.getDistance2(b));
					if (a.getDistance2(b) < .0001) {
						mag = 0;
					}
					repel.scale(mag);
					a.applyForce(repel.getX(), repel.getY());
				}
				a.applyForce(0, Kg);
			}
			for (Edge e : graph.getEdges()) {
				Vertex a = e.getA();
				Vertex b = e.getB();
				Vector attract = new Vector(a.getX() - b.getX(), a.getY() - b.getY());
				attract.normalize();
				double mag = - Ks * (a.getDistance(b) - (e.getStudentDiff() / (float)dataset.getAllStudents().size()) * getHeight() * 3);
				attract.scale(mag);
				a.applyForce(attract.getX(), attract.getY());
				b.applyForce(-attract.getX(), -attract.getY());
			}
			double totalE = 0;
			for (Vertex v : graph.getVertices()) {
				totalE += v.getVelocity2();
				v.move();
				if (v.getX() < radius) {
					v.setX(radius);
				} else if (v.getX() > getWidth() - radius) {
					v.setX(getWidth() - radius);
				}
				if (v.getY() < radius) {
					v.setY(radius);
				} else if (v.getY() > getHeight() - radius) {
					v.setY(getHeight() - radius);
				}
			}
			if (totalE < ENERGY_LIMIT * graph.getVertices().size()) {
				simulating = false;
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (moving == null) { return; }
		double dx = e.getX() - lastPoint.x;
		double dy = e.getY() - lastPoint.y;
		moving.moveDelta(dx, dy);
		simulating = true;
		lastPoint = e.getPoint();
	}

	public void mousePressed(MouseEvent e) {
		if (graph == null) { return; }
		Point p = e.getPoint();
		for (Vertex v : graph.getVertices()) {
			if (v.getDistance(p.x, p.y) <= radius) {
				moving = v;
			}
		}
		if (moving != null) {
			lastPoint = p;
			simulating = true;
			moving.setOverriding(true);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (moving != null) {
			moving.setOverriding(false);
			moving = null;
			lastPoint = null;
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if (graph == null) { return; }
		Point p = e.getPoint();
		boolean clickedSomething = false;
		for (Vertex v : graph.getVertices()) {
			if (v.getDistance(p.x, p.y) <= radius) {
				v.setSelected(!v.isSelected());
				clickedSomething = true;
			}
		}
		if (!clickedSomething) {
			for (Vertex v : graph.getVertices()) {
				v.setSelected(false);
			}
		}
		Set<String> selectedTests = new HashSet<>();
		for (Vertex v : graph.getVertices()) {
			if (v.isSelected()) {
				selectedTests.addAll(v.getTestNames());
			}
		}
		VizState.getState().setMousedOverTests(selectedTests);
	}

	public void mouseMoved(MouseEvent e) {
		if (graph == null) { return; }
		Point p = e.getPoint();
		Set<String> selectedTests = new HashSet<>();
		for (Vertex v : graph.getVertices()) {
			if (v.getDistance(p.x, p.y) <= radius) {
				selectedTests.addAll(v.getTestNames());
			}
		}
		for (Vertex v : graph.getVertices()) {
			if (v.isSelected()) {
				selectedTests.addAll(v.getTestNames());
			}
		}
		VizState.getState().setMousedOverTests(selectedTests);
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}

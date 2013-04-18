package edu.tufts.cs.gv.view;

import java.awt.Color;
import java.awt.Graphics;

import edu.tufts.cs.gv.controller.VizState;
import edu.tufts.cs.gv.model.Dataset;
import edu.tufts.cs.gv.model.graph.Edge;
import edu.tufts.cs.gv.model.graph.Graph;
import edu.tufts.cs.gv.model.graph.Vertex;
import edu.tufts.cs.gv.util.Vector;

public class GraphView extends VizView {
	private static final long serialVersionUID = 1L;

	private static final double Kc = 100000;
	private static final double Ks = .01;
	private static final double radius = 10;
	private static final double diameter = radius * 2;
	
	private Dataset dataset;
	private Graph graph;
	
	public GraphView() {
		VizState.getState().addVizUpdateListener(this);
		dataset = null;
		graph = null;
	}
	
	@Override
	public void vizUpdated() {
		// TODO Auto-generated method stub
		VizState state = VizState.getState();
		if (state.getDataset() != dataset) {
			dataset = state.getDataset();
			graph = new Graph(dataset);
			for (Vertex v : graph.getVertices()) {
				v.setX(Math.random() * getWidth() * .5 + .25 * getWidth());
				v.setY(Math.random() * getWidth() * .5 + .25 * getHeight());
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (graph != null) {
			g.setColor(Color.BLACK);
			for (Edge e : graph.getEdges()) {
				g.drawLine((int)e.getA().getX(), (int)e.getA().getY(), (int)e.getB().getX(), (int)e.getB().getY());
			}
			for (Vertex v : graph.getVertices()) {
				g.setColor(Color.RED);
				g.fillOval((int)(v.getX() - radius), (int)(v.getY() - radius), (int)diameter, (int)diameter);
				g.setColor(Color.BLACK);
				g.drawOval((int)(v.getX() - radius), (int)(v.getY() - radius), (int)diameter, (int)diameter);
			}
		}
	}

	@Override
	public void update() {
		if (graph != null) {
			for (Vertex a : graph.getVertices()) {
				for (Vertex b : graph.getVertices()) {
					if (a == b) { continue; }
					Vector repel = new Vector(b.getX() - a.getX(), b.getY() - a.getY());
					repel.normalize();
					double mag = - Kc / (a.getDistance2(b) * a.getDistance(b));
					repel.scale(mag);
					a.applyForce(repel.getX(), repel.getY());
				}
			}
			for (Edge e : graph.getEdges()) {
				Vertex a = e.getA();
				Vertex b = e.getB();
				Vector attract = new Vector(a.getX() - b.getX(), a.getY() - b.getY());
				attract.normalize();
				double mag = - Ks * (a.getDistance(b) - e.getStudentDiff() * 100);
				attract.scale(mag);
				a.applyForce(attract.getX(), attract.getY());
				b.applyForce(-attract.getX(), -attract.getY());
			}
			for (Vertex v : graph.getVertices()) {
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
		}
	}

}

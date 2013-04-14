package edu.tufts.cs.gv.model.graph;

public class Edge {
	private int studentDiff;
	private Vertex a, b;
	
	public Edge(Vertex a, Vertex b, int studentDiff) {
		this.a = a;
		this.b = b;
		this.studentDiff = studentDiff;
	}
	
	public boolean equals(Object other) {
		if (other instanceof Edge) {
			Edge e = (Edge)other;
			return studentDiff == e.studentDiff &&
					(a == e.a && b == e.b) ||
					(a == e.b && b == e.a);
		} else {
			return false;
		}
	}
}

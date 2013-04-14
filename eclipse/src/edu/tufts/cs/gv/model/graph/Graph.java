package edu.tufts.cs.gv.model.graph;

import java.util.HashSet;
import java.util.Set;

import edu.tufts.cs.gv.model.Dataset;

public class Graph {
	private Set<Vertex> vertices;
	private Set<Edge> edges;
	
	public Graph() {
		vertices = new HashSet<>();
		edges = new HashSet<>();
	}
	
	public Graph(Dataset dataset) {
		this(dataset, 1);
	}
	
	public Graph(Dataset dataset, int cutLength) {
		this();
		// add algorithm for doing superset tree
	}
	
	public void addVertex(Vertex v) {
		vertices.add(v);
	}
	
	public void addEdge(Edge e, Vertex a, Vertex b) {
		vertices.add(a);
		vertices.add(b);
		edges.add(e);
	}
}
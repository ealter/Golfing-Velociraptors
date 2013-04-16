package edu.tufts.cs.gv.model.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
		this();
		List<String> tests = new ArrayList<>(dataset.getAllTests());
		Map<String, Set<String>> passersForTests = dataset.getTestToPassersMap();
		Map<String, Vertex> testToVertex = new HashMap<>();
		// Add vertices
		for (String test : tests) {
			Vertex a = new Vertex(test);
			testToVertex.put(test, a);
			addVertex(a);
			
		}
		// Add edges
		for (String a : tests) {
			for (String b : tests) {
				if (a == b) { continue; }
				// b is a subset of a
				if (passersForTests.get(a).containsAll(passersForTests.get(b))) {
					addEdge(new Edge(testToVertex.get(a), testToVertex.get(b),
									 Math.abs(passersForTests.get(a).size() - passersForTests.get(b).size())),
							testToVertex.get(a), testToVertex.get(b));
				}
			}
		}
	}
	
	public Graph(Dataset dataset, int cutLength) {
		this();
		List<String> tests = new ArrayList<>(dataset.getAllTests());
		Map<String, Set<String>> passersForTests = dataset.getTestToPassersMap();
		// Build uncut graph
		Graph uncut = new Graph(dataset);
		// Cut graph
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

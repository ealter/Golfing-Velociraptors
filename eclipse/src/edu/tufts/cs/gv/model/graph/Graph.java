package edu.tufts.cs.gv.model.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.tufts.cs.gv.model.Dataset;

public class Graph {
	private Map<Vertex, List<Edge>> adjacency;
	
	public Graph() {
		adjacency = new HashMap<>();
	}
	
	public Graph(Dataset dataset) {
		this();
		List<String> tests = new ArrayList<>(dataset.getAllTestNames());
		Map<String, Set<String>> passersForTests = dataset.getTestToPassersMap();
		
		Map<Set<String>, Vertex> equivalentTests = new HashMap<>();
		Vertex root = new Vertex(new ArrayList<String>());
		root.setRoot(true);
		equivalentTests.put(dataset.getAllStudents(), root);
		
		for (String test : tests) {
			boolean equivalent = false;
			for (Set<String> students : equivalentTests.keySet()) {
				if (students.equals(passersForTests.get(test))) {
					equivalent = true;
					equivalentTests.get(passersForTests.get(test)).addTestName(test);
				}
			}
			if (!equivalent) {
				equivalentTests.put(passersForTests.get(test), new Vertex(test));
			}
		}
		
		for (Set<String> studentsA : equivalentTests.keySet()) {
			for (Set<String> studentsB : equivalentTests.keySet()) {
				if (studentsA == studentsB) { continue; }
				if (studentsA.containsAll(studentsB)) {
					addEdge(new Edge(equivalentTests.get(studentsA), equivalentTests.get(studentsB),
							Math.abs(studentsA.size() - studentsB.size())));
				}
			}
		}
	}
	
	public Graph(Dataset dataset, int cutLength) {
		// Build uncut graph
		//this(dataset);
		this();
		Vertex z = new Vertex("a");
		Vertex y = new Vertex("b");
		Vertex c = new Vertex("c");
		Vertex d = new Vertex("d");
		Vertex x = new Vertex("e");
		Vertex f = new Vertex("f");
		addEdge(new Edge(z, y, 1));
		addEdge(new Edge(z, c, 1));
		addEdge(new Edge(c, d, 1));
		addEdge(new Edge(d, x, 1));
		addEdge(new Edge(d, f, 1));
		for (Vertex v : adjacency.keySet()) {
			System.out.println(v);
			for (Edge e : adjacency.get(v)) {
				System.out.println(e);
			}
		}
		// Cut graph
		boolean collapsed = false;
		do {
			collapsed = false;
			Edge e = null;
			for (List<Edge> edges : adjacency.values()) {
				for (Edge edge : edges) {
					if (edge.getStudentDiff() <= cutLength) {
						e = edge;
						collapsed = true;
						break;
					}
				}
				if (collapsed) { break; }
			}
			if (collapsed) {
				Vertex a = e.getA(), b = e.getB();
				adjacency.get(a).remove(e);
				adjacency.get(b).remove(e);
				Collection<String> names = a.getTestNames();
				names.addAll(b.getTestNames());
				Vertex ab = new Vertex(names);
				List<Edge> edges = new ArrayList<>();
				for (Edge ea : adjacency.get(a)) {
					if (ea.getA() == a) {
						ea.setA(ab);
					} else if (ea.getB() == a) {
						ea.setB(ab);
					}
					ea.setStudentDiff(ea.getStudentDiff() + e.getStudentDiff());
					edges.add(ea);
				}
				for (Edge eb : adjacency.get(b)) {
					if (eb.getA() == b) {
						eb.setA(ab);
					} else if (eb.getB() == b) {
						eb.setB(ab);
					}
					eb.setStudentDiff(eb.getStudentDiff() + e.getStudentDiff());
					edges.add(eb);
				}
				adjacency.put(ab, edges);
				adjacency.remove(a);
				adjacency.remove(b);
			}
		} while (collapsed);
		System.out.println();
		for (Vertex v : adjacency.keySet()) {
			System.out.println(v);
			for (Edge e : adjacency.get(v)) {
				System.out.println(e);
			}
		}
	}
	
	public void addVertex(Vertex v) {
		if (!adjacency.containsKey(v)) {
			adjacency.put(v, new ArrayList<Edge>());
		}
	}
	
	public void addEdge(Edge e) {
		addVertex(e.getA());
		addVertex(e.getB());
		adjacency.get(e.getA()).add(e);
		adjacency.get(e.getB()).add(e);
	}
	
	public Collection<Vertex> getVertices() {
		return adjacency.keySet();
	}
	
	public Collection<Edge> getEdges() {
		Set<Edge> edges = new HashSet<>();
		for (List<Edge> es : adjacency.values()) {
			edges.addAll(es);
		}
		return edges;
	}
}

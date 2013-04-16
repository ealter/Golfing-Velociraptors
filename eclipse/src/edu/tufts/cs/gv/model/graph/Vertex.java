package edu.tufts.cs.gv.model.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Vertex {
	private Set<String> testNames;
	
	public Vertex(String testName) {
		testNames = new HashSet<>();
		testNames.add(testName);
	}
	
	public Vertex(Collection<String> testNames) {
		this.testNames = new HashSet<>(testNames);
	}
	
	public Set<String> getTestNames() {
		return testNames;
	}
}

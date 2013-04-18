package edu.tufts.cs.gv.model.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Vertex {
	private Set<String> testNames;
	
	private double x, y;
	private double vx, vy;
	
	public Vertex(String testName) {
		testNames = new HashSet<>();
		testNames.add(testName);
		x = 10;
		y = 10;
	}
	
	public Vertex(Collection<String> testNames) {
		this.testNames = new HashSet<>(testNames);
		x = 10;
		y = 10;
	}
	
	public Set<String> getTestNames() {
		return testNames;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getVx() {
		return vx;
	}

	public double getVy() {
		return vy;
	}

	public void moveDelta(double dx, double dy) {
		x += dx;
		y += dy;
	}
	
	public void applyForce(double fx, double fy) {
		vx += fx;
		vy += fy;
	}
	
	public void move() {
		x += vx;
		y += vy;
		vx = 0;
		vy = 0;
	}
	
	public double getDistance(Vertex v) {
		return Math.sqrt(getDistance2(v));
	}
	
	public double getDistance2(Vertex v) {
		return (v.getX() - x) * (v.getX() - x) + (v.getY() - y) * (v.getY() - y);
	}
	
	public double getVelocity() {
		return Math.sqrt(getVelocity2());
	}
	
	public double getVelocity2() {
		return vx * vx + vy * vy;
	}
	
	public String toString() {
		return testNames.toString();
	}
}

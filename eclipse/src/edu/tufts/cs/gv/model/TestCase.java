package edu.tufts.cs.gv.model;

public class TestCase {
	private String name;
	private int number;
	private String student;
	private boolean passed; //True if the test is a pass
	private String witness;
	
	public TestCase(String name, int number, String student, boolean outcome, String witness) {
		this.name = name;
		this.number = number;
		this.student = student;
		this.passed = outcome;
		this.witness = witness;
	}

	public String getName() {
		return name;
	}

	public int getNumber() {
		return number;
	}

	public String getStudent() {
		return student;
	}

	public boolean didPass() {
		return passed;
	}

	public String getWitness() {
		return witness;
	}
	
	public boolean equals(Object other) {
		if (other instanceof TestCase) {
			TestCase b = (TestCase)other;
			return name.equals(b.name) &&
					number == b.number &&
					student.equals(b.student) &&
					passed == b.passed &&
					witness.equals(b.witness);
		} else {
			return false;
		}
	}
	
	public String toString() {
		String str = "given " + name + " test " + number + ", " + student + " ";
		if (passed) {
			str += "passed";
		} else {
			str += "failed";
		}
		if (witness != null) {
			str += witness;
		}
		return str;
	}
}

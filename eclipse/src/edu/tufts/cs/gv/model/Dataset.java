package edu.tufts.cs.gv.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dataset {
	public static Dataset parseFromFile(String filename) {
		Dataset dataset = new Dataset();
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(filename));
			Pattern pattern = Pattern.compile("given\\s+(\\S+)\\s+test\\s+(\\d+),\\s+(\\w+)\\s+(\\w+)(.*)");
			String line;
			while ((line = in.readLine()) != null) {
				Matcher m = pattern.matcher(line);
				if(m.find() && m.groupCount() == 5) {
					String name = m.group(1);
					int number = Integer.parseInt(m.group(2));
					String student = m.group(3);
					String outcome = m.group(4);
					String witness = m.group(5);
					if(witness.length() == 0) {
						witness = null;
					}
					Boolean didPass;
					if(outcome.equals("passed")) {
						didPass = true;
					} else if(outcome.equals("failed")) {
						didPass = false;
					} else {
						continue;
					}
					dataset.addTestCase(new TestCase(name, number, student, didPass, witness));
				}
			}
			in.close();
			return dataset;
		} catch (IOException e) {
			System.out.println("problem parsing annon file " + filename);
			return null;
		}
	}
	
	private Set<TestCase> testCases;
	private Map<String, Set<TestCase>> testToTestCases;
	private Map<String, Set<TestCase>> studentToTestCases;
	
	public Dataset() {
		testCases = new HashSet<>();
		testToTestCases = new HashMap<>();
		studentToTestCases = new HashMap<>();
	}
	
	public void addTestCase(TestCase testCase) {
		// Add testCase to set of all TestCases
		testCases.add(testCase);
		
		// Add testCase to set for all TestCases of test [Name]
		if (!testToTestCases.containsKey(testCase.getName())) {
			testToTestCases.put(testCase.getName(), new HashSet<TestCase>());
		}
		Set<TestCase> testSet = testToTestCases.get(testCase.getName());
		testSet.add(testCase);
		
		// Add testCase to set for all TestCases 
		if (!studentToTestCases.containsKey(testCase.getName())) {
			studentToTestCases.put(testCase.getName(), new HashSet<TestCase>());
		}
		Set<TestCase> studentSet = studentToTestCases.get(testCase.getName());
		studentSet.add(testCase);
	}
	
	public Set<TestCase> getAllTestCases() {
		return testCases;
	}
	
	public Set<TestCase> getTestCasesForTest(String test) {
		return testToTestCases.get(test);
	}
	
	public Set<TestCase> getTestCasesForStudent(String student) {
		return studentToTestCases.get(student);
	}
	
	public String toString() {
		String str = "";
		for (TestCase testCase : testCases) {
			str += testCase + "\n";
		}
		return str;
	}
}

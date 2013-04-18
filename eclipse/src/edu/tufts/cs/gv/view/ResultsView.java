package edu.tufts.cs.gv.view;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.tufts.cs.gv.controller.VizState;
import edu.tufts.cs.gv.model.Dataset;
import edu.tufts.cs.gv.model.TestCase;

//This class will be a bar chart of the witnesses.

public class ResultsView extends VizView {
	private static final long serialVersionUID = 1L;
	
	private static final int maxBars = 5; //The maximum number of bars for a particular test case
	private static final float barSpacing = 2; //Number of pixels between bars of a test case
	private static final float testCaseSpacing = 8; //Spacing between test cases
	
	private Dataset dataset;
	private ArrayList<HashMap<String, Integer>> witnesses;
	private ArrayList<String> testcases;

	public ResultsView() {
		VizState.getState().addVizUpdateListener(this);
	}
	
	@Override
	public void vizUpdated() {
		// TODO Auto-generated method stub
		Dataset currentDataSet = VizState.getState().getDataset();
		if(dataset != currentDataSet) {
			dataset = currentDataSet;
			Set<String> testNames = dataset.getAllTestNames();
			witnesses = new ArrayList<>(testNames.size());
			testcases = new ArrayList<>(testNames.size());
			for(String testName : testNames) {
				Set<TestCase> testCases = dataset.getTestCasesForTest(testName);
				testcases.add(testName);
				HashMap<String, Integer> witnessMap = new HashMap<>();
				witnesses.add(witnessMap);
				for(TestCase t : testCases) {
					String witness = t.getWitness();
					int count = 1;
					if(witnessMap.containsKey(witness)) {
						count += witnessMap.get(witness);
					}
					witnessMap.put(witness, count);
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		//System.out.println("Results render");
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		//System.out.println("Results update");
	}
}

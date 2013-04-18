package edu.tufts.cs.gv.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import edu.tufts.cs.gv.controller.VizEventType;
import edu.tufts.cs.gv.controller.VizState;
import edu.tufts.cs.gv.model.Dataset;
import edu.tufts.cs.gv.model.TestCase;

//This class will be a bar chart of the witnesses.

public class ResultsView extends VizView {
	private static final long serialVersionUID = 1L;
	
	private static final int maxBars = 5; //The maximum number of bars for a particular test case
	private static final float barSpacing = 2; //Number of pixels between bars of a test case
	private static final int barWidth   = 7;
	private static final float testCaseSpacing = 20; //Spacing between test cases
	
	private ArrayList<HashMap<String, Integer>> witnesses;
	private ArrayList<String> testcases;
	private int maxBarChartHeight;

	public ResultsView() {
		VizState.getState().addVizUpdateListener(this);
		maxBarChartHeight = 0;
	}
	
	@Override
	public void vizUpdated(VizEventType eventType) {
		if(eventType == VizEventType.NEW_DATA_SOURCE) {
			int screenWidth = 0;
			Dataset dataset = VizState.getState().getDataset();
			Set<String> testNames = dataset.getAllTestNames();
			witnesses = new ArrayList<>(testNames.size());
			testcases = new ArrayList<>(testNames.size());
			for(String testName : testNames) {
				Set<TestCase> testCases = dataset.getTestCasesForTest(testName);
				testcases.add(testName);
				HashMap<String, Integer> witnessMap = new HashMap<>();
				witnesses.add(witnessMap);
				int numUniqueWitnesses = 0;
				for(TestCase t : testCases) {
					String witness = t.getWitness();
					int count = 1;
					if(witnessMap.containsKey(witness)) {
						count += witnessMap.get(witness);
					} else {
						numUniqueWitnesses++;
						if(numUniqueWitnesses <= maxBars) {
							screenWidth += barSpacing + barWidth;
						}
					}
					maxBarChartHeight = Math.max(maxBarChartHeight, count);
					witnessMap.put(witness, count);
				}
				//TODO: if there are more than 5, limit to 5
				screenWidth += testCaseSpacing;
			}
			this.setPreferredSize(new Dimension(screenWidth, this.getHeight()));
		}
	}

	public void paint(Graphics g) {
		if(witnesses == null)
			return;
		int height = this.getHeight();
		float heightFactor = height/(float)maxBarChartHeight;
		float x = 0;
		int y = height - 1;
		Color[] colors = {Color.BLUE, Color.GREEN, Color.ORANGE};
		int colorIndex = 0;
		for(HashMap<String, Integer> testcase : witnesses) {
			colorIndex = 0;
			for(Integer count : testcase.values()) {
				g.setColor(colors[colorIndex]);
				int barHeight = (int)(count * heightFactor);
				g.fillRect((int)x, y - barHeight, barWidth, barHeight);
				x += barWidth + barSpacing;
				colorIndex = (colorIndex + 1) % colors.length;
			}
			x += testCaseSpacing;
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		//System.out.println("Results update");
	}
}

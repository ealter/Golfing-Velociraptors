package edu.tufts.cs.gv.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.tufts.cs.gv.model.Dataset;

public class VizState {

	private static VizState singletonVizState;
	
	public static VizState getState() {
		if(singletonVizState == null) {
			singletonVizState = new VizState();
		}
		return singletonVizState;
	}
	
	private List<VizUpdateListener> updateListeners;
	private Dataset dataset;
	private Set<String> mousedOverTests;
	private String mousedOverStudent;
	
	public Set<String> getMousedOverTests() {
		return mousedOverTests;
	}

	public void setMousedOverTests(Set<String> mousedOverTests) {
		this.mousedOverTests = mousedOverTests;
		fireVizUpdateEvent(VizEventType.HOVERING_TESTS);
	}
	
	public String getMousedOverStudent() {
		return mousedOverStudent;
	}

	public void setMousedOverStudent(String mousedOverStudent) {
		this.mousedOverStudent = mousedOverStudent;
		fireVizUpdateEvent(VizEventType.HOVERING_STUDENT);
	}

	private VizState() {
		updateListeners = new ArrayList<>();
	}
	
	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
		fireVizUpdateEvent(VizEventType.NEW_DATA_SOURCE);
	}
	
	public void addVizUpdateListener(VizUpdateListener listener) {
		updateListeners.add(listener);
	}
	
	private void fireVizUpdateEvent(VizEventType type) {
		for (VizUpdateListener listener : updateListeners) {
			listener.vizUpdated(type);
		}
	}
}

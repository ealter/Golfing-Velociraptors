package edu.tufts.cs.gv.controller;

import java.util.ArrayList;
import java.util.List;

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
	
	private VizState() {
		updateListeners = new ArrayList<>();
	}
	
	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
		fireVizUpdateEvent();
	}
	
	public void addVizUpdateListener(VizUpdateListener listener) {
		updateListeners.add(listener);
	}
	
	private void fireVizUpdateEvent() {
		for (VizUpdateListener listener : updateListeners) {
			listener.vizUpdated();
		}
	}
}

package edu.tufts.cs.gv.controller;

import java.util.ArrayList;
import java.util.List;

import edu.tufts.cs.gv.model.Dataset;

public class VizState {
	private List<VizUpdateListener> updateListeners = new ArrayList<>();
	
	private static VizState singletonVizState;
	private Dataset dataset;
	
	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public static VizState getState() {
		if(singletonVizState == null) {
			singletonVizState = new VizState();
		}
		return singletonVizState;
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

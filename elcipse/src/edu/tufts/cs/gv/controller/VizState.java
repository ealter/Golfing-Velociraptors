package edu.tufts.cs.gv.controller;

import java.util.ArrayList;
import java.util.List;

public class VizState {
	private static List<VizUpdateListener> updateListeners = new ArrayList<>();
	
	public static void addVizUpdateListener(VizUpdateListener listener) {
		updateListeners.add(listener);
	}
	
	private static void fireVizUpdateEvent() {
		for (VizUpdateListener listener : updateListeners) {
			listener.vizUpdated();
		}
	}
}

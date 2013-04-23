package edu.tufts.cs.gv.view;

import java.awt.Graphics; 

import javax.swing.JPanel;

import edu.tufts.cs.gv.controller.VizUpdateListener;

public abstract class VizView extends JPanel implements VizUpdateListener {
	private static final long serialVersionUID = 1L;

	public abstract void update();
	
	public abstract void paint(Graphics g);
}

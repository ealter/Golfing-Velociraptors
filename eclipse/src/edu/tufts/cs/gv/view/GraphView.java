package edu.tufts.cs.gv.view;

import java.awt.Color;
import java.awt.Graphics;

public class GraphView extends VizView {
	private static final long serialVersionUID = 1L;

	@Override
	public void vizUpdated() {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.RED);
		g.fillRect(100, 100, 50, 50);
		g.setColor(Color.CYAN);
		g.drawRect(10, 10, 50, 50);
		//System.out.println("Graph render");
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		//System.out.println("Graph update");
	}

}

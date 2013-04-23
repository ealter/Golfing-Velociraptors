package edu.tufts.cs.gv;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.tufts.cs.gv.controller.VizState;
import edu.tufts.cs.gv.model.Dataset;
import edu.tufts.cs.gv.view.GraphView;
import edu.tufts.cs.gv.view.ResultsView;
import edu.tufts.cs.gv.view.StudentView;
import edu.tufts.cs.gv.view.VizView;

public class Visualization extends JFrame {
	//
	// Global
	//
	
	private static final long serialVersionUID = 1L;
	
	static final String STARTSCREEN = "Start Screen";
	static final String VIZSCREEN = "Visualization Screen";
	
	private Timer renderTimer;
	
	private JMenuBar menu;
	private JMenu file;
	private JMenuItem openDataset;
	
	private JFileChooser datasetChooser;
	
	private JPanel pnlRoot;
	private CardLayout crdRoot;
	
	//
	// Start Screen
	//
	
	private JPanel pnlStart;
	private JButton btnChoose;
	
	//
	// Visualization Screen
	//
	
	private JSplitPane studentSplit, testSplit;
	
	// Student view
	private StudentView studentView;
	private JScrollPane studentScrollView;
	
	// Results view
	private VizView resultsView;
	private JScrollPane resultsScrollView;
	
	// Graph view
	private GraphView graphView;
	private JLabel lblSpring, lblSpringLen, lblRepel, lblGrav, lblEnergy;
	private JSlider sldSpring, sldSpringLen, sldRepel, sldGrav, sldEnergy;
	private JPanel pnlVisualization, pnlGraphView;
	
	public Visualization(int fps) {
		//
		// Start Screen
		//
	
		btnChoose = new JButton("Open a Dataset");
		btnChoose.setPreferredSize(new Dimension(200, 50));
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onOpenDataset(); }
		});
		
		pnlStart = new JPanel(new BorderLayout());
		pnlStart.add(btnChoose, BorderLayout.CENTER);
		
		//
		// Visualization Screen
		//
		
		// Student view
		studentView = new StudentView();
		studentScrollView = new JScrollPane(studentView);
		
		// Results view
		resultsView = new ResultsView();
		resultsScrollView = new JScrollPane(resultsView);
		
		// Graph view
		graphView = new GraphView();
		lblSpring = new JLabel("Spring", JLabel.CENTER);
		lblSpringLen = new JLabel("Spring Length", JLabel.CENTER);
		lblRepel = new JLabel("Repulsion", JLabel.CENTER);
		lblGrav = new JLabel("Gravity", JLabel.CENTER);
		lblEnergy = new JLabel("Energy", JLabel.CENTER);
		sldSpring = new JSlider(JSlider.HORIZONTAL, 0, 1000, 500);
		sldSpring.addChangeListener(graphView.getSpringListener());
		sldSpringLen = new JSlider(JSlider.HORIZONTAL, 0, 1000, 300);
		sldSpringLen.addChangeListener(graphView.getSpringLengthListener());
		sldRepel = new JSlider(JSlider.HORIZONTAL, 0, 1000, 200);
		sldRepel.addChangeListener(graphView.getRepulstionListener());
		sldGrav = new JSlider(JSlider.HORIZONTAL, 0, 1000, 100);
		sldGrav.addChangeListener(graphView.getGravityListener());
		sldEnergy = new JSlider(JSlider.HORIZONTAL, 0, 1000, 50);
		sldEnergy.addChangeListener(graphView.getEnergyListener());
		pnlGraphView = new JPanel();
		GroupLayout layout = new GroupLayout(pnlGraphView);
		pnlGraphView.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(lblSpring, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblSpringLen, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblRepel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblGrav, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblEnergy, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(layout.createSequentialGroup()
						.addComponent(sldSpring, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(sldSpringLen, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(sldRepel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(sldGrav, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(sldEnergy, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addComponent(graphView));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(lblSpring)
						.addComponent(lblSpringLen)
						.addComponent(lblRepel)
						.addComponent(lblGrav)
						.addComponent(lblEnergy))
				.addGroup(layout.createParallelGroup()
						.addComponent(sldSpring)
						.addComponent(sldSpringLen)
						.addComponent(sldRepel)
						.addComponent(sldGrav)
						.addComponent(sldEnergy))
				.addComponent(graphView));
		
		// Layout setup
		testSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, pnlGraphView, resultsScrollView);
		testSplit.setResizeWeight(.7);
		testSplit.setDividerLocation(.7);
		testSplit.setOneTouchExpandable(true);
		
		studentSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, studentScrollView, testSplit);
		studentSplit.setResizeWeight(.3);
		studentSplit.setDividerLocation(.3);
		studentSplit.setOneTouchExpandable(true);
		
		pnlVisualization = new JPanel(new BorderLayout());
		pnlVisualization.add(studentSplit, BorderLayout.CENTER);
		
		//
		// Global
		//
		
		crdRoot = new CardLayout();
		
		pnlRoot = new JPanel(crdRoot);
		pnlRoot.add(pnlStart, STARTSCREEN);
		pnlRoot.add(pnlVisualization, VIZSCREEN);
		
		datasetChooser = new JFileChooser("..");
		
		openDataset = new JMenuItem("Open Dataset");
		openDataset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onOpenDataset(); }
		});
		
		file = new JMenu("File");
		file.add(openDataset);
		
		menu = new JMenuBar();
		menu.add(file);
		
		this.setSize(600, 600);
		this.add(pnlRoot);
		this.setJMenuBar(menu);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		renderTimer = new Timer((int)(1000.0 / fps), new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphView.update();
				resultsView.update();
				repaint();
			}
		});
		renderTimer.start();
	}
	
	private void onOpenDataset() {
		int result = datasetChooser.showOpenDialog(Visualization.this);
		if (result == JFileChooser.APPROVE_OPTION) {
			VizState.getState().setDataset(Dataset.parseFromFile(datasetChooser.getSelectedFile().getAbsolutePath()));
			crdRoot.show(pnlRoot, VIZSCREEN);
		}
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new Visualization(30); //TODO: This defines the fps. It should not be 1!
	}
}

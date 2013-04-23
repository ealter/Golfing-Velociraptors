package edu.tufts.cs.gv.view;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import edu.tufts.cs.gv.controller.VizEventType;
import edu.tufts.cs.gv.controller.VizState;
import edu.tufts.cs.gv.controller.VizUpdateListener;
import edu.tufts.cs.gv.view.StudentListCellView;

public class StudentView extends JPanel implements VizUpdateListener {
	private static final long serialVersionUID = 1L;
	
	private JList<String> lstStudents;
	private DefaultListModel<String> modStudents;
	private StudentListCellView viewStudent;

	public StudentView() {
		modStudents = new DefaultListModel<String>();
		
		lstStudents = new JList<String>(modStudents);
		viewStudent = new StudentListCellView(lstStudents);
		
		lstStudents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstStudents.setVisibleRowCount(-1);
		lstStudents.setCellRenderer(viewStudent);
		
		this.setLayout(new BorderLayout());
		this.add(lstStudents, BorderLayout.CENTER);
		
		VizState.getState().addVizUpdateListener(this);
	}
	
	@Override
	public void vizUpdated(VizEventType eventType) {
		if (eventType != VizEventType.NEW_DATA_SOURCE) 
			return;
		
		modStudents.removeAllElements();
		for (String student : VizState.getState().getDataset().getAllStudents()) {
			modStudents.addElement(student);
		}
	}
}

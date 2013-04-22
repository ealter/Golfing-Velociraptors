package edu.tufts.cs.gv.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import edu.tufts.cs.gv.controller.VizEventType;
import edu.tufts.cs.gv.controller.VizState;
import edu.tufts.cs.gv.controller.VizUpdateListener;

public class StudentView extends JPanel implements VizUpdateListener {
	private static final long serialVersionUID = 1L;
	
	private JTextPane studentText;

	public StudentView() {
		studentText = new JTextPane();
		studentText.setEditable(false);
		studentText.setFont(new Font("courier new", Font.PLAIN, 14));
		this.setLayout(new BorderLayout());
		this.add(studentText, BorderLayout.CENTER);
		
		VizState.getState().addVizUpdateListener(this);
	}
	
	@Override
	public void vizUpdated(VizEventType eventType) {
		if (eventType == VizEventType.NEW_DATA_SOURCE) {
			String str = "";
			for (String student : VizState.getState().getDataset().getAllStudents()) {
				str += student + "\n";
			}
			studentText.setText(str);
		} else if (eventType == VizEventType.HOVERING_TESTS) {
			VizState state = VizState.getState();
			Set<String> tests = state.getMousedOverTests();
			Set<String> students = new HashSet<>();
			for (String test : tests) {
				students.addAll(state.getDataset().getPassersOfTest(test));
			}
			
			String text = studentText.getText();
			studentText.setText("");
			studentText.setText(text);
			
			SimpleAttributeSet highlight = new SimpleAttributeSet();
			StyleConstants.setBackground(highlight, new Color(173, 216, 230));
			StyledDocument doc = studentText.getStyledDocument();
			for (String student : students) {
				int offset = studentText.getText().indexOf(student);
				doc.setCharacterAttributes(offset, student.length(), highlight, true);
			}
		}
	}
}

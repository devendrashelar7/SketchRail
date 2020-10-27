import graph.Paper;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * 
 */
@SuppressWarnings("serial")
public class GraphFrame extends JFrame {
	/**
	 * graphPanel
	 */
	GraphPanel graphPanel;

	/**
	 * @param freightSim
	 */
	public GraphFrame(FreightSim freightSim) {
		super("Graph");
		graphPanel = new GraphPanel(freightSim);
		getContentPane().setLayout(null);
		getContentPane().add(graphPanel);
		setBounds(20, 20, 1200, 700);
		graphPanel.setBounds(0, 0, 1200, 700);
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				// System.exit(0);
			}
		});
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				graphPanel.setBounds(0, 0, getWidth(), getHeight());
			}
		});
		setVisible(true);
	}

	/**
	 * @param arg
	 */
	@SuppressWarnings("unused")
	public static void main(String arg[]) {
		GraphFrame gf;
	}

	/**
	 * @return {@link Paper}
	 */
	public Paper getPaper() {
		return graphPanel.getPaper();
	}
}
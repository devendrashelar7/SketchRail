import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

/**
 * 
 */
public class TrainRunTime extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * currentTrain
	 */
	Train currentTrain;
	/**
	 * trainTable
	 */
	JTable trainTable;

	/**
 * 
 */
	public TrainRunTime() {
		super("Running times of all trains");
		trainTable = new JTable(new AbstractTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			String[] columnNames = { "Train No", "Total Time", "Traversal Time" };
			ArrayList<Train> interArray = GlobalVar.trainArrayList;

			public String getColumnName(int col) {
				return columnNames[col].toString();
			}

			// public int getRowCount() { return freightSim.currTrainNo; }
			public int getRowCount() {
				return interArray.size();
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				switch (col) {
				case 0:
					return new Double(((Train) interArray.get(row)).trainNo);

				case 1:
					return GlobalVar.timeToString(Math
							.round(((Train) interArray.get(row)).timeTables
									.size() > 0 ? ((Train) interArray.get(row))
									.totalTime() : 0.0));
				case 2:
					return GlobalVar.timeToString(Math
							.round(((Train) interArray.get(row)).timeTables
									.size() > 0 ? ((Train) interArray.get(row))
									.travelTime() : 0.0));
				}
				return null;
			}

		});

		JScrollPane scrollPane = new JScrollPane(trainTable);
		trainTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		getContentPane().add(scrollPane);
		setBounds(100, 100, 400, 400);
		show();
	}

}

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * 
 */
public class TrainData extends JFrame {
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
	 * @param trn
	 */
	public TrainData(Train trn) {
		super("Time-Table for TrainNo-" + trn.trainNo);
		currentTrain = trn;

		trainTable = new JTable(new AbstractTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			String[] columnNames = { "Block No", "Arrival Time",
					"Departure Time", "Duration of Stay" };
			ArrayList<TimetableEntry> interArray = currentTrain.timeTables;

			public String getColumnName(int col) {
				return columnNames[col].toString();
			}

			public int getRowCount() {
				return interArray.size();
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				switch (col) {
				case 0:
					return new Integer(
							((TimetableEntry) interArray.get(row)).loopNo);
				case 1:
					return GlobalVar.timeToString(((TimetableEntry) interArray
							.get(row)).arrivalTime);
				case 2:
					return GlobalVar.timeToString(((TimetableEntry) interArray
							.get(row)).departureTime);
				case 3:
					return GlobalVar
							.timeToString(((TimetableEntry) interArray.get(row)).departureTime
									- ((TimetableEntry) interArray.get(row)).arrivalTime);

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

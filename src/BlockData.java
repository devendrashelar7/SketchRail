import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Title: Simulation of Train Pathing Description: Copyright: Copyright (c) 2002
 * Company: IIT
 * 
 * @author Rajesh Naik
 * @version 1.0
 */

public class BlockData extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * currentBlock
     */
    Block currentBlock;
    /**
     * blockTable
     */
    JTable blockTable;

    /**
     * @param blk
     * constructor.
     */
    public BlockData(Block blk) {
	super("Occupancy Table for " + blk.blockName);
	currentBlock = blk;

	blockTable = new JTable(new AbstractTableModel() {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String[] columnNames = { "Train No", "Arrival Time",
		    "Departure Time", "Duration of Stay" };
	    IntervalArray interArray = currentBlock.occupy;

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
		    return new Integer(interArray.get(row).trainNo);
		case 1:
		    return GlobalVar.timeToString(interArray
			    .get(row).startTime);
		case 2:
		    return GlobalVar.timeToString(interArray
			    .get(row).endTime);
		case 3:
		    return GlobalVar.timeToString(interArray
			    .get(row).endTime
			    - interArray.get(row).startTime);
		}
		
		return null;
	    }
	});

	JScrollPane scrollPane = new JScrollPane(blockTable);
	blockTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
	getContentPane().add(scrollPane);
	setBounds(100, 100, 400, 400);
	setVisible(true);
    }

}

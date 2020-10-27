import graph.DrawStyle;
import graph.GLine;
import graph.LineLayer;
import graph.Marker;
import graph.Paper;
import graph.XGridLine;
import graph.YGridLine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import util.Debug;

/**
 * GraphPaper
 */
public class GraphPaper extends Paper {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * yScale
	 */
	double yScale;
	/**
	 * xScale
	 */
	double xScale = 2;
	/**
	 * gLayer
	 */
	LineLayer gLayer = new LineLayer();
	/**
	 * blockReserveLayer
	 */
	LineLayer blockReserveLayer = new LineLayer();
	/**
	 * signalLayer
	 */
	LineLayer signalLayer = new LineLayer();
	/**
	 * profLayer
	 */
	LineLayer profileLayer = new LineLayer();
	/**
	 * blockReserve
	 */
	boolean blockReserve = true;

	// ------train graph variables......
	/**
	 * xOrigin
	 */
	final float xOrigin = 100;
	/**
	 * yOrigin
	 */
	final float yOrigin = 20;
	/**
	 * xWidth
	 */
	final float xWidth = 600;
	/**
	 * yShift
	 */
	double yShift = 0;
	/**
	 * xShift
	 */
	double xShift = 100;

	// pointer variables
	/**
	 * xPoint
	 */
	int xPoint;
	/**
	 * yPoint
	 */
	int yPoint;
	/**
	 * xStart
	 */
	int xStart = 0;

	/**
	 * constructor.
	 */
	public GraphPaper() {
		// trainGraph = new Paper();
	}

	/**
	 * nextScreen
	 */
	public void nextScreen() {
		xStart += xWidth / 2;
		gLayer.setXShift(-(xStart / xScale) + xShift);
		blockReserveLayer.setXShift(-(xStart / xScale) + xShift);
		redrawGraph();
	}

	/**
	 * previousScreen
	 */
	public void previousScreen() {
		if (xStart > 0) {
			xStart -= xWidth / 2;
		}
		gLayer.setXShift(-(xStart / xScale) + xShift);
		blockReserveLayer.setXShift(-(xStart / xScale) + xShift);
		redrawGraph();
	}

	/**
	 * redrawGraph
	 */
	public void redrawGraph() {
		gLayer.clear();
		blockReserveLayer.clear();
		drawScheduledTrains(GlobalVar.trainArrayList);
		drawGraph();
	}

	/**
	 * @param trn
	 */

	public void drawTrain(Train trn) {
		int a = (trn.trainNo) % 5;
		profileLayer.clear();
		// ArrayList pts = new ArrayList();
		GLine gl = new GLine();
		signalLayer.clear();

		for (int m = 0; m < trn.timeTables.size(); m++) {
			TimetableEntry trainTimeTableEntry = trn.timeTables.get(m);
			GLine gLine = new GLine();
			gLine.add(new Marker(0, trainTimeTableEntry.startMilePost));
			gLine.add(new Marker(
					0,
					trainTimeTableEntry.startMilePost
							+ (trainTimeTableEntry.endMilePost - trainTimeTableEntry.startMilePost)
							/ 5));

			Color signalColor = null;
			if (GlobalVar.numberOfColour == 4) {
				signalColor = (6 == trainTimeTableEntry.signal) ? Color.pink
						: (3 == trainTimeTableEntry.signal) ? Color.green
								: ((2 == trainTimeTableEntry.signal) ? Color.blue
										: ((1 == trainTimeTableEntry.signal) ? Color.yellow
												: Color.red));
			}

			if (GlobalVar.numberOfColour == 3) {
				signalColor = (6 == trainTimeTableEntry.signal) ? Color.pink
						: (2 == trainTimeTableEntry.signal) ? Color.green
								: (((1 == trainTimeTableEntry.signal) ? Color.yellow
										: Color.red));
			}

			if (GlobalVar.numberOfColour == 2) {
				signalColor = (6 == trainTimeTableEntry.signal) ? Color.pink
						: (0 == trainTimeTableEntry.signal) ? Color.red
								: (1 == trainTimeTableEntry.signal) ? Color.green
										: Color.green;
			}

			gLine.setPaint(signalColor);
			signalLayer.add(gLine);

			ArrayList<VelocityProfile> profileArray = trainTimeTableEntry.velocityProfileArray;

			for (int i = 0; i < profileArray.size(); i++) {
				GLine profLine = new GLine();
				VelocityProfile veloProf = profileArray.get(i);
				Debug.print("printing velo profile   " + i
						+ "   Start Length: " + veloProf.startLength
						+ " End Length " + veloProf.endLength
						+ "   Start Velo " + veloProf.startVelocity
						+ "  End  Velocity:" + veloProf.endVelocity);
				profLine.add(new Marker(veloProf.startVelocity * 25,
						veloProf.startLength));
				profLine.add(new Marker(veloProf.endVelocity * 25,
						veloProf.endLength));
				profLine.setPaint(Color.black);
				profileLayer.add(profLine);
			}
		}

		for (int m = 0; m < trn.timeTables.size(); m++) {
			TimetableEntry trainTimeTableEntry = trn.timeTables.get(m);
			if (((trainTimeTableEntry.arrivalTime < xStart / xScale) && (trainTimeTableEntry.departureTime < xStart
					/ xScale))
					|| ((trainTimeTableEntry.departureTime > ((xStart + xWidth) / xScale)) && (trainTimeTableEntry.arrivalTime > ((xStart + xWidth) / xScale)))) {
				continue;
			}
			gl.add(new Marker(trainTimeTableEntry.arrivalTime,
					trainTimeTableEntry.startMilePost));
			gl.add(new Marker(trainTimeTableEntry.departureTime,
					trainTimeTableEntry.endMilePost));
			Debug.print("Alloted loop:  " + trainTimeTableEntry.loopNo
					+ "   Arr Time: " + trainTimeTableEntry.arrivalTime
					+ "   DepTime: " + trainTimeTableEntry.departureTime
					+ "   milep: " + trainTimeTableEntry.startMilePost
					+ "   Velocity:" + trainTimeTableEntry.svelo);

		}
		if (a == 5) {
			a = -1;
		}
		a++;
		Debug.print("a is :" + a);
		if (a == 0) {
			gl.setPaint(Color.black);
		}
		if (a == 1) {
			gl.setPaint(Color.magenta);
		}
		if (a == 2) {
			gl.setPaint(Color.green);
		}
		if (a == 3) {
			gl.setPaint(Color.cyan);
		}
		if (a == 4) {
			gl.setPaint(Color.red);
		}
		if (a == 5) {
			gl.setPaint(Color.yellow);
		}
		gLayer.add(gl);
		drawBlockReservations(trn);
		drawGraph();
	}

	/**
	 * @param Trains
	 * @param StnArray
	 */
	public void drawGraph(ArrayList<Train> trainArray,
			ArrayList<Station> stnArray) {
		Station stn;
		double yMax;
		addLayer(profileLayer);
		addLayer(signalLayer);
		// ArrayList hrA = new ArrayList();
		// ArrayList vrA = new ArrayList();
		Paper.GridLayer yGridLayer = getGridInstance();
		YGridLine yGridLine;
		XGridLine xGridLine;
		setOrigin(xOrigin, yOrigin);
		Debug.print("graph " + stnArray.size() + " " + trainArray.size());
		stn = stnArray.get(stnArray.size() - 1);
		yMax = stn.endMilePost;

		// Devendra - To add the time axis calibration
		GLine timeAxis = new GLine();
		// System.out.println("ymax" + yMax + " " + this.getBounds().y);

		timeAxis.add(new Marker(0, getHeight(), "time1"));
		timeAxis.add(new Marker(xWidth, getHeight(), "time2"));
		gLayer.add(timeAxis);

		// display no of loops for station in graph

		for (int i = 0; i < stnArray.size(); i++) {
			stn = stnArray.get(i);
			int count = 0;
			for (int j = 0; j < GlobalVar.loopArrayList.size(); j++) {
				String s1, s2;
				s1 = stn.stationName;
				s2 = GlobalVar.loopArrayList.get(j).stationName;

				if (s1.equalsIgnoreCase(s2))
					count++;
			}

			yMax = Math.max(yMax, stn.startMilePost);
			if (i == 0) {
				yShift = stn.startMilePost;
			}

			int noOfLoops = count;
			yGridLine = new YGridLine((stn.startMilePost - yShift),
					stn.stationName + "(" + Integer.toString(noOfLoops) + ")");
			Debug.print("graph " + stn.startMilePost + " " + stn.stationName);
			yGridLayer.add(yGridLine);
		}

		yScale = (getHeight() - 35) / (yMax - yShift);
		Debug.print("max height  " + yMax + " height of graph  " + getHeight()
				+ "Scale " + yScale);
		setScale(xScale, yScale);

		for (int i = 0; i < 100; i += 25) {
			xGridLine = new XGridLine(i, Integer.toString(i * 60 / 25));
			yGridLayer.add(xGridLine);
		}

		// Devendra :- add the time lines
		int timeShift = 100;
		for (int i = 4; i < 1000; i += 4) {
			GLine gline = new GLine();

			if (i % 24 == 0) {
				gline.add(new Marker(i, 0, "hi1"));
				gline.add(new Marker(i, getHeight(), "hi"));
				gline.setName((new Integer(i * 60 / 24)).toString());
				gLayer.add(gline);
			} else {
				// xGridLine = new XGridLine(i + timeShift);
				//
				// yGridLayer.add(xGridLine);
			}

		}

		gLayer.setYShift(-yShift);
		blockReserveLayer.setYShift(-yShift);
		profileLayer.setYShift(-yShift);
		signalLayer.setYShift(-yShift);
		gLayer.setXShift(xShift);
		blockReserveLayer.setXShift(xShift);
		signalLayer.setXShift(xShift);
		yGridLayer.setVisible(true);
		setYGrid(yGridLayer);

		drawScheduledTrains(trainArray);
		gLayer.setStroke(DrawStyle.thinStroke);
		blockReserveLayer.setStroke(DrawStyle.thinStroke);
		addLayer(gLayer);
		addLayer(blockReserveLayer);
		drawGraph();
	}

	/**
	 * @param Trains
	 */
	public void drawScheduledTrains(ArrayList<Train> Trains) {
		TimetableEntry trainTimeTableEntry;
		Train train;
		GLine gl;
		int a = 0;
		for (int i = 0; i < Trains.size(); i++) {
			train = Trains.get(i);
			if (train.timeTables.size() <= 0) {
				continue;
			}
			TimetableEntry trainTimeTableEntryStart = train.timeTables.get(0);
			TimetableEntry trainTimeTableEntryEnd = train.timeTables
					.get(train.timeTables.size() - 1);
			if (((trainTimeTableEntryStart.arrivalTime < xStart / xScale) && (trainTimeTableEntryEnd.departureTime < xStart
					/ xScale))
					|| ((trainTimeTableEntryEnd.departureTime > ((xStart + xWidth) / xScale)) && (trainTimeTableEntryStart.arrivalTime > ((xStart + xWidth) / xScale)))) {
				continue;
			}
			gl = new GLine();
			for (int m = 0; m < train.timeTables.size(); m++) {
				Debug.print("adding train " + m);
				trainTimeTableEntry = train.timeTables.get(m);
				if (((trainTimeTableEntry.arrivalTime < xStart / xScale) && (trainTimeTableEntry.departureTime < xStart
						/ xScale))
						|| ((trainTimeTableEntry.departureTime > ((xStart + xWidth) / xScale)) && (trainTimeTableEntry.arrivalTime > ((xStart + xWidth) / xScale)))) {
					continue;
				}
				gl.add(new Marker(trainTimeTableEntry.arrivalTime,
						trainTimeTableEntry.startMilePost));
				gl.add(new Marker(trainTimeTableEntry.departureTime,
						trainTimeTableEntry.endMilePost));
				Debug.print(" Alloted loop:  " + trainTimeTableEntry.loopNo
						+ "   Arr Time: " + (trainTimeTableEntry.arrivalTime)
						+ "   DepTime: " + (trainTimeTableEntry.departureTime)
						+ "   milep: " + trainTimeTableEntry.startMilePost
						+ "   end mile" + trainTimeTableEntry.endMilePost);

			}
			train.drawColour = (Color) gl.getPaint();
			if (a == 5) {
				a = -1;
			}
			a++;
			if (a == 0) {
				gl.setPaint(Color.black);
			}
			if (a == 1) {
				gl.setPaint(Color.magenta);
			}
			if (a == 2) {
				gl.setPaint(Color.cyan);
			}
			if (a == 3) {
				gl.setPaint(Color.green);
			}
			if (a == 4) {
				gl.setPaint(Color.red);
			}
			if (a == 5) {
				gl.setPaint(Color.yellow);
			}
			gLayer.add(gl);
			drawBlockReservations(train);
		}
	}

	/**
	 * clearBlockReservations.
	 */
	public void clearBlockReservations() {
		blockReserveLayer.clear();
	}

	/**
	 * @param trn
	 */

	public void drawBlockReservations(Train trn) {
		Debug.print("I am drwablock reservations " + trn.trainNo);
		if (blockReserve == false) {
			return;
		}

		for (Enumeration<HashBlockEntry> e = GlobalVar.hashBlock.elements(); e
				.hasMoreElements();) {

			HashBlockEntry hbEntry = e.nextElement();
			Block currBlock = hbEntry.block;
			IntervalArray Occupy = currBlock.occupy;
			Occupy.printInterval();
			Color clr = getColor(trn.trainNo);

			for (int i = 0; i < Occupy.size(); i++) {
				GLine gl = new GLine();
				int trnNo = Occupy.get(i).trainNo;
				Debug.print("Train no " + trnNo);

				Interval interval = Occupy.get(i);
				double startTime = interval.startTime;
				double endTime = interval.endTime;

				if (trnNo == trn.trainNo) {
					if (((startTime < xStart / xScale) && (endTime < xStart
							/ xScale))
							|| ((endTime > ((xStart + xWidth) / xScale)) && (startTime > ((xStart + xWidth) / xScale)))) {
						continue;
					}
					double startMilePost = currBlock.startMilePost;
					double endMilePost = currBlock.endMilePost;

					gl.add(new Marker(startTime, startMilePost));
					gl.add(new Marker(endTime, startMilePost));
					gl.add(new Marker(endTime, endMilePost));
					gl.add(new Marker(startTime, endMilePost));
					gl.add(new Marker(startTime, startMilePost));
					gl.setPaint(clr);
					blockReserveLayer.add(gl);
				}
			}
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return {@link JPopupMenu}
	 */
	public JPopupMenu analysePoint(int x, int y) {
		double time = ((x - xOrigin + xStart) / xScale) - xShift;
		double distance = ((getHeight() - y - yOrigin) / yScale) + yShift;
		Debug.print("yorig : " + y + " xOrigi : " + x);
		Debug.print("y : " + distance + " x : " + time);
		NextBlockArray selectBlk = GlobalVar.getBlock(distance);
		JPopupMenu popup = new JPopupMenu();
		popup.setLightWeightPopupEnabled(false);
		for (int i = 0; i < selectBlk.size(); i++) {
			Block currBlock = (Block) selectBlk.get(i);
			JMenuItem menuItem = new BlockMenuItem(currBlock.blockName,
					currBlock);
			popup.add(menuItem);
			int trainNo = currBlock.getTrainNo(time);
			if (-1 != trainNo) {
				menuItem = new TrainMenuItem("Train no - " + trainNo, trainNo,
						time, currBlock);
				popup.add(menuItem);
			}
		}
		return popup;
	}

	/**
	 * @param No
	 * @return {@link Color}
	 */
	public Color getColor(int No) {
		ArrayList<Train> trainArrayList = GlobalVar.trainArrayList;
		for (int i = 0; i < trainArrayList.size(); i++) {
			if (trainArrayList.get(i).trainNo == No) {
				Debug.print("Train no " + i + " " + No);
				return (trainArrayList.get(i).drawColour);
			}
		}
		return Color.black;
	}

	/**
	 * TrainMenuItem
	 */
	class TrainMenuItem extends JMenuItem {
		/**
		 * trainNo
		 */
		int trainNo;
		/**
		 * time
		 */
		double time;
		/**
		 * block
		 */
		Block block;

		/**
		 * @param name
		 * @param no
		 * @param ti
		 * @param bloc
		 */
		public TrainMenuItem(String name, int no, double ti, Block bloc) {
			super(name);
			trainNo = no;
			time = ti;
			block = bloc;
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					drawGraph();
					Debug.print("Processing menu train no " + trainNo);
					Train tr = GlobalVar.getTrainNew(trainNo, time, block);
					Debug.print(" Train returned is " + tr.trainNo);
					TrainData trnFrame = new TrainData(tr);

					// redraw the velocity profile for the train
					if (GlobalVar.freightSim != null) {
						if (GlobalVar.freightSim.graphPanel != null) {
							Debug.print("GraphPaper: redrawing the train profile");
							GlobalVar.freightSim.graphPanel.drawTrain(tr);
						}
					}
				}
			});
		}
	}

	/**
	 * BlockMenuItem.
	 */
	class BlockMenuItem extends JMenuItem {
		/**
		 * block
		 */
		Block block;

		/**
		 * @param name
		 * @param b
		 */
		public BlockMenuItem(String name, Block b) {
			super(name);
			block = b;
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					drawGraph();
					Debug.print("Processing menu train no " + block.blockNo);
					BlockData blkFrame = new BlockData(block);
				}
			});
		}
	}
}

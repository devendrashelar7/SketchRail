import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Comparator;

import util.Debug;

/**
 * Station
 */
public class Station implements Comparator<Station> {
	/**
	 * stationNumber
	 */
	int stationNumber;
	/**
	 * startMilePost
	 */
	double startMilePost;
	/**
	 * endMilePost
	 */
	double endMilePost;
	/**
	 * crossVelocity
	 */
	double crossVelocity;
	/**
	 * previousStation
	 */
	public Station previousStation = null;
	/** prevStn will store the next station in the down(1) direction */
	public Station nextStation = null;
	/** nextStn will store the next station in the up(0) direction */

	ArrayList<Loop> Loops = new ArrayList<Loop>();
	/**
	 * stationName
	 */
	String stationName;

	/**
     * 
     */
	public Station() {
	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param str
	 */
	public Station(double a, double b, double c, String str) {
		crossVelocity = c;
		stationName = str;
		if (a > b) {
			startMilePost = b;
			endMilePost = a;
		} else {
			startMilePost = a;
			endMilePost = b;
		}
	}

	/**
	 * This will return the next station in the direction given. null if its the
	 * last station in the given direction
	 * 
	 * @param dir
	 * @return next Station
	 */
	public Station nextStation(int dir) {
		if (dir == 0) {// up
			return nextStation;
		}
		return previousStation;
	}

	/**
	 * This will return the previous station in the direction given. null if its
	 * the last station in the given direction
	 * 
	 * @param dir
	 *            direction
	 * @return the previous station
	 */
	public Station prevStation(int dir) {
		if (dir == 0) {// up
			return previousStation;
		}
		return nextStation;
	}

	/**
	 * Get the main loop of the station.
	 * 
	 * @param direction
	 * @return the main loop
	 */
	public Loop getMainLoop(int direction) {
		for (int i = 0; i < Loops.size(); i++) {
			if ((((Loop) Loops.get(i)).whetherMainLine == 1)
					&& (((Loop) Loops.get(i)).direction) == direction) {
				return ((Loop) Loops.get(i));
			}
		}
		return null;
	}

	/**
	 * Get the departing time of the train
	 * 
	 * @param trainNo
	 *            train number
	 * @param dir
	 *            direction
	 * @return double
	 */
	public double getDepartTime(int trainNo, int dir) {
		for (int currLoop = 0; currLoop < Loops.size(); currLoop++) {
			Loop loop = (Loop) Loops.get(currLoop);
			if (loop.direction == dir) {

				for (int currInterv = 0; currInterv < loop.occupy.size(); currInterv++) {
					Interval interv = loop.occupy.get(currInterv);
					if (interv.trainNo == trainNo) {
						return interv.endTime;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Station: int simulateTrain(Train currTrain) 1) Consider the starting
	 * loop. 2) Get the loop's nextBlocks and the previousBlocks. 3) It does
	 * some signal failure changes (which we are not bothered about now) 4) And
	 * until the simulation is not finished it keeps traversing the block by
	 * newLoop.traverseBlock(currTrain,startTime, startTime,0)
	 * 
	 * @param currTrain
	 * @return simulateTrain
	 */
	@SuppressWarnings( {})
	public int simulateTrain(Train currTrain) {

		Loop newLoop = new Loop(currTrain.direction, 0, 0);

		newLoop.startMilePost = startMilePost;
		newLoop.endMilePost = endMilePost;
		newLoop.maximumPossibleSpeed = crossVelocity;
		newLoop.blockNo = currTrain.startLoopNo;
		newLoop.station = this;

		HashBlockEntry hbEntry = GlobalVar.hashBlock.get(currTrain.startLoopNo);
		if (hbEntry == null) {
			Debug.print("Station: simulateTrain: Error: block not present "
					+ currTrain.startLoopNo);
			// System.exit(0);
			System.out.println("simulateTrain: hbEntry is null");
			throw new NullPointerException("simulateTrain: hbEntry is null");
		}

		newLoop.tinyBlock = hbEntry.block.tinyBlock;
		Debug.print("Station: simulateTrain: Train starts at "
				+ currTrain.startLoopNo + " cross velo " + crossVelocity);

		newLoop.nextBlocks = hbEntry.block.nextBlocks;
		newLoop.previousBlocks = hbEntry.block.previousBlocks;

		boolean simFinish = false;
		double startTime = currTrain.startTime;
		if (GlobalVar.simulationType.equalsIgnoreCase("SignalFailure")) {
			Block bi = new Block();
			for (Enumeration<HashBlockEntry> e = GlobalVar.hashBlock.elements(); e
					.hasMoreElements();) {
				hbEntry = e.nextElement();
				bi = hbEntry.block;
				bi.signalFailFlag = 0;
			}
		}

		while (simFinish != true) {

			// just for verification
			if (GlobalVar.verifyTestCases) {
				// Devendra
				boolean verifyLoopAndTrain = checkWhichNewLoop(newLoop);
				verifyLoopAndTrain = verifyLoopAndTrain
						&& checkWhichTrainNo(currTrain);
				if (verifyLoopAndTrain == false) {
					System.out
							.println("TestCase has failed during first verification");
				}

			} else {
				System.out.println("NewLoop " + newLoop.blockNo);
				System.out.println("TrainNo " + currTrain.trainNo);
			}

			StatusTraverseBlock retStatus = newLoop.traverseBlock(currTrain,
					startTime, startTime, 0);

			simFinish = retStatus.status;

			if (retStatus.limit)
				return -1;

			if (simFinish) {

				// Devendra
				if (GlobalVar.verifyTestCases) {
					boolean verifyTrain = checkWhichTrainSimulated(currTrain);
					if (verifyTrain == false) {
						System.out
								.println("checkWhichTrainSimulated: returned false. TestCase failed");
						System.out.println("SimulatedTrain "
								+ currTrain.trainNo);
					}
				} else {
					System.out.println("SimulatedTrain " + currTrain.trainNo);
				}
			}
			startTime = retStatus.departureTime + 1;
		}
		Debug
				.print("Station: simulateTrain: this train simulation is complete "
						+ currTrain.trainNo);

		return 0;
	}

	// Devendra
	/**
	 * Check if the right train is scheduled
	 * 
	 * @param currentTrain
	 * @return true if the current train was simulated else false
	 */
	public static boolean checkWhichTrainSimulated(Train currentTrain) {
		try {
			StreamTokenizer outputFileReaderStreamTokenizer = GlobalVar.outputFileReaderStreamTokenizer;
			// read the "SimulatedTrain" String
			outputFileReaderStreamTokenizer.nextToken();
			String simulatedTrainString = outputFileReaderStreamTokenizer.sval;

			// read the train number
			outputFileReaderStreamTokenizer.nextToken();
			int trainNumber = (int) outputFileReaderStreamTokenizer.nval;

			// System.out.println("simulatedTrain: "+simulatedTrainString+" trainNumber "+
			// trainNumber+" actual "+currentTrain.trainNo);

			if (simulatedTrainString.equalsIgnoreCase("SimulatedTrain")
					&& currentTrain.trainNo == trainNumber) {
				return true;
			}
			GlobalVar.testCaseVerified = false;
			return false;
		} catch (IOException e) {
			System.out
					.println("Station: checkWhichTrainSimulated: IOException has occured");
			GlobalVar.testCaseVerified = false;
			return false;
		}

	}

	// Devendra
	/**
	 * Check if the right train is scheduled
	 * 
	 * @param currentTrain
	 * @return true if the current train was simulated else false
	 */
	public static boolean checkWhichTrainNo(Train currentTrain) {
		try {
			StreamTokenizer outputFileReaderStreamTokenizer = GlobalVar.outputFileReaderStreamTokenizer;
			// read the "SimulatedTrain" String
			outputFileReaderStreamTokenizer.nextToken();
			String simulatedTrainString = outputFileReaderStreamTokenizer.sval;

			// read the train number
			outputFileReaderStreamTokenizer.nextToken();
			int trainNumber = (int) outputFileReaderStreamTokenizer.nval;

			// System.out.println("simulatedTrain: "+simulatedTrainString+" trainNumber "+
			// trainNumber+" actual "+currentTrain.trainNo);

			if (simulatedTrainString.equalsIgnoreCase("TrainNo")
					&& currentTrain.trainNo == trainNumber) {
				return true;
			}
			GlobalVar.testCaseVerified = false;
			return false;
		} catch (IOException e) {
			System.out
					.println("Station: checkWhichTrainSimulated: IOException has occured");
			GlobalVar.testCaseVerified = false;
			return false;
		}

	}

	// Devendra
	/**
	 * Check if the right train is scheduled
	 * 
	 * @param newLoop
	 * @return true if the current train was simulated else false
	 */
	public static boolean checkWhichNewLoop(Loop newLoop) {
		try {
			StreamTokenizer outputFileReaderStreamTokenizer = GlobalVar.outputFileReaderStreamTokenizer;
			// read the "NewLoop" String
			outputFileReaderStreamTokenizer.nextToken();
			String NewLoopString = outputFileReaderStreamTokenizer.sval;

			// read the train number
			outputFileReaderStreamTokenizer.nextToken();
			int loopNumber = (int) outputFileReaderStreamTokenizer.nval;

			// System.out.println("new loop: "+NewLoopString+" loopNumber "+loopNumber+" actual "+newLoop.blockNo);

			if (NewLoopString.equalsIgnoreCase("NewLoop")
					&& newLoop.blockNo == loopNumber) {
				return true;
			}
			GlobalVar.testCaseVerified = false;
			return false;
		} catch (IOException e) {
			System.out
					.println("Station: checkWhichTrainSimulated: IOException has occured");
			GlobalVar.testCaseVerified = false;
			return false;
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Station a, Station b) {

		double c = a.startMilePost;
		double d = b.startMilePost;

		if (c > d) {
			return 1;
		}
		if (c < d) {
			return -1;
		}
		return 0;
	}

} // class ends

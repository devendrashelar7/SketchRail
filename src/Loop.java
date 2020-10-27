import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;

import util.Debug;

/**
 * Loop.
 */
public class Loop extends Block {
	/**
	 * crossMainLine
	 */
	int crossMainLine; /*
						 * 0 - no crossing 1- cross up main line (means crosses
						 * the 0 direction main line 2-cross down main line
						 * (means main line dir = 1)
						 */

	/**
	 * whetherMainLine
	 */
	int whetherMainLine; // Whether main line 0 -not main line 1- main line

	/**
	 * station
	 */
	Station station;
	/**
	 * stationName
	 */
	String stationName;

	/**
	 * constuctor.
	 */
	Loop() {
	}

	/**
	 * @param blkNo
	 * @param stnName
	 */
	Loop(int blkNo, String stnName) {// /////////////
		super(blkNo);
		this.stationName = stnName;// ///////////////////
	}

	/**
	 * @param dir
	 * @param crossMainLine
	 * @param mainLine
	 * @param len
	 */
	Loop(int dir, int crossMainLine, int mainLine, double len) {
		super(dir, len);
		this.crossMainLine = crossMainLine;
		this.whetherMainLine = mainLine;
	}

	/**
	 * @param dir
	 * @param crossMainLine
	 * @param mainLine
	 */
	Loop(int dir, int crossMainLine, int mainLine) {
		super(dir, 0);
		this.crossMainLine = crossMainLine;
		this.whetherMainLine = mainLine;
	}

	/**
	 * @param streamTokenizer
	 *            {@link StreamTokenizer}
	 * @throws IOException
	 * @throws SimException
	 */
	public Loop(StreamTokenizer streamTokenizer) throws IOException,
			SimException {

		Debug.print("Loop: constructor: ");

		if (streamTokenizer.ttype != StreamTokenizer.TT_NUMBER) {
			Debug
					.print("Loop: constructor: Error in format of input file : station.dat......");
			Debug.print("Loop: constructor: Error : loop number expected");
			throw new SimException();
		}

		Debug.print("Loop: constructor: loop number read is "
				+ streamTokenizer.nval);

		blockNo = ((int) streamTokenizer.nval);
		Debug.print("Loop: constructor: loop number is " + blockNo);

		GlobalVar.setMaxBlockNo(blockNo);

		streamTokenizer.nextToken();
		if (streamTokenizer.ttype != StreamTokenizer.TT_WORD) {
			Debug
					.print("Loop: constructor: Error in format of input file : station.dat......");
			Debug.print("Loop: constructor: Error : direction(0/1/2) expected");
			throw new SimException();
		}

		Debug.print("Loop: constructor: value read is " + streamTokenizer.sval);

		if (streamTokenizer.sval.equals("DOWN")) {
			direction = 1;
		} else {
			if (streamTokenizer.sval.equals("UP")) {
				direction = 0;
			} else {
				// Debug.assert(st.sval.equals("COMMON"),"Input either UP, DOWN or COMMON");
				direction = 2;
			}
		}

		streamTokenizer.nextToken();
		if (streamTokenizer.ttype != StreamTokenizer.TT_WORD) {
			Debug
					.print("Loop: constructor: Error in format of input file : station.dat......");
			Debug
					.print("Loop: constructor: Error : cross main line(0/1/2) expected");
			throw new SimException();
		}
		Debug.print("Loop: constructor: value read is " + streamTokenizer.sval);

		if (streamTokenizer.sval.equals("uml")) {
			crossMainLine = 1;
			whetherMainLine = 0;
		} else {
			if (streamTokenizer.sval.equals("dml")) {
				crossMainLine = 2;
				whetherMainLine = 0;
			} else {
				crossMainLine = 0;
				if (streamTokenizer.sval.equals("ml")) {
					whetherMainLine = 1;
				} else {
					// Debug.assert(st.sval.equals("loop"),"Input either ml or loop");
					whetherMainLine = 0;
				}
			}
		}

		streamTokenizer.nextToken();
		if (streamTokenizer.ttype != StreamTokenizer.TT_WORD) {
			Debug
					.print("Loop: constructor: Error in format of input file : station.dat......");
			Debug.print("Loop: constructor: Error : direction(0/1/2) expected");
			throw new SimException();
		}

		String strStn = streamTokenizer.sval;
		Debug.print("Loop: constructor: Station is " + strStn);
		Debug.print("Loop: constructor: value read is " + streamTokenizer.sval);

		streamTokenizer.nextToken();
		if (streamTokenizer.ttype != StreamTokenizer.TT_NUMBER) {
			Debug
					.print("Loop: constructor: Error in format of input file : station.dat......");
			Debug
					.print("Loop: constructor: Error : maximumPossibleSpeed expected");
			throw new SimException();
		}

		Debug.print("Loop: constructor: value read is " + streamTokenizer.nval);
		maximumPossibleSpeed = (streamTokenizer.nval / 60);
		Debug.print("Loop: constructor: " + maximumPossibleSpeed);

		
		streamTokenizer.nextToken();
		
		blockName = "Loop - " + blockNo;

		station = GlobalVar.getStation(strStn);
		stationName=station.stationName;

		// Debug.assert(stn!=null,"Error Station " + strStn + " not found");
		startMilePost = station.startMilePost;
		endMilePost = station.endMilePost;
		
		HashBlockEntry hbEntry = new HashBlockEntry();
		hbEntry.block = this;
		Loop currLoop = new Loop(blockNo, strStn);// ////////////////////
		GlobalVar.loopArrayList.add(currLoop);// ////////////////////////

		Debug.print("  hbEntry is " + hbEntry.block.blockNo);

		readCommonBlockLoop(streamTokenizer, hbEntry);

	}

	/**
	 * @param st
	 * @param isWarner
	 */
	public Loop(StreamTokenizer st, boolean isWarner) {
		warner = false;
	}

	/**
	 * @param time
	 * @return the number of the interval which contains the given time.
	 */
	public int inWhichInterval(double time) {
		return (occupy.inInterval(time));
	} // method isFree ends

	/**
	 * (non-Javadoc)
	 * 
	 * @see Block#waitingPermitted()
	 */
	public boolean waitingPermitted() {
		Debug.print("waiting is permitted ");
		return true;
	}

	/**
	 * Loop: StatusTraverseBlock traverseBlock((Train currTrain, double
	 * arrivalTime, double deptTime, double startVelocity) 1) creates an
	 * instance of the blockScheduler for this loop as the currentBlock and the
	 * currTrain 2) goes to blockScheduler.traverseBlock(arrivalTime, deptTime,
	 * startVelocity) (non-Javadoc)
	 * 
	 * @see Block#traverseBlock(Train, double, double, double)
	 */
	public StatusTraverseBlock traverseBlock(Train currTrain,
			double arrivalTime, double deptTime, double startVelocity) {

		BlockScheduler blockScheduler = new BlockScheduler(this, currTrain);
		Debug.print(" Once again in Loop.traverseblock  ");
		try {
			
			return blockScheduler.traverseBlock(arrivalTime, deptTime,
					startVelocity);
		} catch (Exception e) {
			e.printStackTrace();
			
			System.out.println("Loop: traverseBlock: Caught exception "
					+ arrivalTime + " " + deptTime + " " + startVelocity);
			System.exit(1);
		}
		return null;
	}

	/**
	 * Get the loop number of the next reference entry of a scheduled train.
	 * 
	 * @param referenceTables
	 * @return loop number of next reference entry. If there is no next entry -2
	 *         is returned.
	 */
	public int getNextReferenceLoopNumber(
			ArrayList<ReferenceTableEntry> referenceTables) {
		int i;
		boolean currentReferenceEntryFound = false;
		for (i = 0; i < referenceTables.size() && !currentReferenceEntryFound; i++) {
			ReferenceTableEntry referenceTableEntry = referenceTables.get(i);
			if (referenceTableEntry.refLoopNo == this.blockNo)
				currentReferenceEntryFound = true;
		}
		if (i == referenceTables.size())
			return -1;
		int loopNo = referenceTables.get(i).refLoopNo;
		return loopNo;

	}
} // class loop ends


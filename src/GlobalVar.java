import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import util.*;

/**
 * global variables class.
 */
class GlobalVar {

	/**
	 * This is a specific parameter used only for the 3-line case scenario...
	 * The values it can take are ALL ONLY_SUBURBAN, ONLY_LONG_DISTANCE,
	 * ONLY_FREIGHT, NOT_SUBURBAN, NOT_LONG_DISTANCE, NOT_FREIGHT
	 */
	public static String TRAIN_TYPE_ALLOWED_ON_THIRD_LINE = "ALL";
	/**
	 * redFailWaitTime
	 */
	public static int redFailWaitTime;
	/**
	 * redFailWaitNightTime
	 */
	public static int redFailWaitNightTime = 2;
	/**
	 * redFailVelocity
	 */
	public static double redFailVelocity;
	/**
	 * numberOfColour
	 */
	public static int numberOfColour;
	/**
	 * simulationTime
	 */
	public static int simulationTime;
	/**
	 * simulationType
	 */
	public static String simulationType = "normal";
	/**
	 * blockWorkingTime
	 */
	public static double blockWorkingTime;
	/**
	 * currentTrain
	 */
	public static Train currentTrain = null;
	/**
	 * pathParam
	 */
	public static String pathParam = "param.dat";
	/**
	 * pathBlock
	 */
	public static String pathBlock = "block.dat";
	/**
	 * pathLoop
	 */
	public static String pathLoop = "";
	/**
	 * pathStation
	 */
	public static String pathStation = "station.dat";
	/**
	 * pathUnscheduled
	 */
	public static String pathUnscheduled = "unScheduled.dat";
	/**
	 * pathScheduled
	 */
	public static String pathScheduled = "Scheduled.dat";
	/**
	 * pathGradient
	 */
	public static String pathGradient = "";
	/**
	 * pathGradientEffect
	 */
	public static String pathGradientEffect = "";
	// Devendra
	/**
	 * pathBlockDirectionInInterval
	 */
	public static String pathBlockDirectionInInterval = "";

	/**
	 * fileParam
	 */
	public static File fileParam = new File("");
	/**
	 * fileBlock
	 */
	public static File fileBlock = new File("");
	/**
	 * fileLoop
	 */
	public static File fileLoop = new File("");
	/**
	 * fileStation
	 */
	public static File fileStation = new File("");
	/**
	 * fileUnscheduled
	 */
	public static File fileUnscheduled = new File("");
	/**
	 * fileScheduled
	 */
	public static File fileScheduled = new File("");
	/**
	 * fileSignalFailure
	 */
	public static File fileSignalFailure = new File("");

	/**
	 * fileGradient
	 */
	public static File fileGradient = new File("");
	/**
	 * fileGradientEffect
	 */
	public static File fileGradientEffect = new File("");
	/**
	 * filePassDelay
	 */
	public static File filePassDelay = new File("");

	/**
	 * debugFile
	 */
	public static File debugFile = new File("debugFile.txt");

	// Devendra
	/**
	 * fileBlockDirectionInInterval
	 */
	public static File fileBlockDirectionInInterval = new File("");

	/**
	 * loopArrayList
	 */
	public static ArrayList<Loop> loopArrayList = new ArrayList<Loop>();

	/**
	 * stationArrayList
	 */
	public static ArrayList<Station> stationArrayList = new ArrayList<Station>();

	/**
	 * trainArrayList
	 */
	public static ArrayList<Train> trainArrayList = new ArrayList<Train>();

	/**
	 * oldtrainArrayList
	 */
	public static ArrayList<Train> oldtrainArrayList = new ArrayList<Train>();
	/**
	 * hashBlock
	 */
	public static Hashtable<Integer, HashBlockEntry> hashBlock = new Hashtable<Integer, HashBlockEntry>();

	/**
	 * delayArrayList
	 */
	public static ArrayList<PassengerDelayFormat> delayArrayList = new ArrayList<PassengerDelayFormat>();
	/**
	 * delayFactor
	 */
	public static int delayFactor = 0;
	/**
	 * delay
	 */
	public static int delay;
	/**
	 * flag
	 */
	public static boolean flag = false;// /mj

	/**
	 * testCaseDirectory. For verifying a particular testCase.
	 */
	// Devendra
	public static String testCaseDirectory;
	// Devendra
	/**
	 * testCaseVerified. If something wrong happens while verifying the output
	 * then this is set to false.
	 */
	public static boolean testCaseVerified = true;
	// Devendra
	/**
	 * verifyTestCases
	 */
	public static boolean verifyTestCases = false;
	// Devendra
	/**
	 * toleranceValue. To allow some tolerance for the current values output by
	 * simulator and the previously tested values
	 */
	public static double toleranceValue = 1.5;
	// Devendra
	/**
	 * outputFileReaderStreamTokenizer. To read the previously obtained values
	 * and the current values output by simulator
	 */
	public static StreamTokenizer outputFileReaderStreamTokenizer;

	/**
	 * overlapStartDistance
	 */
	public static double overlapStartDistance;
	/**
	 * overlapEndDistance
	 */
	public static double overlapEndDistance;
	/**
	 * overlap
	 */
	public static boolean overlap = false;

	/**
	 * sudhir
	 */
	public static double sudhir[] = new double[1000];
	public static int temp = 0;

	/**
	 * maxBlockNo
	 */
	public static int maxBlockNo = 0;
	/**
	 * minTime
	 */
	public static double minTime = 0;
	/**
	 * infiSmallTime
	 */
	public static double infiSmallTime = 0.001;
	/**
	 * interBlockTime
	 */
	public static double interBlockTime = 0;
	/**
	 * warnerDistance
	 */
	public static double warnerDistance = 0;
	/**
	 * freightSim
	 */
	public static FreightSim freightSim = null;
	/**
	 * 
	 */
	public static GraphFrame graphFrame = null;
	/**
	 * hasBlockDirectionFile
	 */
	public static boolean hasBlockDirectionFile = false;
	/**
	 * inst
	 */
	private static GlobalVar inst;

	/**
	 * private constructor.
	 */
	private GlobalVar() {
	}

	/**
	 * Reset the stationArrayList, trainArrayList and hashBlock.
	 */
	public static void reset() {
		stationArrayList = new ArrayList<Station>();
		trainArrayList = new ArrayList<Train>();
		hashBlock = new Hashtable<Integer, HashBlockEntry>();
	}

	/**
	 * @param distance
	 * @return {@link NextBlockArray} the array of blocks which are longer than
	 *         distance.
	 */
	public static NextBlockArray getBlock(double distance) {
		NextBlockArray blkArray = new NextBlockArray();

		for (Enumeration<HashBlockEntry> e = hashBlock.elements(); e.hasMoreElements();) {
			HashBlockEntry hbEntry = (HashBlockEntry) e.nextElement();
			Block currBlock = hbEntry.block;

			if ((currBlock.startMilePost <= distance)
					&& (currBlock.endMilePost >= distance)) {
				blkArray.add(currBlock);
			}
		}
		return blkArray;
	}

	public static Block getBlockFromBlockNo(int blockNo) {

		for (Enumeration<HashBlockEntry> e = hashBlock.elements(); e.hasMoreElements();) {
			HashBlockEntry hbEntry = (HashBlockEntry) e.nextElement();
			Block currBlock = hbEntry.block;

			if (currBlock.blockNo == blockNo) {
				return currBlock;
			}
		}
		return null;
	}

	/**
	 * @param blockNo
	 * @return the StationName corresponding to the blockNo
	 */
	public static String getStationName(int blockNo) {
		String s = null;
		for (int j = 0; j < GlobalVar.loopArrayList.size(); j++) {
			Loop loop = GlobalVar.loopArrayList.get(j);
			if (loop.blockNo == blockNo) {
				s = loop.stationName;
			}
		}
		return s;
	}

	/**
	 * @param no
	 */
	public static void setMaxBlockNo(int no) {
		maxBlockNo = (no > maxBlockNo) ? no : maxBlockNo;
	}

	/**
	 * @return the Next Block Number
	 */
	public static int getNextBlockNo() {
		maxBlockNo++;
		return maxBlockNo;
	}

	/**
	 * @param tm
	 * @return convert time in string to minutes
	 */
	public static double convertTimeMin(String tm) {
		StringTokenizer strTkTime = new StringTokenizer(tm, ":");
		int hr = Integer.parseInt(strTkTime.nextToken());
		int min = Integer.parseInt(strTkTime.nextToken());
		return (60 * hr) + min;
	}

	/**
	 * @param st
	 * @return a string from the stream tokenizer
	 */
	public static String readString(StreamTokenizer st) {

		if (st.ttype == StreamTokenizer.TT_WORD) {
			Debug.print("value Str read is " + st.sval);
			return st.sval;
		}

		if (st.ttype == StreamTokenizer.TT_NUMBER) {
			Debug.print("value numm read is " + st.nval);
			return Double.toString(st.nval);
		}
		return st.sval;
	}

	/**
	 * Convert time in long format to string format.
	 * 
	 * @param time
	 *            long
	 * @return time in string format.
	 */
	public static String timeToString(long time) {
		int hr = (int) Math.floor(time / 60);
		int min = (int) time % 60;
		if (min < 10) {
			return new String(hr + ":0" + min);
		}
		return new String(hr + ":" + min);
	}

	/**
	 * Convert time in double format to string format.
	 * 
	 * @param time
	 * @return time in string format.
	 */
	public static String timeToString(double time) {

		double tm = time;

		int TimeHr = (int) tm / 60;
		tm = (tm - TimeHr * 60) * 60;

		int TimeMin = (int) tm / 60;
		double TimeSec = tm - TimeMin * 60;

		String fstr;
		if (TimeHr < 10)
			fstr = "0" + TimeHr + ":";
		else
			fstr = TimeHr + ":";

		if (TimeMin < 10)
			fstr = fstr + "0" + TimeMin + ":";
		else
			fstr = fstr + TimeMin + ":";

		if (TimeSec < 10)
			fstr = fstr + "0" + (int) TimeSec;
		else
			fstr = fstr + (int) TimeSec;

		return fstr;

	}

	/**
	 * @param strStn
	 *            station's abbreviated name
	 * @return {@link Station} having the abbreviated stationName
	 */
	public static Station getStation(String strStn) {
		Station station;
		for (int i = 0; i < stationArrayList.size(); i++) {
			station = stationArrayList.get(i);

			if (strStn.equalsIgnoreCase(station.stationName)) {
				return station;
			}
		}
		return null;
	}

	/**
	 * @param blockNo
	 * @param dir
	 * @return get the endPost according to the direction of the block.
	 */

	public static double getLastEndmilepost(int blockNo, int dir) {
		double s = 0.0;
		for (Enumeration<HashBlockEntry> e = GlobalVar.hashBlock.elements(); e
				.hasMoreElements();) {
			HashBlockEntry hbEntry = (HashBlockEntry) e.nextElement();
			Block currBlock = hbEntry.block;
			if (currBlock.blockNo == blockNo) {

				if (dir == 0)
					s = currBlock.endMilePost;
				if (dir == 1)
					s = currBlock.startMilePost;
			}
		}

		// if the blockNo is -1, it means we have reached the end loop which is
		// not clearly specified, means that the train should be scheduled till
		// the end of the section no matter which loop
		// it may enter. In that case we require the last end mile post to be
		// the end of the section depending upon the direction of the train
		if (blockNo == -1) {
			ArrayList<Station> stationList = GlobalVar.stationArrayList;
			int size = stationList.size();
			Station endStation;
			if (dir == 0) {
				endStation = stationList.get(size - 1);
				// System.out.println(endStation.stationName);
				s = endStation.endMilePost;
			}
			if (dir == 1) {
				endStation = stationList.get(0);
				// System.out.println(endStation.stationName);
				s = endStation.startMilePost;
			}
		}
		return s;
	}

	/**
	 * @param trainNo
	 * @return Train having the corresponding trainNumber
	 */
	public static Train getTrain(int trainNo) {
		Train trn;
		for (int i = 0; i < trainArrayList.size(); i++) {
			trn = trainArrayList.get(i);
			if (trn.trainNo == trainNo) {
				return trn;
			}
		}
		return null;
	}

	/**
	 * @param trainNo
	 * @param time
	 * @param bu
	 *            block
	 * @return getTrainNew
	 */
	public static Train getTrainNew(int trainNo, double time, Block bu) {

		Train result = new Train();

		Debug.print(" Ia m in getTrainNew, time here is  " + time + " LLL "
				+ trainNo + " Block no is " + bu.blockNo);
		for (int i = 0; i < trainArrayList.size(); i++) {
			Train train = trainArrayList.get(i);
			if (train.trainNo == trainNo) {

				for (int j = 0; j < train.timeTables.size(); j++) {
					Debug.print(" nkfndf ");

					if (((train.timeTables.get(j)).loopNo == bu.blockNo)) {

						Debug.print("found block no " + bu.blockNo);
						if (((train.timeTables.get(j)).arrivalTime <= time)
								&& ((train.timeTables.get(j)).departureTime >= time)) {

							Debug.print(" found train " + train.trainNo
									+ " and its starting time is "
									+ train.startTime);

							Debug.print("Returing train is " + train.trainNo
									+ " and its starting time is "
									+ train.startTime);
							result = train;
						}

					}
				}
			}
		}
		return result;
	}

	/**
	 * @param a
	 * @param b
	 * @return new block array
	 */

	public static ArrayList<Block> returnBlockArrayNew(double a, double b) {
		Debug
				.print("GlobalVar: returnBlockArrayNew: In the for loop of ret_block_array of Global Var simstart");
		Debug.print("GlobalVar: returnBlockArrayNew: values passed here are  "
				+ a + "   " + b + "   ");
		ArrayList<Block> at = new ArrayList<Block>();

		Block biu = new Block();
		Block tu = new Block();

		for (Enumeration<HashBlockEntry> e = hashBlock.elements(); e
				.hasMoreElements();) {
			HashBlockEntry hbEntry = e.nextElement();
			biu = hbEntry.block;

			double startMilePost = biu.startMilePost;
			double endMilePost = biu.endMilePost;

			Debug
					.print("GlobalVar: returnBlockArrayNew:  Blk no is   "
							+ biu.blockNo + "  " + startMilePost + "    "
							+ endMilePost);
			if (((startMilePost >= a) && (endMilePost <= b))
					|| ((startMilePost <= a) && (endMilePost >= b))
					|| ((startMilePost >= a) && (endMilePost >= b) && (startMilePost < b))
					|| (((startMilePost <= a) && (endMilePost <= b) && (endMilePost > a)))) {
				tu = biu;
				Debug
						.print("GlobalVar: returnBlockArrayNew: this block no ahbjdbj  is "
								+ biu.blockNo
								+ "  "
								+ startMilePost
								+ "  "
								+ endMilePost);
				at.add(tu);
			}

		}
		Debug.print("GlobalVar: returnBlockArrayNew: Returning block  "
				+ tu.blockNo);

		return at;
	}

	/**
	 * @param signal
	 * @return returns the type of signal in words
	 */
	public static String getSignalColor(int signal) {
		if (signal == 0)
			return "red";
		if (GlobalVar.numberOfColour == 3) {
			if (signal == 1)
				return "yellow";
			if (signal == 2)
				return "green";
		} else if (GlobalVar.numberOfColour == 4) {
			if (signal == 1)
				return "double yellow";
			if (signal == 2)
				return "yellow";
			if (signal == 3)
				return "green";
		} else
			return "null";
		return "yo";
	}

	public static void initializeLogFiles(Level logLevel) {
		FileHandler handler = null;
		try {
			handler = new FileHandler("log/Block.log");
			Block.logger = Logger.getAnonymousLogger();
			Block.logger.addHandler(handler);
			Block.logger.setLevel(logLevel);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import util.Debug;

/**
 * Block class. represents the track section between two signals.
 */
public class Block {

	/**
	 * upFirstBlock The first block on the up direction that it is linked to.
	 */
	public static Block upFirstBlock;
	/**
	 * downFirstBlock the first block in the down direction that it is linked
	 * to.
	 */
	public static Block downFirstBlock;
	/**
	 * blockNo
	 */
	int blockNo;
	/**
	 * blockName
	 */
	String blockName;
	/**
	 * blockSignal
	 */
	Signal blockSignal;
	/**
	 * warner
	 */
	boolean warner;
	/**
	 * direction Direction of the block. Either up or down or bidirectional.
	 */
	public int direction;
	/**
	 * speedRestriction
	 */
	SpeedRestriction speedRestriction;
	/**
	 * sfsr SignalFailureSpeedRestriction
	 */
	TinyBlock sfsr;
	/**
	 * nightsfsr night SignalFailureSpeedRestriction
	 */
	TinyBlock nightsfsr;
	/**
	 * nextBlocks
	 */
	NextBlockArray nextBlocks;
	/**
	 * previousBlocks
	 */
	NextBlockArray previousBlocks;
	/**
	 * gradient
	 */
	Gradient gradient;
	/**
	 * tinyBlock
	 */
	TinyBlock tinyBlock;
	/**
	 * occupy
	 */
	IntervalArray occupy;
	/**
	 * signalFailure
	 */
	SignalFailure signalFailure;
	/**
	 * length of the block.
	 */
	public double length;
	/**
	 * maximumPossibleSpeed
	 */
	double maximumPossibleSpeed;
	/**
	 * startMilePost
	 */
	double startMilePost;
	/**
	 * endMilePost
	 */
	double endMilePost;
	/**
	 * signalFailFlag
	 */
	int signalFailFlag;

	// Devendra
	/**
	 * blockDirectionInInterval: maintains the timeChangeList at which the
	 * direction of block is changed
	 */
	BlockDirectionInInterval blockDirectionInInterval;

	/**
	 * hasPathVisited
	 */
	public boolean hasPathVisited;
	/**
	 * hasPathIsTrue
	 */
	public boolean hasPathIsTrue;
	public static Logger logger;

	/**
	 * constructor
	 */
	Block() {
		blockSignal = new Signal();
		warner = false;
		speedRestriction = new SpeedRestriction();
		sfsr = new TinyBlock();
		nightsfsr = new TinyBlock();
		nextBlocks = new NextBlockArray();
		previousBlocks = new NextBlockArray();
		gradient = new Gradient();
		tinyBlock = new TinyBlock();
		occupy = new IntervalArray();
		signalFailure = new SignalFailure();
		signalFailFlag = 0;
		blockDirectionInInterval = null;

		hasPathVisited = false;
		hasPathIsTrue = false;
	}

	/**
	 * @param i
	 */
	Block(int i) {
		blockSignal = new Signal();
		warner = false;
		speedRestriction = new SpeedRestriction();
		sfsr = new TinyBlock();
		nightsfsr = new TinyBlock();
		nextBlocks = new NextBlockArray();
		previousBlocks = new NextBlockArray();
		gradient = new Gradient();
		tinyBlock = new TinyBlock();
		occupy = new IntervalArray();
		signalFailure = new SignalFailure();
		signalFailFlag = 0;
		blockNo = i;
		blockDirectionInInterval = null;
	}

	/**
	 * @param doubleStartMilePost
	 * @param doubleEndMilePost
	 * @param d2
	 */
	Block(double doubleStartMilePost, double doubleEndMilePost, double maxSpeed) {
		blockSignal = new Signal();
		warner = false;
		speedRestriction = new SpeedRestriction();
		sfsr = new TinyBlock();
		nightsfsr = new TinyBlock();
		nextBlocks = new NextBlockArray();
		previousBlocks = new NextBlockArray();
		gradient = new Gradient();
		tinyBlock = new TinyBlock();
		occupy = new IntervalArray();
		signalFailure = new SignalFailure();
		signalFailFlag = 0;
		maximumPossibleSpeed = maxSpeed;
		startMilePost = doubleStartMilePost;
		endMilePost = doubleEndMilePost;
		length = doubleEndMilePost - doubleStartMilePost;

		blockDirectionInInterval = null;
	}

	/**
	 * @param i
	 * @param d
	 * @param d1
	 * @param doubleMaximumPossibleSpeed
	 */
	Block(int i, double d, double d1, double doubleMaximumPossibleSpeed) {
		blockSignal = new Signal();
		warner = false;
		speedRestriction = new SpeedRestriction();
		sfsr = new TinyBlock();
		nightsfsr = new TinyBlock();
		nextBlocks = new NextBlockArray();
		previousBlocks = new NextBlockArray();
		gradient = new Gradient();
		tinyBlock = new TinyBlock();
		occupy = new IntervalArray();
		signalFailure = new SignalFailure();
		signalFailFlag = 0;
		direction = i;
		startMilePost = d;
		endMilePost = d1;
		maximumPossibleSpeed = doubleMaximumPossibleSpeed;
		length = d1 - d;

		blockDirectionInInterval = null;
	}

	/**
	 * @param i
	 * @param d
	 */
	Block(int i, double d) {
		blockSignal = new Signal();
		warner = false;
		speedRestriction = new SpeedRestriction();
		sfsr = new TinyBlock();
		nightsfsr = new TinyBlock();
		nextBlocks = new NextBlockArray();
		previousBlocks = new NextBlockArray();
		gradient = new Gradient();
		tinyBlock = new TinyBlock();
		occupy = new IntervalArray();
		signalFailure = new SignalFailure();
		signalFailFlag = 0;
		direction = i;
		length = d;

		blockDirectionInInterval = null;
	}

	/**
	 * @param streamtokenizer
	 * @throws IOException
	 * @throws SimException
	 */
	public Block(StreamTokenizer streamtokenizer) throws IOException,
			SimException {

		/* initialization */
		blockSignal = new Signal();
		warner = false;
		speedRestriction = new SpeedRestriction();
		sfsr = new TinyBlock();
		nightsfsr = new TinyBlock();
		nextBlocks = new NextBlockArray();
		previousBlocks = new NextBlockArray();
		gradient = new Gradient();
		tinyBlock = new TinyBlock();
		occupy = new IntervalArray();
		signalFailure = new SignalFailure();
		signalFailFlag = 0;

		blockDirectionInInterval = null;

		Debug.print("Block constructor: Reading a new block");
		if (streamtokenizer.ttype != -2) {
			Debug
					.print("Block constructor: Error in format of input file : block.txt......");
			Debug.print("Block constructor: Error : Block no expected");
			throw new SimException();
		}
		Debug.print((new StringBuilder()).append("value read is ").append(
				streamtokenizer.nval).toString());

		blockNo = (int) streamtokenizer.nval;
		Debug.print((new StringBuilder()).append(" Block no is ").append(
				blockNo).toString());

		// System.out.println("Read block number "+blockNo);
		GlobalVar.setMaxBlockNo(blockNo);

		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -3) {
			Debug
					.print("Block constructor: Error in format of input file : block.txt......");
			Debug.print("Block constructor: Error : direction(0/1/2) expected");
			throw new SimException();
		}
		Debug.print((new StringBuilder()).append("value read is ").append(
				streamtokenizer.sval).toString());

		if (streamtokenizer.sval.equalsIgnoreCase("DOWN")) {
			direction = 1;
		} else if (streamtokenizer.sval.equalsIgnoreCase("UP")) {
			direction = 0;
		} else {
			direction = 2;
		}

		Debug.print((new StringBuilder()).append(" ").append(direction)
				.toString());

		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -2) {
			Debug
					.print("Block constructor: Error in format of input file : block.txt......");
			Debug.print("Block constructor: Error : Start milepost expected");
			throw new SimException();
		}

		Debug.print((new StringBuilder()).append("value read is ").append(
				streamtokenizer.nval).toString());
		startMilePost = streamtokenizer.nval;
		Debug.print((new StringBuilder()).append("  ").append(startMilePost)
				.toString());

		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -2) {
			Debug
					.print("Block constructor: Error in format of input file : block.txt......");
			Debug.print("Block constructor: Error : End milepost expected");
			throw new SimException();
		}

		Debug.print((new StringBuilder()).append("value read is ").append(
				streamtokenizer.nval).toString());
		endMilePost = streamtokenizer.nval;
		Debug.print((new StringBuilder()).append("  ").append(endMilePost)
				.toString());

		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -2) {
			Debug
					.print("Block constructor: Error in format of input file : block.txt......");
			Debug
					.print("Block constructor: Error : maximumPossibleSpeed  expected");
			throw new SimException();
		}

		Debug.print((new StringBuilder()).append("value read is ").append(
				streamtokenizer.nval).toString());
		maximumPossibleSpeed = streamtokenizer.nval / 60D;

		Debug.print((new StringBuilder()).append("  ").append(
				maximumPossibleSpeed).toString());
		streamtokenizer.nextToken();
		HashBlockEntry hashblockentry = new HashBlockEntry();
		hashblockentry.block = this;

		blockName = (new StringBuilder()).append("Block - ").append(blockNo)
				.toString();

		Debug.print((new StringBuilder()).append(" val; ").append(
				hashblockentry.block.blockNo).toString());
		readCommonBlockLoop(streamtokenizer, hashblockentry);

		return;
	}

	/**
	 * @param streamtokenizer
	 * @param hashblockentry
	 * @throws IOException
	 * @throws SimException
	 */
	
	public void readCommonBlockLoop(StreamTokenizer streamtokenizer,
			HashBlockEntry hashblockentry) throws IOException, SimException {

		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: Block passed is ").append(
				hashblockentry.block.blockNo).toString());

		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: value read is ").append(
				streamtokenizer.sval).toString());

		String s = streamtokenizer.sval;
		streamtokenizer.nextToken();
		String s2 = streamtokenizer.sval;

		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: value read is ").append(
				streamtokenizer.sval).toString());

		streamtokenizer.nextToken();
		String s1 = streamtokenizer.sval;

		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: value read is ").append(
				streamtokenizer.sval).toString());

		streamtokenizer.nextToken();
		String s3 = streamtokenizer.sval;
		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: value read is ").append(
				streamtokenizer.sval).toString());

		streamtokenizer.nextToken();

		Link.buildLink(0, s, s2, s1, s3, hashblockentry);
		s = streamtokenizer.sval;

		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: value read is ").append(
				streamtokenizer.sval).toString());
		streamtokenizer.nextToken();

		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: value read  jhfdh is ").append(
				streamtokenizer.sval).toString());
		s2 = streamtokenizer.sval;

		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: value read is ").append(
				streamtokenizer.sval).toString());
		streamtokenizer.nextToken();

		s1 = streamtokenizer.sval;
		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: value read is ").append(
				streamtokenizer.sval).toString());

		streamtokenizer.nextToken();
		s3 = streamtokenizer.sval;
		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: value read is ").append(
				streamtokenizer.sval).toString());

		streamtokenizer.nextToken();
		Link.buildLink(1, s, s2, s1, s3, hashblockentry);

		String s6 = streamtokenizer.sval;
		Debug
				.print((new StringBuilder())
						.append(
								"Block: readCommonBlockLoop: after link_buildLink: value read is ")
						.append(streamtokenizer.sval).toString());

		streamtokenizer.nextToken();
		String s4 = streamtokenizer.sval;
		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: value read is ").append(
				streamtokenizer.sval).toString());

		streamtokenizer.nextToken();
		String s5 = streamtokenizer.sval;
		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: value read is ").append(
				streamtokenizer.sval).toString());

		StringTokenizer stringtokenizer = new StringTokenizer(s6, ",");
		StringTokenizer stringtokenizer1 = new StringTokenizer(s4, ",");
		StringTokenizer stringtokenizer2 = new StringTokenizer(s5, ",");

		while (stringtokenizer.hasMoreTokens()) {

			String s7 = stringtokenizer.nextToken();
			if (!s7.equalsIgnoreCase("#")) {

				double d = Double.parseDouble(s7) / 60D;
				double d1 = Double.parseDouble(stringtokenizer1.nextToken());
				double d2 = Double.parseDouble(stringtokenizer2.nextToken());

				SpeedRestrictionFormat speedRestrictionFormat = new SpeedRestrictionFormat(
						d, d1, d2);
				Debug.print((new StringBuilder()).append(
						"Block: readCommonBlockLoop: Adding speedrestriction ")
						.append(d).append(" ").append(d1).append(" ")
						.append(d2).toString());
				speedRestriction.add(speedRestrictionFormat);
			}
		}

		Debug.print((new StringBuilder()).append(
				"Block: readCommonBlockLoop: Entering ").append(
				hashblockentry.block.blockNo).append("   into the hashtable ")
				.toString());

		GlobalVar.hashBlock.put(new Integer(hashblockentry.block.blockNo),
				hashblockentry);
	}

	/**
	 * @param streamtokenizer
	 * @return {@link SpeedRestrictionFormat}
	 * @throws IOException
	 * @throws SimException
	 */
	public SpeedRestrictionFormat readSpeedRes(StreamTokenizer streamtokenizer)
			throws IOException, SimException {
		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -2) {
			Debug.print("Error in format of input file : station.dat......");
			Debug.print("Error : max velocity in km/hr expected");
			throw new SimException();
		}
		Debug.print((new StringBuilder()).append("value read is ").append(
				streamtokenizer.nval).toString());
		double d = streamtokenizer.nval / 60D;
		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -2) {
			Debug.print("Error in format of input file : station.dat......");
			Debug.print("Error : from mile post expected");
			throw new SimException();
		}
		Debug.print((new StringBuilder()).append("value read is ").append(
				streamtokenizer.nval).toString());
		float f = (float) streamtokenizer.nval;
		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -2) {
			Debug.print("Error in format of input file : station.dat......");
			Debug.print("Error : to mile post expected");
			throw new SimException();
		}
		Debug.print((new StringBuilder()).append("value read is ").append(
				streamtokenizer.nval).toString());
		float f1 = (float) streamtokenizer.nval;
		return new SpeedRestrictionFormat(d, f, f1);
	}

	/**
	 * @param trainNo
	 * @param time1
	 * @param time2
	 */
	public void setOccupied(int trainNo, double time1, double time2) {
		Debug.print("Block: setOccupied: with 3 parameters(topaz_U)");

		occupy.add(new Interval(trainNo, time1, time2));
	}

	/**
	 * @param time1
	 * @param time2
	 */
	public void setOccupied(double time1, double time2) {
		Debug.print("Block: setOccupied: with 2 parameters(topaz_K)");
		occupy.add(new Interval(time1, time2));
	}

	/**
	 * @param d
	 * @return get Train number
	 */
	public int getTrainNo(double d) {
		int i = isFree(d);
		if (i != -1) {
			int j = occupy.get(i).trainNo;
			return j;
		}
		return -1;
	}

	/**
	 * @param d
	 * @param d1
	 * @param d2
	 * @param d3
	 * @param i
	 * @return true if block is free.
	 */
	public int isFree(double d, double d1, double d2, double d3, int i) {
		return occupy.inInterval(d, d1 + d3);
	}

	/**
	 * Consider the block occupancies and determine if the block is free during
	 * the interval specified by the startTime and endTime. If it is not free,
	 * return the ending time of the interval in which "startTime" lies. If the
	 * block is free, then check if the direction of the block, if specified by
	 * BlockdDirectionInInterval file does not change during the same interval.
	 * If it does change then return the ending time of the blockInterval in
	 * which startTime lies. If the direction of the block does not change
	 * during this interval then return -1.
	 * 
	 * @param startTime
	 * @param endTime
	 * @return time when the block is free.
	 */
	public double whenFree(double startTime, double endTime) {
		int i = isFree(startTime, endTime);
		if (i != -1) {
			return occupy.get(i).endTime;
		}

		// Devendra - to check if the block direction is compatible with train's
		// direction
		// during the interval
		if (this.blockDirectionInInterval != null) {
			if (this.blockDirectionInInterval.blockIntervalArray != null) {
				BlockInterval startTimeBlockInterval = this.blockDirectionInInterval
						.getBlockIntervalFromTime(startTime);
				BlockInterval endTimeBlockInterval = this.blockDirectionInInterval
						.getBlockIntervalFromTime(endTime);
				if (startTimeBlockInterval != endTimeBlockInterval) {
					return startTimeBlockInterval.endTime;
				}
			}
		}
		return -1D;
	}

	/**
	 * @param trainNo
	 * @param arrivalTime
	 * @param departureTime
	 */
	public void reserve(int trainNo, double arrivalTime, double departureTime) {
		Debug.print((new StringBuilder()).append("Block: reserve: TOPAZ-R-->")
				.append(trainNo).toString());

		setOccupied(trainNo, arrivalTime, departureTime);
	}

	/**
	 * @param startTime
	 * @param endTime
	 * @return isFree
	 */
	public int isFree(double startTime, double endTime) {
		return occupy.inInterval(startTime, endTime);
	}

	/**
	 * @param time
	 * @return isFree
	 */
	public int isFree(double time) {
		return occupy.inInterval(time);
	}

	/**
	 * @param i
	 * @return startMilePost depending upon the direction of the block.
	 */
	public double getStartMilePost(int i) {
		return i != 0 ? endMilePost : startMilePost;
	}

	/**
	 * @param i
	 * @return the endMilePost of the block depending upon its direction.
	 */
	public double getEndMilePost(int i) {
		return i != 0 ? startMilePost : endMilePost;
	}

	/**
	 * @param i
	 * @return endTime of interval
	 */
	public double getDepartureTime(int i) {
		for (int j = 0; j < occupy.size(); j++) {
			Interval interval = occupy.get(j);
			if (interval.trainNo == i) {
				return interval.endTime;
			}
		}

		return -1D;
	}

	/**
	 * @param trainDirection
	 * @return the nextBlocks or previousBlocks depending upon the direction of
	 *         the block
	 */
	public NextBlockArray getNextBlocks(int trainDirection) {
		if (trainDirection == 0) {
			return nextBlocks;
		}
		return previousBlocks;
	}

	/**
	 * @return true if waiting is permitted on the block. returns false as of
	 *         now.
	 */
	public boolean waitingPermitted() {
		return false;
	}

	/**
	 * 1) It finds a preferred loop or a block as per whether the currentblock
	 * has next block as a loop or block. 2) If the train is scheduled and if
	 * the currentBlock is a "block" it calls to see what is the type of next
	 * Block by calling getNextBlock(int trainDirection)
	 * 
	 * Block getNextBlock(int trainDirection): 1) Take the nextBlockArray and
	 * find the block amongst that for which the link Priority is one. 3) If the
	 * nextBlock is indeed a "block" it returns the block corresponding to the
	 * link which has priority one. 4) If the nextBlock is a "loop" infact then
	 * it finds the preferredLoop from the referenceTable since the train is
	 * scheduled. and returns that loop. 5) If the train is not scheduled or the
	 * currentBlock is a loop then it returns the nextMainBlock by calling the
	 * function getNextMainBlock(trainDirection).
	 * 
	 * @param trainDirection
	 * @param train
	 * @return get next block
	 */

	public Block getNextBlock(int trainDirection, Train train) {
		Block block = new Block();
		Debug.print((new StringBuilder()).append(
				"Block: getNextBlock: came in new funct bfor if.").append(
				getClass().getName()).toString());

		if (train.isScheduled && getClass().getName().equalsIgnoreCase("Block")
				&& blockNo != 0) {

			Debug.print((new StringBuilder()).append(
					"Block: getNextBlock: came in new funct.  ").append(
					getClass().getName()).toString());

			if (getNextBlock(trainDirection).getClass().getName()
					.equalsIgnoreCase("Block")) {
				NextBlockArray nextblockarray = getNextBlocks(trainDirection);
				block = nextblockarray.getNextMainBlock();
			} else {
				String nextStationName = ((Loop) getNextBlock(trainDirection)).station.stationName;
				ArrayList<ReferenceTableEntry> arraylist = train.refTables;
				int refTablesSize = train.refTables.size();
				int preferredLoop = 0;
				for (int l = 0; l < refTablesSize; l++) {
					if (((ReferenceTableEntry) arraylist.get(l)).stationName
							.equalsIgnoreCase(nextStationName)) {
						preferredLoop = train.refTables.get(l).refLoopNo;
					}
				}

				NextBlockArray nextblockarray2 = getNextBlocks(trainDirection);
				block = null;
				int i1 = 0;
				while (i1 < nextblockarray2.size()) {

					block = ((Link) nextblockarray2.get(i1)).nextBlock;
					if (block.blockNo == preferredLoop
							&& block.direction == trainDirection) {
						Debug.print((new StringBuilder()).append(
								"i m in if loop got preff loop  ").append(
								block.blockNo).toString());
						break;
					}
					i1++;
				}
			}
		} else {

			NextBlockArray nextblockarray1 = getNextBlocks(trainDirection);
			block = nextblockarray1.getNextMainBlock();
		}

		return block;
	}

	/**
	 * @param i
	 * @return get the nextMainBlock of the nextBlocks depending upon the
	 *         direction of the block.
	 */
	public Block getNextBlock(int i) {
		NextBlockArray nextblockarray = getNextBlocks(i);
		return nextblockarray.getNextMainBlock();
	}

	/**
	 * @param i
	 * @param j
	 * @return ?
	 */
	public boolean checkLastBlock(int i, int j) {
		NextBlockArray nextblockarray = getNextBlocks(i);
		// Block block = new Block();
		boolean flag = false;
		for (int k = 0; k < nextblockarray.size(); k++) {

			Debug.print("came to find last block in block array");
			if (((Link) nextblockarray.get(k)).nextBlockNo == j) {
				flag = true;
			}
		}

		return flag;
	}

	/**
	 * @param d
	 * @return true if block is not free.
	 */
	public boolean isSignalRed(double d) {
		return isFree(d) != -1;
	}

	/**
	 * @param block
	 * @param trainEndLoopNo
	 * @param trainDirection
	 * @return true if a block has a path to a given endLoop
	 */
	public boolean hasPathMemoizationMethod(Block block, int trainEndLoopNo,
			int trainDirection) {
		Block.resetHasPathVariables();
		boolean hasPath = block.hasPath(block, trainEndLoopNo, trainDirection);
		Block.resetHasPathVariables();

		return hasPath;
	}

	/**
	 * @param block
	 * @param trainEndLoopNo
	 * @param trainDirection
	 * @return true if hasPath
	 */
	public boolean hasPath(Block block, int trainEndLoopNo, int trainDirection) {
		// Block block1 = new Block();
		if (block.blockNo == trainEndLoopNo || trainEndLoopNo == -1)
			return true;
		if (block != null) {
			NextBlockArray nextblockarray = block.getNextBlocks(trainDirection);
			Debug.print((new StringBuilder()).append(block.blockNo).append(
					"-->").toString());

			for (int k = 0; k < nextblockarray.size(); k++) {
				Block block2 = ((Link) nextblockarray.get(k)).nextBlock;
				if (block2 == null) {
					continue;
				}

				// if the block has already been searched for a path till the
				// endLoop then return the result obtained in the initial
				// search.
				if (block2.hasPathVisited == true) {
					return hasPathIsTrue;
				}
				if (block2.blockNo == trainEndLoopNo) {

					Debug.print((new StringBuilder()).append(block2.blockNo)
							.append("-->").toString());
					Debug.print("yes");
					return true;
				}

				if (hasPath(block2, trainEndLoopNo, trainDirection)) {
					return true;
				}
			}
		}

		Debug.print("Block: hasPath: There is no path along this link");
		return false;
	}

	/**
	 * @param arrivalTime
	 * @param trainDirection
	 * @return earliest possible arrival time.
	 */
	public double getEarliestArrivalTime(double arrivalTime, int trainDirection) {

		int j = occupy.inInterval(arrivalTime);
		if (j != -1) {
			double newArrivalTime = occupy.get(j).endTime;

			// Devendra
			if (this.blockDirectionInInterval != null) {
				if (this.blockDirectionInInterval.isDirectionOk(trainDirection,
						newArrivalTime)) {
				} else {
					double timeFromBlockInterval = this.blockDirectionInInterval
							.getEarliestArrivalTimeFromBlockInterval(
									newArrivalTime, trainDirection);

					newArrivalTime = Math.max(newArrivalTime,
							timeFromBlockInterval);
					// System.out.println("Block: getEarliestArrivalTime: "
					// + newArrivalTime);
				}
			}

			return newArrivalTime;
		}
		return arrivalTime;
	}

	/**
	 * @param d
	 * @param d1
	 * @param i
	 * @return get the earliest possible arrival time.
	 */
	public double getEarliestArrivalTime(double d, double d1, int i) {
		int j = occupy.inInterval(d, d1);
		if (j != -1) {
			return occupy.get(j).endTime;
		}
		return d1;
	}

	/**
	 * @param train
	 * @param noOfColor
	 * @param arrivalTime
	 * @param direction
	 * @return signal.
	 */
	public int getSignal(Train train, int noOfColor, double arrivalTime,
			int direction) {
		return blockSignal.getSignal(this, train, noOfColor, arrivalTime,
				direction);
	}

	/**
	 * @param block
	 * @param train
	 * @return path
	 */
	public Block getPath(Block block, Train train) {
		Block block1 = new Block();
		Block block2 = this;
		block1.maximumPossibleSpeed = maximumPossibleSpeed;
		block1.startMilePost = startMilePost;
		block1.endMilePost = endMilePost;
		Debug.print((new StringBuilder()).append("SpeedRes ").append(
				maximumPossibleSpeed).append(" maximumPossibleSpeed   ")
				.append(block2.maximumPossibleSpeed).toString());
		for (int i = 0; i < GlobalVar.numberOfColour; i++) {
			double d = Math.min(block2.maximumPossibleSpeed,
					train.maximumPossibleSpeed);
			block1.speedRestriction.add(new SpeedRestrictionFormat(d,
					block2.startMilePost, block2.endMilePost));
			for (int j = 0; j < block2.speedRestriction.size(); j++) {
				if (block2.speedRestriction.get(j).svelo < d) {
					block1.speedRestriction.add(block2.speedRestriction.get(j));
					Debug
							.print((new StringBuilder()).append(
									"SpeedRes added  from block ").append(i)
									.toString());
				}
			}

			block1.endMilePost = block2.endMilePost;
			if (i == 0) {
				block2 = block;
			} else {
				block2 = block2.getNextBlock(train.direction, train);
			}
		}

		return block1;
	}

	/**
	 * Add the tiny blocks of the blocks from the startBlock till the signal
	 * value number of the blocks into the emptyBlock.
	 * 
	 * @param emptyBlock
	 * @param startBlock
	 * @param train
	 * @param signal
	 */
	public void addTinyBlocksToBlock(Block emptyBlock, Block startBlock,
			Train train, int signal) {
		Block iterBlock = startBlock;
		for (int j = 0; j < signal; j++) {

			if (iterBlock.signalFailFlag == 0) {

				emptyBlock
						.addTinyBlocksToEmptyBlock(iterBlock.tinyBlock.tinyBlockArray);
			} else if (iterBlock.signalFailFlag == 1) {
				Debug
						.print("Block: getRunTimeSignal: TRAIN ENTERS SIGNAL FAILED BLOCK IN day TIME ");

				emptyBlock
						.addTinyBlocksToEmptyBlock(iterBlock.sfsr.tinyBlockArray);
			} else if (iterBlock.signalFailFlag == 2) {
				Debug
						.print("Block: getRunTimeSignal: TRAIN ENTERS SIGNAL FAIELD BLOCK IN NIGHT TIME ");

				emptyBlock
						.addTinyBlocksToEmptyBlock(iterBlock.nightsfsr.tinyBlockArray);
			}
			if (train.direction == 0) {
				emptyBlock.endMilePost = iterBlock.endMilePost;
			} else {
				emptyBlock.startMilePost = iterBlock.startMilePost;
			}

			iterBlock = iterBlock.getNextBlock(train.direction, train);
		}

	}

	/**
	 * 1) It does some signal handling. with signalFailFlag = 0 , 1, 2 which
	 * respectively means that the signal has not failed, that the signal has
	 * failed in a daytime and that the signal has failed in the night time. So,
	 * it respectively adds the tinyblocks of the SpeedRestrictions or
	 * SignalFailedSpeedRestrictions or NightSignalFailedSpeedRestrictions 2) It
	 * does that for all the blocks that it can find till the signal (i.e. the
	 * number of blocks that are free to go at once) by updating the block to
	 * getNextBlock(train.direction,train) 3) It then creates an instance of
	 * velocityProfileArray for the tinyBlockArrays obtained by calling to the
	 * constructor VelocityProfileArray(train, block, startVelocity1,
	 * endVelocity1) 4) After having constructed the velocityProfileArray, it
	 * creates another instance of VelocityProfileArray namely
	 * velocityProfileArray1 which is a velocityProfileArray for the specific
	 * block that we are considering. If the GlobalVar.overlap is false:
	 * velocityProfileArray1 = velocityprofilearray.returnInterval(
	 * startMilePost, endMilePost); 5) The blockVelocityProfileArray, thus
	 * returned is given to a RunTimeReturn constructor which notes the time for
	 * the block and the velocity with the train enters the block and the
	 * velocityProfileArray1 (i.e. the blockVelocityProfileArray), which can be
	 * used for further use.
	 * 
	 * @param train
	 * @param signal
	 * @param d
	 * @param startVelocity1
	 * @param endVelocity1
	 * @return runTimeSignal... totalTime is -1 if the velocityProfileArray has
	 *         no velocity profile in it. Its an exceptional case.
	 */
	public RunTimeReturn getRunTimeSignal(Train train, int signal, double d,
			double startVelocity1, double endVelocity1) {
		Debug.print("Block: getRunTimeSignal: ");

		Block block = new Block();
		block.maximumPossibleSpeed = maximumPossibleSpeed;
		block.startMilePost = startMilePost;
		block.endMilePost = endMilePost;
		block.blockNo = blockNo;

		addTinyBlocksToBlock(block, this, train, signal);
		 System.out.println("added blocks");

		 for (int i = 0; i < block.tinyBlock.tinyBlockArray.size(); i++) {
		 TinyBlockFormat tbf = block.tinyBlock.tinyBlockArray.get(i);
		 System.out.println(tbf.startMilePost + "\t" + tbf.endMilePost
		 + "\t" + tbf.maxSpeed + "\t" + tbf.accelerationChange
		 + "\t" + tbf.decelerationChange);
		 }

		 System.out.println("gRTS: speed " + maximumPossibleSpeed + " sMP "
		 + startMilePost + " eMP " + endMilePost);

		// generates the velocity profile of the train considering the various
		// tiny blocks and acceleration and deceleration parts of the profile
		// System.out.println("block "+block.blockNo);
		// System.out.println("Calling velocityProfile: startVelocity "
		// + startVelocity1 + " endVelocity " + endVelocity1
		// + " startMilePost " + block.startMilePost + " endMilePost "
		// + block.endMilePost);
		VelocityProfileArray velocityProfileArray = new VelocityProfileArray(
				train, block, startVelocity1, endVelocity1);

		Debug.print("Block: getRunTimeSignal: Out of VelocityProfileArray ");

		if (0 == velocityProfileArray.size()) {
			System.out.println("velocity profile array size is zero  ");
			return new RunTimeReturn(-1D, -1D, null);
		}

		// boolean flag = false;

		if (GlobalVar.overlap) {
			return getOverlappingRunTimeReturn(train, velocityProfileArray);
		} else {
			return getNonOverlappingRunTimeReturn(train, velocityProfileArray);
		}
	}

	/**
	 * @param train
	 * @param velocityProfileArray
	 * @return {@link RunTimeReturn} from the beginning of the block till only
	 *         the end of the block and not the overlapping part of the train
	 *         when the train just exits the block.
	 */
	public RunTimeReturn getNonOverlappingRunTimeReturn(Train train,
			VelocityProfileArray velocityProfileArray) {
		VelocityProfileArray nonOverlappingProfileArray = velocityProfileArray
				.returnInterval(startMilePost, endMilePost);

		if (nonOverlappingProfileArray == null)
			throw new NullPointerException("nonOverlappingProfileArray is null");

		double actualStartVelocity = nonOverlappingProfileArray
				.getEndVelocity(train);

		return new RunTimeReturn(nonOverlappingProfileArray.runTime,
				actualStartVelocity, nonOverlappingProfileArray);

	}

	/**
	 * Get the {@link RunTimeReturn} for the block and the overlapping part of
	 * the train after it just exits the block.
	 * 
	 * @param train
	 * @param velocityprofilearray
	 * @return {@link RunTimeReturn} from the beginning of the block till the
	 *         overlapping part of the train
	 */
	public RunTimeReturn getOverlappingRunTimeReturn(Train train,
			VelocityProfileArray velocityprofilearray) {
		VelocityProfileArray overlappingProfileArray;
		if (train.direction == 0) {
			overlappingProfileArray = velocityprofilearray.returnInterval(
					GlobalVar.overlapStartDistance,
					GlobalVar.overlapEndDistance);
		} else {// train.direction == 1
			overlappingProfileArray = velocityprofilearray.returnInterval(
					GlobalVar.overlapEndDistance,
					GlobalVar.overlapStartDistance);
		}
		if (overlappingProfileArray == null) {
			throw new NullPointerException("overlappingProfileArray is null");
		}

		return new RunTimeReturn(overlappingProfileArray.runTime, 0.0D,
				overlappingProfileArray);

	}

	/**
	 * @param train
	 * @param signal
	 * @param arrivalTime
	 * @param nextBlock
	 * @param runtimereturn
	 * @param finalVelocity
	 * @return calculateRecurrenceVelocity
	 */
	public RunTimeReturn calculateWarnerVelocityProfile(Train train,
			int signal, double arrivalTime, Block nextBlock,
			RunTimeReturn runtimereturn, double finalVelocity) {

		double tempArrivalTime = arrivalTime;

		Debug.print(" CAME TO CALCULATE VELOCITY PROFILE FOR WARNER BLOCK ");

		if (Math.abs(endMilePost - startMilePost) <= GlobalVar.warnerDistance) {
			return runtimereturn;
		}

		Debug.print("Calculating recurrence ");
		// runTimeReturnVelocityProfileArray abbreviation
		VelocityProfileArray originalProfile = runtimereturn.velocityProfileArray;

		// We are not completely sure about the signal. So we should try and
		// come to a halt before the signal.
		double startVelocity = originalProfile.getStartVelocity(train);
		originalProfile = new VelocityProfileArray(train, this, startVelocity,
				0);

		System.out.println("Printing original profile &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ");
		originalProfile.print(train.direction);
		
		tempArrivalTime += originalProfile.runTime;

		Block largeBlock = new Block();
		largeBlock.maximumPossibleSpeed = maximumPossibleSpeed;
		largeBlock.nextBlocks = nextBlocks;
		largeBlock.previousBlocks = previousBlocks;

		// System.out.println("largeBlock startMilePost "
		// + largeBlock.startMilePost);
		// abbreviation for Warner Distance till endMilePost
		VelocityProfileArray warnerProfile = null;

		// if (train.direction == 0) {
		// Debug.print("train direction is zero  ");
		//
		// warnerProfile = originalProfile.returnInterval(endMilePost
		// - GlobalVar.warnerDistance, endMilePost);
		//
		// originalProfile = originalProfile.returnInterval(startMilePost,
		// endMilePost
		// - GlobalVar.warnerDistance);
		//
		// warnerBlockStartVelocity =
		// (originalProfile.get(originalProfile.size() -
		// 1)).endVelocity;
		//
		// block1.startMilePost = endMilePost - GlobalVar.warnerDistance;
		// block1.endMilePost = endMilePost;
		// block1.tinyBlock = (TinyBlock) tinyBlock.split(
		// endMilePost - GlobalVar.warnerDistance).get(1);
		// } else {
		// Debug.print("train direction is not zero ");
		// warnerProfile = originalProfile.returnInterval(startMilePost,
		// startMilePost
		// + GlobalVar.warnerDistance);
		// originalProfile = originalProfile.returnInterval(startMilePost
		// + GlobalVar.warnerDistance, endMilePost);
		// if (originalProfile.size() != 0) {
		// warnerBlockStartVelocity =
		// (originalProfile.get(originalProfile.size() -
		// 1)).startVelocity;
		// } else {
		// warnerBlockStartVelocity = 0.0;
		// }
		//
		// block1.startMilePost = startMilePost;
		// block1.endMilePost = startMilePost + GlobalVar.warnerDistance;
		// block1.tinyBlock = (TinyBlock) tinyBlock.split(
		// startMilePost + GlobalVar.warnerDistance).get(0);
		// }

		SplitProfileForWarner splitProfileForWarner = splitProfileForWarnerDistance(
				train, originalProfile, largeBlock);

		warnerProfile = splitProfileForWarner.warnerProfile;
		double warnerBlockStartVelocity = splitProfileForWarner.warnerBlockStartVelocity;

		tempArrivalTime -= warnerProfile.runTime;
		Debug.print("Calculating sIGNAL going to nextblk.getSignal() ");

		int nextBlockSignal = nextBlock.getSignal(train,
				GlobalVar.numberOfColour, tempArrivalTime, train.direction);

		// System.out
		// .println("Returned from getSignal to calculateWarnerVelocityProfile");

		Debug.print("Came back from   --->>>   nextblk.getSignal() ");
		// System.out.println("signal " + signal + " nextBlockSignal "
		// + nextBlockSignal);

		if (signal > nextBlockSignal) {
			return runtimereturn;
		}

		Debug.print((new StringBuilder()).append(" Just B4 first for loop ")
				.append(signal).toString());

		Block blockIter = nextBlock;
		for (int l = 0; l < signal; l++) {

			if (train.direction == 0) {
				largeBlock.endMilePost = blockIter.endMilePost;
			} else {
				largeBlock.startMilePost = blockIter.startMilePost;
			}
			blockIter = blockIter.getNextBlock(train.direction, train);
		}

		Debug.print((new StringBuilder()).append(
				"  Going to calculate warner signal with ").append(
				warnerBlockStartVelocity).append("  ").append(finalVelocity)
				.toString());

		// abbreviation for WarnerDistanceToSignalFreeBlocks
		// System.out.println("calculateWarnerDistance: compute velocityProfile");
		VelocityProfileArray WD_to_SFB = new VelocityProfileArray(train,
				largeBlock, warnerBlockStartVelocity, finalVelocity);

		Debug
				.print("came back after calculating the velocity profile for the warner signal block");
		Debug.print(" profile under consideration is ");

		if (0 == WD_to_SFB.size()) {
			return new RunTimeReturn(-1D, -1D, null);
		}

		Debug
				.print("going to/find the running time required for this section ");

		// boolean flag = false;
		double d4;
		if (train.direction == 0) {
			Debug.print("Train direction is up");
			warnerProfile = WD_to_SFB.returnInterval(endMilePost
					- GlobalVar.warnerDistance, endMilePost);
			d4 = (warnerProfile.get(warnerProfile.size() - 1)).endVelocity;
			// originalProfile.remove(originalProfile.)
		} else {
			Debug.print("Train direction is down");
			warnerProfile = WD_to_SFB.returnInterval(startMilePost,
					startMilePost + GlobalVar.warnerDistance);
			d4 = (warnerProfile.get(0)).startVelocity;
		}

		Debug
				.print("after calculating the velocity profile for the warner signal block ");
		// boolean flag1 = true;
		originalProfile.clear();
		VelocityProfileArray beforeWarnerProfile = splitProfileForWarner.majorProfile;
		originalProfile.addAll(beforeWarnerProfile);
		originalProfile.addAll(warnerProfile);
		originalProfile.runTime = beforeWarnerProfile.runTime
				+ warnerProfile.runTime;

		return new RunTimeReturn(originalProfile.runTime, d4, originalProfile);
	}

	/**
	 * Split the profile of the block into two profiles one for the warner
	 * distance and other for the rest of the block. In the end return the
	 * startVelocity of the warner profile.
	 * 
	 * @param train
	 * @param originalProfile
	 * @param largeBlock
	 * @return the start velocity of the warner profile.
	 */
	public SplitProfileForWarner splitProfileForWarnerDistance(Train train,

	VelocityProfileArray originalProfile, Block largeBlock) {

		SplitProfileForWarner splitProfileForWarner = new SplitProfileForWarner();
		splitProfileForWarner.largeBlock = largeBlock;
		splitProfileForWarner.originalProfile = originalProfile;

		VelocityProfileArray majorProfile = splitProfileForWarner.majorProfile;

		double warnerBlockStartVelocity;
		if (train.direction == 0) {
			Debug.print("train direction is zero  ");

			splitProfileForWarner.warnerProfile = originalProfile
					.returnInterval(endMilePost - GlobalVar.warnerDistance,
							endMilePost);

			majorProfile = originalProfile.returnInterval(startMilePost,
					endMilePost - GlobalVar.warnerDistance);

			warnerBlockStartVelocity = (majorProfile
					.get(majorProfile.size() - 1)).endVelocity;

			largeBlock.startMilePost = endMilePost - GlobalVar.warnerDistance;
			largeBlock.endMilePost = endMilePost;
			largeBlock.tinyBlock = (TinyBlock) tinyBlock.split(
					endMilePost - GlobalVar.warnerDistance).get(1);
		} else {
			Debug.print("train direction is not zero ");
			splitProfileForWarner.warnerProfile = originalProfile
					.returnInterval(startMilePost, startMilePost
							+ GlobalVar.warnerDistance);
			majorProfile = originalProfile.returnInterval(startMilePost
					+ GlobalVar.warnerDistance, endMilePost);
			if (majorProfile.size() != 0) {
				warnerBlockStartVelocity = (majorProfile.get(majorProfile
						.size() - 1)).startVelocity;
			} else {
				warnerBlockStartVelocity = 0.0;
			}

			largeBlock.startMilePost = startMilePost;
			largeBlock.endMilePost = startMilePost + GlobalVar.warnerDistance;
			largeBlock.tinyBlock = (TinyBlock) tinyBlock.split(
					startMilePost + GlobalVar.warnerDistance).get(0);
		}

		splitProfileForWarner.majorProfile = majorProfile;
		splitProfileForWarner.warnerBlockStartVelocity = warnerBlockStartVelocity;
		return splitProfileForWarner;
	}

	/**
	 * @param train
	 * @param signal
	 * @param arrivalTime
	 * @param startVelocity
	 * @return runTimeSignal
	 */
	public RunTimeReturn getRunTimeSignal(Train train, int signal,
			double arrivalTime, double startVelocity) {
		return getRunTimeSignal(train, signal, arrivalTime, startVelocity, 0.0D);
	}

	/**
	 * @param train
	 * @param d
	 * @param d1
	 * @return getRunTimeVelocity
	 */
	public RunTimeReturn getRunTimeVelocity(Train train, double d, double d1) {
		Block block = new Block();
		block.startMilePost = startMilePost;
		block.endMilePost = endMilePost;
		double d2 = maximumPossibleSpeed <= train.maximumPossibleSpeed ? maximumPossibleSpeed
				: train.maximumPossibleSpeed;
		d2 = d <= d2 ? d : d2;
		block.speedRestriction.add(new SpeedRestrictionFormat(d2,
				startMilePost, endMilePost));
		for (int i = 0; i < speedRestriction.size(); i++) {
			if (speedRestriction.get(i).svelo < d2) {
				block.speedRestriction.add(speedRestriction.get(i));
			}
		}

		// System.out.println("getRunTimeVelocity: compute velocityProfile");
		VelocityProfileArray velocityprofilearray = new VelocityProfileArray(
				train, block, 0.0D, 0.0D);
		return new RunTimeReturn(velocityprofilearray.runTime, 0.0D,
				velocityprofilearray);
	}

	/**
	 * @param train
	 * @param arrivalTime
	 * @param departureTime
	 * @param startVelocity
	 * @return {@link StatusTraverseBlock}
	 */
	public StatusTraverseBlock traverseBlock(Train train, double arrivalTime,
			double departureTime, double startVelocity) {
		SchedulerFactory schedulerfactory = new SchedulerFactory();
		BlockScheduler blockscheduler = schedulerfactory.getScheduler(
				GlobalVar.simulationType, this, train);
		if (blockscheduler == null) {
			Debug.print("I have not born");
			throw new NullPointerException(
					"traverseBlock: blockScheduler is null.");
		}
		Debug.print("Block: StatusTraverseBlock: returning.");
		return blockscheduler.traverseBlock(arrivalTime, departureTime,
				startVelocity);
	}

	/**
	 * @return true if the block is a loop
	 */
	public boolean isLoop() {
		return this.getClass().getName().equalsIgnoreCase("Loop");
	}

	/**
	 * @param time
	 * @return false if no signal failure is found. Else return true.
	 */
	public boolean isSignalFailed(double time) {
		Debug.print((new StringBuilder()).append("In signalFailed function   ")
				.append(time).toString());

		int i = 0;
		for (int j = 0; j < signalFailure.arraySignalFailure.size(); j++) {

			SignalFailureFormat signalfailureformat = signalFailure.arraySignalFailure
					.get(j);
			if (time >= signalfailureformat.start
					&& time <= signalfailureformat.end) {
				return true;
				// i = 1;
				// i = j+1;// Devendra Made a correction here which should
				// probably
				// // be right.
			}
		}

		Debug.print((new StringBuilder()).append(" Returning ").append(i)
				.append("from signal_failed function ").toString());
		return false;
	}

	/**
	 * @param train
	 * @param arrivalTime
	 * @return true if the block direction is same as the train direction at the
	 *         given arrival time
	 */
	public boolean isDirectionOk(Train train, double arrivalTime) {

		// block direction not specified at all
		if (this.blockDirectionInInterval == null)
			return (this.direction == train.direction || this.direction == 2);

		return this.blockDirectionInInterval.isDirectionOk(train.direction,
				arrivalTime);
	}

	/**
	 * reset the hasPath variables to false for hasPath memoization search.
	 */
	public static void resetHasPathVariables() {
		for (Enumeration<HashBlockEntry> e = GlobalVar.hashBlock.elements(); e
				.hasMoreElements();) {
			HashBlockEntry hbEntry = e.nextElement();
			hbEntry.block.hasPathIsTrue = false;
			hbEntry.block.hasPathVisited = false;
		}
	}

	/**
	 * @param tinyBlockArray
	 */
	public void addTinyBlocksToEmptyBlock(
			ArrayList<TinyBlockFormat> tinyBlockArray) {
		if (tinyBlockArray == null) {
			throw new NullPointerException(
					"addTinyBlocksToEmptyBlock: tinyBlockArray passed is null");
		} else {
			for (TinyBlockFormat tinyBlockFormat : tinyBlockArray) {
				this.tinyBlock.add(tinyBlockFormat);
			}
		}
	}

	//This function is made for a specific case when the section has three lines.
	public boolean allowsTrain(Train currTrain, double arrivalTime) {
		// TODO Auto-generated method stub
		if (this.blockDirectionInInterval != null) {//is a block on the third line
			if (currTrain.isSuburban()
					&& GlobalVar.TRAIN_TYPE_ALLOWED_ON_THIRD_LINE
							.equalsIgnoreCase("ONLY_LONG_DISTANCE"))
				return false;
			else if (currTrain.isScheduled
					&& GlobalVar.TRAIN_TYPE_ALLOWED_ON_THIRD_LINE
							.equalsIgnoreCase("ONLY_SUBURBAN"))
				return false;
		}
		return true;
	}
}

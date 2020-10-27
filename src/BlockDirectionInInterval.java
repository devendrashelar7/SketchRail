import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import util.Debug;

/**
 * This class is to read the list of time instances when the block direction is
 * reversed.
 * */
public class BlockDirectionInInterval {

	/**
	 * timeOfDirectionSwitching list of time instances at which the direction of
	 * the block is changed. We always consider the first time instance
	 * indicates the change of direction from UP to DOWN.
	 */
	public ArrayList<BlockInterval> blockIntervalArray;

	/**
	 * 
	 */
	public BlockDirectionInInterval() {
		blockIntervalArray = new ArrayList<BlockInterval>();
	}

	/**
	 * For reading the BlockDirectionInInterval file
	 * 
	 * @throws IOException
	 */
	public static void readBlockDirectionInIntervalFile() throws IOException {

		Reader reader;

		// Devendra
		if (simStart.nogui) {
			reader = new FileReader(GlobalVar.pathBlockDirectionInInterval);
		} else {
			reader = new FileReader(GlobalVar.fileBlockDirectionInInterval);
		}

		StreamTokenizer streamTokenizer = new StreamTokenizer(reader);

		streamTokenizer.slashSlashComments(false);

		// debug
		String debugCommentStart = "BlockDirectionInInterval: readBlockDirectionInIntervalFile: ";

		while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {

			// read the block number
			double blockValue = streamTokenizer.nval;
			int blockNumber = (int) blockValue;

			Debug.print(debugCommentStart + "blockNumber " + blockNumber);

			// read the string of list of startTimes of BlockIntervals
			streamTokenizer.nextToken();
			String startTimeListString = streamTokenizer.sval;

			// read the string of list of endTimes of BlockIntervals
			streamTokenizer.nextToken();
			String endTimeListString = streamTokenizer.sval;

			// read the string of list of directions of BlockIntervals
			streamTokenizer.nextToken();
			String directionListString = streamTokenizer.sval;

			Debug.print(debugCommentStart + directionListString + " "
					+ streamTokenizer.ttype);

			// insert the timeChangeList into the above block
			addBlockDirectionInIntervalToBlock(blockNumber,
					startTimeListString, endTimeListString, directionListString);
		}
	}

	/**
	 * Add the list of timeChanges of direction switching to the block
	 * 
	 * @param blockNumber
	 * @param startTimeListString
	 * @param endTimeListString
	 * @param directionString
	 */
	public static void addBlockDirectionInIntervalToBlock(int blockNumber,
			String startTimeListString, String endTimeListString,
			String directionString) {

		String debugCommentStart = "BlockDirectionInInterval: addBlockDirectionInInterval: ";
		StringTokenizer startTimeStringTokenizer = new StringTokenizer(
				startTimeListString, ",");
		StringTokenizer endTimeStringTokenizer = new StringTokenizer(
				endTimeListString, ",");
		StringTokenizer directionStringTokenizer = new StringTokenizer(
				directionString, ",");

		// find the block
		HashBlockEntry blockEntry = GlobalVar.hashBlock.get(blockNumber);
		if (blockEntry == null) {
			throw new NullPointerException(debugCommentStart
					+ "blockEntry is null");
		}

		Block block = blockEntry.block;

		// instantiate BlockDirectionInInterval
		BlockDirectionInInterval blockDirectionInInterval = new BlockDirectionInInterval();
		ArrayList<BlockInterval> blockIntervalArray = blockDirectionInInterval.blockIntervalArray;

		// read the time changes
		while (startTimeStringTokenizer.hasMoreTokens()) {

			// read the startTime value
			String value = startTimeStringTokenizer.nextToken();
			double startTime = Double.parseDouble(value);

			// read the endTime value
			value = endTimeStringTokenizer.nextToken();
			double endTime = Double.parseDouble(value);

			// read the direction value
			String direction = directionStringTokenizer.nextToken();
			if (direction.equalsIgnoreCase("1")) {
				direction = "down";
			} else {
				direction = "up";
			}

			BlockInterval blockInterval = new BlockInterval(startTime, endTime,
					direction);

			// insert
			blockIntervalArray.add(blockInterval);
		}

		block.blockDirectionInInterval = blockDirectionInInterval;
	}

	// Devendra
	/**
	 * Check if train direction is same as block direction at a given time
	 * 
	 * @param trainDirection
	 * @param arrivalTime
	 * @return true if train direction is same as block direction
	 */
	public boolean isDirectionOk(int trainDirection, double arrivalTime) {

		BlockInterval blockInterval = this
				.getBlockIntervalFromTime(arrivalTime);
		if (blockInterval == null) {
			// direction not specified hence is common at arrival time. So true.
			return true;
		}

		// arrival time is in the blockInterval
		if (trainDirection == 0
				&& blockInterval.direction.equalsIgnoreCase("up")) {

			// train direction is up and counter is even
			return true;
		} else if (trainDirection == 1
				&& blockInterval.direction.equalsIgnoreCase("down")) {

			// train direction is down and counter
			return true;
		}

		// block direction explicitly specified and is not the same as train
		// direction
		return false;

	}

	/**
	 * @param time
	 * @return the blockInterval in which the time lies. If there is no such
	 *         blockInterval return null.
	 */
	public BlockInterval getBlockIntervalFromTime(double time) {
		for (int index = 0; index < this.blockIntervalArray.size(); index++) {
			BlockInterval blockInterval = this.blockIntervalArray.get(index);
			if (blockInterval.isTimeInInterval(time)) {
				return blockInterval;
			}
		}
		return null;
	}

	/**
	 * @param arrivalTime
	 * @param direction
	 * @return the earliest time in which the train's direction is not
	 *         conflicting with the block's direction
	 */
	public double getEarliestArrivalTimeFromBlockInterval(double arrivalTime,
			int direction) {

		for (int index = 0; index < this.blockIntervalArray.size(); index++) {
			BlockInterval blockInterval = this.blockIntervalArray.get(index);
			if (blockInterval.isTimeInInterval(arrivalTime)) {
				if ((direction == 0 && blockInterval.direction
						.equalsIgnoreCase("up"))
						|| (direction == 1 && blockInterval.direction
								.equalsIgnoreCase("down")))
					return arrivalTime;
				else
					return blockInterval.endTime;
			}
		}

		return arrivalTime;
	}
}

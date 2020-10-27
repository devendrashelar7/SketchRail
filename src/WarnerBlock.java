import java.io.IOException;
import java.io.StreamTokenizer;

import util.Debug;

/**
 * 
 */
public class WarnerBlock extends Block {

	/**
 * 
 */
	public WarnerBlock() {
	}

	/**
	 * @param st
	 * @throws IOException
	 * @throws SimException
	 */
	public WarnerBlock(StreamTokenizer st) throws IOException, SimException {
		super(st);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Block#getEarliestArrivalTime(double, int)
	 */
	public double getEarliestArrivalTime(double time, int dir) {
		Debug.print("In warner earliest arrival");
		int blockFree = occupy.inInterval(time);

		if (blockFree != -1) {
			Debug.print("In warner earliest arrival   in red ");
			return occupy.get(blockFree).endTime;
		}
		Debug.print("In warner earliest arrival  calling next block");
		return getNextBlock(dir).getEarliestArrivalTime(time, dir);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Block#getEarliestArrivalTime(double, double, int)
	 */
	public double getEarliestArrivalTime(double time1, double time2, int dir) {
		Debug.print("In warner earliest arrival");
		int blockFree = occupy.inInterval(time1, time2);

		if (blockFree != -1) {
			Debug.print("In warner earliest arrival   in red ");
			return occupy.get(blockFree).endTime;
		}
		Debug.print("In warner earliest arrival  calling next block");
		return getNextBlock(dir).getEarliestArrivalTime(time1, time2, dir);
	}

	/**
	 * This returns the signal of red or green and more. /* Thus it will never
	 * return a yellow instead it will return red.
	 * 
	 * @param currTrain
	 * @param noOfColor
	 * @param time
	 * @param dir
	 * @return get number of signal
	 * */

	public int getSignal(Train currTrain, int noOfColor, double time, int dir) {
		if (this.isFree(time) == -1) {
			if (getNextBlock(dir, currTrain) == null) {
				return 1;
			}
			int sig = (getNextBlock(dir, currTrain).getSignal(currTrain,
					noOfColor, time, dir));
			if (sig < 1) {
				sig = 0;
			} else {
				sig++;
			}
			return (sig);
		}
		return (0);
	}
	
	
}
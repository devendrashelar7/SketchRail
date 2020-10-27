import java.util.*;
import util.*;

/*we can change the get method for the seeing to it that the occupy array can be concatenated when one
 * ends and the other starts at the same time.*/

/**
 * IntervalArray for list of intervals.
 */
public class IntervalArray extends ArrayList<Interval> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public IntervalArray() {
	}

	/**
	 * Print all the intervals in the intervalArray.
	 */
	public void printInterval() {
		for (int i = 0; i < size(); i++) {
			Interval interval = get(i);
			Debug.print("In Interval start Time " + interval.startTime
					+ " dept time " + interval.endTime + " trainNo "
					+ interval.trainNo);
		}
	}

	/**
	 * Get total time in each interval of the IntervalArray.
	 * 
	 * @return double time between intervals
	 */
	public double totalInterval() {
		double timeInInterval = 0;
		for (int i = 0; i < size(); i++) {
			Interval interval = get(i);
			timeInInterval += interval.endTime - interval.startTime;
		}
		return timeInInterval;
	}

	/**
	 * @param time
	 *            time to check in which interval
	 * @return the interval in which the given time lies. If there is no such
	 *         interval then returns -1.
	 */
	public int inInterval(double time) {
		Interval interval;
		for (int i = 0; i < size(); i++) {
			interval = get(i);
			if ((time >= interval.startTime) && (time <= interval.endTime)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param startTime
	 *            starting time to check
	 * @param endTime
	 *            ending time to check
	 * 
	 * @return the interval in which the time period between startTime and
	 *         endTime lies
	 */
	public int inInterval(double startTime, double endTime) {
		Debug.print("input - start time " + startTime + " end time " + endTime);
		// printInterval();
		Interval interval;
		for (int count = 0; count < size(); count++) {
			interval = get(count);
			if (!((interval.endTime < startTime) || (interval.startTime > endTime))) {
				return count;
			}
		}
		return -1;

	}

}

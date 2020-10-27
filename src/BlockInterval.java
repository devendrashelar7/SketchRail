/**
 * @author devendra
 * 
 */
public class BlockInterval {

	/**
	 * startTime
	 */
	double startTime;
	/**
	 * endTime
	 */
	double endTime;
	/**
	 * direction
	 */
	String direction;

	/**
	 * @param intervalStartTime
	 * @param intervalEndTime
	 * @param blockDirection
	 */
	public BlockInterval(double intervalStartTime, double intervalEndTime,
			String blockDirection) {
		startTime = Math.min(intervalStartTime, intervalEndTime);
		endTime = Math.max(intervalStartTime, intervalEndTime);
		direction = blockDirection;
	}

	// Devendra
	/**
	 * @param time
	 * @return true if the time lies in the interval
	 */
	public boolean isTimeInInterval(double time) {
		if (time >= this.startTime && time <= this.endTime) {
			return true;
		}
		return false;
	}
}

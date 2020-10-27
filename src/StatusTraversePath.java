/**
 * 
 */
public class StatusTraversePath extends StatusTraverseBlock {
	/**
	 * signal : -1 value indicates that the scheduling of the train for next
	 * block scheduling was not successful. -2 value indicates that there was
	 * signal failure took place on some block along the path
	 */
	int signal;
	/**
	 * arrivalTime
	 */
	double arrivalTime;

	/**
	 * @param st
	 * @param arrtime
	 * @param deptime
	 * @param sig
	 */
	public StatusTraversePath(boolean st, double arrtime, double deptime,
			int sig) {
		super(st, deptime, -1);
		arrivalTime = arrtime;
		signal = sig;
	}

}
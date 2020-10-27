import java.util.*;

/**
 * 
 */
class TimetableEntry {
	/**
	 * loopNo
	 */
	int loopNo;
	/**
	 * arrivalTime
	 */
	double arrivalTime;
	/**
	 * departureTime
	 */
	double departureTime;
	/**
	 * signal
	 */
	int signal = 0;
	/**
	 * startMilePost
	 */
	double startMilePost;
	/**
	 * endMilePost
	 */
	double endMilePost;
	/**
	 * svelo
	 */
	double svelo;
	/**
	 * velocityProfileArray
	 */
	ArrayList<VelocityProfile> velocityProfileArray;

	/**
	 * @param loopNo
	 * @param time1
	 * @param time2
	 */
	TimetableEntry(int loopNo, double time1, double time2) {
		this.loopNo = loopNo;
		if (time1 > time2) {
			arrivalTime = time2;
			departureTime = time1;
		}
		if (time1 < time2) {
			arrivalTime = time1;
			departureTime = time2;
		}
		if (time1 == time2) {
			arrivalTime = time1;
			departureTime = time2;
		}
	}

	/**
	 * @param loopNo
	 * @param time1
	 * @param time2
	 * @param velo
	 */
	TimetableEntry(int loopNo, double time1, double time2, double velo) {
		this.loopNo = loopNo;
		if (time1 > time2) {
			arrivalTime = time2;
			departureTime = time1;
		}
		if (time1 < time2) {
			arrivalTime = time1;
			departureTime = time2;
		}
		if (time1 == time2) {
			arrivalTime = time1;
			departureTime = time2;
		}
		svelo = velo;
	}

	/**
	 * @param loopNo
	 * @param time1
	 * @param time2
	 * @param velo
	 * @param milePost
	 */
	TimetableEntry(int loopNo, double time1, double time2, double velo,
			double milePost) {
		this.loopNo = loopNo;
		if (time1 > time2) {
			arrivalTime = time2;
			departureTime = time1;
		}
		if (time1 < time2) {
			arrivalTime = time1;
			departureTime = time2;
		}
		if (time1 == time2) {
			arrivalTime = time1;
			departureTime = time2;
		}
		svelo = velo;
		startMilePost = milePost;
		endMilePost = milePost;
	}

	/**
	 * @param loopNo
	 * @param time1
	 * @param time2
	 * @param startDistance
	 * @param endDistance
	 * @param veloArray
	 */
	TimetableEntry(int loopNo, double time1, double time2,
			double startDistance, double endDistance,
			ArrayList<VelocityProfile> veloArray) {
		this.loopNo = loopNo;
		if (time1 > time2) {
			arrivalTime = time2;
			departureTime = time1;
		}
		if (time1 < time2) {
			arrivalTime = time1;
			departureTime = time2;
		}
		if (time1 == time2) {
			arrivalTime = time1;
			departureTime = time2;
		}
		startMilePost = startDistance;
		endMilePost = endDistance;
		velocityProfileArray = veloArray;
	}

	/**
	 * @param loopNo
	 * @param time1
	 * @param time2
	 * @param startDistance
	 * @param endDistance
	 * @param veloArray
	 * @param sig
	 */

	TimetableEntry(int loopNo, double time1, double time2,
			double startDistance, double endDistance,
			ArrayList<VelocityProfile> veloArray, int sig) {
		this(loopNo, time1, time2, startDistance, endDistance, veloArray);
		signal = sig;
	}

	/**
     * 
     */
	TimetableEntry() {
	}

	/**
     * 
     */
	public void print() {
		System.out.println("Loop No: " + loopNo + " Start MileP: "
				+ startMilePost + " End Milep: " + endMilePost
				+ " Arrival Time " + arrivalTime + " Departure Time: "
				+ departureTime);
	}
}
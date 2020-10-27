/* ------ interval is used to store the intervals, start time and end
 time of various dynamic properties, for example - block section / loop
        occupancy etc. the object of type interval are used in arraylists to
        retrive and store the dynamic data
 --------------------------*/

/**
 * interval
 */
class Interval {
	/**
	 * startTime
	 */
	double startTime; // start time
	/**
	 * endTime
	 */
	double endTime; // end time
	/**
	 * velocity
	 */
	double velocity;
	/**
	 * trainNo
	 */
	int trainNo;

	/**
 * 
 */
	Interval() {
	}

	/**
	 * @param a
	 * @param b
	 */
	Interval(double a, double b) {

		if (a > b) {
			startTime = b;
			endTime = a;
		}
		if (a < b) {
			startTime = a;
			endTime = b;
		}
		if (a == b) {
			startTime = a;
			endTime = b;
		}
	}

	/**
	 * @param no
	 * @param a
	 * @param b
	 */
	Interval(int no, double a, double b) {
		trainNo = no;
		if (a > b) {
			startTime = b;
			endTime = a;
		}
		if (a < b) {
			startTime = a;
			endTime = b;
		}
		if (a == b) {
			startTime = a;
			endTime = b;
		}
	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 */
	Interval(double a, double b, double c) {
		velocity = c;
		if (a > b) {
			startTime = b;
			endTime = a;
		}
		if (a < b) {
			startTime = a;
			endTime = b;
		}
		if (a == b) {
			startTime = a;
			endTime = b;
		}
	}

	//Devendra
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
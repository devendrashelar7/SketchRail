/**
 * 
 */
class VelocityProfile {
	/**
	 * startLength
	 */
	double startLength;
	/**
	 * endLength
	 */
	double endLength;
	/**
	 * startVelocity
	 */
	double startVelocity;
	/**
	 * endVelocity
	 */
	double endVelocity;
	/**
	 * accelerationParameter
	 */
	double accelerationParameter;
	/**
	 * time
	 */
	double time;

	/**
	 * @param startLen
	 * @param endLen
	 * @param startVelo
	 * @param finalVelo
	 * @param totTime
	 * @param accel
	 */
	public VelocityProfile(double startLen, double endLen, double startVelo,
			double finalVelo, double totTime, double accel) {
		startLength = startLen;
		endLength = endLen;
		startVelocity = startVelo;
		endVelocity = finalVelo;
		accelerationParameter = accel;
		time = totTime;
	}

	public void print(int trainDirection) {
		if (trainDirection == 0) {
			System.out.println("startLength : " + startLength + " endLength "
					+ endLength + " time " + time);
			System.out.println(" startVelocity " + startVelocity
					+ " endVelocity " + endVelocity);
		} else if (trainDirection == 1) {
			System.out.println("startLength : " + endLength + " endLength "
					+ startLength + " time " + time);
			System.out.println(" startVelocity " + endVelocity
					+ " endVelocity " + startVelocity);
		}
	}

	public double getVelocityFromMilepost(double milePost) {
		double s = this.startLength;
		double e = this.endLength;
		double m = milePost;
		double sv = this.startVelocity;
		double ev = this.endVelocity;

		if (s <= m && m <= e)
			return (m - s) / (e - s) * (ev - sv) + sv;
		return -100;
	}

	public boolean containsMilePost(double milePost) {
		if ((this.startLength <= milePost && milePost <= this.endLength)
				|| (this.startLength >= milePost && milePost >= this.endLength))
			return true;
		else
			return false;
	}
	
}

/**
 * Title: Simulation of Train Pathing Description: Velocity Copyright: Copyright
 * (c) 2002 Company: IIT
 * 
 * @author Rajesh Naik
 * @version 1.0
 */

public class Velocity {
	/**
	 * value
	 */
	public double value;
	/**
	 * precision
	 */
	public static double precision = 0;

	/**
 * 
 */
	public Velocity() {
		value = 0;
	}

	/**
	 * @param velocity
	 */
	public Velocity(double velocity) {
		value = velocity;
	}

	/**
	 * @return copy funtion
	 */
	public Velocity copy() {
		return new Velocity(value);
	}

	/**
	 * @param precise
	 */
	public static void setPrecision(double precise) {
		precision = precise;
	}

	/**
	 * @param v
	 * @return true if the v matches to this instance
	 */
	public boolean equals(Velocity v) {
		if (Math.abs(v.value - value) > precision) {
			return false;
		}
		return true;
	}

	/**
	 * @param v
	 * @return true
	 */
	public boolean equals(double v) {
		if (Math.abs(v - value) > precision) {
			return false;
		}
		return true;
	}

	/**
	 * Returns 0 if velocitoies are equal , 1 if value > v.value and -1 if less
	 * 
	 * @param v
	 * @return comparison
	 * */
	public int compare(Velocity v) {
		double diff = value - v.value;
		if (Math.abs(diff) < precision) {
			return 0;
		}
		return (diff > 0) ? 1 : (-1);
	}

	/**
	 * @param v
	 */
	public void setVeloKMPH(double v) {
		value = v / 60;
	}
}
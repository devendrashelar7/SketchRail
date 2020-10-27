/**
 * 
 */
public class GradientFormat {
	/**
	 * gradientValue
	 */
	String gradientValue;
	/**
	 * direction
	 */
	String direction;
	/**
	 * startMilePost
	 */
	double startMilePost;
	/**
	 * endMilePost
	 */
	double endMilePost;

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	public GradientFormat(String a, String b, double c, double d) {
		gradientValue = a;
		direction = b;
		startMilePost = c;
		endMilePost = d;
	}

	/**
 * 
 */
	public GradientFormat() {
	}
}
/**
 * 
 */
public class TinyBlockFormat {
	/**
	 * startMilePost
	 */
	double startMilePost;
	/**
	 * endMilePost
	 */
	double endMilePost;
	/**
	 * accelerationChange
	 */
	double accelerationChange;
	/**
	 * decelerationChange
	 */
	double decelerationChange;
	/**
	 * maxSpeed
	 */
	double maxSpeed;

	/**
	 * constructor.
	 */
	public TinyBlockFormat() {
		startMilePost = 0;
		endMilePost = 0;
		accelerationChange = 0;
		decelerationChange = 0;
		maxSpeed = 0;
	}

	/**
	 * @param startMilePost
	 * @param endMilePost
	 * @param acChange
	 * @param deChange
	 * @param maxSpeed
	 */
	public TinyBlockFormat(double startMilePost, double endMilePost,
			double acChange, double deChange, double maxSpeed) {

		this.startMilePost = startMilePost;
		this.endMilePost = endMilePost;
		this.accelerationChange = acChange;
		this.decelerationChange = deChange;
		this.maxSpeed = maxSpeed;
	}
}
/**
 * 
 */
class SpeedRestrictionFormat {
    /**
     * svelo
     */
    double svelo;
/**
 * startMilePost
 */
double startMilePost;
/**
 * endMilePost
 */
double endMilePost;

    /**
     * @param velo
     * @param b
     * @param c
     */
    SpeedRestrictionFormat(double velo, double b, double c) {
	svelo = velo;
	if (b > c) {
	    startMilePost = c;
	    endMilePost = b;
	}
	if (b < c) {
	    startMilePost = b;
	    endMilePost = c;
	}
	if (b == c) {
	    startMilePost = b;
	    endMilePost = c;
	}
    }

    /**
     * 
     */
    SpeedRestrictionFormat() {
    }
}
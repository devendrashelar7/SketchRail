import java.util.Comparator;

/**
 * 
 */

public class SortStation implements Comparator<Station> {

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Station a, Station b) {

		Station a1 = (Station) a;
		Station b1 = (Station) b;
		double c = a1.startMilePost;
		double d = b1.startMilePost;

		if (c > d) {
			return 1;
		}
		if (c < d) {
			return -1;
		}
		return 0;
	}
}

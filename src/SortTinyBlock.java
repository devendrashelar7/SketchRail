import java.util.Comparator;

/**
 * 
 */

class SortTinyBlock implements Comparator<TinyBlockFormat> {

    /** (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(TinyBlockFormat a, TinyBlockFormat b) {

	TinyBlockFormat firstTiny = (TinyBlockFormat) a;
	TinyBlockFormat secondTiny = (TinyBlockFormat) b;
	
	

	if (firstTiny.startMilePost > secondTiny.startMilePost) {
	    return 1;
	}
	if (firstTiny.startMilePost < secondTiny.startMilePost) {
	    return -1;
	}

	return 0;
    }
}

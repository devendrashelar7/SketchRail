import java.util.Comparator;

/**
 * 
 */

public class SortTrain implements Comparator<Train> {

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Train trainObject1, Train trainObject2) {
		int var = 0;
		double startTime1, startTime2, departTime1, departTime12;
		int priority1, priority2;
		boolean isScheduled1 = false, isScheduled2 = false;
		Train train1, train2;
		train1 = (Train) trainObject1;
		train2 = (Train) trainObject2;
		priority1 = train1.priority;
		priority2 = train2.priority;
		startTime1 = train1.startTime;
		startTime2 = train2.startTime;
		departTime1 = train1.departTime;
		departTime12 = train2.departTime;
		isScheduled1 = train1.isScheduled;
		isScheduled2 = train2.isScheduled;

		if (priority1 > priority2) {
			var = 1;
		} else if (priority1 < priority2) {
			var = -1;
		} else if (priority1 == priority2) {
			if (isScheduled1 == true && isScheduled2 == true) {
				if (departTime1 > departTime12) {
					var = 1;
				} else if (departTime1 < departTime12) {
					var = -1;
				} else {
					var = 0;
				}
			} else {
				if (startTime1 > startTime2) {
					var = 1;
				} else if (startTime1 < startTime2) {
					var = -1;
				} else {
					var = 0;
				}
			}
		}

		return var;
	}

}

/**
 * sorter for the third track.
 */

class SortTrainWithTimeInterval implements Comparator<Train> {

    /**
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     *      This will help in sorting the trains as per the scheduling of the
     *      third track as the up track or the down track.
     */
    @Override
    public int compare(Train trainObject1, Train trainObject2) {
	int var = 0;
	double startTime1, startTime2, departTime1, departTime12;
	int priority1, priority2;
	boolean isScheduled1 = false, isScheduled2 = false;
	Train train1, train2;
	train1 = (Train) trainObject1;
	train2 = (Train) trainObject2;
	priority1 = train1.priority;
	priority2 = train2.priority;
	startTime1 = train1.startTime;
	startTime2 = train2.startTime;
	departTime1 = train1.departTime;
	departTime12 = train2.departTime;
	isScheduled1 = train1.isScheduled;
	isScheduled2 = train2.isScheduled;

	if (priority1 > priority2) {
	    var = 1;
	} else if (priority1 < priority2) {
	    var = -1;
	} else if (priority1 == priority2) {
	    if (isScheduled1 == true && isScheduled2 == true) {
		if (departTime1 > departTime12) {
		    var = 1;
		} else if (departTime1 < departTime12) {
		    var = -1;
		} else {
		    var = 0;
		}
	    } else {
		if (startTime1 > startTime2) {
		    var = 1;
		} else if (startTime1 < startTime2) {
		    var = -1;
		} else {
		    var = 0;
		}
	    }
	}

	return var;
    }

}

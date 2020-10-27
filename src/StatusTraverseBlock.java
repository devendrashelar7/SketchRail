/**
 * 
 */
public class StatusTraverseBlock {
	/**
	 * status
	 */
	public boolean status;
	/**
	 * limit
	 */
	public boolean limit; // added by uhp on May17
	/**
	 * trainNo
	 */
	public int trainNo;
	/**
	 * departureTime
	 */
	public double departureTime;

	/**
	 * @param st
	 * @param time
	 * @param no
	 */
	public StatusTraverseBlock(boolean st, double time, int no) {
		status = st;
		departureTime = time;
		trainNo = no;
		limit = false;
	}

	/**
	 * @param _status
	 * @param _limit
	 * @param _trainNo
	 */
	public StatusTraverseBlock(boolean _status, boolean _limit, int _trainNo) {
		trainNo = _trainNo;
		status = _status;
		limit = _limit;
		departureTime = -2;
	}
	
	/**
	 * @param _status
	 * @param _limit
	 * @param currentTrain
	 */
	public StatusTraverseBlock(boolean _status, boolean _limit, Train currentTrain){
		trainNo=currentTrain.trainNo;
		departureTime=currentTrain.startTime;
		limit=_limit;
		status=_status;
	}
}

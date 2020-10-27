/**
 * 
 */
public class SignalFailureFormat {
	/**
	 * blockNo
	 */
	int blockNo;
	/**
	 * start
	 */
	double start;
	/**
	 * end
	 */
	double end;

	/**
 * 
 */
	public SignalFailureFormat() {
	}

	/**
	 * @param blockNumber
	 * @param startTime
	 * @param endTime
	 */
	public SignalFailureFormat(int blockNumber, double startTime, double endTime) {
		blockNo = blockNumber;
		start = startTime;
		end = endTime;
	}

}
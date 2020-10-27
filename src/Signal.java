import util.Debug;

/**
 * signal
 */
public class Signal {

	/**
	 * constructor
	 */
	public Signal() {
	}

	/**
	 * 1) It first calls block.isFree(arrivalTime) which in return calls
	 * occupy.inInterval(time) IntervalArray: int inInterval(double time)
	 * returns the interval in the array which contains the given time argument
	 * or returns -1 if no such interval exists. 2) If no such interval exists
	 * then the function checks if according to the reference time table and if
	 * the train is a scheduled train has to wait. It then returns yellow
	 * meaning it has to wait. If it is a freight train, it checks if the next
	 * block is free to what extent i.e. it recursively calls for
	 * nextBlock.signal(currTrain, noOfColor -1, arrivalTime, direction) and
	 * returns a (signal+1). 3) Else, meaning there exists an interval in which
	 * the block is occupied, hence it returns a red signal, that is 0.
	 * 
	 * @param currentBlock
	 * @param currTrain
	 * @param noOfColor
	 * @param arrivalTime
	 * @param direction
	 * @return getSignal
	 */

	public int getSignal(Block currentBlock, Train currTrain, int noOfColor,
			double arrivalTime, int direction) {
		Debug.print("In signal.getsignal function -- Values passed here are");
		Debug.print(currentBlock.blockNo + " " + currentBlock.startMilePost
				+ "   " + currentBlock.endMilePost + "   " + currTrain.trainNo
				+ "  " + noOfColor + "   " + arrivalTime + "  " + direction);

		// Devendra- if the direction of the block is not same as the direction
		// of train at arrival time signal should be red
		if (!currentBlock.isDirectionOk(currTrain, arrivalTime)) {
			return 0;
		}

		// 3rd january --- code for controlling third line for suburban trains
		// only or long distance trains only
		if(!currentBlock.allowsTrain(currTrain, arrivalTime)){
			return 0;
		}
		

		if (noOfColor < 2) {
			Debug.print(" in color<2 ");
			return 0;
		}
		// boolean type=true;
		Block nextBlock;

		if (currentBlock.isFree(arrivalTime) == -1) {

			Debug.print(" In other case color >2  ");
			nextBlock = currentBlock.getNextBlock(direction, currTrain);// ////////mj
			int signal = 0;
			if ((currTrain.isScheduled && currentBlock.isLoop())
					&& (currentBlock.blockNo != 0)) {

				Debug.print(" This is passenger train ");

				String stationName = (((Loop) currentBlock).station).stationName;

				ReferenceTableEntry referenceTableEntry = currTrain
						.getRefTabEntryFromStationName(stationName);
				double arrTime = referenceTableEntry.refArrTime;
				double depTime = referenceTableEntry.refDepTime;
				if ((depTime - arrTime) > 0) {
					Debug
							.print(" Train will have to wait so returning signal 1");
					return 1; // as we will have to stop at this station
					// so we show the signal as yellow...

				}
			} else {
				// System.out.println("This is freight train ");
				if (currTrain.endLoopNo != -1 && currentBlock.isLoop()) {

					Loop loop = (Loop) GlobalVar
							.getBlockFromBlockNo(currentBlock.blockNo);
					// System.out.println("here "
					// + loop.stationName + " going "
					// + currTrain.endStation);
					if (currTrain.hasScheduledHalt(loop)) {
						// System.out.println("Has halt for unscheduled train");
						return 1;
					}
				}
			}
			if ((noOfColor > 2) && (nextBlock != null)) {
				Debug.print("6");
				signal = nextBlock.getSignal(currTrain, noOfColor - 1,
						arrivalTime, direction);
			}

			Debug.print("Signal count is " + signal);
			return (signal + 1);
		}
		return (0);
	}

}
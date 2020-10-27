import java.util.ArrayList;

import util.Debug;

/**
 * PassengerTrain
 */
class PassengerTrain extends Train {

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 */
	public PassengerTrain(int a, double b, double c, double d, double e, int f) {
		super(a, b, c, d, e, f);
	}

	/**
	 * @param dir
	 * @param prior
	 * @param len
	 */
	public PassengerTrain(int dir, int prior, double len) {
		direction = dir;
		length = len;
		priority = prior;
	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 */
	public PassengerTrain(int a, double b, double c, double d, double e) {
		super(a, b, c, d, e);
	}

	/**
	 * @param a
	 */
	public PassengerTrain(String a) {
		operatingDays = a;
	}

	/**
	 * @param Stations
	 */
	public void occupyBlocks(ArrayList<Station> Stations) {
		ArrayList<TimetableEntry> currTimeTable = timeTables;
		ArrayList<TimetableEntry> newTimeTable = new ArrayList<TimetableEntry>();
		TimetableEntry ttEntry, nextTTEntry;
		for (int m = 0; m < currTimeTable.size() - 1; m++) {
			ttEntry = (TimetableEntry) currTimeTable.get(m);
			nextTTEntry = (TimetableEntry) currTimeTable.get(m + 1);
			Debug.print("looping " + m + " loop no " + ttEntry.loopNo
					+ "next loop no" + nextTTEntry.loopNo);
			Loop currLoop = (Loop) ReadSection.getBlock(ttEntry.loopNo);
			currLoop.setOccupied(trainNo, ttEntry.arrivalTime,
					ttEntry.departureTime);
			Loop nextLoop = (Loop) ReadSection.getBlock(nextTTEntry.loopNo);
			ttEntry.startMilePost = currLoop.getStartMilePost(this.direction);
			ttEntry.endMilePost = currLoop.getEndMilePost(this.direction);
			double rdist;
			if (direction == 0) {
				rdist = Math.abs(currLoop.endMilePost - nextLoop.startMilePost);
			} else {
				rdist = Math.abs(currLoop.startMilePost - nextLoop.endMilePost);
			}
			Debug.print("Time table entry for loop added ");
			newTimeTable.add(ttEntry);
			double rtime = nextTTEntry.arrivalTime - ttEntry.departureTime;
			double deptTime = 0;
			double arrtime = ttEntry.departureTime;
			Debug.print("rtime is:" + rtime + "rdist is:" + rdist);
			Block currBlk = currLoop.getNextBlock(direction, this);
			Debug.print(" arrtime is:" + arrtime + " deptTime is:" + deptTime);
			Debug.print(" take next " + nextLoop.startMilePost);
			Debug.print(" take curr " + currBlk.startMilePost);
			while (nextLoop.startMilePost != currBlk.startMilePost) {
				deptTime = arrtime
						+ (rtime * (currBlk.endMilePost - currBlk.startMilePost))
						/ rdist;
				ttEntry = new TimetableEntry(currBlk.blockNo, arrtime,
						deptTime, 0, currBlk.getStartMilePost(this.direction));
				ttEntry.endMilePost = currBlk.getEndMilePost(this.direction);
				newTimeTable.add(ttEntry);
				Block nextBlock = currBlk;
				// if 3 colour aspect then 2 blocks should be reserved.
				for (int i = 0; i < GlobalVar.numberOfColour - 1; i++) {
					Debug.print("Reserving for block " + nextBlock.blockName);
					
					
					nextBlock.setOccupied(trainNo, arrtime, deptTime);
					nextBlock = nextBlock.getNextBlock(direction, this);
					if (nextBlock == null)
						break;
				}
				Debug.print("Block no " + currBlk.blockName + " arrtime is:"
						+ arrtime + " deptTime is:" + deptTime);
				arrtime = deptTime;
				currBlk = currBlk.getNextBlock(direction, this);
			} // for n =
		} // for m ++
		// adding the timings for last satation
		ttEntry = (TimetableEntry) currTimeTable.get(currTimeTable.size() - 1);
		Loop currLoop = (Loop) ReadSection.getBlock(ttEntry.loopNo);
		currLoop.setOccupied(trainNo, ttEntry.arrivalTime,
				ttEntry.departureTime);
		ttEntry.startMilePost = currLoop.startMilePost;
		ttEntry.endMilePost = currLoop.startMilePost;
		newTimeTable.add(ttEntry);
		for (int m = 0; m < newTimeTable.size(); m++) {
			ttEntry = (TimetableEntry) newTimeTable.get(m);
			Debug.print(" Alloted loop:  " + ttEntry.loopNo + "   Arr Time: "
					+ ttEntry.arrivalTime + "   DepTime: "
					+ ttEntry.departureTime + "   Velocity:" + ttEntry.svelo
					+ "     dist:" + ttEntry.startMilePost);
		}
		timeTables = newTimeTable;
	}

}// for delay class
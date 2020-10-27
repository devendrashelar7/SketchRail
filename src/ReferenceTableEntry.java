import java.util.*;

/**
 * 
 */
public class ReferenceTableEntry {
	/**
	 * stationName
	 */
	String stationName;
	/**
	 * refLoopNo
	 */
	int refLoopNo;
	/**
	 * refArrTime
	 */
	double refArrTime;
	/**
	 * refDepTime
	 */
	double refDepTime;
	/**
	 * minHaltTime
	 */
	double minHaltTime;
	/**
	 * runTimeToNextStn
	 */
	double runTimeToNextStn = 60;
	/**
	 * velocityProfileArray
	 */
	ArrayList<VelocityProfile> velocityProfileArray; // -- keep this-- KJD

	/**
	 * @param loopNo
	 * @param arrTime
	 * @param depTime
	 * @param stationName
	 * @param runTime
	 */
	ReferenceTableEntry(int loopNo, double arrTime, double depTime,
			String stationName, double runTime) {

		refLoopNo = loopNo;
		this.stationName = stationName;
		if (GlobalVar.delayFactor == 1) {
			/*
			 * if (arrTime > depTime) { System.out.print("arrTime > depTime ");
			 * System.exit(0);} else
			 */{
				refArrTime = arrTime;
				refDepTime = depTime;
			}
		}
		if (GlobalVar.delayFactor == 0) {
			refArrTime = arrTime;
			refDepTime = depTime;
		}
		minHaltTime = refDepTime - refArrTime;
		runTimeToNextStn = runTime;
	}

	/**
     * 
     */
	ReferenceTableEntry() {
	}

	/**
     * 
     */
	public void print() {
		System.out.print("Ref Loop No: " + refLoopNo + " Station: "
				+ stationName + " Ref Arrival Time: " + refArrTime
				+ " Ref Departure Time: " + refDepTime + " minHaltTime: "
				+ minHaltTime + " runTimeToNextStn: " + runTimeToNextStn);
	}
	
}
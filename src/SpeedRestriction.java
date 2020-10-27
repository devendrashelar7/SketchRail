import java.util.*;
import util.*;

/**
 * 
 */
class SpeedRestriction {
	/**
	 * speedRestrictArray
	 */
	ArrayList<SpeedRestrictionFormat> speedRestrictArray;

	/**
     * 
     */
	public SpeedRestriction() {
		speedRestrictArray = new ArrayList<SpeedRestrictionFormat>();
	}

	/**
	 * @param sp
	 */
	public void add(SpeedRestrictionFormat sp) {
		if (speedRestrictArray.size() == 0) {
			speedRestrictArray.add(sp);
			Debug.print("SpeedRestriction: add: first speed restriction added");
			return;
		}

		double currStartMileP = speedRestrictArray.get(0).startMilePost;

		if (sp.startMilePost < currStartMileP) {

			if (sp.endMilePost > currStartMileP) {
				speedRestrictArray.add(0, new SpeedRestrictionFormat(sp.svelo,
						sp.startMilePost, currStartMileP));
			} else {
				speedRestrictArray.add(0, new SpeedRestrictionFormat(sp.svelo,
						sp.startMilePost, sp.endMilePost));
			}
		}
		for (int i = 0; i < speedRestrictArray.size(); i++) {
			// check disatance and spilt
			currStartMileP = speedRestrictArray.get(i).startMilePost;
			double currEndMileP = speedRestrictArray.get(i).endMilePost;
			if (sp.startMilePost > currStartMileP) {
				if (sp.endMilePost < currEndMileP) {
					double velo = speedRestrictArray.get(i).svelo;
					if (sp.svelo < velo) {
						speedRestrictArray.remove(i);
						// restictions r being added inversly they will be
						// shifted down
						speedRestrictArray.add(i, new SpeedRestrictionFormat(
								velo, sp.endMilePost, currEndMileP));
						speedRestrictArray.add(i, new SpeedRestrictionFormat(
								sp.svelo, sp.startMilePost, sp.endMilePost));
						speedRestrictArray.add(i, new SpeedRestrictionFormat(
								velo, currStartMileP, sp.startMilePost));
						Debug.print("speed res added in middle split in three");
					}
					break;
				}
				if (sp.startMilePost < currEndMileP) {
					double velo = speedRestrictArray.get(i).svelo;
					if (sp.svelo < velo) {
						speedRestrictArray.remove(i);
						speedRestrictArray.add(i, new SpeedRestrictionFormat(
								velo, currStartMileP, sp.startMilePost));
						speedRestrictArray.add(i + 1,
								new SpeedRestrictionFormat(sp.svelo,
										sp.startMilePost, currEndMileP));
						i = i + 1;
						Debug.print("speed res added at last");
					}
				}
			} else {
				if (sp.endMilePost > currEndMileP) {
					double velo = speedRestrictArray.get(i).svelo;
					if (sp.svelo < velo) {
						speedRestrictArray.get(i).svelo = sp.svelo;
						Debug.print("speed res was stricter than ther earlier");
					}
				} else {
					if (sp.endMilePost > currStartMileP) {
						double velo = speedRestrictArray.get(i).svelo;
						if (sp.svelo < velo) {
							speedRestrictArray.remove(i);
							speedRestrictArray.add(i,
									new SpeedRestrictionFormat(sp.svelo,
											currStartMileP, sp.endMilePost));
							speedRestrictArray.add(i + 1,
									new SpeedRestrictionFormat(velo,
											sp.endMilePost, currEndMileP));
							i = i + 1;
							Debug.print("speed res added at first");
						}
					}
					break;
				}
			}
		}
		double currEndMileP = speedRestrictArray
				.get(speedRestrictArray.size() - 1).startMilePost;
		if (sp.endMilePost > currEndMileP) {
			if (sp.startMilePost < currEndMileP) {
				speedRestrictArray.add(new SpeedRestrictionFormat(sp.svelo,
						currEndMileP, sp.endMilePost));
			} else {
				speedRestrictArray.add(new SpeedRestrictionFormat(sp.svelo,
						sp.startMilePost, sp.endMilePost));
			}
		}
		mergeSimilar();
	}

	/**
     * 
     */
	public void mergeSimilar() {
		for (int i = 0; i < speedRestrictArray.size() - 1; i++) {
			if (speedRestrictArray.get(i).svelo == speedRestrictArray
					.get(i + 1).svelo) {
				speedRestrictArray.get(i).endMilePost = speedRestrictArray
						.get(i + 1).endMilePost;
				speedRestrictArray.remove(i + 1);
			}

		}
	}

	/**
	 * @param distance
	 * @return {@link ArrayList} of {@link SpeedRestriction}
	 */
	public ArrayList<SpeedRestriction> split(double distance) {
		ArrayList<SpeedRestriction> returnArray = new ArrayList<SpeedRestriction>();
		SpeedRestriction speedRestStart, speedRestEnd;
		speedRestStart = new SpeedRestriction();
		speedRestEnd = new SpeedRestriction();
		double currEndMileP, currStartMileP;
		SpeedRestrictionFormat sp;
		for (int i = 0; i < speedRestrictArray.size(); i++) {
			sp = speedRestrictArray.get(i);
			currStartMileP = sp.startMilePost;
			currEndMileP = sp.endMilePost;
			if (currStartMileP < distance) {
				if (currEndMileP <= distance) {
					speedRestStart.add(sp);
				} else {
					speedRestStart.add(new SpeedRestrictionFormat(sp.svelo,
							sp.startMilePost, distance));
					speedRestEnd.add(new SpeedRestrictionFormat(sp.svelo,
							distance, sp.endMilePost));
				}
			} else {
				speedRestEnd.add(speedRestrictArray.get(i));
			}
		}
		returnArray.add(speedRestStart);
		returnArray.add(speedRestEnd);
		return returnArray;
	}

	/**
	 * @return size of the array
	 */
	public int size() {
		return (speedRestrictArray.size());
	}

	// gradient to be oincluded...
	/**
	 * @param i
	 * @return {@link SpeedRestrictionFormat}
	 */
	public SpeedRestrictionFormat get(int i) {
		SpeedRestrictionFormat sp, newSp;
		sp = speedRestrictArray.get(i);
		newSp = new SpeedRestrictionFormat(sp.svelo, sp.startMilePost,
				sp.endMilePost);
		if (GlobalVar.currentTrain != null) {
			if (i != 0) {
				if (speedRestrictArray.get(i).svelo > speedRestrictArray
						.get(i - 1).svelo) {
					if (GlobalVar.currentTrain.direction == 0) {
						newSp.startMilePost += GlobalVar.currentTrain.length;
					} else {
						newSp.startMilePost -= GlobalVar.currentTrain.length;
					}
				}
			}
			if (i < speedRestrictArray.size() - 1) {
				if (speedRestrictArray.get(i).svelo < speedRestrictArray
						.get(i + 1).svelo) {
					if (GlobalVar.currentTrain.direction == 0) {
						newSp.endMilePost += GlobalVar.currentTrain.length;
					} else {
						newSp.endMilePost -= GlobalVar.currentTrain.length;
					}
				}
			}
		}
		return newSp;
	}

	/**
	 * @return {@link ArrayList}
	 */
	public ArrayList<SpeedRestrictionFormat> getArrayList() {
		return speedRestrictArray;
	}

	/**
	 * CODE BY CHANDRA
	 * 
	 * @param a
	 * @param b
	 * @return MaxSP
	 * **/
	public double returnMaxsp(double a, double b) {
		double tem = 0;
		Debug.print("CAME to returnmaxsp");
		for (int i = 0; i < speedRestrictArray.size(); i++) {
			if ((a >= speedRestrictArray.get(i).startMilePost)
					&& (b <= speedRestrictArray.get(i).endMilePost)) {
				tem = speedRestrictArray.get(i).svelo;
			}
		}
		Debug.print("retirnis  " + tem);
		return tem;
	}

}
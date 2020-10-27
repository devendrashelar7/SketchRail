import java.util.*;
import util.*;

/**
 * 
 */
class VelocityCal {
	/**
	 * @param train
	 * @param currBlock
	 * @param startVelocity
	 * @param finalVelocity
	 * @return list of velocityProfiles
	 */
	public static ArrayList<VelocityProfile> getVelocityProfile(Train train, Block currBlock,
			double startVelocity, double finalVelocity) {

		double tempFinalVelocity = finalVelocity;
		Debug.print("Inside veloprofile size "
				+ currBlock.speedRestriction.size());
		ArrayList<VelocityProfile> veloProfileArray = new ArrayList<VelocityProfile>();
		ArrayList<VelocityProfile> tempVeloProfileArray = new ArrayList<VelocityProfile>();
		double startVelo;
		double accel, deccel;
		if (train.direction == 0) {
			startVelo = startVelocity;
			// finalVelocity is the same;
			accel = train.accParam;
			deccel = train.deceParam;
		} else {
			startVelo = tempFinalVelocity;
			tempFinalVelocity = startVelocity;
			accel = train.deceParam;
			deccel = train.accParam;
		}
		int i = 0;
		while (i < currBlock.speedRestriction.size()) {
			Debug.print("SpeedRes being tackeled. " + i);
			SpeedRestrictionFormat speedRestrictStart = currBlock.speedRestriction
					.get(i);
			for (int j = i; j < currBlock.speedRestriction.size(); j++) {
				SpeedRestrictionFormat speedRestrictCurr = currBlock.speedRestriction
						.get(j);
				double finalVelo;
				if (j != currBlock.speedRestriction.size() - 1) {
					if (currBlock.speedRestriction.get(j + 1).svelo < speedRestrictCurr.svelo) {
						finalVelo = currBlock.speedRestriction.get(j + 1).svelo;
					} else {
						finalVelo = speedRestrictCurr.svelo;
					}
				} else {
					finalVelo = tempFinalVelocity;
				}
				Debug.print("	With SpeedRes " + j + " startvelo " + startVelo
						+ " finalVelo " + finalVelo + " svelo "
						+ speedRestrictCurr.svelo);
				tempVeloProfileArray = calculateVelocity(startVelo, finalVelo,
						accel, deccel, speedRestrictCurr.svelo,
						speedRestrictStart.startMilePost,
						speedRestrictCurr.endMilePost);
				// check if the velo profile is increasing decreasing or both.
				if (tempVeloProfileArray.size() == 1) {
					// if decreasing....
					if (((VelocityProfile) tempVeloProfileArray.get(0)).startVelocity > ((VelocityProfile) tempVeloProfileArray
							.get(0)).endVelocity) {
						Debug.print("	Inside backtrack ");
						VelocityProfile tempVeloProfile = ((VelocityProfile) tempVeloProfileArray
								.get(0));
						// backtrack with the decreasing velo.
						int k = -1;
						for (k = veloProfileArray.size() - 1; k >= 0; k--) {
							Debug.print("	Inside loop " + k);
							// find intersection between this and current curve
							double intersectDistance = findIntersection(
									(VelocityProfile) veloProfileArray.get(k),
									tempVeloProfile);
							if (intersectDistance != -1) {
								Debug.print("	Found point " + k);
								// if intersection != -1 break we have found the
								// point.
								// add this to the existing bveloprofile.
								tempVeloProfileArray = calculateVelocity(
										((VelocityProfile) veloProfileArray
												.get(k)).startVelocity,
										finalVelo, accel, deccel,
										speedRestrictCurr.svelo,
										((VelocityProfile) veloProfileArray
												.get(k)).startLength,
										tempVeloProfile.endLength);
								veloProfileArray.remove(k);
								for (int currProfile = 0; currProfile < tempVeloProfileArray
										.size(); currProfile++) {
									veloProfileArray.add(k + currProfile,
											tempVeloProfileArray
													.get(currProfile));
								}
								break;
							}
							// remove the profile from the list as the new
							// one will superseed i.
							veloProfileArray.remove(k);
						}
						if (k < 0) {// then the speed restictions r getting
							// violated
							return null;
						}
						startVelo = tempVeloProfile.endVelocity;
						i = j + 1;
						break;
					} else if (((VelocityProfile) tempVeloProfileArray.get(0)).startVelocity == ((VelocityProfile) tempVeloProfileArray
							.get(0)).endVelocity) {
						startVelo = ((VelocityProfile) tempVeloProfileArray
								.get(0)).endVelocity;
						i = j + 1;
						break;
					}
					// else if increasing then move forward with the same i
					// continue with the same start velo
				} else {
					startVelo = ((VelocityProfile) tempVeloProfileArray
							.get(tempVeloProfileArray.size() - 1)).endVelocity;
					i = j + 1;
					break;
				}
			}
			// add this to the existing bveloprofile.
			for (int currProfile = 0; currProfile < tempVeloProfileArray.size(); currProfile++) {
				veloProfileArray.add(tempVeloProfileArray.get(currProfile));
			}
		}
		return veloProfileArray;
	}

	/**
	 * The function calculates the velocity profile and returns a arrayLIst of
	 * velocity profiles..
	 * 
	 * @param initVelocity
	 * @param finalVelocity
	 * @param accel
	 * @param deccel
	 * @param maxVelocity
	 * @param startMileP
	 * @param endMileP
	 * @return calculateVelocity
	 */
	public static ArrayList<VelocityProfile> calculateVelocity(double initVelocity,
			double finalVelocity, double accel, double deccel,
			double maxVelocity, double startMileP, double endMileP) {
		Debug.print("	Within calVElocity: startvelo " + initVelocity
				+ " finalVelo " + finalVelocity + " svelo " + maxVelocity);
		double totalTime = 0;
		ArrayList<VelocityProfile> veloArray = new ArrayList<VelocityProfile>();
		double length = endMileP - startMileP;
		System.out.println("SDH " + length);
		// cal distance to reach max velocity from initVElocity
		double startDistance = (Math.pow(maxVelocity, 2) - Math.pow(
				initVelocity, 2))
				/ (2 * accel);
		// cal distance to reach final velocity from max velocity
		double endDistance = (Math.pow(finalVelocity, 2) - Math.pow(
				maxVelocity, 2))
				/ (2 * (-deccel));
		// check distance
		if (endDistance > (length - startDistance)) {
			// if greater then calculate distance when velocity equal
			// check if the how much distance it take for the velocity to become
			// eqaul to the end velo and the
			double initEndDistance = (Math.pow(finalVelocity, 2) - Math.pow(
					initVelocity, 2))
					/ (2 * (-deccel));
			if (initEndDistance > length) {
				// cal start velo and total time
				double startVelo = Math.sqrt(Math.pow(finalVelocity, 2) - 2
						* -deccel * length);
				double startTime = (finalVelocity - startVelo) / (2 * -deccel);
				Debug.print(" 1 : startVelo " + startVelo + " final VElo "
						+ finalVelocity + " total time " + startTime);
			} else {
				double initStartDistance = (Math.pow(finalVelocity, 2) - Math
						.pow(initVelocity, 2))
						/ (2 * accel);
				if (initStartDistance > length) {
					// cal final velo and total time
					double endVelo = Math.sqrt(Math.pow(initVelocity, 2) + 2
							* accel * length);
					double startTime = (endVelo - initVelocity) / (2 * accel);
					veloArray.add(new VelocityProfile(startMileP, endMileP,
							initVelocity, endVelo, startTime, accel));
					Debug.print(" 2 : startVelo " + initVelocity
							+ " final VElo " + endVelo + " total time "
							+ startTime);
				} else {
					startDistance = (Math.pow(finalVelocity, 2)
							- Math.pow(initVelocity, 2) - (2 * (-deccel) * length))
							/ (2 * (accel - (-deccel)));
					endDistance = length - startDistance;
					double interimVelo = Math.sqrt(Math.pow(initVelocity, 2)
							+ 2 * accel * startDistance);
					// calculate time
					double startTime = (interimVelo - initVelocity)
							/ (2 * accel);
					veloArray.add(new VelocityProfile(startMileP, startMileP
							+ startDistance, initVelocity, interimVelo,
							startTime, accel));
					Debug.print(" 3:1 : startVelo " + initVelocity
							+ " final VElo " + interimVelo + " total time "
							+ startTime);
					double endTime = (finalVelocity - interimVelo)
							/ (2 * (-deccel));
					veloArray.add(new VelocityProfile(startMileP
							+ startDistance, endMileP, interimVelo,
							finalVelocity, endTime, -deccel));
					Debug.print(" 3:2 : startVelo " + interimVelo
							+ " final VElo " + finalVelocity
							+ " start distance " + (startMileP + startDistance)
							+ " end distance " + length + " total time "
							+ endTime);
					totalTime = startTime + endTime;
				}
			}
		} else {
			double startTime = (maxVelocity - initVelocity) / (2 * accel);
			if (startTime != 0) {
				veloArray.add(new VelocityProfile(startMileP, startMileP
						+ startDistance, initVelocity, maxVelocity, startTime,
						accel));
				Debug.print(" 4:1 : startVelo " + initVelocity + " final VElo "
						+ maxVelocity + " start distance " + startMileP
						+ " end distance " + (startMileP + startDistance)
						+ " total time " + startTime);

			}
			double endTime = (finalVelocity - maxVelocity) / (2 * (-deccel));

			double middleTime = (length - startDistance - endDistance)
					/ maxVelocity;
			if (middleTime != 0) {
				veloArray.add(new VelocityProfile(startMileP + startDistance,
						endMileP - endDistance, maxVelocity, maxVelocity,
						middleTime, 0));
				Debug.print(" 4:3 : startVelo " + maxVelocity + " final VElo "
						+ maxVelocity + " start distance "
						+ (startMileP + startDistance) + " end distance "
						+ (endMileP - endDistance) + " total time "
						+ middleTime);
			}

			if (endTime != 0) {
				veloArray
						.add(new VelocityProfile(endMileP - endDistance,
								endMileP, maxVelocity, finalVelocity, endTime,
								-deccel));
				Debug.print(" 4:2 : startVelo " + maxVelocity + " final VElo "
						+ finalVelocity + " start distance "
						+ (endMileP - endDistance) + " end distance "
						+ endMileP + " total time " + endTime);
			}
			totalTime = startTime + endTime + middleTime;
		}
		Debug.print("total time " + totalTime);
		return veloArray;
		// calculate the total time to reach there
	}

	/**
	 * @param speedRestrict
	 * @param veloProfile
	 * @return findIntersection
	 */
	public static double findIntersection(SpeedRestrictionFormat speedRestrict,
			VelocityProfile veloProfile) {
		double speed = speedRestrict.svelo;
		if (veloProfile.accelerationParameter != 0) {
			double intersect = (Math.pow(speed, 2) - Math.pow(
					veloProfile.startVelocity, 2))
					/ (2 * veloProfile.accelerationParameter);
			if ((intersect > speedRestrict.startMilePost)
					&& (intersect < speedRestrict.endMilePost)) {
				return intersect;
			}
			return -1;
		}
		return -1;
	}

	/**
	 * @param veloProfile1
	 * @param veloProfile2
	 * @return findIntersection
	 */
	public static double findIntersection(VelocityProfile veloProfile1,
			VelocityProfile veloProfile2) {
		if (veloProfile1.accelerationParameter != 0) {
			Debug.print(" startVelo " + veloProfile1.startVelocity
					+ " final VElo " + veloProfile2.endVelocity
					+ " start distance " + veloProfile1.startLength
					+ " end distance " + veloProfile2.endLength
					+ " accel start " + veloProfile1.accelerationParameter
					+ " accel end" + veloProfile2.accelerationParameter);
			double intersect = (Math.pow(veloProfile2.endVelocity, 2)
					- Math.pow(veloProfile1.startVelocity, 2) - (2 * (veloProfile2.accelerationParameter) * (veloProfile2.endLength - veloProfile1.startLength)))
					/ (2 * (veloProfile1.accelerationParameter - veloProfile2.accelerationParameter));
			if ((intersect > 0)
					&& (intersect < (veloProfile2.endLength - veloProfile1.startLength))) {
				return intersect;
			}
			return -1;
		}
		return -1;
	}

}

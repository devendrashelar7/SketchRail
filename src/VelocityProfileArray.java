import java.util.ArrayList;
import java.util.Collections;

import util.Debug;

/**
 * Class VelocityProfileArray
 */
class VelocityProfileArray extends ArrayList<VelocityProfile> {

	/**
	 * runTime
	 */
	double runTime = 0;

	/**
	 * constructor.
	 */
	public VelocityProfileArray() {
	}

	/**
	 * 1) It calculates velocity of the train for every tinyblock in the middle
	 * of the blocks. Depending upon the direction of the train it can predict
	 * the velocity of the train in a particular region considering the speed
	 * restrictions and newtonian laws of motion. It uses Max_forward and
	 * Max_back arrays of velocities to handle the various speeds of the train
	 * in the tinyBlockRegions depending upon the direction of the train in
	 * forward or backward direction. 2) After calculating the velocities at the
	 * end of the tinyBlocks it calls calVelocity(first1, last1, (accel +
	 * tempTinyBlock.accelerationChange), (deccel +
	 * tempTinyBlock.decelerationChange), tempTinyBlockSpeed,
	 * currentBlockStartMilePost, tempTinyBlock.endMilePost); with proper
	 * parameters to finetune the acceleration and deceleration parts of the
	 * tinyBlock. calVelocity returns a velocityProfileArray consisting of 3
	 * parts accelerating, constant velocity and decelerating part which is then
	 * added to the main VelocityProfileArray of the constructor.
	 * 
	 * @param train
	 * @param currBlock
	 * @param startVelocity
	 * @param finalVelocity
	 */
	public VelocityProfileArray(Train train, Block currBlock,
			double startVelocity, double finalVelocity) {

		// System.out.println("Train " + train.trainNo + " blockNo "
		// + currBlock.blockNo + " startVelocity " + startVelocity
		// + " finalVelocity " + finalVelocity);

		double tempFinalVelocity = finalVelocity;

		Debug.print("VelocityProfileArray: Constructor");
		Debug.print("VelocityProfileArray: trainNo = " + train.trainNo);
		Debug.print("VelocityProfileArray: currBlock = " + currBlock.blockNo);
		Debug.print("VelocityProfileArray: startVelocity = " + startVelocity);
		Debug.print("VelocityProfileArray: finalVelocity = " + finalVelocity);

		Collections.sort(currBlock.tinyBlock.tinyBlockArray,
				new SortTinyBlock());

		// ArrayList veloProfileArray = new ArrayList();
		VelocityProfileArray tempVeloProfileArray = new VelocityProfileArray();

		Debug.print("VelocityProfileArray: train.accParam " + train.accParam);
		Debug.print("VelocityProfileArray: train.deceParam " + train.deceParam);

		// double startVelo = 0, finalVelo = 0;
		double accel = 0, deccel = 0;

		// TinyBlockFormat tins = new TinyBlockFormat();
		TinyBlockFormat nextTinyBlock = new TinyBlockFormat();
		TinyBlockFormat previousTinyBlock = new TinyBlockFormat();
		TinyBlockFormat tempTinyBlock = new TinyBlockFormat();

		Debug.print("VelocityProfileArray: PROFILE needs to be found  between ");
		Debug.print("currBlock.startMilePost = " + currBlock.startMilePost
				+ " currBlock.endMilePost = " + currBlock.endMilePost
				+ " startVelocity " + startVelocity + " finalVelocity "
				+ tempFinalVelocity);

		Debug.print(" tinys in this region are ------- >>>>> ");

		ArrayList<TinyBlockFormat> currentBlockTinyBlockArray = currBlock.tinyBlock.tinyBlockArray;
		// for (int i = 0; i < currentBlockTinyBlockArray.size(); i++) {
		// tempTinyBlock = currentBlockTinyBlockArray.get(i);
		//
		// System.out.println("tinyBlock is	" + tempTinyBlock.startMilePost
		// + "	" + tempTinyBlock.endMilePost + "	"
		// + tempTinyBlock.maxSpeed + "	"
		// + tempTinyBlock.accelerationChange + "	"
		// + tempTinyBlock.decelerationChange + "   ");
		// }

		double Max_forward[] = new double[currentBlockTinyBlockArray.size() + 1];
		double Max_back[] = new double[currentBlockTinyBlockArray.size() + 1];

		if (train.direction == 0) {
			// startVelo = startVelocity;
			// finalVelo = tempFinalVelocity;
			Max_forward[0] = startVelocity;
			Max_back[currentBlockTinyBlockArray.size()] = tempFinalVelocity;
		} else {
			Max_forward[0] = tempFinalVelocity;
			Max_back[currentBlockTinyBlockArray.size()] = startVelocity;
			// startVelo = tempFinalVelocity;
			// tempFinalVelocity = startVelocity;
			// finalVelo = startVelocity;
		}

		Debug.print("VelocityProfileArray: NEW PROFILE WITH THE NEW FUNCTION ");

		// double a = startVelocity;
		double currentBlockStartMilePost = currBlock.startMilePost;
		double currentBlockEndMilePost = currBlock.endMilePost;

		// double b = tempFinalVelocity;
		// ArrayList ui = new ArrayList();
		Debug.print("VelocityProfileArray: startVelocity = " + startVelocity
				+ " final velocity = " + tempFinalVelocity
				+ " currentBlockStartMilePost = " + currentBlockStartMilePost
				+ " currentBlockEndMilePost = " + currentBlockEndMilePost);

		double acce, dece, nextTinyMaxSpeed, previousTinyMaxSpeed, first1 = 0, last1 = 0;

		if (train.direction == 0) {
			accel = train.accParam;
			deccel = train.deceParam;
		} else {
			accel = train.deceParam;
			deccel = train.accParam;
		}

		int cou = 1;

		if (currentBlockTinyBlockArray.size() > 0)
			nextTinyBlock = currentBlockTinyBlockArray.get(cou - 1);

		while (cou < currentBlockTinyBlockArray.size()) {
			nextTinyBlock = currentBlockTinyBlockArray.get(cou);
			previousTinyBlock = currentBlockTinyBlockArray.get(cou - 1);
			previousTinyMaxSpeed = previousTinyBlock.maxSpeed;
			Debug.print("VelocityProfileArray: this block values"
					+ " tint.startMilePost = " + nextTinyBlock.startMilePost
					+ " tint.endMilePost = " + nextTinyBlock.endMilePost
					+ " tint.maxSpeed = " + nextTinyBlock.maxSpeed);

			Debug.print("VelocityProfileArray: previous block values"
					+ " start " + previousTinyBlock.startMilePost + " end "
					+ previousTinyBlock.endMilePost + " maxSpeed "
					+ previousTinyBlock.maxSpeed + " acc "
					+ previousTinyBlock.accelerationChange + " dece "
					+ previousTinyBlock.decelerationChange);

			if (previousTinyMaxSpeed <= 0)
				previousTinyMaxSpeed = train.maximumPossibleSpeed;

			nextTinyMaxSpeed = nextTinyBlock.maxSpeed;
			if (nextTinyMaxSpeed <= 0)
				nextTinyMaxSpeed = train.maximumPossibleSpeed;

			if (train.direction == 0) {
				acce = (previousTinyBlock.accelerationChange + accel);
				dece = (previousTinyBlock.decelerationChange + deccel);
			} else {
				acce = (previousTinyBlock.decelerationChange + accel);
				dece = (previousTinyBlock.accelerationChange + deccel);
			}

			acce = Math.max(acce, 0);
			dece = Math.max(dece, 0);

			Debug.print("VelocityProfileArray: ");
			Debug.print("acce = " + acce + " tinn.accelerationChange = "
					+ previousTinyBlock.accelerationChange + " accel " + accel);
			Debug.print("dece = " + dece + " tinn.decelerationChange = "
					+ previousTinyBlock.decelerationChange + " decel " + deccel);

			Debug.print("VelocityProfileArray: calculating with acce = " + acce
					+ " dece = " + dece + " max_forward[cou-1] "
					+ Max_forward[cou - 1] + " tinj " + nextTinyMaxSpeed
					+ " tinp " + previousTinyMaxSpeed + " cou " + cou);

			Debug.print("VelocityProfileArray: " + (Max_forward[cou - 1]));
			Debug.print("VelocityProfileArray: 2*acce " + (2 * acce));
			Debug.print("VelocityProfileArray: 2*a*s = "
					+ (2 * acce * (previousTinyBlock.endMilePost - previousTinyBlock.startMilePost)));

			Debug.print("VelocityProfileArray: u^2 = "
					+ (Math.pow(Max_forward[cou - 1], 2)));
			Debug.print("VelocityProfileArray: currentBlockStartMilePost = "
					+ currentBlockStartMilePost);

			if (cou != 1) {
				double vsquare = (Math.pow(Max_forward[cou - 1], 2))
						+ (2 * acce * Math.abs(previousTinyBlock.endMilePost
								- previousTinyBlock.startMilePost));
				Debug.print("VelocityProfileArray: vsquare = " + vsquare);

				first1 = Math.sqrt(vsquare);
			}
			if (cou == 1) {
				double twiceAccelerationDistance = 2
						* acce
						* Math.abs(previousTinyBlock.endMilePost
								- currentBlockStartMilePost);
				Debug.print("VelocityProfileArray: twiceAccelerationDistance = "
						+ twiceAccelerationDistance);

				double vsquare = (Math.pow(Max_forward[cou - 1], 2))
						+ twiceAccelerationDistance;
				Debug.print("VelocityProfileArray: vsquare = " + vsquare);
				first1 = Math.sqrt(vsquare);
			}
			Debug.print(" Calculated  Max velocity at the end of first block is   "
					+ first1);

			first1 = Math.min(first1, nextTinyMaxSpeed);
			first1 = Math.min(first1, previousTinyMaxSpeed);
			Debug.print(" train.maximumPossibleSpeed  "
					+ train.maximumPossibleSpeed);

			first1 = Math.min(first1, train.maximumPossibleSpeed);
			Debug.print(" Genuinely max is &&&&  " + first1);

			Max_forward[cou] = first1;
			cou++;
			Debug.print(" PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP ");
		}

		Debug.print("VelocityProfileArray: cou = " + cou
				+ " currentBlockTinyBlockArray = "
				+ currBlock.tinyBlock.tinyBlockArray.size());

		for (int uip = 0; uip <= currBlock.tinyBlock.tinyBlockArray.size(); uip++) {
			Debug.print("VelocityProfileArray: Max_forward{" + uip + "] "
					+ Max_forward[uip]);
		}
		Debug.print("VelocityProfileArray: LL " + nextTinyBlock.startMilePost
				+ "  PP " + nextTinyBlock.endMilePost);
		// Debug.print(" : "+Max_forward[cou-1]+"  ::   " +
		// (Math.pow(Max_forward[currBlock.tinyBlock.tinyblock_array.size()-1],2))+" ==  "+(Math.pow(Max_forward[cou-1],2)));
		// Debug.print(" ********* )))) "+(2*(tint.acchange+accel)*(tint.endmilep-tint.startmilep))+"   |||   "+(Math.pow((Max_forward[currBlock.tinyBlock.tinyblock_array.size()-1]),2)));

		if (train.direction == 0) {
			if (currBlock.tinyBlock.tinyBlockArray.size() > 0)
				Max_forward[currBlock.tinyBlock.tinyBlockArray.size()] = Math
						.sqrt((Math.pow(
								(Max_forward[currBlock.tinyBlock.tinyBlockArray
										.size() - 1]), 2))
								+ (2 * (nextTinyBlock.accelerationChange + accel) * (nextTinyBlock.endMilePost - nextTinyBlock.startMilePost)));

		} else {
			if (currBlock.tinyBlock.tinyBlockArray.size() > 0)
				Max_forward[currBlock.tinyBlock.tinyBlockArray.size()] = Math
						.sqrt((Math.pow(
								(Max_forward[currBlock.tinyBlock.tinyBlockArray
										.size() - 1]), 2))
								+ (2 * (nextTinyBlock.decelerationChange + accel) * (nextTinyBlock.endMilePost - nextTinyBlock.startMilePost)));
			// Max_back[currBlock.tinyBlock.tinyBlockArray.size()] = Math
			// .sqrt((Math.pow(
			// (Max_back[currBlock.tinyBlock.tinyBlockArray
			// .size() - 1]), 2))
			// + (2 * (nextTinyBlock.decelerationChange + accel) *
			// (nextTinyBlock.endMilePost - nextTinyBlock.startMilePost)));
		}

		int size = currBlock.tinyBlock.tinyBlockArray.size();
		Max_forward[size] = Math.min(Max_forward[size],
				currBlock.tinyBlock.tinyBlockArray.get(size - 1).maxSpeed);

		double uipq;
		if (nextTinyBlock.maxSpeed > 0)
			uipq = Math.min(train.maximumPossibleSpeed, nextTinyBlock.maxSpeed);
		else
			uipq = train.maximumPossibleSpeed;

		Max_forward[currBlock.tinyBlock.tinyBlockArray.size()] = Math.min(
				Max_forward[currBlock.tinyBlock.tinyBlockArray.size()], uipq);

		Debug.print("VelocityProfileArray: Max velocity at the final block is  "
				+ Max_forward[currBlock.tinyBlock.tinyBlockArray.size()]);
		Debug.print("VelocityProfileArray: MAX VELOCITIES AT THE BLOCKS ARE ");

		for (int uip = 0; uip <= currBlock.tinyBlock.tinyBlockArray.size(); uip++) {
			Debug.print("VelocityProfileArray: Max_forward[" + uip + "]"
					+ Max_forward[uip]);
		}

		Debug.print("VelocityProfileArray: backward calculation");

		cou = currBlock.tinyBlock.tinyBlockArray.size() - 1;

		while (cou >= 0 && currBlock.tinyBlock.tinyBlockArray.size() > 0) {
			nextTinyBlock = currBlock.tinyBlock.tinyBlockArray.get(cou);

			Debug.print("VelocityProfileArray: this block values are  "
					+ nextTinyBlock.startMilePost + "  "
					+ nextTinyBlock.endMilePost + "  " + nextTinyBlock.maxSpeed);

			nextTinyMaxSpeed = nextTinyBlock.maxSpeed;

			if (nextTinyMaxSpeed <= 0)
				nextTinyMaxSpeed = train.maximumPossibleSpeed;
			if (cou != 0)
				previousTinyBlock = currBlock.tinyBlock.tinyBlockArray
						.get(cou - 1);

			Debug.print("VelocityProfileArray: tinp values are  "
					+ previousTinyBlock.startMilePost + "  "
					+ previousTinyBlock.endMilePost + "  "
					+ previousTinyBlock.maxSpeed);

			previousTinyMaxSpeed = previousTinyBlock.maxSpeed;
			if (previousTinyMaxSpeed <= 0)
				previousTinyMaxSpeed = train.maximumPossibleSpeed;

			if (train.direction == 0) {
				acce = (nextTinyBlock.accelerationChange + accel);
				dece = (nextTinyBlock.decelerationChange + deccel);
			} else {
				acce = (nextTinyBlock.decelerationChange + accel);
				dece = (nextTinyBlock.accelerationChange + deccel);
			}

			acce = Math.max(acce, 0);
			dece = Math.max(dece, 0);

			// dece=(tint.dechange+deccel);
			// dece=Math.max(dece,0);
			Debug.print("VelocityProfileArray: calculating with acce = " + acce
					+ " dece = " + dece + " max_back[" + (cou + 1) + "] "
					+ Max_back[cou + 1] + "  __ " + cou);

			Debug.print("VelocityProfileArray:  ~~~~~~~~~  ");
			Debug.print("VelocityProfileArray: acce = " + acce
					+ " tint.accelerationChange "
					+ nextTinyBlock.accelerationChange + " accel " + accel);
			Debug.print("VelocityProfileArray: dece = " + dece
					+ " tint.decelerationChange = "
					+ nextTinyBlock.decelerationChange + " deccel = " + deccel);

			// if(train.Direction==1) dece=acce;
			if (cou != 0)
				last1 = Math
						.sqrt((Math.pow(Max_back[cou + 1], 2))
								+ (2 * dece * (nextTinyBlock.endMilePost - nextTinyBlock.startMilePost)));
			if (cou == 0)
				last1 = Math
						.sqrt((Math.pow(Max_back[cou + 1], 2))
								+ (2 * dece * (nextTinyBlock.endMilePost - currentBlockStartMilePost)));
			Debug.print("VelocityProfileArray: decelerating velocity is "
					+ last1);
			Debug.print("VelocityProfileArray: max.train speed is "
					+ train.maximumPossibleSpeed);

			last1 = Math.min(last1, nextTinyMaxSpeed);
			last1 = Math.min(last1, train.maximumPossibleSpeed);
			last1 = Math.min(last1, previousTinyMaxSpeed);

			Debug.print("VelocityProfileArray: genune is " + last1);
			Max_back[cou] = last1;
			cou--;
			Debug.print("VelocityProfileArray: PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP ");
		}

		// double accChange;
		// if (train.direction == 0)
		// accChange = nextTinyBlock.accelerationChange;
		// else
		// accChange = nextTinyBlock.decelerationChange;
		//
		// // todo
		// if (currBlock.tinyBlock.tinyBlockArray.size() > 0)
		// Max_back[size] = Math
		// .sqrt((Math.pow(
		// (Max_forward[currBlock.tinyBlock.tinyBlockArray
		// .size() - 1]), 2))
		// + (2 * (nextTinyBlock.accelerationChange + accel) *
		// (nextTinyBlock.endMilePost - nextTinyBlock.startMilePost)));

		Debug.print("VelocityProfileArray: out of while loop #################### ");
		// for (int uip = 0; uip <= currBlock.tinyBlock.tinyBlockArray.size();
		// uip++) {
		// int index = Math.min(uip,
		// currBlock.tinyBlock.tinyBlockArray.size() - 1);
		// TinyBlockFormat tbf = currBlock.tinyBlock.tinyBlockArray.get(index);
		// System.out.println("index: " + uip + " forward " + Max_forward[uip]
		// + " back " + Max_back[uip] + " start " + tbf.startMilePost
		// + " end " + tbf.endMilePost);
		// }

		Debug.print("VelocityProfileArray: velocity profile calculation   ");

		// VelocityProfileArray temp = new VelocityProfileArray();
		// int uyr;
		double tempTinyBlockSpeed;
		for (int ip = 0; ip < currBlock.tinyBlock.tinyBlockArray.size(); ip++) {

			tempTinyBlock = currBlock.tinyBlock.tinyBlockArray.get(ip);

			tempTinyBlockSpeed = tempTinyBlock.maxSpeed;
			if (tempTinyBlockSpeed <= 0)
				tempTinyBlockSpeed = train.maximumPossibleSpeed;

			tempTinyBlockSpeed = Math.min(tempTinyBlockSpeed,
					train.maximumPossibleSpeed);

			// System.out.println("first1: forward " + Max_forward[ip] +
			// " back "
			// + Max_back[ip]);
			first1 = Math.min(Max_forward[ip], Max_back[ip]);

			// System.out.println("last1: forward " + Max_forward[ip + 1] +
			// " back "
			// + Max_back[ip + 1]);
			last1 = Math.min(Max_forward[ip + 1], Max_back[ip + 1]);

			Debug.print("VelocityProfileArray: Going to tempprofile with tou.startMilePost = "
					+ tempTinyBlock.startMilePost
					+ " tou.endMilePost = "
					+ tempTinyBlock.endMilePost);

			Debug.print("VelocityProfileArray: train.direction = "
					+ train.direction + " ip = " + ip);

			if (train.direction == 0) {
				if (ip != 0) {
					System.out.println("calling calVelocity: first1 " + first1
							+ " last1 " + last1 + " startMilePost "
							+ tempTinyBlock.startMilePost + " endMilePost "
							+ tempTinyBlock.endMilePost);
					tempVeloProfileArray = calVelocity(first1, last1,
							(accel + tempTinyBlock.accelerationChange),
							(deccel + tempTinyBlock.decelerationChange),
							tempTinyBlockSpeed, tempTinyBlock.startMilePost,
							tempTinyBlock.endMilePost);
				}
				if (ip == 0) {
					System.out.println("calling calVelocity: first1 " + first1
							+ " last1 " + last1 + " startMilePost "
							+ tempTinyBlock.startMilePost + " endMilePost "
							+ tempTinyBlock.endMilePost);
					tempVeloProfileArray = calVelocity(first1, last1,
							(accel + tempTinyBlock.accelerationChange),
							(deccel + tempTinyBlock.decelerationChange),
							tempTinyBlockSpeed, currentBlockStartMilePost,
							tempTinyBlock.endMilePost);
				}
			}
			if (train.direction == 1) {
				if (ip != 0) {
					// System.out.println("calling calVelocity: first1 " +
					// first1
					// + " last1 " + last1 + " startMilePost "
					// + tempTinyBlock.startMilePost + " endMilePost "
					// + tempTinyBlock.endMilePost);

					tempVeloProfileArray = calVelocity(first1,
							last1,
							(accel + tempTinyBlock.decelerationChange),
							(deccel + tempTinyBlock.accelerationChange),
							// (deccel + tempTinyBlock.decelerationChange),
							// (accel + tempTinyBlock.accelerationChange),
							tempTinyBlockSpeed, tempTinyBlock.startMilePost,
							tempTinyBlock.endMilePost);
				}
				if (ip == 0) {
					// System.out.println("calling calVelocity: first1 " +
					// first1
					// + " last1 " + last1 + " startMilePost "
					// + tempTinyBlock.startMilePost + " endMilePost "
					// + tempTinyBlock.endMilePost);

					tempVeloProfileArray = calVelocity(first1,
							last1,
							(accel + tempTinyBlock.decelerationChange),
							(deccel + tempTinyBlock.accelerationChange),
							// (deccel + tempTinyBlock.decelerationChange),
							// (accel + tempTinyBlock.accelerationChange),
							tempTinyBlockSpeed, currentBlockStartMilePost,
							tempTinyBlock.endMilePost);
				}
			}

			Debug.print(" ############# &&&&&&&&&&&&&&&&&&&&&& ");
			Debug.print("came back from calVelocity ");
			for (int yt = 0; yt < tempVeloProfileArray.size(); yt++) {
				Debug.print(tempVeloProfileArray.get(yt).startLength + "   "
						+ (tempVeloProfileArray.get(yt)).endLength + "   "
						+ (tempVeloProfileArray.get(yt)).startVelocity + "   "
						+ (tempVeloProfileArray.get(yt)).endVelocity);
				this.add(tempVeloProfileArray.get(yt));
			}
			Debug.print(" EEEEEEEEEEEEEEEEEEEEEEEE  ");
		}

		Debug.print("***************");

		Debug.print("NEw profile is");
		for (int ip = 0; ip < tempVeloProfileArray.size(); ip++) {
			Debug.print((tempVeloProfileArray.get(ip)).startLength + "   "
					+ (tempVeloProfileArray.get(ip)).endLength + "   "
					+ (tempVeloProfileArray.get(ip)).startVelocity + "   "
					+ (tempVeloProfileArray.get(ip)).endVelocity);
		}
		Debug.print(" &&&&&&&&&&&&&   ");
	}

	/**
	 * overriding method for add.
	 * 
	 * @param obj
	 * @return true if the obj was added.
	 */

	public boolean add(VelocityProfile obj) {
		VelocityProfile vProf = obj;
		runTime += vProf.time;
		if (vProf.time == 0.0) {
			// System.out
			// .println("Velocity profile has time 0.0, hence not added");
			return true;
		}
		return super.add(vProf);
	}

	/**
	 * @param k
	 * @param obj
	 * 
	 */
	public void add(int k, VelocityProfile obj) {
		VelocityProfile vProf = obj;
		runTime += vProf.time;
		super.add(k, vProf);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#remove(int)
	 */
	public VelocityProfile remove(int i) {
		Debug.print(" In remove function " + i);
		VelocityProfile vProf = super.remove(i);
		runTime -= vProf.time;
		return vProf;
	}

	/**
	 * @param initVelocity
	 * @param finalVelocity
	 * @param accel
	 * @param deccel
	 * @param maxVelocity
	 * @param startMileP
	 * @param endMileP
	 * @return {@link VelocityProfileArray} calculated after feeding all the
	 *         parameters
	 */
	public static VelocityProfileArray calVelocity(double initVelocity,
			double finalVelocity, double accel, double deccel,
			double maxVelocity, double startMileP, double endMileP) {

		 System.out.println("VelocityProfileArray: calVelocity: startvelo "
		 + initVelocity + " finalVelo " + finalVelocity
		 + " maxvelocity " + maxVelocity + "  acc  " + accel
		 + " deccel " + deccel + " startMilePost " + startMileP
		 + " endMilePost " + endMileP);
		finalVelocity = Math.min(finalVelocity, maxVelocity);
		initVelocity = Math.min(initVelocity, maxVelocity);

		double totalTime = 0;
		VelocityProfileArray velocityProfileArray = new VelocityProfileArray();

		if (endMileP > startMileP) {
			double length = endMileP - startMileP;
			Debug.print("VelocityProfileArray: calVelocity: length is = "
					+ length);

			// cal distance to reach max velocity from initVElocity
			double startDistance = 0;

			// System.out.println("maxVelocity "+maxVelocity+" initialVelocity "+initVelocity);
			if (maxVelocity >= initVelocity)
				startDistance = (Math.pow(maxVelocity, 2) - Math.pow(
						initVelocity, 2)) / (2 * accel);

			Debug.print("VelocityProfileArray: calVelocity: start distance is = "
					+ startDistance);

			// cal distance to reach final velocity from max velocity
			double endDistance = (Math.pow(finalVelocity, 2) - Math.pow(
					maxVelocity, 2)) / (2 * (-deccel));

			Debug.print("VelocityProfileArray: calVelocity: end distance is = "
					+ endDistance);

			// check distance
			if (endDistance > (length - startDistance)) {// if greater then
				// calculate distance
				// when velocity equal
				// check if the how much distance it take for the velocity to
				// become
				// eqaul to the end velo and the
				// System.out.println("I am hugging here");

				double initEndDistance = (Math.pow(finalVelocity, 2) - Math
						.pow(initVelocity, 2)) / (2 * (-deccel));
				// System.out.println("initEndDistance " + initEndDistance
				// + " length " + length + " initVelocity " + initVelocity
				// + " finalVelocity " + finalVelocity + " deccel "
				// + deccel);
				if (initEndDistance > length) {

					// cal start velo and total time
					double startVelo = Math.sqrt(Math.abs(Math.pow(
							finalVelocity, 2) - 2 * -deccel * length));
					double startTime = (finalVelocity - startVelo) / (-deccel);
					System.out.println("Case 1");
					velocityProfileArray.add(new VelocityProfile(startMileP,
							endMileP, startVelo, finalVelocity, startTime,
							-deccel));
					// System.out
					// .println("VelocityProfileArray: calVelocity: startVelo = "
					// + startVelo);
					// System.out
					// .println("VelocityProfileArray: calVelocity: finalVelo = "
					// + finalVelocity
					// + " total time "
					// + startTime);
				} else {
					// System.out.println("Sorry rahega");
					double initStartDistance = (Math.pow(finalVelocity, 2) - Math
							.pow(initVelocity, 2)) / (2 * accel);
					if (initStartDistance > length) {
						// System.out.println("initStartDistance > length");
						// cal final velo and total time
						double endVelo = Math.sqrt(Math.pow(initVelocity, 2)
								+ 2 * accel * length);
						double startTime = (endVelo - initVelocity) / (accel);// mj
						System.out.println("Case 2");
						velocityProfileArray.add(new VelocityProfile(
								startMileP, endMileP, initVelocity, endVelo,
								startTime, accel));
						Debug.print(" 2 : startVelo " + initVelocity
								+ " finalVelo " + endVelo + " total time "
								+ startTime);
					} else {
						// System.out.println("initStartDistance < length");
						startDistance = (Math.pow(finalVelocity, 2)
								- Math.pow(initVelocity, 2) - (2 * (-deccel) * length))
								/ (2 * (accel - (-deccel)));
						endDistance = length - startDistance;
						double interimVelo = Math.sqrt(Math
								.pow(initVelocity, 2)
								+ 2
								* accel
								* startDistance);

						// calculate time
						double startTime = (interimVelo - initVelocity)
								/ (accel);// mj
						System.out.println("Case 3");
						velocityProfileArray.add(new VelocityProfile(
								startMileP, startMileP + startDistance,
								initVelocity, interimVelo, startTime, accel));

						Debug.print(" 3:1 : startVelo " + initVelocity
								+ " final VElo " + interimVelo + " total time "
								+ startTime);

						double endTime = (finalVelocity - interimVelo)
								/ ((-deccel));// mj
						System.out.println("Case 4");
						velocityProfileArray.add(new VelocityProfile(startMileP
								+ startDistance, endMileP, interimVelo,
								finalVelocity, endTime, -deccel));

						Debug.print(" 3:2 : startVelo " + interimVelo
								+ " final VElo " + finalVelocity
								+ " start distance "
								+ (startMileP + startDistance)
								+ " end distance " + length + " total time "
								+ endTime);
						totalTime = startTime + endTime;
					}
				}
			} else {
				double startTime = (maxVelocity - initVelocity) / (accel);

				if (startTime != 0) {
					if (startDistance > 0){
						System.out.println("Case 5");
						velocityProfileArray.add(new VelocityProfile(
								startMileP, startMileP + startDistance,
								initVelocity, maxVelocity, startTime, accel));
					}

					Debug.print(" 4:1 : startVelo " + initVelocity
							+ " final VElo " + maxVelocity + " start distance "
							+ startMileP + " end distance "
							+ (startMileP + startDistance) + " total time "
							+ startTime);

				}
				double endTime = (finalVelocity - maxVelocity) / ((-deccel));// mj

				double middleTime = (length - startDistance - endDistance)
						/ maxVelocity;

				if (middleTime != 0) {
					System.out.println("Case 6");
					velocityProfileArray.add(new VelocityProfile(startMileP
							+ startDistance, endMileP - endDistance,
							maxVelocity, maxVelocity, middleTime, 0));
					System.out.println(" 4:3 : startVelo " + maxVelocity
							+ " finalVelo " + maxVelocity + " start distance "
							+ (startMileP + startDistance) + " end distance "
							+ (endMileP - endDistance) + " total time "
							+ middleTime);
				}

				if (endTime != 0) {
					System.out.println("Case 7");
					velocityProfileArray.add(new VelocityProfile(endMileP
							- endDistance, endMileP, maxVelocity,
							finalVelocity, endTime, -deccel));

					System.out.println(" 4:2 : startVelo " + maxVelocity
							+ " final VElo " + finalVelocity
							+ " start distance " + (endMileP - endDistance)
							+ " end distance " + endMileP + " total time "
							+ endTime);
				}
				totalTime = startTime + endTime + middleTime;
			}
			Debug.print("total time " + totalTime + " veloarray size is "
					+ velocityProfileArray.size());
		}

		if (endMileP < startMileP) {
			double length = startMileP - endMileP;

			Debug.print("VelocityProfileArray: calVelocity: lengths = "
					+ length);

			// cal distance to reach max velocity from initVElocity
			double startDistance = 0;
			if (maxVelocity >= initVelocity)
				startDistance = (Math.pow(maxVelocity, 2) - Math.pow(
						initVelocity, 2)) / (2 * accel);

			Debug.print("VelocityProfileArray: calVelocity: start distance is   -->  "
					+ startDistance);

			// cal distance to reach final velocity from max velocity

			double endDistance = (Math.pow(finalVelocity, 2) - Math.pow(
					maxVelocity, 2)) / (2 * (-deccel));

			Debug.print("end t distace is   -->  " + endDistance);

			// check distance
			if (startDistance > (length - endDistance)) {// if greater then
				// calculate distance
				// when velocity equal
				// ----------------
				// check if the how much distance it take for the velocity to
				// become
				// eqaul to the end velo and the

				double initEndDistance = (Math.pow(finalVelocity, 2) - Math
						.pow(initVelocity, 2)) / (2 * (-deccel));
				if (initEndDistance > length) {

					// cal start velo and total time
					double startVelo = Math.sqrt(Math.pow(finalVelocity, 2) - 2
							* -deccel * length);
					double startTime = (finalVelocity - startVelo) / (-deccel);
					System.out.println("Case 8");
					velocityProfileArray.add(new VelocityProfile(endMileP,
							startMileP, startVelo, finalVelocity, startTime,
							-deccel));

					Debug.print(" 1 : startVelo " + startVelo + " final VElo "
							+ finalVelocity + " total time " + startTime);
				} else {
					double initStartDistance = (Math.pow(finalVelocity, 2) - Math
							.pow(initVelocity, 2)) / (2 * accel);
					if (initStartDistance > length) {

						// cal final velo and total time
						double endVelo = Math.sqrt(Math.pow(initVelocity, 2)
								+ 2 * accel * length);

						double startTime = (endVelo - initVelocity) / (accel);// mj
						System.out.println("Case 9");
						velocityProfileArray.add(new VelocityProfile(endMileP,
								startMileP, initVelocity, endVelo, startTime,
								accel));

						Debug.print(" 2 : startVelo " + initVelocity
								+ " final VElo " + endVelo + " total time "
								+ startTime);
					} else {
						startDistance = (Math.pow(finalVelocity, 2)
								- Math.pow(initVelocity, 2) - (2 * (-deccel) * length))
								/ (2 * (accel - (-deccel)));

						endDistance = length - startDistance;
						double interimVelo = Math.sqrt(Math
								.pow(initVelocity, 2)
								+ 2
								* accel
								* startDistance);

						// calculate time
						double startTime = (interimVelo - initVelocity)
								/ (accel);// mj
						System.out.println("Case 10");
						velocityProfileArray.add(new VelocityProfile(endMileP,
								endMileP - startDistance, initVelocity,
								interimVelo, startTime, accel));

						Debug.print(" 3:1 : startVelo " + initVelocity
								+ " final VElo " + interimVelo + " total time "
								+ startTime);

						double endTime = (finalVelocity - interimVelo)
								/ ((-deccel));// mj

						System.out.println("Case 11");
						velocityProfileArray.add(new VelocityProfile(endMileP
								- startDistance, endMileP, interimVelo,
								finalVelocity, endTime, -deccel));

						Debug.print(" 3:2 : startVelo " + interimVelo
								+ " final VElo " + finalVelocity
								+ " start distance "
								+ (startMileP + startDistance)
								+ " end distance " + length + " total time "
								+ endTime);
						totalTime = startTime + endTime;
					}
				}
			} else {
				double startTime = (maxVelocity - initVelocity) / (accel);// mj
				if (startTime != 0) {
					if (startDistance > 0)

						velocityProfileArray.add(new VelocityProfile(endMileP,
								endMileP - startDistance, initVelocity,
								maxVelocity, startTime, accel));

					Debug.print(" 4:1 : startVelo " + initVelocity
							+ " final VElo " + maxVelocity + " start distance "
							+ startMileP + " end distance "
							+ (startMileP + startDistance) + " total time "
							+ startTime);

				}
				double endTime = (finalVelocity - maxVelocity) / ((-deccel));// mj

				double middleTime = (length - startDistance - endDistance)
						/ maxVelocity;
				if (middleTime != 0) {
					velocityProfileArray.add(new VelocityProfile(endMileP
							- startDistance, endMileP + endDistance,
							maxVelocity, maxVelocity, middleTime, 0));

					Debug.print(" 4:3 : startVelo " + maxVelocity
							+ " final VElo " + maxVelocity + " start distance "
							+ (startMileP + startDistance) + " end distance "
							+ (endMileP - endDistance) + " total time "
							+ middleTime);
				}

				if (endTime != 0) {
					velocityProfileArray.add(new VelocityProfile(endMileP
							+ endDistance, endMileP, maxVelocity,
							finalVelocity, endTime, -deccel));

					Debug.print(" 4:2 : startVelo " + maxVelocity
							+ " final VElo " + finalVelocity
							+ " start distance " + (endMileP - endDistance)
							+ " end distance " + endMileP + " total time "
							+ endTime);
				}
				totalTime = startTime + endTime + middleTime;
			}
			Debug.print("total time " + totalTime + " veloarray size is "
					+ velocityProfileArray.size());
		}

		// velocityProfileArray.print();
		// System.out.println();

		return velocityProfileArray;
		// calculate the total time to reach there

	}

	/**
	 * VelocityProfileArray: VelocityProfileArray returnInterval(double
	 * startDistance, double endDistance) 1) It runs through the
	 * velocityProfiles in the current velocityProfile and keeps on adding time
	 * for each of the velocityProfile. 2) It keeps adding the velocityProfiles
	 * in the current velocityProfile to a new blockVelocityProfileArray so that
	 * it can get the velocity descriptions at the beginning, middle and end of
	 * this particular current Block. If the GlobalVar.overlap is true:
	 * velocityprofilearray1 = velocityprofilearray.returnInterval(
	 * GlobalVar.overlapStartDistance, GlobalVar.overlapEndDistance); return new
	 * RunTimeReturn(velocityprofilearray1.runTime, 0.0D,
	 * velocityprofilearray1);
	 * 
	 * @param startDistance
	 * @param endDistance
	 * @return {@link VelocityProfileArray}
	 */
	public VelocityProfileArray returnInterval(double startDistance,
			double endDistance) {
		// System.out.println("profile size " + this.size() + " startDistance "
		// + startDistance + " endDistance " + endDistance);
		VelocityProfileArray blkVeloProfileArray = new VelocityProfileArray();
		double totalTime = 0;

		Debug.print(" I am in velocityprofile.return interval  ");
		Debug.print(" profile under consdieration is ");

		for (int currProfile = 0; currProfile < size(); currProfile++) {
			VelocityProfile currVeloProfile = get(currProfile);

			Debug.print("VEloProfile start length is "
					+ currVeloProfile.startLength + " end length is  "
					+ currVeloProfile.endLength + " start velo is "
					+ currVeloProfile.startVelocity + " end velo is "
					+ currVeloProfile.endVelocity);
		}

		for (int currProfile = 0; currProfile < size(); currProfile++) {

			VelocityProfile currVeloProfile = get(currProfile);

			// System.out.println("VEloProfile start length is "
			// + currVeloProfile.startLength + " end length is  "
			// + currVeloProfile.endLength);
			//
			// System.out.println("VEloProfile should be cut between strt "
			// + startDistance + " end " + endDistance);

			if (currVeloProfile.startLength < endDistance) {

				Debug.print("1");
				if (currVeloProfile.startLength >= startDistance) {

					Debug.print("2");
					if (currVeloProfile.endLength <= endDistance) {

						Debug.print("3");
						Debug.print("Adding directly ");
						totalTime += currVeloProfile.time;
						blkVeloProfileArray.add(get(currProfile));

					} else {
						Debug.print("4");
						VelocityProfile veloProfile = get(currProfile);

						Debug.print(" rajeshend In VEloProfilesize "
								+ veloProfile.accelerationParameter);

						double endVelo = Math.sqrt(Math.pow(
								veloProfile.startVelocity, 2)
								+ 2
								* veloProfile.accelerationParameter
								* (endDistance - veloProfile.startLength));

						double endTime;
						if (veloProfile.accelerationParameter == 0) {
							Debug.print("5");
							endTime = (endDistance - veloProfile.startLength)
									/ endVelo;
							Debug.print("  ****** end EVlo" + endVelo
									+ "  start velo "
									+ veloProfile.startVelocity + " end tiome "
									+ endTime);
						} else {
							Debug.print("6");
							endTime = (endVelo - veloProfile.startVelocity)
									/ (veloProfile.accelerationParameter);
						}

						totalTime += endTime;
						blkVeloProfileArray.add(new VelocityProfile(
								veloProfile.startLength, endDistance,
								veloProfile.startVelocity, endVelo, endTime,
								veloProfile.accelerationParameter));
						// /check this???
					}
				} else {
					Debug.print("7");
					if (currVeloProfile.endLength >= startDistance) {
						Debug.print("8");

						double startVelo = Math.sqrt(Math.pow(
								currVeloProfile.endVelocity, 2)
								- 2
								* currVeloProfile.accelerationParameter
								* (currVeloProfile.endLength - startDistance));
						Debug.print(" rajeshstart In VEloProfilesize "
								+ currVeloProfile.accelerationParameter
								+ " start VElocity  " + startVelo);
						double startTime;

						if (currVeloProfile.accelerationParameter == 0) {
							Debug.print("9");
							startTime = (currVeloProfile.endLength - startDistance)
									/ startVelo;
						} else {
							Debug.print("10");
							startTime = (currVeloProfile.endVelocity - startVelo)
									/ (currVeloProfile.accelerationParameter);// mj
						}
						totalTime += startTime;
						blkVeloProfileArray.add(new VelocityProfile(
								startDistance, currVeloProfile.endLength,
								startVelo, currVeloProfile.endVelocity,
								startTime,
								currVeloProfile.accelerationParameter));
					}
				}
			}
		}

		blkVeloProfileArray.runTime = totalTime;
		Debug.print("!!!##########  " + blkVeloProfileArray.size());
		for (int o = 0; o < blkVeloProfileArray.size(); o++) {
			Debug.print("  " + ((blkVeloProfileArray.get(o)).startLength)
					+ "   " + ((blkVeloProfileArray.get(o)).endLength) + "  "
					+ ((blkVeloProfileArray.get(o)).startVelocity) + "  "
					+ ((blkVeloProfileArray.get(o)).endVelocity));
		}
		return blkVeloProfileArray;
	}

	/**
	 * @param veloProfArray
	 */

	public void addAll(VelocityProfileArray veloProfArray) {
		addAll((ArrayList<VelocityProfile>) veloProfArray);
		runTime += veloProfArray.runTime;
	}

	/**
	 * @param train
	 * @return the endVelocity of the velocity profile array depending upon the
	 *         direction of the train
	 */
	public double getEndVelocity(Train train) {
		if (this.size() != 0) {
			if (train.direction == 0) {

				return (this.get(this.size() - 1)).endVelocity;
			} else {

				return (this.get(0)).startVelocity;
			}
		} else {
			return 0.0;
		}

	}

	/**
	 * @param train
	 * @return the startVelocity of the velocity profile array depending upon
	 *         the direction of the train
	 */
	public double getStartVelocity(Train train) {
		if (this.size() != 0) {
			if (train.direction == 0) {

				return (this.get(0)).startVelocity;
			} else {

				return (this.get(this.size() - 1)).endVelocity;
			}
		} else {
			return 0.0;
		}

	}

	public void print(int trainDirection) {
		for (VelocityProfile velocityProfile : this) {
			velocityProfile.print(trainDirection);
		}
	}

	public void remove(double startMilePost, double endMilePost) {
		int index = 0;
		while (this.size() > 0 && index < this.size()) {
			VelocityProfile vp = this.get(index);
			if (vp.containsMilePost(startMilePost)
					|| vp.containsMilePost(endMilePost)) {
				this.remove(index);
			} else
				index++;
		}
	}

	public static void main(String[] args) {
		VelocityProfile vp1 = new VelocityProfile(0, 1, 0, 0, 1, 0);
		VelocityProfile vp2 = new VelocityProfile(1, 2, 0, 0, 1, 0);
		VelocityProfile vp3 = new VelocityProfile(2, 3, 0, 0, 1, 0);
		VelocityProfile vp4 = new VelocityProfile(3, 4, 0, 0, 1, 0);
		VelocityProfileArray vpa = new VelocityProfileArray();
		vpa.add(vp1);
		vpa.add(vp2);
		vpa.add(vp3);
		vpa.add(vp4);

		vpa.remove(1.2, 2.5);
		vpa.print(0);
	}
}

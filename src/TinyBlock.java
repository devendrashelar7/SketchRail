import java.util.ArrayList;
import java.util.Arrays;

import util.Debug;

/**
 * Tiny Block.
 */
public class TinyBlock {
	/**
	 * tinyBlockArray
	 */
	ArrayList<TinyBlockFormat> tinyBlockArray;

	/**
	 * constructor
	 */
	public TinyBlock() {
		tinyBlockArray = new ArrayList<TinyBlockFormat>();
	}

	/**
	 * @param b
	 * @return creatiny
	 */
	
	public ArrayList<TinyBlockFormat> creatiny(Block b) {
		ArrayList<TinyBlockFormat> a = new ArrayList<TinyBlockFormat>();
		ArrayList<SpeedRestrictionFormat> speedRestrictionArrayList = b.speedRestriction.speedRestrictArray;
		ArrayList<GradientFormat> gradientArrayList = b.gradient.gradientArrayList;

		Debug.print("TinyBlock: creatiny: ");
		double st = 0, en = 0, ach = 0, dch = 0, maxsp;
		String dir, val;
		Debug.print("TinyBlock: creatiny: BLOCK NO UNDER CONSIDERATION IS   "
				+ b.blockNo + "   " + b.startMilePost + "   " + b.endMilePost);

		if (b.isLoop()) {
			maxsp = b.maximumPossibleSpeed;
		} else {
			//what the hell ????
			maxsp = -100;
			maxsp=b.maximumPossibleSpeed;
		}
		
		/***************** CONDITION 1 *********************/
		if (b.gradient.gradientArrayList.size() == 0
				&& b.speedRestriction.size() == 0) {
			Debug
					.print("TinyBlock: creatiny: ALERT: PLEASE CHECK UR INPUT. THERE ARE "
							+ "NEITHER SPEEDRESTRICTIONS NOR GRADIENTS");
		}

		/*************** CONDITION 2 ************************/
		if (b.gradient.gradientArrayList.size() > 0
				&& b.speedRestriction.size() == 0) {
			Debug.print("TinyBlock: creatiny: block number = " + b.blockNo);
			// maxsp=-100;
			Debug
					.print("TinyBlock: creatiny: speedRestrictionArray size is zero");

			for (int i = 0; i < b.gradient.gradientArrayList.size(); i++) {

				double maxStartMilePost = Math.max(b.startMilePost,
						gradientArrayList.get(i).startMilePost);
				double minEndMilePost = Math.min(b.endMilePost,
						gradientArrayList.get(i).endMilePost);
				Debug.print("TinyBlock: creatiny: start = " + maxStartMilePost);
				Debug.print("TinyBlock: creatiny: end = " + minEndMilePost);

				val = gradientArrayList.get(i).gradientValue;
				dir = gradientArrayList.get(i).direction;

				if (dir.equalsIgnoreCase("UP")) {
					Debug.print("TinyBlock: creatiny: GRADIENT DIRECTION IS "
							+ dir);
					ach = GradientEffectUp.returnAcceleration(val);
					dch = GradientEffectUp.returnDeceleration(val);
					Debug
							.print("TinyBlock: creatiny: value returned from acceleration return is  "
									+ ach + "   from deceleration is" + dch);
					if (ach == 0 || dch == 0) {
						Debug
								.print("TinyBlock: creatiny: PLEASE CHECK THE GRADIENT EFFECT FILE >>> VALUES ARE NOT FOUND ");
					}
				}

				if (dir.equalsIgnoreCase("DOWN")) {
					Debug.print("TinyBlock: creatiny: GRADIENT DIRECTION IS "
							+ dir);
					ach = GradientEffectDown.returnAcceleration(val);
					dch = GradientEffectDown.returnDeceleration(val);
					Debug
							.print("TinyBlock: creatiny: value returned from acceleration return is  "
									+ ach + "   from deceleration is" + dch);
					if (ach == 0 || dch == 0) {
						Debug
								.print("TinyBlock: creatiny: PLEASE CHECK THE GRADIENT EFFECT FILE >>> VALUES ARE NOT FOUND ");
					}
				}
				if (dir.equalsIgnoreCase("LEVEL")) {
					Debug.print("TinyBlock: creatiny: ON A LEVEL GROUND ");
					ach = 0;
					dch = 0;
				}
				Debug.print("TinyBlock: creatiny: adding " + maxStartMilePost
						+ "   " + minEndMilePost + "   " + ach + "   " + dch
						+ "   " + maxsp);
				a.add(new TinyBlockFormat(maxStartMilePost, minEndMilePost,
						ach, dch, maxsp));
			}
		}

		/****************** CONDITION 3 ************************/
		if (b.gradient.gradientArrayList.size() == 0
				&& b.speedRestriction.size() > 0) {
			Debug
					.print("TinyBlock: creatiny: ERROR.. IT IS FOUND THAT THERE IS NO GRADIENT IN THIS SECTION ");
		}

		/*************************** CONDITION 4 ****************/
		if (b.gradient.gradientArrayList.size() > 0
				&& b.speedRestriction.size() > 0) {
			double ar2sort[] = new double[(2 * (gradientArrayList.size() + speedRestrictionArrayList
					.size())) + 2];
			double sorted[] = new double[(2 * (gradientArrayList.size() + speedRestrictionArrayList
					.size())) + 2];

			Debug.print("TinyBlock: creatiny: initialized size of arrays is "
					+ (gradientArrayList.size()
							+ speedRestrictionArrayList.size() + 5));
			Debug.print("TinyBlock: creatiny: array length is   "
					+ ar2sort.length);

			for (int i = 0; i < sorted.length; i++) {
				sorted[i] = -100;
			}
			for (int i = 0; i < ar2sort.length; i++) {
				ar2sort[i] = -100;
			}

			int count = 0;
			ar2sort[count] = b.startMilePost;
			count++;
			ar2sort[count] = b.endMilePost;
			count++;
			for (int i = 0; i < gradientArrayList.size(); i++) {
				ar2sort[count] = ((gradientArrayList.get(i)).startMilePost);
				count++;
				ar2sort[count] = ((gradientArrayList.get(i)).endMilePost);
				count++;
			}

			for (int i = 0; i < speedRestrictionArrayList.size(); i++) {
				ar2sort[count] = speedRestrictionArrayList.get(i).startMilePost;
				count++;
				ar2sort[count] = speedRestrictionArrayList.get(i).endMilePost;
				count++;
			}
			Arrays.sort(ar2sort);

			int j = 0;
			Debug.print("TinyBlock: creatiny: Array size is " + ar2sort.length);
			for (int i = 0; i < ar2sort.length; i++) {
				Debug.print(i + "     " + ar2sort[i]);
			}

			for (int i = 0; i < ar2sort.length - 2; i++) {

				if (ar2sort[i] != ar2sort[i + 1]
						&& ar2sort[i] != ar2sort[i + 2]) {
					sorted[j] = ar2sort[i];
					Debug.print("TinyBlock: creatiny: Added elements is "
							+ sorted[j]);
					j++;
					Debug.print("TinyBlock: creatiny: J value is " + j);
				}
			}

			if (ar2sort[ar2sort.length - 2] != ar2sort[ar2sort.length - 1]) {
				sorted[j] = ar2sort[ar2sort.length - 2];
				Debug.print("TinyBlock: creatiny: Added element is "
						+ sorted[j]);
				j++;
				Debug.print("TinyBlock: creatiny: J value is " + j);

				sorted[j] = ar2sort[ar2sort.length - 1];
				j++;
				Debug.print("TinyBlock: creatiny: J value is " + j);
			}

			if (ar2sort[ar2sort.length - 2] == ar2sort[ar2sort.length - 1]) {
				sorted[j] = ar2sort[ar2sort.length - 1];
				j++;
				Debug.print("TinyBlock: creatiny: J value is " + j);
			}

			for (int i = 0; i < sorted.length; i++) {
				Debug.print("TinyBlock: creatiny:  " + sorted[i]);
			}

			GradientFormat gft = new GradientFormat();

			for (int i = 0; i < sorted.length - 1; i++) {
				if (sorted[i] > 0 && sorted[i + 1] > 0) {
					Debug.print("TinyBlock: creatiny: Gradient returned for   "
							+ sorted[i]
							+ "    "
							+ sorted[i + 1]
							+ "      "
							+ b.gradient.returnGradient(sorted[i],
									sorted[i + 1]));

					gft = b.gradient.returnGradient(sorted[i], sorted[i + 1]);

					Debug.print(gft.startMilePost + "    " + gft.endMilePost);
					val = gft.gradientValue;
					dir = gft.direction;

					Debug
							.print("TinyBlock: creatiny: GOING TO GET MAXSP       "
									+ dir);
					maxsp = b.speedRestriction.returnMaxsp(sorted[i],
							sorted[i + 1]);

					Debug.print("TinyBlock: creatiny: VALUE returend is "
							+ maxsp);
					st = sorted[i];
					en = sorted[i + 1];

					if (dir.equalsIgnoreCase("UP")) {
						Debug
								.print("TinyBlock: creatiny: GRADIENT DIRECTION IS "
										+ dir);
						ach = GradientEffectUp.returnAcceleration(val);
						dch = GradientEffectUp.returnDeceleration(val);
						Debug
								.print("TinyBlock: creatiny: value returned from acceleration return is  "
										+ ach + "   from deceleration is" + dch);
						if (ach == 0 || dch == 0) {
							Debug
									.print("TinyBlock: creatiny: PLEASE CHECK THE GRADIENT EFFECT FILE >>> VALUES ARE NOT FOUND ");
						}
					}

					if (dir.equalsIgnoreCase("DOWN")) {
						Debug
								.print("TinyBlock: creatiny: GRADIENT DIRECTION IS "
										+ dir);
						ach = GradientEffectDown.returnAcceleration(val);
						dch = GradientEffectDown.returnDeceleration(val);
						Debug
								.print("TinyBlock: creatiny: value returned from acceleration return is  "
										+ ach + "   from deceleration is" + dch);
						if (ach == 0 || dch == 0) {
							Debug
									.print("TinyBlock: creatiny: PLEASE CHECK THE GRADIENT EFFECT FILE >>> VALUES ARE NOT FOUND ");
						}
					}

					if (dir.equalsIgnoreCase("LEVEL")) {
						Debug.print("TinyBlock: creatiny: ON A LEVEL GROUND ");
						ach = 0;
						dch = 0;
					}
					Debug.print("TinyBlock: creatiny: adding " + st + "   "
							+ en + "   " + ach + "   " + dch + "   " + maxsp);
					a.add(new TinyBlockFormat(st, en, ach, dch, maxsp));
				}
			}
		}
		/** END OF 4th condition IF LOOP **/

		return a;
	}

	/**
	 * END OF THE FUNCTION CREATINY
	 * 
	 * @param distance
	 * @return {@link ArrayList}
	 * **/

	public ArrayList<TinyBlock> split(double distance) {
		Debug.print(" I am in tiny block split  function ");
		ArrayList<TinyBlock> returnArray = new ArrayList<TinyBlock>();
		TinyBlock tinyStart, tinyEnd;
		tinyStart = new TinyBlock();
		tinyEnd = new TinyBlock();
		double currEndMileP, currStartMileP;
		TinyBlockFormat tinyBlockFormat;

		for (int i = 0; i < tinyBlockArray.size(); i++) {
			tinyBlockFormat = tinyBlockArray.get(i);

			currStartMileP = tinyBlockFormat.startMilePost;
			currEndMileP = tinyBlockFormat.endMilePost;

			if (currStartMileP < distance) {
				if (currEndMileP <= distance) {
					tinyStart.add(tinyBlockFormat);
				} else {
					tinyStart.add(new TinyBlockFormat(
							tinyBlockFormat.startMilePost, distance,
							tinyBlockFormat.accelerationChange,
							tinyBlockFormat.decelerationChange,
							tinyBlockFormat.maxSpeed));
					tinyEnd.add(new TinyBlockFormat(distance,
							tinyBlockFormat.endMilePost,
							tinyBlockFormat.accelerationChange,
							tinyBlockFormat.decelerationChange,
							tinyBlockFormat.maxSpeed));
				}
			} else {
				tinyEnd.add(tinyBlockArray.get(i));
			}
		}
		returnArray.add(tinyStart);
		returnArray.add(tinyEnd);
		Debug.print("REturinign from split bloc kfunction ");
		for (int i = 0; i < returnArray.size(); i++) {
			Debug.print(" " + returnArray.get(i));
		}
		return returnArray;
	}

	/**
	 * @param tinyBlockFormat
	 */
	public void add(TinyBlockFormat tinyBlockFormat) {
		// Debug.print("going to ad tiny blocks       ");
		tinyBlockArray.add(tinyBlockFormat);
	}

	/**
	 * @param a
	 * @param b
	 * @return {@link TinyBlockFormat}
	 */
	public TinyBlockFormat returnTinyBlockArray(double a, double b) {
		Debug.print("I am in return tiny " + a + "     ==   " + b);

		TinyBlockFormat tui = new TinyBlockFormat();
		for (int i = 0; i < tinyBlockArray.size(); i++) {
			TinyBlockFormat ti = tinyBlockArray.get(i);

			Debug.print(" tiny block properties are " + ti.startMilePost
					+ "  **  " + ti.endMilePost);
			if (((a >= ti.startMilePost) && (b <= ti.endMilePost))
					|| (((ti.startMilePost >= a) && (ti.endMilePost <= b)))
					|| (((ti.startMilePost >= a) && (ti.endMilePost >= b) && (ti.startMilePost < b)))
					|| ((((ti.startMilePost <= a) && (ti.endMilePost <= b) && (ti.endMilePost > a))))) {
				Debug.print("case  1");
				tui = ti;

			}
		}
		Debug.print(" Returining  tiny block " + tui.startMilePost + "  __ "
				+ tui.endMilePost);
		return tui;
	}
}
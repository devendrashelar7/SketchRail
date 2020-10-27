import java.util.ArrayList;

import util.Debug;

/**
 * Gradient EffectUp
 */
public class GradientEffectUp {
	/**
	 * gradientEffectUpArray
	 */
	static ArrayList<GradientEffect> gradientEffectUpArray;

	/**
     * 
     */
	public GradientEffectUp() {
		gradientEffectUpArray = new ArrayList<GradientEffect>();
	}

	/**
	 * @param gradientEffect
	 */
	public void add(GradientEffect gradientEffect) {
		Debug.print("I am in add UP gradient EFFECT");
		gradientEffectUpArray.add(gradientEffect);
	}

	/**
	 * @param gradientValue
	 * @return acceleration
	 */
	public static double returnAcceleration(String gradientValue) {
		double acc = 0;
		String temp;
		Debug.print("GradientEffectUp: returnAcceleration: In return acceleration "
				+ "function of gradient effect up  ");
		for (int i = 0; i < gradientEffectUpArray.size(); i++) {
			temp = gradientEffectUpArray.get(i).gradientValue;
			if (gradientValue.equalsIgnoreCase(temp)) {
				acc = (gradientEffectUpArray.get(i).accelerationChange);
			}
		}
		Debug.print("GradientEffectUp: returnAcceleration: returning " + acc);
		return acc;
	}

	/**
	 * @param gradientValue
	 * @return deceleration
	 */
	public static double returnDeceleration(String gradientValue) {
		double dec = 0;
		String temp;
		Debug.print("GradientEffectUp: returnDeceleration: In return acceleration function"
				+ " of gradient effect up  ");
		for (int i = 0; i < gradientEffectUpArray.size(); i++) {
			temp = gradientEffectUpArray.get(i).gradientValue;
			if (gradientValue.equalsIgnoreCase(temp)) {
				dec = (gradientEffectUpArray.get(i).decelerationChange);
			}
		}
		Debug.print(" returning " + dec);
		return dec;
	}

}
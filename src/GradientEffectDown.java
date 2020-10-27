import java.util.ArrayList;

import util.Debug;

/**
 * 
 */
public class GradientEffectDown {
	/**
	 * gradientEffectDownArray
	 */

	static ArrayList gradientEffectDownArray;

	/**
	 * constructor.
	 */
	public GradientEffectDown() {
		gradientEffectDownArray = new ArrayList();
	}

	/**
	 * @param gradientEffect
	 */
	public void add(GradientEffect gradientEffect) {
		Debug.print("I am in add DOWN gradient EFFECT");
		gradientEffectDownArray.add(gradientEffect);
	}

	/**
	 * @param gra
	 * @return the acceleration parameter from the gradient
	 */
	public static double returnAcceleration(String gra) {
		double te = 0;
		String temp;
		Debug
				.print("In return acceleration function of gradient effect down  ");
		for (int i = 0; i < gradientEffectDownArray.size(); i++) {
			temp = ((GradientEffect) gradientEffectDownArray.get(i)).gradientValue;
			if (gra.equalsIgnoreCase(temp)) {
				te = (((GradientEffect) gradientEffectDownArray.get(i)).accelerationChange);
			}
		}
		Debug.print(" returning " + te);
		return te;
	}

	/**
	 * @param gra
	 * @return return deceleration from the gradient
	 */
	public static double returnDeceleration(String gra) {
		double te = 0;
		String temp;
		Debug
				.print("In return acceleration function of gradient effect down  ");
		for (int i = 0; i < gradientEffectDownArray.size(); i++) {
			temp = ((GradientEffect) gradientEffectDownArray.get(i)).gradientValue;
			if (gra.equalsIgnoreCase(temp)) {
				te = (((GradientEffect) gradientEffectDownArray.get(i)).decelerationChange);
			}
		}
		Debug.print(" returning " + te);
		return te;
	}

}
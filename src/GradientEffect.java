import java.util.*;
import util.*;
import java.io.*;

/**
 * Gradient Effect class.
 */
public class GradientEffect {

	/**
	 * gradientValue
	 */
	String gradientValue;
	/**
	 * direction
	 */
	String direction;
	/**
	 * accelerationChange
	 */
	double accelerationChange;
	/**
	 * decelerationChange
	 */
	double decelerationChange;

	/**
	 * @param a
	 * @param c
	 * @param d
	 */
	GradientEffect(String a, double c, double d) {
		gradientValue = a;
		accelerationChange = c;
		decelerationChange = d;
	}

	/**
	 * @param Up_gray
	 * @param Down_gray
	 * @throws IOException
	 */
	public static void readEffect(ArrayList<GradientEffect> Up_gray,
			ArrayList<GradientEffect> Down_gray) throws IOException {

		Debug.print("GradientEffect: readEffect: I am in gradient_effect");
		Reader r = new FileReader(GlobalVar.fileGradientEffect);
		GradientEffect grad_eff;
		StreamTokenizer st = new StreamTokenizer(r);
		st.whitespaceChars(0, 3);
		String dir1 = "up";
		String dir2 = "down";
		try {
			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				double acceleration_change, deceleration_change;
				String direction, value;
				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug.print("GradientEffect: readEffect: Error in format of input file : Up_grad_info.dat");
					Debug.print("GradientEffect: readEffect: Error : grad_value expected");
				}
				value = st.sval;
				Debug.print("GradientEffect: readEffect: Gradient value read is "
						+ st.sval);
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug.print("GradientEffect: readEffect: Error in format of input file : gradient_efect.txt.");
					Debug.print("GradientEffect: readEffect: Error : direction expected");
				}

				direction = st.sval;
				Debug.print("GradientEffect: readEffect: DIRECTION IS "
						+ direction);
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("GradientEffect: readEffect: Error in format of input file : Up_grad_info.dat.");
					Debug.print("GradientEffect: readEffect: Error : acceleration_change expected");
				}
				Debug.print("GradientEffect: readEffect: acceleration change read is "
						+ st.nval);
				acceleration_change = st.nval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("GradientEffect: readEffect: Error in format of input file : Up_grad_info.dat.");
					Debug.print("GradientEffect: readEffect: Error : deceleration_change expected");
				}
				Debug.print("GradientEffect: readEffect: value read is "
						+ st.nval);
				Debug.print("GradientEffect: readEffect: deceleration change read is "
						+ st.nval);
				deceleration_change = st.nval;
				// System.out.println(direction +" "+dir1);
				if (direction.equalsIgnoreCase(dir1) == true) {
					grad_eff = new GradientEffect(value, acceleration_change,
							deceleration_change);
					Up_gray.add(grad_eff);
					Debug.print("GradientEffect: readEffect: adding Up");
				}
				if (direction.equalsIgnoreCase(dir2) == true) {
					grad_eff = new GradientEffect(value, acceleration_change,
							deceleration_change);
					Down_gray.add(grad_eff);
					Debug.print("GradientEffect: readEffect: adding down");
				}
			}
		} catch (IOException e) {
			Debug.print("GradientEffect: readEffect: File Not Found");
		}
	}

}

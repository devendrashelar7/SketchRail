import java.util.*;
import util.*;
import java.io.*;

/**
 * Class for Gradient
 */
class Gradient {
	/**
	 * gradientArrayList
	 */
	ArrayList<GradientFormat> gradientArrayList;

	/**
	 * constructor
	 */
	public Gradient() {
		gradientArrayList = new ArrayList<GradientFormat>();
	}

	/**
	 * @return read gradient
	 * @throws IOException
	 */
	@SuppressWarnings( { "static-access" })
	public static ArrayList<GradientFormat> readGradient() throws IOException {

		Debug.print("Gradient: readGradient: I am in read_gradient");

		ArrayList<GradientFormat> gray = new ArrayList<GradientFormat>();

		try {
			Reader reader = new FileReader(GlobalVar.fileGradient);
			StreamTokenizer st = new StreamTokenizer(reader);
			st.slashSlashComments(true);
			st.slashStarComments(true);
			
			st.whitespaceChars(0, 3);

			while (st.nextToken() != st.TT_EOF) {
				double startMileP, endMileP;
				String dir, value;

				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug
							.print("Gradient: readGradient: Error in format of input file : grad_info.dat");
					Debug
							.print("Gradient: readGradient: Error : grad_value expected");
				}
				value = st.sval;
				Debug.print("value read" + st.sval);
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug
							.print("Gradient: readGradient: Error in format of input file : grad_info.dat");
					Debug
							.print("Gradient: readGradient: Error : direction    expected");
				}
				Debug.print("Gradient: readGradient: value read is " + st.sval);
				dir = st.sval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Gradient: readGradient: Error in format of input file : grad_info.dat");
					Debug
							.print("Gradient: readGradient: Error : start mile post expected");
				}
				Debug.print("Gradient: readGradient: value read is " + st.nval);
				startMileP = st.nval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Gradient: readGradient: Error in format of input file : grad_info.dat");
					Debug
							.print("Gradient: readGradient: Error : end mile post expected");
				}
				Debug.print("Gradient: readGradient: value read is " + st.nval);
				endMileP = st.nval;
				GradientFormat gradientFormat = new GradientFormat(value, dir,
						startMileP, endMileP);
				gray.add(gradientFormat);
			}
		} catch (IOException e) {
			Debug
					.print("Gradient: readGradient: *********I am iN gradient File not Found *******");
		}
		for (int i = 0; i < gray.size(); i++) {
			GradientFormat gradientFormat = gray.get(i);
			Debug.print(gradientFormat.direction + "  "
					+ gradientFormat.startMilePost + "  "
					+ gradientFormat.endMilePost);
		}

		return gray;

	}

	/**
	 * @param gradientFormat
	 */
	public void add(GradientFormat gradientFormat) {
		Debug.print("I am in add gradient");
		gradientArrayList.add(gradientFormat);
	}

	/**
	 * @param gradientFormat
	 */
	public void addInvert(GradientFormat gradientFormat) {
		String tem = new String();
		Debug.print("I am in inverse gradient ");
		if ((gradientFormat.direction.equalsIgnoreCase("UP") == true)) {
			tem = "DOWN";
		}
		if ((gradientFormat.direction.equalsIgnoreCase("DOWN") == true)) {
			tem = "UP";
		}
		if ((gradientFormat.direction.equalsIgnoreCase("LEVEL") == true)) {
			tem = "LEVEL";
		}
		gradientFormat.direction = tem;
		gradientArrayList.add(gradientFormat);
	}

	/**
	 * @return size of the list.
	 */
	public int size() {
		return (gradientArrayList.size());
	}

	/**
	 * @param i
	 * @return the ith element in the list
	 */
	public GradientFormat get(int i) {
		GradientFormat gradientFormat;
		gradientFormat = gradientArrayList.get(i);
		return gradientFormat;
	}

	/**
	 * @param a
	 * @param b
	 * @return {@link GradientFormat}
	 */
	public GradientFormat returnGradient(double a, double b) {
		double st, en;
		GradientFormat gradientFormat = new GradientFormat();

		for (int i = 0; i < gradientArrayList.size(); i++) {
			st = gradientArrayList.get(i).startMilePost;
			en = gradientArrayList.get(i).endMilePost;
			if (a >= st && b <= en) {
				gradientFormat = gradientArrayList.get(i);
			}
			if (gradientFormat.direction == null) {
				gradientFormat.direction = "LEVEL";
			}
		}
		return gradientFormat;
	}

}
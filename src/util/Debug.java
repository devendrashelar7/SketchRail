package util;

import java.io.*;

/**
 * 
 */
public class Debug {

	/**
	 * debug
	 */
	public static boolean debug = true;
	/**
	 * level
	 */
	public static int level = 0;
	/**
	 * debugTrain
	 */
	public static int debugTrain = -1;
	/**
	 * currTrainNo
	 */
	public static int currTrainNo = -1;

	/**
	 * fileWriter
	 */

	public static FileWriter fileWriter;
	/**
	 * bufferedWriter
	 */
	public static BufferedWriter bufferedWriter;

	/**
     * 
     */
	public Debug() {
		try {
			Debug.fileWriter = new FileWriter(new File("debugFile.txt"));
			bufferedWriter = new BufferedWriter(fileWriter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param filename
	 * @return setOutput
	 */
	public static boolean setOutput(String filename) {
		try {
			System.setOut(new PrintStream(new FileOutputStream(new File(
					filename))));
		} catch (Exception e) {
			System.out.println("Error during initialisation of op file:"
					.concat(String.valueOf(String.valueOf(filename))));
			boolean flag = false;
			return flag;
		}
		return true;
	}

	/**
	 * @param str
	 */
	public static void print(String str) {
		if (debug /* && (debugTrain == -1 || currTrainNo == debugTrain) */) {
			// System.out.println(str);
			try {
				fileWriter = new FileWriter("debugFile.txt");
				fileWriter.write(str + '\n');
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param prty
	 * @param str
	 */
	public static void print(int prty, String str) {
		if (prty == level) {
			System.out.println(str);
		}
	}

	/**
	 * @param value
	 * @param str
	 */
	public static void debug_assert(boolean value, String str) {
		if (!value) {
			System.out.println("Error : ".concat(String.valueOf(String
					.valueOf(str))));
			System.exit(0);
		}
	}

}

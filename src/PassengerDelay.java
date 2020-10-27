import java.util.*;
import util.*;
import java.io.*;

/**
 * Class PassengerDelay. File to read the format of delay.
 */

public class PassengerDelay extends ArrayList<PassengerDelayFormat> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 */
	public PassengerDelay() {
	}

	/**
	 * Read the delay.
	 * 
	 * @return {@link ArrayList} of the delay.
	 */
	public static ArrayList<PassengerDelayFormat> readDelay() {
		Debug.print("I am in passenger train delay ");

		ArrayList<PassengerDelayFormat> delay = new ArrayList<PassengerDelayFormat>();

		try {
			Reader r = new FileReader(GlobalVar.filePassDelay);
			StreamTokenizer streamTokenizer = new StreamTokenizer(r);
			streamTokenizer.whitespaceChars(0, 3);

			while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {

				if (streamTokenizer.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Error in format of input file : passengerdelay....");
					Debug.print("Error : train priority");
				}

				int trainPriority = (int) streamTokenizer.nval;
				Debug.print("value read" + streamTokenizer.nval);
				streamTokenizer.nextToken();

				if (streamTokenizer.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Error in format of input file : passenferdelay...");
					Debug.print("Error : % delay expected");
				}

				Debug.print("value read is " + streamTokenizer.nval);
				float percentDelay = (float) streamTokenizer.nval;
				streamTokenizer.nextToken();

				if (streamTokenizer.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Error in format of input file : passengerdelay..");
					Debug.print("Error : mean delay  expected");
				}

				Debug.print("value read is " + streamTokenizer.nval);
				double meanDelay = streamTokenizer.nval;
				Debug.print(" delay size is  " + delay.size());

				PassengerDelayFormat pass = new PassengerDelayFormat(
						trainPriority, percentDelay, meanDelay);
				delay.add(pass);
			}
		}

		catch (IOException e) {
			Debug.print("*********I am iN delay File not Found *******");
		}

		for (int i = 0; i < delay.size(); i++) {
			PassengerDelayFormat passengerDelayFormat = delay.get(i);
			Debug.print(passengerDelayFormat.trainPriority + "  "
					+ passengerDelayFormat.percentDelay + "  "
					+ passengerDelayFormat.meanDelay);
		}
		return delay;
	}
}

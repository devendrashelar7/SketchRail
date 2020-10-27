import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

import util.Debug;

/**
 * 
 */
public class SignalFailure {
	/**
	 * arraySignalFailure
	 */
	ArrayList<SignalFailureFormat> arraySignalFailure;

	/**
     * 
     */
	public SignalFailure() {
		arraySignalFailure = new ArrayList<SignalFailureFormat>();
	}

	/**
	 * @return {@link ArrayList} of {@link SignalFailureFormat}
	 */
	public static ArrayList<SignalFailureFormat> readSignalFailure() {

		Debug.print("SignalFailure: readSignalFailure: I am IN signal_failure.read_signal_failure");
		Reader r;
		ArrayList<SignalFailureFormat> sf = new ArrayList<SignalFailureFormat>();
		SignalFailureFormat sff;

		try {
			r = new FileReader(GlobalVar.fileSignalFailure);
			StreamTokenizer st = new StreamTokenizer(r);
			st.whitespaceChars(0, 3);

			while (st.nextToken() != st.TT_EOF) {
				double start, end;
				int blno;

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("SignalFailure: readSignalFailure: Error in format of input file : signal_failure");
					Debug.print("SignalFailure: readSignalFailure: Error : block_no expected");
				}
				blno = (int) st.nval;
				Debug.print("value read" + st.nval);

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("SignalFailure: readSignalFailure: Error in format of input file : signal_failure");
					Debug.print("SignalFailure: readSignalFailure: Error : start_time expected");
				}
				start = st.nval;
				Debug.print("SignalFailure: readSignalFailure: value read"
						+ st.nval);

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("Error in format of input file : signal_failure..");
					Debug.print("Error : end_time expected");
				}
				end = st.nval;
				Debug.print("SignalFailure: readSignalFailure: value read"
						+ st.nval);
				Debug.print("SignalFailure: readSignalFailure: read " + blno
						+ "  " + start + "  " + end);
				sff = new SignalFailureFormat(blno, start, end);
				sf.add(sff);
			}
		} catch (IOException e) {
			Debug.print("SignalFailure: readSignalFailure: signal failure file Not Found");
		}
		Debug.print("PRINTING");
		for (int i = 0; i < sf.size(); i++) {
			Debug.print(sf.get(i).blockNo + "  " + sf.get(i).start + "   "
					+ sf.get(i).end);
		}
		return sf;
	}

	/**
	 * @param sff
	 */
	public void add(SignalFailureFormat sff) {
		Debug.print(" adding signal failure ");
		arraySignalFailure.add(sff);
	}
}
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;
import java.util.StringTokenizer;

import util.Debug;

/**
 * Class file to start the simulator
 */
public class simStart {
	/**
	 * nogui no graphical user interface.
	 */
	static boolean nogui = false;

	/**
	 * debug whether debugging mode is on or off.
	 */
	static boolean debug = false;
	/**
	 * printOccupy if Occupy should be printed or not.
	 */
	static boolean printOccupy = false;

	/**
	 * Main function.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		String filepath = null;
//		Debug debug = new Debug();
		// boolean x;
		processArguments(args);

		// Debug.assert(args.length>0,"Error : keyword expected 1 ");
		Debug.print(args[args.length - 1]);
		Debug.print("*********************** ");
		Debug.print(args[args.length - 1]);

		filepath = args[args.length - 1];
		String testCaseDirectory = args[args.length - 2];
		GlobalVar.testCaseDirectory = testCaseDirectory;

		Debug.print("filepath: " + filepath);

		ReadSection.readFiles(filepath, testCaseDirectory);

		// Devendra: if testCases are to be verified, set the reference
		// outputFileReaderStreamTokenizer
		if (GlobalVar.verifyTestCases) {
			try {
				FileReader fileReader = new FileReader(testCaseDirectory
						+ "output.txt");
				GlobalVar.outputFileReaderStreamTokenizer = new StreamTokenizer(
						fileReader);
			} catch (FileNotFoundException e) {
				System.out.println("simStart: outputFile not found");
				System.out.println("outputFile is: " + testCaseDirectory
						+ "output.txt");
			}
		}

		start();
	}

	/**
	 * Process the arguments.
	 * 
	 * @param args
	 *            arguments
	 */

	public static void processArguments(String args[]) {
		Debug.print("Processing Args");
		for (int i = 0; i < args.length; i++) {
			Debug.print(args[i]);
			nogui = (args[i].equals("-nogui")) ? true : nogui;
			debug = (args[i].equals("-debug")) ? true : debug;
			GlobalVar.verifyTestCases = (args[i]
					.equalsIgnoreCase("-verifyTestCase")) ? true
					: GlobalVar.verifyTestCases;

			if (args[i].equals("-train")) {
				Debug.debugTrain = Integer.parseInt(args[i + 1]);
			}

			if (args[i].equals("-op")) {
				Debug.setOutput(args[i + 1]);
			}

			//this is specific for the 3line case with a bidirectionalline
			if (args[i].equals("-3line")) {
				i++;
				System.out.println("3linemode activated");
				GlobalVar.TRAIN_TYPE_ALLOWED_ON_THIRD_LINE = args[i];
			}
			printOccupy = (args[i].equals("-printOccupy")) ? true : printOccupy;
		}
		Debug.debug = debug;
	}

	/**
	 * Generate integer random number between 1 and num
	 * 
	 * @param num
	 * @return random number
	 */
	public static int randomGenerator(int num) {
		Random generator = new Random();
		return generator.nextInt(num) + 1;
	}

	/**
	 * Start function.
	 */
	public static void start() {

		// FileOutputStream fout;
		Train train, train1, trainDelayed;

		// Station stn;
		ReferenceTableEntry srf, srf1;
		double arrTime, depTime;

		// ArrayList TimeTable = new ArrayList();
		// ArrayList runTime = new ArrayList();
		ArrayList<Station> stationArray = new ArrayList<Station>();
		ArrayList<Train> trainArray = new ArrayList<Train>();
//		ArrayList<Loop> loopArray = new ArrayList<Loop>();// ///////////////mj

		// ArrayList old_arrayTrain = new ArrayList();
		int totalTrains[] = new int[1000];// GlobalVar.Delay.size()]; //array
		// for storing no. of trains for a
		// given priority
		int delayedTrains[] = new int[1000];// GlobalVar.Delay.size()];
		int check[] = new int[1000];// arrayTrain.size()];
		int check1[] = new int[1000];

		/* ---------------- Important parameters ----------------------- */

		// int stationSize;
		int siz, siz1 = 0, siz2 = 0, abc = 0;

		// int SimulationTime = 1500;
		ReadSection.readParameters();

		// final int minInDay = 2400;
		// int minInDay1 = 2400;
		try {

			Debug.print("start: reading Station files");
			stationArray = ReadSection.readStation();
			GlobalVar.stationArrayList = stationArray;

			Debug.print("start: STATION IS READ ");
			Collections.sort(stationArray, new SortStation());

			ReadSection.readSection();// /////////////////mj
			// GlobalVar.loopArrayList = loopArray;// /////////////////////mj
			Debug.print("start: LOOP ARRAY IS READ ");
			// ReadSection.readSection();// //////////////////mj

			// Block bi = new Block();
			// Debug.print("start: SECTION IS READ ");

			ReadSection.linkBlocks(stationArray);
			Debug.print("start: linkBlocks over.");

			ReadSection.convertLinks();
			GlobalVar.stationArrayList = stationArray;

			trainArray = Train.readTrain();
			GlobalVar.trainArrayList = trainArray;

		} catch (Exception e) {
			Debug.print("start: Error reading input " + e.getMessage());
			// return;
		}

		Debug.print("start: GlobalVar.hashblock.size = "
				+ GlobalVar.hashBlock.size());

		for (Enumeration<HashBlockEntry> e = GlobalVar.hashBlock.elements(); e
				.hasMoreElements();) {

			HashBlockEntry hbEntry = e.nextElement();
			Block block = hbEntry.block;
			Debug.print(block.blockNo + "    " + block.startMilePost + "     "
					+ block.endMilePost + "   " + block.nextBlocks.size());
		}

		/************************** Code starts here by Chandra ***************************/
		if (GlobalVar.simulationType.equalsIgnoreCase("SignalFailure")) {
			TinyBlockFormat s = new TinyBlockFormat();

			for (Enumeration<HashBlockEntry> e = GlobalVar.hashBlock.elements(); e
					.hasMoreElements();) {
				HashBlockEntry hbEntry = e.nextElement();
				Block block = hbEntry.block;
				double startMilePost = block.startMilePost;
				double endMilePost = block.endMilePost;

				s = (new TinyBlockFormat(startMilePost, startMilePost + 0.001,
						0, 0, 0.001));
				block.sfsr.add(s);
				s = (new TinyBlockFormat(startMilePost + 0.001, endMilePost, 0,
						0, 0.25));
				block.sfsr.add(s);

				s = (new TinyBlockFormat(startMilePost, startMilePost + 0.001,
						0, 0, 0.0005));
				block.nightsfsr.add(s);
				s = (new TinyBlockFormat(startMilePost + 0.001, endMilePost, 0,
						0, 0.25));
				block.nightsfsr.add(s);
			}

			Debug.print("start: I am in signal failure mode ");
			ArrayList<SignalFailureFormat> SF = new ArrayList<SignalFailureFormat>();
			try {
				SF = SignalFailure.readSignalFailure();
			} catch (Exception e) {
				Debug
						.print("start: ****************      Could not read Signal_Failure file  ********** ");
			}

			Debug.print("start: Signal Failure size is " + SF.size());
			for (int i = 0; i < SF.size(); i++) {
				Debug.print("start: value is   " + (SF.get(i)).blockNo + "   "
						+ SF.get(i).start + "   " + SF.get(i).end);
				for (Enumeration<HashBlockEntry> e = GlobalVar.hashBlock.elements(); e
						.hasMoreElements();) {
					HashBlockEntry hbEntry = (HashBlockEntry) e.nextElement();
					Block block = hbEntry.block;
					if (block.blockNo == SF.get(i).blockNo) {
						block.signalFailure.add(SF.get(i));
					}
				}
			}

			Debug.print("start:  PRINTING THE SIGNAL FAILS ");
			for (Enumeration<HashBlockEntry> e = GlobalVar.hashBlock.elements(); e
					.hasMoreElements();) {
				HashBlockEntry hbEntry = e.nextElement();

				Block block = hbEntry.block;
				Debug.print("start:  block no is " + block.blockNo + "   "
						+ block.signalFailure.arraySignalFailure.size());

				for (int i = 0; i < block.signalFailure.arraySignalFailure
						.size(); i++)
					Debug
							.print("start:  "
									+ block.signalFailure.arraySignalFailure
											.get(i).start
									+ " "
									+ block.signalFailure.arraySignalFailure
											.get(i).end);
			}
		}

		Debug.print("start: Code starts here for passenger train Delay ");

		try {
			GlobalVar.delayArrayList = PassengerDelay.readDelay();
		} catch (Exception e) {
			Debug
					.print("start: Could not read passenger train delay of section ");
		}

		Debug.print("start: Gradient profile array  Size is "
				+ GlobalVar.delayArrayList.size());
		Debug.print("start: ");

		ArrayList<GradientFormat> GR = new ArrayList<GradientFormat>();
		ArrayList<GradientEffect> up_side = new ArrayList<GradientEffect>();
		ArrayList<GradientEffect> down_side = new ArrayList<GradientEffect>();
		// Read Gradient
		try {
			GR = Gradient.readGradient();
		} catch (Exception e) {
			Debug
					.print("start: ****************      Could not read GRadient profile of the section ********** ");
		}

		Debug.print("start: GRadient profile array  Size is " + GR.size());

		/** Read gradient effect of the section ****/
		try {
			GradientEffect.readEffect(up_side, down_side);
		} catch (Exception e) {
			Debug
					.print("start:  ************* Could not read  gradient_effect       ***********");
		}

		// Devendra
		/** Read the values for BlockDirectionInInterval */
		try {
			if (!GlobalVar.pathBlockDirectionInInterval.equalsIgnoreCase("")) {
				BlockDirectionInInterval.readBlockDirectionInIntervalFile();
			}
		} catch (IOException e) {
			Debug
					.print("simStart: BlockDirectionInInterval.txt does not exist");
		}

		Debug.print("start: PRINTING THE UP EFFECTS " + up_side.size());
		for (int i = 0; i < up_side.size(); i++) {
			Debug.print("start: value is   " + up_side.get(i).gradientValue
					+ " " + up_side.get(i).accelerationChange);
		}
		for (int i = 0; i < down_side.size(); i++) {
			Debug.print("start: value is   " + down_side.get(i).gradientValue
					+ "   " + down_side.get(i).accelerationChange);
		}
		GradientEffectUp.gradientEffectUpArray = up_side;
		GradientEffectDown.gradientEffectDownArray = down_side;

		GradientFormat currg = new GradientFormat();
		GradientFormat prevg = new GradientFormat();
		GradientFormat nextg = new GradientFormat();
		int j = 0;
		// String dir1;
		Debug.print("start: J ABOVE IS   " + j);
		Debug.print("start: GRADIENT SIZE IS " + GR.size());
		Block bu = new Block();
//		Block a, b, c, e, d, f;
//		NextBlockArray dt = new NextBlockArray();
//		NextBlockArray pt = new NextBlockArray();
//		NextBlockArray du = new NextBlockArray();
		bu.endMilePost = 0.0;
//		GradientFormat nexg = GR.get(GR.size() - 1);

		while (j < GR.size()) {
			Debug.print("start: In While loop");
			Debug.print("start: J value is " + j);

			currg = GR.get(j);

			Debug.print("start: Current gradient starting mile post is "
					+ currg.startMilePost + "    " + currg.endMilePost);

			if (j != 0) {
				prevg = GR.get(j - 1);
				Debug.print("start: previous gradient starting mile post is "
						+ prevg.startMilePost + "    " + prevg.endMilePost);
			}

			if (j <= GR.size() - 2) {
				nextg = GR.get(j + 1);
				Debug.print("start: NEXT gradient starting mile post is "
						+ nextg.startMilePost + "    " + nextg.endMilePost);
			}

			Debug
					.print("start: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  ");
			ArrayList<Block> yu = new ArrayList<Block>();

			yu = GlobalVar.returnBlockArrayNew(currg.startMilePost,
					currg.endMilePost);

			for (int ty = 0; ty < yu.size(); ty++) {
				Block nut = ((Block) yu.get(ty));
				int blockNo = nut.blockNo;

				double startMilePost = nut.startMilePost;
				double endMilePost = nut.endMilePost;
				int direction = nut.direction;

				Debug.print("start: adding gradient to   " + blockNo + "   "
						+ startMilePost + "*****  " + endMilePost + "  "
						+ direction);

				if (direction == 0 || direction == 2) {
					double maxStartMilePost = Math.max(currg.startMilePost,
							startMilePost);
					double minEndMilePost = Math.min(currg.endMilePost,
							endMilePost);
					GradientFormat gradientFormat = new GradientFormat(
							currg.gradientValue, currg.direction,
							maxStartMilePost, minEndMilePost);
					nut.gradient.add(gradientFormat);

				}

				if (direction == 1) {
					double maxStartMilePost = Math.max(currg.startMilePost,
							startMilePost);
					double minEndMilePost = Math.min(currg.endMilePost,
							endMilePost);
					GradientFormat gradientFormat = new GradientFormat(
							currg.gradientValue, currg.direction,
							maxStartMilePost, minEndMilePost);
					nut.gradient.addInvert(gradientFormat);

				}
			}
			j++;
			Debug
					.print("start: #########################   ONE ITERATION IS OVER ###########################");
		}

		Block biu;
		Debug.print("start: Printing the details of Blocks ");
		for (Enumeration<HashBlockEntry> enumeration = GlobalVar.hashBlock
				.elements(); enumeration.hasMoreElements();) {

			HashBlockEntry hbEntry = enumeration.nextElement();
			biu = hbEntry.block;

			int direction = biu.direction;
			double startMilePost = biu.startMilePost;
			double endMilePost = biu.endMilePost;
			int blockNo = biu.blockNo;

			Debug.print("start: FOR Block no " + blockNo);
			Debug.print("start: Block direction " + direction);
			Debug.print("start: Starting Mile post is = " + startMilePost
					+ "\nEnding Mile post is = " + endMilePost
					+ "\ndirection = " + direction);

			Debug
					.print("start: ####################### Printing Gradient Values ################# ");
			ArrayList<GradientFormat> L = biu.gradient.gradientArrayList;

			if (L.size() == 0) {
				Debug.print("start:  This block does not have any gradients ");
			} else {
				for (int k = 0; k < L.size(); k++) {
					GradientFormat gradientFormat = L.get(k);
					double gradientStartMilePost = gradientFormat.startMilePost;
					double gradientEndMilePost = gradientFormat.endMilePost;
					String gradientDirection = gradientFormat.direction;

					Debug.print("start: gradientStartMilePost = "
							+ gradientStartMilePost);
					Debug.print("start: gradientEndMilePost = "
							+ gradientEndMilePost);
					Debug.print("start: gradientDirection = "
							+ gradientDirection);
				}
			}

		}

		for (Enumeration<HashBlockEntry> ghj = GlobalVar.hashBlock.elements(); ghj
				.hasMoreElements();) {

			HashBlockEntry hbEntry = ghj.nextElement();
			biu = hbEntry.block;
			ArrayList<TinyBlockFormat> Lo = new ArrayList<TinyBlockFormat>();
			Lo = biu.tinyBlock.creatiny(biu);
			biu.tinyBlock.tinyBlockArray = Lo;
		}

		Debug
				.print("start: $$$$$$$$$$$$$$$      Printing TINY BLKS $$$$$$$$$$$$$$$$$$$$ ");
		TinyBlockFormat ti = new TinyBlockFormat();
		Debug
				.print("start: SMP           EMP        ACHANGE      DCHANGE         MAXSP");
		for (Enumeration<HashBlockEntry> ghj = GlobalVar.hashBlock.elements(); ghj
				.hasMoreElements();) {
			HashBlockEntry hbEntry = (HashBlockEntry) ghj.nextElement();
			biu = hbEntry.block;

			Debug.print("start:  %%%%%%%%%%%%%%%%%%%%%%%%%%%%     For block   "
					+ biu.blockNo);

			for (int m = 0; m < biu.tinyBlock.tinyBlockArray.size(); m++) {
				ti = biu.tinyBlock.tinyBlockArray.get(m);

				Debug.print(ti.startMilePost + "    " + ti.endMilePost
						+ "       " + ti.accelerationChange + "      "
						+ ti.decelerationChange + "      " + ti.maxSpeed);
			}
		}

		Debug.print("start: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

		/* code ends here by Chandra */
		siz = trainArray.size();
		Debug.print("start: arrayTrain size is   " + siz);

		for (int m = 0; m < siz; m++) {

			Debug.print("start: ");
			train = trainArray.get(m);
			Debug.print("start: train.trainNo = " + train.trainNo);
			String rdays = train.operatingDays;
			if (train.priority < 5) {
				if (rdays.equalsIgnoreCase("all")) {
					for (int p = 1; p < GlobalVar.simulationTime; p++) {
						train1 = new Train();
						for (int k = 1; k < train.refTables.size(); k++) {

							// train paramters are copied
							train1.priority = train.priority;
							train1.length = train.length;
							train1.accParam = train.accParam;
							train1.deceParam = train.deceParam;
							train1.trainNo = train.trainNo;
							train1.maximumPossibleSpeed = train.maximumPossibleSpeed;
							train1.bookedSpeed = train.bookedSpeed;
							train1.isScheduled = train.isScheduled;
							train1.direction = train.direction;
							train1.startLoopNo = train.startLoopNo;
							train1.velocity = train.velocity;
							train1.startTime = train.startTime + (p * 1440);
							train1.departTime = train.departTime + (p * 1440);
							train1.operatingDays = train.operatingDays;

							srf = train.refTables.get(k);
							arrTime = srf.refArrTime + (p * 1440);
							depTime = srf.refDepTime + (p * 1440);
							srf1 = new ReferenceTableEntry(srf.refLoopNo,
									arrTime, depTime, srf.stationName,
									srf.runTimeToNextStn);
							train1.refTables.add(srf1);
						}
						trainArray.add(train1);
						// System.out.println("added"+ train1.trainNo);
					}
				} else {
					Debug.print("start:  case of non-daily train :");
					StringTokenizer st = new StringTokenizer(rdays, ",");
					int counter = 0;
					while (st.hasMoreTokens()) {
						counter++;
						st.nextToken();
					}
					Debug.print("start: no of days is " + counter);

					int days[] = new int[counter];
					StringTokenizer s = new StringTokenizer(rdays, ",");
					counter = 0;
					while (s.hasMoreTokens()) {
						String str = s.nextToken();
						days[counter] = Integer.parseInt(str);
						counter++;
					}
					// System.out.println(" case of non-daily  train "+train.trainNo+"   "+train.operatingdays);
					for (int p = 0; p < GlobalVar.simulationTime; p++) {

						for (int i = 0; i < counter; i++) {

							// System.out.println(" operating day "+days[i]+" counter is "+i+"  "+((
							// p-1)%7)+" amd p is "+p);
							if ((p) % 7 == days[i])// ||(p==days[i]))
							{
								System.out.println("start: operating day "
										+ days[i]);
								Debug.print("start:  Test passed  " + days[i]);

								train1 = new Train();
								for (int k = 0; k < train.refTables.size(); k++) {
									train1.priority = train.priority;
									train1.length = train.length;
									train1.accParam = train.accParam;
									train1.deceParam = train.deceParam;
									train1.trainNo = train.trainNo;
									train1.maximumPossibleSpeed = train.maximumPossibleSpeed;
									train1.bookedSpeed = train.bookedSpeed;
									train1.direction = train.direction;
									train1.startLoopNo = train.startLoopNo;
									train1.velocity = train.velocity;
									train1.isScheduled = train.isScheduled;
									train1.operatingDays = train.operatingDays;

									// System.out.println("train1.startTime"+train1.startTime+"train no"
									// +train1.trainNo);
									train1.startTime = train.startTime
											+ ((p) * 1440);

									// System.out.println("train1.startTime"+train1.startTime);
									train1.departTime = train.departTime
											+ (p * 1440);
									srf = train.refTables.get(k);
									arrTime = srf.refArrTime + ((p) * 1440);

									// System.out.println("srf.refArrTime"
									// +srf.refArrTime);
									depTime = srf.refDepTime + ((p) * 1440);
									srf1 = new ReferenceTableEntry(
											srf.refLoopNo, arrTime, depTime,
											srf.stationName,
											srf.runTimeToNextStn);
									train1.refTables.add(srf1);
								}
								trainArray.add(train1);
								// System.out.println("added"+train1.trainNo);
							}
						}
					}
				}
			}
		}

		/*
		 * for(int i=0;i<arrayTrain.size();i++) { train =
		 * (Train)arrayTrain.get(i) ; System.out.println(" train is "+i +"  "+
		 * train.trainNo+" :: "+train.operatingdays+" __ "+train.startTime); }
		 */
		siz1 = siz;
		siz2 = siz;
		for (int ji = 0; ji < siz1; ji++) {// mj

			for (int m = 0; m < siz2; m++) {
				Debug.print("start:  SIZE IS " + siz2);
				train = trainArray.get(m);
				Debug.print("start:  ~~~ " + train.trainNo + " !! " + m);

				if (train.priority <= 5) {
					if (train.operatingDays.equalsIgnoreCase("all")) {
						Debug.print("start: adk na");
					} else {
						Debug.print("start: removing train " + train.trainNo);
						trainArray.remove(train);
						abc = siz2;
						siz2 = -1;

						m -= 1;
					}
				}
			}
			siz2 = abc - 1;
		}
		/*
		 * System.out.println("AFTER THE DELETION ^^^^^^^^^^^^^^  "); for(int
		 * i=0;i<arrayTrain.size();i++) { train = (Train)arrayTrain.get(i) ;
		 * System.out.println(" train is "+i +"  "+
		 * train.trainNo+"  "+train.operatingdays+"   "+train.startTime); }
		 */

		Debug.print("start:  &&&&&&&&&&&&&&&&&&&&&&&&&& ");
		Train t = new Train();
		for (int i = 0; i < trainArray.size(); i++) {
			t = trainArray.get(i);
			Debug.print("start: train no is " + t.trainNo
					+ "  arrival time is " + t.startTime);
			for (int kp = 0; kp < t.refTables.size(); kp++) {
				srf = t.refTables.get(kp);
				Debug.print("start:  arrival time " + srf.refArrTime
						+ " dept time " + srf.refDepTime + " llopp "
						+ srf.refLoopNo);
			}
		}

		Debug.print("start: ");
		if (trainArray.size() == 0) {
			System.out
					.println("start: THERE ARE NO TRAINS WITHIN THE SIMULATED TIME ");
			System.exit(0);
		}
		Collections.sort(trainArray, new SortTrain());
		for (int i = 0; i < trainArray.size(); i++) {
			train = trainArray.get(i);
			Debug.print(" train is " + i + "  " + train.trainNo + "  "
					+ train.operatingDays + "   " + train.departTime);
		}

		// delay module start
		// find total no. of train for a given priority
		try {
			OutputStream f1 = new FileOutputStream("Delayfile.txt");
			PrintStream aPrintStream = new PrintStream(f1);
			aPrintStream.println("Delayed Train NO." + "         " + "priority"
					+ "        " + "Delay(min)" + "                 "
					+ "LoopNos.");
			trainDelayed = null;
			if (GlobalVar.delay == 1) {
				for (int i = 0; i < GlobalVar.delayArrayList.size(); i++) {
					for (int p = 0; p < trainArray.size(); p++)
						if (GlobalVar.delayArrayList.get(i).trainPriority == trainArray
								.get(p).priority)
							totalTrains[i]++;
					System.out.println("total trains of type "
							+ GlobalVar.delayArrayList.get(i).trainPriority
							+ "are " + totalTrains[i]);

				}
				// find total no. of train to be delayed for a given priority

				for (int i = 0; i < GlobalVar.delayArrayList.size(); i++) {
					if (GlobalVar.delayArrayList.get(i).meanDelay > 0) {
						float s = GlobalVar.delayArrayList.get(i).percentDelay;
						delayedTrains[i] = (int) s * totalTrains[i] / 100;
					}
				}
				// select trains randomly

				for (int i = 0; i < GlobalVar.delayArrayList.size(); i++) {
					outer: for (int jj = 0; jj < delayedTrains[i]; jj++) {
						int randomTrain = randomGenerator(totalTrains[i]);
						check[jj] = randomTrain;
						for (int k = 0; k <= jj - 1; k++)
							// check to avoid repeatation of same train
							if (randomTrain == check[k]) {
								jj = jj - 1;
								continue outer;
							}

						for (int k = 0; k < trainArray.size(); k++) {

							if (GlobalVar.delayArrayList.get(i).trainPriority == trainArray
									.get(k).priority) {
								trainDelayed = trainArray
										.get(k + check[jj] - 1);
								System.out.println("the train seclected is"
										+ trainDelayed.trainNo);
								aPrintStream.print(trainDelayed.trainNo
										+ "                      ");
								aPrintStream.print(trainDelayed.priority
										+ "                     ");
								break;
							}
						}
						if (trainDelayed == null) {
							throw new NullPointerException(
									"simStart: trainDelayed is null");
						}
						ArrayList<ReferenceTableEntry> refTables = trainDelayed.refTables;
						int siz4 = refTables.size();
						System.out.println("total no of staion in section"
								+ siz4);

						int randomNoStn = randomGenerator(siz4);
						System.out.println(" no of staion to be delayed"
								+ randomNoStn);
						double delayTime = GlobalVar.delayArrayList.get(i).meanDelay
								/ randomNoStn;
						System.out.println("delay time  " + delayTime);

						aPrintStream.print((float) delayTime
								+ "                   ");
						outer1: for (int l = 0; l < randomNoStn; l++) {
							int randomStn = randomGenerator(siz4);

							check1[l] = randomStn;
							for (int k = 0; k <= l - 1; k++) { // check to avoid
								// repeatation of
								// same station
								if (randomStn == check1[k]) {
									l = l - 1;
									continue outer1;
								}
							}
							System.out.println("delayed at Stn   " + randomStn);

							System.out.println("ref dep time before delay  "
									+ ((ReferenceTableEntry) refTables
											.get(randomStn - 1)).refDepTime);
							System.out.println(" delay at loop nn  "
									+ ((ReferenceTableEntry) refTables
											.get(randomStn - 1)).refLoopNo);
							aPrintStream.print(((ReferenceTableEntry) refTables
									.get(randomStn - 1)).refLoopNo
									+ ",");
							((ReferenceTableEntry) refTables.get(randomStn - 1)).refDepTime += delayTime;
							System.out.println("ref dep time is now  "
									+ ((ReferenceTableEntry) refTables
											.get(randomStn - 1)).refDepTime);
							for (int m = randomStn; m < refTables.size(); m++) {
								((ReferenceTableEntry) refTables.get(m)).refArrTime += delayTime;
								((ReferenceTableEntry) refTables.get(m)).refDepTime += delayTime;
							}
						}

						aPrintStream.println(" ");
					}
				}

			}

		} catch (Exception e1) {
			Debug.print("start: Error in handling o/p file ");
			return;
		}

		FreightSim fSim = new FreightSim(trainArray, stationArray, siz);
		if (nogui == true) {
			fSim.simulate();
			Debug.print("start: case 1");
			Debug.print("start:  ~~~~~~~~~~~~~~~~~ ");
			Debug.print("start: simulation is over ");
			Debug.print("start:  ~~~~~~~~~~~~~~~~~ ");
			if (printOccupy == true) {
				Debug.print("start: case 2");
				Debug.print("start:  ~~~~~~~~~~~~~~~~~ ");
				Debug.print("start: simulation is over ");
				Debug.print("start:  ~~~~~~~~~~~~~~~~~ ");
				try {
					// outputLoopOccupancy();
					double upWeightedTraffic = getWeightedTraffic(0);
					double downWeightedTraffic = getWeightedTraffic(1);

					//ut stands for uptraffic; dt stands for downTraffic
//					System.out.print("upTraffic123 " + upWeightedTraffic
//							+ " downTraffic " + downWeightedTraffic + " ");
					System.out.printf("ut %6.3f" + " dt %6.3f ",
							upWeightedTraffic, downWeightedTraffic);
					outputWeightedTrainTravel();
					outputTotalTrainTravel();
					
				} catch (IOException e1) {
					System.out
							.println("simStart: start: IOException has occured in outputLoopOccupancy");
				}

			}

			if (GlobalVar.verifyTestCases && GlobalVar.testCaseVerified) {
				System.out.println("simStart: TestCase "
						+ GlobalVar.testCaseDirectory + " is verified");
			}

			Debug.print("start: case 3");
			Debug.print("start:  ~~~~~~~~~~~~~~~~~ ");
			Debug.print("start: simulation is over ");
			Debug.print("start:  ~~~~~~~~~~~~~~~~~ ");
			Debug.print("start: In Exit");
			System.exit(0);
		} else {
			GraphFrame gf = new GraphFrame(fSim);
			GlobalVar.graphFrame = gf;
		}

	}

	public static double getWeightedTraffic(int direction) {
		double count = 0;
		int sumPriorities = 0;
		ArrayList<Train> trainList = GlobalVar.trainArrayList;
		for (int i = 0; i < trainList.size(); i++) {
			Train train = trainList.get(i);
			if (train.direction == direction) {
				count++;
				int priority = 11 - train.priority;
				sumPriorities += priority;
			}
		}
		System.out.println("count " + count);
		return ((double) sumPriorities) / 11;
	}

	/**
	 * Output the loop occupancy.
	 * 
	 * @throws IOException
	 */

	public static void outputLoopOccupancy() throws IOException {
		Debug.print("\n Block Occupancy \n Block No : total time occupied");
		for (Enumeration<HashBlockEntry> e = GlobalVar.hashBlock.elements(); e
				.hasMoreElements();) {
			HashBlockEntry hbEntry = e.nextElement();
			Block currBlock = hbEntry.block;

			if (GlobalVar.verifyTestCases) {
				boolean verifyBlock = verifyLoopOccupancy(currBlock);
				if (verifyBlock == false) {
					System.out
							.println("TestCase has failed in verifyLoopOccupancy");
					System.out.println(currBlock.getClass().toString() + " "
							+ currBlock.blockNo + " "
							+ currBlock.occupy.totalInterval());
					System.exit(0);
				}
			} else {
				System.out.println(currBlock.getClass().toString() + " "
						+ currBlock.blockNo + " "
						+ currBlock.occupy.totalInterval());
			}

		}
	}

	/**
	 * outputTotalTrainTravel.
	 * 
	 * @throws IOException
	 */
	public static void outputTotalTrainTravel() throws IOException {
		Debug
				.print("\n Train Traversal Time \n Train No  -  total time  -  traversal time");
		double totalTime = 0;
		for (int i = 0; i < GlobalVar.trainArrayList.size(); i++) {
			Train trn = GlobalVar.trainArrayList.get(i);
			totalTime += trn.totalTime();
			Debug.print(trn.trainNo + "  -  " + trn.totalTime() + "  -  "
					+ trn.travelTime());
		}
		// Debug.print("Average Travelling time " +
		// totalTime/GlobalVar.arrayTrain.size());
		double averageTime = totalTime / GlobalVar.trainArrayList.size();

		if (GlobalVar.verifyTestCases) {
			boolean verifyTime = verifyTotalTrainTravel(
					GlobalVar.outputFileReaderStreamTokenizer, averageTime);
			if (verifyTime == false) {
				System.out
						.println("simStart: TestCase has failed in verifyTotalTrainTravel");
				System.out.println("AverageTravellingTime " + averageTime);
				System.out.println("TotalTravellingTime " + totalTime);
			}
		} else {
			System.out.println("AverageTravellingTime " + averageTime);
			System.out.println("TotalTravellingTime " + totalTime);
		}

	}

	public static void outputWeightedTrainTravel() throws IOException {

		double totalTime = 0;
		for (int i = 0; i < GlobalVar.trainArrayList.size(); i++) {
			Train trn = GlobalVar.trainArrayList.get(i);
			int priority = 11 - trn.priority;
			totalTime += trn.totalTime() * priority;

		}

		double averageTime = totalTime / GlobalVar.trainArrayList.size() / 11;

		System.out.printf("WeightedAverageTravellingTime %6.3f\n",averageTime);
		System.out.printf("WeightedTotalTravellingTime %6.3f\n", totalTime);

	}

	/**
	 * Check if the currentBlock is of the same class and has the same occupancy
	 * with some approximation to the previously obtained values while testing
	 * 
	 * @param currentBlock
	 * @return true if the currentBlock has almost the same values as previously
	 *         tested
	 * @throws IOException
	 */
	public static boolean verifyLoopOccupancy(Block currentBlock)
			throws IOException {

		StreamTokenizer outputFileReaderStreamTokenizer = GlobalVar.outputFileReaderStreamTokenizer;

		// reads the string "class"
		outputFileReaderStreamTokenizer.nextToken();
		String classString = outputFileReaderStreamTokenizer.sval;

		// reads the class of currentBlock
		outputFileReaderStreamTokenizer.nextToken();
		String blockClass = outputFileReaderStreamTokenizer.sval;

		// read block number
		outputFileReaderStreamTokenizer.nextToken();
		int blockNumber = (int) outputFileReaderStreamTokenizer.nval;

		// read the block occupancy time by calling totalInterval function
		outputFileReaderStreamTokenizer.nextToken();
		double totalInterval = outputFileReaderStreamTokenizer.nval;

		String getClassString = classString + " " + blockClass;

		String currentBlockClass = currentBlock.getClass().toString();
		double currentBlockTotalInterval = currentBlock.occupy.totalInterval();
		int currentBlockNumber = currentBlock.blockNo;

		/*
		 * System.out.println("verifyLoopOccupancy: " + getClassString +
		 * " blockClass " + currentBlockClass);
		 * System.out.println("verifyLoopOccupancy: " + totalInterval +
		 * " actual " + currentBlockTotalInterval);
		 */

		if (getClassString.equalsIgnoreCase(currentBlockClass)
				&& blockNumber == currentBlockNumber
				&& isInTolerance(currentBlockTotalInterval, totalInterval)) {
			return true;
		}

		GlobalVar.testCaseVerified = false;
		return false;
	}

	// Devendra
	/**
	 * Check if the averageTravellingTime is approximate to the previously
	 * obtained value of averageTravellingTime
	 * 
	 * @param outputFileReaderStreamTokenizer
	 * @param averageTime
	 * @return true if the currentBlock has almost the same values as previously
	 *         tested
	 * @throws IOException
	 */
	public static boolean verifyTotalTrainTravel(
			StreamTokenizer outputFileReaderStreamTokenizer, double averageTime)
			throws IOException {

		// reads the string "AverageTravellingTime"
		outputFileReaderStreamTokenizer.nextToken();
		String averageTravellingTimeString = outputFileReaderStreamTokenizer.sval;

		// read the averageTravellingTime
		outputFileReaderStreamTokenizer.nextToken();
		double referenceAverageTravellingTime = outputFileReaderStreamTokenizer.nval;

		if (averageTravellingTimeString
				.equalsIgnoreCase("AverageTravellingTime")
				&& isInTolerance(averageTime, referenceAverageTravellingTime)) {
			return true;
		}

		GlobalVar.testCaseVerified = false;
		return false;
	}

	/**
	 * Check if the value is approximate to the referenceValue with some error
	 * margin allowed.
	 * 
	 * @param value
	 * @param referenceValue
	 * @return true if the value is approximate to the referenceValue else false
	 */
	public static boolean isInTolerance(double value, double referenceValue) {
		if (Math.abs(value - referenceValue) < GlobalVar.toleranceValue) {
			return true;
		}

		return false;
	}

}

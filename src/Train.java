import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Comparator;

import util.Debug;

/**
 * Train class.
 */
public class Train implements Comparator<Train> {
	/**
	 * priority of train.
	 */
	public int priority;
	/**
	 * maximumPossibleSpeed
	 */
	public double maximumPossibleSpeed;
	/**
	 * BS
	 */
	public double bookedSpeed;
	/**
	 * length of train
	 */
	public double length;
	/**
	 * acceleration
	 */
	public double accParam;
	/**
	 * deceleration
	 */
	public double deceParam;
	/**
	 * direction
	 */
	public int direction;
	/**
	 * trainNo
	 */
	public int trainNo;
	/**
	 * velocity
	 */
	public double velocity;
	/**
	 * startTime
	 */
	public double startTime;
	/**
	 * departTime
	 */
	public double departTime;// mj
	/**
	 * startLoopNo
	 */
	public int startLoopNo;
	/**
	 * endLoopNo
	 */
	public int endLoopNo;// ///mj

	/**
	 * endStation
	 */
	public String endStation;
	/**
	 * drawColour
	 */
	public Color drawColour;

	/**
	 * timeTables
	 */

	public ArrayList<TimetableEntry> timeTables = new ArrayList<TimetableEntry>();
	/**
	 * schedule
	 */

	public ArrayList schedule = new ArrayList();
	/**
	 * refTables
	 */
	public ArrayList<ReferenceTableEntry> refTables = new ArrayList<ReferenceTableEntry>();// KJD
	/**
	 * operatingDays
	 */
	public String operatingDays;
	/**
	 * signalFailedBlocks
	 */
	public int signalFailedBlocks[] = new int[GlobalVar.hashBlock.size()];
	/**
	 * signalFailCounter
	 */
	public int signalFailCounter = 0;
	/**
	 * isScheduled
	 */
	public boolean isScheduled = false;

	/**
	 * @param dir
	 * @param prior
	 * @param len
	 */
	public Train(int dir, int prior, double len) {
		direction = dir;
		length = len;
		priority = prior;
		endStation = null;
	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param prior
	 */
	public Train(int a, double b, double c, double d, double e, int prior) {
		direction = a;
		startTime = b;
		length = c;
		accParam = d;
		deceParam = e;
		priority = prior;
		endStation = null;
	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 */
	public Train(int a, double b, double c, double d, double e) {

		priority = a;
		startTime = b;
		length = c;
		accParam = d;
		deceParam = e;
		endStation = null;
	}

	/**
	 * @param f
	 * @param a
	 * @param c
	 * @param d
	 * @param e
	 */
	public Train(int f, int a, double c, double d, double e) {

		priority = a;
		length = c;
		accParam = d;
		deceParam = e;
		direction = f;
		endStation = null;
	}

	/**
     * 
     */
	public Train() {
		endStation = null;
	}

	/**
	 * @return totalTime
	 */
	public double totalTime() {

		if (GlobalVar.verifyTestCases) {
			boolean verifyTimeTableSize = verifyTimeTableSize();
			if (verifyTimeTableSize == false) {
				System.out
						.println("Testcase has failed in verifyTimeTableSize");
				// System.out.println("TrainTotalTimeTimeTableSize "
				// + timeTables.size());
			}

		} else {
			// System.out.println("TrainTotalTimeTimeTableSize "
			// + timeTables.size());
		}

		double trainArrivalTime = this.startTime;
		// double time = ((TimetableEntry) timeTables.get(timeTables.size() -
		// 1)).departureTime
		// - ((TimetableEntry) timeTables.get(0)).arrivalTime;
		double time = (timeTables.get(timeTables.size() - 1)).departureTime
				- trainArrivalTime;

		return time;
	}

	/**
	 * @return travelTime
	 */
	public double travelTime() {
		double time = (timeTables.get(timeTables.size() - 1)).arrivalTime
				- (timeTables.get(0)).departureTime;
		return time;
	}

	/**
     * 
     */
	public void printTimeTable() {
		System.out.println("Printing timetables for train " + trainNo);
		for (int i = 0; i < timeTables.size(); i++) {
			TimetableEntry ttEntry = timeTables.get(i);
			ttEntry.print();
		}
	}

	public VelocityProfile getVelocityProfileFromMilepost(double milePost) {
		for (int i = 0; i < this.timeTables.size(); i++) {
			TimetableEntry ttEntry = this.timeTables.get(i);

			if (ttEntry.startMilePost <= milePost
					&& milePost <= ttEntry.endMilePost) {

				VelocityProfileArray velocityProfileArray = (VelocityProfileArray) ttEntry.velocityProfileArray;

				for (int j = 0; j < velocityProfileArray.size(); j++) {
					VelocityProfile velocityProfile = velocityProfileArray
							.get(j);

					if (velocityProfile.startLength <= milePost
							&& milePost <= velocityProfile.endLength) {
						return velocityProfile;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @return list of trains
	 * @throws IOException
	 * @throws SimException
	 */
	@SuppressWarnings( { "unused" })
	public static ArrayList<Train> readTrain() throws IOException, SimException {

		Station currStn;
		Train currTrain;
		TimetableEntry currttEntry;
		ReferenceTableEntry lastRefEntry;
		TimetableEntry lastEntry;

		double runTimeLastStn = 0;
		int lastRefLoopNo = 0;
		double lastRefArrTime = 0;
		double lastRefDepTime = 0;
		int refLoopNo = 0;
		double refArrTime = 0;
		double refDepTime = 0;
		String stationName = "NULL";
		String lastStationName = "NULL";
		String days = "NULL";
		ArrayList<Station> arrayStn = GlobalVar.stationArrayList;
		ArrayList<Train> arrayTrain = new ArrayList<Train>();
		int trainNo = 0;

		try {
			Reader reader;

			// Devendra
			if (simStart.nogui) {
				reader = new FileReader(GlobalVar.pathScheduled);

			} else {
				reader = new FileReader(GlobalVar.fileScheduled);
			}

			// Reader r = new FileReader(GlobalVar.fileScheduled);
			StreamTokenizer st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(false);
			st.slashStarComments(true);

			Debug.print("Reading input file : ScheduledTrain.dat......");
			double len, accParam, deceParam;
			int priority, dir;
			// System.out.println(StreamTokenizer.TT_EOF + " "
			// + StreamTokenizer.TT_EOL);
			while (st.nextToken() != StreamTokenizer.TT_EOF) {

				// Train number
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : scheduledTrain.dat");
					Debug
							.print("Train: readTrain: Error : train number expected");
					throw new SimException();
				}
//				System.out.println("Train: readTrain: train no is " + st.nval);
				Debug.print("Train: readTrain: value read is " + st.nval);
				trainNo = (int) st.nval;

				Debug.print("Train: readTrain: Train no is  " + trainNo);

				// Train direction
				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug
							.print("Train: readTrain: Error in format of input file : scheduledTrain.dat......");
					Debug
							.print("Train: readTrain: Error : train direction expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.sval);
				if (st.sval.equalsIgnoreCase("up")) {
					dir = 0;
				} else {
					// Debug.assert(st.sval.equalsIgnoreCase("Down"),"Input either up ,down ");
					dir = 1;
				}

				Debug.print("Train: readTrain: direction is  " + dir);

				// Train length
				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : scheduledTrain.dat......");
					Debug.print("Train: readTrain: Error : length expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.nval);
				len = st.nval;
				Debug.print("Train: readTrain: length is  " + len);

				// Train acceleration parameter
				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : scheduledTrain.dat......");
					Debug
							.print("Train: readTrain: Error : accelaration parameter expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.nval);
				accParam = st.nval;
				Debug.print("Train: readTrain: accel is  " + accParam);

				// Train deceleration
				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : scheduledTrain.dat");
					Debug
							.print("Train: readTrain: Error : deceleration parameter expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.nval);
				deceParam = st.nval;
				Debug.print("Train: readTrain: decel is  " + deceParam);

				// Train priority
				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : scheduledTrain.dat......");
					Debug.print("Train: readTrain: Error : priority expected");
					throw new SimException();
				}
				Debug.print("Train: readTrain: value read is " + st.nval);
				priority = (int) st.nval;
				Debug.print("Train: readTrain: priority is  " + priority);

				currTrain = new PassengerTrain(dir, priority, len);

				currTrain.trainNo = trainNo;

				// Train velocity
				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : scheduledTrain.dat");
					Debug.print("Train: readTrain: Error : velocity expected");
					throw new SimException();
				}
				Debug.print("Train: readTrain: velocity   is " + st.nval);
				Debug.print("Train: readTrain: value read is " + st.nval);

				// Train operating days
				st.nextToken();
				String opera = st.sval;
				Debug.print("Train: readTrain: operating days " + opera);
				currTrain.operatingDays = opera;

				// ENDS HERE
				currTrain.maximumPossibleSpeed = st.nval / 60.0;
				currTrain.isScheduled = true; // KJD
				currTrain.accParam = accParam;
				currTrain.deceParam = deceParam;
				boolean isStartStn = true;// kjd
				arrayTrain.add(currTrain);

				// Now read loops
				int loopNo;
				Loop currLoop;
				double arrTime, depTime;

				int i;
				if (currTrain.direction == 0)
					i = 0;
				else
					i = arrayStn.size() - 1;

				while (i < arrayStn.size() && i >= 0) {

					// loops at which the train can halt
					st.nextToken();
//					System.out.println("Train: readTrain: loop number type "
//							+ st.sval);
//					System.out.println("Train: readTrain: loop number is "
//							+ st.nval);
					Debug.print("Train: readTrain: value read is " + st.sval);
					Debug.print("Train: readTrain: value read is " + st.nval);

					if (st.sval != null) {
						Debug.print("Train: readTrain: st.sval is not null");
						break;
					}

					refLoopNo = ((int) st.nval);

					if (st.ttype == StreamTokenizer.TT_NUMBER) {
						HashBlockEntry hbLinkEntry = (GlobalVar.hashBlock
								.get(new Integer(refLoopNo)));

						// Debug.assert(hbLinkEntry!=null,"Error: block not present ");
						// currLoop = (Loop) hbLinkEntry.block;
						// stationName = currLoop.station.stationName;
						// stationName = arrayStn.get(i).stationName;
						stationName = GlobalVar.getStationName(refLoopNo);

						if (isStartStn == true) {
							currTrain.startLoopNo = refLoopNo;
						}

						st.nextToken();
						Debug.print("Train: readTrain: value read is "
								+ st.nval);
						refArrTime = st.nval;

						st.nextToken();
						Debug.print("Train: readTrain: value read is "
								+ st.nval);
						refDepTime = st.nval;

						if (isStartStn == true) {

							Debug
									.print("Train: readTrain: refDepartureTime is "
											+ refDepTime);
							int top, tod;
							tod = (int) (refDepTime / 100);
							top = (int) (refArrTime / 100);

							currTrain.departTime = (tod) * 60
									+ (refDepTime % 100);
							currTrain.startTime = (top) * 60
									+ (refArrTime % 100);

							Debug
									.print("Train: readTrain: startTime of current train = "
											+ currTrain.startTime);
							currTrain.startLoopNo = refLoopNo;
						}

						if (!isStartStn) {
							runTimeLastStn = Math.abs(refArrTime
									- lastRefDepTime);
							lastRefEntry = new ReferenceTableEntry(
									lastRefLoopNo, lastRefArrTime,
									lastRefDepTime, lastStationName,
									runTimeLastStn);
							currTrain.refTables.add(lastRefEntry);
						}

						lastRefLoopNo = refLoopNo;
						lastRefArrTime = refArrTime;
						lastRefDepTime = refDepTime;
						lastStationName = stationName;
						isStartStn = false;

					} else {
						st.nextToken();
						st.nextToken();
					}
					if (currTrain.direction == 0)
						i++;
					else
						i--;

				}

				lastRefEntry = new ReferenceTableEntry(lastRefLoopNo,
						lastRefArrTime, lastRefDepTime, lastStationName, 0.0);
				currTrain.endStation = lastStationName;

				currTrain.refTables.add(lastRefEntry);
				ChangeTimeTable.changeRefToMin(currTrain);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// reading unscheduled trains
		try {
			Reader reader;

			// Devendra
			if (simStart.nogui) {
				reader = new FileReader(GlobalVar.pathUnscheduled);
			} else {
				reader = new FileReader(GlobalVar.fileUnscheduled);

			}

			// Reader r = new FileReader(GlobalVar.fileUnscheduled);
			StreamTokenizer st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(false);
			st.slashStarComments(true);

			Debug.print("Reading input file : UnScheduled.dat......");

			while (st.nextToken() != StreamTokenizer.TT_EOF) {

				double len, accParam, deceParam, maximumPossibleSpeed;
				int priority, dir;
				double startTime;

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug
							.print("Train: readTrain: Error : train number expected");
					throw new SimException();
				}
				Debug.print("Train: readTrain: value read is " + st.nval);
				trainNo = (int) st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug
							.print("Train: readTrain: Error in format of input file : unscheduledTrain.dat......");
					Debug
							.print("Train: readTrain: Error : train direction expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.sval);

				if (st.sval.equalsIgnoreCase("Down")) {
					dir = 1;
				} else {
					dir = 0;
				}

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug
							.print("Train: readTrain: Error : start time expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.nval);
				startTime = (int) st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug.print("Train: readTrain: Error : length expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.nval);
				len = st.nval;
				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug
							.print("Train: readTrain: Error : accelaration parameter expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.nval);
				accParam = st.nval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug
							.print("Train: readTrain: Error : deccelaration parameter expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.nval);
				deceParam = st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug.print("Train: readTrain: Error : priority expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.nval);
				priority = (int) st.nval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : scheduledTrain.dat......");
					Debug
							.print("Train: readTrain: Error : Max permisible speed expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.nval);
				maximumPossibleSpeed = st.nval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug.print("Train: readTrain: Error : loop no expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.nval);
				int stLoopNo = ((int) st.nval);

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("Train: readTrain: Error in format of input file : scheduledTrain.dat......");
					Debug
							.print("Train: readTrain: Error : end loop no expected");
					throw new SimException();
				}

				Debug.print("Train: readTrain: value read is " + st.nval);
				int enLoopNo = ((int) st.nval);
				currTrain = new Train(dir, startTime, len, accParam, deceParam,
						priority);
				currTrain.trainNo = trainNo;
				currTrain.startLoopNo = stLoopNo;
				currTrain.endLoopNo = enLoopNo;
				trainNo++;

				currTrain.endStation = GlobalVar
						.getStationName(currTrain.endLoopNo);
				if (currTrain.endStation == null) {
					currTrain.endStation = "";
				}

				// the speed given is in km/hr we conver it to km/min
				currTrain.maximumPossibleSpeed = maximumPossibleSpeed / 60;
				ChangeTimeTable.changeToMin(currTrain);
				arrayTrain.add(currTrain);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return (arrayTrain);
	}

	/**
	 * @return true if the timeTablesSize matches the values obtained while
	 *         previously testing the testCase
	 */
	public boolean verifyTimeTableSize() {

		try {
			StreamTokenizer outputFileReaderStreamTokenizer = GlobalVar.outputFileReaderStreamTokenizer;
			// read the TrainTotalTimeTableSize string

			outputFileReaderStreamTokenizer.nextToken();
			String timeTableString = outputFileReaderStreamTokenizer.sval;

			// read the size
			outputFileReaderStreamTokenizer.nextToken();
			int size = (int) outputFileReaderStreamTokenizer.nval;

			int trainTimeTableSize = this.timeTables.size();
			if (timeTableString.equalsIgnoreCase("TrainTotalTimeTimeTableSize")
					&& size == trainTimeTableSize) {
				return true;
			}
			GlobalVar.testCaseVerified = false;
			return false;
		}

		catch (IOException e) {
			System.out
					.println("Train: verifyTimeTableSize: IOException occured");
			System.out.println("TestCase has failed due to IOException");
			GlobalVar.testCaseVerified = false;
			return false;
		}
	}

	/**
	 * @return the designated loop number of the destination station of the
	 *         train.
	 */
	public int getDestinationLoopNumber() {
		if (isScheduled == true) {
			int referenceTableSize = this.refTables.size();

			// reference loop number of the last reference entry.
			return this.refTables.get(referenceTableSize - 1).refLoopNo;
		} else {// unscheduled
			return this.endLoopNo;
		}
	}

	/**
	 * Get reference table entry from the station name.
	 * 
	 * @param stationName
	 * @return the reference entry corresponding to the station name. If there
	 *         is no such entry return null.
	 */
	public ReferenceTableEntry getRefTabEntryFromStationName(String stationName) {
		ArrayList<ReferenceTableEntry> referenceTable = this.refTables;
		for (ReferenceTableEntry referenceTableEntry : referenceTable) {
			if (referenceTableEntry.stationName.equalsIgnoreCase(stationName)) {
				return referenceTableEntry;
			}
		}
		return null;
	}

	/**
	 * Get the loop number of the last loop to be traversed by the train.
	 * 
	 * @return loop number of the destination loop of the train
	 */
	public int getLastLoopNo() {
		if (this.isScheduled) {// scheduled train
			int size = refTables.size();
			int loopNo = refTables.get(size - 1).refLoopNo;
			return loopNo;
		} else {// unscheduled train
			return this.endLoopNo;
		}
	}

	/**
	 * Determine if the train has a scheduled halt at the loop
	 * 
	 * @param loop
	 * @return true if the train has a scheduled halt else returns false.
	 */
	public boolean hasScheduledHalt(Loop loop) {
		if (isScheduled) {
			String stationName = loop.station.stationName;
			ReferenceTableEntry referenceTableEntry = getRefTabEntryFromStationName(stationName);
			if (referenceTableEntry.refDepTime > referenceTableEntry.refArrTime)
				return true;
			else
				return false;
		} else {

			String endStationString = GlobalVar.getStationName(this.endLoopNo);
			// System.out.println("endStation " + endStationString
			// + " loopStation " + loop.stationName);
			if (endStationString.equalsIgnoreCase(loop.stationName))
				return true;
			return false;
		}
	}

	public void printVelocityProfileForTrain() {
		double totalTime = 0;

		ArrayList<VelocityProfile> velocityProfileList = getVelocityProfileList();

		for (VelocityProfile velocityProfile : velocityProfileList) {
			System.out.print("arrivaltime " + totalTime + " ");
			velocityProfile.print(this.direction);
			totalTime += velocityProfile.time;
		}

		System.out.println("Total time of the train is " + totalTime
				+ " stored total time is " + this.totalTime());
		System.out.println("StartTime " + this.startTime + " endTime "
				+ this.timeTables.get(timeTables.size() - 1).departureTime);
	}

	/**
	 * @return {@link ArrayList} of {@link VelocityProfile} in the order which
	 *         the train traverses.
	 */
	public ArrayList<VelocityProfile> getVelocityProfileList() {

		ArrayList<VelocityProfile> velocityProfileList = new ArrayList<VelocityProfile>();

		for (int i = 0; i < this.timeTables.size(); i++) {
			TimetableEntry tte = this.timeTables.get(i);

			int j;
			int inc;
			if (this.direction == 0) {
				j = 0;
				inc = 1;
			} else {
				j = tte.velocityProfileArray.size() - 1;
				inc = -1;
			}
			for (; j < tte.velocityProfileArray.size() && j >= 0; j = j + inc) {
				velocityProfileList.add(tte.velocityProfileArray.get(j));
			}
		}
		return velocityProfileList;
	}

	public void printDiscontinuity() {
		double lastEndVelocity = 0.0;

		ArrayList<VelocityProfile> velocityProfileList = this
				.getVelocityProfileList();
		System.out.println("Discontinuity found at these points");
		for (VelocityProfile velocityProfile : velocityProfileList) {
			if (velocityProfile.startVelocity != lastEndVelocity) {
				System.out.print("lastEndVelocity " + lastEndVelocity + " ");
				velocityProfile.print(this.direction);
			}
			lastEndVelocity = velocityProfile.endVelocity;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unused")
	public int compare(Train train1, Train train2) {
		int var = 0;
		double startTime1, startTime2, departTime1, departTime12;
		int priority1, priority2;
		boolean isScheduled1 = false, isScheduled2 = false;

		TimetableEntry e, f;
		priority1 = train1.priority;
		priority2 = train2.priority;
		startTime1 = train1.startTime;
		startTime2 = train2.startTime;
		departTime1 = train1.departTime;
		departTime12 = train2.departTime;
		isScheduled1 = train1.isScheduled;
		isScheduled2 = train2.isScheduled;

		if (priority1 > priority2) {
			var = 1;
		} else if (priority1 < priority2) {
			var = -1;
		} else if (priority1 == priority2) {
			if (isScheduled1 == true && isScheduled2 == true) {
				if (departTime1 > departTime12) {
					var = 1;
				} else if (departTime1 < departTime12) {
					var = -1;
				} else {
					var = 0;
				}
			} else {
				if (startTime1 > startTime2) {
					var = 1;
				} else if (startTime1 < startTime2) {
					var = -1;
				} else {
					var = 0;
				}
			}
		}

		return var;
	}

	public boolean isSuburban() {
		if (priority == 10)
			return true;
		return false;
	}

}

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Enumeration;

import util.Debug;

/**
 * read section
 */
public class ReadSection {
	/**
	 * @return list of stations
	 * @throws SimException
	 */
	public static ArrayList<Station> readStation() throws SimException {
		ArrayList<Station> arrayStn = new ArrayList<Station>();

		// ArrayList arrayLoop = new ArrayList();/////////////////
		Station currStn;
		// Block currBlk;
		// Loop currLoop;
		// int blockNo =0,loopNo=0;

		try {
			Reader reader;

			// Devendra
			if (simStart.nogui) {
				reader = new FileReader(GlobalVar.pathStation);
			} else {
				reader = new FileReader(GlobalVar.fileStation);
			}

			StreamTokenizer st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(true);
			st.slashStarComments(true);

			while (st.nextToken() != StreamTokenizer.TT_EOF) {

				double startMileP, endMileP, crossVel;
				String name;
				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug
							.print("readStation: Error in format of input file : station.dat......");
					Debug.print("readStation: Error : station name expected");
					throw new SimException();
				}

				Debug.print("readStation: value read is " + st.sval);
				name = st.sval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("readStation: Error in format of input file : station.dat......");
					Debug
							.print("readStation: Error : start mile post expected");
					throw new SimException();
				}

				Debug.print("readStation: value read is " + st.nval);
				startMileP = st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("readStation: Error in format of input file : station.dat......");
					Debug.print("readStation: Error : end mile post expected");
					throw new SimException();
				}

				Debug.print("readStation: value read is " + st.nval);
				endMileP = st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("readStation: Error in format of input file : station.dat......");
					Debug
							.print("readStation: Error : cross over velocity in km/hr  expected");
					throw new SimException();
				}
				Debug.print("readStation: value read is " + st.nval);
				crossVel = st.nval / 60;

				currStn = new Station(startMileP, endMileP, crossVel, name);
				arrayStn.add(currStn);
			}
		} catch (FileNotFoundException e) {
			Debug.print("readStation: FileNotFound");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (arrayStn);
	}

	/**
	 * @return list of sections
	 * @throws SimException
	 */

	public static ArrayList<Loop> readSection() throws SimException {// ///////////////////

		ArrayList<Loop> arrayLoop = new ArrayList<Loop>();
		Reader reader;
		StreamTokenizer st;
		try {

			// Devendra
			if (simStart.nogui) {
				reader = new FileReader(GlobalVar.pathBlock);
			} else {
				reader = new FileReader(GlobalVar.fileBlock);
			}

			st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(true);
			st.slashStarComments(true);

			Debug.print("readSection: Now will read all blocks");

			while (st.nextToken() != StreamTokenizer.TT_EOF) {

				new Block(st);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// Devendra
			if (simStart.nogui) {
				reader = new FileReader(GlobalVar.pathLoop);
			} else {
				reader = new FileReader(GlobalVar.fileLoop);
			}

			st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(true);
			st.slashStarComments(true);

			Debug.print("readSection: Now will read all loops");
			while (st.nextToken() != StreamTokenizer.TT_EOF) {

				new Loop(st);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (arrayLoop);// ////////////////
	}

	/**
	 * @param blkNo
	 * @return {@link Block}
	 */
	public static Block getBlock(int blkNo) {
		HashBlockEntry hbEntry = (GlobalVar.hashBlock.get(new Integer(blkNo)));

		if (hbEntry != null) {
			Debug.print("block no " + blkNo + " read");
			return (hbEntry.block);
		}

		System.out.println("Error: block not present " + blkNo);
		System.exit(0);
		return null;
	}

	/**
	 * @param arrayStn
	 */
	public static void linkBlocks(ArrayList<Station> arrayStn) {
		Debug.print("ReadSection: linkBlocks: ");
		Station stn, prevStn = null;

		// ArrayList blkSect;
		// ArrayList loopArray;
		// Block prevBlock=null,blk;
		// Loop loop,mainLoop=null;
		// for up block sections
		for (int i = 0; i < arrayStn.size(); i++) {

			stn = arrayStn.get(i);
			Debug.print("ReadSection: linkBlocks: " + stn.stationName);

			if (i == arrayStn.size()) {
				stn.nextStation = null;
			}

			// assign previous and next stations........
			if (prevStn != null) {
				prevStn.nextStation = stn;
				stn.previousStation = prevStn;
			} else {
				stn.previousStation = null;
			}
			prevStn = stn;
		}
	}

	/**
	 * convertLinks
	 */
	public static void convertLinks() {

		Debug.print("ReadSection: convertLinks: In convert links function ");

		for (Enumeration<HashBlockEntry> e = GlobalVar.hashBlock.elements(); e
				.hasMoreElements();) {

			HashBlockEntry hbEntry = e.nextElement();
			Block currBlock = hbEntry.block;

//			System.out.println("ReadSection: convertLinks: block no is "
//					+ currBlock.blockNo + "   " + hbEntry.upLinks.size()
//					+ "   " + hbEntry.downLinks.size());

			for (int i = 0; i < hbEntry.upLinks.size(); i++) {

				Debug.print("ReadSection: convertLinks: Uplinks ");
				Link link = hbEntry.upLinks.get(i);

				Debug.print("Link: convert: link's nextBlockNo "
						+ link.nextBlockNo);

				link.convert();
				currBlock.nextBlocks.add(link);
			}

			Debug.print("ReadSection: convertLinks: ");
			for (int i = 0; i < hbEntry.downLinks.size(); i++) {
				Debug.print("Link: convert: Downlinks");
				Link link = hbEntry.downLinks.get(i);

				Debug.print("Link: convert: link's nextBlockNo "
						+ link.nextBlockNo);
				link.convert();
				currBlock.previousBlocks.add(link);
			}
		}
	}

	/**
	 * read parameters
	 */
	public static void readParameters() {
		int DelayFactor = 0;
		double RedFailVelo = 0;
		int NoOfColour = 0, RedFailWaitTime = 0;
		// Block block ;
		double BWT = 0; // BlockWorking Time
		// int SimulationTime; // simulation time

		try {
			Reader reader;

			// Devendra
			if (simStart.nogui) {
				reader = new FileReader(GlobalVar.pathParam);
			} else {
				reader = new FileReader(GlobalVar.fileParam);
			}

			StreamTokenizer st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(false);
			st.slashStarComments(true);

			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("readParameters: Error in format of input file : param.dat......");
					Debug
							.print("readParamaters: Error : Simulation Time expected");
				}

				Debug.print("value read is " + st.nval);
				GlobalVar.simulationTime = (int) st.nval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("readParameters: Error in format of input file : param.dat......");
					Debug
							.print("readParameters: Error : Delay  Factor expected");
				}
				Debug.print("readParameters: value read is " + st.nval);
				DelayFactor = (int) st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("readParameters: Error in format of input file : param.dat......");
					Debug
							.print("readParameters: Error : Block Working Time expected");
				}

				Debug.print("readParameters: value read is " + st.nval);
				BWT = st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("readParameters: Error in format of input file : param.dat......");
					Debug.print("readParameters: Error : No Of Color expected");
				}
				Debug.print("readParameters: value read is " + st.nval);
				NoOfColour = (int) st.nval;

				GlobalVar.numberOfColour = NoOfColour;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("readParameters: Error in format of input file : param.dat......");
					Debug
							.print("readParameters: Error : Red Fail Wait Time expected");
				}

				Debug.print("readParameters: value read is " + st.nval);
				RedFailWaitTime = (int) st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("readParameters: Error in format of input file : param.dat......");
					Debug
							.print("readParameters: Error : Red Fail Velocity expected");
				}
				Debug.print("readParamters: value read is " + st.nval);
				RedFailVelo = st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug
							.print("readParameters: Error in format of input file : param.dat......");
					Debug
							.print("readParameters: Error : warned distance expected");
				}
				Debug.print("readParameters: value read is " + st.nval);
				GlobalVar.warnerDistance = st.nval;
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			Debug.print("readParamters: FileNotFound param.dat");
		} catch (Exception e) {

			e.printStackTrace();
			Debug.print("readParamters: IO Error");
		}

		GlobalVar.delayFactor = DelayFactor;

		GlobalVar.numberOfColour = NoOfColour;
		GlobalVar.redFailVelocity = RedFailVelo / 60;
		GlobalVar.redFailWaitTime = RedFailWaitTime;
		GlobalVar.minTime = .001;
		GlobalVar.blockWorkingTime = BWT;
	}

	/**
	 * @param filepath
	 * @param testCaseDirectory
	 */
	public static void readFiles(String filepath, String testCaseDirectory) {
		try {
			Debug.print("readFiles: In the try section");
			Reader reader = new FileReader(filepath);
			StreamTokenizer st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(false);
			st.slashStarComments(false);
			Debug.print("readFiles: Before while loop");

			// st.nextToken();
			String baseDirectory = testCaseDirectory;
			Debug.print("readSection: readFiles: baseDirectory = "
					+ baseDirectory);

			while (st.nextToken() != StreamTokenizer.TT_EOF) {

				Debug.print("readFiles: Inside while loop");
				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug
							.print("readFiles: Error in format of input file : station.dat......");
					Debug.print("readFiles: Error : keyword expected");
					throw new SimException();
				}

				String sval = st.sval;
				Debug.print("readFiles: value read is " + sval);

				if (sval.equals("param.dat")) {

					sval = st.sval;
					Debug.print("readFiles: value read is " + st.sval);

					String pathParam = baseDirectory + st.sval;
					Debug.print("ReadSection: readFiles: pathParam = "
							+ pathParam);

					GlobalVar.pathParam = pathParam;
					GlobalVar.fileParam = new File(pathParam);
					
					continue;
				}

				if (st.sval.equals("block.txt")) {

					Debug.print("readFiles: value read is " + st.sval);

					String pathBlock = baseDirectory + st.sval;
					Debug.print("ReadSection: readFiles: pathBlock = "
							+ pathBlock);

					GlobalVar.pathBlock = pathBlock;
					GlobalVar.fileBlock = new File(pathBlock);
					continue;
				}

				if (st.sval.equals("loop.txt")) {

					Debug.print("readFiles: value read is " + st.sval);

					String pathLoop = baseDirectory + st.sval;
					Debug.print("ReadSection: readFiles: pathLoop = "
							+ pathLoop);
					GlobalVar.pathLoop = pathLoop;
					GlobalVar.fileLoop = new File(pathLoop);
					continue;
				}

				if (st.sval.equals("station.txt")) {

					Debug.print("value read is " + st.sval);

					String pathStation = baseDirectory + st.sval;
					Debug.print("ReadSection: readFiles: pathStation = "
							+ pathStation);

					GlobalVar.pathStation = pathStation;
					GlobalVar.fileStation = new File(pathStation);
					continue;
				}

				if (st.sval.equals("unscheduled.txt")) {

					Debug.print("value read is " + st.sval);

					String pathUnscheduled = baseDirectory + st.sval;
					Debug.print("ReadSection: readFiles: pathUnscheduled = "
							+ pathUnscheduled);

					GlobalVar.pathUnscheduled = pathUnscheduled;
					GlobalVar.fileUnscheduled = new File(pathUnscheduled);
					continue;
				}

				if (st.sval.equals("scheduled.txt")) {

					Debug.print("value read is " + st.sval);

					String pathScheduled = baseDirectory + st.sval;
					Debug.print("ReadSection: readFiles: pathScheduled = "
							+ pathScheduled);

					GlobalVar.pathScheduled = pathScheduled;
					GlobalVar.fileScheduled = new File(pathScheduled);
					continue;
				}

				if (st.sval.equals("gradient.txt")) {

					Debug.print("value read is " + st.sval);
					// GlobalVar.p = st.sval;

					String pathGradient = baseDirectory + st.sval;
					Debug.print("ReadSection: readFiles: pathGradient = "
							+ pathGradient);

					GlobalVar.pathGradient = pathGradient;
					GlobalVar.fileGradient = new File(pathGradient);
					continue;
				}

				if (st.sval.equals("gradientEffect.txt")) {

					Debug.print("value read is " + st.sval);
					// GlobalVar.pathScheduled = st.sval;

					String pathGradientEffect = baseDirectory + st.sval;
					Debug.print("ReadSection: readFiles: pathGradientEffect = "
							+ pathGradientEffect);

					GlobalVar.pathGradientEffect = pathGradientEffect;
					GlobalVar.fileGradientEffect = new File(pathGradientEffect);
					continue;
				}

				if (st.sval.equals("BlockDirectionInInterval.txt")) {

					Debug.print("value read is " + st.sval);
					// GlobalVar.pathScheduled = st.sval;

					String blockDirectionInInterval = baseDirectory + st.sval;
					Debug
							.print("ReadSection: readFiles: blockDirectionInInterval = "
									+ blockDirectionInInterval);

					GlobalVar.pathBlockDirectionInInterval = blockDirectionInInterval;
					GlobalVar.hasBlockDirectionFile = true;
					GlobalVar.fileBlockDirectionInInterval = new File(
							blockDirectionInInterval);
					continue;
				}

			}
		} catch (FileNotFoundException e) {
			Debug.print("FileNotFound param.dat");
		} catch (Exception e) {
			Debug.print("IO Error");
		}
	}
}

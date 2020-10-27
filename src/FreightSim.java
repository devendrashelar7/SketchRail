import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;

import util.Debug;

/**
 * FreightSimulator
 */
public class FreightSim {
	/**
	 * currentTrainNo
	 */
	int currentTrainNo;// gives the current train which is being scheduled.
	/**
	 * graphPanel
	 */
	GraphPanel graphPanel; // Graphical output

	/**
	 * trainsArray
	 */
	ArrayList<Train> trainsArray;

	/**
	 * stationsArray
	 */
	ArrayList<Station> stationsArray;
	/**
	 * original
	 */
	int original;

	/**
	 * constructor.
	 * 
	 * @param trains
	 *            {@link ArrayList} of trains
	 * @param stations
	 *            {@link ArrayList} of stations
	 * @param ori
	 */
	public FreightSim(ArrayList<Train> trains, ArrayList<Station> stations,
			int ori) {
		trainsArray = trains;
		stationsArray = stations;
		original = ori;
	}

	/**
	 * Simulate the next train.
	 * 
	 * @return currentTrainNumber. returns -1 on error.
	 */
	public int simulateNextTrain() {

		Debug.print("I am in simulate next train ");

		Train currTrain = trainsArray.get(currentTrainNo);

		Debug.print("Train no is " + currTrain.trainNo);
		int val = simulateTrain(currTrain);
		if (val == -1)
			return -1;
		currentTrainNo++;

		// currTrain.printVelocityProfileForTrain();
		// currTrain.printDiscontinuity();

		if (currentTrainNo >= trainsArray.size()) {
			return -1;
		}
		return currentTrainNo;
	}

	/**
	 * FreightSim: int simulateTrain(Train currTrain) 1) First get the train's
	 * starting loop number, then its station from the loop number. Then we go
	 * to station.simulateTrain(Train currTrain)
	 * 
	 * @param currTrain
	 * @return simulateTrain
	 */
	private int simulateTrain(Train currTrain) {
		Station currStn = null;

		// double arrivalTime,departTime;
		Debug
				.print("FreightSim: simulateTrain: trainNo = "
						+ currTrain.trainNo);
		Debug.currTrainNo = currTrain.trainNo;

		GlobalVar.currentTrain = currTrain;

		// arrivalTime=currTrain.startTime;
		// departTime=currTrain.startTime;

		int loopNo = currTrain.startLoopNo;
		HashBlockEntry hbLinkEntry = GlobalVar.hashBlock.get(loopNo);

		if (hbLinkEntry != null) {
			currStn = ((Loop) hbLinkEntry.block).station;
		} else {
			Debug.print("FreightSim: simulateTrain: Error: Loop not present "
					+ loopNo);
			System.out.println("hbLinkEntry is null");
			System.exit(0);
		}

		if (currStn == null) {
			throw new NullPointerException(
					"FreightSim: simulateTrain: currStn is null");
		}
		int val = currStn.simulateTrain(currTrain);

		if (val == -1)
			return -1;

		Debug.print("FreightSim: simulateTrain: ");
		if (graphPanel != null) {
			graphPanel.drawTrain(currTrain);
		}
		GlobalVar.currentTrain = null;

		if (val == -1)
			return -1;
		return 0;
	}

	/**
	 * Produce the graph.
	 * 
	 * @param p
	 */
	public void produceGraph(GraphPanel p) {
		graphPanel = p;
	}

	/**
	 * NoGraph.
	 */
	public void noGraph() {
		graphPanel = null;
	}

	/**
	 * simulate.
	 */
	public void simulate() {
		while (currentTrainNo < trainsArray.size()) {
			int val = simulateNextTrain();
			if (val == -1)
				break;
		}
		System.out.println("Exited");
		if (GlobalVar.verifyTestCases) {
			StreamTokenizer st = GlobalVar.outputFileReaderStreamTokenizer;
			try {
				st.nextToken();
				String simulationOverString = st.sval;
				if (!simulationOverString.equalsIgnoreCase("SimulationIsOver")) {
					System.out.println("TestCase "
							+ GlobalVar.testCaseDirectory
							+ " has failed while reading the simulationIsOver");
				}
			} catch (IOException e) {
				System.out.println("FreightSim: IOException occured");
			}
		} else {
			System.out.println("SimulationIsOver");
		}

		System.out.println("Exited");
	}

}

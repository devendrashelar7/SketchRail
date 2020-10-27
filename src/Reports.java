/**
 * Title: Simulation of Train Pathing Description: Copyright: Copyright (c) 2002
 * Company: IIT
 * 
 * @author
 * @version 1.0
 */

public class Reports {

    /**
     * constructor.
     */
    public Reports() {
    }

    /**
     * 
     */
    public void outputTotalTrainTravel() {
	double totalTime = 0;
	for (int i = 0; i < GlobalVar.trainArrayList.size(); i++) {
	    Train train = GlobalVar.trainArrayList.get(i);
	    totalTime += train.totalTime();

	    System.out.println(train.trainNo + " -  " + train.totalTime() + "  -  "
		    + train.travelTime() + " - " + totalTime / (i + 1));
	}
	// System.out.println("Average Travelling time " +
	// totalTime/GlobalVar.trainArrayList.size());
	System.out.println("Average Travelling time " + totalTime);

    }
}

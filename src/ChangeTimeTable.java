import java.util.ArrayList;

/**
 * 
 */
class ChangeTimeTable {
	/**
	 * @param Trains
	 */

	public static void changeToMin(ArrayList<Train> Trains) {
		Object o;
		TimetableEntry srf;
		int i, j;
		double time, min, hrs;
		Train train;
		for (j = 0; j < Trains.size(); j++) {
			train =  Trains.get(j);

			if (train.priority < 5) {
				for (i = 0; i < train.timeTables.size(); i++) {
					o = train.timeTables.get(i);
					srf = (TimetableEntry) o;
					time = srf.arrivalTime;
					min = (int) (time / 100);
					hrs = time - min * 100;
					srf.arrivalTime = min * 60 + hrs;
					time = srf.departureTime;
					min = (int) (time / 100);
					hrs = time - min * 100;
					srf.departureTime = min * 60 + hrs;
				}
			} else {
				time = train.startTime;
				min = (int) (time / 100);
				hrs = time - min * 100;
				train.startTime = min * 60 + hrs;
			}
		}
	}

	/**
	 * @param train
	 */
	public static void changeToMin(Train train) {
		TimetableEntry srf;
		int i;
		double time, min, hrs;
		if (train.priority < 5) {
			for (i = 0; i < train.timeTables.size(); i++) {

				srf = train.timeTables.get(i);

				time = srf.arrivalTime;
				min = (int) (time / 100);
				hrs = time - min * 100;
				srf.arrivalTime = min * 60 + hrs;

				time = srf.departureTime;
				min = (int) (time / 100);
				hrs = time - min * 100;
				srf.departureTime = min * 60 + hrs;
			}
		} else {
			time = train.startTime;
			min = (int) (time / 100);
			hrs = time - min * 100;
			train.startTime = min * 60 + hrs;
		}
	}

	/**
	 * @param Trains
	 */

	public static void changeToHrs(ArrayList<Train> Trains) {
		TimetableEntry srf;
		int i, j;
		double time, min, hrs;
		Train train;
		for (j = 0; j < Trains.size(); j++) {
			train =  Trains.get(j);
			for (i = 0; i < train.timeTables.size(); i++) {
				srf =  train.timeTables.get(i);
				time = srf.arrivalTime;
				hrs = (int) (time / 60);
				min = time - hrs * 60;
				srf.arrivalTime = hrs * 100 + min;
				time = srf.departureTime;
				hrs = (int) (time / 60);
				min = time - hrs * 60;
				srf.departureTime = hrs * 100 + min;
			}
		}
	}

	/**
	 * @param time
	 * @return time in minutes
	 */
	public static int changeToMin(int time) {
		int min = (time / 100);
		int hrs = time - min * 100;
		return (min * 60 + hrs);
	}

	/**
	 * @param time
	 * @return time in hrs
	 */
	public static int changeToHrs(int time) {
		int hrs = (time / 60);
		int min = time - hrs * 60;
		return (hrs * 100 + min);
	}

	/**
	 * @param Trains
	 */

	public static void changeRefToMin(ArrayList<Train> Trains) {
		ReferenceTableEntry srf;
		int i, j;
		double var, var1, var2;
		Train train;
		for (j = 0; j < Trains.size(); j++) {
			train = Trains.get(j);

			// if (train.priority < 5 ) {
			if (train.isScheduled == true) {
				for (i = 0; i < train.refTables.size(); i++) {
					srf = train.refTables.get(i);
					var = srf.refArrTime;
					var1 = (int) (var / 100);
					var2 = var - var1 * 100;
					srf.refArrTime = var1 * 60 + var2;
					var = srf.refDepTime;
					var1 = (int) (var / 100);
					var2 = var - var1 * 100;
					srf.refDepTime = var1 * 60 + var2;
				}
			} else {
				var = train.startTime;
				var1 = (int) (var / 100);
				var2 = var - var1 * 100;
				train.startTime = var1 * 60 + var2;
			}
		}
	}

	/**
	 * @param train
	 */
	public static void changeRefToMin(Train train) {
		ReferenceTableEntry srf;
		int i;
		double var, var1, var2;

		if (train.isScheduled == true) {
			for (i = 0; i < train.refTables.size(); i++) {
				srf = train.refTables.get(i);

				// converting arrival time in decimal to minutes
				var = srf.refArrTime;
				var1 = (int) (var / 100);
				var2 = var - var1 * 100;
				srf.refArrTime = var1 * 60 + var2;

				// changing departure time in decimal to minutes
				var = srf.refDepTime;
				var1 = (int) (var / 100);
				var2 = var - var1 * 100;
				srf.refDepTime = var1 * 60 + var2;
			}
		} else {
			var = train.startTime;
			var1 = (int) (var / 100);
			var2 = var - var1 * 100;
			train.startTime = var1 * 60 + var2;
		}
	}

	/**
	 * @param Trains
	 */

	public static void changeRefToHrs(ArrayList<Train> Trains) {
		ReferenceTableEntry srf;
		int i, j;
		double var, var1, var2;
		Train train;
		for (j = 0; j < Trains.size(); j++) {
			train =  Trains.get(j);
			for (i = 0; i < train.refTables.size(); i++) {
				srf = train.refTables.get(i);
				var = srf.refArrTime;
				var1 = (int) (var / 60);
				var2 = var - var1 * 60;
				srf.refArrTime = var1 * 100 + var2;
				var = srf.refDepTime;
				var1 = (int) (var / 60);
				var2 = var1 * 60 - var;
				srf.refDepTime = var1 * 100 + var2;
			}
		}
	}
}
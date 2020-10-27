import java.util.ArrayList;

import util.Debug;

/**
 * BlockScheduler.
 */
class BlockScheduler {

	/**
	 * currentTrain
	 */
	protected Train currentTrain;
	/**
	 * currentBlock
	 */
	protected Block currentBlock;
	/**
	 * flag
	 */
	boolean flag;

	/**
	 * blockSchedulingParameters
	 */
	// private BlockSchedulingParameters blockSchedulingParameters;

	/**
	 * @param block
	 * @param train
	 */
	public BlockScheduler(Block block, Train train) {
		flag = false;
		currentTrain = train;
		currentBlock = block;
		// blockSchedulingParameters = null;
	}

	/**
	 * Determine if currenTrain has reached its destination station
	 * 
	 * @return true if the currentTrain has reached its destination station.
	 */
	public boolean hasReachedDestinationStation() {
		if (currentBlock.getClass().getName().equalsIgnoreCase("loop")) {
			String currentStation = GlobalVar
					.getStationName(currentBlock.blockNo);
			if (currentStation.equalsIgnoreCase(currentTrain.endStation))
				return true;
		}
		return false;
	}

	/**
	 * @return true if there is no next block to be considered. If the
	 *         simulation is over or current block has no next block return
	 *         true.
	 */
	public boolean hasNoNextBlockToTraverse() {
		return (currentBlock.getNextBlock(currentTrain.direction, currentTrain) == null || GlobalVar.flag);
	}

	/**
	 * @param arrivalTime
	 * @return true if currentBlock has red signal at the arrival time
	 */
	public boolean sawRedSignal(double arrivalTime) {
		return (currentBlock.getSignal(currentTrain, GlobalVar.numberOfColour,
				arrivalTime, currentTrain.direction) == 0);
	}

	/**
	 * Handle the cases in which departure time is greater than the arrival time
	 * 
	 * @param departureTime
	 * @param arrivalTime
	 * @param startVelocity
	 * @return null in exceptional cases.
	 */
	public StatusTraverseBlock handleDepTimeGreaterThanArrTime(
			double departureTime, double arrivalTime, double startVelocity) {
		if (departureTime > arrivalTime) {
			Debug
					.print("BlockScheduler: statusTraverseBlock: In deptime > arrival time ");

			if (currentBlock.waitingPermitted()) {
				Debug
						.print("BlockScheduler: statusTraverseBlock: waiting is permitted");
				RunTimeReturn runtimereturn = currentBlock.getRunTimeSignal(
						currentTrain, 1, arrivalTime, startVelocity, 0.0D);

				Debug.print("BlockScheduler: traverseBlock: ");
				Debug
						.print("came out of block.getRunTimeSignal and now in blockSheduler.traverseBlock ");

				if (-1D == runtimereturn.totalTime) {
					System.out
							.println("velocityProfileArray of runTimeReturn has size 0");
					return new StatusTraverseBlock(false, departureTime,
							currentTrain.trainNo);
				}
			} else {
				System.out
						.println("waiting is not permitted case of deptime<arrivaltime ");
				return new StatusTraverseBlock(false, departureTime,
						currentTrain.trainNo);
			}
		}
		return null;
	}

	/**
	 * Determine the time when a scheduled train should leave a station of halt.
	 * It follows basic rules like a train should not leave a scheduled halt
	 * before its departure time. Also, if a train arrives at a station late
	 * then it should wait at the station for the pre-specified duration of
	 * waiting time.
	 * 
	 * @param arrivalTime
	 * 
	 * @return departure time for a scheduled train at a halt. returns 0 if the
	 *         train is not scheduled or the currentBlock is not a loop.
	 */
	public double calDepTimeForSchedTrainHalt(double arrivalTime) {
		double calDepTime = 0.0;

		if (currentTrain.isScheduled && currentBlock.isLoop()
				&& currentBlock.blockNo != 0) {
			String stationName = ((Loop) currentBlock).station.stationName;
			ReferenceTableEntry refTabEntry = currentTrain
					.getRefTabEntryFromStationName(stationName);

			double refArrTime = refTabEntry.refArrTime;
			double refDepTime = refTabEntry.refDepTime;

			if (refArrTime == refDepTime) {
				// reference arrival time is same as the reference departure
				// time
				calDepTime = arrivalTime;
			} else if (refArrTime > refDepTime) {
				// arrival time is greater than the departure time
			} else if (arrivalTime >= refArrTime) {
				calDepTime = arrivalTime + (refDepTime - refArrTime);
			} else {// (arrivalTime < refArrTime)
				calDepTime = refDepTime;
			}
		}

		return calDepTime;
	}

	/**
	 * BlockScheduler: StatusTraverseBlock traverseBlock(arrivalTime, deptTime,
	 * startVelocity) 1) if there is no next block it traverses the last block:
	 * traverseLastBlock(arrivalTime, startVelocity) 2) if got a redSignal it
	 * handles that with : handleRedSignal(arrivalTime); Then it calls the
	 * getSortedLinks. 3) if the train is not supposed to wait, then it just
	 * zooms past the block. 4) Otherwise it waits for prescheduled waiting time
	 * obtained using ReferenceTimeTable. 5) Then if the scheduled departure
	 * time exceeds the 24hrs limit it returns a StatusTraverseBlock which says
	 * that the limit is reached. 6) if the link priority selected is 1 then it
	 * marks its flag as true. 7) then it goes to traversePath(link,
	 * arrivalTime, tempDepartureTime, startVelocity, flag2,
	 * GlobalVar.numberOfColour);
	 * 
	 * 
	 * @param arrivalTime
	 * @param departureTime
	 * @param startVelocity
	 * @return {@link StatusTraverseBlock}
	 */
	public StatusTraverseBlock traverseBlock(double arrivalTime,
			double departureTime, double startVelocity) {
		{
//			System.out.println();
//			System.out.println("temp is " + GlobalVar.temp + " block "
//					+ currentBlock.blockNo + " train " + currentTrain.trainNo
//					+ " arrival time " + arrivalTime + " departure time "
//					+ departureTime + " startVelocity " + startVelocity);
//			GlobalVar.temp++;

		}
		double tempDepartureTime = departureTime;

		double minArrTime = 1.7976931348623157E+308D;
		double minDepTime = 1.7976931348623157E+308D;

		// if the loop belongs to the station at which the train has to halt,
		// then traverse the last block.
		if (hasReachedDestinationStation()) {
//			System.out.println("BlockScheduler: Reached the endStation");
			return traverseLastBlock(arrivalTime, startVelocity);
		}

		if (GlobalVar.temp > 2000)
			System.exit(0);

		// if there is no next block traverse the last block again.
		if (hasNoNextBlockToTraverse()) {
			Debug.print("BlockScheduler: traverseBlock: nextBlock is null");
			return traverseLastBlock(arrivalTime, startVelocity);
		}

		// if there is red signal, handleRedSignal.
		if (sawRedSignal(arrivalTime)) {
//			System.out.println("Train " + currentTrain.trainNo
//					+ " saw red signal at block " + currentBlock.blockNo);
			return handleRedSignal(arrivalTime);
		}

		StatusTraverseBlock statusTraverseBlock = handleDepTimeGreaterThanArrTime(
				tempDepartureTime, arrivalTime, startVelocity);
		if (statusTraverseBlock != null)
			return statusTraverseBlock;

		ArrayList<Link> linkList = null;

		linkList = currentBlock.getNextBlocks(currentTrain.direction)
				.getSortedLinks(currentTrain, currentBlock, arrivalTime,
						tempDepartureTime, startVelocity);

		if (linkList == null || linkList.size() == 0) {
			// no links to be considered. Hence return false status. with limit
			// status true
			System.out
					.println("linkList is null or has no links. Hence limit reached");
			return new StatusTraverseBlock(false, true, currentTrain.trainNo);
		}

		double calDepTime = calDepTimeForSchedTrainHalt(arrivalTime);

		// System.out.println("calDepTime "+calDepTime+" tempDepTime "+tempDepartureTime);

		tempDepartureTime = Math.max(tempDepartureTime, calDepTime);

		// 1440D = 24hrs * 60 minutes
		if (tempDepartureTime > 1440D
				&& currentTrain.startLoopNo == currentBlock.blockNo) {

//			System.out.println("Limit Reached");
			return new StatusTraverseBlock(false, true, currentTrain);
		}

		// flag1 false implies that on some particular path, on some particular
		// block the train had to stop or there was signal failure
		boolean flag1 = true;
		for (int i = 0; i < linkList.size(); i++) {
			Link link = linkList.get(i);

			boolean linkPriorityIsNotOne = !link.isPriorityOneLink();

//			System.out.println("Calling traversePath: arrivalTime "
//					+ arrivalTime + " tempDepTime " + tempDepartureTime
//					+ " startVelocity " + startVelocity);
			StatusTraverseBlock statusTraversePath = traversePath(link,
					arrivalTime, tempDepartureTime, startVelocity,
					linkPriorityIsNotOne, GlobalVar.numberOfColour);

			// successfully got a path
			if (statusTraversePath.status) {
				return new StatusTraverseBlock(true, arrivalTime,
						currentTrain.trainNo);
			}

			// signal value of status traverse path ranges is -2,-1 or 0
			if (((StatusTraversePath) statusTraversePath).signal == -2) {
				Debug.print("IN SIGNAL FAILED MODE");
				return new StatusTraverseBlock(false, currentTrain.startTime,
						currentTrain.trainNo);
			}

			if (-1 == ((StatusTraversePath) statusTraversePath).signal) {
				flag1 = false;
			}

			if (minDepTime > statusTraversePath.departureTime) {
				minDepTime = statusTraversePath.departureTime;
				minArrTime = ((StatusTraversePath) statusTraversePath).arrivalTime;
			}
		}// end of for loop consisting of linklist

		if (currentBlock.waitingPermitted() && !flag1) {
			Debug.print("waiting is permitted so trying to wait");
			if (GlobalVar.hasBlockDirectionFile) {
				return currentBlock.traverseBlock(currentTrain,
						arrivalTime + 1, minDepTime, startVelocity);
			} else {
				return currentBlock.traverseBlock(currentTrain, arrivalTime+0.2,
						minDepTime, startVelocity);
			}
		}

		return new StatusTraverseBlock(false, minArrTime, currentTrain.trainNo);
	}

	/**
	 * @param arrivalTime
	 * @param startVelocity
	 * @return {@link StatusTraverseBlock}
	 */
	public StatusTraverseBlock traverseLastBlock(double arrivalTime,
			double startVelocity) {

		Debug
				.print("Last block scheduling complete in   BLOCKSCHEDULER.TRAVERSEBLOCK");
//		System.out.println("Compute last: startVelocity " + startVelocity
//				+ " train " + currentTrain.trainNo + " block "
//				+ currentBlock.blockNo + " startVelocity " + startVelocity);
		VelocityProfileArray velocityprofilearray = new VelocityProfileArray(
				currentTrain, currentBlock, startVelocity, 0.0D);

		Debug.print((new StringBuilder()).append(" Velocuit oia ").append(
				velocityprofilearray.runTime).toString());

		double departureTime = arrivalTime + velocityprofilearray.runTime;
		// System.out.println("traverseLastBlock blockNo " +
		// currentBlock.blockNo
		// + " arr " + arrivalTime + " dep " + departureTime);

		currentTrain.timeTables.add(0, new TimetableEntry(currentBlock.blockNo,
				arrivalTime, departureTime, currentBlock
						.getStartMilePost(currentTrain.direction), currentBlock
						.getEndMilePost(currentTrain.direction),
				velocityprofilearray));

		currentBlock.reserve(currentTrain.trainNo, arrivalTime, arrivalTime
				+ velocityprofilearray.runTime);
		GlobalVar.flag = false;
		return new StatusTraverseBlock(true, arrivalTime, currentTrain.trainNo);
	}

	/**
	 * @param arrivalTime
	 * @return {@link StatusTraverseBlock} with a false status and arrival and
	 *         departureTimes to be the earliestArrivalTime possible for the
	 *         currentBlock
	 */
	public StatusTraverseBlock handleRedSignal(double arrivalTime) {
		Debug.print("In BLOCKSCHEDULER.HANDLEREDSIGNAL");
		Debug.print("Red Signal - backtrack");
		double earliestArrivalTime = currentBlock.getEarliestArrivalTime(
				arrivalTime, currentTrain.direction)
				+ GlobalVar.minTime;

		// System.out.println("Earliest arrival Time " + earliestArrivalTime);
		return new StatusTraverseBlock(false, earliestArrivalTime,
				currentTrain.trainNo);
	}

	/**
	 * If there is a need to consider the warner distance, get the new
	 * {@link RunTimeReturn} for the modified block considering the warner
	 * distance
	 * 
	 * @param link
	 * @param signal
	 * @param arrivalTime
	 * @param linkPriorityIsNotOne
	 * @param runTimeReturn
	 * @return {@link RunTimeReturn} if the warner distance is considered. Else
	 *         returns null.
	 */
	public RunTimeReturn getRunTimeReturnConsideringWarner(Link link,
			int signal, double arrivalTime, boolean linkPriorityIsNotOne,
			RunTimeReturn runTimeReturn) {
		RunTimeReturn newRunTimeReturn = null;
		if (GlobalVar.simulationType.equalsIgnoreCase("SignalFailure")) {
			if (link.nextBlock.signalFailFlag == 0
					&& currentBlock.signalFailFlag == 0 && linkPriorityIsNotOne) {
				Debug
						.print(" From Blocksched.traversepath -->block.calculateWarnerVelocityProfile");
				newRunTimeReturn = currentBlock.calculateWarnerVelocityProfile(
						currentTrain, signal, arrivalTime, link.nextBlock,
						runTimeReturn, 0.0D);
			}
		} else if (linkPriorityIsNotOne) {
			Debug
					.print(" From Blocksched.traversepath -->block.calculateWarnerVelocityProfile");
//			System.out.println("link priority is not one " + link.nextBlockNo);
			newRunTimeReturn = currentBlock.calculateWarnerVelocityProfile(
					currentTrain, signal, arrivalTime, link.nextBlock,
					runTimeReturn, 0.0D);
		}
		return newRunTimeReturn;
	}

	/**
	 * Get the final velocity for the scheduling of the train. If the train can
	 * go with top priority link it may go at the fullest speed allowed.
	 * 
	 * @param link
	 * @return finalVelocity of the profile of the train to be calculated.
	 */
	public double calculateFinalVelocity(Link link, int signal) {
		if (signal < GlobalVar.numberOfColour )
			return 0.0D;

		if (link.isPriorityOneLink())
			return link.nextBlock.maximumPossibleSpeed;
		else
			return 0.0D;
	}

	private class BlockSchedulingParameters {
		Link link;
		double arrivalTime;
		double departureTime;
		double totalTimeTillEnd;
		double finalVelocity;
		double tempDepartureTime;
		int signal;
		int nFreeBlocksToBeTraversed;
		RunTimeReturn runTimeReturn;
	}

	public BlockSchedulingParameters determineBlockSchedulingParameters(
			Link link, double arrivalTime, int numberOfColour,
			double startVelocity, boolean linkPriorityIsNotOne,
			double departureTime) {

		BlockSchedulingParameters blockSchedulingParameters = new BlockSchedulingParameters();

		blockSchedulingParameters.arrivalTime = arrivalTime;

		double tempDepartureTime = departureTime;

		int signal = currentBlock.getSignal(currentTrain, numberOfColour,
				arrivalTime, currentTrain.direction);

		double finalVelocity = calculateFinalVelocity(link, signal);

		System.out.println("temp " + GlobalVar.temp + " signal " + signal
				+ " blockNo " + currentBlock.blockNo + " finalVelocity "
				+ finalVelocity);
		RunTimeReturn runTimeReturn = currentBlock
				.getRunTimeSignal(currentTrain, signal, arrivalTime,
						startVelocity, finalVelocity);

		RunTimeReturn considerWarnerRunTime = getRunTimeReturnConsideringWarner(
				link, signal, arrivalTime, linkPriorityIsNotOne, runTimeReturn);
		if (considerWarnerRunTime != null)
			runTimeReturn = considerWarnerRunTime;

		int nFreeBlocksToBeTraversed = 0;
		if (currentTrain.isScheduled && currentBlock.isLoop()) {

			String stationName = ((Loop) currentBlock).station.stationName;

			ReferenceTableEntry referenceTableEntry = currentTrain
					.getRefTabEntryFromStationName(stationName);
			double refArrivalTime = referenceTableEntry.refArrTime;
			double refDepartureTime = referenceTableEntry.refDepTime;

			if (refDepartureTime > refArrivalTime) {
				nFreeBlocksToBeTraversed = 1;
				runTimeReturn = currentBlock.getRunTimeSignal(currentTrain, 1,
						arrivalTime, startVelocity, 0.0D);
				tempDepartureTime += runTimeReturn.totalTime;
				// System.out.println("hi 1 " + tempDepartureTime);
			} else {
				// the train is not supposed to halt at the station :
				// Devendra
				nFreeBlocksToBeTraversed = signal;
				tempDepartureTime = arrivalTime + runTimeReturn.totalTime
						+ GlobalVar.minTime;
				// System.out.println("hi 2 " + tempDepartureTime + " "
				// + runTimeReturn.totalTime + " " + GlobalVar.minTime
				// + " " + arrivalTime);
			}

			// yet another important backTracking step. If the train can reach
			// earlier than the time it is asked to wait,that means some other
			// previously scheduled train is on its way block this train's path.
			// Devendra
		} else if (tempDepartureTime > arrivalTime + runTimeReturn.totalTime) {
//			System.out.println("tempDepartureTime > arrivalTime + runtimereturn.totalTime");
			nFreeBlocksToBeTraversed = 1;
			runTimeReturn = currentBlock.getRunTimeSignal(currentTrain, 1,
					arrivalTime, startVelocity, 0.0D);
		} else {
//			 System.out.println("I came here "+GlobalVar.temp+" times. "+GlobalVar.minTime);
			nFreeBlocksToBeTraversed = signal;
			tempDepartureTime = arrivalTime + runTimeReturn.totalTime;
			// + GlobalVar.minTime;

			// System.out.println("hi 3 " + tempDepartureTime);
		}

		tempDepartureTime = Math.max(tempDepartureTime, departureTime);

		blockSchedulingParameters.finalVelocity = finalVelocity;
		blockSchedulingParameters.signal = signal;
		blockSchedulingParameters.tempDepartureTime = tempDepartureTime;
		blockSchedulingParameters.runTimeReturn = runTimeReturn;
		blockSchedulingParameters.nFreeBlocksToBeTraversed = nFreeBlocksToBeTraversed;
		// System.out.println("Before returning tempDepartureTime "
		// + tempDepartureTime);
		return blockSchedulingParameters;
	}

	/**
	 * 1) It first finds what is the signal of the block for the train and the
	 * arrival time by calling currentBlock.getSignal(currentTrain, noOfColor,
	 * arrivalTime,currentTrain.direction); 2) It first tries to find the
	 * running time required to traverse the path, by going to
	 * currentBlock.getRunTimeSignal(currentTrain, j, arrivalTime,
	 * startVelocity, !linkPriorityIsOne ? link.nextBlock.maximumPossibleSpeed :
	 * 0.0D); and setting that value in runTimeReturn 3) If the simulation is in
	 * the signalFailureMode or if the linkPriority is one it resets the
	 * runTimeReturn to a block whose starting milePost is the same but the
	 * endMilePost is the actual endMilePost minus the warnerDistance. This it
	 * does by runtimereturn = currentBlock.calculateRecurrenceVelocity(
	 * currentTrain, signal, arrivalTime, link.nextBlock, runtimereturn, 0.0D);
	 * 
	 * Block: RunTimeReturn calculateRecurrenceVelocity(currentTrain, signal,
	 * arrivalTime, link.nextBlock, runtimereturn, 0.0D) 4) If the train is
	 * scheduled and the currentBlock is a loop, then it checks in the
	 * trainReferenceTable if the train has to halt at the station. It does so
	 * by checking against the entries of the train, the stationName of the
	 * loop. If the train has to halt, it signals a yellow so that the train
	 * will have to stop at the station. The departure time will be the
	 * startTime + runTimeReturn.totalTime, meaning that the departureTime is
	 * now set to the arrivalTime of the train in the block plus the time taken
	 * by the train to traverse the block which in this case is the loop. If the
	 * train does not have to halt, then it gives the train the required signal
	 * gotten from currentBlock.getSignal function. The departureTime is
	 * arrivalTime + runTimeReturn.totalTime + GlobalVar.minTime (which is the
	 * minimum time a train should take to traverse any block). If the train is
	 * not scheduled or the currentBlock is just a "block" is the same case as
	 * the case where the scheduled train does not have to halt. 5) It then
	 * marks the overlap region. The overlapStartDistance is the beginning of
	 * the block where the train hits first. The overlapEndDistance is the start
	 * of the block plus the length of the train plus some minimum distance. It
	 * sets the GlobalVar.overlap to be true. 6) Then it makes sure that the
	 * GlobalVar.overlapEndDistance fits between the distance of the last loop
	 * that the train will face. For scheduled trains it is the referenceTable's
	 * last station entry's preferredLoop and for the unscheduled train it is
	 * the train.endLoopNo. 7) Then it loops over block2 by updating it to the
	 * getNextBlock along the trainDirection and finds the last such block. If
	 * that lastBlock is a loop it checks the arrivalTime and departureTime of
	 * the train at the loop's station and accordingly sets the endVelocity of
	 * the train.
	 * 
	 * @param link
	 * @param arrivalTime
	 * @param departureTime
	 * @param startVelocity
	 * @param linkPriorityIsNotOne
	 * @param numberOfColour
	 * @return {@link StatusTraverseBlock}
	 */

	public StatusTraverseBlock traversePath(Link link, double arrivalTime,
			double departureTime, double startVelocity,
			boolean linkPriorityIsNotOne, int numberOfColour) {

		double tempDepartureTime = departureTime;

		Debug.print(" In BlockScheduler.traversePath ");
//		System.out.println("Before going in tempDepartureTime "
//				+ tempDepartureTime + " arrivalTime " + arrivalTime
//				+ " noOfColor " + numberOfColour);
		BlockSchedulingParameters blockSchedulingParameters = determineBlockSchedulingParameters(
				link, arrivalTime, numberOfColour, startVelocity,
				linkPriorityIsNotOne, departureTime);

		int signal = blockSchedulingParameters.signal;
		int nFreeBlocksToBeTraversed = blockSchedulingParameters.nFreeBlocksToBeTraversed;
		RunTimeReturn runTimeReturn = blockSchedulingParameters.runTimeReturn;
		tempDepartureTime = blockSchedulingParameters.tempDepartureTime;
//		System.out.println("After returning tempDepartureTime "
//				+ tempDepartureTime);

		Block nextBlock = currentBlock.getNextBlock(currentTrain.direction,
				currentTrain);

		setOverlapParameters();
		GlobalVar.overlap = true;

		// System.out.println("calculateTotalTimeTillEnd: rTR time "
		// + runTimeReturn.totalTime + " rTR velocity "
		// + runTimeReturn.endVelocity + " arrivalTime " + arrivalTime
		// + " overlap " + GlobalVar.overlap);
		// time to cover overlapping part
		double totalTimeTillEnd = calculateTotalTimeTillEnd(runTimeReturn,
				nextBlock, arrivalTime);
		GlobalVar.overlap = false;

		// GlobalVar.sudhir[currentBlock.blockNo] = totalTimeTillEnd * 60D;

		// System.out.println("Arrival Time " + arrivalTime + " departure time "
		// + tempDepartureTime);

		StatusTraverseBlock earliestFreeTimeSTP = computeEarliestFreeTimeSTP(
				link, nFreeBlocksToBeTraversed, arrivalTime, tempDepartureTime,
				totalTimeTillEnd, startVelocity, linkPriorityIsNotOne);
		if (earliestFreeTimeSTP != null)
			return earliestFreeTimeSTP;

		StatusTraverseBlock signalFailureSTP = handleSignalFailure(link,
				tempDepartureTime);
		if (signalFailureSTP != null)
			return signalFailureSTP;

		Debug
				.print("---------------------------------going to next block----------------------------"
						+ "-------");

		// recursion terminating conditions or the base cases - Devendra
		GlobalVar.flag = isNextBlockLastBlock();

		// System.out.println("calling RBAGCS: tempDepTime " + tempDepartureTime
		// + " arrivalTime " + arrivalTime + " totalTimeTillEnd "
		// + totalTimeTillEnd + " runTimeReturn "
		// + runTimeReturn.totalTime);
		// // if next block scheduling is successful reserve blocks and return
		return reserveBlocksAndGetCurrentSTP(link, tempDepartureTime,
				runTimeReturn, nFreeBlocksToBeTraversed, arrivalTime,
				totalTimeTillEnd, signal);
	}

	/**
	 * Calculate the time since the head of the train leaves the currentBlock
	 * till the tail of the train leaves currentBlock.
	 * 
	 * @param runTimeReturn
	 * @param nextBlock
	 * @param arrivalTime
	 * @return time between the exiting time instances of the head and tail of
	 *         the currentTrain from currentBlock
	 */
	public double calculateTotalTimeTillEnd(RunTimeReturn runTimeReturn,
			Block nextBlock, double arrivalTime) {
		Block blockIter = nextBlock;
		LastOverlapBlock lastOverlapBlock = getLastOverlapBlock(blockIter);
		int numberOfLastBlockFromCurrentBlock = lastOverlapBlock.numberOfBlockFromCurrentBlock;
		Block lastBlock = lastOverlapBlock.block;

		double maxSpeedOfTrain = calculateMaxSpeedOfTrain(lastBlock);

		double runTimeReturnEndVelocity = runTimeReturn.endVelocity;
		RunTimeReturn runtimereturn1 = null;

		// j1 is the number of blocks after the currentBlock
		Debug.print("block1 " + nextBlock.blockNo + " j1 "
				+ numberOfLastBlockFromCurrentBlock);

		// System.out.println("Calling gRTS: arrivalTime " + arrivalTime
		// + " velocity " + runTimeReturnEndVelocity + " speed "
		// + maxSpeedOfTrain + " nBlocks "
		// + numberOfLastBlockFromCurrentBlock);
		runtimereturn1 = nextBlock.getRunTimeSignal(currentTrain,
				numberOfLastBlockFromCurrentBlock, arrivalTime,
				runTimeReturnEndVelocity, maxSpeedOfTrain);

		double totalTimeTillEnd = runtimereturn1.totalTime;

		// totalTimeTillEnd += GlobalVar.blockWorkingTime;
		return totalTimeTillEnd;
	}

	/**
	 * @return true if the next block to be traversed is the last one. Else
	 *         return false.
	 */
	public boolean isNextBlockLastBlock() {

		int loopNo = currentTrain.getLastLoopNo();
		if (currentTrain.isScheduled) {

			int nextBlockNo = currentBlock.getNextBlock(currentTrain.direction,
					currentTrain).blockNo;
			flag = false;
			if (nextBlockNo == loopNo)
				return true;

		} else {// (!currentTrain.isScheduled)
			return currentBlock.checkLastBlock(currentTrain.direction, loopNo);
		}
		return false;
	}

	/**
	 * Get the {@link StatusTraverseBlock} for the current block if the
	 * scheduling for the next block is successful. In that case, the
	 * corresponding blocks should also be reserved. If the scheduling for next
	 * block is unsuccessful, it should return a {@link StatusTraverseBlock}
	 * with false status.
	 * 
	 * @param link
	 * @param tempDepartureTime
	 * @param runtimereturn
	 * @param nFreeBlocksToBeTraversed
	 * @param arrivalTime
	 * @param totalTimeTillEnd
	 * @param signal
	 * @return {@link StatusTraverseBlock} with true or false status depending
	 *         upon the successful scheduling of train on next block.
	 */
	public StatusTraverseBlock reserveBlocksAndGetCurrentSTP(Link link,
			double tempDepartureTime, RunTimeReturn runtimereturn,
			int nFreeBlocksToBeTraversed, double arrivalTime,
			double totalTimeTillEnd, int signal) {

		StatusTraverseBlock nextStatustraverseblock = link.nextBlock
				.traverseBlock(currentTrain, tempDepartureTime,
						tempDepartureTime, runtimereturn.endVelocity);

		if (nextStatustraverseblock.status) {

			// System.out.println("blockNo + " + currentBlock.blockNo
			// + " nFreeBlocksToBeTraversed " + nFreeBlocksToBeTraversed);
			reserveBlocks(link, nFreeBlocksToBeTraversed, arrivalTime,
					tempDepartureTime, totalTimeTillEnd,
					runtimereturn.velocityProfileArray, signal);
			return new StatusTraverseBlock(true, arrivalTime,
					currentTrain.trainNo);
		}

		Debug.print("n  FALSE  case");

		return new StatusTraversePath(false,
				nextStatustraverseblock.departureTime - runtimereturn.totalTime // the
						// original
						// arrival
						// time
						- GlobalVar.minTime, // the original departure time
				nextStatustraverseblock.departureTime, -1);
	}

	/**
	 * @param link
	 * @param departureTime
	 * @return {@link StatusTraverseBlock} with signal -2, suggesting signal
	 *         failure. returns null if there is no signal failure.
	 */
	public StatusTraverseBlock handleSignalFailure(Link link,
			double departureTime) {
		if (/*
			 * (link.nextBlock.isSignalFailed(departureTime) == 1 ||
			 * link.nextBlock .signalFailed(tempDepartureTime) == 2)
			 */
		link.nextBlock.isSignalFailed(departureTime)
				&& link.nextBlock.signalFailFlag == 0) {

			if (departureTime % 1400D > 1140D || departureTime % 1400D < 420D) {
				link.nextBlock.signalFailFlag = 2;
			} else {
				link.nextBlock.signalFailFlag = 1;
			}

			currentTrain.signalFailCounter++;
			currentTrain.signalFailedBlocks[currentTrain.signalFailCounter] = link.nextBlock.blockNo;
			return new StatusTraversePath(false, 0.0D, 0.0D, -2);
		}
		return null;
	}

	/**
	 * @param link
	 * @param nFreeBlocksToBeTraversed
	 * @param arrivalTime
	 * @param departureTime
	 * @param totalTimeTillEnd
	 * @param startVelocity
	 * @param linkPriorityIsNotOne
	 * @return If any block till the lastOverlapping Block is not free then
	 *         "traversePath" the train only until that particular block, else
	 *         if the currentBlock is not free retry with the
	 *         earliestArrivalTime of the currentBlock. If neither of the above
	 *         situations is reached,return null.
	 */
	public StatusTraverseBlock computeEarliestFreeTimeSTP(Link link,
			int nFreeBlocksToBeTraversed, double arrivalTime,
			double departureTime, double totalTimeTillEnd,
			double startVelocity, boolean linkPriorityIsNotOne) {

		Block blockIter = currentBlock;
		for (int j2 = 0; j2 < nFreeBlocksToBeTraversed; j2++) {
			double earliestFreeTime;
			if (j2 == 0) {
				earliestFreeTime = blockIter.whenFree(arrivalTime,
						departureTime + totalTimeTillEnd);

				if (earliestFreeTime == -1D) {
					earliestFreeTime = link.whenFree(departureTime,
							departureTime + totalTimeTillEnd);
				}
			} else {
				earliestFreeTime = blockIter.whenFree(arrivalTime,
						departureTime + totalTimeTillEnd);

			}

			if (earliestFreeTime != -1D) {
				earliestFreeTime += GlobalVar.minTime;

				// System.out.println();
				// System.out.println("minTime "+GlobalVar.minTime);
				// System.out.println();

				if (j2 != 0) {
					Debug.print((new StringBuilder()).append(
							"Reducing Signal to ").append(j2).toString());
					return traversePath(link, arrivalTime, departureTime,
							startVelocity, linkPriorityIsNotOne, j2 + 1);
				}
				Debug.print("block reservation failure. So returning false ");

				return new StatusTraversePath(false, earliestFreeTime,
						earliestFreeTime, 0);
			}
			blockIter = blockIter.getNextBlock(currentTrain.direction,
					currentTrain);
		}
		return null;
	}

	/**
	 * @author devendra This class is to determine the last block which the
	 *         currentTrain will overlap with while just exiting the
	 *         currentBlock.
	 */
	private class LastOverlapBlock {
		/**
		 * numberOfBlockFromCurrentBlock
		 */
		int numberOfBlockFromCurrentBlock;
		/**
		 * block
		 */
		Block block;

		/**
		 * @param count
		 * @param lastBlock
		 */
		public LastOverlapBlock(int count, Block lastBlock) {
			numberOfBlockFromCurrentBlock = count;
			block = lastBlock;
		}
	}

	/**
	 * @param blockIter
	 * @return {@link LastOverlapBlock} such that the last block from the
	 *         currentBlock which the currentTrain will overlap while just
	 *         exiting the currentBlock
	 */
	public LastOverlapBlock getLastOverlapBlock(Block blockIter) {
		int count = 1;
		do {
			if (hasReachedOverlapEndDistance(blockIter)) {
				break;
			}
			count++;

			blockIter = blockIter.getNextBlock(currentTrain.direction,
					currentTrain);
		} while (blockIter != null);
		return new LastOverlapBlock(count, blockIter);
	}

	/**
	 * Get the maximum speed of the train for the block it would overlap into
	 * while just exiting the currentBlock.
	 * 
	 * @param block
	 * @return calculate maximum speed of the train for the last block it would
	 *         overlap onto while exiting the currentBlock.
	 */
	public double calculateMaxSpeedOfTrain(Block block) {
		double maxSpeedOfTrain = Math.max(currentTrain.maximumPossibleSpeed,
				block.maximumPossibleSpeed);

		if (currentTrain.isScheduled && block.isLoop()) {

			String stationName = ((Loop) block).station.stationName;

			ReferenceTableEntry referenceTableEntry = currentTrain
					.getRefTabEntryFromStationName(stationName);
			if (referenceTableEntry != null) {
				double refArrTime = referenceTableEntry.refArrTime;
				double refDepTime = referenceTableEntry.refDepTime;
				if (refDepTime > refArrTime) {
					maxSpeedOfTrain = 0.0D;
				}
			}
		}
		return maxSpeedOfTrain;
	}

	/**
	 * Determine if the block depending upon the train's direction covers the
	 * Global.overlapEndDistance
	 * 
	 * @param block
	 * @return true if the block covers the Global overlapEndDistance
	 */
	public boolean hasReachedOverlapEndDistance(Block block) {
		if (currentTrain.direction == 0) {
			return (block.getEndMilePost(currentTrain.direction) > GlobalVar.overlapEndDistance);
		} else {// currentTrain direction is down
			return (block.getEndMilePost(currentTrain.direction) < GlobalVar.overlapEndDistance);
		}
	}

	/**
	 * Set the overlapStartDistance, overlapEndDistance and overlap boolean
	 * depending upon the block and the train.
	 */
	public void setOverlapParameters() {

		Block nextBlock = currentBlock.getNextBlock(currentTrain.direction,
				currentTrain);
		Block block2 = nextBlock;
		double d4 = 0.12D;
		GlobalVar.overlapStartDistance = nextBlock
				.getStartMilePost(currentTrain.direction);

		if (currentTrain.direction == 0) {
			GlobalVar.overlapEndDistance = nextBlock
					.getStartMilePost(currentTrain.direction)
					+ d4 + currentTrain.length;
		}
		if (currentTrain.direction == 1) {
			GlobalVar.overlapEndDistance = nextBlock
					.getStartMilePost(currentTrain.direction)
					- d4 - currentTrain.length;
		}

		Debug.print((new StringBuilder()).append("overlapStartDistance ")
				.append(GlobalVar.overlapStartDistance).toString());
		Debug.print((new StringBuilder()).append("overlapEndDistance ").append(
				GlobalVar.overlapEndDistance).toString());

		// I had made this a comment. I want to be sure now what this overlap is
		// really doing.
		// GlobalVar.overlap = true;

		Debug.print((new StringBuilder()).append("overlapEndD ").append(
				block2.getEndMilePost(currentTrain.direction)).toString());

		if (currentTrain.isScheduled) {
			int k1 = currentTrain.refTables.size();
			int l1 = currentTrain.refTables.get(k1 - 1).refLoopNo;

			if (currentTrain.direction == 0
					&& GlobalVar.overlapEndDistance >= GlobalVar
							.getLastEndmilepost(l1, currentTrain.direction)) {
				GlobalVar.overlapEndDistance = GlobalVar.getLastEndmilepost(l1,
						currentTrain.direction) - 0.01D;
			}
			if (currentTrain.direction == 1
					&& GlobalVar.overlapEndDistance <= GlobalVar
							.getLastEndmilepost(l1, currentTrain.direction)) {
				GlobalVar.overlapEndDistance = GlobalVar.getLastEndmilepost(l1,
						currentTrain.direction) + 0.01D;
			}
		}

		if (!currentTrain.isScheduled) {
			if (currentTrain.direction == 0
					&& GlobalVar.overlapEndDistance >= GlobalVar
							.getLastEndmilepost(currentTrain.endLoopNo,
									currentTrain.direction)) {
				GlobalVar.overlapEndDistance = GlobalVar.getLastEndmilepost(
						currentTrain.endLoopNo, currentTrain.direction) - 0.01D;
			}
			if (currentTrain.direction == 1
					&& GlobalVar.overlapEndDistance <= GlobalVar
							.getLastEndmilepost(currentTrain.endLoopNo,
									currentTrain.direction)) {

				GlobalVar.overlapEndDistance = GlobalVar.getLastEndmilepost(
						currentTrain.endLoopNo, currentTrain.direction) + 0.01D;
			}
		}

	}

	/**
	 * @param link
	 * @param i
	 * @param arrivalTime
	 * @param departureTime
	 * @param totalTimeForRestOfPath
	 * @param velocityProfileArray
	 * @param signal
	 */

	public void reserveBlocks(Link link, int i, double arrivalTime,
			double departureTime, double totalTimeForRestOfPath,
			VelocityProfileArray velocityProfileArray, int signal) {

		// System.out.println("reserveBlocks blockNo " + currentBlock.blockNo
		// + " arr " + arrivalTime + " dep " + departureTime + " tot "
		// + totalTimeForRestOfPath);

		Debug.print(" In Blockscheduler.RESERVEBLOCKS ");
		Block block = currentBlock;
		for (int k = 0; k < i; k++) {
			Debug.print((new StringBuilder()).append("Reserving for block ")
					.append(block.blockName).toString());
			if (k == 0) {
				block.reserve(currentTrain.trainNo, arrivalTime, departureTime
						+ totalTimeForRestOfPath);

				// block.reserve(currentTrain.trainNo, arrivalTime,
				// departureTime);
				link.reserve(currentTrain.trainNo, departureTime,
						totalTimeForRestOfPath);
			} else {

				// was this commenting necessary or not
				// ??????????????????????????
				block.setOccupied(currentTrain.trainNo, arrivalTime,
						departureTime + totalTimeForRestOfPath);
			}
			block = block.getNextBlock(currentTrain.direction, currentTrain);
		}

		Debug.print("block scheduling complete");
		if (currentBlock.signalFailFlag != 0) {
			currentTrain.timeTables.add(0, new TimetableEntry(
					currentBlock.blockNo, arrivalTime, departureTime,
					currentBlock.getStartMilePost(currentTrain.direction),
					currentBlock.getEndMilePost(currentTrain.direction),
					velocityProfileArray, 6));
		} else {
			currentTrain.timeTables.add(0, new TimetableEntry(
					currentBlock.blockNo, arrivalTime, departureTime,
					currentBlock.getStartMilePost(currentTrain.direction),
					currentBlock.getEndMilePost(currentTrain.direction),
					velocityProfileArray, signal));
		}
	}
}

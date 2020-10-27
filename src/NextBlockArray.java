import java.util.ArrayList;

import util.Debug;

/**
 * NextBlockArray
 */
class NextBlockArray extends ArrayList {

	/**
	 * NextBLockArray: ArrayList<Link> getSortedLinks(Train train, Block block,
	 * double arrivalTime, double deptTime, double startVelocity) 1) The base
	 * cases as in the nextBlockArray has size 0 or 1 it should return the
	 * linkArray or add the only nextBlock and return. 2) It checks if the
	 * current block is actually a "block" and if the train is scheduled. Then
	 * it checks if the next block should be a loop then finds the station name,
	 * refers the referenceTable, finds the preferred loop, adds that first,
	 * then adds the rest of the loops which do not conflict with the train's
	 * direction. 3) Else if the currentBlock is a "loop" or the train is
	 * unscheduled one, it adds those links which has a path till the endLoop
	 * for the train. Block: boolean hasPath(Block block, int trainEndLoopNo,
	 * int trainDirection) 1) It iterates through the all the links and ignores
	 * those which are null, while returns true if one of them is the endLoop 2)
	 * OtherWise it takes that link's nextBlock for recursion and finds if there
	 * exists a path.
	 * 
	 * 
	 * @param train
	 * @param block
	 * @param arrivalTime
	 * @param deptTime
	 * @param startVelocity
	 * @return list of links.
	 */
	public ArrayList<Link> getSortedLinks(Train train, Block block,
			double arrivalTime, double deptTime, double startVelocity) {

//		int direction = train.direction; // KJD
//		String className = (block.getClass()).getName();
		// System.out.println("NextBlockArray: getSortedLinks: Train = " +
		// train.trainNo
		// + ": Block = " + block.blockNo + ": ClassName = " + className);

		int preferredLoop = -1;
		String nextStnName = "NULL";
		Block blk;
		ArrayList<Link> linkArray = new ArrayList<Link>();

		Debug.print("NextBlockArray: getSortedLinks: next Block array size "
				+ size());
		if (0 == size()) {
			Debug
					.print("NextBlockArray: getSortedLinks: nextBlockArraySize is 0");
			return linkArray;
		}

		if (1 == size()) {
			linkArray.add((Link) get(0));
			return linkArray;
		}

		Debug.print("NextBlockArray: getSortedLinks: size = " + size());

		// for scheduled trains
		if ((true == train.isScheduled)) {

			if (!block.isLoop()) {
				if (block.getNextBlock(train.direction).isLoop()) {
					nextStnName = (((Loop) (((Link) get(0)).nextBlock)).station).stationName;

					int refTableSize = (train.refTables).size();
					for (int index = 0; index < refTableSize; index++) {
						if (((train.refTables).get(index)).stationName == nextStnName) {
							preferredLoop = ((train.refTables).get(index)).refLoopNo;
						}
					}

					int preferredLinkNo = -1;
					for (int linkNo = 0; linkNo < size(); linkNo++) {
						Link thisLink = (Link) get(linkNo);
						blk = thisLink.nextBlock;

						// Devendra
						if (blk.isDirectionOk(train, arrivalTime)) {
							// if preferred link, note it to be added later
							if (blk.blockNo == preferredLoop) {
								// Devendra

								preferredLinkNo = linkNo;
							}
							// not the preferred link
							else {
								// Devendra
								// insertion sort
								Link.addLinkPriorityWise(linkArray, thisLink);
							}
						}
					}

					// adding the preferredLink if any in the front of linkArray
					if (preferredLinkNo != -1) {
						Link preferredLink = (Link) get(preferredLinkNo);
						linkArray.add(0, preferredLink);
					}
					return linkArray;
				} else { // currentBlock is block and nextBlock is also a block
					for (int i = 0; i < this.size(); i++) {
						Link link = (Link) this.get(i);
						Link.addLinkPriorityWise(linkArray, link);

					}
					linkArray.add((Link) this.get(0));
					return linkArray;
				}
			} else {// current block is a loop
				int nextLoopNo = ((Loop) block)
						.getNextReferenceLoopNumber(train.refTables);
				for (int i = 0; i < this.size(); i++) {
					Link link = (Link) this.get(i);
					if (link.nextBlock.hasPath(link.nextBlock, nextLoopNo,
							train.direction)) {
						Link.addLinkPriorityWise(linkArray, link);
					}
				}
				return linkArray;
			}

		}

		// for unscheduled trains
		Debug
				.print("NextBlockArray: getSortedLinks: the block "
						+ block.blockNo
						+ " is actually a loop at the station or the train is unscheduled");

		preferredLoop = train.endLoopNo;
		Debug.print("preferredLoop: " + preferredLoop);
		ArrayList<Link> linkSetHavingPath = new ArrayList<Link>();
		ArrayList<Link> linkSetNotHavingPath = new ArrayList<Link>();

		for (int i = 0; i < size(); i++) {

			Debug.print("NextBlockArray: getSortedLinks: inside for loop");

			Debug
					.print(("Block No. HasPath" + (((Link) get(i)).nextBlock).blockNo));

			Debug.print(((((Link) get(i)).nextBlock).getClass()).getName());

			Link thisLink = (Link) get(i);
			if (((thisLink.nextBlock).getClass()).getName() == "Block") {

				Debug.print("Train End Loop-->" + train.endLoopNo);

				if (thisLink.nextBlock.hasPathMemoizationMethod(
						thisLink.nextBlock, train.endLoopNo, train.direction)) {

					Debug.print("HasPath");

					// insertion sort for links having path
					Link.addLinkPriorityWise(linkSetHavingPath, thisLink);
				} else {// if no path exists from this link then simply add this
					// insertion sort for links not having path
					Link.addLinkPriorityWise(linkSetNotHavingPath, thisLink);
				}
			} else {// we are considering links to a loop

				if (thisLink.nextBlock.hasPathMemoizationMethod(
						thisLink.nextBlock, train.endLoopNo, train.direction)) {

					// Devendra uptil above
					if (((Loop) thisLink.nextBlock).whetherMainLine == 1) {

						Debug
								.print("NextBlockArray: getSortedLinks: whetherMainLine==1");
						linkArray.add(0, thisLink);
					} else {
						// insertion sort for links having path
						Link
								.addLinkPriorityWise(linkSetNotHavingPath,
										thisLink);
					}
				} else {

					if (((Loop) thisLink.nextBlock).whetherMainLine == 1) {
						Debug
								.print("NextBlockArray: getSortedLinks: whetherMainLine==1");
						linkArray.add(0, thisLink);
					} else {
						// insertion sort for links not having path
						Link
								.addLinkPriorityWise(linkSetNotHavingPath,
										thisLink);

					}
				}
			}
		}

		linkArray.addAll(linkSetHavingPath);
		linkArray.addAll(linkSetNotHavingPath);

		Debug.print("NextBlockArray: getSortedLinks: end");
		return linkArray;
	}

	/**
	 * @return MainLink
	 */
	public Link getMainLink() {
		for (int i = 0; i < size(); i++) {
			if (((Link) get(i)).priority == 1) {
				return (Link) get(i);
			}
		}
		return null;
	}

	/**
	 * @return next main block.
	 */
	public Block getNextMainBlock() {
		Debug.print("NextBlockArray: getNextMainBlock:");

		Block b = new Block();
		b = null;

		for (int i = 0; i < size(); i++) {
			// if(trType==true) return b = ((Link)get(i)).nextBlock;
			if (((Link) get(i)).priority == 1) {
				b = ((Link) get(i)).nextBlock;
//				int bl = ((Link) get(i)).nextBlock.blockNo;

			}
		}
		return b;
	}

}

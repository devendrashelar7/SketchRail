/**
 * Title:        Simulation of Train Pathing
 * Description:  This class stores the link from one block to another. It also contains the
 * length and blocks that need to be crossed over by the link.
 * Copyright:    Copyright (c) 2002
 * Company:      IIT
 * @author Rajesh Naik
 * @version 1.0
 */
import java.util.*;
import util.*;

/**
 * link
 */
public class Link {
	/**
	 * nextBlockNo
	 */
	int nextBlockNo;
	/**
	 * nextBlock
	 */
	Block nextBlock = null;
	/**
	 * length
	 */
	double length;
	/**
	 * priority
	 */
	int priority;
	/**
	 * crossovers
	 */
	ArrayList<Block> crossovers = new ArrayList<Block>();

	/**
	 * @param dir
	 * @param blkNos
	 * @param linkLength
	 * @param strPriority
	 * @param strCrossOver
	 * @param hbEntry
	 */
	public static void buildLink(int dir, String blkNos, String linkLength,
			String strPriority, String strCrossOver, HashBlockEntry hbEntry) {

		// boolean done=false;
		StringTokenizer strTkBlkNos, strTkLinkLength, strTkCrossover, strTkPriority;

//		System.out.println(blkNos + "\t" + linkLength + "\t" + strPriority
//				+ "\t" + strCrossOver);

		strTkBlkNos = new StringTokenizer(blkNos, ",");
		strTkLinkLength = new StringTokenizer(linkLength, ",");
		strTkPriority = new StringTokenizer(strPriority, ",");
		strTkCrossover = new StringTokenizer(strCrossOver, ",");

		int blkNumber, linkPriority, intCrossover;
		double len;
		Link newLink;

		Debug.print("buildLink: before reading block numbers");
		while (strTkBlkNos.hasMoreTokens()) {

			Debug
					.print("buildLink: inside while loop for reading block numbers");
			String str = strTkBlkNos.nextToken();
			Debug.print("buildLink: value read is " + str);

			if (str.equalsIgnoreCase("#")) {
				Debug.print("buildLink: value read is hash");
				continue;
			}

			Debug.print("buildLink: value read is not hash");

			blkNumber = Integer.parseInt(str);
			Debug.print("buildLink: reading block number = " + blkNumber);

			len = Double.parseDouble(strTkLinkLength.nextToken());
			Debug.print("buildLink: reading link length = " + len);

			linkPriority = Integer.parseInt(strTkPriority.nextToken());
			Debug.print("buildLink: reading link priority = " + linkPriority);

			newLink = new Link(blkNumber, len, linkPriority);
			Debug.print("buildLink: created a new link");

			str = strTkCrossover.nextToken();
			Debug.print("buildLink: reading the crossover");

			if (!str.equalsIgnoreCase("#")) {

				Debug.print("buildLink: crossover read is not hash");

				intCrossover = Integer.parseInt(str);
				newLink.crossovers.add(new Block(new Integer(intCrossover)));
			}

			Debug.print("buildLink: after reading crossover");

			if (dir == 0) {
				Debug.print("buildLink: direction of the link is UP");
				hbEntry.upLinks.add(newLink);
			} else {
				Debug.print("buildLink: direction of the link is DOWN");
				hbEntry.downLinks.add(newLink);
			}
		}
	}

	/**
	 * @param no
	 * @param length
	 */
	public Link(int no, double length) {
		nextBlockNo = no;
		this.length = length;
	}

	/**
	 * @param no
	 * @param length
	 * @param priority
	 */
	public Link(int no, double length, int priority) {
		this.priority = priority;
		nextBlockNo = no;
		this.length = length;
	}

	/**
	 * @param blk
	 * @param length
	 * @param priority
	 */
	public Link(Block blk, double length, int priority) {
		this.priority = priority;
		nextBlock = blk;
		this.length = length;
	}

	/**
	 * @param blk
	 * @param length
	 */
	public Link(Block blk, double length) {
		nextBlock = blk;
		this.length = length;
	}

	/**
	 * This will check if all the crossovers are free during the specified
	 * interval. It will return -1 if all crossovers are free or else it will
	 * return the earliest arrival time so that the train can pass over this
	 * link
	 * 
	 * @param startTime
	 * @param interBlockTime
	 * @return time when link is free.
	 */
	public double whenFree(double startTime, double interBlockTime) {
		int no;
		Debug.print("Crossovers size " + crossovers.size());
		for (int i = 0; i < crossovers.size(); i++) {
			if ((no = crossovers.get(i).isFree(startTime,
					startTime + interBlockTime)) >= 0) {
				return crossovers.get(i).occupy.get(no).endTime;
			}
		}
		return -1;
	}

	/**
	 * @param trainNo
	 * @param startTime
	 * @param interBlockTime
	 */
	public void reserve(int trainNo, double startTime, double interBlockTime) {

		// nextBlock.setOccupied(trainNo, startTime, startTime +
		// interBlockTime);
		for (int i = 0; i < crossovers.size(); i++) {
			crossovers.get(i).setOccupied(trainNo, startTime,
					startTime + interBlockTime);
		}
	}

	/**
     * 
     */
	public void convert() {

		Debug.print("Link: convert: ");
		Debug.print("Link: convert: new link to block " + nextBlockNo);

		HashBlockEntry hashBlockLinkEntry = (GlobalVar.hashBlock
				.get(new Integer(nextBlockNo)));

		if (hashBlockLinkEntry != null) {

			nextBlock = hashBlockLinkEntry.block;
		} else {

			Debug.print("Link: convert: hashBlockLinkEntry is null");
			Debug.print("Link: convert: Error: block not present ");
		}

		for (int i = 0; i < crossovers.size(); i++) {

			Integer noInt = (crossovers.get(i).blockNo);
			hashBlockLinkEntry = (GlobalVar.hashBlock.get((noInt)));

			if (hashBlockLinkEntry != null) {
				crossovers.set(i, hashBlockLinkEntry.block);
				Debug.print("Link: convert: added " + noInt.toString());
			} else {
				Debug.print("Link: convert: Error: block not present "
						+ crossovers.get(i));
				// System.exit(0);
			}
		}
	}

	/**
	 * Insert the link into a linkList sorted according to its priority
	 * 
	 * @param linkList
	 * @param link
	 */
	public static void addLinkPriorityWise(ArrayList<Link> linkList, Link link) {
		if (linkList == null)
			throw new IllegalArgumentException(
					"addPriorityWise: linkList is null");

		int linkListSize = linkList.size();
		boolean isLinkAdded = false;

		// sorted insertion
		for (int i = 0; i < linkListSize; i++) {
			if (link.priority < linkList.get(i).priority) {
				linkList.add(i, link);
				isLinkAdded = true;
				break;
			}
		}
		// if link is not added meaning it has the highest priority and should
		// be added last
		if (isLinkAdded == false) {
			linkList.add(linkListSize, link);
		}
	}

	/**
	 * @return true if the priority of the link is one
	 */
	public boolean isPriorityOneLink() {
		return (this.priority == 1);
	}
}
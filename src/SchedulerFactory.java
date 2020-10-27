/**
 * Title: Simulation of Train Pathing Description: Copyright: Copyright (c) 2002
 * Company: IIT
 * 
 * @author
 * @version 1.0
 */

public class SchedulerFactory {

    /**
     * constructor
     */
    public SchedulerFactory() {
    }

    /**
     * Get the Block scheduler.
     * @param type
     * @param block 
     * @param train
     * @return {@link BlockScheduler}
     */
    public BlockScheduler getScheduler(String type, Block block, Train train) {
	/*
	 * if(type.equalsIgnoreCase("SignalFailure")){ return (new
	 * SignalFailureScheduler(block,train)); }
	 */
	return (new BlockScheduler(block, train));
    }
}
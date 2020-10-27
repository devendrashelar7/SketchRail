

/**
 * Title:        Simulation of Train Pathing
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IIT
 * @author Rajesh Naik
 * @version 1.0
 */

public class RunTimeReturn{
  /**
 * totalTime
 */
double totalTime;
  /**
 * endVelocity
 */
double endVelocity;
  /**
 * velocityProfileArray
 */
VelocityProfileArray velocityProfileArray;

  /**
 * @param time
 * @param velo
 * @param veloProfArray
 */
	public RunTimeReturn(double time,double velo,VelocityProfileArray veloProfArray){
    totalTime=time;
    endVelocity=velo;
    velocityProfileArray=veloProfArray;
  }
}
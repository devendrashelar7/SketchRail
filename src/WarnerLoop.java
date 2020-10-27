import java.io.*;

/**
 * 
 */
public class WarnerLoop extends Loop{

  /**
 * 
 */
public WarnerLoop() { }

  /**
 * @param st
 * @throws IOException
 * @throws SimException
 */
public WarnerLoop(StreamTokenizer st)throws IOException,SimException {
    super(st);
  }

  /*what this returns is not the signal(red , yello, green)
   that is seen by the train but the no of blocks ahead that are free...*/

  /** (non-Javadoc)
 * @see Block#getSignal(Train, int, double, int)
 */
public int getSignal(Train currTrain,int noOfColor,double time,int dir){

    if (this.isFree(time)==-1)
    {
      if (getNextBlock(dir,currTrain)==null)
      {
        return 0;
      }
      int sig = (getNextBlock(dir,currTrain).getSignal(currTrain,noOfColor,time,dir));
      if(sig>0)
      {
        sig++;
      }
      return(sig);
    }
    return(0);
  }
}
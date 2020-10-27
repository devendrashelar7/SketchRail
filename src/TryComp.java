
import java.util.*;
/**
 * 
 */
public class TryComp implements Comparator {
  /** (non-Javadoc)
 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
 */
public int  compare(Object a, Object b){
    double  c ,d;
    int var = 0; Interval a1, b1 ;
    a1 = (Interval) a ; b1 = (Interval) b ;
    c = a1.startTime ; d = b1.startTime ;
    if (c == d ) { var=0 ;} if (c > d) {var =1 ; }
    if (c < d) {var =  -1 ; }
    return var ;
  }
}
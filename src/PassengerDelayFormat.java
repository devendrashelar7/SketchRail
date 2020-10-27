/**
 * Class {@link PassengerDelayFormat}
 * Format for the passenger delay
 */
public class PassengerDelayFormat {
    /**
     * trainPriority
     */
    int trainPriority;
    /**
     * percentDelay
     */
    float percentDelay;
    /**
     * meanDelay
     */
    double meanDelay;

    /**
     * constructor.
     * 
     * @param tp trainPriority
     * @param pD percentDelay
     * @param mD meanDelay
     */
    public PassengerDelayFormat(int tp, float pD, double mD) {
	this.trainPriority = tp;
	this.percentDelay = pD;
	this.meanDelay = mD;

    }
}
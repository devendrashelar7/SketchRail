import java.util.*;

/**
 * HashBlockEntry
 */
public class HashBlockEntry {
    /**
     * block
     */
    Block block;
    /**
     * upLinks
     */
    ArrayList<Link> upLinks = new ArrayList<Link>();
    /**
     * downLinks
     */
    ArrayList<Link> downLinks = new ArrayList<Link>();
}
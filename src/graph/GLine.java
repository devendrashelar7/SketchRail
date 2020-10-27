package graph;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import util.*;

/**
 * This class provides functionality to draw straight lines in the graph. one
 * can shift the whole line along the x and the y axis
 */
public class GLine extends DrawObject {
    /**
     * pts
     */
    ArrayList<Marker> pts;
    /**
     * xShift
     */
    double xShift;
    /**
     * yShift
     */
    double yShift;

    /**
	 * 
	 */
    public GLine() {
	super();
	pts = new ArrayList<Marker>();
	xShift = 0;
	yShift = 0;
    }

    /**
     * @param mrk
     */
    public void add(Marker mrk) {
	pts.add(mrk);
    }

    /**
     * @param i
     * @return ith point
     */
    public Marker get(int i) {
	return pts.get(i);
    }

    /**
     * This method sets parameter which determines by how much the whole line
     * should be shifted by along the x axis. A positive number will shift it
     * towards the positive side of the axis and a negative number will shift it
     * towards the negative side if the axis default is 0
     * 
     * @param x
     */
    public void setXShift(double x) {
	xShift = x;
    }

    /**
     * This method adds the specified shift to the existing one
     * 
     * @param x
     */
    public void addXShift(double x) {
	xShift = xShift + x;
    }

    /**
     * @return XShift
     */
    public double getXShift() {
	return (xShift);
    }

    /**
     * This method sets parameter which determines by how much the whole line
     * should be shifted by along the y axis. A positive number will shift it
     * towards the positive side of the axis and a negative number will shift it
     * towards the negative side if the axis. default is 0
     * 
     * @param y
     */
    public void setYShift(double y) {
	yShift = y;
    }

    /**
     * This method adds the specified shift tp the existing one
     * 
     * @param y
     */
    public void addYShift(double y) {
	yShift = y + yShift;
    }

    /**
     * @return Yshift
     */
    public double getYShift() {
	return (yShift);
    }

    /**
     * This will delete all the points and reset the paramneters to its default
     * values.
     */
    public void reset() {
	pts.clear();
	super.reset();
	yShift = 0;
	xShift = 0;

    }

    /**
     * @param mrk
     * @return true if the line contains the marker
     */
    public boolean contains(Marker mrk) {
	return (pts.contains(mrk));
    }

    /**
     * @param mrk
     * @return the index of marker
     */
    public int indexOf(Marker mrk) {
	return (pts.indexOf(mrk));
    }

    /**
     * @return if the list of points is empty
     */
    public boolean isEmpty() {
	return (pts.isEmpty());
    }

    /**
     * @param mrk
     */
    public void remove(Marker mrk) {
	pts.remove(mrk);
    }

    /**
     * @return size of pts
     */
    public int size() {
	return (pts.size());
    }

    /**
     * (non-Javadoc)
     * 
     * @see graph.DrawObject#draw(java.awt.Graphics2D)
     */
    public void draw(Graphics2D g2) {
	Marker prvMark = null; // or else it gives not initialised error
	g2.setPaint(paint);
	Debug.print("There are " + pts.size() + " pts in this line");
	if (!pts.isEmpty()) {
	    prvMark = pts.get(0);
	}
	for (int i = 1; i < pts.size(); i++) {
	    Marker m = pts.get(i);
	    // g2.setPaint(c.getPaint());

	    // System.out.println("layer calling draw of drawobject");
	    if(prvMark==null) {
		throw new NullPointerException("Gline:draw: prvMark is null");
	    }
	    g2.draw(new Line2D.Double((prvMark.getX() + xShift) * xScale,
		    (prvMark.getY() + yShift) * yScale, (m.getX() + xShift)
			    * xScale, (m.getY() + yShift) * yScale));
	    prvMark = m;
	}

    }

    /*
     * public void draw_circle(Graphics2D g2) { Marker prvMark=null; // or else
     * it gives not initialised error g2.setPaint(paint);
     * Debug.print("There are " + pts.size() +" pts in this line"); if
     * (!pts.isEmpty()) { prvMark = (Marker)pts.get(0); } for(int i=1;
     * i<pts.size();i++) { Marker m; m= (Marker)pts.get(i);
     * //g2.setPaint(c.getPaint());
     * 
     * //System.out.println("layer calling draw of drawobject");
     * g2.drawcircle((prvMark
     * .getX()+xShift)*xScale,(prvMark.getY()+yShift)*yScale,10,10); prvMark =
     * m; }
     * 
     * }
     */

}

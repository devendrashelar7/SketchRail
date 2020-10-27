package graph;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import util.Debug;

/**
 * 
 */
@SuppressWarnings("serial")
public class GraphFrame extends Frame {
	/**
	 * trainGraph
	 */
	Paper trainGraph;

	/**
     * 
     */
	@SuppressWarnings("deprecation")
	public GraphFrame() {
		super("Graph");
		setBounds(10, 10, 800, 500);
		trainGraph = new Paper();
		add(trainGraph);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});
		// setMaxXY(25,25);

		show();
	}

	/**
	 * @return paper
	 */
	public Paper getPaper() {
		return trainGraph;
	}

	/**
	 * @param arg
	 */
	public static void main(String arg[]) {
		GraphFrame gf;
		gf = new GraphFrame();
		Paper gp = gf.getPaper();
		gp.setOrigin(30, 20);
		// gp.setScale(2,1.5);
		Paper.GridLayer xg = gp.getGridInstance();
		Paper.GridLayer yg = gp.getGridInstance();
		XGridLine xl;
		YGridLine yl;
		xl = new XGridLine(200, "hello");
		xg.add(xl);
		xl = new XGridLine(300);
		xg.add(xl);
		xl = new XGridLine(500);
		xg.add(xl);
		xl = new XGridLine(100);
		xg.add(xl);
		yl = new YGridLine(100, "y1");
		yg.add(yl);
		yl = new YGridLine(200, "y2");
		yg.add(yl);
		yl = new YGridLine(300, "y3");
		yg.add(yl);
		yl = new YGridLine(50, "y4");
		yg.add(yl);

		xg.setVisible(true);
		yg.setVisible(true);

		gp.setYGrid(yg);
		// gp.setGrid(xg,yg);
		//

		// adding a curve
		GLine crv = new GLine();
		crv.add(new Marker(1, 1));
		crv.add(new Marker(100, 100));
		crv.add(new Marker(200, 300));
		// crv.lineTo(100,100);
		// crv.lineTo(20,10);
		crv.setPaint(Color.red);
		// crateing a curve list
		LineLayer crL = new LineLayer();
		crL.add(crv);
		crL.setStroke(DrawStyle.dashed);
		// adding another curve
		crv = new GLine();
		crv.add(new Marker(30, 40));
		crv.add(new Marker(145, 300));
		crv.add(new Marker(20, 120));
		crL.setXShift(100);
		crv.setYShift(100);
		// crv.lineTo(145,300);
		// crv.lineTo(23,125);
		crv.setPaint(Color.blue);
		crL.add(crv);
		// crL.setStroke(drawStyle.dashed);

		crL.setVisible(true);
		gp.addLayer(crL);
		Debug.print("Will call curve .drawgraph");
		gp.drawGraph();
	}
}

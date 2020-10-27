import graph.Paper;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import util.Debug;

/**
 * GraphPanel
 */
public class GraphPanel extends JPanel implements PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * btNextTrain
	 */
	JButton btNextTrain;
	/**
	 * btNextStation
	 */
	JButton btNextStation;
	/**
	 * btComplete
	 */
	JButton btComplete;
	/**
	 * btExit
	 */
	JButton btExit;
	/**
	 * btSave take screenshots and save as png files
	 */
	JButton btSave;
	/**
	 * btNextScreen
	 */
	JButton btNextScreen;
	/**
	 * btPrevScreen
	 */
	JButton btPrevScreen;
	/**
	 * scrollPane
	 */
	JScrollPane scrollPane;
	/**
	 * freightSim
	 */
	FreightSim freightSim;
	/**
	 * trainGraph
	 */
	GraphPaper trainGraph;
	/**
	 * xPoint
	 */
	int xPoint;
	/**
	 * yPoint
	 */
	int yPoint;// pointer variables
	/**
	 * checkBlockReserve
	 */
	JCheckBox checkBlockReserve;
	/**
	 * popup
	 */
	JPopupMenu popup;
	/**
	 * blockReserve
	 */
	boolean blockReserve = true;

	// static int numSimTrain=0;
	/**
	 * task
	 */
	private Task task;
	/**
	 * progressBar
	 */
	private JProgressBar progressBar;

	/**
	 * @param propertyChangeEvent
	 *            event
	 * 
	 */
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		if ("progress" == propertyChangeEvent.getPropertyName()) {
			int progress = (Integer) propertyChangeEvent.getNewValue();
			// int progress=freightSim.currTrainNo;
			this.progressBar.setValue(progress);
			// taskOutput.append(String.format(
			// / "Completed %d%% of task.\n", task.getProgress()));
		}
	}

	/**
	 * task
	 */
	class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		public Void doInBackground() {
			// Random random = new Random();
			int progress = 0;
			// Initialize progress property.
			setProgress(progress);

			// Sleep for up to one second.

			while (freightSim.currentTrainNo < freightSim.trainsArray.size()) {

				// freightSim.simulate();
				int val = freightSim.simulateNextTrain();
				progress = ((freightSim.currentTrainNo * 100) / freightSim.trainsArray
						.size()) + 1;
				// if(freightSim.currTrainNo == freightSim.trainsArray.size())
				// progress=100;

				// numSimTrain=progress;
				// System.out.println("NASH2:"+progress+"  "+freightSim.currTrainNo
				// +"=="+freightSim.trainsArray.size());
				if (val == -1)
					break;
				setProgress(progress);
			}

			System.out.println("NASH:" + freightSim.currentTrainNo + "=="
					+ freightSim.trainsArray.size());

			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			// startButton.setEnabled(true);
			setCursor(null); // turn off the wait cursor

			double totalTime = 0;
			int count = 0;
			for (int i = 0; i < GlobalVar.trainArrayList.size(); i++) {
				Train trn = GlobalVar.trainArrayList.get(i);

				if ((trn.timeTables.size() > 0) && (trn.isScheduled == false)) {
					// system.out.println(" LLL "+trn.trainNo+"  "+trn.operatingdays
					// );
					totalTime += trn.totalTime();
					count++;
				}
				// System.out.println(trn.trainNo + "  -  " + trn.totalTime() +
				// "  -  " + trn.travelTime() );
			}
			if (count != 0) {
				System.out
						.println("Average Travelling time for freight trains  "
								+ totalTime / count);

				JOptionPane
						.showMessageDialog(
								null,
								Integer.toString(freightSim.currentTrainNo)
										+ " out of "
										+ Integer
												.toString(freightSim.trainsArray
														.size())
										+ "  Freight trains Scheduled successfully \n Average Travelling time for freight trains  "
										+ Double.toString(totalTime / count)
										+ " mins", "Simulator",
								JOptionPane.INFORMATION_MESSAGE);
			}
			// Display of Timetable in excel file
			Train train1;

			try {
				String fileName = "TimeTable.xls";
				JOptionPane.showMessageDialog(null,
						"Select a file for saving timetable", "TimeTable File",
						JOptionPane.OK_OPTION);
				String currentDirectoryPath = (new File(".."))
						.getCanonicalPath();
				JFileChooser chooser = new JFileChooser(currentDirectoryPath);
				System.out
						.println("I came here *********************************************");
				File file = new File(fileName);
				chooser.setSelectedFile(file);

				int response = chooser.showSaveDialog(GlobalVar.graphFrame);
				if (response == JFileChooser.APPROVE_OPTION) {
					// user clicked Save
					file = chooser.getSelectedFile();
					System.out.println("I saved the file");
				} else {
					// user cancelled...
					System.out.println("I cancelled saving the file");
				}
				System.out.println("help me");
				OutputStream f2 = new FileOutputStream(file);
				PrintStream bPrintStream = new PrintStream(f2);
				bPrintStream.println("T.No." + "\t" + "station Name" + "\t"
						+ "Loop" + "\t" + "A.Time" + "\t" + "D.Time");
				for (int m = 0; m < GlobalVar.trainArrayList.size(); m++) {
					train1 = GlobalVar.trainArrayList.get(m);
					bPrintStream.print(train1.trainNo + "\t");
					for (int n = 0; n < train1.timeTables.size(); n++) {
						if (((GlobalVar.getStationName((train1.timeTables
								.get(n)).loopNo))) != null) {
							bPrintStream
									.print((GlobalVar
											.getStationName((train1.timeTables
													.get(n)).loopNo))
											+ "\t");
							bPrintStream
									.print((train1.timeTables.get(n)).loopNo
											+ "\t");
							double aTime = (train1.timeTables.get(n)).arrivalTime;
							int aTimeHr = (int) aTime / 60;
							aTime = (aTime - aTimeHr * 60) * 60;
							int aTimeMin = (int) aTime / 60;
							double aTimeSec = aTime - aTimeMin * 60;
							bPrintStream.print(aTimeHr + ":" + aTimeMin + ":"
									+ (int) aTimeSec + "\t");
							double dTime = (train1.timeTables.get(n)).departureTime;
							int dTimeHr = (int) dTime / 60;
							dTime = (dTime - dTimeHr * 60) * 60;
							int dTimeMin = (int) dTime / 60;
							double dTimeSec = dTime - dTimeMin * 60;
							bPrintStream.print(dTimeHr + ":" + dTimeMin + ":"
									+ (int) dTimeSec + "\t");
						}

					}
					bPrintStream.println("\t");
				}
			} catch (Exception e1) {
				Debug.print("Error in handling o/p file ");
				return;
			}

			// End for Display of Timetable in text file
			// start for headway

			Train train2;
			try {
				OutputStream f4 = new FileOutputStream("Headway.xls");
				PrintStream bPrintStream = new PrintStream(f4);
				bPrintStream.println("Block No" + "\t" + "Red to Green" + "\t"
						+ "Red to Double Yellow ");
				for (int m = 0; m < 1; m++)
				// for( int m =0 ; m < GlobalVar.trainArrayList.size(); m++)
				{
					train2 = GlobalVar.trainArrayList.get(m);
					for (int n = 1; n < train2.timeTables.size() - 2; n++) {
						int sks = (train2.timeTables.get(n)).loopNo;
						bPrintStream.print((train2.timeTables.get(n)).loopNo
								+ "\t");
//						double aTime = (train2.timeTables.get(n)).arrivalTime;
//						double dTime = (train2.timeTables.get(n + 2)).departureTime;
						// if (train2.direction == 0) {
						// bPrintStream.print(Math.round((dTime - aTime) * 60
						// + GlobalVar.sudhir[n + 3])
						// + "\t");
						// } else {
						// bPrintStream.print(Math.round((dTime - aTime) * 60
						// + GlobalVar.sudhir[sks - 2])
						// + "\t");
						// }
						// dTime = ( train2.timeTables.get(n +
						// 1)).departureTime;
						// if (train2.direction == 0) {
						// bPrintStream.println(Math.round((dTime - aTime)
						// * 60 + GlobalVar.sudhir[n + 2])
						// + "\t");
						// } else {
						// bPrintStream.println(Math.round((dTime - aTime)
						// * 60 + GlobalVar.sudhir[sks - 1])
						// + "\t");
						//
						// }
					}

				}
			} catch (Exception e1) {
				Debug.print("Error in handling o/p file ");
				return;
			}

			// End for Display of Timetable in text file
			// Display of signals seen by trains in a text file
			Train train3;

			try {
				OutputStream f3 = new FileOutputStream("Signal.xls");
				PrintStream bPrintStream = new PrintStream(f3);

				for (int m = 0; m < GlobalVar.trainArrayList.size(); m++) {
					bPrintStream.print("Train.No." + "\t");
					train3 = GlobalVar.trainArrayList.get(m);
					bPrintStream.print(train3.trainNo + "\t");
					String dir = null;
					dir = (train3.direction == 0) ? "UP" : "DOWN";
					bPrintStream.println(dir + "\t");
					bPrintStream
							.println("**************************************");
					bPrintStream.println("Block No." + "\t" + "Signal Colour");
					bPrintStream
							.println("-----------------------------------------------");
					for (int n = 0; n < train3.timeTables.size(); n++) {

						bPrintStream.print((train3.timeTables.get(n)).loopNo
								+ "\t");
						int Color;
						Color = (train3.timeTables.get(n)).signal;

						String signalColor = null;
						if (GlobalVar.numberOfColour == 4) {
							signalColor = (6 == Color) ? "pink"
									: (3 == Color) ? "Green"
											: ((2 == Color) ? "DoubleYellow"
													: ((1 == Color) ? "Yellow"
															: "Red"));
						}
						if (GlobalVar.numberOfColour == 3) {
							signalColor = (6 == Color) ? "pink"
									: (2 == Color) ? "Green"
											: (((1 == Color) ? "Yellow" : "Red"));
						}
						if (GlobalVar.numberOfColour == 2) {
							signalColor = (6 == Color) ? "pink"
									: (0 == Color) ? "Red"
											: (1 == Color) ? "Green" : "Green";
						}

						bPrintStream.println(signalColor + "\t");
					}
					bPrintStream.println("\t");
				}

			} catch (Exception e1) {
				Debug.print("Error in handling o/p file ");
				return;
			}

			// End for Display of Timetable in text file

			System.out.println("Simulation is over ");
			btComplete.setEnabled(false);
			btNextTrain.setEnabled(false);

			// taskOutput.append("Done!\n");
		}
	}

	/**
	 * 
	 * @param s
	 *            freightSim
	 */
	public GraphPanel(FreightSim s) {
		this.freightSim = s;
		setLayout(null);

		trainGraph = new GraphPaper();
		add(trainGraph);
		checkBlockReserve = new JCheckBox("Reservations");
		checkBlockReserve.setMnemonic(KeyEvent.VK_R);
		checkBlockReserve.setSelected(true);
		add(checkBlockReserve);
		task = new Task();
		task.addPropertyChangeListener(this);

		checkBlockReserve.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					trainGraph.blockReserve = false;
					clearBlockReservations();
				} else {
					trainGraph.blockReserve = true;
					Train train;
					for (int m = 0; m < GlobalVar.trainArrayList.size(); m++) {
						train = GlobalVar.trainArrayList.get(m);
						trainGraph.drawBlockReservations(train);
					}
				}
				trainGraph.drawGraph();
			}
		});
		btNextTrain = new JButton("Next Train");
		add(btNextTrain);
		btNextTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debug.print("In Next Train");
				int completeSim = freightSim.simulateNextTrain();
				if (completeSim == -1) {
					Debug.print("Complete simulation");
					btNextTrain.setEnabled(false);
					btComplete.setEnabled(false);
					JOptionPane.showMessageDialog(null,
							"All Freight trains Scheduled successfully",
							"Simulator", JOptionPane.INFORMATION_MESSAGE);
					System.out.println("Simulation is over ");

				}
			}
		});
		btNextStation = new JButton("Print Timetable");
		btNextStation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Train train;
				for (int m = 0; m < GlobalVar.trainArrayList.size(); m++) {
					train = GlobalVar.trainArrayList.get(m);
					train.printTimeTable();
				}
				Debug.print("In Next Station");
			}
		});
		add(btNextStation);
		btComplete = new JButton("Complete");
		btComplete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debug.print("In Complete");
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				task.execute();

				progressBar = new JProgressBar(0, 100);
				progressBar.setValue(0);
				progressBar.setStringPainted(true);

				JPanel panel = new JPanel();
				panel.add(progressBar);

				JFrame frame = new JFrame("ProgressBarDemo");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				// Create and set up the content pane.

				frame.setContentPane(panel);

				// Display the window.
				frame.pack();
				frame.setVisible(true);

				// freightSim.simulate();
				/*
				 * double totalTime = 0; int count = 0; for (int i =0 ;
				 * i<GlobalVar.trainArrayList.size(); i++) { Train trn =
				 * (Train)GlobalVar.trainArrayList.get(i);
				 * if((trn.timeTables.size()>0)&&(trn.Sched==false)) {
				 * //system.out
				 * .println(" LLL "+trn.trainNo+"  "+trn.operatingdays );
				 * totalTime += trn.totalTime(); count++; }
				 * //System.out.println(trn.trainNo + "  -  " + trn.totalTime()
				 * + "  -  " + trn.travelTime() ); } if(count != 0) {
				 * System.out.
				 * println("Average Travelling time for freight trains  " +
				 * totalTime);
				 * 
				 * JOptionPane.showMessageDialog(null,
				 * "All Freight trains Scheduled successfully \n Average Travelling time for freight trains  "
				 * +Double.toString(totalTime),
				 * "Simulator",JOptionPane.INFORMATION_MESSAGE); } // Display of
				 * Timetable in excel file Train train1;
				 * 
				 * try{ OutputStream f2 = new FileOutputStream("Timetable.xls");
				 * PrintStream bPrintStream = new PrintStream(f2);
				 * bPrintStream.println
				 * ("T.No."+"\t"+"station Name"+"\t"+"Loop"+"\t"
				 * +"A.Time"+"\t"+"D.Time"); for( int m =0 ; m <
				 * GlobalVar.trainArrayList.size() ; m++) { train1 =
				 * (Train)GlobalVar.trainArrayList.get(m) ;
				 * bPrintStream.print(train1.trainNo+"\t"); for( int n =0 ; n
				 * <train1.timeTables.size(); n++) {
				 * if(((GlobalVar.getStationName
				 * ((train1.timeTables.get(n)).loopNo)))!= null) {
				 * bPrintStream.print((GlobalVar.getStationName(((TimetableEntry
				 * )train1.timeTables.get(n)).loopNo))+"\t");
				 * bPrintStream.print( (train1.timeTables.get(n)).loopNo+"\t");
				 * double aTime = (train1.timeTables.get(n)).arrTime; int
				 * aTimeHr=(int)aTime/60; aTime=(aTime-aTimeHr*60)*60; int
				 * aTimeMin =(int)aTime/60; double aTimeSec=aTime-aTimeMin*60;
				 * bPrintStream
				 * .print(aTimeHr+":"+aTimeMin+":"+(int)aTimeSec+"\t"); double
				 * dTime = (train1.timeTables.get(n)).depTime; int
				 * dTimeHr=(int)dTime/60; dTime=(dTime-dTimeHr*60)*60; int
				 * dTimeMin =(int)dTime/60; double dTimeSec=dTime-dTimeMin*60;
				 * bPrintStream
				 * .print(dTimeHr+":"+dTimeMin+":"+(int)dTimeSec+"\t"); }
				 * 
				 * } bPrintStream.println("\t"); } } catch(Exception e1) {
				 * Debug.print("Error in handling o/p file " ); return; }
				 * 
				 * // End for Display of Timetable in text file // start for
				 * headway
				 * 
				 * Train train2; try{ OutputStream f4 = new
				 * FileOutputStream("Headway.xls"); PrintStream bPrintStream =
				 * new PrintStream(f4);
				 * bPrintStream.println("Block No"+"\t"+"Red to Green"
				 * +"\t"+"Red to Double Yellow "); for( int m =0 ; m <1; m++)
				 * //for( int m =0 ; m < GlobalVar.trainArrayList.size(); m++) {
				 * train2 = (Train)GlobalVar.trainArrayList.get(m) ; for( int n
				 * =1 ; n <train2.timeTables.size()-2; n++) { int
				 * sks=(train2.timeTables.get(n)).loopNo; bPrintStream
				 * .print((train2.timeTables.get(n)). loopNo+"\t"); double aTime
				 * = (train2.timeTables.get(n)).arrTime; double dTime =
				 * (train2.timeTables.get(n+2)).depTime; if(train2.Direction==
				 * 0) { bPrintStream.print(Math.round((dTime
				 * -aTime)*60+GlobalVar.sudhir[n+3])+"\t"); } else {
				 * bPrintStream
				 * .print(Math.round((dTime-aTime)*60+GlobalVar.sudhir
				 * [sks-2])+"\t"); } dTime =
				 * (train2.timeTables.get(n+1)).depTime; if(train2.Direction==
				 * 0) { bPrintStream.println(Math.round((dTime
				 * -aTime)*60+GlobalVar.sudhir[n+2])+"\t"); } else {
				 * bPrintStream
				 * .println(Math.round((dTime-aTime)*60+GlobalVar.sudhir
				 * [sks-1])+"\t");
				 * 
				 * } }
				 * 
				 * } } catch(Exception e1) {
				 * Debug.print("Error in handling o/p file " ); return; }
				 * 
				 * // End for Display of Timetable in text file // Display of
				 * signals seen by trains in a text file Train train3;
				 * 
				 * try{ OutputStream f3 = new FileOutputStream("Signal.xls");
				 * PrintStream bPrintStream = new PrintStream(f3);
				 * 
				 * for( int m =0 ; m < GlobalVar.trainArrayList.size() ; m++) {
				 * bPrintStream.print("Train.No."+"\t"); train3 =
				 * (Train)GlobalVar.trainArrayList.get(m) ;
				 * bPrintStream.print(train3.trainNo+"\t"); String dir=null;
				 * dir=(train3.Direction==0)?"UP":"DOWN";
				 * bPrintStream.println(dir+"\t");
				 * bPrintStream.println("**************************************"
				 * ); bPrintStream.println("Block No."+"\t"+"Signal Colour");
				 * bPrintStream
				 * .println("-----------------------------------------------");
				 * for( int n =0 ; n <train3.timeTables.size(); n++) {
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * bPrintStream.print((train3.timeTables.get(n)). loopNo+"\t");
				 * int Color; Color=(train3.timeTables.get(n)).signal;
				 * 
				 * String signalColor = null; if(GlobalVar.NoOfColour ==4) {
				 * signalColor =(6==Color)?"pink":(3==Color)?
				 * "Green":((2==Color)?"DoubleYellow":
				 * ((1==Color)?"Yellow":"Red")); } if(GlobalVar.NoOfColour ==3)
				 * { signalColor =
				 * (6==Color)?"pink":(2==Color)?"Green":(((1==Color
				 * )?"Yellow":"Red")); } if(GlobalVar.NoOfColour ==2) {
				 * signalColor =
				 * (6==Color)?"pink":(0==Color)?"Red":(1==Color)?"Green"
				 * :"Green"; }
				 * 
				 * 
				 * bPrintStream.println(signalColor+"\t"); }
				 * bPrintStream.println("\t"); }
				 * 
				 * 
				 * } catch(Exception e1) {
				 * Debug.print("Error in handling o/p file " ); return; }
				 * 
				 * // End for Display of Timetable in text file
				 * 
				 * System.out.println("Simulation is over ");
				 * btComplete.setEnabled(false); btNextTrain.setEnabled(false);
				 */
			}
		});
		add(btComplete);
		btNextScreen = new JButton("Next Screen");
		btNextScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debug.print("In Next Screen");
				trainGraph.nextScreen();
			}
		});
		add(btNextScreen);

		btPrevScreen = new JButton("Previous Screen");
		btPrevScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debug.print("In Prev Screen");
				trainGraph.previousScreen();
			}
		});
		add(btPrevScreen);

		btSave = new JButton("Save");
		btSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Save called");
				try {
					Rectangle rect = trainGraph.getBounds();
					BufferedImage screenCapture = new Robot()
							.createScreenCapture(new Rectangle(20 + rect.x,
									20 + rect.y, rect.width, rect.height + 30));

					JFileChooser fc = new JFileChooser();
					fc.showOpenDialog(GraphPanel.this);
					// save as png
					// FileDialog fileDialog = new FileDialog(new Frame("yo"));

					// File file= new File("ScreenCapture.png");
					File file = fc.getSelectedFile();
					ImageIO.write(screenCapture, "png", file);
					System.out.println("Hopefully saved");
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		add(btSave);
		btExit = new JButton("Close");
		btExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debug.print("In Close");
				System.exit(0);
			}
		});
		add(btExit);
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				drawBounds();
			}
		});

		popup = new JPopupMenu();
		popup.setLightWeightPopupEnabled(false);

		trainGraph.addMouseListener(new MouseAdapter() { // Add listener to
					// components that can
					// bring up popup
					// menus.
					public void mousePressed(MouseEvent e) {
						maybeShowPopup(e);
					}

					public void mouseReleased(MouseEvent e) {
						maybeShowPopup(e);
					}

					private void maybeShowPopup(MouseEvent e) {
						if (e.isPopupTrigger()) {
							xPoint = e.getX();
							yPoint = e.getY();
							popup = trainGraph.analysePoint(xPoint, yPoint);
							popup.show(e.getComponent(), e.getX(), e.getY());
						}
					}
				});
		freightSim.produceGraph(this);
	}

	/**
	 * Set the bounds.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		drawBounds();
		trainGraph.drawGraph(GlobalVar.trainArrayList,
				GlobalVar.stationArrayList);
	}

	/**
	 * Draw the bounds.
	 */
	public void drawBounds() {
		// btPrevScreen.setBounds(400, 10, 130, 30);
		btPrevScreen.setBounds(400, 0, 130, 30);
		btNextScreen.setBounds(550, 0, 130, 30);
		btSave.setBounds(700, 0, 100, 30);
		btNextStation.setBounds(250, 0, 130, 30);
		trainGraph.setBounds(0, 75, getWidth(), getHeight() - 175);
		btExit.setBounds(getWidth() - 150, getHeight() - 75, 120, 30);
		btComplete.setBounds(getWidth() - 400, getHeight() - 75, 120, 30);
		btNextTrain.setBounds(getWidth() - 600, getHeight() - 75, 120, 30);
		checkBlockReserve.setBounds(0, getHeight() - 75, 100, 30);
	}

	/**
	 * @return the trainGraph
	 */
	public Paper getPaper() {
		return trainGraph;
	}

	/**
	 * @param train
	 */
	public void drawTrain(Train train) {
		trainGraph.drawTrain(train);
	}

	/**
	 * @param trains
	 *            {@link ArrayList} of trains
	 * @param stnArray
	 *            {@link ArrayList} of stations
	 */
	public void drawGraph(ArrayList<Train> trains, ArrayList<Station> stnArray) {
		trainGraph.drawGraph(trains, stnArray);
	}

	/**
	 * clearBlockReservations
	 */
	public void clearBlockReservations() {
		trainGraph.clearBlockReservations();
	}

	/**
	 * @param trainNumber
	 * @return Color corresponding to the given trainNumber
	 */
	public Color getColor(int trainNumber) {
		ArrayList<Train> trainArrayList = GlobalVar.trainArrayList;
		for (int i = 0; i < trainArrayList.size(); i++) {
			Train train = trainArrayList.get(i);
			if (train.trainNo == trainNumber) {
				Debug.print("GraphPanel: getColor: Train no " + i + " "
						+ trainNumber);
				return (train.drawColour);
			}
		}
		return Color.black;
	}
}

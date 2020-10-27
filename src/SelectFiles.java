import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import util.Debug;

/**
 * Graphical user interface for selecting files.
 */
public class SelectFiles extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// For station
	/**
	 * jTxtStation
	 */
	JTextField jTxtStation;
	/**
	 * jLblStation
	 */
	JLabel jLblStation;
	/**
	 * jButStation
	 */
	JButton jButStation;
	/**
	 * fc
	 */
	JFileChooser fc;

	// For Block
	/**
	 * jTxtBlock
	 */
	JTextField jTxtBlock;
	/**
	 * jLblBlock
	 */
	JLabel jLblBlock;
	/**
	 * jButBlock
	 */
	JButton jButBlock;

	// For Loop
	/**
	 * jTxtLoop
	 */
	JTextField jTxtLoop;
	/**
	 * jLblLoop
	 */
	JLabel jLblLoop;
	/**
	 * jButLoop
	 */
	JButton jButLoop;

	// For Scheduled
	/**
	 * jTxtScheduled
	 */
	JTextField jTxtScheduled;
	/**
	 * jLblScheduled
	 */
	JLabel jLblScheduled;
	/**
	 * jButScheduled
	 */
	JButton jButScheduled;

	// For Unscheduled
	/**
	 * jTxtUnScheduled
	 */
	JTextField jTxtUnScheduled;
	/**
	 * jLblUnScheduled
	 */
	JLabel jLblUnScheduled;
	/**
	 * jButUnScheduled
	 */
	JButton jButUnScheduled;

	// For param
	/**
	 * jButParam
	 */
	JButton jButParam;
	/**
	 * jLblParam
	 */
	JLabel jLblParam;
	/**
	 * jTxtParam
	 */
	JTextField jTxtParam;

	// For signal Failure
	/**
	 * jTxtSignalFailure
	 */
	JTextField jTxtSignalFailure;
	/**
	 * jLblSignalFailure
	 */
	JLabel jLblSignalFailure;
	/**
	 * jButSignalFailure
	 */
	JButton jButSignalFailure;
	/**
	 * jChkSignalFailure
	 */
	JCheckBox jChkSignalFailure;

	// Ok Button
	/**
	 * jButOk
	 */
	JButton jButOk;

	// Cancel Button
	/**
	 * jButCancel
	 */
	JButton jButCancel;

	// Gradient
	/**
	 * jTxtGradientfile
	 */
	JTextField jTxtGradientfile;
	/**
	 * jLblGradientfile
	 */
	JLabel jLblGradientfile;
	/**
	 * jButGradientfile
	 */
	JButton jButGradientfile;

	// GradientEffect
	/**
	 * jTxtGradientEffectfile
	 */
	JTextField jTxtGradientEffectfile;
	/**
	 * jLblGradientEffectfile
	 */
	JLabel jLblGradientEffectfile;
	/**
	 * jButGradientEffectfile
	 */
	JButton jButGradientEffectfile;

	// Passdelay
	/**
	 * jChkPassengerDelayfile
	 */
	JCheckBox jChkPassengerDelayfile;
	/**
	 * jTxtPassengerDelayfile
	 */
	JTextField jTxtPassengerDelayfile;
	/**
	 * jLblPassengerDelayfile
	 */
	JLabel jLblPassengerDelayfile;
	/**
	 * jButPassengerDelayfile
	 */
	JButton jButPassengerDelayfile;

	/**
	 * jTextBlockDirectionInIntervalFile : filepath for
	 * blockDirectionInInterval.txt
	 */
	JTextField jTextBlockDirectionInIntervalFile;
	/**
	 * jLabelBlockDirectionInIntervalFile
	 */
	JLabel jLabelBlockDirectionInIntervalFile;
	/**
	 * jButtonBlockDirectionInIntervalFile : open file browser
	 */
	JButton jButtonBlockDirectionInIntervalFile;
	/**
	 * jCheckBlockDirectionInIntervalFile
	 */
	JCheckBox jCheckBlockDirectionInIntervalFile;

	//
	/**
	 * me
	 */
	private static SelectFiles me = null;

	/**
	 * fileStation
	 */
	File fileStation;
	/**
	 * fileBlock
	 */
	File fileBlock;
	/**
	 * fileLoop
	 */
	File fileLoop;
	/**
	 * fileScheduled
	 */
	File fileScheduled;
	/**
	 * fileUnScheduled
	 */
	File fileUnScheduled;
	/**
	 * fileParam
	 */
	File fileParam;
	/**
	 * fileSignalFailure
	 */
	File fileSignalFailure;
	/**
	 * fileGradient
	 */
	File fileGradient;
	/**
	 * fileGradientEffect
	 */
	File fileGradientEffect;
	/**
	 * filePassengerDelay
	 */
	File filePassengerDelay;
	/**
	 * fileBlockDirectionInInterval
	 */
	File fileBlockDirectionInInterval;

	public int lblXCoord = 40;
	public int txtXCoord = 240;
	public int butXCoord = 340;
	public int checkBoxXCoord = 20;

	public int lblWidth = 180;
	public int txtWidth = 80;
	public int butWidth = 37;

	public int butHeight = 24;
	public int lblHeight = 27;
	public int txtHeight = 21;

	public int yTxtStartCoord = 39;
	public int yLblStartCoord = 20;
	public int yButStartCoord = 38;
	public int yDiffLblTxt = 5;
	public int yDiffLblBut = 4;
	public int yDiff = 48;
	public int tempY;

	/**
     * 
     */
	private SelectFiles() {
		Debug.print("In Select Files");

		fileStation = GlobalVar.fileStation;
		fileScheduled = GlobalVar.fileScheduled;
		fileBlock = GlobalVar.fileBlock;
		fileLoop = GlobalVar.fileLoop;
		fileUnScheduled = GlobalVar.fileUnscheduled;
		fileParam = GlobalVar.fileParam;
		fileSignalFailure = GlobalVar.fileSignalFailure;
		//
		fileGradient = GlobalVar.fileGradient;
		fileGradientEffect = GlobalVar.fileGradientEffect;
		filePassengerDelay = GlobalVar.filePassDelay;
		fileBlockDirectionInInterval = GlobalVar.fileBlockDirectionInInterval;
		//
		jbInit();
		jTxtStation.setText(fileStation.getPath());
		jTxtBlock.setText(fileBlock.getPath());
		jTxtLoop.setText(fileLoop.getPath());
		jTxtScheduled.setText(fileScheduled.getPath());
		jTxtUnScheduled.setText(fileUnScheduled.getPath());
		jTxtParam.setText(fileParam.getPath());
		jTxtSignalFailure.setText(fileSignalFailure.getPath());
		//
		jTxtPassengerDelayfile.setText(filePassengerDelay.getPath());
		jTxtGradientfile.setText(fileGradient.getPath());
		jTxtGradientEffectfile.setText(fileGradientEffect.getPath());
		jTextBlockDirectionInIntervalFile.setText(fileBlockDirectionInInterval
				.getPath());
		//
		setBounds(0, 0, 600, 600);

	}

	/**
	 * @return {@link SelectFiles}
	 */
	public static SelectFiles getInterface() {
		if (me == null) {
			me = new SelectFiles();
		}
		return me;
	}

	/**
     * 
     */
	private void jbInit() {

		initiateCoordinateValues();

		this.getContentPane().setLayout(null);
		fc = new JFileChooser();

		JLabel jSelectFiles = new JLabel("Select files");
		jSelectFiles.setBounds(lblXCoord, tempY, lblWidth, lblHeight);

		tempY += yDiff;
		createStationComponents();
		tempY += yDiff;
		createBlockComponents();
		tempY += yDiff;
		createLoopComponents();
		tempY += yDiff;
		createScheduledTrainComponents();
		tempY += yDiff;
		createUnscheduledTrainComponents();
		tempY += yDiff;
		createParametersComponents();
		tempY += yDiff;
		createGradientComponents();
		tempY += yDiff;
		createGradientEffectsComponents();
		tempY += yDiff;
		createSignalFailureComponents();
		tempY += yDiff;
		createPassengerDelayComponents();
		tempY += yDiff;
		createBlockDirectionIntervalComponents();
		tempY += yDiff;
		createOKButton();

		createCancelButton();

		Debug.print("adding comp");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				me.setVisible(false);
			}
		});

		this.getContentPane().add(jSelectFiles);
		addComponentsToPane();
	}

	/**
	 * Main function.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		Debug.print("start");
		SelectFiles mi = new SelectFiles();
		Debug.print("Init over");
		mi.setBounds(10, 10, 200, 200);
		mi.setVisible(true);
	}

	/**
	 * Initiate coordinate values;
	 */
	public void initiateCoordinateValues() {
		lblXCoord = 40;
		txtXCoord = 240;
		butXCoord = 340;
		checkBoxXCoord = 20;

		lblWidth = 180;
		txtWidth = 80;
		butWidth = 37;

		butHeight = 24;
		lblHeight = 27;
		txtHeight = 21;

		yTxtStartCoord = 39;
		yLblStartCoord = 20;
		yButStartCoord = 38;
		yDiffLblTxt = 5;
		yDiffLblBut = 4;
		yDiff = 48;

		tempY = yLblStartCoord;
	}

	/**
	 * create and set label, text, button and bounds
	 * 
	 * @param jLabel
	 * @param jText
	 * @param jButton
	 */
	public void createAndSetLblTxtButBounds(Object jLabel, Object jText,
			Object jButton, String textField) {
		jLabel = new JLabel();
		jText = new JTextField(textField);
		jButton = new JButton();

		((JLabel) jLabel).setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		((JTextField) jText).setBounds(txtXCoord, tempY + yDiffLblTxt,
				txtWidth, txtHeight);
		((JButton) jButton).setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);

	}

	/**
	 * create Station Components
	 */
	public void createStationComponents() {
		// For station
		jTxtStation = new JTextField();
		jLblStation = new JLabel();
		jButStation = new JButton();
		jLblStation.setText("Station File");

		jLblStation.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		jTxtStation.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth,
				txtHeight);
		jButStation.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(jLblStation, jTxtStation, jButStation,
		// "Station File");
		Debug.print("now action");
		jButStation.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileStation = fc.getSelectedFile();
					Debug.print("Opening: " + fileStation.getPath() + " "
							+ fileStation.getName() + ".");
					jTxtStation.setText(fileStation.getPath());

				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});

	}

	/**
	 * create BlockComponents
	 */
	public void createBlockComponents() {

		// For Block
		jTxtBlock = new JTextField();
		jLblBlock = new JLabel("Block File");
		jButBlock = new JButton();

		jLblBlock.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		jTxtBlock
				.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth, txtHeight);
		jButBlock
				.setBounds(butXCoord, tempY + yDiffLblBut, butWidth, butHeight);
		// createAndSetLblTxtButBounds(jLblBlock, jTxtBlock, jButBlock,
		// "Block File");
		jButBlock.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileBlock = fc.getSelectedFile();
					Debug.print("Opening: " + fileBlock.getPath() + " "
							+ fileBlock.getName() + ".");
					jTxtBlock.setText(fileBlock.getPath());
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});

	}

	/**
	 * create loop components
	 */
	public void createLoopComponents() {
		// For Loop
		jTxtLoop = new JTextField();
		jLblLoop = new JLabel("Loop File");
		jButLoop = new JButton();

		jLblLoop.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		jTxtLoop.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth, txtHeight);
		jButLoop.setBounds(butXCoord, tempY + yDiffLblBut, butWidth, butHeight);
		// createAndSetLblTxtButBounds(jLblLoop, jTxtLoop, jButLoop,
		// "Loop file");
		jButLoop.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileLoop = fc.getSelectedFile();
					Debug.print("Opening: " + fileLoop.getPath() + " "
							+ fileLoop.getName() + ".");
					jTxtLoop.setText(fileLoop.getPath());
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});

	}

	/**
	 * createScheduledTrainComponents
	 */
	public void createScheduledTrainComponents() {
		// For Scheduled Train

		jTxtScheduled = new JTextField();
		jLblScheduled = new JLabel("Scheduled Trains File");
		jButScheduled = new JButton();

		jLblScheduled.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		jTxtScheduled.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth,
				txtHeight);
		jButScheduled.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(jLblScheduled, jTxtScheduled,
		// jButScheduled, "Scheduled file");
		jButScheduled.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileScheduled = fc.getSelectedFile();
					Debug.print("Opening: " + fileScheduled.getPath() + " "
							+ fileScheduled.getName() + ".");
					jTxtScheduled.setText(fileScheduled.getPath());
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});
	}

	/**
	 * create unscheduled train components
	 */
	public void createUnscheduledTrainComponents() {

		// For Unscheduled Train
		jTxtUnScheduled = new JTextField();
		jLblUnScheduled = new JLabel("UnScheduled Trains File");
		jButUnScheduled = new JButton();

		jLblUnScheduled.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		jTxtUnScheduled.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth,
				txtHeight);
		jButUnScheduled.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(jLblUnScheduled, jTxtUnScheduled,
		// jButUnScheduled, "Unscheduled file");
		jButUnScheduled.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileUnScheduled = fc.getSelectedFile();
					Debug.print("Opening: " + fileUnScheduled.getPath() + " "
							+ fileUnScheduled.getName() + ".");
					jTxtUnScheduled.setText(fileUnScheduled.getPath());
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});

	}

	/**
	 * Create parameters components
	 */
	public void createParametersComponents() {
		// For Param
		jButParam = new JButton();
		jLblParam = new JLabel("Parameter File");
		jTxtParam = new JTextField();

		jLblParam.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		jTxtParam
				.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth, txtHeight);
		jButParam
				.setBounds(butXCoord, tempY + yDiffLblBut, butWidth, butHeight);
		// createAndSetLblTxtButBounds(jLblParam, jTxtParam, jButParam,
		// "Parameters File");
		jButParam.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileParam = fc.getSelectedFile();
					Debug.print("Opening: " + fileParam.getPath() + " "
							+ fileParam.getName() + ".");
					jTxtParam.setText(fileParam.getPath());
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});

	}

	/**
	 * Create Gradient components
	 */
	public void createGradientComponents() {

		// Gradient
		//
		jButGradientfile = new JButton();
		jLblGradientfile = new JLabel("Gradient File");
		jTxtGradientfile = new JTextField();

		jLblGradientfile.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		jTxtGradientfile.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth,
				txtHeight);
		jButGradientfile.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(jLblGradientfile, jTxtGradientfile,
		// jButGradientfile, "Gradient File");
		jButGradientfile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileGradient = fc.getSelectedFile();
					Debug.print("Opening: " + fileGradient.getPath() + " "
							+ fileGradient.getName() + ".");
					jTxtGradientfile.setText(fileGradient.getPath());
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});
	}

	/**
	 * create GradientEffects Components
	 */
	public void createGradientEffectsComponents() {
		// For Gradient Effect
		jButGradientEffectfile = new JButton();
		jLblGradientEffectfile = new JLabel("Gradient Effect File");
		jTxtGradientEffectfile = new JTextField();

		jLblGradientEffectfile.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		jTxtGradientEffectfile.setBounds(txtXCoord, tempY + yDiffLblTxt,
				txtWidth, txtHeight);
		jButGradientEffectfile.setBounds(butXCoord, tempY + yDiffLblBut,
				butWidth, butHeight);
		// createAndSetLblTxtButBounds(jLblGradientEffectfile,
		// jTxtGradientEffectfile, jButGradientEffectfile,
		// "Gradient Effect File");
		jButGradientEffectfile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileGradientEffect = fc.getSelectedFile();
					Debug.print("Opening: " + fileGradientEffect.getPath()
							+ " " + fileGradientEffect.getName() + ".");
					jTxtGradientEffectfile.setText(fileGradientEffect.getPath());
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});
	}

	/**
	 * Create signal failure components
	 */
	public void createSignalFailureComponents() {

		// Signal Failure

		jChkSignalFailure = new JCheckBox();
		jTxtSignalFailure = new JTextField();
		jLblSignalFailure = new JLabel("Signal Failure File");
		jButSignalFailure = new JButton();

		jLblSignalFailure.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		jTxtSignalFailure.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth,
				txtHeight);
		jButSignalFailure.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(jLblSignalFailure, jTxtSignalFailure,
		// jButSignalFailure, "Signal Failure File");
		jChkSignalFailure.setBounds(checkBoxXCoord, tempY, butWidth, butHeight);

		jButSignalFailure.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileSignalFailure = fc.getSelectedFile();
					Debug.print("Opening: " + fileSignalFailure.getPath() + " "
							+ fileSignalFailure.getName() + ".");
					jTxtSignalFailure.setText(fileSignalFailure.getPath());
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});
		jChkSignalFailure.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Debug.print("Checked");
				if (jChkSignalFailure.isSelected() == true) {
					Debug.print("Checked - in true");
					GlobalVar.simulationType = "SignalFailure";
					System.out
							.println(" SIM ULATION IS IN SIGANL FAILURE MODE ");
					jButSignalFailure.setEnabled(true);
					jTxtSignalFailure.setEnabled(true);
				} else {
					Debug.print("Checked -  not in true:-)");
					GlobalVar.simulationType = "Normal";
					System.out.println(" SIM ULATION IS IN NORMAL      MODE ");
					jButSignalFailure.setEnabled(false);
					jTxtSignalFailure.setEnabled(false);
				}
			}
		});
	}

	/**
	 * Create PassengerDelay components.
	 */
	public void createPassengerDelayComponents() {
		// Passenger Train Delay

		jChkPassengerDelayfile = new JCheckBox();
		jTxtPassengerDelayfile = new JTextField();
		jLblPassengerDelayfile = new JLabel("Delay File");
		jButPassengerDelayfile = new JButton();

		jLblPassengerDelayfile.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		jTxtPassengerDelayfile.setBounds(txtXCoord, tempY + yDiffLblTxt,
				txtWidth, txtHeight);
		jButPassengerDelayfile.setBounds(butXCoord, tempY + yDiffLblBut,
				butWidth, butHeight);
		// createAndSetLblTxtButBounds(jLblPassengerDelayfile,
		// jTxtPassengerDelayfile, jButPassengerDelayfile,
		// "Passenger Delay File");
		jChkPassengerDelayfile.setBounds(checkBoxXCoord, tempY, butWidth,
				butHeight);

		jButPassengerDelayfile.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					filePassengerDelay = fc.getSelectedFile();
					Debug.print("Opening: " + filePassengerDelay.getPath()
							+ " " + filePassengerDelay.getName() + ".");
					jTxtPassengerDelayfile.setText(filePassengerDelay.getPath());
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});
		jChkPassengerDelayfile.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Debug.print("Checked");
				if (jChkPassengerDelayfile.isSelected() == true) {
					GlobalVar.delay = 1;
					Debug.print("Checked - in true");

					jButPassengerDelayfile.setEnabled(true);
					jTxtPassengerDelayfile.setEnabled(true);
				} else {
					Debug.print("Checked -  not in true:-)");
					GlobalVar.delay = 0;
					jButPassengerDelayfile.setEnabled(false);
					jTxtPassengerDelayfile.setEnabled(false);
				}
			}
		});
	}

	/**
	 * create BlockDirectionInInterval components
	 */
	public void createBlockDirectionIntervalComponents() {
		// blockDirectionInIntervalFile

		jCheckBlockDirectionInIntervalFile = new JCheckBox();
		jTextBlockDirectionInIntervalFile = new JTextField();
		jLabelBlockDirectionInIntervalFile = new JLabel(
				"BlockDirectionInInterval File");
		jButtonBlockDirectionInIntervalFile = new JButton();

		jLabelBlockDirectionInIntervalFile.setBounds(lblXCoord, tempY,
				lblWidth, lblHeight);
		jTextBlockDirectionInIntervalFile.setBounds(txtXCoord, tempY
				+ yDiffLblTxt, txtWidth, txtHeight);
		jButtonBlockDirectionInIntervalFile.setBounds(butXCoord, tempY
				+ yDiffLblBut, butWidth, butHeight);
		// createAndSetLblTxtButBounds(jLabelBlockDirectionInIntervalFile,
		// jTextBlockDirectionInIntervalFile,
		// jButtonBlockDirectionInIntervalFile, "Block Direction File");
		jCheckBlockDirectionInIntervalFile.setBounds(checkBoxXCoord, tempY,
				butWidth, butHeight);

		jButtonBlockDirectionInIntervalFile
				.addActionListener(new ActionListener() {

					
					public void actionPerformed(ActionEvent e) {
						int returnVal = fc.showOpenDialog(me);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							fileBlockDirectionInInterval = fc.getSelectedFile();
							Debug.print("Opening: "
									+ fileBlockDirectionInInterval.getPath()
									+ " "
									+ fileBlockDirectionInInterval.getName()
									+ ".");
							jTextBlockDirectionInIntervalFile
									.setText(fileBlockDirectionInInterval
											.getPath());
						} else {
							Debug.print("Open command cancelled by user.");
						}
					}
				});
		jCheckBlockDirectionInIntervalFile
				.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						Debug.print("Checked");
						if (jCheckBlockDirectionInIntervalFile.isSelected() == true) {
							GlobalVar.hasBlockDirectionFile = true;
							System.out.println("Turned hasBlockDirectionFile "
									+ GlobalVar.hasBlockDirectionFile);
							Debug.print("Checked - in true");

							jButtonBlockDirectionInIntervalFile
									.setEnabled(true);
							jTextBlockDirectionInIntervalFile.setEnabled(true);
						} else {
							Debug.print("Checked -  not in true:-)");
							// GlobalVar.delay = 0;
							GlobalVar.hasBlockDirectionFile = false;
							System.out.println("Turned hasBlockDirectionFile "
									+ GlobalVar.hasBlockDirectionFile);
							jButtonBlockDirectionInIntervalFile
									.setEnabled(false);
//							jTextBlockDirectionInIntervalFile.setEnabled(false);
							jTextBlockDirectionInIntervalFile.setEnabled(false);
						}
					}
				});
	}

	/**
	 * Create OK Button
	 */
	public void createOKButton() {
		// OK
		jButOk = new JButton("OK");
		jButOk.setBounds(64, tempY, 94, butHeight);
		jButOk.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				GlobalVar.fileStation = fileStation;
				GlobalVar.fileScheduled = fileScheduled;
				GlobalVar.fileBlock = fileBlock;
				GlobalVar.fileLoop = fileLoop;
				GlobalVar.fileUnscheduled = fileUnScheduled;
				GlobalVar.fileParam = fileParam;
				GlobalVar.fileSignalFailure = fileSignalFailure;
				GlobalVar.fileGradient = fileGradient;
				GlobalVar.fileGradientEffect = fileGradientEffect;
				GlobalVar.filePassDelay = filePassengerDelay;
				GlobalVar.fileBlockDirectionInInterval = fileBlockDirectionInInterval;
				setVisible(false);
			}
		});
	}

	/**
	 * Create a cancel button
	 */
	public void createCancelButton() {
		// Cancel
		jButCancel = new JButton("Cancel");
		jButCancel.setBounds(317, tempY, 94, butHeight);
		jButCancel.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent e) {
				me.setVisible(false);
				fileStation = GlobalVar.fileStation;
				fileScheduled = GlobalVar.fileScheduled;
				fileBlock = GlobalVar.fileBlock;
				fileLoop = GlobalVar.fileLoop;
				fileUnScheduled = GlobalVar.fileUnscheduled;
				fileParam = GlobalVar.fileParam;
				fileSignalFailure = GlobalVar.fileSignalFailure;
				//
				fileGradient = GlobalVar.fileGradient;
				fileGradientEffect = GlobalVar.fileGradientEffect;
				filePassengerDelay = GlobalVar.filePassDelay;
				//
				jTxtStation.setText(fileStation.getPath());
				jTxtBlock.setText(fileBlock.getPath());
				jTxtLoop.setText(fileLoop.getPath());
				jTxtScheduled.setText(fileScheduled.getPath());
				jTxtUnScheduled.setText(fileUnScheduled.getPath());
				jTxtParam.setText(fileParam.getPath());
				jTxtSignalFailure.setText(fileSignalFailure.getPath());
				//
				jTxtGradientfile.setText(fileGradient.getPath());
				jTxtGradientEffectfile.setText(fileGradientEffect.getPath());
				jTxtPassengerDelayfile.setText(filePassengerDelay.getPath());
				//
			}
		});
	}

	/**
	 * Add the components to the pane
	 */
	public void addComponentsToPane() {
		this.getContentPane().add(jButStation);
		this.getContentPane().add(jTxtStation);
		this.getContentPane().add(jLblStation);
		this.getContentPane().add(jLblBlock);
		this.getContentPane().add(jTxtBlock);
		this.getContentPane().add(jButBlock);
		this.getContentPane().add(jTxtLoop);
		this.getContentPane().add(jLblLoop);
		this.getContentPane().add(jButLoop);
		this.getContentPane().add(jTxtScheduled);
		this.getContentPane().add(jLblScheduled);
		this.getContentPane().add(jButScheduled);
		this.getContentPane().add(jTxtUnScheduled);
		this.getContentPane().add(jLblUnScheduled);
		this.getContentPane().add(jButUnScheduled);
		this.getContentPane().add(jButParam);
		this.getContentPane().add(jLblParam);
		this.getContentPane().add(jTxtParam);
		//
		this.getContentPane().add(jButGradientfile);
		this.getContentPane().add(jLblGradientfile);
		this.getContentPane().add(jTxtGradientfile);

		this.getContentPane().add(jButGradientEffectfile);
		this.getContentPane().add(jLblGradientEffectfile);
		this.getContentPane().add(jTxtGradientEffectfile);

		this.getContentPane().add(jButPassengerDelayfile);
		this.getContentPane().add(jLblPassengerDelayfile);
		this.getContentPane().add(jTxtPassengerDelayfile);
		this.getContentPane().add(jChkPassengerDelayfile);
		//
		this.getContentPane().add(jButSignalFailure);
		this.getContentPane().add(jLblSignalFailure);
		this.getContentPane().add(jTxtSignalFailure);
		this.getContentPane().add(jChkSignalFailure);

		this.getContentPane().add(jButtonBlockDirectionInIntervalFile);
		this.getContentPane().add(jLabelBlockDirectionInIntervalFile);
		this.getContentPane().add(jTextBlockDirectionInIntervalFile);
		this.getContentPane().add(jCheckBlockDirectionInIntervalFile);

		this.getContentPane().add(jButOk);
		this.getContentPane().add(jButCancel);
		Debug.print("adding comp - done- :-)");
		// this.getContentPane().add(fc);
		Debug.print("adding comp - done");

	}
}
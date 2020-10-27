import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ui.StartUpScreen;
import util.Debug;

/**
 * 
 */
public class MainInterface extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * mainMenuBar
	 */
	JMenuBar mainMenuBar = new JMenuBar();
	/**
	 * jMenu1
	 */
	JMenu jMenu1 = new JMenu();
	/**
	 * jMnItSelect
	 */
	JMenuItem jMnItSelect = new JMenuItem();
	/**
	 * jMnItRun
	 */
	JMenuItem jMnItRun = new JMenuItem();
	/**
	 * jMnItExit
	 */
	JMenuItem jMnItExit = new JMenuItem();
	/**
	 * jMenu2
	 */
	JMenu jMenu2 = new JMenu();
	/**
	 * jMnRunTime
	 */
	JMenuItem jMnRunTime = new JMenuItem();
	/**
	 * borderLayout1
	 */
	BorderLayout borderLayout1 = new BorderLayout();
	/**
	 * jMenu3
	 */
	JMenu jMenu3 = new JMenu();
	/**
	 * jChkMnuDebug
	 */
	JCheckBoxMenuItem jChkMnuDebug = new JCheckBoxMenuItem();

	/**
 * 
 */
	public MainInterface() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
 */
	private void jbInit() {
		this.getContentPane().setLayout(borderLayout1);
		jMenu1.setText("Simulation");
		jMnItSelect.setText("Select");
		jMnItSelect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMnItSelect_actionPerformed(e);
			}
		});
		jMnItRun.setText("Run");
		jMnItRun.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMnItRun_actionPerformed(e);
			}
		});
		jMnItExit.setText("Exit");
		jMnItExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});
		this.setJMenuBar(mainMenuBar);
		this.setTitle("Simulator");
		jMenu2.setText("Report");
		jMnRunTime.setText("Running Time");
		jMnRunTime.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMnRunTime_actionPerformed(e);
			}
		});
		jMenu3.setText("Options");
		jChkMnuDebug.setText("Debug");
		jChkMnuDebug.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jChkMnuDebug_actionPerformed(e);
			}
		});
		mainMenuBar.add(jMenu1);
		mainMenuBar.add(jMenu2);
		mainMenuBar.add(jMenu3);
		jMenu1.add(jMnItSelect);
		jMenu1.add(jMnItRun);
		jMenu1.addSeparator();
		jMenu1.add(jMnItExit);
		jMenu2.add(jMnRunTime);
		jMenu3.add(jChkMnuDebug);
	}

	/**
	 * @param e
	 */
	void jMnItRun_actionPerformed(ActionEvent e) {
		System.out.println("Boom");
		GlobalVar.reset();
		simStart.start();
	}

	/**
	 * @param args
	 */
	public static void main(String args[]) {

//		Debug debug = new Debug();
		StartUpScreen startUp = new StartUpScreen();
		startUp.setVisible(true);
		startUp.setBounds(10, 10, 200, 200);
		simStart.processArguments(args);
		MainInterface mi = new MainInterface();
		mi.setBounds(10, 10, 200, 200);
		mi.setVisible(true);
		startUp.dispose();
	}

	/**
	 * @param e
	 */
	void this_windowClosing(WindowEvent e) {
		System.exit(0);
	}

	/**
	 * @param e
	 */
	void jMnItSelect_actionPerformed(ActionEvent e) {
		Debug.print("Selecte se4lected");
		SelectFiles selectFiles = SelectFiles.getInterface();
		selectFiles.setBounds(100, 100, 500, 700);
		selectFiles.setVisible(true);
	}

	/**
	 * @param e
	 */
	void jMnRunTime_actionPerformed(ActionEvent e) {
		TrainRunTime trnFrame = new TrainRunTime();
	}

	/**
	 * @param e
	 */
	void jChkMnuDebug_actionPerformed(ActionEvent e) {
		if (jChkMnuDebug.getState() == true) {
			Debug.debug = true;
		} else {
			Debug.debug = false;
		}
	}
}
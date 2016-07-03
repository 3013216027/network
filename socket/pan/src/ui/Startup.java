/**
 * 
 */
package ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import backend.DownloadServer;

/**
 * @date 2016年5月9日
 * @author zhengdongjian@tju.edu.cn
 *
 */
@SuppressWarnings("unused")
public class Startup extends JFrame {
	/**
	 * ID
	 */
	private static final long serialVersionUID = -8259271651599013874L;
	
//	/**
//	 * Menu
//	 */
//	final int COUNTMENU = 3;
//	final String USERNAME = "dong";
//	final String PASSWORD = "123456";
//	private SignUpFrame signup;
////	private Checker checker = new Checker();
//	
//	JMenuBar menubar = new JMenuBar();
//	JMenu menuFile = new JMenu("File");
//	JMenuItem[] fileOpts = null;
//
//	//login panel
//	protected boolean identify = false;
//	
//	JLabel lbUserName = new JLabel("User name:");
//	JLabel lbPassword = new JLabel("Password:");
//	JFormattedTextField username = new JFormattedTextField();
//	JPasswordField password = new JPasswordField();
//	JButton logIn = new JButton("Log in");
//	
//	//database
//	DataLoader db = new DataLoader();
//	
//	public Startup() {
//		this.setJMenuBar(menubar);
//		//add menu
//		menubar.add(menuFile);
//		menuFile.setMnemonic('F');
//		
//		fileOpts = new JMenuItem[COUNTMENU];
//		
//		fileOpts[0] = new JMenuItem("Sign up");
//		fileOpts[0].setMnemonic('S');
//		if (signup == null) {
//			signup = new SignUpFrame(db);
//		}
//		fileOpts[0].addActionListener(signup);
//		
//		fileOpts[1] = new JMenuItem("About");
//		fileOpts[1].setMnemonic('A');
//		fileOpts[1].addActionListener(new HelpAboutAction());
//		
//		fileOpts[2] = new JMenuItem("Exit");
//		fileOpts[2].setMnemonic('E');
//		fileOpts[2].addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Startup.this.dispose();
//			}
//		});
//		
//		for (int i = 0; i < COUNTMENU; ++i) {
//			menuFile.add(fileOpts[i]);
//		}
//		
//		
//		//log in
//		lbUserName.setBounds(119, 59, 60, 15);
//		lbPassword.setBounds(119, 100, 60, 15);
//		username.setBounds(230, 56, 96, 21);
//		password.setBounds(230, 97, 96, 21);
//		logIn.setBounds(295, 200, 96, 23);
//		
//		this.getContentPane().setLayout(null);
//		this.getContentPane().add(lbUserName);
//		this.getContentPane().add(lbPassword);
//		this.getContentPane().add(username);
//		this.getContentPane().add(password);
//		this.getContentPane().add(logIn);
//		logIn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (username.getText().equals(Startup.this.username)
//						&& (String.valueOf(password.getPassword()).equals(Startup.this.password))) {
//					Startup.this.identify = true;
//				}
//				System.out.println(username.getText());
//				System.out.println(String.valueOf(password.getPassword()).equals("jian"));
//			}
//		});
//		
//		this.setBounds(100, 100, 760, 512);
//		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		this.setVisible(true);
//		
//		if (!db.open()) {
//			System.out.println("data file open failed!");
//			this.dispose();
//		}
//		
//	}
	
	public static void main(String args[])throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		//new DownloadServer();
		//System.out.println("hello");
		Checker home = new Checker();
		home.setVisible(true);
	}
}
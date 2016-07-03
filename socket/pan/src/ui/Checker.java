/**
 * 
 */
package ui;

import java.awt.EventQueue;
import java.awt.Label;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dialog;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JSeparator;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPasswordField;
import javax.swing.JFormattedTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

/**
 * @date 2016年6月9日
 * @author zhengdongjian@tju.edu.cn
 *
 */
@SuppressWarnings("unused")
public class Checker extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6451794570654083349L;
	
	protected static final boolean debug = false; 
	
	/**
	 * input fields
	 */
	private JPasswordField passwordField;
	private JFormattedTextField formattedTextField;
	
	//signup frame
	private SignUpFrame signup;
	
	//database
	DataLoader db = new DataLoader();
	
	
	/**
	 * Create the application.
	 */
	public Checker() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				db.close();
			}
		});
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JLabel lblUserName = new JLabel("User name:");
		lblUserName.setFont(new Font("宋体", Font.PLAIN, 16));
		lblUserName.setBounds(119, 55, 95, 31);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("宋体", Font.PLAIN, 16));
		lblPassword.setBounds(119, 100, 83, 28);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(224, 97, 127, 31);
		
		formattedTextField = new JFormattedTextField();
		formattedTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
			}
		});
		formattedTextField.setBounds(224, 56, 127, 31);
		this.getContentPane().setLayout(null);
		this.getContentPane().add(lblUserName);
		this.getContentPane().add(lblPassword);
		this.getContentPane().add(passwordField);
		this.getContentPane().add(formattedTextField);
		
		JButton btnLogIn = new JButton("Sign in");
		btnLogIn.setFont(new Font("宋体", Font.PLAIN, 16));
		btnLogIn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//System.out.println("You clicked me!");
				if (db.query(Checker.this.formattedTextField.getText(), String.valueOf(Checker.this.passwordField.getPassword()))) {
					//correct
					System.out.println("query " + Checker.this.formattedTextField.getText());
					new NetDisk(Checker.this.formattedTextField.getText());
					Checker.this.dispose();
				} else {
					JOptionPane.showMessageDialog(Checker.this,
						    "用户名或密码错误！",
						    "Warning",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnLogIn.setBounds(288, 192, 103, 31);
		this.getContentPane().add(btnLogIn);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Menu");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Sign Up");
		if (signup == null) {
			signup = new SignUpFrame(db);
		}
		mntmNewMenuItem.addActionListener(signup);
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("About");
		mntmNewMenuItem_1.addActionListener(new HelpAboutAction());
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Checker.this.dispose();
			}
		});
		mnNewMenu.add(mntmExit);
		
		/**
		 * load data
		 */
		if (!db.open()) {
			System.out.println("data file open failed!");
			this.dispose();
		}
	}
}

//about
class HelpAboutAction implements ActionListener {
  private  Label spa = new Label("\n");
  private final JLabel version = new JLabel(" Version: v1.1.0");
  private final JLabel author = new JLabel( " Author: 3013216027");
  private final JLabel email = new JLabel(  " E-Mail: zhengdongjian@tju.edu.cn");
  //private final JLabel qq = new JLabel("QQ：763400483");
  private Box box = Box.createVerticalBox();
  @Override
  public void actionPerformed(ActionEvent e) {
      JDialog jf = new JDialog();
      jf.setTitle("About");
      //spa.setBackground(Color.CYAN);
      //spa.setFont(new Font("Microsoft Yahei", Font.BOLD, 5));
      jf.add(box);
      //jf.setLayout(new BoxLayout(jf, BoxLayout.Y_AXIS));
      box.add(spa);
      box.add(version);
      box.add(author);
      box.add(email);
      box.add(new JLabel(" "));
      box.add(new JLabel(" "));
      box.add(new JLabel(" "));
      //box.add(qq);
      jf.setBounds(100, 100, 500, 200);
      //jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jf.setVisible(true);
  }
}

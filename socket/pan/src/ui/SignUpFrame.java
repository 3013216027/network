/**
 * 
 */
package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Cursor;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * @date 2016年5月9日
 * @author zhengdongjian@tju.edu.cn
 *
 */
@SuppressWarnings("unused")
public class SignUpFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5712600928163755134L;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton btnSignUp;
	JCheckBox chckbxReadAndAccept;
	private DataLoader db;
	
	
	JLabel label = new JLabel("4-16\u4F4D\u5B57\u6BCD/\u6570\u5B57/\u4E0B\u5212\u7EBF\u7EC4\u5408\uFF0C\u5B57\u6BCD\u5F00\u5934");
	JLabel label_1 = new JLabel("6-16\u4F4D\u5B57\u6BCD/\u6570\u5B57/\u7279\u6B8A\u5B57\u7B26");
	JLabel label_2 = new JLabel("\u4E24\u6B21\u5BC6\u7801\u4E0D\u76F8\u540C\uFF01");
	private final static String usernameDump = "用户已存在";
	private final static String usernameRule = "4-16\u4F4D\u5B57\u6BCD/\u6570\u5B57/\u4E0B\u5212\u7EBF\u7EC4\u5408\uFF0C\u5B57\u6BCD\u5F00\u5934";
			
	/**
	 * Regex for username & password
	 */
	private final static String regexUsername = "[a-zA-Z]\\w{3,15}";
	private final static String regexPassword = "[\\S]{6,16}";
	private JPasswordField passwordField_1;

	/**
	 * Check Username 
	 */
	private Boolean checkUsername() {
		return textField.getText().matches(regexUsername);
	}
	
	/**
	 * Check password
	 */
	private Boolean checkPassword() {
		return String.valueOf(passwordField.getPassword()).matches(regexPassword);
	}

	/**
	 * Check the 2 password
	 */
	private Boolean checkRepeat() {
		return String.valueOf(passwordField_1.getPassword()).equals(String.valueOf(passwordField.getPassword()));
	}
	
	/**
	 * check all input
	 */
	private Boolean checkInput() {
		return chckbxReadAndAccept.isSelected() && checkUsername() && !db.query(textField.getText()) && checkPassword() && checkRepeat();
	}
	
	/**
	 * Create the frame.
	 */
	public SignUpFrame(DataLoader db) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				System.out.println("Singup frame closed!");
			}
		});
		this.db = db;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		//contentPane.setVisible(false);
		//contentPane.setForeground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUserName = new JLabel("User name:");
		lblUserName.setBounds(73, 37, 72, 15);
		contentPane.add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(73, 91, 72, 15);
		contentPane.add(lblPassword);
		
		chckbxReadAndAccept = new JCheckBox("I have read and accept the Priocy");
		chckbxReadAndAccept.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (checkInput()) {
					SignUpFrame.this.btnSignUp.setForeground(new Color(50, 170, 255));
					SignUpFrame.this.btnSignUp.setEnabled(true);
				} else {
					SignUpFrame.this.btnSignUp.setForeground(Color.DARK_GRAY);
					SignUpFrame.this.btnSignUp.setEnabled(false);
				}
			}
		});
		chckbxReadAndAccept.setBounds(205, 199, 223, 23);
		contentPane.add(chckbxReadAndAccept);
		
		//System.out.println("test");
		textField = new JTextField();
		textField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				if (db.query(SignUpFrame.this.textField.getText())) {
					SignUpFrame.this.label.setText(usernameDump);
					SignUpFrame.this.label.setForeground(Color.RED);
				} else {
					SignUpFrame.this.label.setText(usernameRule);
					if (checkUsername()) {
						SignUpFrame.this.label.setForeground(Color.BLACK);
					} else {
						SignUpFrame.this.label.setForeground(Color.RED);
					}
					if (checkInput()) {
						SignUpFrame.this.btnSignUp.setForeground(new Color(50, 170, 255));
						SignUpFrame.this.btnSignUp.setEnabled(true);
					} else {
						SignUpFrame.this.btnSignUp.setForeground(Color.DARK_GRAY);
						SignUpFrame.this.btnSignUp.setEnabled(false);
					}
				}
			}
		});
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				//System.out.println("username focused!");
				SignUpFrame.this.label.setVisible(true);
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if (!db.query(SignUpFrame.this.textField.getText()) && checkUsername()) {
					SignUpFrame.this.label.setVisible(false);
				} else {
					SignUpFrame.this.label.setForeground(Color.RED);
				}
			}
		});
		textField.setFont(new Font("宋体", Font.PLAIN, 12));
		textField.setBounds(186, 34, 156, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (checkPassword()) {
					SignUpFrame.this.label_1.setForeground(Color.BLACK);
				} else {
					SignUpFrame.this.label_1.setForeground(Color.RED);
				}
				if (checkInput()) {
					SignUpFrame.this.btnSignUp.setForeground(new Color(50, 170, 255));
					SignUpFrame.this.btnSignUp.setEnabled(true);
				} else {
					SignUpFrame.this.btnSignUp.setForeground(Color.DARK_GRAY);
					SignUpFrame.this.btnSignUp.setEnabled(false);
				}
			}
		});
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				SignUpFrame.this.label_1.setVisible(true);
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (checkPassword()) {
					SignUpFrame.this.label_1.setVisible(false);
				} else {
					SignUpFrame.this.label_1.setForeground(Color.RED);
				}
			}
		});
		passwordField.setBounds(186, 88, 156, 21);
		contentPane.add(passwordField);

		btnSignUp = new JButton("Sign Up");
		btnSignUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (SignUpFrame.this.btnSignUp.isEnabled()) {
					submit(SignUpFrame.this.textField.getText(), String.valueOf(SignUpFrame.this.passwordField.getPassword()));
				}
			}
		});
		btnSignUp.setEnabled(false);
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("ActionPerformed!");
			}
		});
		btnSignUp.setBackground(SystemColor.textHighlight);
		btnSignUp.setForeground(SystemColor.textInactiveText);
		btnSignUp.setBounds(204, 228, 156, 23);
		contentPane.add(btnSignUp);
		label.setFont(new Font("宋体", Font.PLAIN, 12));
		label.setForeground(Color.RED);
		
		label.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println("test");
			}
		});
		label.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		label.setBounds(186, 65, 261, 15);
		contentPane.add(label);
		label_1.setForeground(Color.RED);
		label_1.setFont(new Font("宋体", Font.PLAIN, 12));
		
		label_1.setVisible(false);
		label_1.setBounds(186, 119, 261, 15);
		contentPane.add(label_1);
		
		JLabel lblRepeatAgain = new JLabel("Repeat again:");
		lblRepeatAgain.setBounds(73, 154, 88, 15);
		contentPane.add(lblRepeatAgain);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				//System.out.println(String.valueOf(SignUpFrame.this.passwordField_1.getPassword()) + "|" + String.valueOf(SignUpFrame.this.passwordField.getPassword()));
				if (checkRepeat()) {
					SignUpFrame.this.label_2.setVisible(false);
				} else {
					SignUpFrame.this.label_2.setVisible(true);
				}
				if (checkInput()) {
					SignUpFrame.this.btnSignUp.setForeground(new Color(50, 170, 255));
					SignUpFrame.this.btnSignUp.setEnabled(true);
				} else {
					SignUpFrame.this.btnSignUp.setForeground(Color.DARK_GRAY);
					SignUpFrame.this.btnSignUp.setEnabled(false);
				}
			}
		});
		passwordField_1.setBounds(186, 151, 156, 21);
		contentPane.add(passwordField_1);
		
		label_2.setVisible(false);
		label_2.setForeground(Color.RED);
		label_2.setFont(new Font("宋体", Font.PLAIN, 12));
		label_2.setBounds(186, 182, 261, 15);
		contentPane.add(label_2);
		
		this.setTitle("Sign Up");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(true);
	}
	
	/**
	 * SignUp -> submit to database
	 */
	private void submit(String username, String password) {
		db.insert(username, password);
		this.dispose();
	}
}

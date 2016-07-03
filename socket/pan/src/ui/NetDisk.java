/**
 * 
 */
package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.List;
import java.net.Socket;
import java.io.*;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @date 2016年6月27日
 * @author zhengdongjian@tju.edu.cn
 *
 */
public class NetDisk extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4558351085623959485L;
	
	final static String address = "127.0.0.1";
	final static int downloadPort = 3222;
	final static int uploadPort = 2333;
	
	Socket downloadSocket = null;
	BufferedInputStream dbis = null;
	BufferedOutputStream dbos = null;
	
	Socket uploadSocket = null;
	BufferedInputStream ubis = null;
	BufferedOutputStream ubos = null;
	
	private final static int BUFFERSIZE = 512; 
	byte[] buffer = new byte[BUFFERSIZE];
	
	private JPanel contentPane;
	private List list;
	
	@SuppressWarnings("unused")
	private String username = null;
	
	/**
	 * Create the frame.
	 */
	public NetDisk(String username) {
		// connect download server
		buildConnection();
		//System.out.println("connection finished!");
		this.username = username;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		list = new List();
		list.setBounds(5, 38, 227, 218);
		try {
			int length;
			String tmp = username;
			while (tmp.length() < BUFFERSIZE) {
				tmp += ' ';
			}
			buffer = tmp.getBytes();
			dbos.write(buffer);
			dbos.flush();
			length = dbis.read(buffer);
			if (length == -1) {
				System.out.println("Can't read count when load list");
			}
			int count = Integer.parseInt(new String(buffer).trim());
			for (int i = 0; i < count; ++i) {
				length = dbis.read(buffer);
				if (length != -1) {
					list.add(new String(buffer).trim());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		contentPane.add(list);
		
		JButton uploadButton = new JButton("\u4E0A\u4F20\u6587\u4EF6");
		uploadButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//upload
				FileDialog fd = new FileDialog(NetDisk.this, "Upload File");
				fd.setVisible(true);
				String filename = username + "/" + fd.getFile();
				while (filename.length() < BUFFERSIZE) {
					filename += ' ';
				}
				buffer = filename.getBytes();
				try {
					ubos.write(buffer, 0, BUFFERSIZE);
					File file = new File(fd.getDirectory() + fd.getFile());
					DataInputStream dis = new DataInputStream(new FileInputStream(file));
					//System.out.println("file " + file.getName() + " => " + file.length());
					long count = file.length();
					System.out.println("count = " + count);
					//count = (count + BUFFERSIZE - 1) / BUFFERSIZE;
					String tmp = String.valueOf(count);
					while (tmp.length() < BUFFERSIZE) {
						tmp += ' ';
					}
					buffer = tmp.getBytes();
					//write the length at the first 512B(the first chunk)
					ubos.write(buffer, 0, BUFFERSIZE);
					int length;
					while (count > 0) {
						length = dis.read(buffer);
						if (length == -1) {
							System.out.println("Can't read more data chunk of " + file.getName());
						}
						ubos.write(buffer, 0, length);
						count -= length;
						System.out.println("count = " + count);
					}
					ubos.flush();
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(fd.getFile());
				list.add(fd.getFile());
			}
		});
		uploadButton.setFont(new Font("宋体", Font.PLAIN, 16));
		uploadButton.setBounds(285, 139, 106, 35);
		contentPane.add(uploadButton);
		
		JButton downloadButton = new JButton("\u4E0B\u8F7D");
		downloadButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//download
				String item = list.getSelectedItem();
				while (item.length() < BUFFERSIZE) {
					item += ' ';
				}
				buffer = item.getBytes();
				try {
					dbos.write(buffer, 0, BUFFERSIZE); //write filename want to download
					dbos.flush();
					FileDialog fd = new FileDialog(NetDisk.this, "Save File");
					fd.setVisible(true);
					String filename = fd.getDirectory() + fd.getFile();
					System.out.println("Saving to file " + filename);
					DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(filename))));
//					int length;
//					while ((length = dbis.read(buffer)) != -1) {
//						dos.write(buffer, 0, length);
//					}
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		downloadButton.setFont(new Font("宋体", Font.PLAIN, 16));
		downloadButton.setBounds(285, 203, 106, 35);
		contentPane.add(downloadButton);
		
		JLabel label = new JLabel("\u6587\u4EF6\u5217\u8868");
		label.setFont(new Font("宋体", Font.PLAIN, 16));
		label.setBounds(86, 10, 86, 22);
		contentPane.add(label);
		
		this.setVisible(true);
	}
	
	private void buildConnection() {
		try {
			System.out.println("before connect to download server...");
			downloadSocket = new Socket(address, downloadPort);
			dbis = new BufferedInputStream(new DataInputStream(downloadSocket.getInputStream()));
			dbos = new BufferedOutputStream(new DataOutputStream(downloadSocket.getOutputStream()));
			
			uploadSocket = new Socket(address, uploadPort);
			ubis = new BufferedInputStream(new DataInputStream(uploadSocket.getInputStream()));
			ubos = new BufferedOutputStream(new DataOutputStream(uploadSocket.getOutputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		new NetDisk("dong");
	}
}

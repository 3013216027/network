/**
 * 
 */
package backend;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.io.*;

/**
 * @date 2016Äê6ÔÂ28ÈÕ
 * @author zhengdongjian@tju.edu.cn
 *
 */
public class DownloadServer {
	final static String address = "127.0.0.1";
	final static int downloadPort = 3222;
	static int downloadCount = 0;
	
	public DownloadServer() {
		try {
			ServerSocket server = new ServerSocket(downloadPort);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						server.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			while (true) {
				Socket socket = server.accept();
				System.out.println(LocalTime.now().toString() + ": a new connection with DownloadServer!");
				new FileSender(socket, ++downloadCount).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		new DownloadServer();
	}
}

class FileSender extends Thread {
	final static int BUFFERSIZE = 512; //buffer size
	
	// storage directory
	final private static String storageDirectory = "storage/";
	
	private Socket socket = null;
	private byte[] buffer = new byte[BUFFERSIZE]; //buffer array
	@SuppressWarnings("unused")
	private int cid = 0;
	private String username;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	FileSender(Socket socket, int cid) {
		this.socket = socket;
		this.cid = cid;
		try {
			dis = new DataInputStream(this.socket.getInputStream());
			dos = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		/**
		 * firstly, we broadcast the file list of him
		 */
		pushFileList();
		
		/**
		 * then, we can get the filename that the user want, and send it out :)
		 */
		pop();
	}
	
	/**
	 * onvert filename(String) to buffer(Byte[]), padding with 512B
	 * @param filename String
	 */
	private void parseString(String filename) {
		while (filename.length() < BUFFERSIZE) {
			filename += ' '; 
		}
		buffer = filename.getBytes();
	}
	private void pushFileList() {
		try {
			int length = dis.read(buffer, 0, BUFFERSIZE);
			if (length == -1) {
				System.out.println("read username failed before pushFileList!");
			}
			username = new String(buffer).trim();
			System.out.println("parse username = " + username + " before pushFileList");
			
			String dir = storageDirectory + username + "/";
			//if the dir is not exist, create it now
			File fDir = new File(dir);
			if (!fDir.exists()) {
				fDir.mkdirs();
			}
			//ArrayList<String> filenames = new ArrayList<String>();
			File[] fileList = fDir.listFiles();
			int count = 0;
			for (File file: fileList) {
				if (file.isFile()) {
					++count;
				}
			}

			String tmp = String.valueOf(count);
			while (tmp.length() < BUFFERSIZE) {
				tmp += ' ';
			}
			buffer = tmp.getBytes();
			dos.write(buffer, 0, BUFFERSIZE);
			dos.flush();
			
			for (File file: fileList) {
				if (file.isFile()) {
					parseString(file.getName());
					dos.write(buffer, 0, BUFFERSIZE);									
				}
			}
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void pop() {
		try {
			int length = dis.read(buffer, 0, BUFFERSIZE);
			if (length == -1) {
				System.out.println("read filename failed while pop!");
			}
			dis.close();
			
			System.out.println("dos == null ? " + dos == null);
			String filename = storageDirectory + username + "/" + new String(buffer).trim();
			DataInputStream fs = new DataInputStream(new FileInputStream(new File(filename)));
			while ((length = fs.read(buffer)) != -1) {
				System.out.println("length = " + length);
				dos.write(buffer, 0, length);
			}
			dos.flush();
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
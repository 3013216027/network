/**
 * 
 */
package backend;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.io.*;

/**
 * @date 2016Äê6ÔÂ27ÈÕ
 * @author zhengdongjian@tju.edu.cn
 *
 */

public class Server {
	final static String address = "127.0.0.1";
	final static int uploadPort = 2333;
	static int uploadCount = 0;
	
	public Server() {
		try {
			ServerSocket server = new ServerSocket(uploadPort);
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
				(new FileReceiver(socket, ++uploadCount)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(LocalTime.now().toString() + ": upload server startup!");
	}
	public static void main(String args[]) {
		new Server();
	}
}

class FileReceiver extends Thread {
	final static int BUFFERSIZE = 512; //buffer size
	
	// storage directory
	final private static String storageDirectory = "storage/";
	
	@SuppressWarnings("unused")
	private Socket socket = null;
	private byte[] buffer = new byte[BUFFERSIZE]; //buffer array
	private DataInputStream dis = null;
	private FileOutputStream fos = null;
	private int cid = 0;
	
	FileReceiver(Socket socket, int cid) {
		this.socket = socket;
		this.cid = cid;
		try {
			this.dis = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		int length = 0;
		//the first 512B specific the sub-directory & filename
		try {
			//let's use a time-alive connection here :)
			while (true) {
				length = dis.read(buffer, 0, BUFFERSIZE);
				if (length == -1) {
					System.out.println("Error: no filename specific with connection #" + cid);
				}
				String filename = storageDirectory + new String(buffer).trim();
				System.out.println("filename = " + filename);
				fos = new FileOutputStream(new File(filename));
				//then we read the file size
				length = dis.read(buffer, 0, BUFFERSIZE);
				if (length == -1) {
					System.out.println("Error when read the file length of connection #" + cid);
				}
				int fileSize = Integer.parseInt(new String(buffer).trim());
				System.out.println("file size = " + fileSize);
				while (fileSize > 0) {
					length = dis.read(buffer, 0, Math.min(BUFFERSIZE, fileSize));
					if (length == -1) {
						System.out.println("#" + cid + ": no more data transfered!");
						break;
					}
					fileSize -= length;
					fos.write(buffer, 0, length);
					System.out.println("fileSize rest = " + fileSize);
				}
				fos.flush();
				fos.close();				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
/* **********************************************

  File Name: Client.java

  Author: zhengdongjian@tju.edu.cn

  Created Time: Fri 22 Apr 2016 11:26:38 AM CST

*********************************************** */

import java.io.*;
import java.util.*;
import java.net.*;

public class Client {
	public final static String serverAddress = Server.address;
	public final static int serverPort = Server.port;
	
	private final static boolean DEBUG = true;

	public static void main(String args[]) throws Exception {
		if (args.length > 0) {
			if (DEBUG) {
				System.out.println("args[0] = |" + args[0] + "|");
			}
			if (args.length == 1 && args[0].equals("-")) {
				System.out.println("Connection established! Send data from stdin...");
				(new FileSender()).start(); //read data from stdin
			} else {
				for (int i = 0; i < args.length; ++i) {
					if (DEBUG) {
						System.err.println("read from file " + args[i]);
					}
					(new FileSender(args[i])).start();
				}
			}
		} else {
			//show help
			String helpInformation = "\n\tUsage:\n";
			helpInformation += "\t    java Client -\n";
			helpInformation += "\tor\n";
			helpInformation += "\t    java Client file1 file2 ...\n\n";
			helpInformation += "\twhere \"-\"(dash) stand for stdin,\n";
			helpInformation += "\tand \"file1 file2 ...\" are the files you want to send.\n";
			System.out.println(helpInformation);
		}
	}
}

class FileSender extends Thread {
	final static int MAX = 4096; //default buffer size: 4K

	byte[] buffer = new byte[MAX]; //buffer

	Socket socket = null;
	BufferedInputStream bis = null;
	BufferedOutputStream bos = null;

	FileSender() {
		try {
			socket = new Socket(Client.serverAddress, Client.serverPort);
			bis = new BufferedInputStream(new DataInputStream(System.in));
			bos = new BufferedOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	FileSender(String filename) {
		this();
		try {
			bis = new BufferedInputStream(new DataInputStream(new FileInputStream(filename)));
		} catch (Exception e) {
			e.printStackTrace();
			//Thread.currentThread().interrupt();
		}
	}

	public void run() {
		try {
			int length;
			if (bis != null) {
				while ((length = bis.read(buffer)) != -1) {
					if (bos != null) {
						bos.write(buffer, 0, length);
					}
				}
				bis.close();
			}
			if (bos != null) {
				bos.flush();
				bos.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

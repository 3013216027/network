/* **********************************************

  File Name: Server.java

  Author: zhengdongjian@tju.edu.cn

  Created Time: Fri 22 Apr 2016 10:11:18 AM CST

*********************************************** */

import java.util.*;
import java.io.*;
import java.time.*;
import java.net.*;

/**
 * The server receive file from client(s)
 * Listen to port 2333.
 */
public class Server {
	public static String address = "127.0.0.1";
	public static int port = 2333;
	static int sum = 0;

	private static boolean parsePort(String port) {
		int res = -1;
		try {
			res = Int.parseInt(port);
		} catch (NumberFormatException e) {
			res = -1;
		}
		return res != -1 && res > 0 && res <= 65535;
	}

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			if (parsePort(args[0])) {
				port = Int.toString(args[0]);
			} else {
				System.out.println("port format error! Use port " + port + " instead...");
			}
		}
		ServerSocket server  = new ServerSocket(port);
		System.out.println((LocalTime.now()).toString() + ": Listening to " + address + ":" + port);

		while (true) {
			Socket socket = server.accept();
			System.out.println("Connection #" + (++sum) + " building up...");
			(new FileReceiver(socket, sum)).start();
		}
	}
}

class FileReceiver extends Thread {
	final static int MAX = 4096; //buffer size: 4k
	final static int MOD = 1000007; //mod for getting random integer = =
	final static Random randomer = new Random(); //random integer generator
	final static String storageDirectory = "storage/"; //default storage directory

	private final static boolean DEBUG = true; //debug define

	private static boolean storageTest() throws Exception {
		File fileTest = new File(storageDirectory);
		if (!fileTest.exists()) {
			try {
				if (DEBUG) {
					System.err.println("[" + (LocalTime.now()).toString() + "] DEBUG: "
						+ "storage directory not exists, touching dir " + storageDirectory + " now...");
				}
				fileTest.mkdir();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			if (!fileTest.exists()) {
				throw new Exception("Failed to create storage directory :(");
			}
		}
		return true;
	}

	/* allocate memory */
	Socket socket = null; //connection socket
	byte[] buffer = new byte[MAX]; //buffer
	DataInputStream dis = null; //input stream from socket
	FileOutputStream fos = null; //file output stream for storing file

	/* * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * some static functions to make life easier o.O
	 * * * * * * * * * * * * * * * * * * * * * * * * * * */

	/**
	 * we need a random string for filename,
	 * or use Server.sum(the counter) for naming,
	 * or use something like base64 encoding..
	 */
	/**
	 * convert integer to formatted string, like 4 to "04", 2016 to "2016"
	 * As we'd like string like "20160422094825", which is "yyyymmddHHMMSS"
	 * we can get it by unix command: date +%Y%m%d%H%M%S
	 */
	static String is(int x) {
		String str = Integer.toString(x);
		return str.length() < 2 ? '0' + str : str;
	}
	static String now() {
		LocalDateTime a = LocalDateTime.now(); //LocalDateTime is only supportted by Java 8 now
		return is(a.getYear()) + is(a.getMonthValue()) + is(a.getDayOfMonth())
			+ is(a.getHour()) + is(a.getMinute()) + is(a.getSecond());
	}

	/**
	 * return a random string, based on system clock and a random integer
	 */
	static String random() {
		return now() + '_' + randomer.nextInt(MOD);
	}

	//make the default constructor private to fobbiden it :)
	private FileReceiver() {}

	/**
	 * constructor the one we would use~
	 * @param socket: Socket to handle
	 * @param cid: the connection id
	 */
	FileReceiver(Socket socket, int cid) {
		this.socket = socket;
		try {
			this.dis = new DataInputStream(socket.getInputStream());
			//String filename = storageDirectory + random();
			String filename = storageDirectory + "file" + cid;
			if (storageTest()) {
				this.fos = new FileOutputStream(filename);
				System.out.println("Saving data from connection #" + cid + " to: " + filename);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * thread core:
	 * we continuously read data from socket into and write it to the output stream
	 */
	public void run() {
		int length;
		try {
			/* dis.read(buffer) or dis.read(buffer, 0, buffer.length) */
			while ((length = dis.read(buffer)) != -1) {
				if (fos != null) {
					fos.write(buffer, 0, length);
				}
			}
			if (fos != null) {
				fos.flush();
				fos.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

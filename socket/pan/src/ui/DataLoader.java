/**
 * 
 */
package ui;

import java.io.*;
import java.util.*;

/**
 * @date 2016Äê6ÔÂ27ÈÕ
 * @author zhengdongjian@tju.edu.cn
 *
 */
public class DataLoader {
	private final boolean debug = Checker.debug;
	final String dataFile = "users.txt";
	Scanner input = null;
	HashMap<String, String> map = new HashMap<String, String>();
	
	public Boolean open() {
		try {
			//File file = new File(dataFile);
			//System.out.println(file.getAbsolutePath());
			input = new Scanner(new BufferedInputStream(new FileInputStream(new File(dataFile))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (input != null) {
			bufferUserList();
			input.close();
			return true;
		} else {
			return false;
		}
	}
	
	private void bufferUserList() { 
		map.clear();
		String line;
		while (input.hasNextLine()) {
			line = input.nextLine();
			String[] pair = line.split(" ");
			map.put(pair[0], pair[1]);
		}
	}
	
	/**
	 * add a user
	 * @param user
	 * @param password
	 * @return
	 */
	public Boolean insert(String user, String password) {
		if (debug) {
			System.out.println("insert [" + user + ", " + password + "]");
		}
		if (map.containsKey(user)) {
			return false; //the username already registered!
		} else {
			map.put(user,  password);
			return true;
		}
	}
	
	/**
	 * query if there exists the specific <user>
	 * @param user		the user name
	 * @return			if the input is correct
	 */
	public Boolean query(String user) {
		if (debug) {
			System.out.println("query [" + user + "]");
		}
		return map.containsKey(user);
	}
	
	/**
	 * query if there exists the specific <user, passwd>
	 * @param user		the user name
	 * @param password	the password
	 * @return			if the input is correct
	 */
	public Boolean query(String user, String password) {
		if (debug) {
			System.out.println("query [" + user + ", " + password + "]");
		}
		return map.containsKey(user) && map.get(user).equals(password);
	}
	
	/**
	 * delete the specific user
	 * @param username
	 * @return if the operation is successful
	 */
	public Boolean delete(String username) {
		if (debug) {
			System.out.println("delete user [" + username + "]");
		}
		if (map.containsKey(username)) {
			map.remove(username);
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean update(String username, String password) {
		if (debug) {
			System.out.println("update [" + username + ", " + password + "]");
		}
		if (map.containsKey(username)) {
			map.replace(username, password);
			return true;
		} else {
			return false;
		}
	}
	
	
	
	/**
	 * sync data back into database file
	 */
	public void close() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(dataFile))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (Map.Entry<String, String> kv : map.entrySet()) {
			if (debug) {
				System.out.println("write [" + kv.getKey() + ", " + kv.getValue() + "]");
			}
			pw.println(kv.getKey() + " " + kv.getValue());
		}
		pw.close();
	}
}

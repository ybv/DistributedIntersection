
import java.net.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.TreeSet;
import java.io.*;

public class SocketClient {
	private static HashSet<String> h = new HashSet<String>();
	public static void main(String[] args) throws IOException {

		String host = "localhost";
		int port = 19920;
		String s = "";
		NavigableSet<Integer> navigableSet = new TreeSet<Integer>();
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter integers to populate the dataset, press a return after each element -----When done press #");
		System.out.println("Example:"+"\n"+"1"+"\n"+"2"+"\n"+"#"+"\n");
		while(true){
		
		 s = bufferRead.readLine();
		if("#".equals(s)){
			break;
		}
		int dataset = Integer.parseInt(s);
		navigableSet.add(dataset);
		}
		StringBuffer instr = new StringBuffer();
		String TimeStamp;
		
	
		System.out.println("SocketClient initialized");
		try {
		
			InetAddress address = InetAddress.getByName(host);
			Socket connection = new Socket(address, port);
			BufferedOutputStream bos = new BufferedOutputStream(connection.
					getOutputStream());
			PrintWriter out2 = new PrintWriter(connection.getOutputStream(), true);
			TimeStamp = new java.util.Date().toString();
		
			StringBuffer intersectiondata = new StringBuffer();
			Iterator<Integer> iterator = navigableSet.iterator();
			while(iterator.hasNext()){
				int m =iterator.next();
				intersectiondata.append(m);
				intersectiondata.append(" ");
		
			}
			String process2 = intersectiondata+ "$";
	
			out2.write(process2);
			out2.flush();
		}
		catch (IOException f) {
			System.out.println("IOException: " + f);
		}
		catch (Exception g) {
			System.out.println("Exception: " + g);
		}
	}
}

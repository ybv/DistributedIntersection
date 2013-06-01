
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Protocol1Server implements Runnable {

	private Socket connection;
	private String TimeStamp;
	private int ID;
	private HashSet<String> h = new HashSet<String>();
	HashSet<String> h2 = new HashSet<String>();
	private static long startTime;
	private static final StringBuffer allprocessdata = new StringBuffer();
	private static final NavigableSet<Integer> init_incomingset = new TreeSet<Integer>();
	private static final NavigableSet<Integer> incomingset = new TreeSet<Integer>();
	private static  long total=0;
	
	public static void main(String[] args) {
		int port = 19920;
		int count = 0;
		try{
			ServerSocket socket1 = new ServerSocket(port);
			System.out.println("Protocol1Server Initialized");
			while (true) {
				Socket connection = socket1.accept();
				Runnable runnable = new Protocol1Server(connection, ++count);
				Thread thread = new Thread(runnable);
				thread.start();
				 startTime = System.currentTimeMillis();  
			}
		}
		catch (Exception e) {}
	}
	Protocol1Server(Socket s, int i) {
		this.connection = s;
		this.ID = i;
	}

	@Override
	public void run() {

		try{
			BufferedInputStream is2 = new BufferedInputStream(connection.getInputStream());
			InputStreamReader isr = new InputStreamReader(is2);
			
			int character;
			StringBuffer process = new StringBuffer();
			if (ID==1){
				System.out.println("number of clients "+ ID);
				StringBuffer proc = new StringBuffer();
				System.out.println("Only one client to read from; set intesection is :");
				while((character = isr.read()) != '$') {
					
				
					proc.append((char)character);
				
				}
			String[] splits = proc.toString().split(" ");
			for (String m : splits){
				init_incomingset.add(Integer.parseInt(m));
			}
				
				System.out.println(init_incomingset.toString()+" is the incoming set");
				System.out.println(proc.toString());
			}
			else{
				System.out.println("number of clients "+ ID);
				int clients= ID;
		
				StringBuffer proc = new StringBuffer();
			
				while((character = isr.read()) != '$') {
					proc.append((char)character);
				}
				String[] splits = proc.toString().split(" ");
				for (String m : splits){
					incomingset.add(Integer.parseInt(m));
				}
				System.out.println(incomingset.toString()+" is the incoming set");
				boolean a =init_incomingset.retainAll(incomingset);
			if(init_incomingset.size()==0){
				System.out.println("There is no intersection in the given sets of data.");
			}else{
				System.out.println("Bingo! The intersection  exists and the set is "+init_incomingset.toString());
			}
			incomingset.clear();
			}
			long estimatedTime = System.currentTimeMillis() - startTime;
			System.out.println("Time taken to compute 'this' intersection, in milliseconds "+ estimatedTime);
			total += estimatedTime;
			TimeStamp = new java.util.Date().toString();
			System.out.println("Time taken to compute the total intersection of all client data sets till now , in milliseconds "+total );
			String returnCode = "Protocol1Server repsonded at "+ TimeStamp + (char) 13;
			
			BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
			osw.write(returnCode);
			osw.flush();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		finally {
			try {
				connection.close();
			}
			catch (IOException e){}
		}
	}
}


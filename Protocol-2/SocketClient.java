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
		int port = 19921;
		StringBuffer instr = new StringBuffer();
		
		String TimeStamp;
		String s = "";
		//NavigableSet<Integer> navigableSet = new TreeSet<Integer>(Arrays.asList(-10,25,69,40,256));
		//NavigableSet<Integer> navigableSet = new TreeSet<Integer>(Arrays.asList(396,256,25,75,66));
		NavigableSet<Integer> navigableSet = new TreeSet<Integer>();
		//NavigableSet<Integer> navigableSet = new TreeSet<Integer>(Arrays.asList(256,257,-256,10,25));

		Iterator iterator = navigableSet.iterator();
		System.out.println("Client works!");
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

		try {

			InetAddress address = InetAddress.getByName(host);
			Socket connection = new Socket(address, port);

			TimeStamp = new java.util.Date().toString();
			StringBuffer process = new StringBuffer();
			process.append("RANGE:");
			process.append(navigableSet.first());
			process.append(":");
			process.append(navigableSet.last());

			String process2 = process.toString()+"$";
			//System.out.println("range string"+ process.toString());

			BufferedOutputStream bos = new BufferedOutputStream(connection.
					getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");
			PrintWriter out = new PrintWriter(connection.getOutputStream(), true);

			out.print(process2);
			out.flush();

			while(true){
				BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
				InputStreamReader isr = new InputStreamReader(is); 
				StringBuffer serverresp = new StringBuffer();

				int messagefromserver;
				//System.out.println("Response from server:");

				while ( (messagefromserver = isr.read())!='%') {
					try {
						serverresp.append((char)messagefromserver);
					} catch (NumberFormatException e) {
						System.out.println("This is not a number");
						System.out.println(e.getMessage());
					}
				}


				PrintWriter out2 = new PrintWriter(connection.getOutputStream(), true);
				//serverresp.append(":");
				serverresp.append("$");
				System.out.println("Server's Response: "+ serverresp);
				String[] servermessagesplit= serverresp.toString().split(":");
				int randomnumfromserver = 0;


				

				if(servermessagesplit[0].equals("RANDOM")){
					//	Thread.sleep(1000);
					randomnumfromserver = Integer.parseInt(servermessagesplit[1]);
					//write code for equals case
					if(navigableSet.contains(randomnumfromserver)){
						System.out.println("we have found an element that is equal to the random number ");
						String intersectingat=	"&SUCC"+":"+Integer.toString(randomnumfromserver)+":"+"PRED"+":"+Integer.toString(randomnumfromserver)+"$"; 
						Thread.sleep(100);
						out.print(intersectingat);
						out.flush();	
					}
					else{
						int succ = navigableSet.higher(Integer.parseInt(servermessagesplit[1]));
						//System.out.println(z);
						int pred = navigableSet.lower(Integer.parseInt(servermessagesplit[1]));
						//System.out.println(w);
						System.out.println("Sending out successor and predecessor values");
						String succpred = "&SUCC"+":"+Integer.toString(succ)+":"+"PRED"+":"+Integer.toString(pred)+"$"; 
						try{
							Thread.sleep(100);
							out.print(succpred);
							
							out.flush();
							//	System.out.println("this happened");
						}catch(Exception e4){
							System.out.println(e4.getStackTrace());
						}
					}
				}

				//	osw.flush();
				//out.close();
				//	isr.close();
				//	out2.close();
				// connection.close();
			}}
		catch (IOException f) {
			System.out.println("IOException: " + f);
		}
		catch (Exception g) {
			System.out.println("Exception: " + g);
		}
	}
}

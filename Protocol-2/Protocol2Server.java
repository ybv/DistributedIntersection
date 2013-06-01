import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Protocol2Server implements Runnable {
	private static final StringBuffer intersectiondata = new StringBuffer();
	public static boolean servflag =true;
	private Socket connection;
	private String TimeStamp;
	private static int rangemin =0;
	private static int rangemax= 0 ;
	static int succ_count=0;;
	static int x=0;
	static int intersectioncount =0;
	
	private static ArrayList<Integer> intersectionarray= new ArrayList<Integer>();
	static int readcount=0;
	private static NavigableSet<Integer> intersection= new TreeSet<Integer>();
	private static int ID;
	private static int ID1;

	private static ArrayList<Integer> reprangemin= new ArrayList<Integer>();
	private static ArrayList<Integer> reprangemax= new ArrayList<Integer>();

	private static ArrayList<Integer> activeranges = new ArrayList<Integer>();
	private static int count =0;
	private static NavigableSet<Integer> allsucvals= new TreeSet<Integer>();
	private static NavigableSet<Integer> allpredvals= new TreeSet<Integer>();
	private static ArrayList<Socket> conn = new ArrayList<Socket>();
	private HashSet<String> h = new HashSet<String>();
	HashSet<String> h2 = new HashSet<String>();
	private static ArrayList<Integer> randmarray= new ArrayList<Integer>();
	private static NavigableSet<Integer> uprangevals= new TreeSet<Integer>();
	private static NavigableSet<Integer> lowrangevals = new TreeSet<Integer>();
	private static NavigableSet<Integer> allrangevals = new TreeSet<Integer>();
	private static long startTime;
	private static final StringBuffer allprocessdata = new StringBuffer();
	private static ServerSocket socket1 ;
	private static int min =0;
	private static int max =0;
	private static  long total=0;
	public static void main(String[] args) {
		int port = 19921;
		int count = 0;

		try{

			socket1 = new ServerSocket(port);
			System.out.println("Protocol2Server Initialized");
			System.out.println("Please input the number of clients");
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String s = bufferRead.readLine();
			ID1 = Integer.parseInt(s);
			while (true) {

				Socket connection = socket1.accept();
				Runnable runnable = new Protocol2Server(connection, ++count);
				Thread thread = new Thread(runnable);
				thread.start();
				
				conn.add(connection);

			
			}
		}
		catch (Exception e) {}
	}
	Protocol2Server(Socket s, int i) {
		this.connection = s;
		this.ID = i;
	}

	@Override
	public void run() 
	{
		try {
			while(true){

				BufferedInputStream is;
				String returnCode ="";
				is = new BufferedInputStream(connection.getInputStream());

				InputStreamReader isr = new InputStreamReader(is);

				StringBuffer process = new StringBuffer();
				int messagefromclient ;
				startTime = System.currentTimeMillis();  
				while((messagefromclient = isr.read()) != '$') 
				{
					process.append((char)messagefromclient);
				}
				System.out.println("Values received from client"+process);
				String[] clientmessagesplit= process.toString().split(":");
				if(clientmessagesplit[0].equals("RANGE"))
				{

					uprangevals.add(Integer.parseInt(clientmessagesplit[1]));
					lowrangevals.add(Integer.parseInt(clientmessagesplit[2]));
					rangemin= uprangevals.last();
					rangemax = lowrangevals.first();
					allrangevals.add(rangemin);
					allrangevals.add(rangemax);
					ID1--;
					if(ID1==0){
						System.out.println(" Server's range "+ rangemin+ " and " + rangemax);
						for( int i=rangemin; i<=rangemax; i++){
							activeranges.add(i);

						}

					//	System.out.println("Array list initialised");
						returnCode = random_number_generator(rangemin, rangemax);
						System.out.println("first return code  "+returnCode.toString());
						for(int i = 0; i < conn.size(); i++) 
						{

							PrintWriter out2 = new PrintWriter(connection.getOutputStream(), true);
							out2 = new PrintWriter(conn.get(i).getOutputStream(), true);
							out2.println(returnCode);
							out2.flush();
							//Thread.sleep(1000);
							//System.out.println("connections111====== "+conn.get(i));

						}
					}
				}
				if(clientmessagesplit[0].equals("&SUCC"))
				{


				//	System.out.println("Inside SUCC");
					//System.out.println("client------------"+connection.toString());
					//System.out.println("clientmessage and rangemax"+clientmessagesplit[1]+" "+rangemax);
					if(clientmessagesplit[1].equals(clientmessagesplit[3])){
						allsucvals.add(Integer.parseInt(clientmessagesplit[1]));
						allpredvals.add(Integer.parseInt(clientmessagesplit[3]));

					}

					if(Integer.parseInt(clientmessagesplit[1])>rangemax)
					{
						//System.out.println("succ values modified in succ if"+connection.toString());
						allsucvals.add(rangemax);
						//	System.out.println("succ values added in succ if after"+connection.toString());
						succ_count++;
					}
					else
					{
								//System.out.println("succ value added before in else"+connection.toString());
						allsucvals.add(Integer.parseInt(clientmessagesplit[1]));
						//	System.out.println("values on allsuccvals inside else "+allsucvals.toString());
						//System.out.println("succ value added after in else"+connection.toString());
						succ_count++;
					}


					if(Integer.parseInt(clientmessagesplit[3])<rangemin)
					{
						//System.out.println("pred values modified------");
						allpredvals.add(rangemin);
					}
					else
					{
						//System.out.println("pred values added");
						allpredvals.add(Integer.parseInt(clientmessagesplit[3]));
					}
					//System.out.println("ID value is"+" "+ID);
					//System.out.println("allsucvals size="+allsucvals.size());
					//System.out.println("succcount="+succ_count);
					if(succ_count==ID){
						
						succ_count=0;
						System.out.println("after receiving all clients succ values");
						min= allpredvals.first();
						System.out.println("all succ values"+allsucvals.toString());
						System.out.println("all pred values"+allpredvals.toString());
						max= allsucvals.last();
						System.out.println("Min and Max values"+min+" "+max);

						allrangevals.add(min);
						allrangevals.add(max);
						allpredvals.clear();
						allsucvals.clear();
						count =ID;
						//System.out.println("count and ID values"+count+" and "+ ID);
					}
					if(count==ID)
					{
						//System.out.println("count and ID values "+ count+ "and "+ ID);
						//System.out.println("Inserted all clients succ values");

						rangemin= uprangevals.last();
						rangemax = lowrangevals.first();
					//	System.out.println("Argument values"+min+max+rangemin+rangemax);
						returnCode = random_number_generator2(min,(max),(rangemin),(rangemax));
						String[] randomsplit= returnCode.split(":");
					//	System.out.println("retuncode" +returnCode);
						if(returnCode.equals(" ")){
							System.out.println("Terminating ther server program");
							if(intersectionarray.size()==0){
								System.out.println("No intersection in the data sets ");
							}
							else{
							System.out.println("Bingo! the Intersection set among all the datasets is "+intersectionarray.toString());
							socket1.close();
							long estimatedTime = System.currentTimeMillis() - startTime;
							total += estimatedTime;
							System.out.println("Time taken to compute the intersection between all client data sets, in milliseconds "+ total);
						
							TimeStamp = new java.util.Date().toString();
							System.out.println("Connection closed");
							}	break;
						}
						System.out.println("return code "+returnCode.toString());
						
					
						for(int i = 0; i < conn.size(); i++) 
						{

							PrintWriter out3 = new PrintWriter(connection.getOutputStream(), true);
							out3= new PrintWriter(conn.get(i).getOutputStream(), true);
							out3.println(returnCode);
							out3.flush();
							//Thread.sleep(1000);
							System.out.println("connections ============ "+conn.get(i));

						}
						
						count=0;
					}

				}

				readcount++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String random_number_generator2(int elimin, int elimax, int rangemin, int rangemax) {
		for(int i=elimin+1;i<elimax;i++){
			activeranges.remove((Object)i);
		}
		System.out.println("ArrayList before manipulations"+activeranges.toString());
		if( (rangemin==elimin) && (rangemin ==elimax) && (rangemin==randmarray.get(randmarray.size()-1)))
		{
			System.out.println("Inside rangemin elimination"+":"+"rangemin"+rangemin+":"+"random"+randmarray.get(randmarray.size()-1));
			intersectionarray.add(rangemin);
			activeranges.remove((Object)rangemin);
		}
		
		else if( (rangemax==elimin) && (rangemax ==elimax) && (rangemax==randmarray.get(randmarray.size()-1)))
		{
			System.out.println("Inside rangemax elimination"+":"+"rangemax"+rangemax+":"+"random"+randmarray.get(randmarray.size()-1));
			
			intersectionarray.add(rangemax);
			activeranges.remove((Object)rangemax);
		}

		else	if(elimax==elimin)
		{

		//	System.out.println("Intersection element removed");
			intersectionarray.add(elimin);
			activeranges.remove((Object)elimin);
			//System.out.println("activeranges here:"+activeranges.toString());		
		}
		else	if((elimin!=elimax)&& (rangemin==elimin) && (rangemin==randmarray.get(randmarray.size()-1) ))
		{
			//System.out.println("Inside rangemax non intersection elimi");
			activeranges.remove((Object)elimin);
		//	System.out.println("activeranges here:"+activeranges.toString());
			

		}
		else if((elimin!=elimax)&& (rangemax==elimax)  && (rangemax==randmarray.get(randmarray.size()-1)) )
		{
			//System.out.println("Inside rangemax non intersection elimi");
			activeranges.remove((Object)elimax);
			//System.out.println("activeranges here:"+activeranges.toString());
			

		}
		else{
		//	System.out.println("didn't cover any case !!");
		}
		int activesize = activeranges.size();
		String returnCode ="";
		if(activesize==0)
		{
			returnCode=" ";
		}
		else
		{
		Random rand = new Random();
		int randomNum = rand.nextInt((activesize-1)-(0) + 1) + (0);
		System.out.println("index in shrinked array "+ randomNum);
		returnCode = "RANDOM"+":"+(activeranges.toArray()[randomNum])+":"+"%";
		randmarray.add((Integer) activeranges.toArray()[randomNum]);
		System.out.println("random values sent to client are"+randmarray.toString());
	//	System.out.println("random number inside random gen2"+returnCode);	
		}
		
		return returnCode;

	}



	private String random_number_generator(int x, int y) {
		String returnCode1="";
		Random rand = new Random();
		try
		{
		int randomNum = rand.nextInt((y)-(x) + 1) + (x);
		returnCode1 = "RANDOM"+":"+Integer.toString(randomNum)+":"+"%";
		randmarray.add(randomNum);
		}
		catch(Exception e)
		{
			System.out.println("Entered Data sets are invalid!");
			returnCode1=" ";
		}

		return returnCode1;
	}

}


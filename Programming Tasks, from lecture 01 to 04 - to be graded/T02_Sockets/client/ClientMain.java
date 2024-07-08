import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class ClientMain extends Thread{
	private static String num = null;
	private static String name = ";";
	private static boolean start = false;
	public static void main(String[] args) throws IOException {
		List<ClientMain> sockets = new ArrayList<>(4);
		for (int i = 6600; i < 6604; i++) {
			ClientMain c = new ClientMain(i);
			c.start();
			sockets.add(c);
		}
		
		//  TASK 2
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	do {
    		System.out.print("Select your name: ");
    		name = reader.readLine();
    	} while(name.contains(";"));
    	
    	// TASK 1
    	
		System.out.print("Select a number to increment: ");
		num = reader.readLine();
		System.out.println("Press enter to continue");
		reader.readLine();
		start = true;
	}
	
	private int port;
	public ClientMain(int port) {
		this.port = port;
	}
	
    private PrintWriter out;
    private BufferedReader in;

    private String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }
    private double sum(List<Long> v) {
    	long f = 0;
    	for(long d : v)
    		f+=d;
    	return f;
    }
    public void run() {
		try (Socket clientSocket = new Socket("127.0.0.1", port);){
			System.out.println("Started client on port "+port);
	        out = new PrintWriter(clientSocket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    	while(num == null)
	    		Thread.sleep(100);
	    	System.out.println(sendMessage(num+";"+name+";"+System.currentTimeMillis()));
	    	while(!start)
	    		Thread.sleep(100);
			
			//TASK 3
			
			Map<Integer, List<Long>> responseTimes = new TreeMap<>();
			
			for(int a = 0; a < 1000; a++)
				for(int i = 0; i < 8; i++) {
					int size = (int) Math.pow(2, i);
					if(responseTimes.get(size)== null) {
						responseTimes.put(size, new LinkedList<>());
					}
					//System.out.println("Sending "+size+"KiB data");
					long begin = System.currentTimeMillis();
					sendMessage("#".repeat(size * 1000));
					long end = System.currentTimeMillis();
					responseTimes.get(size).add(end-begin);
					//System.out.println("Received at "+(end-begin)+"ms");
				}
			for(Entry<Integer, List<Long>> e : responseTimes.entrySet()) {
				System.out.println("["+port+"] Average time for "+e.getKey()+"KiB its "+sum(e.getValue())/1000+"ms");
			}
			System.out.println("Closing socket");
			in.close();
	        out.close();
	        clientSocket.close();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

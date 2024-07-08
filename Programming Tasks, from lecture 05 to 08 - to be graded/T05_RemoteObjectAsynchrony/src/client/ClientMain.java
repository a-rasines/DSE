package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class ClientMain{

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		RequestHandler.setInstance(6600);
		while(true) {
			System.out.print(
				"Select a function to use:\n"+
				"1. Add log\n"+
				"2. Clear logs\n"+
				"3. Bulk add logs\n"+
				"4. Search logs\n"+
				"Type the number here: "
			);
			String res;
			switch(reader.readLine()) {
				case "1":
					System.out.println("Type the log's content: ");
					Proxy.addLog(reader.readLine());
					break;
				case "2":
					res = "a";
					boolean isNumber = true;
					while (res.strip().equals("") || isNumber) {
						System.out.println("Type the number of logs to remove: ");
						res = reader.readLine();
						try {
							int temp = Integer.parseInt(res);
							if(!(isNumber = temp <= 0))
								Proxy.clearLogs(temp);
						} catch (NumberFormatException e) {}
					}
					break;
				case "3":
					res = "a";
					List<String> logs = new LinkedList<>();
					while (!res.strip().equals("")) {
						System.out.println("Type the content of the log or leave it empty to finish: ");
						res = reader.readLine();
						if(!res.strip().equals(""))
							logs.add(res.strip());
						System.out.println("\n".repeat(10)+"Actual logs:"); logs.forEach(System.out::println);System.out.println("--------------------");
					}
					System.out.println();
					Proxy.bulkAdd(logs);
					break;
				case "4":
					System.out.println("Type the term to search: ");
					Proxy.searchLogs(reader.readLine());
					break;
				default:
					System.out.println("Please, select a valid option");
			}
		}
	}
}

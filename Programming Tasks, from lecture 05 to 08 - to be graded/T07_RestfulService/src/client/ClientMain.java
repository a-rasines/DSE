package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.ClientController.Response;
import domain.Doodle;
import domain.MeetingTime;

public class ClientMain {
	public static void main(String[] args) {
		try {
			ClientController.setServerHandler(new ServiceLocator("127.0.0.1", 8000));
			 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             while (true) {

            	System.out.println("Select an option:\n"
            					 + "1. Create Doodle\n"
            					 + "2. Get doodle by id\n"
            					 + "3. View your doodles\n"
            					 + "4. Delete doodle\n"
            					 + "5. Manage meeting times\n"
            					 + "6. Register in doodle\n"
            					 + "7. Deregister from doodle\n"
            					 + "8. Exit\n");
            	Doodle d;
            	switch(reader.readLine()) {
            		case "1":
            			System.out.println("\n".repeat(10));
            			System.out.println(ClientController.Doodles.createDoodle(new Doodle("temp")) == null?"Doodle succesfully created":"");
            			break;
            		case "2":
            			System.out.println("\n".repeat(10));
            			System.out.print("Write Doodle's id: ");
            			d = ClientController.Doodles.getDoodle(reader.readLine());
            			System.out.println("\n".repeat(10) + "------ Doodle -------");
            			System.out.println("Id: " + d.id);
            			System.out.println("MeetingTimes: ");
            			for (MeetingTime mt : d.getMeetingTimes())
            				System.out.println("\t"+mt.day+"/"+mt.month+"/"+mt.year+" From " +mt.startingHour + ":"+mt.startingMinute+" to " + mt.finishHour + ":" + mt.finishMinute+" Confirmations: " + mt.userCount);
            			System.out.println("Also accesible at: 127.0.0.1:8000/doodle/"+d.id);
            			System.out.println("---------------------");
            			System.out.print("Press enter to continue...");
            			reader.readLine();
            			System.out.println("\n".repeat(10));
            			break;
            		case "3":
            			System.out.println("\n".repeat(10));
            			for (Doodle d_ : ClientController.Doodles.getDoodles()) {
            				System.out.println("------ Doodle -------");
                			System.out.println("Id: " + d_.id);
                			System.out.println("MeetingTimes: ");
                			for (MeetingTime mt : d_.getMeetingTimes())
                				System.out.println("\t"+mt.day+"/"+mt.month+"/"+mt.year+" From " +mt.startingHour + ":"+mt.startingMinute+" to " + mt.finishHour + ":" + mt.finishMinute+" Confirmations: " + mt.userCount);
                			System.out.println("Also accesible at: 127.0.0.1:8000/doodle/"+d_.id);
                			System.out.println("---------------------");
            			}
            			System.out.print("Press enter to continue...");
            			reader.readLine();
            			System.out.println("\n".repeat(10));
            			break;
            		case "4":
            			System.out.println("\n".repeat(10));
            			for (Doodle d_ : ClientController.Doodles.getDoodles()) {
            				System.out.println("------ Doodle -------");
                			System.out.println("Id: " + d_.id);
                			System.out.println("MeetingTimes: ");
                			for (MeetingTime mt : d_.getMeetingTimes())
                				System.out.println("\t"+mt.day+"/"+mt.month+"/"+mt.year+" From " +mt.startingHour + ":"+mt.startingMinute+" to " + mt.finishHour + ":" + mt.finishMinute+" Confirmations: " + mt.userCount);
                			System.out.println("Also accesible at: 127.0.0.1:8000/doodle/"+d_.id);
                			System.out.println("---------------------");
            			}
            			System.out.print("\nWrite the doodle's id to delete: ");
            			System.out.println(ClientController.Doodles.deleteDoodle(reader.readLine()).message + "\n".repeat(10));
            			break;
            		case "5":
            			System.out.println("\n".repeat(10));
            			for (Doodle d_ : ClientController.Doodles.getDoodles()) {
            				System.out.println("------ Doodle -------");
                			System.out.println("Id: " + d_.id);
                			System.out.println("MeetingTimes: ");
                			for (MeetingTime mt : d_.getMeetingTimes())
                				System.out.println("\t"+mt.day+"/"+mt.month+"/"+mt.year+" From " +mt.startingHour + ":"+mt.startingMinute+" to " + mt.finishHour + ":" + mt.finishMinute+" Confirmations: " + mt.userCount);
                			System.out.println("Also accesible at: 127.0.0.1:8000/doodle/"+d_.id);
                			System.out.println("---------------------");
            			}
            			System.out.print("\nWrite the doodle's id to manage: ");
            			d = ClientController.Doodles.getDoodle(reader.readLine());
            			System.out.println("\n".repeat(10));
            			if(d != null) {
            				manage_meeting : {
	            				while (true){
	            					System.out.println("Select an option:\n"
	            									 + "1. Add meeting time\n"
	            									 + "2. Delete meeting time\n"
	            									 + "3. Finish");
	            					MeetingTime mt;
	            					switch (reader.readLine()) {
	            						case "1":
	            							mt = ClientController.Meeting.createMeeting(
	            									new MeetingTime(
	            										d.id, 
	            										input(reader, "Select initial hour: "), 
	            										input(reader, "Select initial minute: "), 
	            										input(reader, "Select final hour"), 
	            										input(reader, "Select final minute"), 
	            										input(reader, "Select day"), 
	            										input(reader, "Select month"), 
	            										input(reader, "Select year")
	            									)
	            								);
	            							if(mt != null) {
	            								d.addMeeting(mt);
	            								System.out.println("Meeting added: " + mt.toString());
	            								System.out.print("Press enter to continue...");
	            		            			reader.readLine();
	            							}
	            							System.out.println("\n".repeat(10));
	            							break;
	            						
	            						case "2":
	            							for (MeetingTime mt_ : d.getMeetingTimes())
	    	            						System.out.println(mt_.id + ". "+mt_.day+"/"+mt_.month+"/"+mt_.year+" From " +mt_.startingHour + ":"+mt_.startingMinute+" to " + mt_.finishHour + ":" + mt_.finishMinute+" Confirmations: " + mt_.userCount);
	    	            					System.out.print("Select a meeting to delete: ");
	    	            					try {
	    	            						boolean r = ClientController.Meeting.deleteMeeting(d.id, Integer.parseInt(reader.readLine()));
	    	            						if (r) {
	    	            							System.out.println("Meeting deleted");
	    	            							d = ClientController.Doodles.getDoodle(d.id);
	    	            						}
	    	            					} catch (NumberFormatException e) {}
	            							break;
	            							
	            						case "3":
	            							break manage_meeting;
	            					}
	            				}
	            			}
            			}
            			break;
            		case "6":
            			System.out.println("\n".repeat(10));
            			System.out.print("Insert Doodle's id: ");
            			d = ClientController.Doodles.getDoodle(reader.readLine());
            			if(d == null) break;
            			for (MeetingTime mt_ : d.getMeetingTimes())
    						System.out.println(mt_.id + ". "+mt_.day+"/"+mt_.month+"/"+mt_.year+" From " +mt_.startingHour + ":"+mt_.startingMinute+" to " + mt_.finishHour + ":" + mt_.finishMinute+" Confirmations: " + mt_.userCount);
    					System.out.print("Select a meeting to register in: ");
    					try {
    						boolean r = ClientController.MeetingPeople.register(d.id, Integer.parseInt(reader.readLine()));
    						if (r) {
    							System.out.println("Succesfully registered to meeting");
    						}
    					} catch (NumberFormatException e) {}
            			break;
            		case "7":
            			System.out.println("\n".repeat(10));
            			System.out.print("Insert Doodle's id: ");
            			d = ClientController.Doodles.getDoodle(reader.readLine());
            			if(d == null) break;
            			for (MeetingTime mt_ : d.getMeetingTimes())
    						System.out.println(mt_.id + ". "+mt_.day+"/"+mt_.month+"/"+mt_.year+" From " +mt_.startingHour + ":"+mt_.startingMinute+" to " + mt_.finishHour + ":" + mt_.finishMinute+" Confirmations: " + mt_.userCount);
    					System.out.print("Select a meeting to deregister from: ");
    					try {
    						boolean r = ClientController.MeetingPeople.deregister(d.id, Integer.parseInt(reader.readLine()));
    						if (r) {
    							System.out.println("Succesfully deregistered from meeting");
    						}
    					} catch (NumberFormatException e) {}
    					break;
            		case "8":
            			return;
            		default: {}
            	
            	}
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static int input(BufferedReader reader, String label) {
		while (true) {
			System.out.println(label);
			try {
				return Integer.parseInt(reader.readLine());
			} catch(NumberFormatException e) {} 
			  catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import com.google.gson.Gson;



public class Server_Final {
	int port = 3002;
	public static boolean isRunning = true;
	public List<ServerThread_Final> clients = new ArrayList<ServerThread_Final>();
	
	//We'll use a queue and a thread to separate our chat history
	Queue<String> messages = new LinkedList<String>();
	public GameState1 state = new GameState1();
	public static int ClientID = 0;
	public int balance = 500;
	Deck deck = new Deck();
	Hand hand = new Hand(deck);
	Client_Final client = new Client_Final();
	int playerCount = clients.size();
	
	public int getBalance() {
		return balance;
	}
	public synchronized int getNextId() {
		ClientID++;
		return ClientID;
	}
	public synchronized void toggleButton(Payload_Final payload) {
		if(state.isButtonOn1 && !payload.IsOn()) {
			state.isButtonOn1 = false;
			broadcast(payload);
		}
		else if (!state.isButtonOn1 && payload.IsOn()) {
			state.isButtonOn1 = true;
			broadcast(payload);
		}
	}
	private void start(int port) {
		this.port = port;
		startQueueReader();
		
		System.out.println("Waiting for client");
		try (ServerSocket serverSocket = new ServerSocket(port);) {
			while(Server_Final.isRunning) {
				try {
					Socket client = serverSocket.accept();
					System.out.println("Client connecting...");
					//Server thread is the server's representation of the client
					ServerThread_Final thread = new ServerThread_Final(client, this);
					thread.start();
					thread.setClientId(getNextId());
					//add client thread to list of clients
					clients.add(thread);
					System.out.println("Client added to clients pool");
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				isRunning = false;
				Thread.sleep(50);
				System.out.println("closing server socket");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	void startQueueReader() {
		System.out.println("Preparing Queue Reader");
		Thread queueReader = new Thread() {
			@Override
			public void run() {
				String message = "";
				try(FileWriter write = new FileWriter("chathistory.txt", true)){
					while(isRunning) {
						message = messages.poll();
						if(message != null) {
							message = messages.poll();
							write.append(message);
							write.write(System.lineSeparator());
							write.flush();
						}
						//sleep for a bit to let OS multi-task
						//since it's FIFO we don't need immediate polling
						sleep(50);
					}
				}
				catch(IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		queueReader.start();
		System.out.println("Started Queue Reader");
	}
	@Deprecated
	int getClientIndexByThreadId(long id) {
		for(int i = 0, l = clients.size(); i < l;i++) {
			if(clients.get(i).getId() == id) {
				return i;
			}
		}
		return -1;
	}
	public synchronized void broadcast(Payload_Final payload, String name) {
		String msg = payload.getMessage();
		payload.setMessage(
				//prepending client name to front of message
				(name!=null?name:"[Name Error]") 
				//including original message if not null (with a prepended colon)
				+ (msg != null?": "+ msg:"")
		);
		broadcast(payload);
	}
	public synchronized void broadcast(Payload_Final payload) {
		System.out.println("Sending message to " + clients.size() + " clients");
		
		Iterator<ServerThread_Final> iter = clients.iterator();
		while(iter.hasNext()) {
			ServerThread_Final client = iter.next();
			boolean messageSent = client.send(payload);
			if(!messageSent) {
				//if we got false, due to update of send()
				//we can assume the client lost connection
				//so let's clean it up
				iter.remove();
				System.out.println("Removed client " + client.getId());
			}
		}
	}
	//Broadcast given payload to everyone connected
	public synchronized void broadcast(Payload_Final payload, long id) {
		//let's temporarily use the index as the client identifier to
		//show in all client's chat. You'll see why this is a bad idea
		//when clients disconnect/reconnect.
		int from = getClientIndexByThreadId(id);
		String msg = payload.getMessage();
		payload.setMessage(
				//prepending client name to front of message
				(from>-1?"Client[" + from+"]":"unknown") 
				//including original message if not null (with a prepended colon)
				+ (msg != null?": "+ msg:"")
		);
		//end temp identifier (maybe this won't be too temporary as I've reused
		//it in a few samples now)
		broadcast(payload);
		
	}
	//Broadcast given message to everyone connected
	public synchronized void broadcast(String message, long id) {
		Payload_Final payload = new Payload_Final();
		payload.setPayloadType(PayloadType_Final.MESSAGE);
		payload.setMessage(message);
		broadcast(payload, id);
	}
	
	//ServerThread_Final currentPlayer = clients.get(clients.size()+1);
	//ServerThread_Final lastStart = clients.get(0);;
	//create a function ROUND that deals with the shuffling and dealing
	//when the cards have been displayed shuffle and then deal
	//create a parameter or function marking the need for another round, so when hands are displayed and winnings 
	//are given restart the round.
	public void round() {
		int players2 = 2;
		int players3 = 3;
		int players4 = 4;
		while(playerCount > 2){
			Deck deck = new Deck();
			if(hand.display()) {
				switch(playerCount) {
				case 2:
					for(int i = 0; i < players2; i++) {
						Hand hand = new Hand(deck);
					}
					break;
				case 3:
					for(int i = 0; i < players3; i++) {
						Hand hand = new Hand(deck);
					}
					break;
				case 4:
					for(int i = 0; i < players4; i++) {
						Hand hand = new Hand(deck);
					}
					break;
				}
			}
			
		/*for(int i = 0; i < clients.size(); i ++) {
			if(clients.get(i) != null ) {
				
			}
		}
		*/	
	}
}
	/*
	public int winnings() {
		if(hand.compareTo(hand) == -1) {
			clients.get(index of whoever won).getBalance() -= the loser's index and how ever much they lost
		}
		if(hand.compareTo(hand) == 1) {
			clients.get(index of who won)
		}
		
		return balance;
	}
	*/
	public void bust() {
		if(balance == 0) {			
			System.out.println("You have lost all your money, game over");
			System.out.println("Leave or stay to spectate");		
		}
	}
	
	public static int pot;
	public static int bigBlind = 10, smallBlind = 5;
	public static void blinds() {
		for(int i = 0; i <= 3; i++) {
			//clients.get(currentPlayer).getBalance() -= bigBlind;
			//clients.get(lastPlayer).getBalance() -= smallBlind;
		}
		pot += bigBlind +smallBlind;
	}
	public static void main(String[] args) {
		//let's allow port to be passed as a command line arg
		//in eclipse you can set this via "Run Configurations" 
		//	-> "Arguments" -> type the port in the text box -> Apply
		int port = 3001;//make some default
		if(args.length >= 1) {
			String arg = args[0];
			try {
				port = Integer.parseInt(arg);
			}
			catch(Exception e) {
				//ignore this, we know it was a parsing issue
			}
		}
		System.out.println("Starting Server");
		Server_Final server = new Server_Final();
		System.out.println("Listening on port " + port);
		server.start(port);
		System.out.println("Server Stopped");
	}
}
class GameState1{
	boolean isButtonOn1 = false;
	public boolean isButtonOn;
}
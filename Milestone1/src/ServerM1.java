import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.gson.Gson;


public class ServerM1 {
	int port = 3002;
	public static boolean isRunning = true;
	private List<ServerThreadM1> clients = new ArrayList<ServerThreadM1>();
	Queue<String> messages = new LinkedList<String>();
	private void start(int port) {
		this.port = port;
		startQueueReader();
		loadScore();
		saveScore(1000);
		System.out.println("Waiting for client");
		try (ServerSocket serverSocket = new ServerSocket(port);) {
			while(ServerM1.isRunning) {
				try {
					Socket client = serverSocket.accept();
					System.out.println("Client connecting...");
					ServerThreadM1 thread = new ServerThreadM1(client, this);
					thread.start();
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
	void loadScore() {
		try {
			Gson gson = new Gson();
			ScoreStateM1 ss = gson.fromJson(new FileReader("score.json"), ScoreStateM1.class);
			long s = (long) ss.scores.get(0).score;
			System.out.println("Loaded score: " + s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void saveScore(int score){
		ScoreStateM1 ss = new ScoreStateM1();
		ss.scores.add(new Score("Bob", 1000));
		ss.scores.add(new Score("Joe", 500));
		System.out.println(ss.toString());
		try(FileWriter writer = new FileWriter("score.json",false)){
			Gson gson = new Gson();
			writer.write(gson.toJson(ss));
			writer.flush();
		}
		catch(Exception e) {
			e.printStackTrace();
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
	public synchronized void broadcast(PayloadM1 payload, String name) {
		String msg = payload.getMessage();
		payload.setMessage(
				(name!=null?name:"[Name Error]") 
				+ (msg != null?": "+ msg:"")
		);
		broadcast(payload);
	}
	public synchronized void broadcast(PayloadM1 payload) {
		System.out.println("Sending message to " + clients.size() + " clients");
		//TODO record message
		storeInFile(payload.getMessage());
		Iterator<ServerThreadM1> iter = clients.iterator();
		while(iter.hasNext()) {
			ServerThreadM1 client = iter.next();
			boolean messageSent = client.send(payload);
			if(!messageSent) {
				iter.remove();
				System.out.println("Removed client " + client.getId());
			}
		}
	}
	public synchronized void broadcast(PayloadM1 payload, long id) {
		int from = getClientIndexByThreadId(id);
		String msg = payload.getMessage();
		payload.setMessage(
				(from>-1?"Client[" + from+"]":"unknown") 
				+ (msg != null?": "+ msg:"")
		);
		broadcast(payload);
		
	}
	public synchronized void broadcast(String message, long id) {
		PayloadM1 payload = new PayloadM1();
		payload.setPayloadType(PayloadTypeM1.MESSAGE);
		payload.setMessage(message);
		broadcast(payload, id);
	}
	void storeInFile(String message) {
		messages.add(message);
	}

	public static void main(String[] args) {
		int port = 3002;
		if(args.length >= 1) {
			String arg = args[0];
			try {
				port = Integer.parseInt(arg);
			}
			catch(Exception e) {
			}
		}
		System.out.println("Starting Server");
		ServerM1 server = new ServerM1();
		System.out.println("Listening on port " + port);
		server.start(port);
		System.out.println("Server Stopped");
	}
}
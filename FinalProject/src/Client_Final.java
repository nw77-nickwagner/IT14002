import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;


public class Client_Final {
	private Socket server;
	private OnReceive clickListener;
	public void registerClickListener(OnReceive listener) {
		this.clickListener = listener;
	}
	private OnReceive messageListener;
	public void registerMessageListener(OnReceive listener) {
		this.messageListener = listener;
	}
	private Queue<Payload_Final> toServer = new LinkedList<Payload_Final>();
	private Queue<Payload_Final> fromServer = new LinkedList<Payload_Final>();
	
	public static Client_Final connect(String address, int port) {
		Client_Final client = new Client_Final();
		client._connect(address, port);
		Thread clientThread =  new Thread() {
			@Override
			public void run() {
				client.start();
			}
		};
		clientThread.start();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return client;
	}
	private void _connect(String address, int port) {
		try {
			server = new Socket(address, port);
			System.out.println("Client connected");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void start() {
		if(server == null) {
			return;
		}
		System.out.println("Client Started");
		//listen to console, server in, and write to server out
		try(	ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());){
			Thread inputThread = new Thread() {
				@Override
				public void run() {
					try {
						while(!server.isClosed()) {
							Payload_Final p = toServer.poll();
							if(p != null) {
								out.writeObject(p);
							}
							else {
								try {
									Thread.sleep(8);
								}
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
					catch(Exception e) {
						System.out.println("Client shutdown");
					}
					finally {
						close();
					}
				}
			};
			inputThread.start();//start the thread
			
			//Thread to listen for responses from server so it doesn't block main thread
			Thread fromServerThread = new Thread() {
				@Override
				public void run() {
					try {
						Payload_Final p;
						//while we're connected, listen for payloads from server
						while(!server.isClosed() && (p = (Payload_Final)in.readObject()) != null) {
							//System.out.println(fromServer);
							//processPayload(fromServer);
							fromServer.add(p);
						}
						System.out.println("Stopping server listen thread");
					}
					catch (Exception e) {
						if(!server.isClosed()) {
							e.printStackTrace();
							System.out.println("Server closed connection");
						}
						else {
							System.out.println("Connection closed");
						}
					}
					finally {
						close();
					}
				}
			};
			fromServerThread.start();//start the thread
			
			
			Thread payloadProcessor = new Thread(){
				@Override
				public void run() {
					while(!server.isClosed()) {
						Payload_Final p = fromServer.poll();
						if(p != null) {
							processPayload(p);
						}
						else {
							try {
								Thread.sleep(8);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			};
			payloadProcessor.start();
			//Keep main thread alive until the socket is closed
			//initialize/do everything before this line
			while(!server.isClosed()) {
				Thread.sleep(50);
			}
			System.out.println("Exited loop");
			System.exit(0);//force close
			//TODO implement cleaner closure when server stops
			//without this, it still waits for input before terminating
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}
	}
	public void postConnectionData() {
		Payload_Final payload = new Payload_Final();
		payload.setPayloadType(PayloadType_Final.CONNECT);
		//payload.IsOn(isOn);
		toServer.add(payload);
	}
	public void doCheck(boolean isOn) {
		Payload_Final payload = new Payload_Final();
		payload.setPayloadType(PayloadType_Final.CHECK);
		toServer.add(payload);
	}
	public void doFold(boolean isOn) {
		Payload_Final payload = new Payload_Final();
		payload.setPayloadType(PayloadType_Final.FOLD);
		toServer.add(payload);
	}
	public void sendMessage(String message) {
		Payload_Final payload = new Payload_Final();
		payload.setPayloadType(PayloadType_Final.MESSAGE);
		payload.setMessage(message);
		toServer.add(payload);
	}
	private void processPayload(Payload_Final payload) {
		System.out.println(payload);
		String msg = "";
		switch(payload.getPayloadType()) {
		case CONNECT:
			msg = String.format("Client \"%s\" connected", payload.getMessage());
			System.out.println(msg);
			if(messageListener != null) {
				messageListener.onReceivedMessage(msg);
			}
			break;
		case DISCONNECT:
			msg = String.format("Client \"%s\" disconnected", payload.getMessage());
			System.out.println(msg);
			if(messageListener != null) {
				messageListener.onReceivedMessage(msg);
			}
			break;
		case MESSAGE:
			System.out.println(
					String.format("%s", payload.getMessage())
			);
			
			break;
		case STATE_SYNC:
			System.out.println("Sync");
			//break; //this state will drop down to next state
		case FOLD:		
			if(messageListener != null) {
				messageListener.onReceivedMessage(payload.getMessage());
			}
			break;
		case CHECK:
			if(messageListener != null) {
				messageListener.onReceivedMessage(payload.getMessage());
			}
			break;
		default:
			System.out.println("Unhandled payload type: " + payload.getPayloadType().toString());
			break;
		}
	}
	
	private void close() {
		if(server != null) {
			try {
				server.close();
				System.out.println("Closed socket");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		Client_Final client = new Client_Final();
		client.connect("127.0.0.1", 3001);
		try {
			//if start is private, it's valid here since this main is part of the class
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

interface OnReceive{
	void onReceivedSwitch(boolean isOn);
	void onReceivedMessage(String msg);
}
	import java.awt.Point;
	import java.io.IOException;
	import java.io.ObjectInputStream;
	import java.io.ObjectOutputStream;
	import java.net.Socket;
	import java.net.UnknownHostException;
	import java.util.LinkedList;
	import java.util.Queue;
	import java.util.Scanner;

public class ClientM2 {
	Socket server;
	GameClientM2 gc;
	public static boolean isConnected = false;
	Queue<PayloadM2> toServer = new LinkedList<PayloadM2>();
	Queue<PayloadM2> fromServer = new LinkedList<PayloadM2>();
	public static boolean isRunning = false;

	public void SetGameClient(GameClientM2 gc) {
		this.gc = gc;
	}
	private void _connect(String address, int port) {
		try {
			server = new Socket(address, port);
			System.out.println("Client connected");
			isRunning = true;
			isConnected = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setClientName(String name) {
		PayloadM2 p = new PayloadM2();
		p.setPayloadType(PayloadTypeM2.CONNECT);
		p.setUsername(name);
		toServer.add(p);
	}
	public void start() throws IOException {
		if(server == null) {
			return;
		}
		System.out.println("Client Started");
		isRunning = true;
		try(	ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());){
			Thread inputThread = new Thread() {
				@Override
				public void run() {
					try {
						while(isRunning && !server.isClosed()) {
							PayloadM2 p = toServer.poll();
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
						e.printStackTrace();
						System.out.println("Client shutdown");
					}
					finally {
						close();
					}
				}
			};
			inputThread.start();
			Thread fromServerThread = new Thread() {
				@Override
				public void run() {
					try {
						PayloadM2 p;
						while(isRunning && !server.isClosed() && (p = (PayloadM2)in.readObject()) != null) {
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
			fromServerThread.start();
			
			Thread payloadProcessor = new Thread(){
				@Override
				public void run() {
					while(isRunning) {
						PayloadM2 p = fromServer.poll();
						if(p != null) {
							processPayload(p);
						}
						else {
							try {
								Thread.sleep(8);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			};
			payloadProcessor.start();
			while(!server.isClosed()) {
				Thread.sleep(50);
			}
			isRunning = false;
			
			System.out.println("Exited loop");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}
	}
	
	private synchronized void processPayload(PayloadM2 payload) {
		System.out.println(payload);
		switch(payload.getPayloadType()) {
		case CONNECT:
			System.out.println(
					String.format("Client \"%s\" connected", payload.getUsername())
			);
			if(gc != null) {
				gc.UpdatePlayerName(payload.getUsername());
			}
			break;
		case DISCONNECT:
			System.out.println(
					String.format("Client \"%s\" disconnected", payload.getUsername())
			);
			break;
		case MESSAGE:
			System.out.println(
					String.format("%s: %s", payload.getUsername(), payload.getMessage())
			);
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
	public static ClientM2 connect(String host, int port) {
		ClientM2 client = new ClientM2();
		client._connect(host, port);
		Thread clientThread = new Thread() {
			@Override
			public void run() {
				try {
					client.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
	public static void main(String[] args) {
		ClientM2 client = ClientM2.connect("127.0.0.1", 3002);
		
		System.out.println("Client connected and started");
		client.setClientName("Test");
	}

}

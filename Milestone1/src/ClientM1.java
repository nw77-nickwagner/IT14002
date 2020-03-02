	import java.io.IOException;
	import java.io.ObjectInputStream;
	import java.io.ObjectOutputStream;
	import java.net.Socket;
	import java.net.UnknownHostException;
	import java.util.Scanner;

	public class ClientM1 {
		Socket server;
		
		public void connect(String address, int port) {
			try {
				server = new Socket(address, port);
				System.out.println("Client connected");
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void start() throws IOException {
			if(server == null) {
				return;
			}
			System.out.println("Client Started");
			try(Scanner si = new Scanner(System.in);
					ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(server.getInputStream());){
				String name ="";
				do {
					System.out.println("Please enter a username to continue");
					name = si.nextLine();
					if(name == null || name.trim().length() == 0) {
						name="";
					}
				}
				while(!server.isClosed() && name != null && name.length() == 0);
				PayloadM1 p = new PayloadM1();
				p.setPayloadType(PayloadTypeM1.CONNECT);
				p.setMessage(name);
				out.writeObject(p);
				
				Thread inputThread = new Thread() {
					@Override
					public void run() {
						try {
							while(!server.isClosed()) {
								System.out.println("Waiting for input");
								String line = si.nextLine();
								if(!"quit".equalsIgnoreCase(line) && line != null) {
									PayloadM1 p = new PayloadM1();
									p.setPayloadType(PayloadTypeM1.MESSAGE);
									p.setMessage(line);
									out.writeObject(p);
								}
								else {
									System.out.println("Stopping input thread");
									PayloadM1 p = new PayloadM1();
									p.setPayloadType(PayloadTypeM1.DISCONNECT);
									p.setMessage("bye");
									out.writeObject(p);
									break;
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
				inputThread.start();						
				Thread fromServerThread = new Thread() {
					@Override
					public void run() {
						try {
							PayloadM1 fromServer;
							while(!server.isClosed() && (fromServer = (PayloadM1)in.readObject()) != null) {
								processPayload(fromServer);
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
				while(!server.isClosed()) {
					Thread.sleep(50);
				}
				System.out.println("Exited loop");
				System.exit(0);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				close();
			}
		}
		private void processPayload(PayloadM1 payload) {
			System.out.println(payload);
			switch(payload.getPayloadType()) {
			case CONNECT:
				System.out.println(
						String.format("Client \"%s\" connected", payload.getMessage())
				);
				break;
			case DISCONNECT:
				System.out.println(
						String.format("Client \"%s\" disconnected", payload.getMessage())
				);
				break;
			case MESSAGE:
				System.out.println(
						String.format("%s", payload.getMessage())
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
		public static void main(String[] args) {
			ClientM1 client = new ClientM1();
			client.connect("127.0.0.1", 3002);
			try {
				client.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

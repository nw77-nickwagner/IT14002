	import java.io.IOException;
	import java.io.ObjectInputStream;
	import java.io.ObjectOutputStream;
	import java.net.Socket;

	public class ServerThreadM2 extends Thread{
		private Socket client;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private boolean isRunning = false;
		private ServerM2 server;

		private String userName = "Anon";
		public ServerThreadM2(Socket myClient, ServerM2 server) throws IOException {
			this.client = myClient;
			this.server = server;
			isRunning = true;
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());

		}
		void broadcastConnected() {
			PayloadM2 payload = new PayloadM2();
			payload.setPayloadType(PayloadTypeM2.CONNECT);
			payload.setUsername(userName);
			server.broadcast(payload);
		}
		void broadcastDisconnected() {
			PayloadM2 payload = new PayloadM2();
			payload.setPayloadType(PayloadTypeM2.DISCONNECT);
			payload.setUsername(userName );
			server.broadcast(payload);
		}
		public boolean send(PayloadM2 payload) {
			try {
				out.writeObject(payload);
				return true;
			}
			catch(IOException e) {
				System.out.println("Error sending message to client");
				e.printStackTrace();
				cleanup();
				return false;
			}
		}
		@Deprecated
		public boolean send(String message) {
			PayloadM2 payload = new PayloadM2();
			payload.setPayloadType(PayloadTypeM2.MESSAGE);
			payload.setMessage(message);
			return send(payload);
		}
		@Override
		public void run() {
			try {
				PayloadM2 fromClient;
				while(isRunning 
						&& !client.isClosed()
						&& (fromClient = (PayloadM2)in.readObject()) != null) {
					processPayload(fromClient);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				System.out.println("Terminating Client");
			}
			finally {
				broadcastDisconnected();
				System.out.println("Server Cleanup");
				cleanup();
			}
		}
		private void processPayload(PayloadM2 payload) {
			System.out.println("Received from client: " + payload);
			switch(payload.getPayloadType()) {
			case CONNECT:
				String m = payload.getUsername();
				if(m != null) {
					m = BlacklistM2.filter(m);
					this.userName  = m;
				}
				broadcastConnected();
				break;
			case DISCONNECT:
				System.out.println("Received disconnect");
				break;
			case MESSAGE:
				payload.setMessage(BlacklistM2.filter(payload.getMessage()));
				payload.setUsername(this.userName );
				server.broadcast(payload);
				break;
			default:
				System.out.println("Unhandled payload type from client " + payload.getPayloadType());
				break;
			}
		}
		private void cleanup() {
			if(in != null) {
				try {in.close();}
				catch(IOException e) {System.out.println("Input already closed");}
			}
			if(out != null) {
				try {out.close();}
				catch(IOException e) {System.out.println("Client already closed");}
			}
			if(client != null && !client.isClosed()) {
				try {client.shutdownInput();}
				catch(IOException e) {System.out.println("Socket/Input already closed");}
				try {client.shutdownOutput();}
				catch(IOException e) {System.out.println("Socket/Output already closed");}
				try {client.close();}
				catch(IOException e) {System.out.println("Client already closed");}
			}
		}
	}

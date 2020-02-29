import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThreadPart4 extends Thread{
	private Socket client;
	private ObjectInputStream in;//from client
	private ObjectOutputStream out;//to client
	private boolean isRunning = false;
	private SampleSocketServerPart4 server;//ref to our server so we can call methods on it
	//more easily
	private String clientName = "Anon";
	public ServerThreadPart4(Socket myClient, SampleSocketServerPart4 server) throws IOException {
		this.client = myClient;
		this.server = server;
		isRunning = true;
		out = new ObjectOutputStream(client.getOutputStream());
		in = new ObjectInputStream(client.getInputStream());

	}
	void broadcastConnected() {
		PayloadPart4 payload = new PayloadPart4();
		payload.setPayloadType(PayloadTypePart4.CONNECT);
		server.broadcast(payload, this.clientName);
	}
	void broadcastDisconnected() {
		PayloadPart4 payload = new PayloadPart4();
		payload.setPayloadType(PayloadTypePart4.DISCONNECT);
		server.broadcast(payload, this.clientName);
	}
	public boolean send(PayloadPart4 payload) {
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
		PayloadPart4 payload = new PayloadPart4();
		payload.setPayloadType(PayloadTypePart4.MESSAGE);
		payload.setMessage(message);
		return send(payload);
	}
	@Override
	public void run() {
		try {
			PayloadPart4 fromClient;
			while(isRunning 
					&& !client.isClosed()
					&& (fromClient = (PayloadPart4)in.readObject()) != null) {//open while loop
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
	private void processPayload(PayloadPart4 payload) {
		System.out.println("Received from client: " + payload);
		switch(payload.getPayloadType()) {
		case CONNECT:
			String m = payload.getMessage();
			if(m != null) {
				m = WordBlackList.filter(m);
				this.clientName = m;
			}
			broadcastConnected();
			break;
		case DISCONNECT:
			System.out.println("Received disconnect");
			break;
		case MESSAGE:
			payload.setMessage(WordBlackList.filter(payload.getMessage()));
			server.broadcast(payload, this.clientName);
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngineM2 extends BaseGEM2{
	
	public static List<PlayerM2> players = new ArrayList<PlayerM2>();
	GameClientM2 ui;
	ClientM2 client;
	private int defaultBalance;
	public void connect(String host, int port, String clientName) {
		client = ClientM2.connect(host, port);
		if(client != null && ClientM2.isConnected) {
			client.setClientName(clientName);
		}
	}
	public void SetUI(GameClientM2 ui) {
		this.ui = ui;
	}
	@Override
	protected void Awake() {
		System.out.println("Game Engine Awake");
		
	}

	@Override
	protected void OnStart() {

		System.out.println("Game Engine Start");
		for(int i = 0; i < 5; i++) {
			PlayerM2 p = new PlayerM2();
			players.add(p);
			System.out.println("Added player " + i);
		}
		
		for(int i = 0; i < players.size(); i++) {
			System.out.println("Set player" + i +  "'s default balance: " + defaultBalance);
			//can use balance instead of i to set a default balance?
		}
	}

/*	@Override
	protected void Update() {
		// TODO Auto-generated method stub
		if(ui == null) {
			return;
		}
		for(int i = 0; i < players.size(); i++) {
			PlayerM2 player = players.get(i);
			player.move(ui.getBounds());
			if(ClientM2.isConnected && player.changedDirection) {
				client.SyncDirection(player.getLastDirection());
			}
		}
	}
*/
	
	@Override
	protected void End() {
		System.out.println("Game Engine End");
	}

	@Override
	protected void UILoop() {
		if(ui == null) {
			return;
		}
		//ui.draw();
	}
	@Override
	protected void Update() {	
	}

}


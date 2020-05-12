import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine_Final extends BaseGE_Final{
	
	public static List<Player_Final> players = new ArrayList<Player_Final>();
	GameClient_Final ui;
	Client_Final client;
	private int defaultBalance;
	public void connect(String host, int port, String userName) {
		client = Client_Final.connect(host, port);
		if(client != null && Client_Final.isConnected) {
			client.setUsername(userName);
		}
	}
	public void SetUI(GameClient_Final ui) {
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
			Player_Final p = new Player_Final();
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


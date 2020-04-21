import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine_M3 extends BaseGE_M3{
	
	public static List<Player_M3> players = new ArrayList<Player_M3>();
	GameClient_M3 ui;
	Client_M3 client;
	private int defaultBalance;
	public void connect(String host, int port, String userName) {
		client = Client_M3.connect(host, port);
		if(client != null && Client_M3.isConnected) {
			client.setUsername(userName);
		}
	}
	public void SetUI(GameClient_M3 ui) {
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
			Player_M3 p = new Player_M3();
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


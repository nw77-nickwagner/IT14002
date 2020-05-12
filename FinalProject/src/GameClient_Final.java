	import java.awt.BorderLayout;
	import java.awt.Color;
	import java.awt.Component;
	import java.awt.Dimension;
	import java.awt.Graphics;
	import java.awt.Graphics2D;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Random;
	import javax.swing.BorderFactory;
	import javax.swing.BoxLayout;
	import javax.swing.JButton;
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.JTextField;
	import javax.swing.UIManager;
	import javax.swing.UnsupportedLookAndFeelException;
	import javax.swing.WindowConstants;

	public class GameClient_Final extends JPanel{

	private static final long serialVersionUID = 6748325367132904432L;
	
	public static boolean isRunning = true;
	
	UIUtil_Final ui = new UIUtil_Final();
	
	static HashMap<String, Component> components = new HashMap<String, Component>();
	static GameState gameState = GameState.LOBBY;
	public static JFrame myFrame;
	GameEngine_Final ge;
	public GameClient_Final() {
		
		
	}
	public void toggleRunningState(boolean s) {
		isRunning = s;
	}

	void toggleComponent(String name, boolean toggle) {
		if(components.containsKey(name)) {
			components.get(name).setVisible(toggle);
		}
	}

	void ChangePanels() {
		switch(GameClient_Final.gameState) {
			case GAME:
				toggleComponent("lobby", false);
				toggleComponent("game", true);
				break;
			case LOBBY:
				toggleComponent("lobby", true);
				toggleComponent("game", false);
				break;
			default:
				break;
		}
		myFrame.pack();
        myFrame.revalidate();
        myFrame.repaint();
	}

	public Component GetUIElement(String name) {
		if(components.containsKey(name)) {
			return components.get(name);
		}
		return null;
	}
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException ex) {
		} catch (InstantiationException ex) {
		} catch (IllegalAccessException ex) {
		} catch (UnsupportedLookAndFeelException ex) {
		}
		JFrame frame = new JFrame("Poker");
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(new Dimension(600,600));
		GameClient_Final.myFrame = frame;
		InitLobby();
		
		InitGameCanvas();
		
		GameClient_Final gc = (GameClient_Final)components.get("game");
		gc.ChangePanels();
		GameClient_Final.myFrame.pack();
		GameClient_Final.myFrame.setVisible(true);
		
	}
	public static GameClient_Final InitGameCanvas() {
		JPanel playArea = new GameClient_Final();
		components.put("game", playArea);
		playArea.setPreferredSize(new Dimension(600,600));
		
		myFrame.add(playArea, BorderLayout.CENTER);
		return (GameClient_Final)playArea;
	}
	public static void InitLobby() {
		JPanel lobby = new JPanel();
		components.put("lobby", lobby);
		lobby.setName("lobby");
		lobby.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel container = new JPanel();
		JTextField name = new JTextField(20);
		name.setText("Guest");
		JTextField host = new JTextField(15);
		host.setText("127.0.0.1");
		JTextField port = new JTextField(4);
		port.setText("3111");
		JButton connectButton = new JButton();
		connectButton.setText("Connect");
		connectButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	GameClient_Final gc = (GameClient_Final)components.get("game");
		    	if(gc != null) {
		    		gc.StartGameLoop(
		    				host.getText().trim(),
		    				Integer.parseInt(port.getText().trim()),
		    				name.getText().trim());
		    	}
		    }
		});
		JTextField message = new JTextField(60);
		message.setEditable(false);
		container.add(name);
		container.add(host);
		container.add(port);
		container.add(connectButton);
		lobby.setLayout(new BoxLayout(lobby, BoxLayout.PAGE_AXIS));
		lobby.add(container);
		lobby.add(message);
		components.put("lobby.message", message);
		myFrame.add(lobby, BorderLayout.NORTH);
		
	}
	void StartGameLoop(String host, int port, String playername) {
		GameClient_Final.gameState = GameState.GAME;
    	ChangePanels();
        toggleRunningState(true);
        ge = new GameEngine_Final();
        ge.connect(host,port, playername);
        ge.SetUI(this);
        ge.start();
    	run();
	}
	public void UpdatePlayerName(String str) {
		for(int i = 0; i < GameEngine_Final.players.size(); i++) {
			GameEngine_Final.players.get(i).name = str;
		}
	}
	/*
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		for(int i = 0; i < GameEngineM2.players.size(); i++) {
			GameEngineM2.players.get(i).draw(g2d);
		}
		ui.showFPS(g2d);
	}
	public void draw() {
		myFrame.repaint();
	}*/
	public void run() {
		/*while(GameEngine.isRunning) {
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}
}
enum GameState{
	LOBBY,
	GAME
}




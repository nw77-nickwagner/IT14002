import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

public class UI_M3 extends JFrame implements OnReceive{
	private static final long serialVersionUID = 1L;
	static Client_M3 client;
	static Server_M3 server;
	static JButton toggle;
	static JButton clickit;
	static JTextArea history;
	public UI_M3() {
		super("Poker");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
			}
		});
	}
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException ex) {
		} catch (InstantiationException ex) {
		} catch (IllegalAccessException ex) {
		} catch (UnsupportedLookAndFeelException ex) {
		}
		UI_M3 window = new UI_M3();
		window.setLayout(new BorderLayout());
		JPanel connectionDetails = new JPanel();
		JTextField host = new JTextField();
		host.setText("127.0.0.1");
		JTextField port = new JTextField();
		port.setText("3001");
		JButton connect = new JButton();
		
		connect.setText("Connect");
		connectionDetails.add(host);
		connectionDetails.add(port);
		connectionDetails.add(connect);
		window.add(connectionDetails, BorderLayout.NORTH);
		JPanel area = new JPanel();
		area.setLayout(new BorderLayout());
		window.add(area, BorderLayout.CENTER);
		JPanel chat = new JPanel();
		chat.setPreferredSize(new Dimension(400,400));
		chat.setLayout(new BorderLayout());
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText("");
		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BorderLayout());
		chatArea.add(textArea, BorderLayout.CENTER);
		chatArea.setBorder(BorderFactory.createLineBorder(Color.black));
		chat.add(chatArea, BorderLayout.CENTER);
		JPanel userInput = new JPanel();
		JTextField textField = new JTextField();
		textField.setPreferredSize(new Dimension(100,30));
		JButton c = new JButton();
		c.setPreferredSize(new Dimension(75,30));
		c.setText("Check");	
		userInput.add(c, BorderLayout.WEST);
		JButton f = new JButton();
		f.setPreferredSize(new Dimension(75,30));
		f.setText("Fold");
		userInput.add(f, BorderLayout.EAST);
		
		JPanel sliderPanel = new JPanel();
		JSlider raise = new JSlider(JSlider.HORIZONTAL, 0, 3, 0);
		JLabel sliderLabel = new JLabel("Raise", JLabel.CENTER);
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
		sliderPanel.add(sliderLabel);
		raise.setPreferredSize(new Dimension(150,30));
		sliderPanel.add(raise);

		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		history = ta;
		history.setWrapStyleWord(true);
		history.setAutoscrolls(true);
		history.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(history);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		DefaultCaret caret = (DefaultCaret)history.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		container.add(scroll, BorderLayout.CENTER);
		
		userInput.add(sliderPanel, BorderLayout.CENTER);
		userInput.add(c);
		userInput.add(f);
		chat.add(userInput, BorderLayout.SOUTH);
		chat.add(container, BorderLayout.CENTER);
		
		window.add(chat, BorderLayout.CENTER);
		
		window.pack();
		window.setVisible(true);
		c.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	String msg = c.getText();
		    	System.out.println(msg);
		    }
		});
		
		f.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	String msg = f.getText();
		    	System.out.println(msg);
		    	
		    }
		});
		
		/*raise.addChangeListener(new SliderListener()); {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	if(!)
		    }
		});
		*/
		
		connect.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	client = new Client_M3();
		    	int _port = -1;
		    	try {
		    		_port = Integer.parseInt(port.getText());
		    	}
		    	catch(Exception num) {
		    		System.out.println("Port not a number");
		    	}
		    	if(_port > -1) {
			    	client = Client_M3.connect(host.getText(), _port);
			    	
			    	//METHOD 1 Using the interface
			    	client.registerClickListener(window);			    	
			    	//trigger any one-time data after client connects			    
			    	//register our history/message listener
			    	client.registerMessageListener(window);
			    	client.postConnectionData();
			    	connect.setEnabled(false);
		    	}
		    }
		});
		/*
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		history = ta;
		history.setWrapStyleWord(true);
		history.setAutoscrolls(true);
		history.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(history);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		DefaultCaret caret = (DefaultCaret)history.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		container.add(scroll, BorderLayout.CENTER);
		*/
	
		window.setPreferredSize(new Dimension(400,600));
		window.pack();
		window.setVisible(true);
	}
	@Override
	public void onReceivedMessage(String msg) {
		if(history != null) {
			history.append(msg);
			history.append(System.lineSeparator());
		}
	}
	@Override
	public void onReceivedSwitch(boolean isOn) {
		// TODO Auto-generated method stub
		
	}
}

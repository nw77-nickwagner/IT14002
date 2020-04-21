
	import java.awt.BorderLayout;
	import java.awt.Color;
	import java.awt.Dimension;
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
	import javax.swing.JSlider;
	import javax.swing.JTextArea;
	import javax.swing.JTextField;
	import javax.swing.UIManager;
	import javax.swing.UnsupportedLookAndFeelException;
	
	public class UI_M3 extends JFrame{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		static Client_M3 client;
		
		public UI_M3() {
			super("Connection");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// add a window listener
			this.addWindowListener(new WindowAdapter() {
				/* (non-Javadoc)
				 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
				 */
				@Override
				public void windowClosing(WindowEvent e) {
					// before we stop the JVM stop the example
					//client.isRunning = false;
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
			JFrame frame = new JFrame("My UI");
			frame.setLayout(new BorderLayout());
			
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
			JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 3, 0);
			JLabel sliderLabel = new JLabel("Raise", JLabel.CENTER);
			sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
			sliderPanel.add(sliderLabel);
			slider.setPreferredSize(new Dimension(150,30));
			sliderPanel.add(slider);

			userInput.add(sliderPanel, BorderLayout.CENTER);
			userInput.add(c);
			userInput.add(f);
			chat.add(userInput, BorderLayout.SOUTH);
			
			frame.add(chat, BorderLayout.CENTER);
			
			frame.pack();
			frame.setVisible(true);
		
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
		    	client.registerListener(window);
		    	//METHOD 2 Lamba Expression (unnamed function to handle callback)
		    	/*client.registerListener(()->{	
		    		if(UISample.toggle != null) {
		    			UISample.toggle.setText("OFF");
		    			UISample.toggle.setBackground(Color.RED);
		    		}
		    	});*/
		    	
		    	
		    	//trigger any one-time data after client connects
		    	client.postConnectionData();
		    	connect.setEnabled(false);
	    		}
			}
		});
	
	window.setPreferredSize(new Dimension(400,600));
	window.pack();
	window.setVisible(true);
	}
		public void onReceived(boolean isOn) {
			}
		}		


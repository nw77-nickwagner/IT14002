
	import java.awt.BorderLayout;
	import java.awt.Color;
	import java.awt.Dimension;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;

	import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
	import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
	import javax.swing.JTextField;
	
	public class UIM2 {
		public static void main(String[] args) {
			JFrame frame = new JFrame("My UI");
			frame.setLayout(new BorderLayout());
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
	}
}

package kapitalMonopolyUI;

import kapitalMonopoly.DomainController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StartingWindow {

	static JFrame frame;
	protected MonopolyGameFrame monopolyFrame;
	protected final int STARTING_X = 200;
	protected final int STARTING_Y = 200;
	protected final int FRAME_WIDTH = 650;
	protected final int FRAME_HEIGHT = 600;
	protected static JButton loadButton;
	protected static JComboBox botBox = new JComboBox();
	protected final int[] LOAD_BUTTON = {79, 402, 176, 40};
	protected final int[] BOT_BUTTON ={274, 359, 194, 40};
	protected final int[] CONNECT_BUTTON = {274, 402, 194, 40};
	protected final int[] BOT_BOX = {79,361, 176, 38};
	protected final int[] ID_SLIDER = {244, 296, 240, 40};
	protected final int[] COUNT_SLIDER = {244, 235, 240, 40};
	private final int[] MONOPOLY_GIF = {0, 443, 62, 90};
	private final int[] MONOPOLY_LOGO = { 24, 16, 510, 190};
	private final String MONOPOLY_LOGO_FILE = "resources/InitialScreen/MonopolyLogo.png";
	

	private final Color BUTTON_COLOR = new Color(120,190,118);

	private static final int MAX_BOT_COUNT = 3;

	private static int addedBot = 0;
	private ArrayList<String> botNames = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartingWindow window = new StartingWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StartingWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(STARTING_X, STARTING_Y, FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("KAPITAL - Monopoly");
		botBox.addItem("Aggressive");
		botBox.addItem("Dummy");
		botBox.addItem("Passive");
		botBox.addItem("Reasonable");
		botBox.setBackground(BUTTON_COLOR);
		botBox.setOpaque(true);

		JSlider countSlider = new JSlider();
		countSlider.setMajorTickSpacing(1);
		countSlider.setSnapToTicks(true);
		countSlider.setPaintTicks(true);
		countSlider.setMinorTickSpacing(1);
		countSlider.setPaintLabels(true);
		countSlider.setMaximum(8);
		countSlider.setMinimum(1);
		countSlider.setBounds(COUNT_SLIDER[0], COUNT_SLIDER[1], COUNT_SLIDER[2], COUNT_SLIDER[3]);
		countSlider.setValue(1);
		frame.getContentPane().add(countSlider);

		JSlider idSlider = new JSlider();
		idSlider.setSnapToTicks(true);
		idSlider.setPaintTicks(true);
		idSlider.setPaintLabels(true);
		idSlider.setMinorTickSpacing(1);
		idSlider.setMinimum(1);
		idSlider.setMaximum(8);
		idSlider.setMajorTickSpacing(1);
		idSlider.setBounds(ID_SLIDER[0], ID_SLIDER[1], ID_SLIDER[2], ID_SLIDER[3]);
		idSlider.setValue(1);
		frame.getContentPane().add(idSlider);

		JLabel lblCount = new JLabel("Enter player count:");
		lblCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblCount.setBounds(24, 237, 240, 16);
		frame.getContentPane().add(lblCount);

		Font f = lblCount.getFont();
		lblCount.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));

		JLabel lblEnterPlayerId = new JLabel("Enter player ID:");
		lblEnterPlayerId.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterPlayerId.setBounds(24, 296, 240, 16);
		frame.getContentPane().add(lblEnterPlayerId);
		
		f = lblEnterPlayerId.getFont();
		lblEnterPlayerId.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
		
		JLabel bots = new JLabel("");
		bots.setHorizontalAlignment(SwingConstants.CENTER);
		bots.setVerticalAlignment(SwingConstants.TOP);
		bots.setBounds(500, 296, 98, 97);
		frame.getContentPane().add(bots);
		bots.setVisible(false);
		bots.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		bots.setOpaque(true);

		botBox.setBounds(BOT_BOX[0], BOT_BOX[1], BOT_BOX[2], BOT_BOX[3]);
		frame.getContentPane().add(botBox);

		JLabel lblAddedBots = new JLabel("Added Bots:");
		lblAddedBots.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		lblAddedBots.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddedBots.setBackground(BUTTON_COLOR);
		lblAddedBots.setForeground(Color.WHITE);
		lblAddedBots.setBounds(500, 254, 98, 40);
		frame.getContentPane().add(lblAddedBots);
		lblAddedBots.setVisible(false);
		lblAddedBots.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblAddedBots.setOpaque(true);

		JButton botConnect = new JButton("Add Bot");
		botConnect.setBounds(BOT_BUTTON[0], BOT_BUTTON[1], BOT_BUTTON[2], BOT_BUTTON[3]);
		frame.getContentPane().add(botConnect);
		botConnect.setBackground(BUTTON_COLOR);
		botConnect.setOpaque(true);
		botConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblAddedBots.setVisible(true);
				bots.setVisible(true);
				botNames.add(botBox.getSelectedItem().toString());
				
				if(!bots.getText().equals(null)) {
					bots.setText(null);
				}
				String list = "";
				for(int i=0;i<botNames.size();i++) {
					list += botNames.get(i) + "<br/>";
				}
				bots.setText("<html>" + list + "</html>");
				addedBot++;
				if(addedBot == MAX_BOT_COUNT){
					botBox.setEnabled(false);
					botConnect.setEnabled(false);
				}
			}
		});

		JButton connectButton = new JButton("Connect");
		connectButton.setBounds(CONNECT_BUTTON[0], CONNECT_BUTTON[1], CONNECT_BUTTON[2], CONNECT_BUTTON[3]);
		frame.getContentPane().add(connectButton);
		connectButton.setBackground(BUTTON_COLOR);
		connectButton.setOpaque(true);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DomainController.initializeMonopolyGame(countSlider.getValue(), idSlider.getValue(), botNames);
				frame.dispose();
				monopolyFrame = new MonopolyGameFrame();
				monopolyFrame.frame.setVisible(true);
			}
		});

		JButton startButton = new JButton("Start");
		startButton.setBounds(CONNECT_BUTTON[0], CONNECT_BUTTON[1], CONNECT_BUTTON[2], CONNECT_BUTTON[3]);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DomainController.loadGame();
				frame.dispose();
				monopolyFrame = new MonopolyGameFrame();
				monopolyFrame.frame.setVisible(true);
			}
		});

		loadButton = new JButton("Load a game");
		loadButton.setBounds(LOAD_BUTTON[0], LOAD_BUTTON[1], LOAD_BUTTON[2], LOAD_BUTTON[3]);
		loadButton.setVisible(true);
		loadButton.setEnabled(true);
		loadButton.setBackground(BUTTON_COLOR);
		loadButton.setOpaque(true);
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadButton.setEnabled(false);
				connectButton.setVisible(false);
				frame.getContentPane().remove(connectButton);
				frame.getContentPane().add(startButton);
				startButton.setVisible(true);
				startButton.setEnabled(true);
				botBox.setEnabled(false);
				botConnect.setEnabled(false);
			}
		});

		frame.getContentPane().add(loadButton);
		frame.getContentPane().setBackground(Color.white);
		MonopolyGIF monopolyGIF = new MonopolyGIF();
		monopolyGIF.start();

		JLabel monopolyLogo = new JLabel("");
		monopolyLogo.setHorizontalAlignment(SwingConstants.CENTER);
		monopolyLogo.setBounds(24, 16, 587, 190);
		frame.getContentPane().add(monopolyLogo);
		monopolyLogo.setIcon(new ImageIcon(MONOPOLY_LOGO_FILE));


		startButton.setBackground(BUTTON_COLOR);
		startButton.setOpaque(true);

	}

	private class MonopolyGIF extends Thread{

		private final String RIGHT_GIF = "resources/InitialScreen/Monopoly2.gif";
		private final String LEFT_GIF = "resources/InitialScreen/Monopoly1.gif";
		private final int[] SIZE = {MONOPOLY_GIF[2], MONOPOLY_GIF[3]};
		private final int Y_COORDINATE = MONOPOLY_GIF[1];
		private final int[] X_COORDINATES = {0, FRAME_WIDTH-SIZE[0]};
		private final int WAIT_MILLISECONDS = 60;
		private int currX;
		private int direction;
		private JLabel monopolyGif;

		public MonopolyGIF(){
			monopolyGif = new JLabel("");
			currX = X_COORDINATES[0];
			direction = 1;
			monopolyGif.setBounds(currX, Y_COORDINATE, SIZE[0], SIZE[1]);
			frame.getContentPane().add(monopolyGif);
			monopolyGif.setIcon(new ImageIcon(RIGHT_GIF));
		}

		public void run() {
			while(true){
				try {
					Thread.sleep(WAIT_MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				currX += direction * 5;
				if(direction == 1){
					if(currX <= X_COORDINATES[1]){
						monopolyGif.setBounds(currX, Y_COORDINATE, SIZE[0], SIZE[1]);
					} else {
						monopolyGif.setIcon(new ImageIcon(LEFT_GIF));
						direction = -1;
					}
				} else {
					if(currX >= X_COORDINATES[0]){
						monopolyGif.setBounds(currX, Y_COORDINATE, SIZE[0], SIZE[1]);
					} else {
						monopolyGif.setIcon(new ImageIcon(RIGHT_GIF));
						direction = 1;
					}
				}
			}
		}
	}
}

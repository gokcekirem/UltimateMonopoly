package kapitalMonopolyUI;

import kapitalMonopoly.DomainController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MonopolyGameFrame {

	public static final int BREAK_AMOUNT = 30;

	protected final int FRAME_WIDTH = 1400;
	protected final int FRAME_HEIGHT = 875;
	protected JFrame frame = new JFrame();

	private DiePanel diePanel;
	private CardPanel cardPanel;
	private BoardPanel boardPanel;
	private PlayerPanel playerPanel;
	private DeedPanel deedPanel;
	private PropertyPanel propertyPanel;
	private ChatPanel chatPanel;
	protected JComboBox themeBox;
	private JLabel themeSelection = new JLabel("Theme :");
	private final static String[] TOKEN_SEPARATOR = {"-", ","};
	private final static String THEME_DETAILS_FILE = "resources/ThemeDetails.txt";
	private final static String THEME_ICONS_FILE = "resources/ThemeIcons/";
	private final static String SIDE_ICON = "resources/ThemeIcons/BaseLogo.png";
	private final static String THEME_ICONS_EXTENTION = ".png";
	private final static int[] THEME_SIZE = {200, 25};
	private String selectedTheme = "Default";
	private Map<String,Color> themeBackground = new HashMap<>();
	private Map<String,Color> themeForeground = new HashMap<>();
	private Map<String,Color> themeChatColor = new HashMap<>();
	private Map<String,Color> themeChatTextColor = new HashMap<>();
	protected static JLabel themeImg = new JLabel("");
	protected static JLabel sideImg = new JLabel("");
	/**
	 * Create the application.
	 */
	public MonopolyGameFrame() {
		
		initialize();

		diePanel = new DiePanel();
		cardPanel = new CardPanel();
		boardPanel = new BoardPanel();
		playerPanel = new PlayerPanel();
		deedPanel = new DeedPanel();
		propertyPanel = new PropertyPanel();
		chatPanel = new ChatPanel();

		frame.setTitle("KAPITAL - Monopoly");
		frame.getContentPane().add(themeSelection);
		frame.getContentPane().add(themeBox);
		frame.getContentPane().add(diePanel.dieJPanel);
		frame.getContentPane().add(chatPanel.chatJPanel);
		frame.getContentPane().add(cardPanel.cardJPanel);
		frame.getContentPane().add(boardPanel.boardJPanel);
		frame.getContentPane().add(playerPanel.playerJPanel);
		frame.getContentPane().add(deedPanel.deedJPanel);
		frame.getContentPane().add(propertyPanel.propertyJPanel);
		frame.getContentPane().add(themeImg);
		frame.getContentPane().add(sideImg);
		applyTheme();
		setLabelsBold();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				DomainController.disconnect();
			}
		});

		createThemes();
		themeSelection.setBounds(BoardPanel.BOARD_WIDTH/2 - THEME_SIZE[0]/2 + BREAK_AMOUNT, BoardPanel.BOARD_HEIGHT + BREAK_AMOUNT*2, THEME_SIZE[0] , THEME_SIZE[1]);
		themeBox = new JComboBox();
		themeBox.setBounds(BoardPanel.BOARD_WIDTH/2 - THEME_SIZE[0]/2 + BREAK_AMOUNT, BoardPanel.BOARD_HEIGHT + BREAK_AMOUNT * 3, THEME_SIZE[0] , THEME_SIZE[1]);
		for(String themeName : themeBackground.keySet()){
			themeBox.addItem(themeName);
		}
		themeBox.setSelectedItem(selectedTheme);
		themeImg.setBounds(BoardPanel.BOARD_WIDTH/2 + THEME_SIZE[0]/2 + BREAK_AMOUNT*3, BoardPanel.BOARD_HEIGHT + BREAK_AMOUNT, 200,150);

		sideImg.setIcon(new ImageIcon(SIDE_ICON));
		sideImg.setBounds( BREAK_AMOUNT , BoardPanel.BOARD_HEIGHT + BREAK_AMOUNT/2, 200,150);

		themeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyTheme();
			}
		});

	}

	private void applyTheme(){
		selectedTheme = themeBox.getSelectedItem().toString();
		themeImg.setIcon(new ImageIcon(THEME_ICONS_FILE + selectedTheme + THEME_ICONS_EXTENTION));

		Color backColor = themeBackground.get(selectedTheme);
		Color foreColor = themeForeground.get(selectedTheme);

		frame.getContentPane().setBackground(backColor);
		diePanel.dieJPanel.setBackground(backColor);
		cardPanel.cardJPanel.setBackground(backColor);
		boardPanel.boardJPanel.setBackground(backColor);
		playerPanel.playerJPanel.setBackground(backColor);
		deedPanel.deedJPanel.setBackground(backColor);
		chatPanel.chatJPanel.setBackground(backColor);
		propertyPanel.propertyJPanel.setBackground(backColor);
		propertyPanel.house.setBackground(backColor);
		propertyPanel.hotel.setBackground(backColor);
		propertyPanel.skyscraper.setBackground(backColor);

		themeSelection.setForeground(foreColor);
		deedPanel.deedLabel.setForeground(foreColor);
		playerPanel.playerLabel.setForeground(foreColor);
		playerPanel.playerBalanceLabel.setForeground(foreColor);
		playerPanel.playerDeedLabel.setForeground(foreColor);
		playerPanel.lblSavedCards.setForeground(foreColor);
		playerPanel.deedInformationLabel.setForeground(foreColor);
		propertyPanel.propertyInformationLabel.setForeground(foreColor);
		propertyPanel.house.setForeground(foreColor);
		propertyPanel.hotel.setForeground(foreColor);
		propertyPanel.skyscraper.setForeground(foreColor);
		playerPanel.playerLabel.setBorder(BorderFactory.createLineBorder(foreColor));

		chatPanel.chatBox.setBackground(themeChatColor.get(selectedTheme));
		chatPanel.textBox.setBackground(themeChatColor.get(selectedTheme));
		chatPanel.sendButton.setBackground(themeChatColor.get(selectedTheme));
		chatPanel.chatBox.setBorder(BorderFactory.createLineBorder(foreColor));
		chatPanel.textBox.setBorder(BorderFactory.createLineBorder(foreColor));
		chatPanel.sendButton.setBorder(BorderFactory.createLineBorder(foreColor));
		chatPanel.sendButton.setForeground(foreColor);

		chatPanel.chatBox.setForeground(themeChatTextColor.get(selectedTheme));
		chatPanel.textBox.setForeground(themeChatTextColor.get(selectedTheme));
	}

	private void setLabelsBold(){
		Font f = themeSelection.getFont();
		themeSelection.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));

		f = playerPanel.playerDeedLabel.getFont();
		playerPanel.playerDeedLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));

		f = deedPanel.deedLabel.getFont();
		deedPanel.deedLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));

		f = playerPanel.playerBalanceLabel.getFont();
		playerPanel.playerBalanceLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));

		f = playerPanel.deedInformationLabel.getFont();
		playerPanel.deedInformationLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));

		f = propertyPanel.propertyInformationLabel.getFont();
		propertyPanel.propertyInformationLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));

		f = playerPanel.lblSavedCards.getFont();
		playerPanel.lblSavedCards.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
	}

	private void createThemes(){

		try {
			FileReader file = new FileReader(THEME_DETAILS_FILE);
			BufferedReader br = new BufferedReader(file);
			String line;
			while( (line=br.readLine()) != null ){
				String[] tokens = line.split(TOKEN_SEPARATOR[0]);
				String name = tokens[0].trim();
//				System.out.println(name + tokens.length);
//				System.out.println(tokens[1]);
//				System.out.println(tokens[2]);

				String[] color = tokens[1].trim().split(TOKEN_SEPARATOR[1]);
				int r = Integer.parseInt(color[0].trim());
				int g = Integer.parseInt(color[1].trim());
				int b = Integer.parseInt(color[2].trim());
				Color backColor = new Color(r,g,b);
				themeBackground.put(name,backColor);

				color = tokens[2].trim().split(TOKEN_SEPARATOR[1]);
				r = Integer.parseInt(color[0].trim());
				g = Integer.parseInt(color[1].trim());
				b = Integer.parseInt(color[2].trim());
				Color foreColor = new Color(r,g,b);
				themeForeground.put(name,foreColor);

				color = tokens[3].trim().split(TOKEN_SEPARATOR[1]);
				r = Integer.parseInt(color[0].trim());
				g = Integer.parseInt(color[1].trim());
				b = Integer.parseInt(color[2].trim());
				Color chatBackColor = new Color(r,g,b);
				themeChatColor.put(name,chatBackColor);

				if(tokens.length>4){
					color = tokens[4].trim().split(TOKEN_SEPARATOR[1]);
					r = Integer.parseInt(color[0].trim());
					g = Integer.parseInt(color[1].trim());
					b = Integer.parseInt(color[2].trim());
					Color chatTextColor = new Color(r,g,b);
					themeChatTextColor.put(name,chatTextColor);
				} else{
					themeChatTextColor.put(name, Color.BLACK);
				}
			}
			file.close();
			br.close();
		} catch (IOException e) {
			System.out.println("The file is not a valid format for the reader!");
		}
	}
}

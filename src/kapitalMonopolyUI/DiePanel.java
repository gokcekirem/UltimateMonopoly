package kapitalMonopolyUI;

import kapitalMonopoly.DomainController;
import kapitalMonopolyObservers.AnimationRollListener;
import kapitalMonopolyObservers.RollListener;
import kapitalMonopolyObservers.TurnListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class DiePanel implements RollListener, TurnListener, AnimationRollListener {

	JPanel dieJPanel = new JPanel();
	public static JButton btnRollDice;
	
	public static final int PANEL_WIDTH = 300;
	public static final int PANEL_HEIGHT = 200;
	
	protected final String DIE_ICON = "resources/DiePictures/game-die_1.png";
	protected final String DIE_IMAGE_PATH = "resources/DiePictures/die-face-";
	protected final String DIE_IMAGE_EXTENSION = ".png";
	protected final int BUS_ROLL = 4;
	protected final int MR_MONOPOLY_ROLL1 = 5;
	protected final int MR_MONOPOLY_ROLL2 = 6;
	protected final String BUS_ROLL_PATH = "resources/DiePictures/die-face-bus.png";
	protected final String MR_MONOPOLY_PATH = "resources/DiePictures/die-face-mr.monopoly.png";
	protected static final String[] diePictures = {"die-face-1.png", "die-face-2.png", 
			"die-face-3.png", "die-face-4.png", "die-face-5.png", "die-face-6.png", 
			"die-face-bus.png", "die-face-mr.monopoly.png"};


	protected final int[] ROLL_BUTTON = {110, 23, 116, 62};
	protected final static int[] DIE_1_COORDINATES = {80, 114};//{110, 134};
	protected final static int[] DIE_2_COORDINATES = {142, 114};//{172, 134};
	protected final static int[] DIE_3_COORDINATES = {204, 114}; //{234, 134};
	protected final static int[] DIE_SIZE = {50,50};
	
	protected String pathReg1 = "";
	protected String pathReg2 = "";
	protected String pathSpeed = "";

	protected static JLabel dieLabel1 = new JLabel("");
	protected static JLabel dieLabel2 = new JLabel("");
	protected static JLabel dieLabel3 = new JLabel("");

	public DiePanel() {
		initialize();
	}

	public void initialize() {

		dieJPanel.setBounds(BoardPanel.BOARD_WIDTH + MonopolyGameFrame.BREAK_AMOUNT,
				PlayerPanel.PANEL_HEIGHT + CardPanel.PANEL_HEIGHT + MonopolyGameFrame.BREAK_AMOUNT*2,
				PANEL_WIDTH, PANEL_HEIGHT);

		dieJPanel.setLayout(null);
		DomainController.addRollListener(this);
		DomainController.addTurnListener(this);
		DomainController.addAnimationRollListener(this);

		btnRollDice = new JButton("Roll Dice");
		btnRollDice.setVisible(true);
		btnRollDice.setHorizontalAlignment(SwingConstants.TRAILING);
		btnRollDice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRollDice.setEnabled(false);
				int currentPlayerId = DomainController.getCurrentPlayerID();
				boolean usedToBeInJail = DomainController.getIsPlayerInJail();
				DomainController.rollDice();
			}
		});

		btnRollDice.setBounds(ROLL_BUTTON[0], ROLL_BUTTON[1], ROLL_BUTTON[2], ROLL_BUTTON[3]);
		btnRollDice.setIcon(new ImageIcon(DIE_ICON));
		dieJPanel.add(btnRollDice);

		dieLabel1.setLayout(null);
		dieLabel1.setBounds(DIE_1_COORDINATES[0], DIE_1_COORDINATES[1], DIE_SIZE[0], DIE_SIZE[1]);
		dieLabel1.setIcon(new ImageIcon(getPath(1)));
		dieJPanel.add(dieLabel1);

		dieLabel2.setBounds(DIE_2_COORDINATES[0], DIE_2_COORDINATES[1], DIE_SIZE[0], DIE_SIZE[1]);
		dieLabel2.setIcon(new ImageIcon(getPath(2)));
		dieJPanel.add(dieLabel2);

		dieLabel3.setBounds(DIE_3_COORDINATES[0], DIE_3_COORDINATES[1], DIE_SIZE[0], DIE_SIZE[1]);
		dieLabel3.setIcon(new ImageIcon(getPath(3)));
		dieJPanel.add(dieLabel3);

		btnRollDice.setEnabled(DomainController.isYourTurn());

	}

	@Override
	public void onRollEvent(Object source, String name, String value) {

		pathReg1 = getPath(Integer.parseInt("" + value.charAt(0)));
		pathReg2 = getPath(Integer.parseInt("" + value.charAt(1)));
		int speedDie = Integer.parseInt("" + value.charAt(2));
		if(speedDie == BUS_ROLL) {
			pathSpeed = BUS_ROLL_PATH;
		} else if(speedDie == MR_MONOPOLY_ROLL1 || speedDie == MR_MONOPOLY_ROLL2) {
			pathSpeed = MR_MONOPOLY_PATH;
		} else {
			pathSpeed = getPath(speedDie);
		}
		dieLabel1.setIcon(new ImageIcon(pathReg1));
		dieLabel2.setIcon(new ImageIcon(pathReg2));
		dieLabel3.setIcon(new ImageIcon(pathSpeed));
//		System.out.println(pathReg1);
//		System.out.println(pathReg2);
		if (pathReg1.equals(pathReg2)) {
//			System.out.println("doubled");
			btnRollDice.setEnabled(!DomainController.isBot() && DomainController.isYourTurn());
		} else{
			btnRollDice.setEnabled(false);
		}
	}

	public String getPath(int number){
		return DIE_IMAGE_PATH + number + DIE_IMAGE_EXTENSION;
	}
	
	Random rand = new Random();
	
	
	int[] finalReg1Coordinate = {DIE_1_COORDINATES[0], DIE_1_COORDINATES[1]};
	int[] finalReg2Coordinate = {DIE_2_COORDINATES[0], DIE_2_COORDINATES[1]};
	int[] finalSpeedCoordinate = {DIE_3_COORDINATES[0], DIE_3_COORDINATES[1]};
	
	public void onAnimationRollEvent(Object source, String name, int value1, int value2, int value3, int j, int step_number) {
		
		int[] initialReg1Coordinate = {rand.nextInt(DIE_1_COORDINATES[0]),
				rand.nextInt(DIE_1_COORDINATES[1])};
		int[] initialReg2Coordinate = {rand.nextInt(DIE_2_COORDINATES[0]),
				rand.nextInt(DIE_2_COORDINATES[0])};
		int[] initialSpeedCoordinate = {rand.nextInt(DIE_3_COORDINATES[0]),
				rand.nextInt(DIE_3_COORDINATES[0])};
		
		int[] coordinate1= {initialReg1Coordinate[0]+j*(finalReg1Coordinate[0]-initialReg1Coordinate[0])/step_number,
				initialReg1Coordinate[1]+j*(finalReg1Coordinate[1]-initialReg1Coordinate[1])/step_number};
		int[] coordinate2= {initialReg2Coordinate[0]+j*(finalReg2Coordinate[0]-initialReg2Coordinate[0])/step_number,
				initialReg2Coordinate[1]+j*(finalReg2Coordinate[1]-initialReg2Coordinate[1])/step_number};
		int[] coordinateS= {initialSpeedCoordinate[0]+j*(finalSpeedCoordinate[0]-initialSpeedCoordinate[0])/step_number,
				initialSpeedCoordinate[1]+j*(finalSpeedCoordinate[1]-initialSpeedCoordinate[1])/step_number};
		dieLabel1.setLocation(coordinate1[0], coordinate1[1]);
		dieLabel2.setLocation(coordinate2[0], coordinate2[1]);
		dieLabel3.setLocation(coordinateS[0], coordinateS[1]);
		if(value3 == BUS_ROLL) {
			pathSpeed = BUS_ROLL_PATH;
		} else if(value3 == MR_MONOPOLY_ROLL1 || value3 == MR_MONOPOLY_ROLL2) {
			pathSpeed = MR_MONOPOLY_PATH;
		} else {
			pathSpeed = getPath(value3);
		}
		
		dieLabel1.setIcon(new ImageIcon("resources/DiePictures/" + diePictures[value1-1]));
		dieLabel2.setIcon(new ImageIcon("resources/DiePictures/" + diePictures[value2-1]));
		dieLabel3.setIcon(new ImageIcon(pathSpeed));
	}

	@Override
	public void onTurnEvent(Object source, String name, int playerId) {
		btnRollDice.setEnabled(DomainController.isYourTurn());
	}
}

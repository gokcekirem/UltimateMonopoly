package kapitalMonopolyUI;

import kapitalMonopoly.Animation;
import kapitalMonopoly.DomainController;
import kapitalMonopoly.MonopolyGame;
import kapitalMonopolyObservers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class PlayerPanel implements TurnListener, DrawListener, NetworkListener, JailListener, RollListener, PurchaseListener {

	protected static final int PANEL_WIDTH = 675;
	protected static final int PANEL_HEIGHT = 140; //225

	protected final int[] BUY_BUTTON = {160, 56, 100, 30};
	protected final int[] SELL_BUTTON = {260, 56, 100, 30};
	protected final int[] END_BUTTON = {360, 56, 100, 30};
	protected final int[] PLAYER_LABEL = {12, 13, 131, 67};
	protected final int[] DEED_LABEL = {12, 80, 150, 67};
	protected final int[] BALANCE_LABEL = {12, 92, 150, 67};
	protected final int[] DEED_INFO_LABEL = {160, 85, 300, 29};
	protected final int[] DEEDS_LABEL = {470, 56, 200, 60};
	protected final int[] CARDS_LABEL = {470, 0, 200, 60};
	//protected final int[] SAVE_CARD = {310, 106, 100, 30};
	protected final int[] USE_CARD = {210, 106, 100, 30};
	protected final int[] JAIL_BUTTON = {60, 56, 100, 30}; //on top of buy button
	protected final int[] PAUSE_BUTTON = {190,13, 75, 30};
	protected final int[] RESUME_BUTTON = {265,13, 80, 30};
	protected final int[] SAVE_BUTTON = {345, 13, 75, 30};

	protected JPanel playerJPanel = new JPanel();
	protected static JLabel playerBalanceLabel;
	protected JLabel playerLabel;
	protected JLabel playerDeedLabel;
	protected JLabel lblSavedCards;
	protected static JLabel deedInformationLabel;
	protected static JButton buyDeedButton;
	protected static JButton sellDeedButton;
	public static JButton endTurnButton;
	protected static JComboBox playerDeeds;
	protected static JComboBox playerCards;
	protected static JButton getOutOfJailWithBailButton;
	protected static JButton useSavedCardButton;
	protected static JButton pauseButton;
	protected static JButton resumeButton;
	protected static JButton saveButton;

	public PlayerPanel() {
		initialize();
		buyDeedButton.setEnabled(DomainController.isYourTurn());
		sellDeedButton.setEnabled(DomainController.isYourTurn());
		endTurnButton.setEnabled(DomainController.isYourTurn());
		
		lblSavedCards = new JLabel("Saved Cards:");
		lblSavedCards.setHorizontalAlignment(SwingConstants.CENTER);
		lblSavedCards.setBounds(473, 54, 189, 25);
		playerJPanel.add(lblSavedCards);
	}

	private void initialize() {

		playerJPanel.setBounds(BoardPanel.BOARD_WIDTH + MonopolyGameFrame.BREAK_AMOUNT, MonopolyGameFrame.BREAK_AMOUNT, 679, 155);		
		playerJPanel.setLayout(null);

		DomainController.addTurnListener(this);
		DomainController.addDrawListener(this);
		DomainController.addNetworkListener(this);
		DomainController.addJailListener(this);
		DomainController.addRollListener(this);
		DomainController.addPurchaseListener(this);

		playerLabel = new JLabel("Player 1's Panel");
		
		playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerLabel.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 25));
		deedInformationLabel = new JLabel("");
		deedInformationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerBalanceLabel = new JLabel("Balance: " + DomainController.getPlayerBalance());
		playerDeedLabel = new JLabel("Deeds: " + DomainController.getPlayerDeeds().size());
		playerDeedLabel.setHorizontalAlignment(SwingConstants.CENTER);

		playerDeeds = new JComboBox();
		playerCards = new JComboBox();

		playerDeeds.setVisible(true);
		addDeedsToCombo();

		buyDeedButton = new JButton("Buy Deed");
		buyDeedButton.setBounds(120, 119, BUY_BUTTON[2], BUY_BUTTON[3]);

		buyDeedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				attemptBuyDeed(false);
				addDeedsToCombo();
				addCardsToCombo();
			}
		});

		endTurnButton = new JButton("End Turn");
		endTurnButton.setBounds(317, 119, 144, 30);

		endTurnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DomainController.updateCurrentPlayer();
				setButtons();
			}
		});

		endTurnButton.setVisible(true);
		endTurnButton.setEnabled(false);

		sellDeedButton = new JButton("Sell Deed");
		sellDeedButton.setBounds(217, 119, SELL_BUTTON[2], SELL_BUTTON[3]);
		sellDeedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				attemptSellDeed(false);
			}

		});

		getOutOfJailWithBailButton = new JButton("Pay Bail");
		getOutOfJailWithBailButton.setBounds(27, 119, JAIL_BUTTON[2], JAIL_BUTTON[3]);

		getOutOfJailWithBailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currentPlayerId = DomainController.getCurrentPlayerID();
				deedInformationLabel.setText("Got out of jail with bail");
				DomainController.getOutOfJailWithBail(currentPlayerId);
				endTurnButton.setEnabled(true);
				deedInformationLabel.setText("");
				sellDeedButton.setEnabled(true);
				buyDeedButton.setEnabled(true);
				getOutOfJailWithBailButton.setEnabled(false);
				PlayerPanel.playerBalanceLabel.setText("Balance: "+ DomainController.getPlayerBalance());
			}
		});

		getOutOfJailWithBailButton.setVisible(true);
		getOutOfJailWithBailButton.setEnabled(false);


		playerDeeds.setBounds(473, 119, 200, 38);
		playerCards.setBounds(473, 77, 200, 30);
		playerLabel.setBounds(29, 6, 613, 38);
		playerDeedLabel.setBounds(482, 105, 177, 25);
		playerBalanceLabel.setBounds(21, 84, 136, 38);
		deedInformationLabel.setBounds(39, 56, 422, 29);

		useSavedCardButton = new JButton("Use Card");
		useSavedCardButton.setBounds(370, 97, USE_CARD[2], USE_CARD[3]);
		useSavedCardButton.setVisible(true);

		useSavedCardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currentPlayerId = DomainController.getCurrentPlayerID();
				String selectedCard = (String) playerCards.getSelectedItem();
				endTurnButton.setVisible(false);
				deedInformationLabel.setText(DomainController.useSavedCard("Get_Out_of_Jail_Free",currentPlayerId));
				sellDeedButton.setEnabled(true);
				buyDeedButton.setEnabled(true);
				useSavedCardButton.setEnabled(DomainController.isYourTurn());
				endTurnButton.setEnabled(true);
				playerBalanceLabel.setText("Balance: "+ DomainController.getPlayerBalance());
				addCardsToCombo();

			}
		});

		useSavedCardButton.setEnabled(DomainController.isYourTurn());

		pauseButton = new JButton("Pause");
		pauseButton.setBounds(147,97,PAUSE_BUTTON[2],PAUSE_BUTTON[3]);
		pauseButton.setVisible(true);
		pauseButton.setEnabled(DomainController.isYourTurn());
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buyDeedButton.setEnabled(false);
				sellDeedButton.setEnabled(false);
				endTurnButton.setEnabled(false);
				useSavedCardButton.setEnabled(false);
				pauseButton.setEnabled(false);
				resumeButton.setEnabled(true);
				getOutOfJailWithBailButton.setEnabled(false);
				DomainController.pauseGame();
				saveButton.setEnabled(true);
				if(Animation.moveThread != null)
					Animation.moveThread.suspend();
			}
		});

		resumeButton = new JButton("Resume");
		resumeButton.setBounds(218,97,90,30);
		resumeButton.setVisible(true);
		resumeButton.setEnabled(false);
		resumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buyDeedButton.setEnabled(true);
				sellDeedButton.setEnabled(true);
				endTurnButton.setEnabled(true);
				useSavedCardButton.setEnabled(true);
				pauseButton.setEnabled(true);
				resumeButton.setEnabled(false);
				DomainController.resumeGame();
				saveButton.setEnabled(false);
				if(Animation.moveThread != null)
					Animation.moveThread.resume();
			}
		});

		saveButton = new JButton("Save");
		saveButton.setBounds(300,97,SAVE_BUTTON[2],SAVE_BUTTON[3]);
		saveButton.setVisible(true);
		saveButton.setEnabled(false);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DomainController.saveGame();
			}
		});

		playerJPanel.add(playerLabel);
		playerJPanel.add(buyDeedButton);
		playerJPanel.add(sellDeedButton);
		playerLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		playerJPanel.add(deedInformationLabel);
		playerJPanel.add(playerDeedLabel);
		playerJPanel.add(playerDeeds);
		playerJPanel.add(playerCards);
		playerJPanel.add(endTurnButton);
		playerJPanel.add(playerBalanceLabel);
		playerJPanel.add(useSavedCardButton);
		playerJPanel.add(getOutOfJailWithBailButton);
		playerJPanel.add(pauseButton);
		playerJPanel.add(resumeButton);
		playerJPanel.add(saveButton);

		addCardsToCombo();
		addDeedsToCombo();
	}


	@Override
	public void onTurnEvent(Object source, String name, int playerId) {
		setButtons();
	}

	private void setButtons(){
		playerLabel.setText("Player " + MonopolyGame.getCurrentPlayerID() + "'s Label");
		playerBalanceLabel.setText("Balance: "+ DomainController.getPlayerBalance());
		playerDeedLabel.setText("Deeds: "+ DomainController.getPlayerDeeds().size());

		useSavedCardButton.setEnabled(DomainController.isYourTurn());
		endTurnButton.setEnabled(DomainController.isYourTurn());
		deedInformationLabel.setText("");
		pauseButton.setEnabled(DomainController.isYourTurn());
		if(!DomainController.isPlayerInJail(DomainController.getCurrentPlayerID())){
			buyDeedButton.setEnabled(DomainController.isYourTurn());
			sellDeedButton.setEnabled(DomainController.isYourTurn());
		} else {
			getOutOfJailWithBailButton.setEnabled(true);
			endTurnButton.setEnabled(false);
			sellDeedButton.setEnabled(false);
			buyDeedButton.setEnabled(false);
		}
		addDeedsToCombo();
		addCardsToCombo();
	}

	public void addDeedsToCombo() {
		ArrayList<String> deeds = DomainController.getPlayerDeeds();
		playerDeeds.removeAllItems();
		Iterator itr = deeds.iterator();
		while(itr.hasNext()) {
			playerDeeds.addItem(itr.next().toString());
		}
	}

	public void addCardsToCombo() {
		ArrayList<String> cards = DomainController.getPlayerOwnedCards();
		playerCards.removeAllItems();
		Iterator itr = cards.iterator();
		while(itr.hasNext()) {
			String cardName = itr.next().toString();
			cardName = cardName.substring(cardName.indexOf("/")+1);
			playerCards.addItem(cardName.substring(cardName.indexOf("/")+1, cardName.indexOf(".")));
		}
	}

	@Override
	public void onDrawEvent(Object source, String name, String value) {
		playerBalanceLabel.setText("Balance: " + DomainController.getPlayerBalance());
		playerDeedLabel.setText("Deeds: "+ DomainController.getPlayerDeeds().size());
		addDeedsToCombo();
		addCardsToCombo();
	}


	@Override
	public void onNetworkEvent(Object source, String name, String value) {
		if(name.equals("BUYDEED")) {
			attemptBuyDeed(true);
		} else if(name.equals("SELLDEED")){
			attemptSellDeed(true);
		} else if(name.equals("ENDTURN")){
			setButtons();
		} else if(name.equals("PAUSE")){
			pauseButton.doClick();
		} else if(name.equals("RESUME")){
			resumeButton.doClick();
		} else if(name.equalsIgnoreCase("PAYJAILBAIL")){
			playerBalanceLabel.setText("Balance: "+ DomainController.getPlayerBalance());
		}
	}


	private void attemptBuyDeed(boolean networkUpdate){
		try {
			if(networkUpdate){
				deedInformationLabel.setText(DomainController.buyDeedHelper());
			} else{
				deedInformationLabel.setText(DomainController.buyDeed());
			}
			playerBalanceLabel.setText("Balance: "+ DomainController.getPlayerBalance());
			playerDeedLabel.setText("Deeds: "+ DomainController.getPlayerDeeds().size());
			addDeedsToCombo();
			addDeedsToCombo();
		} catch (Error e1) {
			deedInformationLabel.setText("something is wrong");
		}
	}

	private void attemptSellDeed(boolean networkUpdate){
		int currentPlayerId = DomainController.getCurrentPlayerID();
		if(networkUpdate){
			deedInformationLabel.setText(DomainController.sellDeedHelper(currentPlayerId));
		} else{
			deedInformationLabel.setText(DomainController.sellDeed(currentPlayerId));
		}
		playerDeedLabel.setText("Deeds: "+DomainController.getPlayerDeeds().size());
		playerBalanceLabel.setText("Balance: "+ DomainController.getPlayerBalance());
		sellDeedButton.setEnabled(false);
		buyDeedButton.setEnabled(DomainController.isYourTurn());
	}

	@Override
	public void onRollEvent(Object source, String name, String value) {
		endTurnButton.setEnabled(DomainController.isYourTurn() && value.charAt(0)!=value.charAt(1));
	}

	@Override
	public void onJailEvent() {
		if (DomainController.getIsPlayerInJail()) {
			getOutOfJailWithBailButton.setEnabled(true);
			DiePanel.btnRollDice.setEnabled(false);
			buyDeedButton.setEnabled(false);
			sellDeedButton.setEnabled(false);
			endTurnButton.setEnabled(true);
		}
	}


	@Override
	public void onPurchaseEvent(Object source, String name, String value) {
		addDeedsToCombo();
		deedInformationLabel.setText(value);
		playerDeedLabel.setText("Deeds: "+ DomainController.getPlayerDeeds().size());
		playerBalanceLabel.setText("Balance: "+ DomainController.getPlayerBalance());

	}
}

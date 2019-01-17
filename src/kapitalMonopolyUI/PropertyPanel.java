package kapitalMonopolyUI;

import kapitalMonopoly.DomainController;
import kapitalMonopolyObservers.ConstructionListener;
import kapitalMonopolyObservers.NetworkListener;
import kapitalMonopolyObservers.TurnListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

public class PropertyPanel implements TurnListener, ConstructionListener, NetworkListener {

	public static final int PANEL_WIDTH = 300;
	public static final int PANEL_HEIGHT = 90;
	
	JPanel propertyJPanel = new JPanel();

	static JRadioButton house;
	static JRadioButton hotel;
	static JRadioButton skyscraper;

	final int[] BUY_PROPERTY_BUTTON = {0, 24, 150, 30};
	final int[] SELL_PROPERTY_BUTTON = {150, 24, 150, 30};
	
	final int[] HOUSE = {0, 30, 100, 30};
	final int[] HOTEL = {100, 30, 100, 30};
	final int[] SKYSCRAPER = {200, 30, 100, 30};
	final int[] PROPERTY_INFO_LABEL = {5, 60, 295, 30};
	
	static JButton buyPropertyButton;
	static JButton sellPropertyButton;
	static JLabel propertyInformationLabel;


	public PropertyPanel(){
		initialize();
		propertyJPanel.setVisible(true);
	}
	
	private void initialize() {
		
		propertyJPanel.setBounds(BoardPanel.BOARD_WIDTH + MonopolyGameFrame.BREAK_AMOUNT + MonopolyGameFrame.BREAK_AMOUNT + CardPanel.PANEL_WIDTH,
				PlayerPanel.PANEL_HEIGHT + MonopolyGameFrame.BREAK_AMOUNT*2, 300, 87);
		propertyJPanel.setLayout(null);
		
		DomainController.addTurnListener(this);
		DomainController.addConstructionListener(this);
        DomainController.addNetworkListener(this);
		
		propertyInformationLabel = new JLabel("");
		propertyInformationLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		propertyInformationLabel.setHorizontalAlignment(SwingConstants.CENTER);

		house = new JRadioButton("House");
		house.setActionCommand("House");
		hotel = new JRadioButton("Hotel");
		hotel.setActionCommand("Hotel");
		skyscraper = new JRadioButton("Skyscraper");
		skyscraper.setActionCommand("Skyscraper");
        ButtonGroup props = new ButtonGroup();
		props.add(house);
		props.add(hotel);
		props.add(skyscraper);

		house.setVisible(true);
		hotel.setVisible(true);
		skyscraper.setVisible(true);
		house.setSelected(false);
		
		buyPropertyButton = new JButton("Buy Property");
		buyPropertyButton.setBounds(BUY_PROPERTY_BUTTON[0], BUY_PROPERTY_BUTTON[1], BUY_PROPERTY_BUTTON[2], BUY_PROPERTY_BUTTON[3]);
		buyPropertyButton.setVisible(true);

		buyPropertyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				attemptBuyProperty(props.getSelection().getActionCommand().toString(), false);
			}
		});

		sellPropertyButton = new JButton("Sell Property");
		sellPropertyButton.setBounds(SELL_PROPERTY_BUTTON[0], SELL_PROPERTY_BUTTON[1], SELL_PROPERTY_BUTTON[2], SELL_PROPERTY_BUTTON[3]);
		sellPropertyButton.setVisible(true);

		sellPropertyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                attemptSellProperty(props.getSelection().getActionCommand().toString(), false);
			}
		});
		
		
		
		propertyInformationLabel.setBounds(0, 53, 300, 30);
		house.setBounds(6, 0, HOUSE[2], HOUSE[3]);
		hotel.setBounds(100, 0, HOTEL[2], HOTEL[3]);
		skyscraper.setBounds(183, 0, SKYSCRAPER[2], SKYSCRAPER[3]);
		
		propertyJPanel.add(buyPropertyButton);
		propertyJPanel.add(sellPropertyButton);
		propertyJPanel.add(propertyInformationLabel);
		propertyJPanel.add(house);
		propertyJPanel.add(hotel);
		propertyJPanel.add(skyscraper);

		buyPropertyButton.setEnabled(false);
		sellPropertyButton.setEnabled(false);

	}

    private void attemptBuyProperty(String type, boolean networkUpdate){
		int currentPlayerId = DomainController.getCurrentPlayerID();
		String squareName = PlayerPanel.playerDeeds.getSelectedItem().toString();
        if(networkUpdate){
        	propertyInformationLabel.setText(DomainController.buyPropertyHelper(squareName, type));
        } else{
        	propertyInformationLabel.setText(DomainController.buyProperty(squareName, type));
        }
        PlayerPanel.playerBalanceLabel.setText("Balance: "+ DomainController.getPlayerBalance());
        sellPropertyButton.setEnabled(false);
    }

    private void attemptSellProperty(String type, boolean networkUpdate){
		int currentPlayerId = DomainController.getCurrentPlayerID();
		String squareName = PlayerPanel.playerDeeds.getSelectedItem().toString();
		if(networkUpdate){
			propertyInformationLabel.setText(DomainController.sellPropertyHelper(squareName, type));
		} else{
			propertyInformationLabel.setText(DomainController.sellProperty(squareName, type));
		}
		PlayerPanel.playerBalanceLabel.setText("Balance: "+ DomainController.getPlayerBalance());
		buyPropertyButton.setEnabled(false);
    }

	@Override
	public void onConstructionEvent(Object source, String name, String value) {
		updatePanel();
	}

	@Override
	public void onTurnEvent(Object source, String name, int playerId) {
		updatePanel();
	}

	@Override
	public void onNetworkEvent(Object source, String name, String value) {
		if(name.equals("BUYPROPERTY")) {
			attemptBuyProperty(value, true);
		} else if(name.equals("SELLPROPERTY")){
			attemptSellProperty(value, true);
		}
	}

	private void updatePanel(){
		boolean panelEnable = DomainController.isYourTurn() && DomainController.hasDeeds();
		buyPropertyButton.setEnabled(panelEnable);
		sellPropertyButton.setEnabled(panelEnable);
		house.setEnabled(panelEnable);
		hotel.setEnabled(panelEnable);
		skyscraper.setEnabled(panelEnable);
	}
}

package kapitalMonopolyUI;

import kapitalMonopoly.DomainController;
import kapitalMonopolyObservers.DeedListener;
import kapitalMonopolyObservers.TurnListener;

import javax.swing.*;


public class DeedPanel implements DeedListener, TurnListener {

	public static final int PANEL_WIDTH = 290;
	public static final int PANEL_HEIGHT = 360;
	
	protected static final int[] DEED_COORDINATES = {20, 0};
	protected static final int[] DEED_IMAGE_SIZE = {270, 360};
	
	protected JPanel deedJPanel = new JPanel();
	protected static JLabel deedLabel = new JLabel();
	
	/**
	 * Create the panel.
	 */
	public DeedPanel() {
		initialize();
	}
	
	private void initialize() {
		
		DomainController.addDeedListener(this);
		DomainController.addTurnListener(this);
		deedJPanel.setBounds(BoardPanel.BOARD_WIDTH + CardPanel.PANEL_WIDTH + MonopolyGameFrame.BREAK_AMOUNT, PlayerPanel.PANEL_HEIGHT + PropertyPanel.PANEL_HEIGHT + MonopolyGameFrame.BREAK_AMOUNT, PANEL_WIDTH, PANEL_HEIGHT);
		deedJPanel.setLayout(null);
		
		deedLabel.setBounds(DEED_COORDINATES[0], DEED_COORDINATES[1], DEED_IMAGE_SIZE[0], DEED_IMAGE_SIZE[1]);
		deedJPanel.add(deedLabel);
		
	}
	
	@Override
	public void onDeedEvent(Object source, String name, String value) {
		// UI doesn't know how Domain logic works, so contacts the controller only to obtain the required information
		deedLabel.setIcon(new ImageIcon("resources/DeedCardPics/" +value+".png")); // Example
		deedJPanel.add(deedLabel);
		deedJPanel.setVisible(true);
		deedLabel.setVisible(true);
	}

	@Override
	public void onTurnEvent(Object source, String name, int playerId) {
		deedJPanel.setVisible(false);
		deedLabel.setVisible(false);
	}
}

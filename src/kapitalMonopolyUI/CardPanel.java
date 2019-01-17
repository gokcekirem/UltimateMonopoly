package kapitalMonopolyUI;

import kapitalMonopoly.DomainController;
import kapitalMonopolyObservers.DrawListener;
import kapitalMonopolyObservers.TurnListener;

import javax.swing.*;

public class CardPanel implements DrawListener, TurnListener {

	/**
	 * Create the panel.
	 */
	protected static final int PANEL_WIDTH = 360;
	protected static final int PANEL_HEIGHT = 200;
	protected static final int[] CARD_COORDINATES = {16, 6};
	protected static final int[] CARD_IMAGE_SIZE = {300,180};
	
	protected JPanel cardJPanel;
	protected static JLabel cardImage;
	
	public CardPanel() {
		initialize();
		
	}

	private void initialize() {

		DomainController.addDrawListener(this);
		DomainController.addTurnListener(this);
		
		cardJPanel = new JPanel();
		cardJPanel.setBounds(BoardPanel.BOARD_WIDTH + MonopolyGameFrame.BREAK_AMOUNT*2,
				PlayerPanel.PANEL_HEIGHT + MonopolyGameFrame.BREAK_AMOUNT*2, 300, 181);
		cardJPanel.setLayout(null);
		
		cardImage = new JLabel("");

		cardImage.setBounds(0, 0, CARD_IMAGE_SIZE[0], CARD_IMAGE_SIZE[1]);
		cardJPanel.add(cardImage);
	}

	@Override
	public void onDrawEvent(Object source, String name, String value) {
		cardJPanel.setVisible(true);
		cardImage.setVisible(true);
		cardImage.setIcon(new ImageIcon(value));
	}

	@Override
	public void onTurnEvent(Object source, String name, int playerId) {
		cardJPanel.setVisible(false);
		cardImage.setVisible(false);
	}
}

package kapitalMonopolyUI;

import kapitalMonopoly.DomainController;
import kapitalMonopolyObservers.AnimationMoveListener;
import kapitalMonopolyObservers.ConstructionListener;

import javax.swing.*;
import java.util.HashMap;

public class BoardPanel implements ConstructionListener, AnimationMoveListener {

	private static final String PROPERTY_ICON_FILE = "resources/PropertyPics/";
	private static final String PROPERTY_ICON_EXTENTION = ".png";
	protected static final int BOARD_WIDTH = 650;
	protected static final int BOARD_HEIGHT = 650;
	protected static final int[] BOARD_COORDINATES = {25 , 25};
	protected final String BOARD_IMAGE_PATH = "resources/BoardPic/board_with_portals.png";
	private static final int[] HOUSE_SIZE = {30,33};
	private static final int[] HOTEL_SIZE= {32,32};
	private static final int[] SKYSCRAPER_SIZE= {35,45};
	protected static JPanel boardJPanel;
	protected JLabel boardLabel;
	protected static JLabel[] pieceLabel = new JLabel[DomainController.getPlayerCount()];
	protected int[] iconSize = {DomainController.getIconSize()[0], DomainController.getIconSize()[1]};
	HashMap<String, JLabel> propertyImages = new HashMap<>();

	/**
	 * Create the panel.
	 */
	public BoardPanel() {
		initialize();
	}

	private void initialize() {

		DomainController.addConstructionListener(this);
		DomainController.addAnimationMoveListener(this);
		
		boardJPanel = new JPanel();
		boardJPanel.setBounds(BOARD_COORDINATES[0], BOARD_COORDINATES[1], BOARD_WIDTH, BOARD_HEIGHT);
		boardJPanel.setLayout(null);

		boardLabel = new JLabel("");
		boardLabel.setIcon(new ImageIcon(BOARD_IMAGE_PATH));
		boardLabel.setBounds(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		boardJPanel.add(boardLabel);

		for(int i=0;i<pieceLabel.length;i++) {
			pieceLabel[i] = new JLabel("");
			pieceLabel[i].setIcon(new ImageIcon(DomainController.getPlayerIcon(i+1)));
			int[] coordinates = DomainController.getPlayerCoordinates(i+1);
			pieceLabel[i].setBounds(coordinates[0], coordinates[1], iconSize[0], iconSize[1]);
			boardLabel.add(pieceLabel[i]);
		}
	}
	
	

	@Override
	public void onConstructionEvent(Object source, String name, String value) {
		String imagePath = PROPERTY_ICON_FILE;
		JLabel tempImage = new JLabel("");

		if(name.contains("House")){
			int houseCount = Integer.parseInt("" + name.charAt(name.length()-1));
			imagePath += "house" + houseCount + PROPERTY_ICON_EXTENTION;
			tempImage.setBounds(DomainController.getPropertyLocation(value)[0], DomainController.getPropertyLocation(value)[1] + SKYSCRAPER_SIZE[1] - HOUSE_SIZE[1], HOUSE_SIZE[0],HOUSE_SIZE[1]);
		} else if(name.contains("Hotel")){
			if(name.contains("Bought")){
				imagePath += "hotel" + PROPERTY_ICON_EXTENTION;
				tempImage.setBounds(DomainController.getPropertyLocation(value)[0], DomainController.getPropertyLocation(value)[1] + SKYSCRAPER_SIZE[1] - HOTEL_SIZE[1], HOTEL_SIZE[0],HOTEL_SIZE[1]);
			} else{
				imagePath += "house4" + PROPERTY_ICON_EXTENTION;
				tempImage.setBounds(DomainController.getPropertyLocation(value)[0], DomainController.getPropertyLocation(value)[1] + SKYSCRAPER_SIZE[1] - HOUSE_SIZE[1], HOUSE_SIZE[0],HOUSE_SIZE[1]);
			}
		} else if(name.contains("Skyscraper")){
			if(name.contains("Bought")){
				imagePath += "Skyscraper" + PROPERTY_ICON_EXTENTION;
				tempImage.setBounds(DomainController.getPropertyLocation(value)[0], DomainController.getPropertyLocation(value)[1], SKYSCRAPER_SIZE[0],SKYSCRAPER_SIZE[1]);
			} else{
				imagePath += "hotel" + PROPERTY_ICON_EXTENTION;
				tempImage.setBounds(DomainController.getPropertyLocation(value)[0], DomainController.getPropertyLocation(value)[1] + SKYSCRAPER_SIZE[1] - HOTEL_SIZE[1], HOTEL_SIZE[0],HOTEL_SIZE[1]);
			}

		}
		if(propertyImages.containsKey(value)){
			boardLabel.remove(propertyImages.get(value));
		}
		boardLabel.add(tempImage);
		tempImage.setIcon(new ImageIcon(imagePath));
		propertyImages.put(value,tempImage);
		System.out.println(imagePath);
	}

	@Override
	public void onAnimationMoveEvent(Object source, String name, int currentPlayerId, int coordinateX,
			int coordinateY) {
		pieceLabel[currentPlayerId-1].setLocation(coordinateX, coordinateY);
		
	}
}

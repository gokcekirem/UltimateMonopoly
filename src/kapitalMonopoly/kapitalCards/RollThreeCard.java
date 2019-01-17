package kapitalMonopoly.kapitalCards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RollThreeCard extends Card implements Serializable{
	
	private static final String CARD_FILES_PATH  = "resources/Roll3DiceCards/";
	private static final String CARD_FILE_EXTENSION  = ".png";
	private static final boolean SAVABLE = true;


	int die1;
	int die2;
	int die3;

	int cardNumber;


	public RollThreeCard(int cardNumber, String cardPath) {
		super(CARD_FILES_PATH + cardPath + CARD_FILE_EXTENSION, SAVABLE);
		this.die1 = Integer.parseInt("" + cardPath.charAt(10));
		this.die2 = Integer.parseInt("" + cardPath.charAt(11));
		this.die3 = Integer.parseInt("" + cardPath.charAt(12));
		this.cardNumber = cardNumber;
	}
	
	public List<Integer> getDiceValues() {
		 List<Integer> dieValues = new ArrayList<>();
		 dieValues.add(die1);
		 dieValues.add(die2);
		 dieValues.add(die3);
		return dieValues;
	}
	
	public static String getRoll3Path() {
		return CARD_FILES_PATH;
	}
	
	@Override
	public void doAction() {
		
		switch(cardNumber) {
		case 1:
			
			break;
		case 2:
			break;	
		case 3:
			break;
		case 4:
			break;	
		case 5:
			break;
		case 6:
			break;	
		case 7:
			break;
		case 8:
			break;	
		case 9:
			break;
		case 10:
			break;	
		case 11:
			break;
		case 12:
			break;	
		case 13:
			break;	
		case 14:
			break;
		case 15:
			break;	
		case 16:
			break;	
		case 17:
			break;
		case 18:
			break;	
		case 19:
			break;	
		case 20:
			break;	
		case 21:
			break;	
		case 22:
			break;	
		case 23:
			break;
		case 24:
			break;
		default:
			break;
		}
	}



}

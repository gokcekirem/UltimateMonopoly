package kapitalMonopoly;

import java.io.Serializable;
import java.util.Random;

public class SpeedDie implements Die, Serializable {
	
	private int faceValue;
	
	public SpeedDie() {

	}
	
	@Override
	public int rollDie() {
		Random rand = new Random();
		faceValue = rand.nextInt(6) + 1;
		return faceValue;
	}
}

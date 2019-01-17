package kapitalMonopoly;
import java.io.Serializable;
import java.util.Random;

public class RegularDie implements Die, Serializable {
	private int faceValue;
	
	private static final int MAX_FACE_VALUE = 6;
	private static final int MIN_FACE_VALUE = 1;
	
	public RegularDie() {
		
	}
	
	@Override
	public int rollDie() {
		Random rand = new Random();
		faceValue = rand.nextInt(MAX_FACE_VALUE) + MIN_FACE_VALUE;
		return faceValue;
	}
	
	
}

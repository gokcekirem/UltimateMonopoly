package kapitalBot;

import kapitalMonopoly.MonopolyGame;

public class AggressiveBot extends Bot implements BotInterface{

	private static final String BOT_TYPE = "Aggressive";

	public AggressiveBot(String icon, int ID) {
		super(icon, ID, BOT_TYPE) ;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		aggressiveAct();
	}
	
	
	private void aggressiveAct() {
		int[] rolled = MonopolyGame.getInstance().rollDice();
		networkAct(rolled);
	}

	public void networkAct(int[] rolled){
		if (this.isOnBuyableDeed()) {
			this.buyDeed();
		}
	}

	
}

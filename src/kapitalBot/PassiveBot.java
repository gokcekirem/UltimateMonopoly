package kapitalBot;

import kapitalMonopoly.MonopolyGame;

public class PassiveBot extends Bot implements BotInterface{

	private static final String BOT_TYPE = "Passive";

	public PassiveBot(String icon, int ID) {
		super(icon, ID, BOT_TYPE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		passiveAct();
	}

	private void passiveAct() {
		if(isAdmin()){
			int[] rolled = MonopolyGame.getInstance().rollDice();
			networkAct(rolled);
		}
	}

	public void networkAct(int[] rolled){
		// This bot does not do anything by definition, so no methods needed here.
	}
	

}

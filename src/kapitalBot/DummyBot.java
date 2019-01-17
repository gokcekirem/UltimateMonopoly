package kapitalBot;

import kapitalMonopoly.MonopolyGame;

public class DummyBot extends Bot implements BotInterface{

	private static final String BOT_TYPE = "Dummy";

	public DummyBot(String icon, int ID) {
		super(icon, ID, BOT_TYPE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		dummyAct();
	}

	private void dummyAct() {
		if(isAdmin()){
			int[] rolled = MonopolyGame.getInstance().rollDice();
			networkAct(rolled);
		}
	}

	public void networkAct(int[] rolled){
		int total = 0;
		for(int i=0;i<rolled.length;i++) {
			total += rolled[i];
		}
		if(total<10 && this.isOnBuyableDeed()) {
			this.buyDeed();
		}
	}


}

package kapitalBot;

import kapitalMonopoly.MonopolyGame;

public class ReasonableBot extends Bot implements BotInterface{

	private static final String BOT_TYPE = "Reasonable";

	public ReasonableBot(String icon, int ID) {
		super(icon, ID, BOT_TYPE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		reasonableAct();
	}

	private void reasonableAct() {
		if(isAdmin()){
			int[] rolled = MonopolyGame.getInstance().rollDice();
			networkAct(rolled);
		}
	}

	@Override
	public void networkAct(int[] rolled) {
		// TODO Auto-generated method stub
		if(this.getBalance().getBalanceMoneyAmount()>1000 && this.isOnBuyableDeed() && this.getOwnedDeeds().size()<16) {
			this.buyDeed();
		}
		
		if(this.isInJail() && this.getOwnedCards().contains("Get_Out_of_Jail_Free")) {
			this.useCard("Get_Out_of_Jail_Free");
		}
	}


	

}

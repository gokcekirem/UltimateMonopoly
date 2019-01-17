package kapitalBot;

public interface BotInterface {

		void act();
		void networkAct(int[] rolled);
		boolean isAdmin();
}

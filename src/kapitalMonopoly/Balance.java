package kapitalMonopoly;

import java.io.Serializable;

public class Balance implements Serializable{
	
	private static final int INITIAL_BALANCE = 3200;
	
	private Money balanceMoney;

	public Balance() {
		balanceMoney=new Money();
		balanceMoney.setAmount(INITIAL_BALANCE);
	}
	public Money getBalanceMoney() {
		return balanceMoney;
	}
	public void setAmount(double amount) {
		balanceMoney.setAmount(amount);
	}
	
	public void increaseAmount(double amount) {
		balanceMoney.increaseAmount(amount);
	}
	
	public void decreaseAmount(double amount) {
		balanceMoney.decreaseAmount(amount);
	}

    public double getBalanceMoneyAmount(){
        return balanceMoney.getAmount();
    }

	@Override
	public String toString() {
		return "Player's Balance =" + balanceMoney.getAmount();
	}
	
	
	
}

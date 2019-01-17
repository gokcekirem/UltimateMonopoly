package kapitalMonopoly;

import java.io.Serializable;

public class Money implements Serializable{
	
	private static String units;
	private double amount;
	
	public Money() {
		units = "Monopoly Money";
		amount = 0;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public void increaseAmount(double amount) {
		this.amount+=amount;
	}
	
	public void decreaseAmount(double amount) {
		this.amount-=amount;
	}
	
	
	
	
}

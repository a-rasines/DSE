package domain;

import java.io.Serializable;

public class Transaction implements Serializable{

	private static final long serialVersionUID = 2070953433673841640L;
	public final String from;
	public final String to;
	public final double amount;
	
	public Transaction(String from, String to, double amount) {
		super();
		this.from = from;
		this.to = to;
		this.amount = amount;
	}
	
	
}

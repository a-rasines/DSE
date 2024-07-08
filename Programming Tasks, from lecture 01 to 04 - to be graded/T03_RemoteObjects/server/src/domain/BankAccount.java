package domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BankAccount {
	private final static Map<String, BankAccount> accounts = new HashMap<>();
	public static BankAccount getAccount(String hash) {
		return accounts.get(hash);
	}
	public static void registerTransaction(Transaction t) {
		accounts.get(t.from).queueTransaction(t);
		accounts.get(t.to).queueTransaction(t);
	}
	public static void addAccount(BankAccount ba) {
		accounts.put(ba.getAccHash(), ba);
		new TransactionHandler(ba).start();
	}
	public static Collection<BankAccount> getAccounts() {
		return accounts.values();
	}
	
	public static Collection<String> getHashes() {
		return accounts.keySet();
	}
	
	private final BlockingQueue<Transaction> transactions = new LinkedBlockingQueue<Transaction>();
	private String accHash;
	private double balance;
	public BankAccount(double balance) {
		super();
		this.accHash = Integer.toHexString(hashCode());
		this.balance = balance;
	}
	public String getAccHash() {
		return accHash;
	}
	public BlockingQueue<Transaction> getTransactionQueue(){
		return transactions;
	}
	public void queueTransaction(Transaction t) {
		transactions.add(t);
	}
	public void setAccHash(String accHash) {
		this.accHash = accHash;
	}
	public double getBalance() {
		return balance;
	}
	public void modBalance(double balance) {
		this.balance += balance;
	}
	
	private static class TransactionHandler extends Thread {
		private BankAccount ba;
		private BlockingQueue<Transaction> q;
		public TransactionHandler(BankAccount ba) {
			this.ba = ba;
			this.q = ba.getTransactionQueue();
		}
		@Override 
		public void run() {
			while(true)
				try {
					Transaction t = q.take();
					Thread.sleep(new Random().nextInt(1000, 20000));
					System.out.println("Working on transaction " +t.hashCode()+" in account " + ba.accHash);
					ba.modBalance(t.from.equals(ba.getAccHash())?-t.amount:t.amount);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
}

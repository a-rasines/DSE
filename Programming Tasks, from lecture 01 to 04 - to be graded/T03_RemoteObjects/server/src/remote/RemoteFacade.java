package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;

import domain.BankAccount;
import domain.Transaction;

public class RemoteFacade extends UnicastRemoteObject implements IRemoteFacade {

	private static final long serialVersionUID = -7150245000960352568L;

	public RemoteFacade() throws RemoteException {
		super();
	}
	
	public double audit() throws RemoteException {
		double b = 0;
		for(BankAccount a: BankAccount.getAccounts())
			b += a.getBalance();
		return b;
	}
	
	public Collection<String> getHashes() throws RemoteException {
		return new ArrayList<>(BankAccount.getHashes());
	}
	
	public boolean transfer(Transaction tr) throws RemoteException{
		BankAccount from = BankAccount.getAccount(tr.from);
		BankAccount to = BankAccount.getAccount(tr.to);
		
		if(from == null || to == null || from.getBalance() < tr.amount || tr.amount < 0) return false;
		BankAccount.registerTransaction(tr);
		return true;
	}
	
	public double getBalance(String hash) throws RemoteException {
		BankAccount b = BankAccount.getAccount(hash);
		if(b != null)
			return b.getBalance();
		return 0;
	}
}

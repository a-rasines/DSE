package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import domain.Transaction;

public interface IRemoteFacade extends Remote{
	public double audit() throws RemoteException;
	public boolean transfer(Transaction tr) throws RemoteException;
	public Collection<String> getHashes() throws RemoteException;
	public double getBalance(String hash) throws RemoteException;
}

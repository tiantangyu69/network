/**
 * 
 */
package org.sagacity.network.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author lizhitao
 * 
 */
public interface CalculatorService extends Remote {
	int add(int a, int b) throws RemoteException;

	int minus(int a, int b) throws RemoteException;
}

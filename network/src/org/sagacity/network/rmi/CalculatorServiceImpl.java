/**
 * 
 */
package org.sagacity.network.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author lizhitao
 *
 */
public class CalculatorServiceImpl extends UnicastRemoteObject implements
		CalculatorService {

	public CalculatorServiceImpl() throws RemoteException {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.sagacity.network.rmi.CalculatorService#add(int, int)
	 */
	@Override
	public int add(int a, int b) throws RemoteException {
		// TODO Auto-generated method stub
		return a + b;
	}

	/* (non-Javadoc)
	 * @see org.sagacity.network.rmi.CalculatorService#plus(int, int)
	 */
	@Override
	public int minus(int a, int b) throws RemoteException {
		// TODO Auto-generated method stub
		return a - b;
	}

}

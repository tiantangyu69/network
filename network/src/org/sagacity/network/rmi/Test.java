/**
 * 
 */
package org.sagacity.network.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * @author lizhitao
 * 
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CalculatorService calculatorService = new CalculatorServiceImpl();
			Context context = new InitialContext();
			LocateRegistry.createRegistry(1099);
			context.bind("rmi:CalculatorService", calculatorService);

			Context context2 = new InitialContext();
			CalculatorService service = (CalculatorService) context2
					.lookup("rmi://localhost/CalculatorService");
			System.out.println(service.add(11, 33));
			
			NamingEnumeration<NameClassPair> enums = context2.list("rmi:");
			while(enums.hasMore()){
				System.out.println(enums.next().getName());
			}
			System.out.println(service.getClass().getName());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

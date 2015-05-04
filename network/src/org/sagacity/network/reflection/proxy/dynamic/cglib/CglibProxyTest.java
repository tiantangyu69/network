/**
 * 
 */
package org.sagacity.network.reflection.proxy.dynamic.cglib;

/**
 * @author lizhitao
 * 
 */
public class CglibProxyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CglibService service = (CglibService) new CglibServiceProxy()
				.newInstance(new CglibServiceImpl());
		service.say();
	}

}

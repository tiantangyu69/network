/**
 * 
 */
package org.sagacity.network.reflection.proxy.dynamic;


/**
 * @author lizhitao
 * 
 */
public class DynamicProxyDemo {

	/**
	 * @param args
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException {
		MyInvocationHandler handler = new MyInvocationHandler();
		EchoService proxyService = (EchoService) handler.bind(new EchoServiceImpl());
		System.out.println("代理类的类名为：" + proxyService.getClass().getName());
		proxyService.echo();
	}

}

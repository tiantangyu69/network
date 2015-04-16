/**
 * 
 */
package org.sagacity.network.reflection.proxy.dynamic.simplermi;


/**
 * @author lizhitao
 * 
 */
public class SimpleClient {

	/**
	 * @param args
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException {
		EchoServiceProxyFactory handler = new EchoServiceProxyFactory(new EchoServiceImpl(),"localhost", 8000);
		EchoService proxyService = (EchoService) handler.newInstance();
		System.out.println("代理类的类名为：" + proxyService.getClass().getName());
		String result = proxyService.echo();
		System.out.println("执行远程方法成功，输出结果为：" + result);
	}

}

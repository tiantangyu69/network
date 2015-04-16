/**
 * 
 */
package org.sagacity.network.reflection.proxy.dynamic.simplermi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author lizhitao
 * 
 */
public class EchoServiceProxyFactory implements InvocationHandler {
	private Object target;
	private String host;
	private int port;

	public EchoServiceProxyFactory(Object target, String host, int port) {
		this.target = target;
		this.host = host;
		this.port = port;
	}

	public Object newInstance() {
		return Proxy.newProxyInstance(target.getClass().getClassLoader(),
				target.getClass().getInterfaces(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Connector connector = new Connector(host, port);
		Call call = new Call(target.getClass().getName(), method.getName(),
				method.getParameterTypes(), args);
		connector.send(call);
		call = (Call) connector.receive();
		Object result = call.getResult();
		if (null != connector) {
			connector.close();
		}
		return result;
	}

}

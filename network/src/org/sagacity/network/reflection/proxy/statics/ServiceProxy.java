/**
 * 
 */
package org.sagacity.network.reflection.proxy.statics;

/**
 * @author lizhitao
 *
 */
public class ServiceProxy implements Service {
	private Service service;
	
	public ServiceProxy(Service service){
		this.service = service;
	}

	/* (non-Javadoc)
	 * @see org.sagacity.network.reflection.proxy.Service#echo(java.lang.String)
	 */
	@Override
	public String echo(String msg) {
		System.out.println("执行echo之前调用=========");
		String str =  service.echo(msg);
		System.out.println("执行echo输出" + str);
		System.out.println("执行echo之后调用========");
		return str;
	}

}

/**
 * 
 */
package org.sagacity.network.reflection.proxy.statics;

/**
 * @author lizhitao
 *
 */
public class ServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Service service = new ServiceProxy(new ServiceImpl());
		service.echo("hello");
	}

}

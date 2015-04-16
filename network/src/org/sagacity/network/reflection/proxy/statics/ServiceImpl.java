/**
 * 
 */
package org.sagacity.network.reflection.proxy.statics;

/**
 * @author lizhitao
 *
 */
public class ServiceImpl implements Service {

	/* (non-Javadoc)
	 * @see org.sagacity.network.reflection.proxy.Service#echo(java.lang.String)
	 */
	@Override
	public String echo(String msg) {
		return "echo:" + msg;
	}

}

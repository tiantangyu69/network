/**
 * 
 */
package org.sagacity.network.reflection.proxy.dynamic.cglib;

/**
 * @author lizhitao
 *
 */
public class CglibServiceImpl implements CglibService {

	/* (non-Javadoc)
	 * @see org.sagacity.network.reflection.proxy.dynamic.cglib.CglibService#say()
	 */
	@Override
	public void say() {
		System.out.println("CglibServiceImpl say()");
	}

}

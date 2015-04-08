/**
 * 
 */
package org.sagacity.network.nio;

import java.nio.ByteBuffer;

/**
 * @author lizhitao
 *
 */
public class ByteBufferDemo2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		byte[] bytes = new byte[]{1,2,3,4,5,6,7,8};
		buffer.put(bytes);
		
	}

}

/**
 * 
 */
package org.sagacity.network.nio;

import java.nio.ByteBuffer;

/**
 * @author lizhitao
 * 
 */
public class ByteBufferDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		System.out.println("新创建buffer：[position:" + buffer.position()
				+ ", limit:" + buffer.limit() + ", capacity:"
				+ buffer.capacity() + "]");
		
		byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		buffer.put(bytes);
		System.out.println("添加数据后buffer：[position:" + buffer.position()
				+ ", limit:" + buffer.limit() + ", capacity:"
				+ buffer.capacity() + "]");;
				
		buffer.flip();
		System.out.println("flip buffer后：[position:" + buffer.position()
				+ ", limit:" + buffer.limit() + ", capacity:"
				+ buffer.capacity() + "]");
		
		System.out.print("循环获取buffer：");
		while (buffer.hasRemaining()) {
			System.out.print(buffer.get() + ",");
		}
		
		System.out.println();
		System.out.println("循环buffer后：[position:" + buffer.position()
				+ ", limit:" + buffer.limit() + ", capacity:"
				+ buffer.capacity() + "]");
		
		buffer.clear();
		System.out.println("clear buffer后：[position:" + buffer.position()
				+ ", limit:" + buffer.limit() + ", capacity:"
				+ buffer.capacity() + "]");
	}

}

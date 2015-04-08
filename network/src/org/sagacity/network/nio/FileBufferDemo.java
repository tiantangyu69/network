/**
 * 
 */
package org.sagacity.network.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lizhitao
 * 
 */
public class FileBufferDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = null;
		file = new File("e:" + File.separator + "test.txt");

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel channel = null;
		try {
			fis = new FileInputStream(file);
			channel = fis.getChannel();
			
			ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
			channel.read(buffer);
			buffer.flip();
			
			fos = new FileOutputStream(new File("e:" + File.separator + "copy.txt"));
			
			fos.getChannel().write(buffer);
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fos.close();
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}

/**
 * 
 */
package org.sagacity.network.socket.client.nonblocking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lizhitao
 * 
 */
public class NioNonBlockingEchoClient {
	private Selector selector;
	private SocketChannel socketChannel;
	private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
	private ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
	private String host = "localhost";
	public static final int PORT = 8888;

	public NioNonBlockingEchoClient() throws UnknownHostException, IOException {
		selector = Selector.open();

		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(host, PORT));
		socketChannel.configureBlocking(false);// 设置为非阻塞模式
		System.out.println("与服务器建立连接成功！");
	}

	public void receiveFromUser() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			String msg = null;
			while ((msg = reader.readLine()) != null) {
				synchronized (sendBuffer) {
					System.out.println("client说：" + msg);
					sendBuffer.put(encode(msg + "\r\n"));
				}
				if (msg.equals("bye")) {
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startEcho() throws IOException {
		socketChannel.register(selector, SelectionKey.OP_READ
				| SelectionKey.OP_WRITE);

		while (selector.select() > 0) {
			Set<SelectionKey> set = selector.selectedKeys();
			Iterator<SelectionKey> it = set.iterator();
			while (it.hasNext()) {
				SelectionKey key = null;
				try {
					key = it.next();
					it.remove();

					if (key.isReadable()) {
						handleRead(key);
					}
					if (key.isWritable()) {
						handleWrite(key);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					key.cancel();
					key.channel().close();
				}
			}
		}

	}

	public String decode(ByteBuffer buffer) {
		CharBuffer charBuffer = Charset.defaultCharset().decode(buffer);
		return charBuffer.toString();
	}

	public ByteBuffer encode(String str) {
		return Charset.defaultCharset().encode(str);
	}

	/**
	 * 处理写请求
	 * 
	 * @param key
	 * @throws IOException
	 */
	public void handleWrite(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		synchronized (sendBuffer) {
			sendBuffer.flip();
			channel.write(sendBuffer);
			sendBuffer.compact();
		}
	}

	/**
	 * 处理读请求
	 * 
	 * @param key
	 * @throws IOException
	 */
	public void handleRead(SelectionKey key) throws IOException {
		System.out.println("read");
		SocketChannel socketChannel = (SocketChannel) key.channel();
		socketChannel.read(receiveBuffer);
		receiveBuffer.flip();
		String receiveData = decode(receiveBuffer);

		if (receiveData.indexOf("\n") == -1) {
			return;
		}

		String outputData = receiveData.substring(0,
				receiveData.indexOf("\n") + 1);
		System.out.println(outputData);
		if (outputData.equals("echo:bye\r\n")) {
			key.cancel();
			socketChannel.close();
			System.out.println("关闭与服务器的连接");
			selector.close();
			System.exit(0);
		}

		ByteBuffer temp = encode(outputData);
		receiveBuffer.position(temp.limit());
		receiveBuffer.compact();
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException,
			IOException {
		final NioNonBlockingEchoClient client = new NioNonBlockingEchoClient();
		new Thread(new Runnable() {
			@Override
			public void run() {
				client.receiveFromUser();
			}
		}).start();
		client.startEcho();
	}

}

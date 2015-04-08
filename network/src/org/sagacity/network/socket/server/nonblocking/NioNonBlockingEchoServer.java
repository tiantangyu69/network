/**
 * 
 */
package org.sagacity.network.socket.server.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lizhitao
 * 
 */
public class NioNonBlockingEchoServer {
	public static final int PORT = 8888;
	private ServerSocketChannel serverSocketChannel;
	private Selector selector;

	public NioNonBlockingEchoServer() throws IOException {
		selector = Selector.open();

		serverSocketChannel = ServerSocketChannel.open();// 打开通道
		serverSocketChannel.socket().setReuseAddress(true);// 可以重用端口
		serverSocketChannel.configureBlocking(false);// 设置为非阻塞
		serverSocketChannel.socket().bind(new InetSocketAddress(PORT));// 绑定本地端口
		System.out.println("非阻塞服务器已启动，端口号为：" + PORT);
	}

	public void service() throws IOException {
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		while (selector.select() > 0) {
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> it = keys.iterator();
			while (it.hasNext()) {
				SelectionKey key = null;
				try {
					key = it.next();
					it.remove();
					if (key.isAcceptable()) {// 接收到连接事件
						handleAccept(key);// 处理连接请求
					}
					if (key.isReadable()) {
						handleRead(key);
					}
					if (key.isWritable()) {
						handleWrite(key);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (key != null) {
						key.cancel();
						key.channel().close();
					}
				} finally {
				}
			}
		}
	}

	/**
	 * 处理连接请求
	 * 
	 * @param key
	 * @throws IOException
	 */
	public void handleAccept(SelectionKey key) throws IOException {
		// 处理接受连接事件
		ServerSocketChannel channel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = channel.accept();
		System.out.println("接收到客户端连接：地址"
				+ socketChannel.socket().getInetAddress() + ",端口号"
				+ socketChannel.socket().getPort());
		socketChannel.configureBlocking(false);

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		socketChannel.register(selector, SelectionKey.OP_READ
				| SelectionKey.OP_WRITE, buffer);
	}
	
	/**
	 * 处理写请求
	 * @param key
	 * @throws IOException
	 */
	public void handleWrite(SelectionKey key) throws IOException{
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		SocketChannel socketChannel = (SocketChannel) key
				.channel();
		buffer.flip();
		String data = decode(buffer);
		if (data.indexOf("\r\n") == -1)
			return;
		String outputData = data.substring(0,
				data.indexOf("\n") + 1);
		ByteBuffer outputBuffer = encode("echo:" + outputData);
		while (outputBuffer.hasRemaining()) {
			socketChannel.write(outputBuffer);
		}

		ByteBuffer temp = encode(outputData);
		buffer.position(temp.limit());
		buffer.compact();

		if (outputData.equals("bye\r\n")) {
			key.cancel();
			socketChannel.close();
			System.out.println("关闭与客户端连接");
		}
	}
	
	/**
	 * 处理读请求
	 * @param key
	 * @throws IOException
	 */
	public void handleRead(SelectionKey key) throws IOException{
		// 获取与selectionKey关联的附件
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		SocketChannel socketChannel = (SocketChannel) key
				.channel();
		ByteBuffer readBuffer = ByteBuffer.allocate(32);
		socketChannel.read(readBuffer);
		readBuffer.flip();
		buffer.limit(buffer.capacity());
		buffer.put(readBuffer);
	}

	public String decode(ByteBuffer buffer) {
		CharBuffer charBuffer = Charset.defaultCharset().decode(buffer);
		return charBuffer.toString();
	}

	public ByteBuffer encode(String str) {
		return Charset.defaultCharset().encode(str);
	}

	public String echoStr(String msg) {
		return "echo: " + msg;
	}

	/**
	 * 使用channel读取缓冲区数据
	 * 
	 * @param socketChannel
	 * @return
	 * @throws IOException
	 */
	public String readLine(SocketChannel socketChannel) throws IOException {
		// 存放所有读到的数据，假设一行字符串对应的字节序列的长度小于1024
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		// 存放一次读到的数据，一次只读一个字节
		ByteBuffer tempBuffer = ByteBuffer.allocate(1);
		boolean isLine = false;// 表示是否读到了一行字符串
		boolean isEnd = false;// 表示是否到达了输入流的末尾
		String data = null;
		while (!isLine && !isEnd) {
			tempBuffer.clear();// 清空缓冲区
			// 在阻塞模式下，只有读到了一个字节或者读到输入流末尾才返回
			// 在非阻塞模式下有可能返回0
			int n = socketChannel.read(tempBuffer);
			if (n == -1) {// 读到了输入流的末尾
				isEnd = true;
				break;
			}
			if (n == 0) {
				continue;
			}

			tempBuffer.flip();// 把极限设为位置，把位置设为0
			buffer.put(tempBuffer);// 把tempBuffer中的数据复制到buffer中
			buffer.flip();
			Charset charset = Charset.defaultCharset();
			CharBuffer charBuffer = charset.decode(buffer);// 解码
			data = charBuffer.toString();
			if (data.indexOf("\r\n") != -1) {// 读到了一行数据
				isLine = true;
				data = data.substring(0, data.indexOf("\r\n"));
				break;
			}
			buffer.position(buffer.limit());// 把位置设为极限，为下次读数据做准备
			buffer.limit(buffer.capacity());// 把极限设为容量，为下次读数据做准备
		}

		// 如果读取到一行数据就返回这行数据不包括\r\n
		// 如果读取到输入流末尾就返回null
		return data;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NioNonBlockingEchoServer server;
		try {
			server = new NioNonBlockingEchoServer();
			server.service();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

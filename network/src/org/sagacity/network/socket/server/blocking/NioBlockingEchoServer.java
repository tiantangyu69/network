/**
 * 
 */
package org.sagacity.network.socket.server.blocking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lizhitao
 * 
 */
public class NioBlockingEchoServer {
	public static final int PORT = 8888;
	private ServerSocketChannel serverSocketChannel;
	private ExecutorService executorService;

	public NioBlockingEchoServer() throws IOException {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * 4);// 创建线程池

		serverSocketChannel = ServerSocketChannel.open();// 打开通道
		serverSocketChannel.socket().setReuseAddress(true);// 可以重用端口
		serverSocketChannel.socket().bind(new InetSocketAddress(PORT));// 绑定本地端口
		System.out.println("阻塞服务器已启动，端口号为：" + PORT);
	}

	public void service() {
		while (true) {
			SocketChannel socketChannel = null;
			try {
				socketChannel = serverSocketChannel.accept();// 使用ServerSocketChannel通道获取SocketChannel
				executorService.execute(new SocketChannelWork(socketChannel));// 处理socket
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class SocketChannelWork implements Runnable {
		private SocketChannel socketChannel;

		public SocketChannelWork(SocketChannel socketChannel) {
			this.socketChannel = socketChannel;
		}

		@Override
		public void run() {
			try {
				Socket socket = socketChannel.socket();
				System.out.println("接收到客户端连接：ip为" + socket.getInetAddress()
						+ ", 端口号为：" + socket.getPort());
				BufferedReader bufferedReader = getBufferedReader(socket);
				PrintWriter printWriter = getPrintWriter(socket);

				String str = null;
				while ((str = bufferedReader.readLine()) != null) {
					printWriter.println(echoStr(str));
					if (str.equals("bye")) {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	public BufferedReader getBufferedReader(Socket socket) throws IOException {
		return new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
	}

	public PrintWriter getPrintWriter(Socket socket) throws IOException {
		return new PrintWriter(socket.getOutputStream(), true);
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
		NioBlockingEchoServer server;
		try {
			server = new NioBlockingEchoServer();
			server.service();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

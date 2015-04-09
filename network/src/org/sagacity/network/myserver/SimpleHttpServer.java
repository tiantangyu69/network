/**
 * 
 */
package org.sagacity.network.myserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lizhitao
 * 
 */
public class SimpleHttpServer {
	private int port = 80;
	private ServerSocketChannel serverSocketChannel;
	private ExecutorService executorService;
	private static final int POOL_MULTIPLE = 4;

	public SimpleHttpServer() throws IOException {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_MULTIPLE);// 初始化线程池

		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("服务器已启动，监听端口为：" + port);
	}

	public void service() {
		while (true) {
			SocketChannel socketChannel = null;
			try {
				socketChannel = serverSocketChannel.accept();
				executorService.execute(new Handle(socketChannel));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class Handle implements Runnable {
		SocketChannel socketChannel;

		public Handle(SocketChannel socketChannel) {
			this.socketChannel = socketChannel;
		}

		@Override
		public void run() {
			handle(socketChannel);
		}

		public void handle(SocketChannel socketChannel) {
			try {
				Socket socket = socketChannel.socket();
				System.out.println("接收到客户端连接：" + socket.getInetAddress() + ":"
						+ socket.getPort());

				ByteBuffer buffer = ByteBuffer.allocate(1024);
				socketChannel.read(buffer);
				buffer.flip();

				String request = decode(buffer);
				System.out.println(request);

				StringBuffer sb = new StringBuffer();
				sb.append("HTTP/1.1 200 OK\r\n");
				sb.append("Content-Type: text/html; charset=utf-8\r\n\r\n");
				socketChannel.write(encode(sb.toString()));

				FileInputStream in = null;
				String firstLineOfRequest = request.substring(0,
						request.indexOf("\r\n"));
				if(firstLineOfRequest == null){
					return;
				}
				if (firstLineOfRequest.indexOf("login.html") != -1) {
					in = new FileInputStream("webapps/login.html");
				} else {
					in = new FileInputStream("webapps/hello.html");
				}

				FileChannel fileChannel = in.getChannel();
				fileChannel.transferTo(0, fileChannel.size(), socketChannel);
				
				fileChannel.close();
				in.close();
				socket.close();
				socketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public String decode(ByteBuffer buffer) {
			CharBuffer charBuffer = Charset.defaultCharset().decode(buffer);
			return charBuffer.toString();
		}

		public ByteBuffer encode(String str) {
			return Charset.defaultCharset().encode(str);
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new SimpleHttpServer().service();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

/**
 * 
 */
package org.sagacity.network.socket.client.blocking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

/**
 * @author lizhitao
 * 
 */
public class NioBlockingEchoClient {
	private SocketChannel socketChannel;
	private String host = "localhost";
	public static final int PORT = 8888;

	public NioBlockingEchoClient() throws UnknownHostException, IOException {
		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(host, PORT));
		System.out.println("与服务器建立连接成功！");
	}

	public void startEcho() throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			PrintWriter printWriter = getPrintWriter(socketChannel.socket());
			BufferedReader bufferedReader = getBufferedReader(socketChannel
					.socket());

			String msg = null;
			while ((msg = reader.readLine()) != null) {
				printWriter.println(msg);
				System.out.println("收到回应：" + bufferedReader.readLine());
				if (msg.equals("bye")) {
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			socketChannel.close();
		}
	}

	public BufferedReader getBufferedReader(Socket socket) throws IOException {
		return new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
	}

	public PrintWriter getPrintWriter(Socket socket) throws IOException {
		return new PrintWriter(socket.getOutputStream(), true);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException,
			IOException {
		NioBlockingEchoClient client = new NioBlockingEchoClient();
		client.startEcho();
	}

}

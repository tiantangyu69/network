/**
 * 
 */
package org.sagacity.network.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.sagacity.network.socket.server.blocking.BlockingEchoServer;

/**
 * @author lizhitao
 * 
 */
public class EchoClient {
	private Socket socket;
	private String host = "localhost";

	public EchoClient() throws UnknownHostException, IOException {
		socket = new Socket(host, BlockingEchoServer.PORT);
	}

	public void startEcho() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		PrintWriter printWriter = getPrintWriter(socket);
		BufferedReader bufferedReader = getBufferedReader(socket);
		
		String msg = null;
		while ((msg = reader.readLine()) != null) {
			try {
				printWriter.println(msg);
				System.out.println("收到回应：" + bufferedReader.readLine());
				if(msg.equals("bye")){
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
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

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException,
			IOException {
		EchoClient client = new EchoClient();
		client.startEcho();
	}

}

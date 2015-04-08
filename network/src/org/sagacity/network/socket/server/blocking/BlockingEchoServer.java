package org.sagacity.network.socket.server.blocking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockingEchoServer {
	public static final int PORT = 8888;
	private ServerSocket serverSocket;
	private ExecutorService executorService;

	public BlockingEchoServer() throws IOException {
		serverSocket = new ServerSocket(PORT);
		System.out.println("服务器启动,端口号：" + PORT);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * 4);
	}

	public void service() {
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				System.out.println("客户端已连接：" + socket.getPort());
				executorService.execute(new EchoWorker(socket));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class EchoWorker implements Runnable {
		private Socket socket;

		public EchoWorker(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				BufferedReader bufferedReader = getBufferedReader(socket);
				PrintWriter printWriter = getPrintWriter(socket);
				
				String str = null;
				while ((str = bufferedReader.readLine()) != null) {
					printWriter.println(echoStr(str));
					if(str.equals("bye")){
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
				}
			} catch (IOException e) {
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

	public String echoStr(String msg) {
		return "echo: " + msg;
	}

	public static void main(String[] args) throws IOException {
		new BlockingEchoServer().service();
	}

}

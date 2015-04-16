/**
 * 
 */
package org.sagacity.network.reflection.proxy.dynamic.simplermi;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author lizhitao
 * 
 */
public class SimpleServer {
	private int port = 8000;
	private ServerSocket serverSocket;
	private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;

	public SimpleServer() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("服务器已启动，监听端口为：" + port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void service() {
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				System.out.println("接收到客户端连接：" + socket.getInetAddress() + ":"
						+ socket.getPort());

				is = socket.getInputStream();
				ois = new ObjectInputStream(is);
				Call call = (Call) ois.readObject();
				Class<?> c = Class.forName(call.getClassName());
				EchoService service = (EchoService) c.newInstance();
				Method m = service.getClass().getMethod(call.getMethodName(),
						call.getParamstype());
				Object result = m.invoke(service, call.getParams());
				call.setResult(result);

				os = socket.getOutputStream();
				oos = new ObjectOutputStream(os);
				oos.writeObject(call);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					is.close();
					ois.close();
					os.close();
					oos.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SimpleServer().service();
	}

}

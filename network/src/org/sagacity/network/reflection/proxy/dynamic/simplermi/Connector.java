/**
 * 
 */
package org.sagacity.network.reflection.proxy.dynamic.simplermi;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author lizhitao
 * 
 */
public class Connector {
	private String host;
	private int port;
	private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;
	private Socket socket;

	public Connector(String host, int port) {
		this.host = host;
		this.port = port;
		connect();
	}

	public void send(Object obj) throws IOException {
		os = socket.getOutputStream();
		oos = new ObjectOutputStream(os);
		oos.writeObject(obj);
	}

	public Object receive() throws IOException, ClassNotFoundException {
		is = socket.getInputStream();
		ois = new ObjectInputStream(is);
		return ois.readObject();
	}

	public void connect() {
		connect(host, port);
	}

	public void connect(String host, int port) {
		try {
			socket = new Socket(host, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			os.close();
			is.close();
			oos.close();
			ois.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

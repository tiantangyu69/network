package org.sagacity.network.reflection.proxy.dynamic;

public class EchoServiceImpl implements EchoService {

	@Override
	public void echo() {
		System.out.println("EchoServiceImpl echo()");
	}

}

package org.sagacity.network.reflection.proxy.dynamic.simplermi;

public class EchoServiceImpl implements EchoService {

	@Override
	public String echo() {
		return "EchoServiceImpl echo()";
	}

}

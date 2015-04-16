/**
 * 
 */
package org.sagacity.network.reflection.proxy.dynamic.simplermi;

import java.io.Serializable;

/**
 * @author lizhitao
 * 
 */
public class Call implements Serializable{
	private static final long serialVersionUID = 1L;
	private String className;
	private String methodName;
	private Class[] paramstype;
	private Object[] params;
	private Object result;

	public Call(String className, String methodName, Class[] paramstype,
			Object[] params) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.paramstype = paramstype;
		this.params = params;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class[] getParamstype() {
		return paramstype;
	}

	public void setParamstype(Class[] paramstype) {
		this.paramstype = paramstype;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}

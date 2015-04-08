/**
 * 
 */
package org.sagacity.network.util;

/**
 * @author lizhitao
 * 
 */
public class MyThreadPoolTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MyThreadPool pool = new MyThreadPool(2);
		for (int i = 0; i < 5; i++) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println("开始执行任务："
							+ Thread.currentThread().getThreadGroup().getName()
							+ ":" + Thread.currentThread().getName());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("执行任务完成："
							+ Thread.currentThread().getThreadGroup().getName()
							+ ":" + Thread.currentThread().getName());
				}
			});
		}
	}

}

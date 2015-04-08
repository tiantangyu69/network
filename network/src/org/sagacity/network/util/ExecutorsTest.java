/**
 * 
 */
package org.sagacity.network.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author lizhitao
 *
 */
public class ExecutorsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(2);
		
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
		pool.shutdown();
		
		while(!pool.isTerminated()){
			try {
				System.out.println("==============");
				pool.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

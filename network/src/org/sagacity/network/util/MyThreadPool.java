/**
 * 
 */
package org.sagacity.network.util;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author lizhitao
 * 自定义线程池
 */
public class MyThreadPool extends ThreadGroup {
	private boolean isClosed = false; // 线程池是否关闭
	private Queue<Runnable> workQueue; // 工作队列
	private static int threadPoolId; // 线程池ID
	private int threadId; // 工作线程ID

	public MyThreadPool(int poolSize) {
		super("ThreadPool-" + (threadPoolId++));
		setDaemon(true);
		workQueue = new LinkedList<Runnable>();
		for (int i = 0; i < poolSize; i++) {
			new WorkThread().start();
		}
	}

	public synchronized void execute(Runnable task) {
		if (isClosed) {
			throw new IllegalStateException();
		}
		if (null != task) {
			workQueue.add(task);
			notify();
		}
	}

	protected synchronized Runnable getTask() throws InterruptedException {
		while (workQueue.size() == 0) {
			if (isClosed) {
				return null;
			}
			wait();
		}
		return workQueue.poll();
	}

	public synchronized void close() {
		if (!isClosed) {
			isClosed = true;
			workQueue.clear();
			interrupt();
		}
	}

	public synchronized void join() {
		synchronized (this) {
			isClosed = true;
			notifyAll();
		}

		Thread[] threads = new Thread[activeCount()];
		int count = enumerate(threads);

		for (int i = 0; i < count; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class WorkThread extends Thread {
		public WorkThread() {
			super(MyThreadPool.this, "WorkThread-" + (threadId++));
		}

		public void run() {
			while (!isInterrupted()) {// 判断线程是否被中断
				Runnable task = null;
				try {
					task = getTask();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (task == null)
					return;

				task.run();
			}
		}
	}
}
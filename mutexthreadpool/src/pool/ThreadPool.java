package pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {
	
	private List<Worker> workers;
	
	private List<Job> jobs = new ArrayList<>();
	
	private final Lock lock = new ReentrantLock();
	
	private final Condition notEmpty = lock.newCondition();
	
	public ThreadPool(int size) {
		workers = new ArrayList<>(size);
		for(int i = 0; i < size; i++) {
			Worker w = new Worker(jobs);
			workers.add(w);
			w.start();
		}
	}
	
	public void execute(Job job) {
		lock.lock();
		try {
			jobs.add(job);
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}
	
	private class Worker extends Thread{
		
		private List<Job> jobs;

		public Worker(List<Job> jobs) {
			this.jobs = jobs;
		}
		
		@Override
		public void run() {
			
			while (true) {
	            Job job = null;
	            lock.lock();
	            try {
	                while (jobs.isEmpty()) {
	                	try {
							notEmpty.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	                }
	                job = jobs.remove(0);
		            if (job != null) {
		                job.run();
		            }
	            } finally {
	            	lock.unlock();
	            }
	        }
		}
	}
}

package pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {
	
	private List<Worker> workers;
	
	private BlockingQueue<Job> jobs = new ArrayBlockingQueue<>(8);
	
	public ThreadPool(int size) {
		workers = new ArrayList<>(size);
		for(int i = 0; i < size; i++) {
			Worker w = new Worker(jobs);
			workers.add(w);
			w.start();
		}
	}
	
	public void execute(Job job) {
		try {
			jobs.put(job);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
}

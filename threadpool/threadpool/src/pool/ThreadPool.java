package pool;

import java.util.ArrayList;
import java.util.List;

public class ThreadPool {
	
	private List<Worker> workers;
	
	private List<Job> jobs = new ArrayList<>();
	
	public ThreadPool(int size) {
		workers = new ArrayList<>(size);
		for(int i = 0; i < size; i++) {
			Worker w = new Worker(jobs);
			workers.add(w);
			w.start();
		}
	}
	
	public void execute(Job job) {
		synchronized (jobs) {
			jobs.add(job);
			jobs.notify();
		}
	}
	
	
}

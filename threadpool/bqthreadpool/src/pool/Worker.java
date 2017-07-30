package pool;

import java.util.concurrent.BlockingQueue;

public class Worker extends Thread{
	
	private BlockingQueue<Job> jobs;

	public Worker(BlockingQueue<Job> jobs) {
		this.jobs = jobs;
	}
	
	@Override
	public void run() {
		
		while (true) {
            Job job = null;
			try {
				job = jobs.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
            
            if (job != null) {
                job.run();
            }
        }
	}
}

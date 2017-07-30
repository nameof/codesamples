package pool;

import java.util.List;

public class Worker extends Thread{
	
	private List<Job> jobs;

	public Worker(List<Job> jobs) {
		this.jobs = jobs;
	}
	
	@Override
	public void run() {
		
		while (true) {
            Job job = null;
            //线程的等待/通知机制
            synchronized (jobs) {
                while (jobs.isEmpty()) {
                    try {
                        jobs.wait();//线程等待唤醒
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                // 取出一个job
                job = jobs.remove(0);
            }
            //执行job
            if (job != null) {
                job.run();
            }
        }
	}
}

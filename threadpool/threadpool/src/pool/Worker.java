package pool;

import java.util.List;

public class Worker extends Thread {

    private List<Job> jobs;

    public Worker(List<Job> jobs) {
        this.jobs = jobs;
    }

    @Override
    public void run() {

        while (true) {
            Job job = null;
            synchronized (jobs) {
                while (jobs.isEmpty()) {
                    try {
                        jobs.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                job = jobs.remove(0);
            }
            if (job != null) {
                job.run();
            }
        }
    }
}

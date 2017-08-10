package concurrentapi;

import java.util.concurrent.CountDownLatch;


public class CountDownLatchTest {
	
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch begincdl = new CountDownLatch(1);
		CountDownLatch endcdl = new CountDownLatch(5);

		for (int i = 0; i < 5; i++) {
			WorkThead wt = new WorkThead("" + i, begincdl, endcdl);
			wt.start();
		}
		new DispatchThread(begincdl, endcdl).start();
	}
}

class WorkThead extends Thread {

	private CountDownLatch begincdl;

	private CountDownLatch endcdl;

	private String jobname;

	public WorkThead(String jobname, CountDownLatch begincdl,
			CountDownLatch endcdl) {
		this.begincdl = begincdl;
		this.endcdl = endcdl;
		this.jobname = jobname;
	}

	@Override
	public void run() {
		try {
			begincdl.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(jobname + "--execute!");
		endcdl.countDown();
	}
}

class DispatchThread extends Thread {

	private CountDownLatch begincdl;

	private CountDownLatch endcdl;

	public DispatchThread(CountDownLatch begincdl, CountDownLatch endcdl) {
		this.begincdl = begincdl;
		this.endcdl = endcdl;
	}

	@Override
	public void run() {
		System.out.println("开始执行：");
		begincdl.countDown();
		try {
			endcdl.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("执行完毕！");
	}
}
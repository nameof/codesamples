package concurrentapi;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {
	
	public static void main(String[] args) {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Dispatcher());
		
		for (int i = 0; i < 4; i++) {
			new Thread(new Task(i, cyclicBarrier)).start();
		}
	}
}

class Task implements Runnable {

	private CyclicBarrier cyclicBarrier;
	
	private int id;

	public Task(int id, CyclicBarrier cyclicBarrier) {
		this.cyclicBarrier = cyclicBarrier;
		this.id = id;
	}

	@Override
	public void run() {
		try {
			System.out.println("任务" + id + "正在加载资源...");
			cyclicBarrier.await();
			System.out.println("任务" + id + "--execute!");
			cyclicBarrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

}

class Dispatcher implements Runnable {
	
	private boolean isover = false;
	
	@Override
	public void run() {
		if(!isover) {
			System.out.println("全部资源加载完毕！开始执行");
		} else {
			System.out.println("执行完毕！");
		}
		isover = !isover;
	}
}
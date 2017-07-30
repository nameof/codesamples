package pool;

public class Job implements Runnable{

	private String name;
	
	public Job(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getId() + "：do " + name);
	}

}

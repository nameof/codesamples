package pool;

public class Run {
	public static void main(String[] args) {
		ThreadPool pool = new ThreadPool(5);
		for(int i = 1000; i > 0; i--) {
			pool.execute(new Job("job" + i));
		}
	}
}

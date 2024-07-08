import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MergeThread extends Thread{
	private static MergeThread instance = new MergeThread();
	static {instance.start();}
	public static MergeThread getInstance() {
		return instance;
	}
	
	private List<BlockingQueue<Integer>> inputQueues;
	
	private MergeThread() {
		inputQueues = List.of(new LinkedBlockingQueue<Integer>(),new LinkedBlockingQueue<Integer>(),new LinkedBlockingQueue<Integer>());
	}
	public synchronized void addToQueue(int index, int value) {
		inputQueues.get(index).add(value);
	}
	
	@Override
	public void run() {
		while (true) {
			int[] values = new int[3];
			for (int i = 0; i < 3; i++)
				try {
					values[i] = inputQueues.get(i).take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			Arrays.sort(values);
			for (int i : values)
				CopyThread.getInstance().addToQueue(i);
			
		}
	}
}

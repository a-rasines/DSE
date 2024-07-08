import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class QueuedThread extends Thread {
	protected BlockingQueue<Integer> inputQueue;
	public synchronized void addToQueue(int num) {
		inputQueue.add(num);
	}
	protected QueuedThread() {
		inputQueue = new LinkedBlockingQueue<>();
	}
	
	public abstract void run();
}
